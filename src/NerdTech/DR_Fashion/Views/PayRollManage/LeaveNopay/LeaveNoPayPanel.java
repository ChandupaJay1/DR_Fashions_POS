/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage.LeaveNopay;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class LeaveNoPayPanel extends javax.swing.JPanel {

    public LeaveNoPayPanel() {
        initComponents();
        loadEmployeeData();
        setupTableListener();
    }

    private void loadEmployeeData() {
        try {
            Connection connection = DatabaseConnection.getConnection();

            // ✅ CORRECTED QUERY - Leave data with current date
            String query = "SELECT "
                    + "e.epf_no, "
                    + "e.name_with_initial, "
                    + "s.section_name, "
                    + "d.title, "
                    + "e.nic, "
                    + "COALESCE(l.leave_count, "
                    + "    CASE "
                    + "        WHEN MONTH(e.joined_date) BETWEEN 1 AND 3 THEN 14 "
                    + "        WHEN MONTH(e.joined_date) BETWEEN 4 AND 6 THEN 10 "
                    + "        WHEN MONTH(e.joined_date) BETWEEN 7 AND 9 THEN 7 "
                    + "        WHEN MONTH(e.joined_date) BETWEEN 10 AND 12 THEN 4 "
                    + "        ELSE 0 "
                    + "    END) AS leave_count, "
                    + "COALESCE(l.`leave`, '') as leave_days, "
                    + "COALESCE(l.leave_amount, '') as leave_amount, "
                    + "COALESCE(l.day, '') as day, "
                    + "COALESCE(l.hour, '') as hour, "
                    + "COALESCE(l.total, '') as total, "
                    + "COALESCE(l.day_amount, '') as day_amount, "
                    + "COALESCE(l.hour_amount, '') as hour_amount, "
                    + "COALESCE(l.total_amount, '') as total_amount, "
                    + "COALESCE(l.short_working_day, '') as short_working_days, "
                    + "COALESCE(l.short_working_day_amount, '') as short_working_days_amount, "
                    + "COALESCE(l.vacation_day, '') as vacation_day, "
                    + "COALESCE(l.total_day, '') as total_day, "
                    + "COALESCE(l.arreas, '') as arreas, "
                    + "COALESCE(l.advance, '') as advance "
                    + "FROM employee e "
                    + "JOIN section s ON e.section_id = s.id "
                    + "JOIN designation d ON e.designation_id = d.id "
                    + "LEFT JOIN attendence a ON e.id = a.employee_id AND a.attendance_date = CURDATE() "
                    + "LEFT JOIN `leave` l ON a.id = l.attendence_id "
                    + "WHERE e.status = 'Active'";

            PreparedStatement pst = connection.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); // Clear existing data

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("epf_no"),
                    rs.getString("name_with_initial"),
                    rs.getString("section_name"),
                    rs.getString("title"),
                    rs.getString("nic"),
                    rs.getString("leave_count"),
                    rs.getString("leave_days"),
                    rs.getString("leave_amount"),
                    rs.getString("day"),
                    rs.getString("hour"),
                    rs.getString("total"),
                    rs.getString("day_amount"),
                    rs.getString("hour_amount"),
                    rs.getString("total_amount"),
                    rs.getString("short_working_days"),
                    rs.getString("short_working_days_amount"),
                    rs.getString("vacation_day"),
                    rs.getString("total_day"),
                    rs.getString("arreas"),
                    rs.getString("advance")
                };
                model.addRow(row);
            }

            rs.close();
            pst.close();
            connection.close();

            // ✅ Refresh table UI
            jTable1.revalidate();
            jTable1.repaint();

            System.out.println("Data loaded successfully with " + model.getRowCount() + " rows");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    // ✅ NEW METHOD: refreshData() - Database එකෙන් data reload කරන්න
    public void refreshData() {
        System.out.println("Refreshing data from database...");
        loadEmployeeData();
    }

    private void setupTableListener() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double click
                    int row = jTable1.getSelectedRow();
                    if (row >= 0) {
                        openAddLeaveDialog(row);
                    }
                }
            }
        });
    }

    private void openAddLeaveDialog(int selectedRow) {
        try {
            // Find the parent frame
            java.awt.Component parent = this;
            while (parent != null && !(parent instanceof java.awt.Frame)) {
                parent = parent.getParent();
            }

            java.awt.Frame parentFrame = (java.awt.Frame) parent;

            AddLeaveNoPayDFrame dialog = new AddLeaveNoPayDFrame(
                    parentFrame,
                    true,
                    this,
                    selectedRow
            );
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening dialog: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ✅ JTable එක access කිරීමට getter method එක
    public javax.swing.JTable getJTable1() {
        return jTable1;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Leave & No Pay Details");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "EPF No", "Name", "Section", "Designation", "NIC", "Leave Count", "Leave", "Leave Amount", "Day", "Hour", "Total", "Day Amount", "Hour Amount", "Total Amount", "Short Working Days", "Short Working Days Amount", "Vacation Day", "Total Day", "Arreas", "Advance"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 893, Short.MAX_VALUE)))
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
                .addContainerGap(124, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
