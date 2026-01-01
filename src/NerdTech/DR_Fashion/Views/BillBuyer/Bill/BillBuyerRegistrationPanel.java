package NerdTech.DR_Fashion.Views.BillBuyer.Bill;

import NerdTech.DR_Fashion.Views.BillBuyer.Bill.RegisterInvoice.RegisterInvoiceNoPanel;
import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.BillBuyer.Bill.AddBillBuyerDFrame;
import NerdTech.DR_Fashion.Views.BillBuyer.Bill.UpdateBillBuyer;
import NerdTech.DR_Fashion.Views.Bill.BillPanel;
import NerdTech.DR_Fashion.Views.Dashboard;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MG_Pathum
 */
public class BillBuyerRegistrationPanel extends javax.swing.JPanel {

    private Dashboard dashboard;

    public BillBuyerRegistrationPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        initComponents();
        loadBillBuyerData();
        setupTableClickListener(); // නව method එක
    }

    // Table double-click listener එකක් add කරමු
    private void setupTableClickListener() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double click
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow != -1) {
                        int buyerId = (int) jTable1.getValueAt(selectedRow, 0);
                        String buyerName = (String) jTable1.getValueAt(selectedRow, 1);
                        navigateToInvoicePanel(buyerId, buyerName); // මෙතන method එක වෙනස්
                    }
                }
            }
        });
    }

    private void navigateToInvoicePanel(int buyerId, String buyerName) {
        if (dashboard != null) {
            dashboard.loadPanelWithLoading("Invoice Management - " + buyerName,
                    () -> new RegisterInvoiceNoPanel(buyerId, buyerName));
        } else {
            // Fallback method
            try {
                javax.swing.JFrame frame = (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this);
                if (frame != null) {
                    RegisterInvoiceNoPanel invoicePanel = new RegisterInvoiceNoPanel(buyerId, buyerName);
                    frame.setContentPane(invoicePanel);
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
        }
    }

    // Method to load bill buyer data into table
    private void loadBillBuyerData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT id, name, invoice_no FROM bill_buyer ORDER BY id";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("invoice_no") // මේකයි අලුතින් add වෙන column එක
                });
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading bill buyer data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Method to refresh table data
    public void refreshTable() {
        loadBillBuyerData();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(1257, 0));
        setPreferredSize(new java.awt.Dimension(1257, 0));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Name"
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

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Bill Buyer Registration");

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Delete");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Add ");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1543, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 684, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(333, 333, 333)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(17, 17, 17))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Add button - Open Add Dialog
        AddBillBuyerDFrame dialog = new AddBillBuyerDFrame(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true,
                this
        );
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a row to update!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) jTable1.getValueAt(selectedRow, 0);
        String currentName = (String) jTable1.getValueAt(selectedRow, 1);

        UpdateBillBuyer dialog = new UpdateBillBuyer(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true,
                this,
                id,
                currentName
        );
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a row to delete!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) jTable1.getValueAt(selectedRow, 0);
        String name = (String) jTable1.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete '" + name + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                String query = "DELETE FROM bill_buyer WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);

                int result = ps.executeUpdate();
                ps.close();

                if (result > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Bill Buyer deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadBillBuyerData();
                }
            } catch (SQLException e) {
                if (e.getErrorCode() == 1451) {
                    JOptionPane.showMessageDialog(this,
                            "Cannot delete this bill buyer!\nThere are bills associated with this buyer.",
                            "Delete Error - Foreign Key Constraint",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error deleting bill buyer: " + e.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                e.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting bill buyer: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed


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
