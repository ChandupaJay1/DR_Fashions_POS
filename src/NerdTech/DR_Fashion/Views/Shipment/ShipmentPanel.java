package NerdTech.DR_Fashion.Views.Shipment;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.Shipment.Cutting.OrderNoPanel;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MG_Pathum
 */
public class ShipmentPanel extends javax.swing.JPanel {

    public ShipmentPanel() {
        initComponents();
        loadShipmentData();
        setupTableDoubleClick();
    }

    private void setupTableDoubleClick() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double click
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow != -1) {
                        // Get buyer name from the selected row (column index 2)
                        String buyerName = jTable1.getValueAt(selectedRow, 2) != null
                                ? jTable1.getValueAt(selectedRow, 2).toString()
                                : "";

                        if (!buyerName.isEmpty()) {
                            // Open ViewBuyerWise dialog with the buyer name
                            ViewBuyerWise dialog = new ViewBuyerWise(
                                    (javax.swing.JFrame) SwingUtilities.getWindowAncestor(ShipmentPanel.this),
                                    true,
                                    buyerName
                            );
                            dialog.setLocationRelativeTo(ShipmentPanel.this);
                            dialog.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(ShipmentPanel.this,
                                    "Buyer name is empty for this record",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private void openEditDialog(int row) {
        // Get data from selected row
        String orderNo = jTable1.getValueAt(row, 0) != null ? jTable1.getValueAt(row, 0).toString() : "";
        String fabricInhouseDate = jTable1.getValueAt(row, 1) != null ? jTable1.getValueAt(row, 1).toString() : "";
        String buyerName = jTable1.getValueAt(row, 2) != null ? jTable1.getValueAt(row, 2).toString() : "";
        String rollCutQty = jTable1.getValueAt(row, 3) != null ? jTable1.getValueAt(row, 3).toString() : "";
        String yardageFabric = jTable1.getValueAt(row, 4) != null ? jTable1.getValueAt(row, 4).toString() : "";
        String yardagePocketing = jTable1.getValueAt(row, 5) != null ? jTable1.getValueAt(row, 5).toString() : "";
        String subGarment = jTable1.getValueAt(row, 6) != null ? jTable1.getValueAt(row, 6).toString() : "";
        String style = jTable1.getValueAt(row, 7) != null ? jTable1.getValueAt(row, 7).toString() : "";
        String fabricUse = jTable1.getValueAt(row, 8) != null ? jTable1.getValueAt(row, 8).toString() : "";
        String brand = jTable1.getValueAt(row, 9) != null ? jTable1.getValueAt(row, 9).toString() : "";
        String cutPcs = jTable1.getValueAt(row, 10) != null ? jTable1.getValueAt(row, 10).toString() : "";
        String lineInQty = jTable1.getValueAt(row, 11) != null ? jTable1.getValueAt(row, 11).toString() : "";
        String month = jTable1.getValueAt(row, 12) != null ? jTable1.getValueAt(row, 12).toString() : "";
        String shipmentQty = jTable1.getValueAt(row, 13) != null ? jTable1.getValueAt(row, 13).toString() : "";
        String shipmentDate = jTable1.getValueAt(row, 14) != null ? jTable1.getValueAt(row, 14).toString() : "";
        String drInvoiceNo = jTable1.getValueAt(row, 15) != null ? jTable1.getValueAt(row, 15).toString() : "";
        String nextOrderBalance = jTable1.getValueAt(row, 16) != null ? jTable1.getValueAt(row, 16).toString() : "";
        String remark = jTable1.getValueAt(row, 17) != null ? jTable1.getValueAt(row, 17).toString() : "";
        String status = jTable1.getValueAt(row, 18) != null ? jTable1.getValueAt(row, 18).toString() : "";
        String paymentMethod = jTable1.getValueAt(row, 19) != null ? jTable1.getValueAt(row, 19).toString() : "";

        // Open dialog with data for editing
        AddShipmentDFrame dialog = new AddShipmentDFrame(
                (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this),
                true,
                orderNo, fabricInhouseDate, buyerName, rollCutQty, yardageFabric,
                yardagePocketing, subGarment, style, fabricUse, brand, cutPcs,
                lineInQty, month, shipmentQty, shipmentDate, drInvoiceNo,
                nextOrderBalance, remark, status, paymentMethod
        );
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        // Reload table after dialog closes
        loadShipmentData();
    }

    private void loadShipmentData() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        String query = """
        SELECT 
            id,
            order_no,
            fabric_inhouse_date,
            buyer_name,
            roll_cut_qty,
            yardage_fabric,
            yardage_pocketing,
            sub_garment,
            style,
            fabric_use,
            brand,
            cut_pcs,
            line_in_qty,
            month,
            shipment_qty,
            shipment_date,
            dr_invoice_no,
            next_order_balance,
            remark,
            status,
            payment_method
        FROM shipment
        ORDER BY id DESC
    """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            int rowCount = 0;

            while (rs.next()) {
                rowCount++;
                model.addRow(new Object[]{
                    rs.getString("order_no"),
                    rs.getString("fabric_inhouse_date"),
                    rs.getString("buyer_name"),
                    rs.getString("roll_cut_qty"),
                    rs.getString("yardage_fabric"),
                    rs.getString("yardage_pocketing"),
                    rs.getString("sub_garment"),
                    rs.getString("style"),
                    rs.getString("fabric_use"),
                    rs.getString("brand"),
                    rs.getString("cut_pcs"),
                    rs.getString("line_in_qty"),
                    rs.getString("month"),
                    rs.getString("shipment_qty"),
                    rs.getString("shipment_date"),
                    rs.getString("dr_invoice_no"),
                    rs.getString("next_order_balance"),
                    rs.getString("remark"),
                    rs.getString("status"),
                    rs.getString("payment_method")
                });
            }

            if (rowCount == 0) {
                JOptionPane.showMessageDialog(this,
                        "No data found in shipment table. Please add some records first.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("Loaded " + rowCount + " records successfully");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Connection Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Shipment Details");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Order Number", "Fabric Inhouse Date", "Buyer Name", "Roll Cut Qty", "Yardage Fabric", "Yardage Pocketing", "Sub Garment", "Style", "Fabric Use", "Brand", "Cut Pcs", "Line In Qty", "Month", "Shipment Qty", "Shipment Date", "Dr Invoice No", "Next Order Balance", "Remark", "Status", "Payment Method"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Add ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Delete");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Update");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton4.setText("Cutting");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1452, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(318, 318, 318)
                        .addComponent(jButton3)
                        .addGap(384, 384, 384)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Add new shipment - Open empty dialog
        AddShipmentDFrame dialog = new AddShipmentDFrame(
                (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this),
                true
        );
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        // Reload table after dialog closes
        loadShipmentData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Update selected shipment - Open dialog with selected row data
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a shipment record to update",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Use the same method as double-click
        openEditDialog(selectedRow);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Delete selected shipment
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a shipment record to delete",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get order number from selected row
        String orderNo = jTable1.getValueAt(selectedRow, 0).toString();

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete shipment: " + orderNo + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            deleteShipment(orderNo);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Cutting button - Switch to CuttingPanel within the same panel
        switchToOrderNoPanel();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void switchToOrderNoPanel() {
        try {
            // Remove all current components
            this.removeAll();

            // Create OrderNoPanel instance
            OrderNoPanel orderNoPanel = new OrderNoPanel();

            // Set layout and add the new panel
            this.setLayout(new java.awt.BorderLayout());
            this.add(orderNoPanel, java.awt.BorderLayout.CENTER);

            // Refresh panel visually
            this.revalidate();
            this.repaint();

            System.out.println("Switched to OrderNoPanel successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error switching to Order No Panel: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteShipment(String orderNo) {
        String deleteQuery = "DELETE FROM shipment WHERE order_no = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(deleteQuery)) {

            ps.setString(1, orderNo);
            int result = ps.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this,
                        "Shipment deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                loadShipmentData(); // Reload table
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete shipment",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Connection Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
