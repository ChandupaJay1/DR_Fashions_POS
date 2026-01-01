/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.*;
import java.sql.*;

public class AddEPFDFrame extends javax.swing.JDialog {

    private String epfNo;
    private String employeeName;
    private Connection connection;

    // Modified constructor to accept data
    public AddEPFDFrame(java.awt.Frame parent, boolean modal, String epfNo, String employeeName,
            String totalForEPF, String epf12, String etf3, String epf8,
            String totalEPFETF, String grossSalary, String netSalary) {
        super(parent, modal);
        this.epfNo = epfNo;
        this.employeeName = employeeName;
        initializeDatabaseConnection();
        initComponents();
        populateFields(totalForEPF, epf12, etf3, epf8, totalEPFETF, grossSalary, netSalary);
        setupCalculations();
    }

    private void initializeDatabaseConnection() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Database Connection Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFields(String totalForEPF, String epf12, String etf3,
            String epf8, String totalEPFETF, String grossSalary, String netSalary) {
        jTextField1.setText(totalForEPF);
        jTextField2.setText(epf12);
        jTextField3.setText(etf3);
        jTextField4.setText(epf8);
        jTextField5.setText(totalEPFETF);
        jTextField6.setText(grossSalary);
        jTextField7.setText(netSalary);

        // Set title with employee info
        jLabel1.setText("Add EPF & ETF - " + employeeName + " (" + epfNo + ")");
    }

    private void setupCalculations() {
        // Auto-calculate EPF 12% when Total For EPF changes
        jTextField1.addCaretListener(e -> calculateEPF12());

        // Auto-calculate ETF 3% when Total For EPF changes  
        jTextField1.addCaretListener(e -> calculateETF3());

        // Auto-calculate EPF 8% when Total For EPF changes
        jTextField1.addCaretListener(e -> calculateEPF8());

        // Auto-calculate Total EPF/ETF
        jTextField1.addCaretListener(e -> calculateTotalEPFETF());
        jTextField2.addCaretListener(e -> calculateTotalEPFETF());
        jTextField3.addCaretListener(e -> calculateTotalEPFETF());
        jTextField4.addCaretListener(e -> calculateTotalEPFETF());

        // Auto-calculate Net Salary
        jTextField6.addCaretListener(e -> calculateNetSalary());
        jTextField5.addCaretListener(e -> calculateNetSalary());
    }

    private void calculateEPF12() {
        try {
            double totalForEPF = Double.parseDouble(jTextField1.getText().isEmpty() ? "0" : jTextField1.getText());
            double epf12 = totalForEPF * 0.12;
            jTextField2.setText(String.format("%.2f", epf12));
        } catch (NumberFormatException e) {
            jTextField2.setText("0");
        }
    }

    private void calculateETF3() {
        try {
            double totalForEPF = Double.parseDouble(jTextField1.getText().isEmpty() ? "0" : jTextField1.getText());
            double etf3 = totalForEPF * 0.03;
            jTextField3.setText(String.format("%.2f", etf3));
        } catch (NumberFormatException e) {
            jTextField3.setText("0");
        }
    }

    private void calculateEPF8() {
        try {
            double totalForEPF = Double.parseDouble(jTextField1.getText().isEmpty() ? "0" : jTextField1.getText());
            double epf8 = totalForEPF * 0.08;
            jTextField4.setText(String.format("%.2f", epf8));
        } catch (NumberFormatException e) {
            jTextField4.setText("0");
        }
    }

    private void calculateTotalEPFETF() {
        try {
            double epf12 = Double.parseDouble(jTextField2.getText().isEmpty() ? "0" : jTextField2.getText());
            double etf3 = Double.parseDouble(jTextField3.getText().isEmpty() ? "0" : jTextField3.getText());
            double epf8 = Double.parseDouble(jTextField4.getText().isEmpty() ? "0" : jTextField4.getText());
            double totalEPFETF = epf12 + etf3 + epf8;
            jTextField5.setText(String.format("%.2f", totalEPFETF));
        } catch (NumberFormatException e) {
            jTextField5.setText("0");
        }
    }

    private void calculateNetSalary() {
        try {
            double grossSalary = Double.parseDouble(jTextField6.getText().isEmpty() ? "0" : jTextField6.getText());
            double totalEPFETF = Double.parseDouble(jTextField5.getText().isEmpty() ? "0" : jTextField5.getText());
            double netSalary = grossSalary - totalEPFETF;
            jTextField7.setText(String.format("%.2f", netSalary));
        } catch (NumberFormatException e) {
            jTextField7.setText("0");
        }
    }

