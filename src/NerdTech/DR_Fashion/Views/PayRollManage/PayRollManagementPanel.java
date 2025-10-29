package NerdTech.DR_Fashion.Views.PayRollManage;

import NerdTech.DR_Fashion.Views.PayRollManage.NewUser.NewUserPanel;
import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.LoadingPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class PayRollManagementPanel extends javax.swing.JPanel {

    private Connection connection;
    private DefaultTableModel tableModel;

    public PayRollManagementPanel() {
        initComponents();
        setupScrollBars();
        initializeDatabaseConnection();
        loadPayrollData();
    }

    private void openEPFDFrame(int selectedRow) {
        try {
            // Get selected row data
            String epfNo = model.getValueAt(selectedRow, 0).toString();
            String name = model.getValueAt(selectedRow, 1).toString();

            // Get existing values from table
            String totalForEPF = model.getValueAt(selectedRow, 44) != null
                    ? model.getValueAt(selectedRow, 44).toString() : "0";
            String epf12 = model.getValueAt(selectedRow, 45) != null
                    ? model.getValueAt(selectedRow, 45).toString() : "0";
            String etf3 = model.getValueAt(selectedRow, 46) != null
                    ? model.getValueAt(selectedRow, 46).toString() : "0";
            String epf8 = model.getValueAt(selectedRow, 47) != null
                    ? model.getValueAt(selectedRow, 47).toString() : "0";
            String totalEPFETF = model.getValueAt(selectedRow, 48) != null
                    ? model.getValueAt(selectedRow, 48).toString() : "0";
            String grossSalary = model.getValueAt(selectedRow, 49) != null
                    ? model.getValueAt(selectedRow, 49).toString() : "0";
            String netSalary = model.getValueAt(selectedRow, 50) != null
                    ? model.getValueAt(selectedRow, 50).toString() : "0";

            // Open EPF Frame with data
            AddEPFDFrame epfFrame = new AddEPFDFrame(null, true, epfNo, name,
                    totalForEPF, epf12, etf3, epf8, totalEPFETF, grossSalary, netSalary);
            epfFrame.setLocationRelativeTo(this);
            epfFrame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error opening EPF details: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeDatabaseConnection() {
        try {
            // ඔබේ existing database connection එක භාවිතා කරන්න
            connection = DatabaseConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Database Connection Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPayrollData() {
        if (connection == null) {
            JOptionPane.showMessageDialog(this,
                    "Database connection is not available",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Modified query to avoid duplicates - use DISTINCT and proper joins
            String query = """
            SELECT DISTINCT
                e.epf_no AS 'EPF No',
                e.name_with_initial AS 'Name',
                s.section_name AS 'Section',
                d.title AS 'Designation',
                e.nic AS 'NIC',
                COALESCE(sal.basic_salary, '0') AS 'Basic Salary',
                
                -- Incentive Columns (get latest incentive)
                COALESCE((SELECT attendance_incentive FROM incentive WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Attendance Incentive',
                COALESCE((SELECT grading_incentive FROM incentive WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Grading Incentive',
                COALESCE((SELECT production1_incentive FROM incentive WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Production Incentive I',
                COALESCE((SELECT production2_incentive FROM incentive WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Production Incentive II',
                COALESCE((SELECT total_incentive FROM incentive WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Total Incentive',
                
                -- Day & Amount Columns (get latest day)
                COALESCE((SELECT working_day FROM day WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Working Day',
                COALESCE((SELECT sunday FROM day WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Sunday',
                COALESCE((SELECT poya_day FROM day WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Poya Day',
                COALESCE((SELECT holiday FROM day WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Holiday',
                COALESCE((SELECT total_day FROM day WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Total Day',
                COALESCE((SELECT working_day_amount FROM day WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Working Day Amount',
                COALESCE((SELECT sunday_amount FROM day WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Sunday Amount',
                COALESCE((SELECT poya_day_amount FROM day WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Poya Day Amount',
                COALESCE((SELECT holiday_amount FROM day WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Holiday Amount',
                COALESCE((SELECT total_day_amount FROM day WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Total Day Amount',
                
                -- Overtime Columns (get latest overtime)
                COALESCE((SELECT normal FROM overtime WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Normal',
                COALESCE((SELECT extra FROM overtime WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Extra',
                COALESCE((SELECT trible FROM overtime WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Trible',
                COALESCE((SELECT total FROM overtime WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Total',
                COALESCE((SELECT normal_amount FROM overtime WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Normal Amount',
                COALESCE((SELECT extra_amount FROM overtime WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Extra Amount',
                COALESCE((SELECT trible_amount FROM overtime WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Trible Amount',
                COALESCE((SELECT total_amount FROM overtime WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Total Amount',
                
                -- Leave & No Pay Columns (get latest leave)
                COALESCE((SELECT leave_count FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Leave Count',
                COALESCE((SELECT `leave` FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Leave',
                COALESCE((SELECT leave_amount FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Leave Amount',
                COALESCE((SELECT day FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Day',
                COALESCE((SELECT hour FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Hour',
                COALESCE((SELECT total FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Total',
                COALESCE((SELECT day_amount FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Day Amount',
                COALESCE((SELECT hour_amount FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Hour Amount',
                COALESCE((SELECT total_amount FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Total Amount',
                COALESCE((SELECT short_working_day FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Short Working Days',
                COALESCE((SELECT short_working_day_amount FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Short Working Days Amount',
                COALESCE((SELECT vacation_day FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Vacation Day',
                COALESCE((SELECT total_day FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Total Day',
                COALESCE((SELECT arreas FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Arreas',
                COALESCE((SELECT advance FROM `leave` WHERE attendence_id = a.id ORDER BY id DESC LIMIT 1), '0') AS 'Advance',
                
                -- EPF/ETF Columns from epf table
                COALESCE(epf.total_efp, '0') AS 'Total For EPF',
                COALESCE(epf.epf_12, '0') AS 'EPF 12%',
                COALESCE(epf.epf_3, '0') AS 'ETF 3%',
                COALESCE(epf.epf_8, '0') AS 'EPF 8%',
                COALESCE(epf.`total_epf/etf`, '0') AS 'Total EPF/ETF',
                
                -- Salary Columns from epf table
                COALESCE(epf.gross_salary, '0') AS 'Gross Salary',
                COALESCE(epf.net_salary, '0') AS 'Net Salary'
                
            FROM employee e
            LEFT JOIN section s ON e.section_id = s.id
            LEFT JOIN designation d ON e.designation_id = d.id
            LEFT JOIN salary sal ON e.id = sal.employee_id
            LEFT JOIN attendence a ON e.id = a.employee_id 
                AND a.attendance_date = (SELECT MAX(attendance_date) FROM attendence WHERE employee_id = e.id)
            LEFT JOIN epf ON e.id = epf.id
            WHERE e.status = 'active'
            ORDER BY e.epf_no
            """;

            System.out.println("Executing query..."); // Debug message

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Table model එක clear කරන්න
            tableModel = (DefaultTableModel) model.getModel();
            tableModel.setRowCount(0);

            // Data rows add කරන්න
            int rowCount = 0;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= 51; i++) {
                    Object value = rs.getObject(i);
                    row.add(value != null ? value : "0");
                }
                tableModel.addRow(row);
                rowCount++;
            }

            // Column widths set කරන්න
            setColumnWidths();

            rs.close();
            stmt.close();

            System.out.println("Loaded " + rowCount + " rows"); // Debug message

            if (rowCount > 0) {
                JOptionPane.showMessageDialog(this,
                        "Payroll data loaded successfully! " + rowCount + " records found.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No payroll data found for active employees.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading payroll data: " + e.getMessage()
                    + "\nPlease check if all required tables exist in the database.",
                    "Error", JOptionPane.ERROR_MESSAGE);

            // Fallback: අත්හදා බලන්න සරල query එකක්
            loadBasicEmployeeData();
        }
    }

    // Fallback method: සරල employee data load කිරීමට
    private void loadBasicEmployeeData() {
        try {
            String simpleQuery = """
                SELECT 
                    e.epf_no AS 'EPF No',
                    e.name_with_initial AS 'Name',
                    s.section_name AS 'Section',
                    d.title AS 'Designation',
                    e.nic AS 'NIC',
                    COALESCE(sal.basic_salary, '0') AS 'Basic Salary'
                FROM employee e
                LEFT JOIN section s ON e.section_id = s.id
                LEFT JOIN designation d ON e.designation_id = d.id
                LEFT JOIN salary sal ON e.id = sal.employee_id
                WHERE e.status = 'active'
                ORDER BY e.epf_no
                LIMIT 50
                """;

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(simpleQuery);

            tableModel = (DefaultTableModel) model.getModel();
            tableModel.setRowCount(0);

            // Only add basic columns
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getObject("EPF No"));
                row.add(rs.getObject("Name"));
                row.add(rs.getObject("Section"));
                row.add(rs.getObject("Designation"));
                row.add(rs.getObject("NIC"));
                row.add(rs.getObject("Basic Salary"));

                // අනෙක් columns සඳහා empty values add කරන්න
                for (int i = 6; i < 51; i++) {
                    row.add("0");
                }

                tableModel.addRow(row);
            }

            rs.close();
            stmt.close();

            JOptionPane.showMessageDialog(this,
                    "Basic employee data loaded. Some payroll details may be missing.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading basic employee data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setColumnWidths() {
        try {
            // Column widths set කරන්න
            model.getColumnModel().getColumn(0).setPreferredWidth(80);  // EPF No
            model.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
            model.getColumnModel().getColumn(2).setPreferredWidth(100); // Section
            model.getColumnModel().getColumn(3).setPreferredWidth(120); // Designation
            model.getColumnModel().getColumn(4).setPreferredWidth(120); // NIC
            model.getColumnModel().getColumn(5).setPreferredWidth(100); // Basic Salary

            // අනෙක් columns වලට අවශ්‍ය widths set කරන්න
            for (int i = 6; i < model.getColumnCount(); i++) {
                model.getColumnModel().getColumn(i).setPreferredWidth(90);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupScrollBars() {
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        model.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        model = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1542, 664));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("PayRoll Management");

        model.setFont(new java.awt.Font("JetBrains Mono", 0, 12)); // NOI18N
        model.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "EPF No", "Name", "Section", "Designation", "NIC", "Basic Salary", "Attendance Incentive", "Grading Incentive", "Production Incentive I", "Production Incentive II", "Total Incentive", "Working Day", "Sunday", "Poya Day", "Holiday", "Total Day", "Working Day Amount", "Sunday Amount", "Poya Day Amount", "Holiday Amount", "Total Day Amount", "Normal", "Extra", "Trible", "Total", "Normal Amount", "Extra Amount", "Trible Amount", "Total Amount", "Total Amount", "Leave", "Leave Amount", "Day", "Hour", "Total", "Day Amount", "Hour Amount", "Total Amount", "Short Working Days", "Short Working Days Amount ", "Vacation Day", "Total Day", "Arreas", "Advance", "Total For EPF", "EPF 12% ", "ETF 3%", "EPF 8%", "Total EPF/ETF", "Gross Salary", "Net Salary"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        model.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                modelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(model);
        if (model.getColumnModel().getColumnCount() > 0) {
            model.getColumnModel().getColumn(0).setResizable(false);
            model.getColumnModel().getColumn(1).setResizable(false);
        }

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("New User");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Incentive");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Days & Amount");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton4.setText("Over Time");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton5.setText("Leave & No Pay");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1530, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4)
                                .addGap(18, 18, 18)
                                .addComponent(jButton5)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton1)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addContainerGap(42, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void modelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modelMouseClicked
        if (evt.getClickCount() == 2) { // Double click check
            int selectedRow = model.getSelectedRow();
            if (selectedRow != -1) {
                openEPFDFrame(selectedRow);
            }
        }

    }//GEN-LAST:event_modelMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        loadNewUserPanel();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        loadIncentivePanel();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadDaysAmountPanel();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        loadOverTimePanel();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        loadLeaveNoPayPanel();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void loadLeaveNoPayPanel() {
        try {
            // 1️⃣ Create and show the loading panel
            LoadingPanel loadingPanel = new LoadingPanel("Loading Leave & No Pay Panel...");

            // 2️⃣ Get parent container (the main panel holder)
            java.awt.Container parent = this.getParent();
            if (parent != null) {
                // Remove current panel and show loading
                parent.remove(this);
                parent.add(loadingPanel);
                parent.revalidate();
                parent.repaint();

                // 3️⃣ Load LeaveNoPayPanel in background (non-blocking)
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Optional: small artificial delay to show loading screen
                        Thread.sleep(500);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            // Remove loading panel
                            parent.remove(loadingPanel);

                            // Load the LeaveNoPayPanel
                            NerdTech.DR_Fashion.Views.PayRollManage.LeaveNopay.LeaveNoPayPanel leaveNoPayPanel
                                    = new NerdTech.DR_Fashion.Views.PayRollManage.LeaveNopay.LeaveNoPayPanel();

                            // Add and refresh
                            parent.add(leaveNoPayPanel);
                            parent.revalidate();
                            parent.repaint();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(parent,
                                    "Error loading Leave & No Pay Panel: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                // Execute background task
                worker.execute();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading Leave & No Pay Panel!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadOverTimePanel() {
        try {
            // 1️⃣ Create and show the loading panel
            LoadingPanel loadingPanel = new LoadingPanel("Loading Over Time Panel...");

            // 2️⃣ Get parent container (the main panel holder)
            java.awt.Container parent = this.getParent();
            if (parent != null) {
                // Remove current panel and show loading
                parent.remove(this);
                parent.add(loadingPanel);
                parent.revalidate();
                parent.repaint();

                // 3️⃣ Load OverTimePanel in background (non-blocking)
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Optional: small artificial delay to show loading screen
                        Thread.sleep(500);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            // Remove loading panel
                            parent.remove(loadingPanel);

                            // Load the OverTimePanel
                            NerdTech.DR_Fashion.Views.PayRollManage.OverTime.OverTimePanel overTimePanel
                                    = new NerdTech.DR_Fashion.Views.PayRollManage.OverTime.OverTimePanel();

                            // Add and refresh
                            parent.add(overTimePanel);
                            parent.revalidate();
                            parent.repaint();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(parent,
                                    "Error loading Over Time Panel: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                // Execute background task
                worker.execute();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading Over Time Panel!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDaysAmountPanel() {
        try {
            // 1️⃣ Create and show the loading panel
            LoadingPanel loadingPanel = new LoadingPanel("Loading Days & Amount Panel...");

            // 2️⃣ Get parent container (the main panel holder)
            java.awt.Container parent = this.getParent();
            if (parent != null) {
                // Remove current panel and show loading
                parent.remove(this);
                parent.add(loadingPanel);
                parent.revalidate();
                parent.repaint();

                // 3️⃣ Load Days_Amount_Panel in background (non-blocking)
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Optional: small artificial delay to show loading screen
                        Thread.sleep(500);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            // Remove loading panel
                            parent.remove(loadingPanel);

                            // Load the Days_Amount_Panel
                            NerdTech.DR_Fashion.Views.PayRollManage.Days_Amount_Panel.Days_Amount_Panel daysAmountPanel
                                    = new NerdTech.DR_Fashion.Views.PayRollManage.Days_Amount_Panel.Days_Amount_Panel();

                            // Add and refresh
                            parent.add(daysAmountPanel);
                            parent.revalidate();
                            parent.repaint();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(parent,
                                    "Error loading Days & Amount Panel: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                // Execute background task
                worker.execute();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading Days & Amount Panel!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadIncentivePanel() {
        try {
            // 1️⃣ Create and show the loading panel
            LoadingPanel loadingPanel = new LoadingPanel("Loading Incentive Panel...");

            // 2️⃣ Get parent container (the main panel holder)
            java.awt.Container parent = this.getParent();
            if (parent != null) {
                // Remove current panel and show loading
                parent.remove(this);
                parent.add(loadingPanel);
                parent.revalidate();
                parent.repaint();

                // 3️⃣ Load IncentivePanel in background (non-blocking)
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Optional: small artificial delay to show loading screen
                        Thread.sleep(500);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            // Remove loading panel
                            parent.remove(loadingPanel);

                            // Load the IncentivePanel
                            NerdTech.DR_Fashion.Views.PayRollManage.IncentivePanel.IncentivePanel incentivePanel
                                    = new NerdTech.DR_Fashion.Views.PayRollManage.IncentivePanel.IncentivePanel();

                            // Add and refresh
                            parent.add(incentivePanel);
                            parent.revalidate();
                            parent.repaint();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(parent,
                                    "Error loading Incentive Panel: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                // Execute background task
                worker.execute();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading Incentive Panel!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadNewUserPanel() {
        try {
            // Show loading panel first
            LoadingPanel loadingPanel = new LoadingPanel("Loading New User Panel");

            // Get the parent container
            java.awt.Container parent = this.getParent();
            if (parent != null) {
                // Remove current panel and show loading
                parent.remove(this);
                parent.add(loadingPanel);
                parent.revalidate();
                parent.repaint();

                // Use SwingWorker to load NewUserPanel in background
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Simulate loading time (you can remove this in production)
                        Thread.sleep(500);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            // Remove loading panel and add NewUserPanel
                            parent.remove(loadingPanel);
                            NewUserPanel newUserPanel = new NewUserPanel();
                            parent.add(newUserPanel);
                            parent.revalidate();
                            parent.repaint();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(parent,
                                    "Error loading New User panel: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                worker.execute();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading New User panel!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    // End of variables declaration//GEN-END:variables
}
