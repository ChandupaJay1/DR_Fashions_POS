/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Accesories;

import javax.swing.SwingUtilities;
import java.sql.Connection;
import static NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection;

/**
 *
 * @author MG_Pathum
 */
public class AccesoriesPanel extends javax.swing.JPanel {

    /**
     * Creates new form AccesoriesPanel
     */
    public AccesoriesPanel() {
        initComponents();
        loadAccessories();
    }

    public void loadAccessories() {
        try {
            Connection con = getConnection();
            String sql = "SELECT * FROM accesories";
            java.sql.Statement stmt = con.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);

            javax.swing.table.DefaultTableModel tableModel = (javax.swing.table.DefaultTableModel) model.getModel();
            tableModel.setRowCount(0); // Clear existing rows

            while (rs.next()) {
                String formattedPrice = String.format("Rs. %.2f", rs.getDouble("unit_price"));
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("order_no"),
                    rs.getString("colour_name"),
                    rs.getString("size"),
                    rs.getInt("stock_qty"),
                    rs.getString("uom"),
                    rs.getDate("received_date").toString(),
                    rs.getDate("issued_date").toString(),
                    rs.getInt("total_issued"),
                    rs.getInt("available_qty"),
                    formattedPrice
                };

                tableModel.addRow(row);
            }

            con.close();
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
        jButton4 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1237, 0));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Accesories");

        model.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        model.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "id", "Order Num", "Colour Name", "Size", "Stock Quantity", "UOM", "Recieve Date", "Issued Date", "Totall Issued", "Available Quantiy", "Unit Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
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
            model.getColumnModel().getColumn(3).setPreferredWidth(10);
            model.getColumnModel().getColumn(4).setResizable(false);
            model.getColumnModel().getColumn(4).setPreferredWidth(50);
            model.getColumnModel().getColumn(5).setResizable(false);
            model.getColumnModel().getColumn(5).setPreferredWidth(30);
            model.getColumnModel().getColumn(6).setResizable(false);
            model.getColumnModel().getColumn(6).setPreferredWidth(100);
            model.getColumnModel().getColumn(7).setResizable(false);
            model.getColumnModel().getColumn(7).setPreferredWidth(100);
            model.getColumnModel().getColumn(8).setResizable(false);
            model.getColumnModel().getColumn(9).setResizable(false);
            model.getColumnModel().getColumn(10).setResizable(false);
            model.getColumnModel().getColumn(10).setPreferredWidth(100);
        }

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton1.setText("Delete");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton4.setText("Update");
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1216, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(286, 286, 286)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        AddAccesoriesDFrame addAccesories = new AddAccesoriesDFrame((java.awt.Frame) parentWindow, true);
        addAccesories.setVisible(true);
        loadAccessories();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedRow = model.getSelectedRow();
        if (selectedRow >= 0) {
            // Get the ID of the selected row
            String id = model.getValueAt(selectedRow, 0).toString();

            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this record?",
                    "Confirm Delete",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                try {
                    Connection con = getConnection();
                    String sql = "DELETE FROM accesories WHERE id = ?";
                    java.sql.PreparedStatement pst = con.prepareStatement(sql);
                    pst.setInt(1, Integer.parseInt(id));
                    int deleted = pst.executeUpdate();

                    if (deleted > 0) {
                        javax.swing.JOptionPane.showMessageDialog(this, "Record deleted successfully.");
                        loadAccessories(); // Refresh table
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(this, "Failed to delete record.");
                    }

                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        UpdateAccesoriesDFrame updateAccesories = new UpdateAccesoriesDFrame((java.awt.Frame) parentWindow, true);

        String issueDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

        int selectedRow = model.getSelectedRow();
        if (selectedRow >= 0) {
            String id = model.getValueAt(selectedRow, 0).toString();
            String orderNo = model.getValueAt(selectedRow, 1).toString();
            String colourName = model.getValueAt(selectedRow, 2).toString();
            String size = model.getValueAt(selectedRow, 3).toString();

            // 🔑 Adjusted logic
            String stockQty = model.getValueAt(selectedRow, 9).toString();  // Available Qty → StockQty
            String uom = model.getValueAt(selectedRow, 5).toString();
            String totalIssued = "";   // always empty
            String availableQty = "";  // always empty
            String unitPrice = model.getValueAt(selectedRow, 10).toString().replace("Rs. ", "");

            updateAccesories.setData(
                    id, orderNo, colourName, size,
                    stockQty, uom, totalIssued, availableQty, unitPrice, issueDate
            );

            updateAccesories.setVisible(true);
            loadAccessories();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a row to update.");
        }
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    // End of variables declaration//GEN-END:variables
}
