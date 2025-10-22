package NerdTech.DR_Fashion.Views.BuyerRegistrationPanel;

import NerdTech.DR_Fashion.Views.Dashboard;
import NerdTech.DR_Fashion.Views.ViewBuyerStock.ViewBuyerStock;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class RegistrationBuyerPanel extends javax.swing.JPanel {

    private Dashboard parentDashboard; // 🔹 Reference to Dashboard

    // Modified constructor
    public RegistrationBuyerPanel(Dashboard parentDashboard) {
        initComponents();
        this.parentDashboard = parentDashboard;
        loadBuyerData();
        // initComponents() method eke table eka define karana kotala, me code eka add karanna:
        model.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double click check
                    jTableMouseClicked(evt);
                }
            }
        });
    }

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = model.getSelectedRow();
        if (selectedRow != -1) {
            String buyerName = model.getValueAt(selectedRow, 0).toString();
            openViewBuyerStock(buyerName);
        }
    }

    private void openViewBuyerStock(String buyerName) {
        try {
            // Create a new JFrame to show the buyer stock
            javax.swing.JFrame frame = new javax.swing.JFrame("Buyer Stock - " + buyerName);
            frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

            // Set the specific size [675, 80]
            frame.setSize(1800, 800);
            frame.setLocationRelativeTo(null);

            ViewBuyerStock buyerStockPanel = new ViewBuyerStock(buyerName);
            frame.add(buyerStockPanel);
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error opening buyer stock: " + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadBuyerData() {
        try {
            Connection con = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, email, mobile_no, lan_no, coodinator, address, company_name, brand_name, br_no, br_name, payment_method, due_payment FROM registration_buyer");

            DefaultTableModel tableModel = (DefaultTableModel) model.getModel();
            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] rowData = new Object[13];
                rowData[0] = rs.getString("name");
                rowData[1] = rs.getString("email");
                rowData[2] = rs.getString("mobile_no");
                rowData[3] = rs.getString("lan_no");
                rowData[4] = rs.getString("coodinator");
                rowData[5] = rs.getString("address");
                rowData[6] = rs.getString("company_name");
                rowData[7] = rs.getString("brand_name");
                rowData[8] = rs.getString("br_no");
                rowData[9] = rs.getString("br_name");
                rowData[10] = rs.getString("payment_method");
                rowData[11] = rs.getString("due_payment");

                tableModel.addRow(rowData);
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error loading buyers: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        model = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Registration Buyer");

        model.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Name", "Email", "Mobile No", "Lan-No", "Coodinator", "Address", "Company Name", "Brand Name", "BR No", "BR Name", "Payment Method", "Due Payments"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(model);
        if (model.getColumnModel().getColumnCount() > 0) {
            model.getColumnModel().getColumn(0).setResizable(false);
            model.getColumnModel().getColumn(0).setPreferredWidth(100);
            model.getColumnModel().getColumn(1).setResizable(false);
            model.getColumnModel().getColumn(1).setPreferredWidth(150);
            model.getColumnModel().getColumn(2).setResizable(false);
            model.getColumnModel().getColumn(2).setPreferredWidth(100);
            model.getColumnModel().getColumn(3).setResizable(false);
            model.getColumnModel().getColumn(3).setPreferredWidth(100);
            model.getColumnModel().getColumn(4).setResizable(false);
            model.getColumnModel().getColumn(5).setResizable(false);
            model.getColumnModel().getColumn(5).setPreferredWidth(100);
            model.getColumnModel().getColumn(6).setResizable(false);
            model.getColumnModel().getColumn(6).setPreferredWidth(100);
            model.getColumnModel().getColumn(7).setResizable(false);
            model.getColumnModel().getColumn(7).setPreferredWidth(100);
            model.getColumnModel().getColumn(8).setResizable(false);
            model.getColumnModel().getColumn(9).setResizable(false);
            model.getColumnModel().getColumn(10).setResizable(false);
            model.getColumnModel().getColumn(11).setResizable(false);
        }

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Delete Buyer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Add Buyer");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Update Buyer");
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(92, 92, 92)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 727, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Delete Buyer
        int selectedRow = model.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "⚠️ Please select a row to delete!",
                    "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get buyer name from selected row
        String buyerName = model.getValueAt(selectedRow, 0).toString();

        // Confirmation dialog
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete buyer:\n" + buyerName + "?",
                "Confirm Delete",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE
        );

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            deleteBuyer(selectedRow);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void deleteBuyer(int selectedRow) {
        try {
            // Remove the selected row from the table model (UI only)
            DefaultTableModel tableModel = (DefaultTableModel) model.getModel();
            tableModel.removeRow(selectedRow);

            javax.swing.JOptionPane.showMessageDialog(this,
                    "✅ Buyer removed from table (UI only).",
                    "Removed",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "❌ Error removing buyer from UI:\n" + e.getMessage(),
                    "UI Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        AddBuyerDFrame dialog = new AddBuyerDFrame(
                (java.awt.Frame) parentWindow,
                true,
                this // Pass the panel reference
        );
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = model.getSelectedRow();

        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "⚠️ Please select a row to update!",
                    "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ✅ table model එක ගන්නවා
        javax.swing.table.TableModel tableModel = model.getModel();

        // Get row data from table
        String name = tableModel.getValueAt(selectedRow, 0).toString();
        String email = tableModel.getValueAt(selectedRow, 1).toString();
        String mobileNo = tableModel.getValueAt(selectedRow, 2).toString();
        String lanNo = tableModel.getValueAt(selectedRow, 3).toString();
        String coordinator = tableModel.getValueAt(selectedRow, 4).toString();
        String address = tableModel.getValueAt(selectedRow, 5).toString();
        String companyName = tableModel.getValueAt(selectedRow, 6).toString();
        String brandName = tableModel.getValueAt(selectedRow, 7).toString();
        String brNo = tableModel.getValueAt(selectedRow, 8).toString();
        String brName = tableModel.getValueAt(selectedRow, 9).toString();
        String paymentMethod = tableModel.getValueAt(selectedRow, 10).toString();
        String duePayment = tableModel.getValueAt(selectedRow, 11).toString();

        // Open update dialog with data
        java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(this);

        if (parentWindow instanceof java.awt.Frame) {
            NerdTech.DR_Fashion.Views.BuyerPanel.UpdateBuyersDFrame dialog
                    = new NerdTech.DR_Fashion.Views.BuyerPanel.UpdateBuyersDFrame(
                            (java.awt.Frame) parentWindow,
                            true,
                            this,
                            name, email, mobileNo, lanNo, coordinator, address,
                            companyName, brandName, brNo, brName, paymentMethod, duePayment
                    );
            dialog.setVisible(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    // End of variables declaration//GEN-END:variables
}
