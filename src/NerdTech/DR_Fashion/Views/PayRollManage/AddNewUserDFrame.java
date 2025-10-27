/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddNewUserDFrame extends javax.swing.JDialog {

    private String epfNo;
    private String employeeName;
    private int employeeId;

    public AddNewUserDFrame(java.awt.Frame parent, boolean modal, String epfNo, String employeeName) throws Exception {
        super(parent, modal);
        this.epfNo = epfNo;
        this.employeeName = employeeName;
        initComponents();
        setTitle("Add Salary Details for: " + employeeName + " (" + epfNo + ")");
        loadEmployeeId();
        loadExistingSalaryData();
    }

    private void loadEmployeeId() throws Exception {
        try {
            Connection connection = DatabaseConnection.getConnection();
            String query = "SELECT id FROM employee WHERE epf_no = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, epfNo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                this.employeeId = resultSet.getInt("id");
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Employee not found in database!",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading employee data: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadExistingSalaryData() {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String query = "SELECT basic_salary, grading_incentive, attendance_incentive, production_incentive, "
                    + "peoples_bank_account_no, hnb_bank_account_no "
                    + "FROM salary WHERE employee_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, employeeId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Load existing data into text fields
                jTextField1.setText(String.valueOf(resultSet.getDouble("basic_salary")));
                jTextField2.setText(String.valueOf(resultSet.getDouble("grading_incentive")));
                jTextField3.setText(String.valueOf(resultSet.getDouble("attendance_incentive")));
                jTextField4.setText(String.valueOf(resultSet.getDouble("production_incentive")));

                String peoplesBank = resultSet.getString("peoples_bank_account_no");
                String hnbBank = resultSet.getString("hnb_bank_account_no");

                if (peoplesBank != null) {
                    peoples_bank_account_no.setText(peoplesBank);
                }
                if (hnbBank != null) {
                    hnb_bank_account_no.setText(hnbBank);
                }
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("No existing salary data found for Employee ID: " + employeeId);
        } catch (Exception e) {
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
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        peoples_bank_account_no = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        hnb_bank_account_no = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Add New User");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Basic Salary");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Grading Lncentive");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Attendance Incentive");

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Production Incentive");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Peoples Bank Account No");

        peoples_bank_account_no.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Hnb Bank Account No");

        hnb_bank_account_no.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1))
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1)
                            .addComponent(jTextField2)
                            .addComponent(jTextField3)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addComponent(peoples_bank_account_no)
                            .addComponent(hnb_bank_account_no))))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(peoples_bank_account_no, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(hnb_bank_account_no, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveSalaryDetails();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void saveSalaryDetails() {
        try {
            // Validate input - salary fields required, bank accounts optional
            if (jTextField1.getText().trim().isEmpty()
                    || jTextField2.getText().trim().isEmpty()
                    || jTextField3.getText().trim().isEmpty()
                    || jTextField4.getText().trim().isEmpty()) {

                javax.swing.JOptionPane.showMessageDialog(this,
                        "Please fill all salary fields!",
                        "Validation Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse salary values
            double basicSalary = Double.parseDouble(jTextField1.getText().trim());
            double gradingIncentive = Double.parseDouble(jTextField2.getText().trim());
            double attendanceIncentive = Double.parseDouble(jTextField3.getText().trim());
            double productionIncentive = Double.parseDouble(jTextField4.getText().trim());

            // Get bank account numbers (can be empty)
            String peoplesBank = peoples_bank_account_no.getText().trim();
            String hnbBank = hnb_bank_account_no.getText().trim();

            // If empty, set to null for database
            if (peoplesBank.isEmpty()) {
                peoplesBank = null;
            }
            if (hnbBank.isEmpty()) {
                hnbBank = null;
            }

            // BRA values - automatic values
            double bra1 = 1000.0;
            double bra2 = 2500.0;

            Connection connection = DatabaseConnection.getConnection();

            // Check if record already exists for this employee
            boolean recordExists = checkSalaryRecordExists(connection);

            String query;
            PreparedStatement statement;

            if (recordExists) {
                // Update existing record with bank accounts and BRA values
                query = "UPDATE salary SET basic_salary = ?, grading_incentive = ?, "
                        + "attendance_incentive = ?, production_incentive = ?, "
                        + "peoples_bank_account_no = ?, hnb_bank_account_no = ?, "
                        + "bra_1 = ?, bra_2 = ?, "
                        + "last_modified = CURRENT_TIMESTAMP "
                        + "WHERE employee_id = ?";

                statement = connection.prepareStatement(query);
                statement.setDouble(1, basicSalary);
                statement.setDouble(2, gradingIncentive);
                statement.setDouble(3, attendanceIncentive);
                statement.setDouble(4, productionIncentive);
                statement.setString(5, peoplesBank);
                statement.setString(6, hnbBank);
                statement.setDouble(7, bra1);
                statement.setDouble(8, bra2);
                statement.setInt(9, employeeId);
            } else {
                // Insert new record with bank accounts and BRA values
                query = "INSERT INTO salary (employee_id, basic_salary, grading_incentive, "
                        + "attendance_incentive, production_incentive, peoples_bank_account_no, "
                        + "hnb_bank_account_no, bra_1, bra_2, last_modified) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

                statement = connection.prepareStatement(query);
                statement.setInt(1, employeeId);
                statement.setDouble(2, basicSalary);
                statement.setDouble(3, gradingIncentive);
                statement.setDouble(4, attendanceIncentive);
                statement.setDouble(5, productionIncentive);
                statement.setString(6, peoplesBank);
                statement.setString(7, hnbBank);
                statement.setDouble(8, bra1);
                statement.setDouble(9, bra2);
            }

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Salary details saved successfully for " + employeeName + "!",
                        "Success",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Failed to save salary details!",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            statement.close();
            connection.close();

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for salary fields!",
                    "Validation Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error saving salary details: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error saving salary details: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean checkSalaryRecordExists(Connection connection) {
        try {
            String checkQuery = "SELECT COUNT(*) FROM salary WHERE employee_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, employeeId);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            rs.close();
            checkStmt.close();

            return count > 0;
        } catch (SQLException e) {
            return false;
        }
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
            java.util.logging.Logger.getLogger(AddNewUserDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewUserDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewUserDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewUserDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddNewUserDFrame dialog = null;
                try {
                    dialog = new AddNewUserDFrame(new javax.swing.JFrame(), true, "TEST123", "Test Employee");
                } catch (Exception ex) {
                    Logger.getLogger(AddNewUserDFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField hnb_bank_account_no;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField peoples_bank_account_no;
    // End of variables declaration//GEN-END:variables
}
