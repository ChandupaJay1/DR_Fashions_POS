package NerdTech.DR_Fashion.Views.Stock.MachineRegistration.Part;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class PartPanel extends javax.swing.JPanel {

    public PartPanel() {
        initComponents();
        try {
            loadPartData();
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading part data: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshTable() {
        try {
            loadPartData();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error refreshing table: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadPartData() throws Exception {
        try {
            Connection conn = DatabaseConnection.getConnection();

            // ✅ FIXED: Use actual database column names (part_name, qty)
            String sql = "SELECT p.id, m.name as machine_name, p.part_name, p.description, "
                    + "p.price, p.qty, p.discount, p.amount, p.date "
                    + "FROM part p "
                    + "INNER JOIN machine m ON p.machine_id = m.id";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                String machineName = rs.getString("machine_name");
                String partName = rs.getString("part_name");
                String description = rs.getString("description");
                String price = rs.getString("price");
                String qty = rs.getString("qty");
                String discount = rs.getString("discount");
                String amount = rs.getString("amount");
                String date = rs.getString("date");

                model.addRow(new Object[]{
                    machineName,
                    partName,
                    description,
                    formatPrice(price),
                    qty,
                    formatPrice(discount),
                    formatPrice(amount),
                    date
                });
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e);
        }
    }

    private String formatPrice(String price) {
        if (price == null || price.trim().isEmpty()) {
            return "Rs. 0.00";
        }
        try {
            double value = Double.parseDouble(price);
            return String.format("Rs. %.2f", value);
        } catch (NumberFormatException e) {
            return "Rs. 0.00";
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

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Machine Parts");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Machine Name", "Part Name", "Description", "Price", "Qty", "Discount", "Amount", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(538, 538, 538)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 520, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        AddPart dialog = new AddPart(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true,
                this
        );
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String machineName = jTable1.getValueAt(selectedRow, 0).toString();
                String partName = jTable1.getValueAt(selectedRow, 1).toString();

                Connection conn = DatabaseConnection.getConnection();
                // ✅ FIXED: Use part_name and qty
                String sql = "SELECT p.id, p.part_name, p.description, p.price, p.qty, p.discount, p.amount, p.date "
                        + "FROM part p "
                        + "INNER JOIN machine m ON p.machine_id = m.id "
                        + "WHERE p.part_name = ? AND m.name = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, partName);
                pstmt.setString(2, machineName);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int partId = rs.getInt("id");
                    String description = rs.getString("description");
                    String price = rs.getString("price");
                    String qty = rs.getString("qty");
                    String discount = rs.getString("discount");
                    String amount = rs.getString("amount");
                    String date = rs.getString("date");

                    UpdatePart dialog = new UpdatePart(
                            (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                            true,
                            this,
                            partId, machineName, partName, description, price, qty, discount, amount, date
                    );
                    dialog.setVisible(true);
                }

                rs.close();
                pstmt.close();
                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error loading part details: " + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a part to update",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this part?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String machineName = jTable1.getValueAt(selectedRow, 0).toString();
                    String partName = jTable1.getValueAt(selectedRow, 1).toString();

                    Connection conn = DatabaseConnection.getConnection();

                    // ✅ FIXED: Use part_name
                    String selectSql = "SELECT p.id FROM part p "
                            + "INNER JOIN machine m ON p.machine_id = m.id "
                            + "WHERE p.part_name = ? AND m.name = ?";
                    PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                    selectStmt.setString(1, partName);
                    selectStmt.setString(2, machineName);
                    ResultSet rs = selectStmt.executeQuery();

                    if (rs.next()) {
                        int partId = rs.getInt("id");

                        String deleteSql = "DELETE FROM part WHERE id = ?";
                        PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                        deleteStmt.setInt(1, partId);

                        int rowsAffected = deleteStmt.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this,
                                    "Part deleted successfully!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                            loadPartData();
                        }

                        deleteStmt.close();
                    }

                    rs.close();
                    selectStmt.close();
                    conn.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Error deleting part: " + e.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a part to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed


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
