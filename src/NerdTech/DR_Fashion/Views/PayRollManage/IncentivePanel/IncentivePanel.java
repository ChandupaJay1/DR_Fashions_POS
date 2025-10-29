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

    // Load employee data only (no incentives initially)
    private void loadEmployeeData() {
        System.out.println("\n=== Loading Employee Data ===");
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

            int employeeCount = 0;
            while (rs.next()) {
                String epfNo = rs.getString("EPF No");

                Object[] row = {
                    epfNo,
                    rs.getString("Name"),
                    rs.getString("Section"),
                    rs.getString("Designation"),
                    rs.getString("NIC"),
                    "", "", "", "", "" // Empty incentive columns
                };
                model.addRow(row);
                employeeCount++;

                // Load incentive data for this employee (if exists)
                loadIncentiveForEmployee(epfNo, model.getRowCount() - 1);
            }

            System.out.println("Total employees loaded: " + employeeCount);
            System.out.println("=========================\n");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load incentive data for a specific employee
    private void loadIncentiveForEmployee(String epfNo, int rowIndex) {
        // වත්මන මාසයේ latest attendance record එක හොයාගන්නවා
        String query = """
            SELECT 
                COALESCE(i.attendance_incentive, 0) AS 'Attendance',
                COALESCE(i.grading_incentive, 0) AS 'Grading',
                COALESCE(i.production1_incentive, 0) AS 'Production1',
                COALESCE(i.production2_incentive, 0) AS 'Production2',
                COALESCE(i.total_incentive, 0) AS 'Total'
            FROM employee e
            LEFT JOIN attendence a ON e.id = a.employee_id 
                AND MONTH(a.attendance_date) = MONTH(CURRENT_DATE()) 
                AND YEAR(a.attendance_date) = YEAR(CURRENT_DATE())
            LEFT JOIN incentive i ON a.id = i.attendence_id
            WHERE e.epf_no = ?
            ORDER BY a.attendance_date DESC
            LIMIT 1
        """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, epfNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

                double attendance = rs.getDouble("Attendance");
                double grading = rs.getDouble("Grading");
                double production1 = rs.getDouble("Production1");
                double production2 = rs.getDouble("Production2");
                double total = rs.getDouble("Total");

                // Debug output (optional - remove if too much output)
                if (attendance > 0 || grading > 0 || production1 > 0 || production2 > 0) {
                    System.out.println("📊 EPF " + epfNo + " - Att: " + attendance
                            + ", Grad: " + grading
                            + ", Prod1: " + production1
                            + ", Prod2: " + production2
                            + ", Total: " + total);
                }

                model.setValueAt(formatIncentive(attendance), rowIndex, 5);
                model.setValueAt(formatIncentive(grading), rowIndex, 6);
                model.setValueAt(formatIncentive(production1), rowIndex, 7);
                model.setValueAt(formatIncentive(production2), rowIndex, 8);
                model.setValueAt(formatIncentive(total), rowIndex, 9);
            }
        } catch (Exception e) {
            System.err.println("❌ Error loading incentive for EPF " + epfNo + ": " + e.getMessage());
        }
    }

    // Format incentive values (0.00 වෙනුවට "" පෙන්වන්න)
    private String formatIncentive(double value) {
        if (value == 0) {
            return "";
        }
        return String.format("%.2f", value);
    }

    // Update incentives in table - now just refreshes the whole table
    public void updateIncentiveInTable(String epfNo, String attendance, String grading, String prod1, String prod2, String total) {
        System.out.println("\n=== 🔄 Refreshing Table After Save ===");
        loadEmployeeData(); // Refresh entire table from database
        System.out.println("✅ Table refreshed successfully!\n");
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
