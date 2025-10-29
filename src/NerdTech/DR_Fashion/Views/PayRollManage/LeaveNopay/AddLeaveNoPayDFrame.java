/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage.LeaveNopay;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 *
 * @author MG_Pathum
 */
public class AddLeaveNoPayDFrame extends javax.swing.JDialog {

    private LeaveNoPayPanel parentPanel;
    private int selectedRow;

    public AddLeaveNoPayDFrame(java.awt.Frame parent, boolean modal, LeaveNoPayPanel parentPanel, int selectedRow) {
        super(parent, modal);
        this.parentPanel = parentPanel;
        this.selectedRow = selectedRow;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButtonSaveActionPerformed = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Add Leave & No Pay Details");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Leave");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Leave Amount");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jButtonSaveActionPerformed.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButtonSaveActionPerformed.setText("Save");
        jButtonSaveActionPerformed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformedActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Hour");

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Total");

        jTextField4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Day Amount");

        jTextField5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Hour Amount");

        jTextField6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel8.setText("Short Working Days");

        jTextField7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel9.setText("Short Working Days Amount");

        jTextField8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel10.setText("Vacation Day");

        jLabel11.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel11.setText("Total Day");

        jTextField9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel12.setText("Arreas");

        jTextField11.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel13.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel13.setText("Advance");

