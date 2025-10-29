/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage.NewUser;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author MG_Pathum
 */
public class UpdateSalaryDFrame extends javax.swing.JDialog {

    private String epfNo;
    private String employeeName;
    private int employeeId;

    /**
     * Creates new form UpdateSalaryDFrame
     */
    public UpdateSalaryDFrame(java.awt.Frame parent, boolean modal, String epfNo, String employeeName) {
        super(parent, modal);
        this.epfNo = epfNo;
        this.employeeName = employeeName;
        initComponents();
        loadEmployeeSalaryDetails();
    }

    private void loadEmployeeSalaryDetails() {
        try {
            Connection connection = DatabaseConnection.getConnection();

            String query = "SELECT e.id, sal.basic_salary, sal.grading_incentive, "
                    + "sal.attendance_incentive, sal.production_incentive, "
                    + "sal.peoples_bank_account_no, sal.hnb_bank_account_no "
                    + "FROM employee e "
                    + "INNER JOIN salary sal ON e.id = sal.employee_id "
                    + "WHERE e.epf_no = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, epfNo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                employeeId = resultSet.getInt("id");

                // Load salary details into text fields
                Double basicSalary = resultSet.getDouble("basic_salary");
                jTextField1.setText(resultSet.wasNull() ? "" : String.valueOf(basicSalary));

                Double gradingIncentive = resultSet.getDouble("grading_incentive");
                jTextField2.setText(resultSet.wasNull() ? "" : String.valueOf(gradingIncentive));

                Double attendanceIncentive = resultSet.getDouble("attendance_incentive");
                jTextField3.setText(resultSet.wasNull() ? "" : String.valueOf(attendanceIncentive));

                Double productionIncentive = resultSet.getDouble("production_incentive");
                jTextField4.setText(resultSet.wasNull() ? "" : String.valueOf(productionIncentive));

                String peoplesBank = resultSet.getString("peoples_bank_account_no");
                peoples_bank_account_no.setText(peoplesBank != null ? peoplesBank : "");

                String hnbBank = resultSet.getString("hnb_bank_account_no");
                hnb_bank_account_no.setText(hnbBank != null ? hnbBank : "");

                // Set dialog title with employee name
                this.setTitle("Update Salary - " + employeeName + " (" + epfNo + ")");
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading salary details: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading salary details: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSalaryDetails() {
        try {
            // Validate inputs
            String basicSalaryStr = jTextField1.getText().trim();
            String gradingIncentiveStr = jTextField2.getText().trim();
            String attendanceIncentiveStr = jTextField3.getText().trim();
            String productionIncentiveStr = jTextField4.getText().trim();
            String peoplesBank = peoples_bank_account_no.getText().trim();
            String hnbBank = hnb_bank_account_no.getText().trim();

            // Parse salary values
            Double basicSalary = basicSalaryStr.isEmpty() ? null : Double.parseDouble(basicSalaryStr);
            Double gradingIncentive = gradingIncentiveStr.isEmpty() ? null : Double.parseDouble(gradingIncentiveStr);
            Double attendanceIncentive = attendanceIncentiveStr.isEmpty() ? null : Double.parseDouble(attendanceIncentiveStr);
            Double productionIncentive = productionIncentiveStr.isEmpty() ? null : Double.parseDouble(productionIncentiveStr);

            Connection connection = DatabaseConnection.getConnection();

            String updateQuery = "UPDATE salary SET "
                    + "basic_salary = ?, "
                    + "grading_incentive = ?, "
                    + "attendance_incentive = ?, "
                    + "production_incentive = ?, "
                    + "peoples_bank_account_no = ?, "
                    + "hnb_bank_account_no = ? "
                    + "WHERE employee_id = ?";

            PreparedStatement statement = connection.prepareStatement(updateQuery);

            if (basicSalary != null) {
                statement.setDouble(1, basicSalary);
            } else {
                statement.setNull(1, Types.DOUBLE);
            }

            if (gradingIncentive != null) {
                statement.setDouble(2, gradingIncentive);
            } else {
                statement.setNull(2, Types.DOUBLE);
            }

            if (attendanceIncentive != null) {
                statement.setDouble(3, attendanceIncentive);
            } else {
                statement.setNull(3, Types.DOUBLE);
            }

            if (productionIncentive != null) {
                statement.setDouble(4, productionIncentive);
            } else {
                statement.setNull(4, Types.DOUBLE);
            }

            if (!peoplesBank.isEmpty()) {
                statement.setString(5, peoplesBank);
            } else {
                statement.setNull(5, Types.VARCHAR);
            }

            if (!hnbBank.isEmpty()) {
                statement.setString(6, hnbBank);
            } else {
                statement.setNull(6, Types.VARCHAR);
            }

            statement.setInt(7, employeeId);

            int rowsUpdated = statement.executeUpdate();

            statement.close();
            connection.close();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this,
                        "Salary details updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Close dialog after successful update
            } else {
                JOptionPane.showMessageDialog(this,
                        "No records were updated.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values for salary fields.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error updating salary details: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error updating salary details: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        peoples_bank_account_no = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        hnb_bank_account_no = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Update Salary Details");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Basic Salary");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Grading Lncentive");

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Attendance Incentive");

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Production Incentive");

        jTextField4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        peoples_bank_account_no.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Peoples Bank Account No");

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Hnb Bank Account No");

        hnb_bank_account_no.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Update");
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
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1)
                            .addComponent(jTextField2)
                            .addComponent(jTextField3)
                            .addComponent(jTextField4)
                            .addComponent(peoples_bank_account_no)
                            .addComponent(hnb_bank_account_no, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(230, 230, 230)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(54, 54, 54)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        updateSalaryDetails();
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UpdateSalaryDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateSalaryDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateSalaryDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateSalaryDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UpdateSalaryDFrame dialog = new UpdateSalaryDFrame(new javax.swing.JFrame(), true, "TEST001", "Test Employee");
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
