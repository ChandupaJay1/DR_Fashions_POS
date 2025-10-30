package NerdTech.DR_Fashion.Views.Stock.MachineRegistration;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.Dashboard;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MachineRegistrationPanel extends javax.swing.JPanel {

    public MachineRegistrationPanel() {
        initComponents();
        try {
            loadMachineData();
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading machine data: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    // Make this method public so AddMachine can call it
    public void loadMachineData() throws Exception {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT id, name, model_no, serial_no, remark FROM machine";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            // ✅ FIX: Use jTable1's model directly
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); // Clear existing data

            // Add data to table
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String modelNo = rs.getString("model_no");
                String serialNo = rs.getString("serial_no");
                String remark = rs.getString("remark");

                model.addRow(new Object[]{name, modelNo, serialNo, remark});
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e);
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
        jLabel1.setText("Machine Registration");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Name", "Model No", "Serial No", "Remark"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true
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
        jButton4.setText("Part");
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
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(290, 290, 290)
                        .addComponent(jButton2)
                        .addGap(371, 371, 371)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 371, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Add new machine functionality
        AddMachine dialog = new AddMachine(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                true,
                this
        );
        dialog.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Update machine functionality
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            // Get data from selected row
            String name = jTable1.getValueAt(selectedRow, 0).toString();
            String modelNo = jTable1.getValueAt(selectedRow, 1).toString();
            String serialNo = jTable1.getValueAt(selectedRow, 2).toString();
            String remark = jTable1.getValueAt(selectedRow, 3).toString();

            UpdateMachine dialog = new UpdateMachine(
                    (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this),
                    true,
                    this,
                    name, modelNo, serialNo, remark
            );
            dialog.setVisible(true);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Please select a machine to update",
                    "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Delete machine functionality
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this machine?",
                    "Confirm Delete",
                    javax.swing.JOptionPane.YES_NO_OPTION);

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                try {
                    // Get the machine name from selected row
                    String machineName = jTable1.getValueAt(selectedRow, 0).toString();

                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM machine WHERE name = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, machineName);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        // Remove from table
                        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                        model.removeRow(selectedRow);

                        javax.swing.JOptionPane.showMessageDialog(this,
                                "Machine deleted successfully!",
                                "Success",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }

                    pstmt.close();
                    conn.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Error deleting machine: " + e.getMessage(),
                            "Delete Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Please select a machine to delete",
                    "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        final Dashboard dashboard = (Dashboard) javax.swing.SwingUtilities.getWindowAncestor(this);

    if (dashboard != null) {
        dashboard.loadPanel("Machine Parts", new Dashboard.PanelLoader() {
            public javax.swing.JPanel loadPanel() throws Exception {
                return new NerdTech.DR_Fashion.Views.Stock.MachineRegistration.Part.PartPanel();
            }
        });
    } else {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Error: Cannot access Dashboard!",
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton4ActionPerformed


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
