package NerdTech.DR_Fashion.Views.PayRollManage.OverTime;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OverTimePanel extends javax.swing.JPanel {

    private int selectedMonth;
    private int selectedYear;

    public OverTimePanel() throws Exception {
        initComponents();
        setupMonthYearComboBoxes();
        checkDatabaseConnection(); // Add this line
        loadEmployeeData();
        setupTableListeners();
        setupDoubleClickListener();

        System.out.println("🎯 OverTimePanel initialized successfully!");
    }

    /**
     * Month සහ Year combo boxes setup කරන්න
     */
    private void setupMonthYearComboBoxes() {
        // Get current month and year
        java.util.Calendar cal = java.util.Calendar.getInstance();
        selectedMonth = cal.get(java.util.Calendar.MONTH) + 1;
        selectedYear = cal.get(java.util.Calendar.YEAR);

        // Month names
        String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

        // Populate month combo box
        monthComboBox.removeAllItems();
        for (String month : months) {
            monthComboBox.addItem(month);
        }
        monthComboBox.setSelectedIndex(selectedMonth - 1);

        // Populate year combo box (last 5 years to next 2 years)
        yearComboBox.removeAllItems();
        for (int year = selectedYear - 5; year <= selectedYear + 2; year++) {
            yearComboBox.addItem(String.valueOf(year));
        }
        yearComboBox.setSelectedItem(String.valueOf(selectedYear));

        // Add listeners
        monthComboBox.addActionListener(e -> {
            selectedMonth = monthComboBox.getSelectedIndex() + 1;
            try {
                loadEmployeeData();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        yearComboBox.addActionListener(e -> {
            selectedYear = Integer.parseInt(yearComboBox.getSelectedItem().toString());
            try {
                loadEmployeeData();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * ⭐ Double click listener එක setup කරන්න - FIXED VERSION
     */
    private void setupDoubleClickListener() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double click
                    System.out.println("🖱️ Double click detected!");
                    int row = jTable1.getSelectedRow();
                    if (row != -1) {
                        System.out.println("✅ Opening dialog for row: " + row);
                        openAddOverTimeDialog();
                    } else {
                        System.out.println("❌ No row selected!");
                        JOptionPane.showMessageDialog(OverTimePanel.this,
                                "Please select an employee first!",
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        System.out.println("✅ Double click listener setup complete!");
    }

    /**
     * Employee table එකෙන් දත්ත load කරන්න method එක
     */
    private void loadEmployeeData() throws Exception {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT e.epf_no, e.name_with_initial, s.section_name, d.title, e.nic, e.id as employee_id "
                    + "FROM employee e "
                    + "LEFT JOIN section s ON e.section_id = s.id "
                    + "LEFT JOIN designation d ON e.designation_id = d.id "
                    + "WHERE e.status = 'Active'";

            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            // Get the table model
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); // Clear existing data

            System.out.println("📊 Loading employee data for " + selectedMonth + "/" + selectedYear);

            // Add data to table
            while (rs.next()) {
                // Calculate normal overtime from attendance
                double normalOvertime = calculateNormalOvertime(rs.getInt("employee_id"));

                model.addRow(new Object[]{
                    rs.getInt("epf_no"),
                    rs.getString("name_with_initial"),
                    rs.getString("section_name"),
                    rs.getString("title"),
                    rs.getString("nic"),
                    normalOvertime, // Normal (calculated from attendance)
                    0.0, // Extra (initially 0)
                    0.0, // Trible (initially 0)
                    normalOvertime, // Total (initially same as normal)
                    0.0, // Normal Amount (initially 0)
                    0.0, // Extra Amount (initially 0)
                    0.0, // Trible Amount (initially 0)
                    0.0 // Total Amount (initially 0)
                });
            }

            System.out.println("✅ Loaded " + model.getRowCount() + " employees");

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Attendance table එකෙන් normal overtime calculate කරන්න
     */
    private double calculateNormalOvertime(int employeeId) {
        double totalOvertime = 0.0;
        try {
            Connection conn = DatabaseConnection.getConnection();

            String query = "SELECT id, attendance_date, come_off FROM attendence "
                    + "WHERE employee_id = ? "
                    + "AND come_off IS NOT NULL "
                    + "AND MONTH(attendance_date) = ? AND YEAR(attendance_date) = ?";

            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, employeeId);
            pst.setInt(2, selectedMonth);
            pst.setInt(3, selectedYear);
            ResultSet rs = pst.executeQuery();

            // Define time boundaries
            Time morningBoundary = Time.valueOf("07:45:00"); // 7:45 AM
            Time eveningStart = Time.valueOf("17:30:00");    // 5:30 PM

            while (rs.next()) {
                Time comeOff = rs.getTime("come_off");

                if (comeOff != null) {
                    // Evening/Night overtime (after 5:30 PM)
                    if (comeOff.after(eveningStart)) {
                        double overtimeHours = (comeOff.getTime() - eveningStart.getTime()) / (1000.0 * 60 * 60);
                        totalOvertime += overtimeHours;
                    } // Night shift overtime (before/at 7:45 AM)
                    else if (comeOff.before(morningBoundary) || comeOff.equals(morningBoundary)) {
                        double hoursBeforeMidnight = 6.5;

                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        cal.setTime(comeOff);
                        int hours = cal.get(java.util.Calendar.HOUR_OF_DAY);
                        int minutes = cal.get(java.util.Calendar.MINUTE);
                        int seconds = cal.get(java.util.Calendar.SECOND);

                        double hoursAfterMidnight = hours + (minutes / 60.0) + (seconds / 3600.0);
                        double totalNightHours = hoursBeforeMidnight + hoursAfterMidnight;
                        totalOvertime += totalNightHours;
                    }
                }
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Math.round(totalOvertime * 100.0) / 100.0;
    }

    /**
     * Table listeners setup කරන්න
     */
    private void setupTableListeners() {
        jTable1.getModel().addTableModelListener(e -> {
            if (e.getColumn() >= 5 && e.getColumn() <= 11) {
                calculateTotals(e.getFirstRow());
            }
        });
        System.out.println("✅ Table listeners setup complete!");
    }

    /**
     * ⭐ Totals calculate කරන්න - FIXED VERSION (No Infinite Loop)
     */
    private void calculateTotals(int row) {
        try {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

            // ⭐⭐ CRITICAL FIX: Remove table listener temporarily to prevent infinite loop
            javax.swing.event.TableModelListener[] listeners = model.getTableModelListeners();
            for (javax.swing.event.TableModelListener listener : listeners) {
                model.removeTableModelListener(listener);
            }

            System.out.println("\n🧮 Calculating totals for row: " + row);

            // Get values from table
            double normal = getDoubleValue(model.getValueAt(row, 5));
            double extra = getDoubleValue(model.getValueAt(row, 6));
            double trible = getDoubleValue(model.getValueAt(row, 7));
            double normalAmount = getDoubleValue(model.getValueAt(row, 9));
            double extraAmount = getDoubleValue(model.getValueAt(row, 10));
            double tribleAmount = getDoubleValue(model.getValueAt(row, 11));

            System.out.println("  Normal: " + normal + " h, Extra: " + extra + " h, Trible: " + trible + " h");
            System.out.println("  Normal Amt: Rs." + normalAmount + ", Extra Amt: Rs." + extraAmount + ", Trible Amt: Rs." + tribleAmount);

            // Calculate totals
            double totalHours = normal + extra + trible;
            double totalAmount = normalAmount + extraAmount + tribleAmount;

            System.out.println("  ✅ Total Hours: " + totalHours + " h");
            System.out.println("  ✅ Total Amount: Rs." + totalAmount);

            // Update table
            model.setValueAt(totalHours, row, 8); // Total hours (Column 8)
            model.setValueAt(totalAmount, row, 12); // Total amount (Column 12)

            System.out.println("✅ Totals updated in table!\n");

            // ⭐⭐ CRITICAL FIX: Add listeners back
            for (javax.swing.event.TableModelListener listener : listeners) {
                model.addTableModelListener(listener);
            }

        } catch (Exception ex) {
            System.err.println("❌ Error calculating totals: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * String value එක double එකකට convert කරන්න
     */
    private double getDoubleValue(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * ⭐ Add OverTime dialog එක open කරන්න
     */
    private void openAddOverTimeDialog() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee first!", "Warning", JOptionPane.WARNING_MESSAGE);
            System.out.println("⚠️ No row selected!");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        String epfNo = model.getValueAt(selectedRow, 0).toString();
        String name = model.getValueAt(selectedRow, 1).toString();

        System.out.println("📝 Opening dialog for: " + name + " (EPF: " + epfNo + ")");

        // Get current values from table
        double currentExtra = getDoubleValue(model.getValueAt(selectedRow, 6));
        double currentTrible = getDoubleValue(model.getValueAt(selectedRow, 7));
        double currentNormalAmount = getDoubleValue(model.getValueAt(selectedRow, 9));
        double currentExtraAmount = getDoubleValue(model.getValueAt(selectedRow, 10));
        double currentTribleAmount = getDoubleValue(model.getValueAt(selectedRow, 11));

        System.out.println("  Current Extra: " + currentExtra + ", Trible: " + currentTrible);
        System.out.println("  Current Amounts: Normal=" + currentNormalAmount + ", Extra=" + currentExtraAmount + ", Trible=" + currentTribleAmount);

        AddOverTime dialog = new AddOverTime(
                (java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                true,
                epfNo,
                name,
                this,
                selectedRow,
                currentExtra,
                currentTrible,
                currentNormalAmount,
                currentExtraAmount,
                currentTribleAmount
        );
        dialog.setVisible(true);
    }

    /**
     * ⭐⭐⭐ Update table with overtime data - MOST IMPORTANT METHOD!
     */
    public void updateOvertimeData(int row, double extra, double trible, double normalAmount, double extraAmount, double tribleAmount) {
        System.out.println("\n💾 UPDATING OVERTIME DATA");
        System.out.println("================================");
        System.out.println("Row: " + row);
        System.out.println("Extra: " + extra + " h, Trible: " + trible + " h");
        System.out.println("Normal Amount: Rs." + normalAmount);
        System.out.println("Extra Amount: Rs." + extraAmount);
        System.out.println("Trible Amount: Rs." + tribleAmount);
        System.out.println("================================");

        try {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

            // Get employee ID from the table
            int epfNo = Integer.parseInt(model.getValueAt(row, 0).toString());
            int employeeId = getEmployeeIdByEPF(epfNo);

            // Set values in table
            model.setValueAt(extra, row, 6);           // Column 6: Extra
            model.setValueAt(trible, row, 7);          // Column 7: Trible
            model.setValueAt(normalAmount, row, 9);    // Column 9: Normal Amount
            model.setValueAt(extraAmount, row, 10);    // Column 10: Extra Amount
            model.setValueAt(tribleAmount, row, 11);   // Column 11: Trible Amount

            System.out.println("✅ Values set in table columns");

            // Save to database
            if (employeeId != -1) {
                saveOvertimeToDatabase(employeeId, extra, trible, normalAmount, extraAmount, tribleAmount);
            } else {
                System.err.println("❌ Could not find employee ID for EPF: " + epfNo);
                JOptionPane.showMessageDialog(this,
                        "Error: Could not find employee ID for EPF: " + epfNo,
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            // Trigger total calculation
            calculateTotals(row);

            System.out.println("🎉 Update complete!\n");

        } catch (Exception ex) {
            System.err.println("❌ ERROR updating overtime data: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error updating data: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * ⭐⭐⭐ Save overtime data to database - NEW METHOD
     */
    public void saveOvertimeToDatabase(int employeeId, double extra, double trible,
            double normalAmount, double extraAmount, double tribleAmount) throws Exception {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DatabaseConnection.getConnection();

            // First, get attendance_id for this employee and current month/year
            String attendanceQuery = "SELECT id FROM attendence WHERE employee_id = ? "
                    + "AND MONTH(attendance_date) = ? AND YEAR(attendance_date) = ? "
                    + "LIMIT 1";

            pst = conn.prepareStatement(attendanceQuery);
            pst.setInt(1, employeeId);
            pst.setInt(2, selectedMonth);
            pst.setInt(3, selectedYear);

            ResultSet rs = pst.executeQuery();

            int attendanceId = -1;
            if (rs.next()) {
                attendanceId = rs.getInt("id");
            }
            rs.close();

            // If no attendance record found, create one
            if (attendanceId == -1) {
                attendanceId = createAttendanceRecord(employeeId);
            }

            // Calculate totals
            double totalHours = calculateNormalOvertime(employeeId) + extra + trible;
            double totalAmount = normalAmount + extraAmount + tribleAmount;

            // Check if overtime record already exists
            String checkQuery = "SELECT id FROM overtime WHERE attendence_id = ?";
            pst = conn.prepareStatement(checkQuery);
            pst.setInt(1, attendanceId);
            rs = pst.executeQuery();

            if (rs.next()) {
                // Update existing record
                int overtimeId = rs.getInt("id");
                String updateQuery = "UPDATE overtime SET normal = ?, normal_amount = ?, extra = ?, "
                        + "extra_amount = ?, trible = ?, trible_amount = ?, total = ?, "
                        + "total_amount = ? WHERE id = ?";

                pst = conn.prepareStatement(updateQuery);
                pst.setString(1, String.valueOf(calculateNormalOvertime(employeeId)));
                pst.setString(2, String.valueOf(normalAmount));
                pst.setString(3, String.valueOf(extra));
                pst.setString(4, String.valueOf(extraAmount));
                pst.setString(5, String.valueOf(trible));
                pst.setString(6, String.valueOf(tribleAmount));
                pst.setString(7, String.valueOf(totalHours));
                pst.setString(8, String.valueOf(totalAmount));
                pst.setInt(9, overtimeId);

                int rowsUpdated = pst.executeUpdate();
                System.out.println("✅ Overtime record UPDATED in database. Rows affected: " + rowsUpdated);

            } else {
                // Insert new record
                String insertQuery = "INSERT INTO overtime (normal, normal_amount, extra, extra_amount, "
                        + "trible, trible_amount, total, total_amount, attendence_id) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                pst = conn.prepareStatement(insertQuery);
                pst.setString(1, String.valueOf(calculateNormalOvertime(employeeId)));
                pst.setString(2, String.valueOf(normalAmount));
                pst.setString(3, String.valueOf(extra));
                pst.setString(4, String.valueOf(extraAmount));
                pst.setString(5, String.valueOf(trible));
                pst.setString(6, String.valueOf(tribleAmount));
                pst.setString(7, String.valueOf(totalHours));
                pst.setString(8, String.valueOf(totalAmount));
                pst.setInt(9, attendanceId);

                int rowsInserted = pst.executeUpdate();
                System.out.println("✅ Overtime record INSERTED into database. Rows affected: " + rowsInserted);
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error saving overtime: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saving to database: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create attendance record if not exists
     */
    private int createAttendanceRecord(int employeeId) throws Exception {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Create a date for the current month/year (using first day of month)
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(selectedYear, selectedMonth - 1, 1); // Month is 0-based
            java.sql.Date attendanceDate = new java.sql.Date(cal.getTimeInMillis());

            String insertQuery = "INSERT INTO attendence (employee_id, attendance_date, status) VALUES (?, ?, 'Present')";
            pst = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, employeeId);
            pst.setDate(2, attendanceDate);

            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                System.out.println("✅ Created new attendance record with ID: " + newId);
                return newId;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error creating attendance record: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    /**
     * Get employee ID by EPF number
     */
    /**
     * Check database connection
     */
    private void checkDatabaseConnection() throws Exception {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connection is working!");
                conn.close();
            } else {
                System.err.println("❌ Database connection failed!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Database connection error: " + e.getMessage());
        }
    }

    private int getEmployeeIdByEPF(int epfNo) throws Exception {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT id FROM employee WHERE epf_no = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, epfNo);
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error getting employee ID: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        monthComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        yearComboBox = new javax.swing.JComboBox<>();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Over Time Details");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "EPF No", "Name", "Section", "Designation", "NIC", "Normal", "Extra", "Trible", "Total", "Normal Amount", "Extra Amount", "Trible Amount", "Total Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Month");

        monthComboBox.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        monthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Year");

        yearComboBox.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        yearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(138, 138, 138)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 581, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(monthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(135, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> monthComboBox;
    private javax.swing.JComboBox<String> yearComboBox;
    // End of variables declaration//GEN-END:variables
}
