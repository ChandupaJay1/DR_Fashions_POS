package NerdTech.DR_Fashion.Views.BillBuyer.Bill.RegisterInvoice;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class RegisterInvoiceNoPanel extends javax.swing.JPanel {

    private int buyerId;
    private String buyerName;

    // Constructor with buyer details
    public RegisterInvoiceNoPanel(int buyerId, String buyerName) {
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        initComponents();
        fixTableColumns();
        updateTitle();
        loadInvoiceData();
        setupTableClickListener();
    }

    private void fixTableColumns() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Invoice No", "Date"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(model);

        // ID column එක hide කරන්න
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    private void setupTableClickListener() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Double click
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow != -1) {
                        int invoiceId = (int) jTable1.getValueAt(selectedRow, 0);
                        String invoiceNo = (String) jTable1.getValueAt(selectedRow, 1);
                        navigateToBillPanel(invoiceId, invoiceNo);
                    }
                }
            }
        });
    }

    // BillPanel එකට යන method එක - Dashboard සමග
    private void navigateToBillPanel(int invoiceId, String invoiceNo) {
        try {
            javax.swing.JFrame frame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

            if (frame != null && frame instanceof NerdTech.DR_Fashion.Views.Dashboard) {
                // Dashboard එක use කරන්න
                NerdTech.DR_Fashion.Views.Dashboard dashboard
                        = (NerdTech.DR_Fashion.Views.Dashboard) frame;

                dashboard.loadPanelWithLoading("Bill Details - " + buyerName + " - Invoice: " + invoiceNo,
                        () -> new NerdTech.DR_Fashion.Views.Bill.BillPanel(invoiceId, buyerId, buyerName, invoiceNo));

            } else {
                // Fallback - Dashboard එක නැත්නම්
                NerdTech.DR_Fashion.Views.Bill.BillPanel billPanel
                        = new NerdTech.DR_Fashion.Views.Bill.BillPanel(invoiceId, buyerId, buyerName, invoiceNo);

                frame.setContentPane(billPanel);
                frame.revalidate();
                frame.repaint();
            }

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading bill panel: " + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    // Title එක buyer name එක සමග update කරන method
    private void updateTitle() {
        jLabel1.setText("Invoice No - " + buyerName);
    }

    // Invoice data load කරන method
    private void loadInvoiceData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT id, invoice_no, DATE_FORMAT(date, '%Y-%m-%d') as formatted_date "
                    + "FROM invoice_no WHERE bill_buyer_id = ? ORDER BY id DESC";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, buyerId);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("invoice_no"),
                    rs.getString("formatted_date")
                });
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading invoice data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Table refresh කරන method
    public void refreshTable() {
        loadInvoiceData();
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

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Invoice No");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Invoice No", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
        }

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
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(479, 479, 479)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 495, Short.MAX_VALUE)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an invoice to delete!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int invoiceId = (int) jTable1.getValueAt(selectedRow, 0);
        String invoiceNo = (String) jTable1.getValueAt(selectedRow, 1);
        String invoiceDate = (String) jTable1.getValueAt(selectedRow, 2);

        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this invoice?\n\n"
                + "Invoice No: " + invoiceNo + "\n"
                + "Date: " + invoiceDate,
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DatabaseConnection.getConnection();

                // First check if there are bills associated with this invoice
                String checkQuery = "SELECT COUNT(*) FROM bill WHERE invoice_no_id = ?";
                PreparedStatement checkPs = conn.prepareStatement(checkQuery);
                checkPs.setInt(1, invoiceId);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    rs.close();
                    checkPs.close();

                    int confirmWithBills = JOptionPane.showConfirmDialog(this,
                            "This invoice has " + rs.getInt(1) + " bill(s) associated with it!\n\n"
                            + "Deleting this invoice will also delete all related bills.\n"
                            + "Are you sure you want to continue?",
                            "Warning - Associated Bills Found",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (confirmWithBills != JOptionPane.YES_OPTION) {
                        return;
                    }

                    // Delete associated bills first
                    String deleteBillsQuery = "DELETE FROM bill WHERE invoice_no_id = ?";
                    PreparedStatement deleteBillsPs = conn.prepareStatement(deleteBillsQuery);
                    deleteBillsPs.setInt(1, invoiceId);
                    deleteBillsPs.executeUpdate();
                    deleteBillsPs.close();
                }
                rs.close();
                checkPs.close();

                // Now delete the invoice
                String deleteQuery = "DELETE FROM invoice_no WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(deleteQuery);
                ps.setInt(1, invoiceId);

                int result = ps.executeUpdate();
                ps.close();

                if (result > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Invoice deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadInvoiceData();
                }
            } catch (SQLException e) {
                // Handle foreign key constraint error
                if (e.getErrorCode() == 1451) {
                    JOptionPane.showMessageDialog(this,
                            "Cannot delete this invoice!\n"
                            + "There are bills associated with this invoice that cannot be deleted.\n\n"
                            + "Error: " + e.getMessage(),
                            "Delete Error - Foreign Key Constraint",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Database Error: " + e.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                e.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting invoice: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // AddInvoiceNo dialog එක ඇරිය
        AddInvoiceNoDialog dialog = new AddInvoiceNoDialog(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true,
                buyerId
        );
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        // Dialog එක close වුන පසු table refresh කරන්න
        if (dialog.isSaved()) {
            loadInvoiceData();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a row to update!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int invoiceId = (int) jTable1.getValueAt(selectedRow, 0);
        String invoiceNo = (String) jTable1.getValueAt(selectedRow, 1);
        String invoiceDate = (String) jTable1.getValueAt(selectedRow, 2);

        // UpdateInvoiceNoDialog එක ඇරිය
        UpdateInvoiceNoDialog dialog = new UpdateInvoiceNoDialog(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true,
                buyerId,
                invoiceId,
                invoiceNo,
                invoiceDate
        );
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        // Dialog එක close වුන පසු table refresh කරන්න
        if (dialog.isSaved()) {
            loadInvoiceData();
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
