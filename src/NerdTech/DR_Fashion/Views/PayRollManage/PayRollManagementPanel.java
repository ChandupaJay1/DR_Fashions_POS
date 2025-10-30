package NerdTech.DR_Fashion.Views.PayRollManage;

import NerdTech.DR_Fashion.Views.PayRollManage.NewUser.NewUserPanel;
import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.LoadingPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
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
        jButton6 = new javax.swing.JButton();

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

        jButton6.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton6.setText("Print");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
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
                                .addComponent(jButton5)
                                .addGap(18, 18, 18)
                                .addComponent(jButton6)))
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
                    .addComponent(jButton5)
                    .addComponent(jButton6))
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

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        printPayroll();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void printPayroll() {
        int selectedRow = model.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to print payroll",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Get selected employee data
            String epfNo = model.getValueAt(selectedRow, 0).toString();
            String name = model.getValueAt(selectedRow, 1).toString();
            String section = model.getValueAt(selectedRow, 2).toString();
            String designation = model.getValueAt(selectedRow, 3).toString();
            String nic = model.getValueAt(selectedRow, 4).toString();

            // Earnings
            String basicSalary = formatCurrency(model.getValueAt(selectedRow, 5).toString());
            String gradingIncentive = formatCurrency(model.getValueAt(selectedRow, 7).toString());
            String sundayAmount = formatCurrency(model.getValueAt(selectedRow, 17).toString());
            String holidayAmount = formatCurrency(model.getValueAt(selectedRow, 19).toString());
            String poyadayAmount = formatCurrency(model.getValueAt(selectedRow, 18).toString());
            String overtimeAmount = formatCurrency(model.getValueAt(selectedRow, 28).toString());
            String attendanceIncentive = formatCurrency(model.getValueAt(selectedRow, 6).toString());
            String productionIncentives = formatCurrency(model.getValueAt(selectedRow, 8).toString());
            String arrears = formatCurrency(model.getValueAt(selectedRow, 42).toString());

            // Deductions
            String epf12 = formatCurrency(model.getValueAt(selectedRow, 45).toString());
            String etf3 = formatCurrency(model.getValueAt(selectedRow, 46).toString());
            String epf8 = formatCurrency(model.getValueAt(selectedRow, 47).toString());
            String advance = formatCurrency(model.getValueAt(selectedRow, 43).toString());
            String shortWorkingDays = formatCurrency(model.getValueAt(selectedRow, 38).toString());
            String noPayDays = "0.00";
            String noPayHours = "0.00";

            // Totals
            String grossSalary = formatCurrency(model.getValueAt(selectedRow, 49).toString());
            String totalForEPF = formatCurrency(model.getValueAt(selectedRow, 44).toString());
            String netSalary = formatCurrency(model.getValueAt(selectedRow, 50).toString());

            // Data section
            String daysWorked = formatNumber(model.getValueAt(selectedRow, 11).toString());
            String leave = formatNumber(model.getValueAt(selectedRow, 30).toString());
            String sundayDays = formatNumber(model.getValueAt(selectedRow, 12).toString());
            String holidayDays = formatNumber(model.getValueAt(selectedRow, 14).toString());
            String poyadayDays = formatNumber(model.getValueAt(selectedRow, 13).toString());
            String normalOT = formatNumber(model.getValueAt(selectedRow, 21).toString());
            String extraOT = formatNumber(model.getValueAt(selectedRow, 22).toString());
            String tribleOT = formatNumber(model.getValueAt(selectedRow, 23).toString());

            // Create and show stylish print preview
            showStylishPrintPreview(epfNo, name, section, designation, nic,
                    basicSalary, gradingIncentive, sundayAmount, holidayAmount, poyadayAmount, overtimeAmount,
                    attendanceIncentive, productionIncentives, arrears,
                    epf12, etf3, epf8, advance, shortWorkingDays, noPayDays, noPayHours,
                    grossSalary, totalForEPF, netSalary,
                    daysWorked, leave, sundayDays, holidayDays, poyadayDays, normalOT, extraOT, tribleOT);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error printing payroll: " + e.getMessage(),
                    "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showStylishPrintPreview(String epfNo, String name, String section, String designation, String nic,
            String basicSalary, String gradingIncentive, String sunday, String holiday,
            String poyaday, String overtime, String attendanceIncentive, String productionIncentives,
            String arrears, String epf12, String etf3, String epf8, String advance,
            String shortWorkingDays, String noPayDays, String noPayHours, String grossSalary,
            String totalForEPF, String netSalary, String daysWorked, String leave,
            String sundayDays, String holidayDays, String poyadayDays, String normalOT,
            String extraOT, String tribleOT) {

        // Create a professional panel with white background
        JPanel printPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Draw white background
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Draw subtle border
                g2d.setColor(new Color(200, 200, 200));
                g2d.drawRect(5, 5, getWidth() - 10, getHeight() - 10);
            }
        };

        printPanel.setLayout(new BorderLayout());
        printPanel.setPreferredSize(new Dimension(400, 600)); // A4 half size
        printPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header - DR FASHIONS with professional styling
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel headerLabel = new JLabel("DR FASHIONS", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // Employee details table
        JPanel detailsPanel = createProfessionalTablePanel("EMPLOYEE DETAILS", new String[][]{
            {"EPF No:", epfNo, "Name:", name},
            {"Section:", section, "Designation:", designation},
            {"NIC Number:", nic, "Month:", "September 25"}
        });

        // Earnings & Deductions table
        JPanel earningsDeductionsPanel = createProfessionalEarningsDeductionsPanel(
                basicSalary, gradingIncentive, sunday, holiday, poyaday, overtime,
                attendanceIncentive, productionIncentives, arrears,
                epf12, etf3, epf8, advance, shortWorkingDays, noPayDays, noPayHours
        );

        // Salary Summary
        JPanel salarySummaryPanel = createProfessionalTablePanel("SALARY SUMMARY", new String[][]{
            {"Gross Salary:", grossSalary, "Total For EPF:", totalForEPF},
            {"Net Salary:", netSalary, "", ""}
        });

        // Attendance Data
        JPanel attendancePanel = createProfessionalTablePanel("ATTENDANCE DATA", new String[][]{
            {"Days Worked:", daysWorked, "Normal OT:", normalOT},
            {"Leave:", leave, "Extra OT:", extraOT},
            {"Sunday:", sundayDays, "Trible OT:", tribleOT},
            {"Holiday:", holidayDays, "", ""},
            {"Poyaday:", poyadayDays, "", ""}
        });

        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel footerLabel = new JLabel("Authorized Signature: _________________________");
        footerLabel.setFont(new Font("Arial", Font.BOLD, 11));
        footerLabel.setForeground(Color.BLACK);
        footerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        // Add all components to content panel
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        contentPanel.add(detailsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        contentPanel.add(earningsDeductionsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        contentPanel.add(salarySummaryPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        contentPanel.add(attendancePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(footerPanel);

        printPanel.add(contentPanel, BorderLayout.CENTER);

        // Create scroll pane for preview
        JScrollPane scrollPane = new JScrollPane(printPanel);
        scrollPane.setPreferredSize(new Dimension(450, 650));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Show print dialog
        int option = JOptionPane.showConfirmDialog(this, scrollPane,
                "Payroll Print Preview - " + name,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            printStylishPanel(printPanel, name + " - Payroll Slip");
        }
    }

    private JPanel createProfessionalEarningsDeductionsPanel(String basicSalary, String gradingIncentive, String sunday,
            String holiday, String poyaday, String overtime,
            String attendanceIncentive, String productionIncentives,
            String arrears, String epf12, String etf3, String epf8,
            String advance, String shortWorkingDays, String noPayDays,
            String noPayHours) {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Title
        JLabel titleLabel = new JLabel("EARNINGS & DEDUCTIONS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Content panel with two columns
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        contentPanel.setOpaque(false);

        // Earnings Panel
        JPanel earningsPanel = createProfessionalColumnPanel("EARNINGS", new String[][]{
            {"Basic Salary:", basicSalary},
            {"Grading Incentive:", gradingIncentive},
            {"Sunday:", sunday},
            {"Holiday:", holiday},
            {"Poyaday:", poyaday},
            {"Overtime:", overtime},
            {"Attendance Incentive:", attendanceIncentive},
            {"Production Incentives:", productionIncentives},
            {"Arrears:", arrears}
        });

        // Deductions Panel
        JPanel deductionsPanel = createProfessionalColumnPanel("DEDUCTIONS", new String[][]{
            {"EPF 12%:", epf12},
            {"ETF 3%:", etf3},
            {"EPF 8%:", epf8},
            {"Advance:", advance},
            {"Short Working Days:", shortWorkingDays},
            {"No Pay Days:", noPayDays},
            {"No Pay Hours:", noPayHours}
        });

        contentPanel.add(earningsPanel);
        contentPanel.add(deductionsPanel);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createProfessionalColumnPanel(String title, String[][] data) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Column title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 11));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        // Data panel
        JPanel dataPanel = new JPanel(new GridLayout(0, 2, 5, 3));
        dataPanel.setOpaque(false);
        dataPanel.setBorder(BorderFactory.createEmptyBorder(8, 5, 5, 5));

        for (String[] row : data) {
            JLabel label = new JLabel(row[0]);
            label.setFont(new Font("Arial", Font.PLAIN, 9));
            label.setForeground(Color.BLACK);

            JLabel value = new JLabel(row[1]);
            value.setFont(new Font("Arial", Font.PLAIN, 9));
            value.setForeground(Color.DARK_GRAY);
            value.setHorizontalAlignment(SwingConstants.RIGHT);

            dataPanel.add(label);
            dataPanel.add(value);
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(dataPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfessionalTablePanel(String title, String[][] data) {
        JPanel panel = new JPanel(new GridLayout(0, 4, 8, 4));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create a wrapper panel for the title to span all columns
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        // Add data rows
        for (String[] row : data) {
            for (int i = 0; i < 4; i++) {
                String text = i < row.length ? row[i] : "";
                JLabel label = new JLabel(text);

                if (i % 2 == 0) {
                    // Labels - bold and left aligned
                    label.setFont(new Font("Arial", Font.BOLD, 10));
                    label.setForeground(Color.BLACK);
                    label.setHorizontalAlignment(SwingConstants.LEFT);
                } else {
                    // Values - plain and right aligned
                    label.setFont(new Font("Arial", Font.PLAIN, 10));
                    label.setForeground(Color.DARK_GRAY);
                    label.setHorizontalAlignment(SwingConstants.RIGHT);
                }

                panel.add(label);
            }
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(titlePanel, BorderLayout.NORTH);
        wrapper.add(panel, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createTablePanel(String title, String[][] data) {
        JPanel panel = new JPanel(new GridLayout(0, 4, 5, 3));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create a wrapper panel for the title to span all columns
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        // Add data rows
        for (String[] row : data) {
            for (int i = 0; i < 4; i++) {
                String text = i < row.length ? row[i] : "";
                JLabel label = new JLabel(text);
                label.setFont(new Font("Arial", i % 2 == 0 ? Font.BOLD : Font.PLAIN, 10));
                label.setForeground(Color.WHITE);

                if (i % 2 == 1 && !text.isEmpty()) {
                    // Right align values
                    label.setHorizontalAlignment(SwingConstants.RIGHT);
                }

                panel.add(label);
            }
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(titlePanel, BorderLayout.NORTH);
        wrapper.add(panel, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createEarningsDeductionsPanel(String basicSalary, String gradingIncentive, String sunday,
            String holiday, String poyaday, String overtime,
            String attendanceIncentive, String productionIncentives,
            String arrears, String epf12, String etf3, String epf8,
            String advance, String shortWorkingDays, String noPayDays,
            String noPayHours) {

        JPanel panel = new JPanel(new GridLayout(0, 4, 5, 3));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        // Title
        JLabel titleLabel = new JLabel("EARNINGS & DEDUCTIONS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Column headers
        JLabel earningsHeader = new JLabel("EARNINGS");
        earningsHeader.setFont(new Font("Arial", Font.BOLD, 12));
        earningsHeader.setForeground(Color.WHITE);
        earningsHeader.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel earningsAmountHeader = new JLabel("AMOUNT");
        earningsAmountHeader.setFont(new Font("Arial", Font.BOLD, 12));
        earningsAmountHeader.setForeground(Color.WHITE);
        earningsAmountHeader.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel deductionsHeader = new JLabel("DEDUCTIONS");
        deductionsHeader.setFont(new Font("Arial", Font.BOLD, 12));
        deductionsHeader.setForeground(Color.WHITE);
        deductionsHeader.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel deductionsAmountHeader = new JLabel("AMOUNT");
        deductionsAmountHeader.setFont(new Font("Arial", Font.BOLD, 12));
        deductionsAmountHeader.setForeground(Color.WHITE);
        deductionsAmountHeader.setHorizontalAlignment(SwingConstants.RIGHT);

        // Add headers
        panel.add(earningsHeader);
        panel.add(earningsAmountHeader);
        panel.add(deductionsHeader);
        panel.add(deductionsAmountHeader);

        // Add earnings and deductions data
        String[][] earningsData = {
            {"Basic Salary:", basicSalary},
            {"Grading Incentive:", gradingIncentive},
            {"Sunday:", sunday},
            {"Holiday:", holiday},
            {"Poyaday:", poyaday},
            {"Overtime:", overtime},
            {"Attendance Incentive:", attendanceIncentive},
            {"Production Incentives:", productionIncentives},
            {"Arrears:", arrears}
        };

        String[][] deductionsData = {
            {"EPF 12%:", epf12},
            {"ETF 3%:", etf3},
            {"EPF 8%:", epf8},
            {"Advance:", advance},
            {"Short Working Days:", shortWorkingDays},
            {"No Pay Days:", noPayDays},
            {"No Pay Hours:", noPayHours}
        };

        int maxRows = Math.max(earningsData.length, deductionsData.length);

        for (int i = 0; i < maxRows; i++) {
            // Earnings column
            if (i < earningsData.length) {
                JLabel earningLabel = new JLabel(earningsData[i][0]);
                earningLabel.setFont(new Font("Arial", Font.PLAIN, 9));
                earningLabel.setForeground(Color.WHITE);
                panel.add(earningLabel);

                JLabel earningAmount = new JLabel(earningsData[i][1]);
                earningAmount.setFont(new Font("Arial", Font.PLAIN, 9));
                earningAmount.setForeground(Color.WHITE);
                earningAmount.setHorizontalAlignment(SwingConstants.RIGHT);
                panel.add(earningAmount);
            } else {
                panel.add(new JLabel(""));
                panel.add(new JLabel(""));
            }

            // Deductions column
            if (i < deductionsData.length) {
                JLabel deductionLabel = new JLabel(deductionsData[i][0]);
                deductionLabel.setFont(new Font("Arial", Font.PLAIN, 9));
                deductionLabel.setForeground(Color.WHITE);
                panel.add(deductionLabel);

                JLabel deductionAmount = new JLabel(deductionsData[i][1]);
                deductionAmount.setFont(new Font("Arial", Font.PLAIN, 9));
                deductionAmount.setForeground(Color.WHITE);
                deductionAmount.setHorizontalAlignment(SwingConstants.RIGHT);
                panel.add(deductionAmount);
            } else {
                panel.add(new JLabel(""));
                panel.add(new JLabel(""));
            }
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        wrapper.add(titlePanel, BorderLayout.NORTH);
        wrapper.add(panel, BorderLayout.CENTER);

        return wrapper;
    }

    private void printStylishPanel(JPanel panel, String jobName) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName(jobName);

            // Set page format for A4 half size
            PageFormat pf = job.defaultPage();
            Paper paper = new Paper();
            double width = 8.27 * 72; // A4 width in points
            double height = 5.83 * 72; // A4 half height
            paper.setSize(width, height);
            paper.setImageableArea(18, 18, width - 36, height - 36); // Smaller margins
            pf.setPaper(paper);
            pf.setOrientation(PageFormat.PORTRAIT);

            job.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
                    if (pageIndex > 0) {
                        return Printable.NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    // Scale to fit the page
                    double scaleX = pageFormat.getImageableWidth() / panel.getWidth();
                    double scaleY = pageFormat.getImageableHeight() / panel.getHeight();
                    double scale = Math.min(scaleX, scaleY);
                    g2d.scale(scale, scale);

                    // Print the panel
                    panel.print(g2d);
                    return Printable.PAGE_EXISTS;
                }
            }, pf);

            if (job.printDialog()) {
                job.print();
                JOptionPane.showMessageDialog(this,
                        "Payroll slip printed successfully!",
                        "Print Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error printing: " + ex.getMessage(),
                    "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatCurrency(String value) {
        try {
            double amount = Double.parseDouble(value);
            return String.format("%,.2f", amount);
        } catch (Exception e) {
            return "0.00";
        }
    }

    private String formatNumber(String value) {
        try {
            double num = Double.parseDouble(value);
            return num % 1 == 0 ? String.format("%.0f", num) : String.format("%.1f", num);
        } catch (Exception e) {
            return "0";
        }
    }

    private void showPrintPreview(String epfNo, String name, String section, String designation, String nic,
            String basicSalary, String gradingIncentive, String sunday, String holiday,
            String poyaday, String overtime, String attendanceIncentive, String productionIncentives,
            String arrears, String epf12, String etf3, String epf8, String advance,
            String shortWorkingDays, String noPayDays, String noPayHours, String grossSalary,
            String totalForEPF, String netSalary, String daysWorked, String leave,
            String sundayDays, String holidayDays, String poyadayDays, String normalOT,
            String extraOT, String tribleOT) {

        // Create a custom panel for printing
        JPanel printPanel = new JPanel();
        printPanel.setLayout(new BorderLayout());
        printPanel.setBackground(Color.WHITE);
        printPanel.setPreferredSize(new Dimension(400, 600)); // A4 half size

        // Header
        JLabel headerLabel = new JLabel("DR FASHIONS", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Employee details panel
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 2));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        addDetailRow(detailsPanel, "EPF No", epfNo);
        addDetailRow(detailsPanel, "Name", name);
        addDetailRow(detailsPanel, "Section", section);
        addDetailRow(detailsPanel, "Designation", designation);
        addDetailRow(detailsPanel, "NIC Number", nic);
        addDetailRow(detailsPanel, "Month", "September 25");

        // Earnings and Deductions panel
        JPanel earningsDeductionsPanel = new JPanel(new GridLayout(0, 2, 10, 2));
        earningsDeductionsPanel.setBorder(BorderFactory.createTitledBorder("Earnings & Deductions"));

        // Earnings column
        JPanel earningsPanel = new JPanel(new GridLayout(0, 1, 2, 2));
        earningsPanel.setBorder(BorderFactory.createTitledBorder("Earnings"));
        addAmountRow(earningsPanel, "Basic Salary", basicSalary);
        addAmountRow(earningsPanel, "Grading Incentive", gradingIncentive);
        addAmountRow(earningsPanel, "Sunday", sunday);
        addAmountRow(earningsPanel, "Holiday", holiday);
        addAmountRow(earningsPanel, "Poyaday", poyaday);
        addAmountRow(earningsPanel, "Overtime", overtime);
        addAmountRow(earningsPanel, "Attendance Incentive", attendanceIncentive);
        addAmountRow(earningsPanel, "Production Incentives", productionIncentives);
        addAmountRow(earningsPanel, "Arrears", arrears);

        // Deductions column
        JPanel deductionsPanel = new JPanel(new GridLayout(0, 1, 2, 2));
        deductionsPanel.setBorder(BorderFactory.createTitledBorder("Deductions"));
        addAmountRow(deductionsPanel, "EPF 12%", epf12);
        addAmountRow(deductionsPanel, "ETF 3%", etf3);
        addAmountRow(deductionsPanel, "EPF 8%", epf8);
        addAmountRow(deductionsPanel, "Advance", advance);
        addAmountRow(deductionsPanel, "Short Working Days", shortWorkingDays);
        addAmountRow(deductionsPanel, "No Pay Days", noPayDays);
        addAmountRow(deductionsPanel, "No Pay Hours", noPayHours);

        earningsDeductionsPanel.add(earningsPanel);
        earningsDeductionsPanel.add(deductionsPanel);

        // Totals panel
        JPanel totalsPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        totalsPanel.setBorder(BorderFactory.createTitledBorder("Salary Summary"));
        addAmountRow(totalsPanel, "Gross Salary", grossSalary);
        addAmountRow(totalsPanel, "Total For EPF", totalForEPF);
        addAmountRow(totalsPanel, "Net Salary", netSalary);

        // Data panel
        JPanel dataPanel = new JPanel(new GridLayout(0, 4, 5, 2));
        dataPanel.setBorder(BorderFactory.createTitledBorder("Attendance Data"));

        addDataRow(dataPanel, "Days Worked", daysWorked, "Normal OT", normalOT);
        addDataRow(dataPanel, "Leave", leave, "Extra OT", extraOT);
        addDataRow(dataPanel, "Sunday", sundayDays, "Trible OT", tribleOT);
        addDataRow(dataPanel, "Holiday", holidayDays, "", "");
        addDataRow(dataPanel, "Poyaday", poyadayDays, "", "");

        // Footer
        JPanel footerPanel = new JPanel(new GridLayout(0, 1, 2, 2));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addDetailRow(footerPanel, "EPF No", epfNo);
        addDetailRow(footerPanel, "Name", name);
        addDetailRow(footerPanel, "NIC Number", nic);
        addDetailRow(footerPanel, "Month", "September 25");
        addDetailRow(footerPanel, "Salary Amount", netSalary);

        JLabel signatureLabel = new JLabel("Signature: ___________________");
        signatureLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerPanel.add(signatureLabel);

        // Add all panels to main print panel
        printPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(detailsPanel);
        contentPanel.add(earningsDeductionsPanel);
        contentPanel.add(totalsPanel);
        contentPanel.add(dataPanel);
        contentPanel.add(footerPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setPreferredSize(new Dimension(380, 500));
        printPanel.add(scrollPane, BorderLayout.CENTER);

        // Show print dialog
        int option = JOptionPane.showConfirmDialog(this, printPanel,
                "Payroll Print Preview - " + name,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            printPanel(printPanel, name + " - Payroll");
        }
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JLabel labelLabel = new JLabel(label + ":");
        labelLabel.setFont(new Font("Arial", Font.BOLD, 10));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        panel.add(labelLabel);
        panel.add(valueLabel);
    }

    private void addAmountRow(JPanel panel, String label, String amount) {
        JLabel labelLabel = new JLabel(label + ":");
        labelLabel.setFont(new Font("Arial", Font.PLAIN, 9));

        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 9));
        amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(labelLabel);
        panel.add(amountLabel);
    }

    private void addDataRow(JPanel panel, String label1, String value1, String label2, String value2) {
        JLabel label1Label = new JLabel(label1);
        label1Label.setFont(new Font("Arial", Font.PLAIN, 9));

        JLabel value1Label = new JLabel(value1);
        value1Label.setFont(new Font("Arial", Font.PLAIN, 9));

        JLabel label2Label = new JLabel(label2);
        label2Label.setFont(new Font("Arial", Font.PLAIN, 9));

        JLabel value2Label = new JLabel(value2);
        value2Label.setFont(new Font("Arial", Font.PLAIN, 9));

        panel.add(label1Label);
        panel.add(value1Label);
        panel.add(label2Label);
        panel.add(value2Label);
    }

    private void printPanel(JPanel panel, String jobName) {
        try {
            // Create printer job
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName(jobName);

            // Set page format for A4 half size
            PageFormat pf = job.defaultPage();
            Paper paper = new Paper();
            double width = 8.27 * 72; // A4 width in points (1 inch = 72 points)
            double height = 5.83 * 72; // A4 half height
            paper.setSize(width, height);
            paper.setImageableArea(36, 36, width - 72, height - 72); // 0.5 inch margins
            pf.setPaper(paper);
            pf.setOrientation(PageFormat.PORTRAIT);

            job.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
                    if (pageIndex > 0) {
                        return Printable.NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    // Scale to fit the page
                    double scaleX = pageFormat.getImageableWidth() / panel.getWidth();
                    double scaleY = pageFormat.getImageableHeight() / panel.getHeight();
                    double scale = Math.min(scaleX, scaleY);
                    g2d.scale(scale, scale);

                    panel.print(g2d);
                    return Printable.PAGE_EXISTS;
                }
            }, pf);

            if (job.printDialog()) {
                job.print();
                JOptionPane.showMessageDialog(this,
                        "Payroll printed successfully!",
                        "Print Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error printing: " + ex.getMessage(),
                    "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String createPayrollPrintContent(String... params) {
        return String.format(
                "╔══════════════════════════════════════════════════════════════════════╗\n"
                + "║                             DR FASHIONS                             ║\n"
                + "╠══════════════════════════════════════════════════════════════════════╣\n"
                + "║ EPF No    │ %-50s ║\n"
                + "║ Name      │ %-50s ║\n"
                + "║ Section   │ %-50s ║\n"
                + "║ Designation│ %-49s ║\n"
                + "║ NIC Number│ %-49s ║\n"
                + "║ Month     │ September 25%-37s ║\n"
                + "╠══════════════════════════════════════════════════════════════════════╣\n"
                + "║ %-35s ║ %-25s ║\n"
                + "╠══════════════════════════════════════════════════════════════════════╣\n"
                + "║ %-35s ║ %-25s ║\n"
                + "║ Basic Salary (Including BRA) │ %-25s ║ EPF 12%%        │ %-10s ║\n"
                + "║ Grading Incentive           │ %-25s ║ ETF 3%%         │ %-10s ║\n"
                + "║ Sunday                      │ %-25s ║ EPF 8%%         │ %-10s ║\n"
                + "║ Holiday                     │ %-25s ║ Advance         │ %-10s ║\n"
                + "║ Poyaday                     │ %-25s ║ Short Working Days│ %-8s ║\n"
                + "║ Overtime                    │ %-25s ║ No Pay Days     │ %-8s ║\n"
                + "║ Attendance Incentive        │ %-25s ║ No Pay Hours    │ %-8s ║\n"
                + "║ Production Incentives       │ %-25s ║                 │ %-10s ║\n"
                + "║ Arrears                     │ %-25s ║                 │ %-10s ║\n"
                + "╠══════════════════════════════════════════════════════════════════════╣\n"
                + "║ Gross Salary                │ %-25s ║                 │ %-10s ║\n"
                + "║ Total For EPF               │ %-25s ║                 │ %-10s ║\n"
                + "║                             │ %-25s ║                 │ %-10s ║\n"
                + "║ Net Salary                  │ %-25s ║                 │ %-10s ║\n"
                + "╠══════════════════════════════════════════════════════════════════════╣\n"
                + "║ %-35s ║ %-25s ║\n"
                + "╠══════════════════════════════════════════════════════════════════════╣\n"
                + "║ Days Worked    │ %-8s ║ Normal OT      │ %-8s ║\n"
                + "║ Leave          │ %-8s ║ Extra OT       │ %-8s ║\n"
                + "║ Sunday         │ %-8s ║ Trible OT      │ %-8s ║\n"
                + "║ Holiday        │ %-8s ║                │ %-8s ║\n"
                + "║ Poyaday        │ %-8s ║                │ %-8s ║\n"
                + "╠══════════════════════════════════════════════════════════════════════╣\n"
                + "║ EPF No: %-58s ║\n"
                + "║ Name: %-60s ║\n"
                + "║ NIC Number: %-54s ║\n"
                + "║ Month: September 25%-42s ║\n"
                + "║ Salary Amount: %-50s ║\n"
                + "║                                                                      ║\n"
                + "║ Signature: _________________________                                 ║\n"
                + "╚══════════════════════════════════════════════════════════════════════╝",
                // Parameters
                params[0], params[1], params[2], params[3], params[4], "",
                "Earnings", "Deductions",
                "", "",
                params[5], params[13], // Basic Salary, EPF 12%
                params[6], params[14], // Grading Incentive, ETF 3%
                params[7], params[15], // Sunday, EPF 8%
                params[8], params[16], // Holiday, Advance
                params[9], params[17], // Poyaday, Short Working Days
                params[10], params[18], // Overtime, No Pay Days
                params[11], params[19], // Attendance Incentive, No Pay Hours
                params[12], "", // Production Incentives
                params[13], "", // Arrears
                params[20], "", // Gross Salary
                params[21], "", // Total For EPF
                "", "",
                params[22], "", // Net Salary
                "Data", "",
                params[23], params[29], // Days Worked, Normal OT
                params[24], params[30], // Leave, Extra OT
                params[25], params[31], // Sunday Days, Trible OT
                params[26], "", // Holiday Days
                params[27], "", // Poyaday Days
                params[0], params[1], params[4], "", params[22]
        );
    }

    private void printTextContent(String content) {
        try {
            // Create a temporary file
            java.io.File tempFile = java.io.File.createTempFile("payroll", ".txt");
            try (java.io.PrintWriter writer = new java.io.PrintWriter(tempFile, "UTF-8")) {
                writer.print(content);
            }

            // Print using system default printer
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported(java.awt.Desktop.Action.PRINT)) {
                desktop.print(tempFile);
                JOptionPane.showMessageDialog(this,
                        "Payroll sent to printer successfully!",
                        "Print Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Fallback: Show print dialog with text area
                showPrintDialog(content);
            }

            // Delete temp file after printing (you might want to keep it for debugging)
            tempFile.deleteOnExit();

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to dialog
            showPrintDialog(content);
        }
    }

    private void showPrintDialog(String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 10));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(600, 700));

        // Print option
        int option = JOptionPane.showConfirmDialog(this, scrollPane,
                "Payroll Print Preview - Copy this content manually",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                textArea.print();
            } catch (java.awt.print.PrinterException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error printing: " + ex.getMessage(),
                        "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

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
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    // End of variables declaration//GEN-END:variables
}
