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
    private int attendanceId = -1;
    private int employeeId = -1;

    public AddIncentiveDFrame(java.awt.Frame parent, boolean modal, String epfNo, String name, IncentivePanel parentPanel) {
        super(parent, modal);
        initComponents();
        this.epfNo = epfNo;
        this.name = name;
        this.parentPanel = parentPanel;

        // Set title with employee name
        setTitle("Add Incentive - " + name + " (" + epfNo + ")");

        loadEmployeeDetails();
        loadExistingIncentives();
    }

    // Load employee details and calculate attendance incentive
    private void loadEmployeeDetails() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Get employee ID and capacity
            String employeeSQL = "SELECT e.id, e.capacity_id, c.name AS capacity_name "
                    + "FROM employee e "
                    + "INNER JOIN capacity c ON e.capacity_id = c.id "
                    + "WHERE e.epf_no = ?";
            PreparedStatement psEmployee = conn.prepareStatement(employeeSQL);
            psEmployee.setString(1, epfNo);
            ResultSet rsEmployee = psEmployee.executeQuery();

            boolean isWorker = false;
            if (rsEmployee.next()) {
                employeeId = rsEmployee.getInt("id");
                String capacity = rsEmployee.getString("capacity_name");
                isWorker = "Worker".equalsIgnoreCase(capacity);

                System.out.println("=== Employee Details ===");
                System.out.println("EPF No: " + epfNo);
                System.out.println("Employee ID: " + employeeId);
                System.out.println("Capacity: " + capacity);
                System.out.println("Is Worker: " + isWorker);
            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ Employee not found!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the most recent attendance record for this employee
            String attendanceSQL = "SELECT id, attendance_date "
                    + "FROM attendence "
                    + "WHERE employee_id = ? "
                    + "ORDER BY attendance_date DESC LIMIT 1";
            PreparedStatement psAttendance = conn.prepareStatement(attendanceSQL);
            psAttendance.setInt(1, employeeId);
            ResultSet rsAttendance = psAttendance.executeQuery();

            if (rsAttendance.next()) {
                attendanceId = rsAttendance.getInt("id");
                System.out.println("Latest Attendance ID: " + attendanceId);
                System.out.println("Latest Attendance Date: " + rsAttendance.getDate("attendance_date"));
            } else {
                System.out.println("⚠️ No attendance record found");
            }

            // Calculate attendance incentive only for Workers
            if (isWorker) {
                String presentDaysSQL = """
                SELECT COUNT(*) AS present_days 
                FROM attendence 
                WHERE employee_id = ? 
                AND status = 'Present' 
                AND MONTH(attendance_date) = MONTH(CURRENT_DATE()) 
                AND YEAR(attendance_date) = YEAR(CURRENT_DATE())
            """;
                PreparedStatement psPresentDays = conn.prepareStatement(presentDaysSQL);
                psPresentDays.setInt(1, employeeId);
                ResultSet rsPresentDays = psPresentDays.executeQuery();

                if (rsPresentDays.next()) {
                    int presentDays = rsPresentDays.getInt("present_days");

                    // Attendance Incentive Logic:
                    // - Present days >= 26: 6000
                    // - Present days < 26: 0
                    attendanceIncentive = (presentDays >= 26) ? 6000.0 : 0.0;

                    System.out.println("=== Attendance Calculation ===");
                    System.out.println("Present Days (Current Month): " + presentDays);
                    System.out.println("Attendance Incentive: Rs. " + attendanceIncentive);
                    System.out.println("Status: " + (presentDays >= 26 ? "✅ Eligible (≥26 days)" : "❌ Not Eligible (<26 days)"));
                } else {
                    attendanceIncentive = 0.0;
                    System.out.println("⚠️ No present days found for current month");
                }
            } else {
                attendanceIncentive = 0.0;
                System.out.println("=== Attendance Calculation ===");
                System.out.println("Employee is not a Worker - Attendance Incentive: Rs. 0.00");
            }

            System.out.println("========================\n");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error loading employee details: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load existing incentive data if available
    private void loadExistingIncentives() {
        if (attendanceId == -1) {
            System.out.println("No attendance record found - creating new incentive");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = """
                SELECT grading_incentive, production1_incentive, production2_incentive 
                FROM incentive 
                WHERE attendence_id = ?
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, attendanceId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Load existing values into text fields
                String grading = rs.getString("grading_incentive");
                String prod1 = rs.getString("production1_incentive");
                String prod2 = rs.getString("production2_incentive");

                jTextFieldGrading.setText(grading != null && !grading.equals("0") && !grading.equals("0.0") ? grading : "");
                jTextFieldProd1.setText(prod1 != null && !prod1.equals("0") && !prod1.equals("0.0") ? prod1 : "");
                jTextFieldProd2.setText(prod2 != null && !prod2.equals("0") && !prod2.equals("0.0") ? prod2 : "");

                System.out.println("Loaded existing incentives: G=" + grading + ", P1=" + prod1 + ", P2=" + prod2);
            }
        } catch (Exception e) {
            // No existing record - fields remain empty
            System.out.println("No existing incentive record found");
        }
    }

    // Save incentive to DB
    private void saveIncentive() {
        if (attendanceId == -1) {
            JOptionPane.showMessageDialog(this,
                    "❌ No attendance record found for this employee!\nPlease mark attendance first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            double grading = jTextFieldGrading.getText().trim().isEmpty() ? 0 : Double.parseDouble(jTextFieldGrading.getText().trim());
            double prod1 = jTextFieldProd1.getText().trim().isEmpty() ? 0 : Double.parseDouble(jTextFieldProd1.getText().trim());
            double prod2 = jTextFieldProd2.getText().trim().isEmpty() ? 0 : Double.parseDouble(jTextFieldProd2.getText().trim());

            // Calculate total: Production I + Production II ONLY
            // Note: Attendance and Grading are saved separately but NOT included in total
            double total = prod1 + prod2;

            System.out.println("=== Saving Incentive ===");
            System.out.println("Attendance: " + attendanceIncentive + " (not in total)");
            System.out.println("Grading: " + grading + " (not in total)");
            System.out.println("Production 1: " + prod1);
            System.out.println("Production 2: " + prod2);
            System.out.println("Total Incentive (Prod1 + Prod2): " + total);
            System.out.println("========================");

            // Check if record exists
            String checkSQL = "SELECT id FROM incentive WHERE attendence_id = ?";
            PreparedStatement psCheck = conn.prepareStatement(checkSQL);
            psCheck.setInt(1, attendanceId);
            ResultSet rsCheck = psCheck.executeQuery();

            String sql;
            PreparedStatement ps;

            if (rsCheck.next()) {
                // UPDATE existing record
                sql = """
                UPDATE incentive 
                SET grading_incentive = ?, 
                    production1_incentive = ?, 
                    production2_incentive = ?, 
                    attendance_incentive = ?, 
                    total_incentive = ?
                WHERE attendence_id = ?
            """;
                ps = conn.prepareStatement(sql);
                ps.setDouble(1, grading);
                ps.setDouble(2, prod1);
                ps.setDouble(3, prod2);
                ps.setDouble(4, attendanceIncentive);
                ps.setDouble(5, total);  // Production 1 + Production 2 only
                ps.setInt(6, attendanceId);

                System.out.println("Updating existing record...");
            } else {
                // INSERT new record
                sql = """
                INSERT INTO incentive
                (attendence_id, grading_incentive, production1_incentive, production2_incentive, attendance_incentive, total_incentive)
                VALUES (?, ?, ?, ?, ?, ?)
            """;
                ps = conn.prepareStatement(sql);
                ps.setInt(1, attendanceId);
                ps.setDouble(2, grading);
                ps.setDouble(3, prod1);
                ps.setDouble(4, prod2);
                ps.setDouble(5, attendanceIncentive);
                ps.setDouble(6, total);  // Production 1 + Production 2 only

                System.out.println("Inserting new record...");
            }

            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("✅ Incentive saved successfully!");
                JOptionPane.showMessageDialog(this, "✅ Incentive saved successfully!");

                // Refresh parent panel table
                if (parentPanel != null) {
                    parentPanel.updateIncentiveInTable(epfNo, "", "", "", "", "");
                }

                dispose();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Please enter valid numbers only!",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error saving incentive: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
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
