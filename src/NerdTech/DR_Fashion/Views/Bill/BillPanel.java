package NerdTech.DR_Fashion.Views.Bill;

import NerdTech.DR_Fashion.Views.Bill.AddBill;
import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MG_Pathum
 */
public class BillPanel extends javax.swing.JPanel {

    private Integer filteredBuyerId = null;
    private String filteredBuyerName = null;

    // Constructor 1: Show all bills
    public BillPanel() {
        this.filteredBuyerId = null;
        this.filteredBuyerName = null;
        initComponents();
        loadBillData();
        setupButtonActions();
        updateTitle();
    }

    // Constructor 2: Show bills for specific buyer
    public BillPanel(int buyerId, String buyerName) {
        this.filteredBuyerId = buyerId;
        this.filteredBuyerName = buyerName;
        initComponents();
        loadBillData();
        setupButtonActions();
        updateTitle();
    }

    // Update title based on filter
    private void updateTitle() {
        if (filteredBuyerId != null && filteredBuyerName != null) {
            jLabel1.setText("Bill Details - " + filteredBuyerName);
        } else {
            jLabel1.setText("Bill Details - All Buyers");
        }
    }

    // Setup Button Actions
    private void setupButtonActions() {
        // Add Bill Button
        jButton1.addActionListener(e -> {
            java.awt.Frame parentFrame = (java.awt.Frame) SwingUtilities.getWindowAncestor(this);
            AddBill dialog = new AddBill(parentFrame, true, this);
            dialog.setVisible(true);
        });

        // Update Bill Button (සීරීස් bill update කරන්න)
        jButton2.addActionListener(e -> {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a bill to update!",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String invoiceNo = jTable1.getValueAt(selectedRow, 2).toString();
                int billId = getBillIdByInvoiceNo(invoiceNo);
                if (billId > 0) {
                    java.awt.Frame parentFrame = (java.awt.Frame) SwingUtilities.getWindowAncestor(this);
                    UpdateBill dialog = new UpdateBill(parentFrame, true, this, billId);
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Bill not found!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete Bill Button
        jButton3.addActionListener(e -> {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a bill to delete!",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String invoiceNo = jTable1.getValueAt(selectedRow, 2).toString();
            String buyerName = jTable1.getValueAt(selectedRow, 0).toString();

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this bill?\n\nBuyer: " + buyerName
                    + "\nInvoice: " + invoiceNo,
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                deleteBill(invoiceNo);
            }
        });

        // Go To Buyer Registration
        jButton4.addActionListener(e -> goToBuyerRegistration());
    }

    // Load Bill Data (with or without filter)
    private void loadBillData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT bb.name AS buyer_name, b.date, b.invoice_no, "
                    + "b.description, b.size, b.qty, b.unit, b.price, "
                    + "b.return_amount, b.total_amount, b.paid_amount, b.balance, "
                    + "b.remark, b.payment_method "
                    + "FROM bill b "
                    + "INNER JOIN bill_buyer bb ON b.bill_buyer_id = bb.id ";

            if (filteredBuyerId != null) {
                query += "WHERE b.bill_buyer_id = ? ";
            }

            query += "ORDER BY b.date DESC, b.id DESC";

            PreparedStatement ps = conn.prepareStatement(query);

            if (filteredBuyerId != null) {
                ps.setInt(1, filteredBuyerId);
            }

            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            int rowCount = 0;
            while (rs.next()) {
                Object[] row = {
                    rs.getString("buyer_name"),
                    rs.getString("date"),
                    rs.getString("invoice_no"),
                    rs.getString("description"),
                    rs.getString("size"),
                    rs.getString("qty"),
                    rs.getString("unit"),
                    rs.getString("price"),
                    rs.getString("return_amount"),
                    rs.getString("total_amount"),
                    rs.getString("paid_amount"),
                    rs.getString("balance"),
                    rs.getString("remark"),
                    rs.getString("payment_method")
                };
                model.addRow(row);
                rowCount++;
            }

            rs.close();
            ps.close();

            if (filteredBuyerId != null && rowCount == 0) {
                JOptionPane.showMessageDialog(this,
                        "No bills found for " + filteredBuyerName,
                        "No Data",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading bill data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Get Bill ID
    private int getBillIdByInvoiceNo(String invoiceNo) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT id FROM bill WHERE invoice_no = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, invoiceNo);
            ResultSet rs = ps.executeQuery();

            int id = 0;
            if (rs.next()) {
                id = rs.getInt("id");
            }

            rs.close();
            ps.close();
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Delete Bill
    private void deleteBill(String invoiceNo) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "DELETE FROM bill WHERE invoice_no = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, invoiceNo);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Bill deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete bill!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting bill: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Go to Buyer Registration Panel
    private void goToBuyerRegistration() {
        try {
            javax.swing.JFrame frame = (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this);

            if (frame != null && frame instanceof NerdTech.DR_Fashion.Views.Dashboard) {
                NerdTech.DR_Fashion.Views.Dashboard dashboard
                        = (NerdTech.DR_Fashion.Views.Dashboard) frame;
                dashboard.loadPanelWithLoading("Bill Registration",
                        () -> new NerdTech.DR_Fashion.Views.BillBuyer.Bill.BillBuyerRegistrationPanel(dashboard));
            } else {
                JOptionPane.showMessageDialog(this,
                        "Cannot navigate to registration panel. Please use the Bill button in the main menu.",
                        "Navigation Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading registration panel: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Refresh Table
    public void refreshTable() {
        loadBillData();
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
        jLabel1.setText("Bill Details");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Buyer Name", "Date", "Invoice No", "Description", "Size", "Qty", "Unit", "Price", "Return Amount", "Total Amount", "Paid Amount", "Balance", "Remark", "Payment Method"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Update");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton4.setText("Go to Registration Buyer");
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
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 283, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(274, 274, 274)
                        .addComponent(jButton3)
                        .addGap(217, 217, 217)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        goToBuyerRegistration();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void openUpdateBillDialog(int billId) {
        try {
            java.awt.Frame parentFrame = (java.awt.Frame) SwingUtilities.getWindowAncestor(this);
            UpdateBill updateDialog = new UpdateBill(parentFrame, true, this, billId);
            updateDialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening update bill: " + e.getMessage());
        }
    }


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


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
