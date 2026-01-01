/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Stock;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.DefaultComboBoxModel;

public class AddStockDFrame extends javax.swing.JDialog {

    private final StockPanel stockPanel;

    public AddStockDFrame(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.stockPanel = null;
        initComponents();
    }

    public AddStockDFrame(java.awt.Frame parent, boolean modal, StockPanel stockPanel) {
        super(parent, modal);
        this.stockPanel = stockPanel;
        initComponents();

        // ✅ Load colours from database into combo box
        loadPreviousColours();

        // ✅ Disable fields that should be read-only
        AQty.setEnabled(false);      // Available Qty is auto-calculated
        jTextField2.setEnabled(false); // Previous Stock Qty is auto-filled

        // ✅ Create a document listener to update Available Qty
        javax.swing.event.DocumentListener updateAvailableListener = new javax.swing.event.DocumentListener() {
            private void updateAvailable() {
                try {
                    String prevStockText = jTextField2.getText().trim();
                    String receivedQtyText = TIssued.getText().trim();
                    String stockQtyText = SQty.getText().trim();

                    int previousStock = prevStockText.isEmpty() ? 0 : Integer.parseInt(prevStockText);
                    int receivedQty = receivedQtyText.isEmpty() ? 0 : Integer.parseInt(receivedQtyText);
                    int stockQty = stockQtyText.isEmpty() ? 0 : Integer.parseInt(stockQtyText);

                    // ✅ හරි Logic
                    int available;

                    if (stockQty == 0) {
                        // Stock Qty හිස් නම්: Available = Previous + Received
                        available = previousStock + receivedQty;
                    } else {
                        // Stock Qty තියෙනවා නම්: Available = Stock - (Previous + Received)
                        available = stockQty - (previousStock + receivedQty);
                    }

                    if (available < 0) {
                        available = 0;
                    }

                    AQty.setText(String.valueOf(available));

                } catch (NumberFormatException e) {
                    AQty.setText("0");
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateAvailable();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateAvailable();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateAvailable();
            }
        };

        // ✅ Colour field එකේ type කරද්දී combo box එක reset කරන්න
        jTextField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void resetComboBox() {
                String colourText = jTextField1.getText().trim();
                String selectedColour = (String) jComboBox1.getSelectedItem();

                // User type කරනවා නම් සහ combo box එකේ colour එක selected වෙලා තියෙනවා නම්
                if (!colourText.isEmpty() && selectedColour != null
                        && !selectedColour.equals("-- Select Previous Colour --")
                        && !colourText.equals(selectedColour)) {

                    // Combo box එක reset කරන්න (listener trigger වෙන්නේ නැතිව)
                    jComboBox1.removeActionListener(jComboBox1.getActionListeners()[0]);
                    jComboBox1.setSelectedIndex(0);
                    jComboBox1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            loadPreviousStockDetails();
                        }
                    });
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                resetComboBox();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                resetComboBox();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                resetComboBox();
            }
        });

        // ✅ Add listener to all three fields
        jTextField2.getDocument().addDocumentListener(updateAvailableListener);
        TIssued.getDocument().addDocumentListener(updateAvailableListener);
        SQty.getDocument().addDocumentListener(updateAvailableListener);

        // ✅ Add listener to combo box to load previous stock details
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadPreviousStockDetails();
            }
        });
    }

// ✅ Load colours from database
    private void loadPreviousColours() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT DISTINCT colour FROM stock WHERE status = 'active' ORDER BY colour";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("-- Select Previous Colour --");

            while (rs.next()) {
                String colour = rs.getString("colour");
                model.addElement(colour);
            }

            jComboBox1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading colours: " + e.getMessage());
        }
    }

