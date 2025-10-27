/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage.IncentivePanel;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class IncentivePanel extends javax.swing.JPanel {

    public IncentivePanel() {
        initComponents();
        loadEmployeeData(); // Load employees when panel opens

        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && jTable1.getSelectedRow() != -1) {
                    int row = jTable1.getSelectedRow();
                    String epfNo = jTable1.getValueAt(row, 0).toString();
                    String name = jTable1.getValueAt(row, 1).toString();

                    AddIncentiveDFrame dialog = new AddIncentiveDFrame(null, true, epfNo, name, IncentivePanel.this);
                    dialog.setVisible(true);
                }
            }
        });
    }

    // Load employee data
    private void loadEmployeeData() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear table

        String query = """
            SELECT 
                e.epf_no AS 'EPF No',
                CONCAT(e.fname, ' ', e.surname) AS 'Name',
                s.section_name AS 'Section',
                d.title AS 'Designation',
                e.nic AS 'NIC'
            FROM employee e
            INNER JOIN section s ON e.section_id = s.id
            INNER JOIN designation d ON e.designation_id = d.id
            ORDER BY e.epf_no;
        """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getString("EPF No"),
                    rs.getString("Name"),
                    rs.getString("Section"),
                    rs.getString("Designation"),
                    rs.getString("NIC"),
                    "", "", "", "", "" // Incentive columns
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Update incentives in table
    // IncentivePanel.java එකේ
    public void updateIncentiveInTable(String epfNo, String attendance, String grading, String prod1, String prod2, String total) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(epfNo)) {
                model.setValueAt(attendance, i, 5); // Attendance Incentive
                model.setValueAt(grading, i, 6);    // Grading Incentive
                model.setValueAt(prod1, i, 7);      // Production I
                model.setValueAt(prod2, i, 8);      // Production II
                model.setValueAt(total, i, 9);      // Total Incentive
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Incentive Details");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "EPF No", "Name", "Section", "Designation", "NIC", "Attendance Incentive", "Grading Incentive", "Production Incentive I", "Production Incentive II", "Total Incentive"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 1049, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1477, Short.MAX_VALUE))
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
                .addContainerGap(179, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
