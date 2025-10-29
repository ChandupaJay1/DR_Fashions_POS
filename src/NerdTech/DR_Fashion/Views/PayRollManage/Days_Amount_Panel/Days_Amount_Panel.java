/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage.Days_Amount_Panel;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Days_Amount_Panel extends javax.swing.JPanel {

    public Days_Amount_Panel() {
        initComponents();
        loadEmployeeData(); // Employee data load කරන්න
        setupDoubleClickListener(); // Double click listener add කරන්න
    }

    /**
     * Double click listener setup කිරීම
     */
    private void setupDoubleClickListener() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double click check කිරීම
                    openAddDaysAmountFrame();
                }
            }
        });
    }

    /**
     * Employee table එකෙන් දත්ත load කර JTable එකට set කරන method එක
     */
    private void loadEmployeeData() {
        try {
            // Database connection එක ගන්න
            Connection conn = DatabaseConnection.getConnection();

            // Updated SQL query with day table joins
            String query = "SELECT "
                    + "e.epf_no, "
                    + "e.name_with_initial as Name, "
                    + "s.section_name as Section, "
                    + "d.title as Designation, "
                    + "e.nic as NIC, "
                    + "COUNT(CASE WHEN a.status = 'Present' THEN 1 END) as Working_Day, "
                    + "COALESCE(day.sunday, 0) as Sunday, "
                    + "COALESCE(day.poya_day, 0) as Poya_Day, "
                    + "COALESCE(day.holiday, 0) as Holiday, "
                    + "(COUNT(CASE WHEN a.status = 'Present' THEN 1 END) + "
                    + " COALESCE(day.sunday, 0) + "
                    + " COALESCE(day.poya_day, 0) + "
                    + " COALESCE(day.holiday, 0)) as Total_Day, "
                    + "COALESCE(day.working_day_amount, 0) as Working_Day_Amount, "
                    + "COALESCE(day.sunday_amount, 0) as Sunday_Amount, "
                    + "COALESCE(day.poya_day_amount, 0) as Poya_Day_Amount, "
                    + "COALESCE(day.holiday_amount, 0) as Holiday_Amount, "
                    + "(COALESCE(day.working_day_amount, 0) + "
                    + " COALESCE(day.sunday_amount, 0) + "
                    + " COALESCE(day.poya_day_amount, 0) + "
                    + " COALESCE(day.holiday_amount, 0)) as Total_Day_Amount "
                    + "FROM employee e "
                    + "LEFT JOIN section s ON e.section_id = s.id "
                    + "LEFT JOIN designation d ON e.designation_id = d.id "
                    + "LEFT JOIN attendence a ON e.id = a.employee_id "
                    + "    AND YEAR(a.attendance_date) = YEAR(CURDATE()) "
                    + "    AND MONTH(a.attendance_date) = MONTH(CURDATE()) "
                    + "LEFT JOIN day ON a.id = day.attendence_id "
                    + "WHERE e.status = 'active' "
                    + "GROUP BY "
                    + "    e.id, e.epf_no, e.name_with_initial, s.section_name, d.title, e.nic, "
                    + "    day.sunday, day.poya_day, day.holiday, "
                    + "    day.working_day_amount, day.sunday_amount, "
                    + "    day.poya_day_amount, day.holiday_amount "
                    + "ORDER BY e.epf_no";

            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            // JTable එකේ model එක get කරන්න
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

            // පළමුව existing data ඉවත් කරන්න
            model.setRowCount(0);

            // ResultSet එකෙන් දත්ත read කර table එකට add කරන්න
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("epf_no"),
                    rs.getString("Name"),
                    rs.getString("Section"),
                    rs.getString("Designation"),
                    rs.getString("NIC"),
                    rs.getInt("Working_Day"), // Working Day
                    rs.getInt("Sunday"), // Sunday
                    rs.getInt("Poya_Day"), // Poya Day
                    rs.getInt("Holiday"), // Holiday
                    rs.getInt("Total_Day"), // Total Day
                    rs.getDouble("Working_Day_Amount"), // Working Day Amount
                    rs.getDouble("Sunday_Amount"), // Sunday Amount
                    rs.getDouble("Poya_Day_Amount"), // Poya Day Amount
                    rs.getDouble("Holiday_Amount"), // Holiday Amount
                    rs.getDouble("Total_Day_Amount") // Total Day Amount
                });
            }

            // Resources close කරන්න
            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading employee data: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Specific employee එකකට days and amounts add කිරීම
     */
    public void addDaysAndAmounts(int epfNo, int sundayDays, int poyaDays, int holidayDays,
            double workingDayAmount, double sundayAmount, double poyaDayAmount, double holidayAmount) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        // EPF No එකට අනුව row එක find කරන්න
        for (int i = 0; i < model.getRowCount(); i++) {
            int currentEpfNo = (int) model.getValueAt(i, 0);

            if (currentEpfNo == epfNo) {
                // Days set කරන්න
                model.setValueAt(sundayDays, i, 6); // Sunday
                model.setValueAt(poyaDays, i, 7); // Poya Day
                model.setValueAt(holidayDays, i, 8); // Holiday

                // Amounts set කරන්න
                model.setValueAt(workingDayAmount, i, 10); // Working Day Amount
                model.setValueAt(sundayAmount, i, 11); // Sunday Amount
                model.setValueAt(poyaDayAmount, i, 12); // Poya Day Amount
                model.setValueAt(holidayAmount, i, 13); // Holiday Amount

                // Total Day calculate කරන්න
                int workingDays = (int) model.getValueAt(i, 5);
                int totalDays = workingDays + sundayDays + poyaDays + holidayDays;
                model.setValueAt(totalDays, i, 9); // Total Day

                // Total Day Amount calculate කරන්න
                double totalAmount = workingDayAmount + sundayAmount + poyaDayAmount + holidayAmount;
                model.setValueAt(totalAmount, i, 14); // Total Day Amount

                // Database එකට save කිරීම
                saveToDatabase(epfNo, sundayDays, poyaDays, holidayDays,
                        workingDayAmount, sundayAmount, poyaDayAmount, holidayAmount);
                break;
            }
        }
    }

    /**
     * Database එකට දත්ත save කිරීම
     */
    private void saveToDatabase(int epfNo, int sundayDays, int poyaDays, int holidayDays,
            double workingDayAmount, double sundayAmount, double poyaDayAmount, double holidayAmount) {
        try {
            Connection conn = DatabaseConnection.getConnection();

            // පළමුව employee ID එක ගන්න
            String employeeQuery = "SELECT id FROM employee WHERE epf_no = ?";
            PreparedStatement empPst = conn.prepareStatement(employeeQuery);
            empPst.setInt(1, epfNo);
            ResultSet empRs = empPst.executeQuery();

            if (empRs.next()) {
                int employeeId = empRs.getInt("id");

                // Attendance record එක find කරන්න (current month)
                String attendanceQuery = "SELECT id FROM attendence WHERE employee_id = ? "
                        + "AND YEAR(attendance_date) = YEAR(CURDATE()) "
                        + "AND MONTH(attendance_date) = MONTH(CURDATE()) "
                        + "LIMIT 1";
                PreparedStatement attPst = conn.prepareStatement(attendanceQuery);
                attPst.setInt(1, employeeId);
                ResultSet attRs = attPst.executeQuery();

                int attendanceId;
                if (attRs.next()) {
                    attendanceId = attRs.getInt("id");
                } else {
                    // New attendance record එකක් create කරන්න
                    String insertAttendance = "INSERT INTO attendence (employee_id, attendance_date) VALUES (?, CURDATE())";
                    PreparedStatement insAttPst = conn.prepareStatement(insertAttendance, Statement.RETURN_GENERATED_KEYS);
                    insAttPst.setInt(1, employeeId);
                    insAttPst.executeUpdate();

                    ResultSet generatedKeys = insAttPst.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        attendanceId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to create attendance record");
                    }
                    insAttPst.close();
                }

                // Day table එක update කරන්න
                String dayQuery = "SELECT id FROM day WHERE attendence_id = ?";
                PreparedStatement dayPst = conn.prepareStatement(dayQuery);
                dayPst.setInt(1, attendanceId);
                ResultSet dayRs = dayPst.executeQuery();

                if (dayRs.next()) {
                    // Update existing record
                    int dayId = dayRs.getInt("id");
                    String updateDay = "UPDATE day SET sunday = ?, poya_day = ?, holiday = ?, "
                            + "working_day_amount = ?, sunday_amount = ?, poya_day_amount = ?, holiday_amount = ? "
                            + "WHERE id = ?";
                    PreparedStatement updatePst = conn.prepareStatement(updateDay);
                    updatePst.setInt(1, sundayDays);
                    updatePst.setInt(2, poyaDays);
                    updatePst.setInt(3, holidayDays);
                    updatePst.setDouble(4, workingDayAmount);
                    updatePst.setDouble(5, sundayAmount);
                    updatePst.setDouble(6, poyaDayAmount);
                    updatePst.setDouble(7, holidayAmount);
                    updatePst.setInt(8, dayId);
                    updatePst.executeUpdate();
                    updatePst.close();
                } else {
                    // Insert new record
                    String insertDay = "INSERT INTO day (sunday, poya_day, holiday, "
                            + "working_day_amount, sunday_amount, poya_day_amount, holiday_amount, attendence_id) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertPst = conn.prepareStatement(insertDay);
                    insertPst.setInt(1, sundayDays);
                    insertPst.setInt(2, poyaDays);
                    insertPst.setInt(3, holidayDays);
                    insertPst.setDouble(4, workingDayAmount);
                    insertPst.setDouble(5, sundayAmount);
                    insertPst.setDouble(6, poyaDayAmount);
                    insertPst.setDouble(7, holidayAmount);
                    insertPst.setInt(8, attendanceId);
                    insertPst.executeUpdate();
                    insertPst.close();
                }

                dayRs.close();
                dayPst.close();
                attRs.close();
                attPst.close();
            }

            empRs.close();
            empPst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error saving to database: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Add Days & Amount Details button click event සඳහා method එක
     */
    private void openAddDaysAmountFrame() {
        // Selected row එක check කරන්න
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Please select an employee first!",
                    "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Selected employee ගේ EPF No එක get කරන්න
        int epfNo = (int) jTable1.getValueAt(selectedRow, 0);
        String employeeName = (String) jTable1.getValueAt(selectedRow, 1);

        // Current values get කරන්න (edit කිරීම සඳහා)
        int sundayDays = (int) jTable1.getValueAt(selectedRow, 6);
        int poyaDays = (int) jTable1.getValueAt(selectedRow, 7);
        int holidayDays = (int) jTable1.getValueAt(selectedRow, 8);
        double workingDayAmount = (double) jTable1.getValueAt(selectedRow, 10);
        double sundayAmount = (double) jTable1.getValueAt(selectedRow, 11);
        double poyaDayAmount = (double) jTable1.getValueAt(selectedRow, 12);
        double holidayAmount = (double) jTable1.getValueAt(selectedRow, 13);

        // AddDaysAmountDFrame open කරන්න
        AddDaysAmountDFrame dialog = new AddDaysAmountDFrame(null, true, epfNo, employeeName, this,
                sundayDays, poyaDays, holidayDays, workingDayAmount, sundayAmount, poyaDayAmount, holidayAmount);
        dialog.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Days & Amount Details");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "EPF No", "Name", "Section", "Designation", "NIC", "Working Day", "Sunday", "Poya Day", "Holiday", "Total Day", "Working Day Amount", "Sunday Amount", "Poya Day Amount", "Holiday Amount", "Total Day Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 992, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1486, Short.MAX_VALUE))
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
                .addContainerGap(90, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
