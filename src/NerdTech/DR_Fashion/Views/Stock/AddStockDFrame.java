/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Stock;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddStockDFrame extends javax.swing.JDialog {

    private final StockPanel stockPanel;

    // 👇 Add this constructor
    public AddStockDFrame(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.stockPanel = null;
        initComponents();
    }

    public AddStockDFrame(java.awt.Frame parent, boolean modal, StockPanel stockPanel) {
        super(parent, modal);
        this.stockPanel = stockPanel;
        initComponents();

        // ✅ Only disable Available Qty field
        AQty.setEnabled(false);

        // ✅ Auto-update Available Qty when Total Issued changes
        TIssued.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateAvailable() {
                try {
                    int stockQty = Integer.parseInt(SQty.getText().trim());
                    int totalIssued = Integer.parseInt(TIssued.getText().trim());
                    int available = stockQty - totalIssued;
                    if (available < 0) {
                        available = 0;
                    }
                    AQty.setText(String.valueOf(available));
                } catch (NumberFormatException e) {
                    AQty.setText("");
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
        });
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
        IDate = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

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
        jLabel6.setText("Issued Date");

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Total  Issued");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8))
                                .addGap(156, 156, 156)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Materials, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(IDate, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(AQty, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(107, 107, 107)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(RDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(SQty, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TIssued, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(UPrice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1)
                                .addGap(40, 40, 40)
                                .addComponent(jButton2)))
                        .addGap(27, 27, 27))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(SQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(Materials, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(RDate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel6))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(TIssued, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(IDate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void MaterialsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MaterialsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MaterialsActionPerformed

    private void addStockToDatabase() {
        String colour = jTextField1.getText().trim(); // 🟢 New Colour field
        String material = Materials.getText().trim();
        String stockQty = SQty.getText().trim();
        String availableQty = AQty.getText().trim();
        String totalIssued = TIssued.getText().trim();
        String unitPrice = UPrice.getText().trim();

        java.util.Date receivedDateUtil = RDate.getDate();
        java.util.Date issuedDateUtil = IDate.getDate();

        // ✅ Basic validation
        if (colour.isEmpty() || material.isEmpty() || stockQty.isEmpty() || unitPrice.isEmpty() || receivedDateUtil == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill all required fields (Name, Colour, Material, Stock Qty, Unit Price, Received Date).");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // 🟢 Updated query with Colour + Status
            String query = "INSERT INTO stock (colour, stock_qty, material, received_date, issued_date, total_issued, available_qty, unit_price, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, colour); // 🟢 Add colour
            ps.setInt(2, Integer.parseInt(stockQty));
            ps.setString(3, material);
            ps.setDate(4, new java.sql.Date(receivedDateUtil.getTime()));

            if (issuedDateUtil != null) {
                ps.setDate(5, new java.sql.Date(issuedDateUtil.getTime()));
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }

            ps.setInt(6, totalIssued.isEmpty() ? 0 : Integer.parseInt(totalIssued));
            ps.setInt(7, availableQty.isEmpty() ? Integer.parseInt(stockQty) : Integer.parseInt(availableQty));
            ps.setDouble(8, Double.parseDouble(unitPrice));
            ps.setString(9, "active"); // 🟢 Always insert status as active

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "✅ Stock added successfully!");
                this.dispose(); // Close the dialog

                // Reload the stock panel table data
                if (stockPanel != null) {
                    stockPanel.loadStockData();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "❌ Error while adding stock: " + e.getMessage());
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
            java.util.logging.Logger.getLogger(AddStockDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddStockDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddStockDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddStockDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
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
    private com.toedter.calendar.JDateChooser IDate;
    private javax.swing.JTextField Materials;
    private com.toedter.calendar.JDateChooser RDate;
    private javax.swing.JTextField SQty;
    private javax.swing.JTextField TIssued;
    private javax.swing.JTextField UPrice;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
