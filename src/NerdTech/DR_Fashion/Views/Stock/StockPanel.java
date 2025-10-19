/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Stock;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
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
        tableModel.setRowCount(0); // table eka clear karanawa

        String query = "SELECT name, stock_qty, material, received_date, issued_date, total_issued, available_qty, unit_price FROM stock";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getString("name"),
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

        } catch (SQLException e) {
            e.printStackTrace(); // You can use logging or show dialog instead
        } catch (Exception e) {
            e.printStackTrace();
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
                "Name", "Stock Qty", "Materials", "Recieved Date", "Issued Date", "Total Issued", "Available Qty", "Unit Price"
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
            model.getColumnModel().getColumn(0).setPreferredWidth(150);
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
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(102, 102, 102)
                                .addComponent(jButton1)))
                        .addGap(0, 848, Short.MAX_VALUE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
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

        // Get row data
        String name = model.getValueAt(selectedRow, 0).toString();
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
                StockPanel.this, // pass panel
                name, stockQty, material, receivedDate, issuedDate, totalIssued, availableQty, unitPrice
        );
        dialog.setVisible(true);

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    // End of variables declaration//GEN-END:variables
}
