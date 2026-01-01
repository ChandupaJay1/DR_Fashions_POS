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
    private int attendanceId = -1;
    private int employeeId = -1;

    public AddIncentiveDFrame(java.awt.Frame parent, boolean modal, String epfNo, String name, IncentivePanel parentPanel) {
        super(parent, modal);
        initComponents();
        this.epfNo = epfNo;
        this.name = name;
        this.parentPanel = parentPanel;

        setTitle("Add Incentive - " + name + " (" + epfNo + ")");

        loadEmployeeDetails();
        loadExistingIncentives();
    }

    /**
     * Load employee details and get attendance ID
     */
    private void loadEmployeeDetails() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Get employee ID
            String employeeSQL = "SELECT id FROM employee WHERE epf_no = ?";
            PreparedStatement psEmployee = conn.prepareStatement(employeeSQL);
            psEmployee.setString(1, epfNo);
            ResultSet rsEmployee = psEmployee.executeQuery();

            if (rsEmployee.next()) {
                employeeId = rsEmployee.getInt("id");
                System.out.println("Employee ID: " + employeeId);
            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ Employee not found!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the most recent attendance record for current month
            String attendanceSQL = """
                SELECT id, attendance_date 
                FROM attendence 
                WHERE employee_id = ? 
                AND MONTH(attendance_date) = MONTH(CURRENT_DATE()) 
                AND YEAR(attendance_date) = YEAR(CURRENT_DATE())
                ORDER BY attendance_date DESC 
                LIMIT 1
            """;
            PreparedStatement psAttendance = conn.prepareStatement(attendanceSQL);
            psAttendance.setInt(1, employeeId);
            ResultSet rsAttendance = psAttendance.executeQuery();

            if (rsAttendance.next()) {
                attendanceId = rsAttendance.getInt("id");
                System.out.println("Latest Attendance ID (Current Month): " + attendanceId);
                System.out.println("Attendance Date: " + rsAttendance.getDate("attendance_date"));
            } else {
                System.out.println("⚠️ No attendance record found for current month");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error loading employee details: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Load existing incentive data if available
     */
    private void loadExistingIncentives() {
        if (attendanceId == -1) {
            System.out.println("No attendance record found - fields will remain empty");
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
                // Load existing values (excluding attendance_incentive - it's auto-calculated)
                String grading = rs.getString("grading_incentive");
                String prod1 = rs.getString("production1_incentive");
                String prod2 = rs.getString("production2_incentive");

                jTextFieldGrading.setText(grading != null && !grading.equals("0") && !grading.equals("0.0") ? grading : "");
                jTextFieldProd1.setText(prod1 != null && !prod1.equals("0") && !prod1.equals("0.0") ? prod1 : "");
                jTextFieldProd2.setText(prod2 != null && !prod2.equals("0") && !prod2.equals("0.0") ? prod2 : "");

                System.out.println("Loaded existing incentives: G=" + grading + ", P1=" + prod1 + ", P2=" + prod2);
            }
        } catch (Exception e) {
            System.out.println("No existing incentive record found");
        }
    }

    /**
     * Calculate attendance incentive based on business logic Returns 6000 if:
     * 1. Employee is a Worker AND 2. Present days in current month >= 26
     * Otherwise returns 0
     */
    private double calculateAttendanceIncentive() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // Check if employee is a Worker
            String capacitySQL = """
                SELECT c.name AS capacity_name
                FROM employee e
                INNER JOIN capacity c ON e.capacity_id = c.id
                WHERE e.id = ?
            """;
            PreparedStatement psCapacity = conn.prepareStatement(capacitySQL);
            psCapacity.setInt(1, employeeId);
            ResultSet rsCapacity = psCapacity.executeQuery();

            boolean isWorker = false;
            String capacity = "";

            if (rsCapacity.next()) {
                capacity = rsCapacity.getString("capacity_name");
                isWorker = "Worker".equalsIgnoreCase(capacity);
            }

            System.out.println("\n=== Attendance Incentive Calculation ===");
            System.out.println("Capacity: " + capacity);
            System.out.println("Is Worker: " + isWorker);

            // If not a Worker, return 0 immediately
            if (!isWorker) {
                System.out.println("Result: Rs. 0.00 (Not a Worker)");
                System.out.println("=======================================\n");
                return 0.0;
            }

            // Count present days in current month
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
                double incentive = (presentDays >= 26) ? 6000.0 : 0.0;

                System.out.println("Present Days (Current Month): " + presentDays);
                System.out.println("Eligible: " + (presentDays >= 26 ? "YES (≥26 days)" : "NO (<26 days)"));
                System.out.println("Attendance Incentive: Rs. " + incentive);
                System.out.println("=======================================\n");

                return incentive;
            }

            System.out.println("No attendance records found");
            System.out.println("Result: Rs. 0.00");
            System.out.println("=======================================\n");
            return 0.0;

        } catch (Exception e) {
            System.err.println("Error calculating attendance incentive: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * Save incentive data with auto-calculated attendance incentive
     */
    private void saveIncentive() {
        if (attendanceId == -1) {
            JOptionPane.showMessageDialog(this,
                    "❌ No attendance record found for this employee in current month!\nPlease mark attendance first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Get manual input values
            double grading = jTextFieldGrading.getText().trim().isEmpty() ? 0 : Double.parseDouble(jTextFieldGrading.getText().trim());
            double prod1 = jTextFieldProd1.getText().trim().isEmpty() ? 0 : Double.parseDouble(jTextFieldProd1.getText().trim());
            double prod2 = jTextFieldProd2.getText().trim().isEmpty() ? 0 : Double.parseDouble(jTextFieldProd2.getText().trim());

            // ⭐ AUTO-CALCULATE attendance incentive
            double attendanceIncentive = calculateAttendanceIncentive();

            // Calculate total (Production1 + Production2 only, as per your logic)
            double total = prod1 + prod2;

            System.out.println("=== Saving Incentive ===");
            System.out.println("Grading: Rs. " + grading);
            System.out.println("Production 1: Rs. " + prod1);
            System.out.println("Production 2: Rs. " + prod2);
            System.out.println("Attendance (Auto): Rs. " + attendanceIncentive);
            System.out.println("Total: Rs. " + total);
            System.out.println("========================");

            Connection conn = null;
            PreparedStatement psCheck = null;
            PreparedStatement ps = null;
            ResultSet rsCheck = null;

            try {
                conn = DatabaseConnection.getConnection();

                // Check if record exists
                String checkSQL = "SELECT id FROM incentive WHERE attendence_id = ?";
                psCheck = conn.prepareStatement(checkSQL);
                psCheck.setInt(1, attendanceId);
                rsCheck = psCheck.executeQuery();

                String sql;
                boolean isUpdate = rsCheck.next();

                if (isUpdate) {
                    // UPDATE existing record
                    sql = """
                        UPDATE incentive SET 
                            grading_incentive = ?, 
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
                    ps.setDouble(5, total);
                    ps.setInt(6, attendanceId);
                    System.out.println("📝 Updating existing record...");
                } else {
                    // INSERT new record
                    sql = """
                        INSERT INTO incentive 
                        (attendence_id, grading_incentive, production1_incentive, 
                         production2_incentive, attendance_incentive, total_incentive) 
                        VALUES (?, ?, ?, ?, ?, ?)
                    """;
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, attendanceId);
                    ps.setDouble(2, grading);
                    ps.setDouble(3, prod1);
                    ps.setDouble(4, prod2);
                    ps.setDouble(5, attendanceIncentive);
                    ps.setDouble(6, total);
                    System.out.println("➕ Inserting new record...");
                }

                int result = ps.executeUpdate();

                if (result > 0) {
                    System.out.println("✅ Database save successful!");

                    // Close resources immediately
                    if (rsCheck != null) {
                        rsCheck.close();
                    }
                    if (psCheck != null) {
                        psCheck.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }

                    System.out.println("🔒 Database connections closed");

                    // Show success message
                    JOptionPane.showMessageDialog(this,
                            "✅ Incentive saved successfully!\n\n"
                            + "Attendance Incentive: Rs. " + String.format("%.2f", attendanceIncentive));

                    // Close dialog - WindowListener will handle refresh
                    System.out.println("🚪 Disposing dialog...");
                    dispose();
                }

            } finally {
                // Safety cleanup
                try {
                    if (rsCheck != null) {
                        rsCheck.close();
                    }
                    if (psCheck != null) {
                        psCheck.close();
                    }
                    if (ps != null) {
                        ps.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Please enter valid numbers only!",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error: " + e.getMessage(),
                    "Error",
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
