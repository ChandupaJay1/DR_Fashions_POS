/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage.IncentivePanel;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.*;
import java.sql.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author MG_Pathum
 */
public class AddIncentiveDFrame extends javax.swing.JDialog {

    private String epfNo;
    private String name;
    private IncentivePanel parentPanel;
    private double attendanceIncentive = 0;

    public AddIncentiveDFrame(java.awt.Frame parent, boolean modal, String epfNo, String name, IncentivePanel parentPanel) {
        super(parent, modal);
        initComponents();
        this.epfNo = epfNo;
        this.name = name;
        this.parentPanel = parentPanel;

        loadEmployeeDetails();
    }

    // Load attendance incentive based on employee capacity and attendance
    private void loadEmployeeDetails() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if employee is a Worker
            String capacitySQL = "SELECT c.name AS capacity_name FROM employee e INNER JOIN capacity c ON e.capacity_id = c.id WHERE e.epf_no = ?";
            PreparedStatement psCapacity = conn.prepareStatement(capacitySQL);
            psCapacity.setString(1, epfNo);
            ResultSet rsCapacity = psCapacity.executeQuery();

            boolean isWorker = false;
            if (rsCapacity.next()) {
                String capacity = rsCapacity.getString("capacity_name");
                isWorker = "Worker".equalsIgnoreCase(capacity);
            }

            // If Worker, count present days in current month
            if (isWorker) {
                String attendanceSQL = """
                    SELECT COUNT(*) AS present_days 
                    FROM attendence a 
                    INNER JOIN employee e ON a.employee_id = e.id 
                    WHERE e.epf_no = ? 
                    AND a.status = 'Present' 
                    AND MONTH(a.attendance_date) = MONTH(CURRENT_DATE()) 
                    AND YEAR(a.attendance_date) = YEAR(CURRENT_DATE())
                """;
                PreparedStatement psAttendance = conn.prepareStatement(attendanceSQL);
                psAttendance.setString(1, epfNo);
                ResultSet rsAttendance = psAttendance.executeQuery();

                if (rsAttendance.next()) {
                    int presentDays = rsAttendance.getInt("present_days");
                    // If present days >= 26, give 6000 incentive, else 0
                    attendanceIncentive = (presentDays >= 26) ? 6000 : 0;
                }
            } else {
                attendanceIncentive = 0;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading employee details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Save incentive to DB and update table
    private void saveIncentive() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            double grading = jTextFieldGrading.getText().isEmpty() ? 0 : Double.parseDouble(jTextFieldGrading.getText());
            double prod1 = jTextFieldProd1.getText().isEmpty() ? 0 : Double.parseDouble(jTextFieldProd1.getText());
            double prod2 = jTextFieldProd2.getText().isEmpty() ? 0 : Double.parseDouble(jTextFieldProd2.getText());

            // Calculate total: Production I + Production II only
            double total = prod1 + prod2;

            String sql = """
                INSERT INTO incentive
                (attendence_id, grading_incentive, production1_incentive, production2_incentive, attendance_incentive, total_incentive)
                VALUES (
                    (SELECT a.id FROM attendence a INNER JOIN employee e ON a.employee_id = e.id WHERE e.epf_no = ? LIMIT 1),
                    ?, ?, ?, ?, ?
                )
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, epfNo);
            ps.setDouble(2, grading);
            ps.setDouble(3, prod1);
            ps.setDouble(4, prod2);
            ps.setDouble(5, attendanceIncentive);
            ps.setDouble(6, total);

            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "✅ Incentive saved successfully!");
                if (parentPanel != null) {
                    // Update table with all values including auto-calculated total
                    parentPanel.updateIncentiveInTable(
                            epfNo,
                            String.valueOf(attendanceIncentive),
                            String.valueOf(grading),
                            String.valueOf(prod1),
                            String.valueOf(prod2),
                            String.valueOf(total) // Auto-calculated total
                    );
                }
                dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error saving incentive: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldGrading = new javax.swing.JTextField();
        jTextFieldProd1 = new javax.swing.JTextField();
        jTextFieldProd2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Add Incentive Details");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Grading Incentive");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Production Incentive I");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Production Incentive II");

        jTextFieldGrading.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextFieldProd1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextFieldProd2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(115, 115, 115)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldProd1)
                                    .addComponent(jTextFieldProd2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextFieldGrading, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(281, 281, 281)
                        .addComponent(jButton1)))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldGrading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldProd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldProd2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveIncentive();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextFieldGrading;
    private javax.swing.JTextField jTextFieldProd1;
    private javax.swing.JTextField jTextFieldProd2;
    // End of variables declaration//GEN-END:variables
}
