/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.ViewBuyerStock;

import javax.swing.JOptionPane;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class UpdateBuyerStockDFrame extends javax.swing.JDialog {

    private ViewBuyerStock parentPanel;
    private int stockId;

    public UpdateBuyerStockDFrame() {
        super((java.awt.Frame) null, true);
        initComponents();
        setLocationRelativeTo(null);
    }

    // ✅ FIXED: Added workOrderNo parameter
    public UpdateBuyerStockDFrame(ViewBuyerStock parent, int stockId, String buyerName, String colour,
            int stockQty, String brandName, double size, String material, java.sql.Date receivedDate,
            java.sql.Date issuedDate, int totalIssued, int availableQty, double unitPrice, String workOrderNo) {

        initComponents();
        this.parentPanel = parent;
        this.stockId = stockId;
        setLocationRelativeTo(null);

        // ✅ Load all fields including work order no
        jTextField1.setText(buyerName);                         // Buyer Name
        jTextField2.setText(colour);                            // Colour
        jTextField3.setText(String.valueOf(availableQty));      // Stock Qty (showing available)
        jTextField4.setText(material);                          // Material
        jTextField8.setText(brandName);                         // Brand Name
        jTextField10.setText(String.valueOf(size));             // Size
        jTextField9.setText(workOrderNo != null ? workOrderNo : ""); // ⭐ Work Order No
        jDateChooser1.setDate(receivedDate);                    // Received Date
        jDateChooser2.setDate(issuedDate);                      // Issued Date
        jTextField5.setText(String.valueOf(totalIssued));       // Total Issued
        jTextField6.setText(String.valueOf(availableQty));      // Available Qty
        jTextField7.setText(String.format("%.2f", unitPrice));  // Unit Price

        // Set read-only fields
        jTextField1.setEditable(false);  // Buyer Name
        jTextField3.setEditable(false);  // Stock Qty
        jTextField6.setEditable(false);  // Available Qty (auto-calculated)
        jDateChooser1.setEnabled(false); // Received Date

        // Add listener to update available qty dynamically
        javax.swing.event.DocumentListener listener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateAvailableQty();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateAvailableQty();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateAvailableQty();
            }
        };

        jTextField3.getDocument().addDocumentListener(listener);
        jTextField5.getDocument().addDocumentListener(listener);
    }

    private void updateAvailableQty() {
        try {
            int stockQty = jTextField3.getText().isEmpty() ? 0 : Integer.parseInt(jTextField3.getText());
            int totalIssued = jTextField5.getText().isEmpty() ? 0 : Integer.parseInt(jTextField5.getText());
            int availableQty = Math.max(0, stockQty - totalIssued);
            jTextField6.setText(String.valueOf(availableQty));
        } catch (Exception e) {
            jTextField6.setText("0");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Update Buyer Stock");

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Update Buyer Stock");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Buyer Name");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Colour");

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Stock Qty");

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Material");

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Recieved Date");

        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel8.setText("Issued Date");

        jTextField6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Total Issued");

        jTextField4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel9.setText("Available Qty");

        jTextField5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel10.setText("Unit Price");

        jTextField7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel11.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel11.setText("Brand Name");

        jTextField8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel13.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel13.setText("Size");

        jTextField10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel12.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel12.setText("Work Order No :");

        jTextField9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(458, 458, 458)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(843, 843, 843)
                                .addComponent(jButton2)
                                .addGap(69, 69, 69)
                                .addComponent(jButton1)))
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel5)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(53, 53, 53)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                                .addComponent(jTextField6)))
                        .addGap(97, 97, 97))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(94, 94, 94)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel11)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel4)
                                .addComponent(jLabel7)
                                .addComponent(jLabel6)
                                .addComponent(jLabel10))
                            .addGap(90, 90, 90)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField8)
                                .addComponent(jTextField1)
                                .addComponent(jTextField3)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                .addComponent(jTextField4)
                                .addComponent(jTextField7))
                            .addGap(89, 89, 89)
                            .addComponent(jLabel3)))
                    .addGap(132, 132, 132)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(95, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel12)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(17, 17, 17))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(90, 90, 90)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel7)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(91, 91, 91)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void updateBuyerStock() {
        try {
            int stockQty = Integer.parseInt(jTextField3.getText());
            int totalIssued = Integer.parseInt(jTextField5.getText());
            int availableQty = Math.max(0, stockQty - totalIssued);

            double unitPrice = Double.parseDouble(jTextField7.getText().replace("Rs.", "").trim());
            java.sql.Date receivedDate = new java.sql.Date(jDateChooser1.getDate().getTime());
            java.sql.Date issuedDate = (jDateChooser2.getDate() != null)
                    ? new java.sql.Date(jDateChooser2.getDate().getTime()) : null;

            String colour = jTextField2.getText();
            String material = jTextField4.getText();
            String brandName = jTextField8.getText();
            double size = Double.parseDouble(jTextField10.getText());
            String workOrderNo = jTextField9.getText().trim(); // ⭐ Work Order No

            Connection conn = DatabaseConnection.getConnection();

            // ✅ Updated SQL to include work_order_no
            String sql = "UPDATE bstock SET colour=?, stock_qty=?, brand_name=?, size=?, material=?, "
                    + "received_date=?, issued_date=?, total_issued=?, available_qty=?, unit_price=?, "
                    + "work_order_no=?, last_modified=NOW() WHERE id=?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, colour);
            pst.setInt(2, stockQty);
            pst.setString(3, brandName);
            pst.setDouble(4, size);
            pst.setString(5, material);
            pst.setDate(6, receivedDate);
            if (issuedDate != null) {
                pst.setDate(7, issuedDate);
            } else {
                pst.setNull(7, java.sql.Types.DATE);
            }
            pst.setInt(8, totalIssued);
            pst.setInt(9, availableQty);
            pst.setDouble(10, unitPrice);
            pst.setString(11, workOrderNo.isEmpty() ? null : workOrderNo); // ⭐ NEW
            pst.setInt(12, stockId);

            int updated = pst.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Buyer stock updated successfully!");
                if (parentPanel != null) {
                    parentPanel.refreshTable();
                }
                this.dispose();
            }

            pst.close();
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating buyer stock: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {
            int stockQty = Integer.parseInt(jTextField3.getText());
            int totalIssued = Integer.parseInt(jTextField5.getText());
            int availableQty = Math.max(0, stockQty - totalIssued);

            double unitPrice = Double.parseDouble(jTextField7.getText().replace("Rs.", "").trim());
            java.sql.Date receivedDate = new java.sql.Date(jDateChooser1.getDate().getTime());
            java.sql.Date issuedDate = (jDateChooser2.getDate() != null)
                    ? new java.sql.Date(jDateChooser2.getDate().getTime()) : null;

            String colour = jTextField2.getText();
            String material = jTextField4.getText();
            String brandName = jTextField8.getText();
            double size = Double.parseDouble(jTextField10.getText());
            String workOrderNo = jTextField9.getText().trim(); // ⭐ Work Order No

            Connection conn = DatabaseConnection.getConnection();

            // ✅ Updated SQL to include work_order_no
            String sql = "UPDATE bstock SET colour=?, stock_qty=?, brand_name=?, size=?, material=?, "
                    + "received_date=?, issued_date=?, total_issued=?, available_qty=?, unit_price=?, "
                    + "work_order_no=?, last_modified=NOW() WHERE id=?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, colour);
            pst.setInt(2, stockQty);
            pst.setString(3, brandName);
            pst.setDouble(4, size);
            pst.setString(5, material);
            pst.setDate(6, receivedDate);
            if (issuedDate != null) {
                pst.setDate(7, issuedDate);
            } else {
                pst.setNull(7, java.sql.Types.DATE);
            }
            pst.setInt(8, totalIssued);
            pst.setInt(9, availableQty);
            pst.setDouble(10, unitPrice);
            pst.setString(11, workOrderNo.isEmpty() ? null : workOrderNo); // ⭐ NEW
            pst.setInt(12, stockId);

            int updated = pst.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Buyer stock updated successfully!");
                if (parentPanel != null) {
                    parentPanel.refreshTable();
                }
                this.dispose();
            }

            pst.close();
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating buyer stock: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) {
        try {
            com.formdev.flatlaf.themes.FlatMacLightLaf.setup();
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.themes.FlatMacLightLaf());
        } catch (Exception ex) {
            System.err.println("⚠️ FlatLaf not found. Falling back to Nimbus Look and Feel.");
        }

        java.awt.EventQueue.invokeLater(() -> {
            UpdateBuyerStockDFrame dialog = new UpdateBuyerStockDFrame();
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    public com.toedter.calendar.JDateChooser jDateChooser1;
    public com.toedter.calendar.JDateChooser jDateChooser2;
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
    public javax.swing.JTextField jTextField1;
    public javax.swing.JTextField jTextField10;
    public javax.swing.JTextField jTextField2;
    public javax.swing.JTextField jTextField3;
    public javax.swing.JTextField jTextField4;
    public javax.swing.JTextField jTextField5;
    public javax.swing.JTextField jTextField6;
    public javax.swing.JTextField jTextField7;
    public javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
