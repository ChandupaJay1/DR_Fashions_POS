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

            // Fixed query - only columns that exist in salary table
            String query = "SELECT e.id, sal.basic_salary, sal.bra1, sal.bra2, "
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
                basicSalaryField.setText(resultSet.wasNull() ? "" : String.valueOf(basicSalary));

                String peoplesBank = resultSet.getString("peoples_bank_account_no");
                peoplesBankField.setText(peoplesBank != null ? peoplesBank : "");

                String hnbBank = resultSet.getString("hnb_bank_account_no");
                hnbBankField.setText(hnbBank != null ? hnbBank : "");

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
            String basicSalaryStr = basicSalaryField.getText().trim();
            String peoplesBank = peoplesBankField.getText().trim();
            String hnbBank = hnbBankField.getText().trim();

            // Parse salary value
            Double basicSalary = basicSalaryStr.isEmpty() ? null : Double.parseDouble(basicSalaryStr);

            // Auto BRA values
            double bra1 = 1000.0;
            double bra2 = 2500.0;

            Connection connection = DatabaseConnection.getConnection();

            // Update query with auto BRA values
            String updateQuery = "UPDATE salary SET "
                    + "basic_salary = ?, "
                    + "bra1 = ?, "
                    + "bra2 = ?, "
                    + "peoples_bank_account_no = ?, "
                    + "hnb_bank_account_no = ? "
                    + "WHERE employee_id = ?";

            PreparedStatement statement = connection.prepareStatement(updateQuery);

            if (basicSalary != null) {
                statement.setDouble(1, basicSalary);
            } else {
                statement.setNull(1, Types.DOUBLE);
            }

            statement.setDouble(2, bra1);  // Auto BRA1
            statement.setDouble(3, bra2);  // Auto BRA2

            if (!peoplesBank.isEmpty()) {
                statement.setString(4, peoplesBank);
            } else {
                statement.setNull(4, Types.VARCHAR);
            }

            if (!hnbBank.isEmpty()) {
                statement.setString(5, hnbBank);
            } else {
                statement.setNull(5, Types.VARCHAR);
            }

            statement.setInt(6, employeeId);

            int rowsUpdated = statement.executeUpdate();

            statement.close();
            connection.close();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this,
                        "Salary details updated successfully!\n\n"
                        + "BRA 1: Rs. " + bra1 + "\n"
                        + "BRA 2: Rs. " + bra2,
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
        basicSalaryField = new javax.swing.JTextField();
        peoplesBankField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        hnbBankField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Update Salary Details");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Basic Salary");

        basicSalaryField.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        peoplesBankField.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Peoples Bank Account No");

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Hnb Bank Account No");

        hnbBankField.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
                                .addComponent(basicSalaryField, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(peoplesBankField, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(hnbBankField, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(231, 231, 231)
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
                    .addComponent(basicSalaryField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(peoplesBankField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(hnbBankField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
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
    private javax.swing.JTextField basicSalaryField;
    private javax.swing.JTextField hnbBankField;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField peoplesBankField;
    // End of variables declaration//GEN-END:variables
}
