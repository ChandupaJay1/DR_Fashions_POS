package NerdTech.DR_Fashion.Views.Bill;

import NerdTech.DR_Fashion.Views.Bill.AddBill;
import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BillPanel extends javax.swing.JPanel {

    private Integer filteredInvoiceId = null;
    private Integer filteredBuyerId = null;
    private String filteredBuyerName = null;
    private String filteredInvoiceNo = null;

    // Constructor 1: Show all bills
    public BillPanel() {
        this.filteredInvoiceId = null;
        this.filteredBuyerId = null;
        this.filteredBuyerName = null;
        this.filteredInvoiceNo = null;
        initComponents();
        fixBillTableColumns();  // මේක add කරන්න
        loadBillData();
        setupButtonActions();
        updateTitle();
    }

    // Constructor 2: Show bills for specific buyer (old constructor - keep for compatibility)
    public BillPanel(int buyerId, String buyerName) {
        this.filteredInvoiceId = null;
        this.filteredBuyerId = buyerId;
        this.filteredBuyerName = buyerName;
        this.filteredInvoiceNo = null;
        initComponents();
        fixBillTableColumns();  // මේක add කරන්න
        loadBillData();
        setupButtonActions();
        updateTitle();
    }

    private void fixBillTableColumns() {
        // Bill ID column එක සමග table model එක reset කරන්න
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Bill ID", "Date", "Description", "Size", "Qty", "Unit",
                    "Price", "Return Amount", "Total Amount", "Paid Amount",
                    "Balance", "Remark", "Payment Method"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // කිසිම cell එකක් edit කරන්න බෑ
            }
        };
        jTable1.setModel(model);

        // Bill ID column එක hide කරන්න (පළමු column එක)
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    // Constructor 3: Show bills for specific invoice (NEW - මේක තමයි අලුත constructor එක)
    // Constructor 3: Show bills for specific invoice
    public BillPanel(int invoiceId, int buyerId, String buyerName, String invoiceNo) {
        this.filteredInvoiceId = invoiceId;
        this.filteredBuyerId = buyerId;
        this.filteredBuyerName = buyerName;
        this.filteredInvoiceNo = invoiceNo;
        initComponents();
        fixBillTableColumns();  // ⬅️ මේ line එක add කරන්න!
        loadBillData();
        setupButtonActions();
        updateTitle();
    }

    // Update title based on filter
    private void updateTitle() {
        if (filteredInvoiceId != null && filteredInvoiceNo != null) {
            // Invoice specific view
            jLabel1.setText("Bill Details - " + filteredBuyerName + " - Invoice: " + filteredInvoiceNo);
        } else if (filteredBuyerId != null && filteredBuyerName != null) {
            // Buyer specific view (all invoices)
            jLabel1.setText("Bill Details - " + filteredBuyerName);
        } else {
            // All buyers view
            jLabel1.setText("Bill Details - All Buyers");
        }
    }

    // Setup Button Actions
    private void setupButtonActions() {

        jButton1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                if (filteredInvoiceId != null) {

                    java.awt.Frame parentFrame
                            = (java.awt.Frame) SwingUtilities.getWindowAncestor(BillPanel.this);

                    AddBill dialog = new AddBill(
                            parentFrame,
                            true,
                            BillPanel.this,
                            filteredInvoiceId,
                            filteredBuyerId,
                            filteredBuyerName,
                            filteredInvoiceNo
                    );
                    dialog.setVisible(true);

                } else if (filteredBuyerId != null && filteredBuyerName != null) {

                    JOptionPane.showMessageDialog(
                            BillPanel.this,
                            "Please select a specific invoice first!",
                            "No Invoice Selected",
                            JOptionPane.WARNING_MESSAGE
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            BillPanel.this,
                            "Please select a buyer and invoice first!",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

    }

    // Load Bill Data (with filter based on invoice or buyer)
    private void loadBillData() {
        try {
            Connection conn = DatabaseConnection.getConnection();

            // Query එකට bill ID එක add කරන්න
            String query = "SELECT b.id, b.date, b.description, b.size, b.qty, b.unit, b.price, "
                    + "b.return_amount, b.total_amount, b.paid_amount, b.balance, "
                    + "b.remark, b.payment_method, bb.name AS buyer_name, i.invoice_no "
                    + "FROM bill b "
                    + "INNER JOIN invoice_no i ON b.invoice_no_id = i.id "
                    + "INNER JOIN bill_buyer bb ON i.bill_buyer_id = bb.id ";

            if (filteredInvoiceId != null) {
                query += "WHERE b.invoice_no_id = ? ";
            } else if (filteredBuyerId != null) {
                query += "WHERE i.bill_buyer_id = ? ";
            }

            query += "ORDER BY b.date DESC, b.id DESC";

            PreparedStatement ps = conn.prepareStatement(query);

            if (filteredInvoiceId != null) {
                ps.setInt(1, filteredInvoiceId);
            } else if (filteredBuyerId != null) {
                ps.setInt(1, filteredBuyerId);
            }

            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            int rowCount = 0;
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"), // Bill ID (hidden column)
                    rs.getString("date"),
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

            if (filteredInvoiceId != null && rowCount == 0) {
                JOptionPane.showMessageDialog(this,
                        "No bills found for invoice: " + filteredInvoiceNo,
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

    // Delete Bill
    private void deleteBill(int billId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "DELETE FROM bill WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, billId);

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
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                JOptionPane.showMessageDialog(this,
                        "Cannot delete this bill!\nThere are related records.",
                        "Delete Error - Foreign Key Constraint",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting bill: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting bill: " + e.getMessage(),
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
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Date", "Description", "Size", "Qty", "Unit", "Price", "Return Amount", "Total Amount", "Paid Amount", "Balance", "Remark", "Payment Method"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Add");

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
        jButton4.setText("Back");
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
                        .addGap(379, 379, 379)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 383, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addGap(292, 292, 292)
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
        try {
            javax.swing.JFrame frame
                    = (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this);

            if (frame != null && frame instanceof NerdTech.DR_Fashion.Views.Dashboard) {

                NerdTech.DR_Fashion.Views.Dashboard dashboard
                        = (NerdTech.DR_Fashion.Views.Dashboard) frame;

                // 🔑 Invoice list panel load කරනවා
                dashboard.loadPanelWithLoading(
                        "Invoice No - " + filteredBuyerName,
                        () -> new NerdTech.DR_Fashion.Views.BillBuyer.Bill.RegisterInvoice.RegisterInvoiceNoPanel(
                                filteredBuyerId,
                                filteredBuyerName
                        )
                );

            } else {
                // fallback
                frame.setContentPane(
                        new NerdTech.DR_Fashion.Views.BillBuyer.Bill.RegisterInvoice.RegisterInvoiceNoPanel(
                                filteredBuyerId,
                                filteredBuyerName
                        )
                );
                frame.revalidate();
                frame.repaint();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading invoice panel: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a bill to update!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Hidden column (0) එකෙන් Bill ID එක ගන්න
        int billId = (int) jTable1.getValueAt(selectedRow, 0);

        // Update dialog open කරන්න
        openUpdateBillDialog(billId);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bill to delete!",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Hidden column එකෙන් Bill ID එක ගන්න
        int billId = (int) jTable1.getValueAt(selectedRow, 0);
        String description = (String) jTable1.getValueAt(selectedRow, 2); // Column 2 is description (0 is hidden ID)

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this bill?\nDescription: " + description,
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            deleteBill(billId);
        }
    }//GEN-LAST:event_jButton3ActionPerformed


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
