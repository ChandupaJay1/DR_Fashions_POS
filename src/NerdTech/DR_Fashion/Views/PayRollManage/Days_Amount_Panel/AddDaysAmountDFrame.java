/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage.Days_Amount_Panel;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddDaysAmountDFrame extends javax.swing.JDialog {

    private int epfNo;
    private String employeeName;
    private Days_Amount_Panel parentPanel;

    /**
     * Creates new form AddDaysAmountDFrame
     */
    // Updated constructor
    public AddDaysAmountDFrame(java.awt.Frame parent, boolean modal, int epfNo, String employeeName,
            Days_Amount_Panel parentPanel, int sundayDays, int poyaDays, int holidayDays,
            double workingDayAmount, double sundayAmount, double poyaDayAmount, double holidayAmount) {
        super(parent, modal);
        this.epfNo = epfNo;
        this.employeeName = employeeName;
        this.parentPanel = parentPanel;
        initComponents();
        setEmployeeDetails();

        // Existing values set කරන්න text fields වලට
        jTextField5.setText(String.valueOf(sundayDays));
        jTextField6.setText(String.valueOf(poyaDays));
        jTextField7.setText(String.valueOf(holidayDays));
        jTextField1.setText(String.valueOf(workingDayAmount));
        jTextField3.setText(String.valueOf(sundayAmount));
        jTextField2.setText(String.valueOf(poyaDayAmount));
        jTextField4.setText(String.valueOf(holidayAmount));

        // Auto calculation setup කරන්න
        setupAutoCalculation();

        // Dialog open වෙද්දිම amounts calculate කරන්න
        calculateAmounts();
    }

    /**
     * Auto calculation setup කිරීම
     */
    private void setupAutoCalculation() {
        // Sunday days field එකට listener එකතු කිරීම
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateAmounts();
            }
        });

        // Poya days field එකට listener එකතු කිරීම
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateAmounts();
            }
        });

        // Holiday days field එකට listener එකතු කිරීම
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateAmounts();
            }
        });
    }

    // Amounts ගණනය කිරීමේ method එක - UPDATED
    private void calculateAmounts() {
        try {
            // Basic salary එක database එකෙන් ගන්න
            double basicSalary = getBasicSalaryFromDatabase();
            if (basicSalary == 0) {
                return; // Basic salary නැත්නම් stop කරන්න
            }

            // Working day amount ගණනය කිරීම (මාසයේ දින 26ක් ලෙස)
            double dailyRate = basicSalary / 26;
            jTextField1.setText(String.format("%.2f", dailyRate));

            // Sunday amount ගණනය කිරීම (1.5 ගුණයක්)
            if (!jTextField5.getText().trim().isEmpty()) {
                int sundayDays = Integer.parseInt(jTextField5.getText().trim());
                double sundayAmount = dailyRate * 1.5 * sundayDays;
                jTextField3.setText(String.format("%.2f", sundayAmount));
            } else {
                jTextField3.setText("0.00");
            }

            // Poya Day amount ගණනය කිරීම (2න් බෙදන්න) - UPDATED
            if (!jTextField6.getText().trim().isEmpty()) {
                int poyaDays = Integer.parseInt(jTextField6.getText().trim());
                // Poya Day amount = (Basic Salary / 26) × Poya Days ÷ 2
                double poyaDayAmount = dailyRate * poyaDays / 2;
                jTextField2.setText(String.format("%.2f", poyaDayAmount));
            } else {
                jTextField2.setText("0.00");
            }

            // Holiday amount ගණනය කිරීම (1 ගුණයක්)
            if (!jTextField7.getText().trim().isEmpty()) {
                int holidayDays = Integer.parseInt(jTextField7.getText().trim());
                double holidayAmount = dailyRate * 1 * holidayDays;
                jTextField4.setText(String.format("%.2f", holidayAmount));
            } else {
                jTextField4.setText("0.00");
            }

        } catch (NumberFormatException e) {
            // දින ගණන ඇතුලත් කර නැති විට හෝ invalid numbers
            jTextField3.setText("0.00");
            jTextField2.setText("0.00");
            jTextField4.setText("0.00");
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error calculating amounts: " + e.getMessage(),
                    "Calculation Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Database එකෙන් basic salary එක ලබා ගන්න
     */
    private double getBasicSalaryFromDatabase() {
        double basicSalary = 0.0;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT basic_salary FROM salary WHERE employee_id = (SELECT id FROM employee WHERE epf_no = ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, epfNo);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                basicSalary = rs.getDouble("basic_salary");
            } else {
                // Salary record එක නැතිවිට warning message එකක් දක්වන්න
                basicSalary = 0.0;
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Basic salary not found for EPF: " + epfNo + "\nPlease set basic salary first!",
                        "Salary Not Found",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error retrieving basic salary: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return basicSalary;
    }

    /**
     * Employee details set කරන්න
     */
    private void setEmployeeDetails() {
        // Employee details display කිරීමට අවශ්‍ය නම් මෙහි code එක add කරන්න
        setTitle("Add Days & Amount - EPF: " + epfNo + " - " + employeeName);
    }

    /**
     * Save button action
     */
    private void saveDaysAndAmounts() {
        try {
            // Text fields වලින් values get කරන්න
            int sundayDays = Integer.parseInt(jTextField5.getText().trim());
            int poyaDays = Integer.parseInt(jTextField6.getText().trim());
            int holidayDays = Integer.parseInt(jTextField7.getText().trim());
            double workingDayAmount = Double.parseDouble(jTextField1.getText().trim());
            double sundayAmount = Double.parseDouble(jTextField3.getText().trim());
            double poyaDayAmount = Double.parseDouble(jTextField2.getText().trim());
            double holidayAmount = Double.parseDouble(jTextField4.getText().trim());

            // Parent panel එකට data pass කරන්න
            parentPanel.addDaysAndAmounts(epfNo, sundayDays, poyaDays, holidayDays,
                    workingDayAmount, sundayAmount, poyaDayAmount, holidayAmount);

            // Success message
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Days and amounts added successfully!",
                    "Success",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

            // Dialog close කරන්න
            this.dispose();

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers in all fields!",
                    "Invalid Input",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
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
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Add Days & Amount Details");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Sunday");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Poya Day");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Holiday");

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Working Day Amount");

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Sunday Amount");

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Poya Day Amount");

        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel8.setText("Holiday Amount");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7))
                                .addGap(104, 104, 104)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField1)
                                    .addComponent(jTextField2)
                                    .addComponent(jTextField5)
                                    .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
                                .addGap(45, 45, 45)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8))
                                .addGap(116, 116, 116)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField4)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(542, 542, 542)
                        .addComponent(jButton1)))
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
                    .addComponent(jLabel3)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveDaysAndAmounts();
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
            java.util.logging.Logger.getLogger(AddDaysAmountDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddDaysAmountDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddDaysAmountDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddDaysAmountDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // This main method is not used when called from Days_Amount_Panel
                // AddDaysAmountDFrame dialog = new AddDaysAmountDFrame(new javax.swing.JFrame(), true, 0, "Test", null, 0, 0, 0, 0, 0, 0, 0);
                // dialog.setVisible(true);
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