    private void saveEPFDetails() {
        if (connection == null) {
            JOptionPane.showMessageDialog(this,
                    "Database connection is not available",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Get values from text fields
            String totalForEPF = jTextField1.getText();
            String epf12 = jTextField2.getText();
            String etf3 = jTextField3.getText();
            String epf8 = jTextField4.getText();
            String totalEPFETF = jTextField5.getText();
            String grossSalary = jTextField6.getText();
            String netSalary = jTextField7.getText();

            // First, get employee_id from epf_no
            String getEmployeeIdQuery = "SELECT id FROM employee WHERE epf_no = ?";
            PreparedStatement getEmployeeStmt = connection.prepareStatement(getEmployeeIdQuery);
            getEmployeeStmt.setString(1, epfNo);
            ResultSet rs = getEmployeeStmt.executeQuery();

            if (rs.next()) {
                int employeeId = rs.getInt("id");

                // Check if record already exists in epf table for this employee
                String checkQuery = "SELECT id FROM epf WHERE id = ?";
                PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                checkStmt.setInt(1, employeeId);
                ResultSet checkRs = checkStmt.executeQuery();

                if (checkRs.next()) {
                    // Update existing record in epf table
                    String updateQuery = """
                    UPDATE epf 
                    SET total_efp = ?, epf_12 = ?, epf_8 = ?, epf_3 = ?, 
                        `total_epf/etf` = ?, gross_salary = ?, net_salary = ?
                    WHERE id = ?
                    """;

                    PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                    updateStmt.setString(1, totalForEPF);
                    updateStmt.setString(2, epf12);
                    updateStmt.setString(3, epf8);
                    updateStmt.setString(4, etf3);  // Note: epf_3 is ETF 3% in your table
                    updateStmt.setString(5, totalEPFETF);
                    updateStmt.setString(6, grossSalary);
                    updateStmt.setString(7, netSalary);
                    updateStmt.setInt(8, employeeId);

                    int rowsUpdated = updateStmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this,
                                "EPF & ETF details updated successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to update EPF details!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    updateStmt.close();
                } else {
                    // Insert new record into epf table
                    String insertQuery = """
                    INSERT INTO epf 
                    (id, total_efp, epf_12, epf_8, epf_3, `total_epf/etf`, gross_salary, net_salary) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;

                    PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                    insertStmt.setInt(1, employeeId);
                    insertStmt.setString(2, totalForEPF);
                    insertStmt.setString(3, epf12);
                    insertStmt.setString(4, epf8);
                    insertStmt.setString(5, etf3);  // Note: epf_3 is ETF 3% in your table
                    insertStmt.setString(6, totalEPFETF);
                    insertStmt.setString(7, grossSalary);
                    insertStmt.setString(8, netSalary);

                    int rowsInserted = insertStmt.executeUpdate();

                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(this,
                                "EPF & ETF details saved successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to save EPF details!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    insertStmt.close();
                }

                checkRs.close();
                checkStmt.close();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Employee not found with EPF No: " + epfNo,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            getEmployeeStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saving EPF details: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
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
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Add EPF & ETF ");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Total For EPF");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("EPF 12%");

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("ETF 3%");

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("EPF 8%");

        jTextField4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Total EPF/ETF");

        jTextField5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Gross Salary");

        jTextField6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel8.setText("Net Salary");

        jTextField7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Save");
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
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8))
                        .addGap(167, 167, 167)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1)
                            .addComponent(jTextField3)
                            .addComponent(jTextField5)
                            .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                        .addGap(102, 102, 102)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7))
                        .addGap(146, 146, 146)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2)
                            .addComponent(jTextField4)
                            .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(577, 577, 577))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveEPFDetails();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(AddEPFDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddEPFDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddEPFDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddEPFDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Default values සහිතව constructor එක call කරන්න
                AddEPFDFrame dialog = new AddEPFDFrame(
                        new javax.swing.JFrame(),
                        true,
                        "000", // default EPF No
                        "Test Employee", // default employee name  
                        "0", // default Total For EPF
                        "0", // default EPF 12%
                        "0", // default ETF 3%
                        "0", // default EPF 8%
                        "0", // default Total EPF/ETF
                        "0", // default Gross Salary
                        "0" // default Net Salary
                );
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
