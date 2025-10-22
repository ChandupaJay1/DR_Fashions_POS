/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.ViewBuyerAccesories;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AddBuyerAccesoriesDFrame extends javax.swing.JDialog {

    private ViewBuyerAccesoriesPanel parentPanel;

    public AddBuyerAccesoriesDFrame(ViewBuyerAccesoriesPanel parent) {
        initComponents();
        this.parentPanel = parent;
        setLocationRelativeTo(null);

        // Disable editing for buyer name and available qty
        jTextField1.setEditable(false); // Buyer Name
        jTextField8.setEditable(false); // Available Qty

        // Load types into combo box
        loadTypes();

        // Add listeners to calculate available qty automatically
        jTextField5.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateAvailableQty();
            }

            public void removeUpdate(DocumentEvent e) {
                updateAvailableQty();
            }

            public void changedUpdate(DocumentEvent e) {
                updateAvailableQty();
            }
        });

        jTextField7.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateAvailableQty();
            }

            public void removeUpdate(DocumentEvent e) {
                updateAvailableQty();
            }

            public void changedUpdate(DocumentEvent e) {
                updateAvailableQty();
            }
        });
    }

    public void setBuyerName(String buyerName) {
        jTextField1.setText(buyerName);
    }

    /**
     * Auto-calculate Available Qty = Stock Qty - Total Issued
     */
    private void updateAvailableQty() {
        try {
            int stockQty = jTextField5.getText().isEmpty() ? 0 : Integer.parseInt(jTextField5.getText());
            int totalIssued = jTextField7.getText().isEmpty() ? 0 : Integer.parseInt(jTextField7.getText());
            int availableQty = stockQty - totalIssued;
            if (availableQty < 0) {
                availableQty = 0; // prevent negative
            }
            jTextField8.setText(String.valueOf(availableQty));
        } catch (NumberFormatException e) {
            jTextField8.setText("0");
        }
    }

    /**
     * Load types from database into combo box
     */
    private void loadTypes() {
        try {
            jComboBox1.removeAllItems();
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT type_name FROM type ORDER BY type_name");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                jComboBox1.addItem(rs.getString("type_name"));
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading types: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField9 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Add Buyer Accesory");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Buyer Name");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Order No");

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Colour");

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Size");

        jTextField4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Stock Qty");

        jTextField5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("UOM");

        jTextField6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel8.setText("Received Date");

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel9.setText("Issued Date");

        jLabel10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel10.setText("Total Issued");

        jTextField7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel11.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel11.setText("Available Qty");

        jTextField8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel12.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel12.setText("Unit Price");

        jComboBox1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextField9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel13.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel13.setText("Type");

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Add Buyer Accesory");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton2)
                            .addGap(66, 66, 66)
                            .addComponent(jButton1))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel4)
                                .addComponent(jLabel6)
                                .addComponent(jLabel8)
                                .addComponent(jLabel10)
                                .addComponent(jLabel12))
                            .addGap(175, 175, 175)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField3)
                                .addComponent(jTextField1)
                                .addComponent(jTextField5)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                                .addComponent(jTextField7)
                                .addComponent(jTextField9))
                            .addGap(98, 98, 98)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel5)
                                .addComponent(jLabel7)
                                .addComponent(jLabel9)
                                .addComponent(jLabel11)
                                .addComponent(jLabel13))
                            .addGap(104, 104, 104)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel9)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(91, 91, 91)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Get values from form
        String buyerName = jTextField1.getText().trim();
        String orderNo = jTextField2.getText().trim();
        String colour = jTextField3.getText().trim();
        String size = jTextField4.getText().trim();
        String stockQtyStr = jTextField5.getText().trim();
        String uom = jTextField6.getText().trim();
        String totalIssuedStr = jTextField7.getText().trim();
        String availableQtyStr = jTextField8.getText().trim();
        String unitPriceStr = jTextField9.getText().trim();
        String typeName = (String) jComboBox1.getSelectedItem();

        // Validation
        if (buyerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter buyer name.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (orderNo.isEmpty() || colour.isEmpty() || size.isEmpty()
                || stockQtyStr.isEmpty() || uom.isEmpty() || unitPriceStr.isEmpty() || typeName == null) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (jDateChooser1.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select received date.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int stockQty = Integer.parseInt(stockQtyStr);
            java.sql.Date receivedDate = new java.sql.Date(jDateChooser1.getDate().getTime());
            java.sql.Date issuedDate = (jDateChooser2.getDate() != null)
                    ? new java.sql.Date(jDateChooser2.getDate().getTime())
                    : null;
            int totalIssued = totalIssuedStr.isEmpty() ? 0 : Integer.parseInt(totalIssuedStr);
            int availableQty = availableQtyStr.isEmpty() ? 0 : Integer.parseInt(availableQtyStr);

            // Remove Rs. prefix if exists before parsing
            unitPriceStr = unitPriceStr.replace("Rs.", "").replace(",", "").trim();
            double unitPrice = Double.parseDouble(unitPriceStr);

            try (Connection conn = DatabaseConnection.getConnection()) {
                // Find buyer ID from registration_buyer table
                PreparedStatement pstBuyer = conn.prepareStatement("SELECT id FROM registration_buyer WHERE name = ?");
                pstBuyer.setString(1, buyerName);
                ResultSet rsBuyer = pstBuyer.executeQuery();

                int buyerId = 0;
                if (rsBuyer.next()) {
                    buyerId = rsBuyer.getInt("id");
                } else {
                    JOptionPane.showMessageDialog(this, "Buyer not found in database!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                rsBuyer.close();
                pstBuyer.close();

                // Find type ID from type table
                PreparedStatement pstType = conn.prepareStatement("SELECT type_id FROM type WHERE type_name = ?");
                pstType.setString(1, typeName);
                ResultSet rsType = pstType.executeQuery();

                int typeId = 0;
                if (rsType.next()) {
                    typeId = rsType.getInt("type_id");  // instead of "id"
                } else {
                    JOptionPane.showMessageDialog(this, "Type not found in database!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                rsType.close();
                pstType.close();

                // ✅ UPDATED: Insert into baccesories table with status = 'active'
                String sql = "INSERT INTO baccesories (registration_buyer_id, order_no, colour_name, size, "
                        + "stock_qty, uom, received_date, issued_date, total_issued, available_qty, "
                        + "unit_price, type_type_id, status, last_modified) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'active', NOW())";

                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, buyerId);
                pst.setString(2, orderNo);
                pst.setString(3, colour);
                pst.setString(4, size);
                pst.setInt(5, stockQty);
                pst.setString(6, uom);
                pst.setDate(7, receivedDate);
                if (issuedDate != null) {
                    pst.setDate(8, issuedDate);
                } else {
                    pst.setNull(8, java.sql.Types.DATE);
                }
                pst.setInt(9, totalIssued);
                pst.setInt(10, availableQty);
                pst.setDouble(11, unitPrice);
                pst.setInt(12, typeId);

                int rows = pst.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Buyer accessory added successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    if (parentPanel != null) {
                        parentPanel.refreshTable();
                    }
                    this.dispose();
                }

                pst.close();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error adding buyer accessory: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for quantity and price fields.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddBuyerAccesoriesDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddBuyerAccesoriesDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddBuyerAccesoriesDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddBuyerAccesoriesDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog - For testing only */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Create a dummy ViewBuyerAccesoriesPanel for testing
                ViewBuyerAccesoriesPanel dummyPanel = new ViewBuyerAccesoriesPanel();
                AddBuyerAccesoriesDFrame dialog = new AddBuyerAccesoriesDFrame(dummyPanel);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
