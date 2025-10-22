/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Stock;

import NerdTech.DR_Fashion.Views.Dashboard;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.Accesories.AccesoriesPanel;
import NerdTech.DR_Fashion.Views.BuyerRegistrationPanel.RegistrationBuyerPanel;
import NerdTech.DR_Fashion.Views.Dashboard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class StockPanel extends javax.swing.JPanel {

    public StockPanel() {
        initComponents();
        loadStockData();
    }

    public void loadStockData() {
        DefaultTableModel tableModel = (DefaultTableModel) model.getModel();
        tableModel.setRowCount(0); // clear existing rows

        // ✅ Only load active records
        String query = "SELECT colour, stock_qty, material, received_date, issued_date, total_issued, available_qty, unit_price FROM stock WHERE status = 'active'";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getString("colour"),
                    rs.getInt("stock_qty"),
                    rs.getString("material"),
                    rs.getDate("received_date"),
                    rs.getDate("issued_date"),
                    rs.getInt("total_issued"),
                    rs.getInt("available_qty"),
                    "Rs. " + String.format("%.2f", rs.getDouble("unit_price"))
                };
                tableModel.addRow(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error loading stock data: " + e.getMessage());
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
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(0, 763));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Company Stock");

        model.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        model.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Colour", "Stock Qty", "Materials", "Recieved Date", "Issued Date", "Total Issued", "Available Qty", "Unit Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(model);
        if (model.getColumnModel().getColumnCount() > 0) {
            model.getColumnModel().getColumn(0).setResizable(false);
            model.getColumnModel().getColumn(1).setResizable(false);
            model.getColumnModel().getColumn(2).setResizable(false);
            model.getColumnModel().getColumn(3).setResizable(false);
            model.getColumnModel().getColumn(3).setPreferredWidth(100);
            model.getColumnModel().getColumn(4).setResizable(false);
            model.getColumnModel().getColumn(4).setPreferredWidth(100);
            model.getColumnModel().getColumn(5).setResizable(false);
            model.getColumnModel().getColumn(6).setResizable(false);
            model.getColumnModel().getColumn(7).setResizable(false);
            model.getColumnModel().getColumn(7).setPreferredWidth(100);
        }

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Update Stock");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Add Stock");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Register Buyer");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton5.setText("Go to Accesories");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton4.setText("Delete Stock");
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
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 1105, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(102, 102, 102)
                        .addComponent(jButton1)
                        .addGap(93, 93, 93)
                        .addComponent(jButton4)
                        .addGap(93, 93, 93)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)
                    .addComponent(jButton4))
                .addGap(30, 30, 30))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        AddStockDFrame dialog = new AddStockDFrame(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true,
                this
        );
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedRow = model.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(null, "Please select a row to update!");
            return;
        }

        // ✅ Get row data with correct column indices
        String colour = model.getValueAt(selectedRow, 0).toString();
        String stockQty = model.getValueAt(selectedRow, 1).toString();
        String material = model.getValueAt(selectedRow, 2).toString();
        String receivedDate = model.getValueAt(selectedRow, 3).toString();
        String issuedDate = model.getValueAt(selectedRow, 4).toString();
        String totalIssued = model.getValueAt(selectedRow, 5).toString();
        String availableQty = model.getValueAt(selectedRow, 6).toString();
        String unitPrice = model.getValueAt(selectedRow, 7).toString().replace("Rs. ", "");

        UpdateDFrame dialog = new UpdateDFrame(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(StockPanel.this),
                true,
                StockPanel.this,
                colour, stockQty, material, receivedDate, issuedDate, totalIssued, availableQty, unitPrice
        );
        dialog.setVisible(true);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        final Dashboard dashboard = (Dashboard) javax.swing.SwingUtilities.getWindowAncestor(this);

        if (dashboard != null) {
            dashboard.loadPanel("Registration Buyer", new Dashboard.PanelLoader() {
                public javax.swing.JPanel loadPanel() throws Exception {
                    return new RegistrationBuyerPanel(dashboard);
                }
            });
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error: Cannot access Dashboard!",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int selectedRow = model.getSelectedRow();

        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a row to delete!");
            return;
        }

        // Confirm delete
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this stock item?",
                "Confirm Delete",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirm != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        // ✅ FIX: Get colour from column 0 (first column) and use it in WHERE clause
        String stockColour = model.getValueAt(selectedRow, 0).toString();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // ✅ FIX: Use 'colour' column instead of 'name'
            String sql = "UPDATE stock SET status = 'deactivated' WHERE colour = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, stockColour);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Remove from UI table
                DefaultTableModel tableModel = (DefaultTableModel) model.getModel();
                tableModel.removeRow(selectedRow);

                javax.swing.JOptionPane.showMessageDialog(this,
                        "✅ Stock item successfully deactivated and removed from view!");
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "⚠️ No stock item found with the selected colour!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "❌ Error deactivating stock: " + e.getMessage());
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        final Dashboard dashboard = (Dashboard) javax.swing.SwingUtilities.getWindowAncestor(this);

        if (dashboard != null) {
            dashboard.loadPanel("Accessories", new Dashboard.PanelLoader() {
                public javax.swing.JPanel loadPanel() throws Exception {
                    return new AccesoriesPanel();
                }
            });
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error: Cannot access Dashboard!",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    // End of variables declaration//GEN-END:variables
}
