package NerdTech.DR_Fashion.Views.PayRollManage;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewUserPanel extends javax.swing.JPanel {

    private String selectedEpfNo;
    private String selectedEmployeeName;

    public NewUserPanel() {
        initComponents();
        loadEmployeeData(); // Load data when panel is created
        setupTableDoubleClick();
    }

    private void setupTableDoubleClick() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double click
                    int row = jTable1.getSelectedRow();
                    if (row != -1) {
                        selectedEpfNo = jTable1.getValueAt(row, 0).toString();
                        selectedEmployeeName = jTable1.getValueAt(row, 1).toString();

                        try {
                            // Open the dialog to add salary details
                            openAddNewUserDialog();
                        } catch (Exception ex) {
                            Logger.getLogger(NewUserPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }

    private void openAddNewUserDialog() throws Exception {
        AddNewUserDFrame dialog = new AddNewUserDFrame(null, true, selectedEpfNo, selectedEmployeeName);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        // Refresh table data after dialog is closed
        loadEmployeeData();
    }

    private void loadEmployeeData() {
        try {
            // Use your DatabaseConnection class to get connection
            Connection connection = DatabaseConnection.getConnection();

            // Query with proper join using employee.id = salary.employee_id
            String query = "SELECT e.epf_no, e.fname, sec.section_name, d.title, e.nic, "
                    + "sal.basic_salary, sal.grading_incentive, sal.attendance_incentive, sal.production_incentive "
                    + "FROM employee e "
                    + "LEFT JOIN section sec ON e.section_id = sec.id "
                    + "LEFT JOIN designation d ON e.designation_id = d.id "
                    + "LEFT JOIN salary sal ON e.id = sal.employee_id " // CHANGED: e.id = sal.employee_id
                    + "WHERE (e.status = 'active' OR e.status IS NULL) "
                    + "ORDER BY e.epf_no";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Get the table model
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

            // Clear existing data
            model.setRowCount(0);

            // Add data to the table
            while (resultSet.next()) {
                String epfNo = resultSet.getString("epf_no");
                String name = resultSet.getString("fname");
                String section = resultSet.getString("section_name");
                String designation = resultSet.getString("title");
                String nic = resultSet.getString("nic");

                // Get salary details - they might be null if not set yet
                Double basicSalary = resultSet.getDouble("basic_salary");
                if (resultSet.wasNull()) {
                    basicSalary = null;
                }

                Double gradingIncentive = resultSet.getDouble("grading_incentive");
                if (resultSet.wasNull()) {
                    gradingIncentive = null;
                }

                Double attendanceIncentive = resultSet.getDouble("attendance_incentive");
                if (resultSet.wasNull()) {
                    attendanceIncentive = null;
                }

                Double productionIncentive = resultSet.getDouble("production_incentive");
                if (resultSet.wasNull()) {
                    productionIncentive = null;
                }

                // Add row to table
                model.addRow(new Object[]{
                    epfNo,
                    name,
                    section,
                    designation,
                    nic,
                    basicSalary, // Basic Salary
                    gradingIncentive, // Grading Incentive
                    attendanceIncentive, // Attendance Incentive
                    productionIncentive // Production Incentive
                });
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            // Handle specific SQL errors
            if (e.getMessage().contains("Unknown column") || e.getMessage().contains("doesn't exist")) {
                // Salary table might not exist or have wrong structure
                loadEmployeeDataWithoutSalary();
            } else {
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Error loading employee data: " + e.getMessage(),
                        "Database Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading employee data: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEmployeeDataWithoutSalary() {
        try {
            Connection connection = DatabaseConnection.getConnection();

            // Load only employee data without salary information
            String query = "SELECT e.epf_no, e.fname, sec.section_name, d.title, e.nic "
                    + "FROM employee e "
                    + "LEFT JOIN section sec ON e.section_id = sec.id "
                    + "LEFT JOIN designation d ON e.designation_id = d.id "
                    + "WHERE (e.status = 'active' OR e.status IS NULL) "
                    + "ORDER BY e.epf_no";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                String epfNo = resultSet.getString("epf_no");
                String name = resultSet.getString("fname");
                String section = resultSet.getString("section_name");
                String designation = resultSet.getString("title");
                String nic = resultSet.getString("nic");

                // Add row to table with null salary columns
                model.addRow(new Object[]{
                    epfNo,
                    name,
                    section,
                    designation,
                    nic,
                    null, // Basic Salary
                    null, // Grading Incentive
                    null, // Attendance Incentive
                    null // Production Incentive
                });
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading basic employee data: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("New Uesr Details");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "EPF No", "Name", "Section", "Designation", "NIC", "Basic Salary", "Grading Lncentive", "Attendance Incentive", "Production Incentive"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1414, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(138, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
