package NerdTech.DR_Fashion.Views.PayRollManage.OverTime;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.LoadingPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OverTimePanel extends javax.swing.JPanel {

    private int selectedMonth;
    private int selectedYear;
    private LoadingPanel loadingPanel;

    public OverTimePanel() {
        try {
            initComponents();
            setupMonthYearComboBoxes();
            checkDatabaseConnection();
            loadEmployeeData();

            setupDoubleClickListener();
            System.out.println("🎯 OverTimePanel initialized successfully!");
        } catch (Exception e) {
            System.err.println("❌ Error initializing OverTimePanel: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error initializing panel: " + e.getMessage(),
                    "Initialization Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupMonthYearComboBoxes() {
        monthComboBox = new JComboBox<>();
        yearComboBox = new JComboBox<>();

        String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

        for (String month : months) {
            monthComboBox.addItem(month);
        }

        for (int year = 2020; year <= 2030; year++) {
            yearComboBox.addItem(String.valueOf(year));
        }

        // 🧩 Avoid null when selected item not ready yet
        monthComboBox.addActionListener(e -> {
            Object selected = monthComboBox.getSelectedItem();
            if (selected != null) {
                System.out.println("Selected Month: " + selected.toString());
                // Your existing logic here
            }
        });

        yearComboBox.addActionListener(e -> {
            Object selected = yearComboBox.getSelectedItem();
            if (selected != null) {
                System.out.println("Selected Year: " + selected.toString());
                // Your existing logic here
            }
        });
    }

    private void setupDoubleClickListener() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openAddOverTimeDialog();
                }
            }
        });
    }

    private void loadEmployeeData() throws Exception {
        showLoadingPanel("Loading employee data...");

        SwingWorker<DefaultTableModel, Void> worker = new SwingWorker<DefaultTableModel, Void>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                DefaultTableModel model = new DefaultTableModel(
                        new String[]{"EPF No", "Name", "Section", "Designation", "NIC",
                            "Normal", "Extra", "Trible", "Total",
                            "Normal Amount", "Extra Amount", "Trible Amount", "Total Amount"}, 0);

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "SELECT e.epf_no, e.name_with_initial, s.section_name, d.title, e.nic, e.id as employee_id "
                            + "FROM employee e "
                            + "LEFT JOIN section s ON e.section_id = s.id "
                            + "LEFT JOIN designation d ON e.designation_id = d.id "
                            + "WHERE e.status = 'Active'";
                    PreparedStatement pst = conn.prepareStatement(query);
                    ResultSet rs = pst.executeQuery();
                    int count = 0;

                    while (rs.next()) {
                        updateLoadingMessage("Processing employee " + (++count));
                        double normalOvertime = calculateNormalOvertime(rs.getInt("employee_id"));

                        model.addRow(new Object[]{
                            rs.getInt("epf_no"),
                            rs.getString("name_with_initial"),
                            rs.getString("section_name"),
                            rs.getString("title"),
                            rs.getString("nic"),
                            normalOvertime, 0.0, 0.0,
                            normalOvertime, 0.0, 0.0, 0.0, 0.0
                        });
                    }
                    rs.close();
                    pst.close();
                }
                return model;
            }

            @Override
            protected void done() {
                try {
                    hideLoadingPanelAndShowTable(get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void hideLoadingPanelAndShowTable(DefaultTableModel model) {
        SwingUtilities.invokeLater(() -> {
            removeAll();
            setLayout(new BorderLayout());

            // Recreate header labels
            jLabel1 = new JLabel("Over Time Details");
            jLabel1.setFont(new Font("JetBrains Mono", Font.BOLD, 36));

            jLabel2 = new JLabel("Month");
            jLabel2.setFont(new Font("JetBrains Mono", Font.PLAIN, 18));

            jLabel3 = new JLabel("Year");
            jLabel3.setFont(new Font("JetBrains Mono", Font.PLAIN, 18));

            // Re-setup comboboxes
            setupMonthYearComboBoxes();

            // Top section (Month & Year)
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
            topPanel.add(jLabel2);
            topPanel.add(monthComboBox);
            topPanel.add(jLabel3);
            topPanel.add(yearComboBox);

            // Header section
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.add(jLabel1, BorderLayout.WEST);
            headerPanel.add(topPanel, BorderLayout.EAST);

            // Create new table with given model
            jTable1 = new JTable();
            jTable1.setModel(model);

            // ✅ Disable all column editing (important line)
            jTable1.setDefaultEditor(Object.class, null);

            // Optional: adjust font or row height
            jTable1.setRowHeight(28);
            jTable1.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));

            JScrollPane scrollPane = new JScrollPane(jTable1);

            // Reconnect listeners
            setupDoubleClickListener();
            setupTableListeners();

            // Main content area
            JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
            jSeparator1 = new JSeparator();
            contentPanel.add(jSeparator1, BorderLayout.NORTH);
            contentPanel.add(scrollPane, BorderLayout.CENTER);

            // Add all panels to main layout
            add(headerPanel, BorderLayout.NORTH);
            add(contentPanel, BorderLayout.CENTER);

            revalidate();
            repaint();
        });
    }

    private void updateLoadingMessage(String message) {
        if (loadingPanel != null) {
            loadingPanel.setMessage(message);
        }
    }

    private void showLoadingPanel(String message) {
        removeAll();
        setLayout(new BorderLayout());
        loadingPanel = new LoadingPanel(message);
        add(loadingPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void hideLoadingPanel() {
        SwingUtilities.invokeLater(() -> {
            removeAll();
            revalidate();
            repaint();
            loadingPanel = null;
        });
    }

    private double calculateNormalOvertime(int employeeId) {
        double totalOvertime = 0.0;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT come_off, attendance_date FROM attendance WHERE employee_id=? "
                    + "AND come_off IS NOT NULL AND MONTH(attendance_date)=? AND YEAR(attendance_date)=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, employeeId);
            pst.setInt(2, selectedMonth);
            pst.setInt(3, selectedYear);
            ResultSet rs = pst.executeQuery();

            Time eveningStart = Time.valueOf("17:30:00");

            while (rs.next()) {
                Time comeOff = rs.getTime("come_off");
                if (comeOff != null && comeOff.after(eveningStart)) {
                    double overtimeHours = (comeOff.getTime() - eveningStart.getTime()) / 3600000.0;
                    totalOvertime += Math.max(0, overtimeHours); // Ensure positive value
                }
            }
            rs.close();
            pst.close();
        } catch (Exception e) {
            System.err.println("Error calculating overtime for employee " + employeeId + ": " + e.getMessage());
        }
        return Math.round(totalOvertime * 100.0) / 100.0;
    }

    private void setupTableListeners() {
        if (jTable1.getModel() instanceof DefaultTableModel) {
            ((DefaultTableModel) jTable1.getModel()).addTableModelListener(e -> {
                if (e.getColumn() >= 5 && e.getColumn() <= 11) {
                    calculateTotals(e.getFirstRow());
                }
            });
        }
    }

    private boolean isCalculating = false; // ✅ Flag එකක් add කරන්න

    private void calculateTotals(int row) {
        // ✅ දැනටමත් calculate කරනවා නම් return කරන්න
        if (isCalculating) {
            return;
        }

        try {
            isCalculating = true; // ✅ Calculation start කරනවා කියලා mark කරන්න

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            double normal = getDoubleValue(model.getValueAt(row, 5));
            double extra = getDoubleValue(model.getValueAt(row, 6));
            double trible = getDoubleValue(model.getValueAt(row, 7));
            double normalAmount = getDoubleValue(model.getValueAt(row, 9));
            double extraAmount = getDoubleValue(model.getValueAt(row, 10));
            double tribleAmount = getDoubleValue(model.getValueAt(row, 11));

            double totalHours = normal + extra + trible;
            double totalAmount = normalAmount + extraAmount + tribleAmount;

            model.setValueAt(totalHours, row, 8);
            model.setValueAt(totalAmount, row, 12);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            isCalculating = false; // ✅ Calculation අවසානයි කියලා mark කරන්න
        }
    }

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

    private void openAddOverTimeDialog() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee first!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        String epfNo = model.getValueAt(selectedRow, 0).toString();
        String name = model.getValueAt(selectedRow, 1).toString();

        double currentExtra = getDoubleValue(model.getValueAt(selectedRow, 6));
        double currentTrible = getDoubleValue(model.getValueAt(selectedRow, 7));
        double currentNormalAmount = getDoubleValue(model.getValueAt(selectedRow, 9));
        double currentExtraAmount = getDoubleValue(model.getValueAt(selectedRow, 10));
        double currentTribleAmount = getDoubleValue(model.getValueAt(selectedRow, 11));

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

    public void updateOvertimeData(int selectedRow, double normal, double extra, double trible,
            double normalAmount, double extraAmount, double tribleAmount) {
        try {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            int epfNo = (int) model.getValueAt(selectedRow, 0);

            model.setValueAt(normal, selectedRow, 5);
            model.setValueAt(extra, selectedRow, 6);
            model.setValueAt(trible, selectedRow, 7);
            model.setValueAt(normalAmount, selectedRow, 9);
            model.setValueAt(extraAmount, selectedRow, 10);
            model.setValueAt(tribleAmount, selectedRow, 11);

            double totalHours = normal + extra + trible;
            double totalAmount = normalAmount + extraAmount + tribleAmount;
            model.setValueAt(totalHours, selectedRow, 8);
            model.setValueAt(totalAmount, selectedRow, 12);

            saveOvertimeToDatabase(epfNo, normal, extra, trible, normalAmount, extraAmount, tribleAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveOvertimeToDatabase(int epfNo, double normal, double extra, double trible,
            double normalAmount, double extraAmount, double tribleAmount) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            int employeeId = getEmployeeIdByEPF(epfNo);
            if (employeeId == -1) {
                System.err.println("Employee not found for EPF: " + epfNo);
                return;
            }

            // Rest of the method remains similar but with better error handling
            // ...
        } catch (Exception e) {
            System.err.println("Error saving overtime data: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saving data: " + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int createAttendanceRecord(int employeeId) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(selectedYear, selectedMonth - 1, 1);
            java.sql.Date attendanceDate = new java.sql.Date(cal.getTimeInMillis());
            String insertQuery = "INSERT INTO attendence(employee_id, attendance_date, status) VALUES(?, ?, 'Present')";
            PreparedStatement pst = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, employeeId);
            pst.setDate(2, attendanceDate);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    private int getEmployeeIdByEPF(int epfNo) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id FROM employee WHERE epf_no=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, epfNo);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    private void checkDatabaseConnection() throws Exception {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Database connection OK!");
            }
            conn.close(); // Close connection after checking
        } catch (Exception e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            throw e;
        }
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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
            jTable1.getColumnModel().getColumn(7).setResizable(false);
            jTable1.getColumnModel().getColumn(8).setResizable(false);
            jTable1.getColumnModel().getColumn(9).setResizable(false);
            jTable1.getColumnModel().getColumn(10).setResizable(false);
            jTable1.getColumnModel().getColumn(11).setResizable(false);
            jTable1.getColumnModel().getColumn(12).setResizable(false);
        }

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