// ✅ Load previous stock details when colour is selected
    private void loadPreviousStockDetails() {
        String selectedColour = (String) jComboBox1.getSelectedItem();

        if (selectedColour == null || selectedColour.equals("-- Select Previous Colour --")) {
            jTextField2.setText("0");
            Materials.setText("");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // ✅ Get the latest stock record for this colour using id column
            // Table structure අනුව: id INT PRIMARY KEY AUTO_INCREMENT
            String query = "SELECT available_qty, material FROM stock "
                    + "WHERE colour = ? AND status = 'active' "
                    + "ORDER BY id DESC LIMIT 1";  // ✅ Order by id (newest first)

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, selectedColour);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String availableQty = rs.getString("available_qty");
                String material = rs.getString("material");

                // ✅ Previous Stock Qty ලෙස Available Qty එක set කරන්න
                jTextField2.setText(availableQty != null ? availableQty : "0");

                // ✅ Material එකත් fill කරන්න
                Materials.setText(material != null ? material : "");

                // ✅ Colour field එකත් fill කරන්න
                jTextField1.setText(selectedColour);

                // ✅ Debug message (optional)
                System.out.println("Loaded previous stock for colour: " + selectedColour
                        + ", Available Qty: " + availableQty
                        + ", Material: " + material);
            } else {
                jTextField2.setText("0");
                Materials.setText("");
                System.out.println("No previous stock found for colour: " + selectedColour);
            }

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading previous stock details: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        SQty = new javax.swing.JTextField();
        Materials = new javax.swing.JTextField();
        AQty = new javax.swing.JTextField();
        UPrice = new javax.swing.JTextField();
        TIssued = new javax.swing.JTextField();
        RDate = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Stock");

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Add Stock");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Materials");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Stock Qty");

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Recieved Date");

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Previous Stock Qty");

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Recieved Qty");

        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel8.setText("Available Qty");

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel9.setText("Unit Price");

        SQty.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        SQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SQtyActionPerformed(evt);
            }
        });

        Materials.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        Materials.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MaterialsActionPerformed(evt);
            }
        });

        AQty.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        UPrice.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        TIssued.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        TIssued.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TIssuedActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Add Stock");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel10.setText("Colour");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jComboBox1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Previous Colour");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1)
                                .addGap(40, 40, 40)
                                .addComponent(jButton2))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(676, 676, 676))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel5))
                                        .addGap(156, 156, 156)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                            .addComponent(Materials, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                            .addComponent(AQty, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                            .addComponent(RDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(107, 107, 107)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel6))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(SQty, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                        .addComponent(TIssued, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                        .addComponent(UPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                        .addComponent(jTextField2))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(27, 27, 27))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(SQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(Materials, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(TIssued, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(AQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(UPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addGap(16, 16, 16))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(RDate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void MaterialsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MaterialsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MaterialsActionPerformed

    private void addStockToDatabase() {
        String colour = jTextField1.getText().trim();
        String material = Materials.getText().trim();
        String stockQty = SQty.getText().trim();
        String availableQty = AQty.getText().trim();
        String receivedQty = TIssued.getText().trim();
        String unitPrice = UPrice.getText().trim();
        String previousStockQty = jTextField2.getText().trim();

        java.util.Date receivedDateUtil = RDate.getDate();

        // ✅ Validate other required fields
        if (colour.isEmpty() || material.isEmpty() || unitPrice.isEmpty() || receivedDateUtil == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill all required fields!");
            return;
        }

        // ✅ Validate unit price is a valid positive number
        try {
            double unitPriceValue = Double.parseDouble(unitPrice);
            if (unitPriceValue <= 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Unit Price must be greater than 0!");
                UPrice.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Unit Price must be a valid number!");
            UPrice.requestFocus();
            return;
        }

        // ✅ Validate Stock Qty if provided (not required, but if provided must be valid)
        if (!stockQty.isEmpty()) {
            try {
                int stockQtyValue = Integer.parseInt(stockQty);
                if (stockQtyValue < 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Stock Quantity cannot be negative!");
                    SQty.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Stock Quantity must be a valid number!");
                SQty.requestFocus();
                return;
            }
        }

        // ✅ Validate Received Qty if provided
        if (!receivedQty.isEmpty()) {
            try {
                int receivedQtyValue = Integer.parseInt(receivedQty);
                if (receivedQtyValue < 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Received Quantity cannot be negative!");
                    TIssued.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Received Quantity must be a valid number!");
                TIssued.requestFocus();
                return;
            }
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO stock (colour, stock_qty, previous_stock_qty, material, received_date, recieved_qty, available_qty, unit_price, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, colour);

            // ✅ Stock Qty - if empty, insert as 0
            ps.setString(2, stockQty.isEmpty() ? "0" : stockQty);

            ps.setString(3, previousStockQty.isEmpty() ? "0" : previousStockQty);
            ps.setString(4, material);
            ps.setString(5, new java.text.SimpleDateFormat("yyyy-MM-dd").format(receivedDateUtil));
            ps.setString(6, receivedQty.isEmpty() ? "0" : receivedQty);
            ps.setString(7, availableQty.isEmpty() ? "0" : availableQty);
            ps.setString(8, unitPrice);
            ps.setString(9, "active");

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "✅ Stock added successfully!");
                this.dispose();

                if (stockPanel != null) {
                    stockPanel.loadStockData();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
        }
    }


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addStockToDatabase();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void TIssuedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TIssuedActionPerformed

    }//GEN-LAST:event_TIssuedActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void SQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SQtyActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(AddStockDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddStockDFrame dialog = new AddStockDFrame(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AQty;
    private javax.swing.JTextField Materials;
    private com.toedter.calendar.JDateChooser RDate;
    private javax.swing.JTextField SQty;
    private javax.swing.JTextField TIssued;
    private javax.swing.JTextField UPrice;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