        jTextField12.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel2)
                                                .addComponent(jLabel6)
                                                .addComponent(jLabel8)
                                                .addComponent(jLabel12)
                                                .addComponent(jLabel10))
                                            .addGap(140, 140, 140)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jTextField3)
                                                .addComponent(jTextField1)
                                                .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                                                .addComponent(jTextField7)
                                                .addComponent(jTextField9)
                                                .addComponent(jTextField11))))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(65, 65, 65)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel11)
                                                .addComponent(jLabel9)
                                                .addComponent(jLabel13))
                                            .addGap(18, 18, 18))))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(407, 407, 407)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                            .addComponent(jTextField4)
                            .addComponent(jTextField6)
                            .addComponent(jTextField8)
                            .addComponent(jTextField10)
                            .addComponent(jTextField12))
                        .addGap(66, 66, 66))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonSaveActionPerformed)
                        .addGap(605, 605, 605))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jButtonSaveActionPerformed)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveActionPerformedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformedActionPerformed
        try {
            if (parentPanel != null && selectedRow >= 0) {
                String leaveStr = jTextField1.getText().trim();
                String leaveAmountStr = jTextField2.getText().trim();
                String hourStr = jTextField3.getText().trim();
                String totalStr = jTextField4.getText().trim();
                String dayAmountStr = jTextField5.getText().trim();
                String hourAmountStr = jTextField6.getText().trim();
                String shortWorkingDaysStr = jTextField7.getText().trim();
                String shortWorkingDaysAmountStr = jTextField8.getText().trim();
                String vacationDayStr = jTextField9.getText().trim();
                String totalDayStr = jTextField10.getText().trim();
                String arreasStr = jTextField11.getText().trim();
                String advanceStr = jTextField12.getText().trim();

                // Validation
                if (leaveStr.isEmpty() && hourStr.isEmpty() && shortWorkingDaysStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter leave days, hours or short working days", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (totalStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter total value", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Parse values
                int leaveDays = leaveStr.isEmpty() ? 0 : Integer.parseInt(leaveStr);
                int leaveAmount = leaveAmountStr.isEmpty() ? 0 : Integer.parseInt(leaveAmountStr);
                int leaveHours = hourStr.isEmpty() ? 0 : Integer.parseInt(hourStr);
                int totalValue = Integer.parseInt(totalStr);
                int dayAmount = dayAmountStr.isEmpty() ? 0 : Integer.parseInt(dayAmountStr);
                int hourAmount = hourAmountStr.isEmpty() ? 0 : Integer.parseInt(hourAmountStr);
                int shortWorkingDaysInput = shortWorkingDaysStr.isEmpty() ? 0 : Integer.parseInt(shortWorkingDaysStr);
                int shortWorkingDaysAmount = shortWorkingDaysAmountStr.isEmpty() ? 0 : Integer.parseInt(shortWorkingDaysAmountStr);
                int vacationDay = vacationDayStr.isEmpty() ? 0 : Integer.parseInt(vacationDayStr);
                int totalDay = totalDayStr.isEmpty() ? 0 : Integer.parseInt(totalDayStr);
                int arreas = arreasStr.isEmpty() ? 0 : Integer.parseInt(arreasStr);
                int advance = advanceStr.isEmpty() ? 0 : Integer.parseInt(advanceStr);

                // Short Working Days calculation (1 = 0.5 days)
                double shortWorkingDays = shortWorkingDaysInput * 0.5;

                // Total leave calculation (including hours and short working days)
                double totalLeaveRequested = leaveDays + (leaveHours / 8.0) + shortWorkingDays;

                // Get employee details from table
                int epfNo = Integer.parseInt(parentPanel.getJTable1().getValueAt(selectedRow, 0).toString());

                // Get current leave count
                Object currentLeaveCountObj = parentPanel.getJTable1().getValueAt(selectedRow, 5);
                if (currentLeaveCountObj == null || currentLeaveCountObj.toString().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No leave count found for selected employee", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double currentLeaveCount = Double.parseDouble(currentLeaveCountObj.toString());

                // Get current values from table
                Object currentLeaveDaysObj = parentPanel.getJTable1().getValueAt(selectedRow, 6);
                int currentLeaveDays = (currentLeaveDaysObj != null && !currentLeaveDaysObj.toString().isEmpty())
                        ? Integer.parseInt(currentLeaveDaysObj.toString()) : 0;

                Object currentLeaveAmountObj = parentPanel.getJTable1().getValueAt(selectedRow, 7);
                int currentLeaveAmount = (currentLeaveAmountObj != null && !currentLeaveAmountObj.toString().isEmpty())
                        ? Integer.parseInt(currentLeaveAmountObj.toString()) : 0;

                Object currentNoPayDaysObj = parentPanel.getJTable1().getValueAt(selectedRow, 8);
                double currentNoPayDays = (currentNoPayDaysObj != null && !currentNoPayDaysObj.toString().isEmpty())
                        ? Double.parseDouble(currentNoPayDaysObj.toString()) : 0;

                Object currentHoursObj = parentPanel.getJTable1().getValueAt(selectedRow, 9);
                int currentHours = (currentHoursObj != null && !currentHoursObj.toString().isEmpty())
                        ? Integer.parseInt(currentHoursObj.toString()) : 0;

                // ✅ FIXED LOGIC: Calculate new leave count and no-pay days
                double newLeaveCount;
                double additionalNoPayDays = 0;

                if (currentLeaveCount >= totalLeaveRequested) {
                    // Sufficient leave available
                    newLeaveCount = currentLeaveCount - totalLeaveRequested;
                } else {
                    // Insufficient leave - calculate no-pay days
                    if (currentLeaveCount > 0) {
                        additionalNoPayDays = totalLeaveRequested - currentLeaveCount;
                        newLeaveCount = 0;
                    } else {
                        additionalNoPayDays = totalLeaveRequested;
                        newLeaveCount = 0;
                    }
                }

                // Calculate new cumulative values
                int newLeaveDays = currentLeaveDays + leaveDays;
                int newLeaveAmount = currentLeaveAmount + leaveAmount;
                double newNoPayDays = currentNoPayDays + additionalNoPayDays;
                int newHours = currentHours + leaveHours;

                // Total Amount calculation
                int totalAmount = dayAmount + hourAmount + shortWorkingDaysAmount;

                // Database connection
                Connection connection = DatabaseConnection.getConnection();

                // Get or create attendance record
                int attendanceId = getOrCreateAttendance(connection, epfNo);

                // Save leave data to database
                saveLeaveDataToDatabase(connection, attendanceId,
                        newLeaveDays, newLeaveAmount, newHours, totalValue,
                        dayAmount, hourAmount, totalAmount,
                        shortWorkingDaysInput, shortWorkingDaysAmount,
                        vacationDay, totalDay, arreas, advance,
                        newLeaveCount, newNoPayDays);

                connection.close();

                // Create detailed message
                StringBuilder message = new StringBuilder();
                message.append("Leave details saved successfully!\n\n");
                message.append("Leave Requested: ").append(String.format("%.2f", totalLeaveRequested)).append(" days\n");
                message.append("Previous Leave Balance: ").append(String.format("%.2f", currentLeaveCount)).append(" days\n");
                message.append("New Leave Balance: ").append(String.format("%.2f", newLeaveCount)).append(" days\n");

                if (additionalNoPayDays > 0) {
                    message.append("\n⚠️ No-Pay Days Added: ").append(String.format("%.2f", additionalNoPayDays)).append(" days\n");
                    message.append("Total No-Pay Days: ").append(String.format("%.2f", newNoPayDays)).append(" days\n");
                }

                if (shortWorkingDaysInput > 0) {
                    message.append("\nShort Working Days: ").append(shortWorkingDaysInput)
                            .append(" (counted as ").append(String.format("%.2f", shortWorkingDays)).append(" days)");
                }

                JOptionPane.showMessageDialog(this, message.toString(), "Success", JOptionPane.INFORMATION_MESSAGE);

                // Refresh parent panel data from database
                parentPanel.refreshData();

                this.dispose();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonSaveActionPerformedActionPerformed

    private void updateEmployeeLeaveCount(Connection connection, int epfNo, double newLeaveCount) throws SQLException {
        String updateQuery = "UPDATE employee e "
                + "JOIN attendence a ON e.id = a.employee_id "
                + "JOIN `leave` l ON a.id = l.attendence_id "
                + "SET l.leave_count = ? "
                + "WHERE e.epf_no = ? AND a.attendance_date = CURDATE()";

        PreparedStatement pst = connection.prepareStatement(updateQuery);
        pst.setDouble(1, newLeaveCount);
        pst.setInt(2, epfNo);
        pst.executeUpdate();
        pst.close();
    }

    private int getOrCreateAttendance(Connection connection, int epfNo) throws SQLException {
        // First get employee_id from epf_no
        String employeeQuery = "SELECT id FROM employee WHERE epf_no = ?";
        PreparedStatement employeeStmt = connection.prepareStatement(employeeQuery);
        employeeStmt.setInt(1, epfNo);
        ResultSet employeeRs = employeeStmt.executeQuery();

        if (!employeeRs.next()) {
            throw new SQLException("Employee not found with EPF: " + epfNo);
        }

        int employeeId = employeeRs.getInt("id");
        employeeRs.close();
        employeeStmt.close();

        // Check if attendance record exists for today
        String attendanceQuery = "SELECT id FROM attendence WHERE employee_id = ? AND attendance_date = CURDATE()";
        PreparedStatement attendanceStmt = connection.prepareStatement(attendanceQuery);
        attendanceStmt.setInt(1, employeeId);
        ResultSet attendanceRs = attendanceStmt.executeQuery();

        int attendanceId;
        if (attendanceRs.next()) {
            attendanceId = attendanceRs.getInt("id");
        } else {
            // Create new attendance record
            String insertAttendance = "INSERT INTO attendence (employee_id, status, attendance_date, come_in, come_off) "
                    + "VALUES (?, 'Present', CURDATE(), '08:00:00', '17:00:00')";
            PreparedStatement insertStmt = connection.prepareStatement(insertAttendance, Statement.RETURN_GENERATED_KEYS);
            insertStmt.setInt(1, employeeId);
            insertStmt.executeUpdate();

            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                attendanceId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to create attendance record");
            }
            generatedKeys.close();
            insertStmt.close();
        }

        attendanceRs.close();
        attendanceStmt.close();
        return attendanceId;
    }

    private void saveLeaveDataToDatabase(Connection connection, int attendanceId,
            int leaveDays, int leaveAmount, int leaveHours, int totalValue,
            int dayAmount, int hourAmount, int totalAmount,
            int shortWorkingDays, int shortWorkingDaysAmount,
            int vacationDay, int totalDay, int arreas, int advance,
            double leaveCount, double noPayDays) throws SQLException {

        // Check if leave record exists
        String checkQuery = "SELECT id FROM `leave` WHERE attendence_id = ?";
        PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
        checkStmt.setInt(1, attendanceId);
        ResultSet checkRs = checkStmt.executeQuery();

        if (checkRs.next()) {
            // Update existing record - ADD to existing values
            String updateQuery = "UPDATE `leave` SET "
                    + "`leave_count` = ?, "
                    + "`leave` = ?, "
                    + "`leave_amount` = ?, "
                    + "`day` = ?, "
                    + "`hour` = ?, "
                    + "`total` = ?, "
                    + "`day_amount` = ?, "
                    + "`hour_amount` = ?, "
                    + "`total_amount` = ?, "
                    + "`short_working_day` = ?, "
                    + "`short_working_day_amount` = ?, "
                    + "`vacation_day` = ?, "
                    + "`total_day` = ?, "
                    + "`arreas` = ?, "
                    + "`advance` = ? "
                    + "WHERE attendence_id = ?";

            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
            updateStmt.setDouble(1, leaveCount);
            updateStmt.setInt(2, leaveDays);
            updateStmt.setInt(3, leaveAmount);
            updateStmt.setDouble(4, noPayDays);
            updateStmt.setInt(5, leaveHours);
            updateStmt.setInt(6, totalValue);
            updateStmt.setInt(7, dayAmount);
            updateStmt.setInt(8, hourAmount);
            updateStmt.setInt(9, totalAmount);
            updateStmt.setInt(10, shortWorkingDays);
            updateStmt.setInt(11, shortWorkingDaysAmount);
            updateStmt.setInt(12, vacationDay);
            updateStmt.setInt(13, totalDay);
            updateStmt.setInt(14, arreas);
            updateStmt.setInt(15, advance);
            updateStmt.setInt(16, attendanceId);

            updateStmt.executeUpdate();
            updateStmt.close();
        } else {
            // Insert new record
            String insertQuery = "INSERT INTO `leave` (attendence_id, `leave_count`, `leave`, `leave_amount`, `day`, `hour`, `total`, "
                    + "`day_amount`, `hour_amount`, `total_amount`, `short_working_day`, `short_working_day_amount`, "
                    + "`vacation_day`, `total_day`, `arreas`, `advance`) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
            insertStmt.setInt(1, attendanceId);
            insertStmt.setDouble(2, leaveCount);
            insertStmt.setInt(3, leaveDays);
            insertStmt.setInt(4, leaveAmount);
            insertStmt.setDouble(5, noPayDays);
            insertStmt.setInt(6, leaveHours);
            insertStmt.setInt(7, totalValue);
            insertStmt.setInt(8, dayAmount);
            insertStmt.setInt(9, hourAmount);
            insertStmt.setInt(10, totalAmount);
            insertStmt.setInt(11, shortWorkingDays);
            insertStmt.setInt(12, shortWorkingDaysAmount);
            insertStmt.setInt(13, vacationDay);
            insertStmt.setInt(14, totalDay);
            insertStmt.setInt(15, arreas);
            insertStmt.setInt(16, advance);

            insertStmt.executeUpdate();
            insertStmt.close();
        }

        checkRs.close();
        checkStmt.close();
    }

    private void updateTableData(double currentLeaveCount, double totalLeave,
            int leaveDays, int leaveAmount, int leaveHours, int totalValue,
            int dayAmount, int hourAmount, int totalAmount, int shortWorkingDaysInput,
            int shortWorkingDaysAmount, int vacationDay, int totalDay, int arreas, int advance) {

        // Get current day value
        Object currentDayObj = parentPanel.getJTable1().getValueAt(selectedRow, 8);
        double currentDay = 0;
        if (currentDayObj != null && !currentDayObj.toString().isEmpty()) {
            try {
                currentDay = Double.parseDouble(currentDayObj.toString());
            } catch (NumberFormatException e) {
                currentDay = 0;
            }
        }

        // Get current hour value
        Object currentHourObj = parentPanel.getJTable1().getValueAt(selectedRow, 9);
        int currentHour = 0;
        if (currentHourObj != null && !currentHourObj.toString().isEmpty()) {
            try {
                currentHour = Integer.parseInt(currentHourObj.toString());
            } catch (NumberFormatException e) {
                currentHour = 0;
            }
        }

        // Update leave count and no-pay days
        if (currentLeaveCount >= totalLeave) {
            double newLeaveCount = currentLeaveCount - totalLeave;
            parentPanel.getJTable1().setValueAt(String.format("%.2f", newLeaveCount), selectedRow, 5);
        } else {
            double remainingDays = totalLeave - currentLeaveCount;
            parentPanel.getJTable1().setValueAt("0.00", selectedRow, 5);
            double newDay = currentDay + remainingDays;
            parentPanel.getJTable1().setValueAt(String.format("%.2f", newDay), selectedRow, 8);
        }

        // Update other fields
        parentPanel.getJTable1().setValueAt(leaveDays, selectedRow, 6);
        parentPanel.getJTable1().setValueAt(leaveAmount, selectedRow, 7);
        int newHour = currentHour + leaveHours;
        parentPanel.getJTable1().setValueAt(newHour, selectedRow, 9);
        parentPanel.getJTable1().setValueAt(totalValue, selectedRow, 10);
        parentPanel.getJTable1().setValueAt(dayAmount, selectedRow, 11);
        parentPanel.getJTable1().setValueAt(hourAmount, selectedRow, 12);
        parentPanel.getJTable1().setValueAt(totalAmount, selectedRow, 13);
        parentPanel.getJTable1().setValueAt(shortWorkingDaysInput, selectedRow, 14);
        parentPanel.getJTable1().setValueAt(shortWorkingDaysAmount, selectedRow, 15);
        parentPanel.getJTable1().setValueAt(vacationDay, selectedRow, 16);
        parentPanel.getJTable1().setValueAt(totalDay, selectedRow, 17);
        parentPanel.getJTable1().setValueAt(arreas, selectedRow, 18);
        parentPanel.getJTable1().setValueAt(advance, selectedRow, 19);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddLeaveNoPayDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddLeaveNoPayDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddLeaveNoPayDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddLeaveNoPayDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddLeaveNoPayDFrame dialog = new AddLeaveNoPayDFrame(new javax.swing.JFrame(), true, null, -1);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSaveActionPerformed;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
