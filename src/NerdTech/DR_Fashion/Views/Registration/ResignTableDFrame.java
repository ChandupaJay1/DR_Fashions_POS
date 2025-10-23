/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Registration;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResignTableDFrame extends javax.swing.JDialog {

    private int employeeId;
    private String epfNo;
    private String joinedDate;
    private EmployeeRegistration employeeRegistrationPanel;

    public ResignTableDFrame(java.awt.Frame parent, boolean modal, int employeeId, String epfNo, String joinedDate, EmployeeRegistration employeeRegistrationPanel) {
        super(parent, modal);
        this.employeeId = employeeId;
        this.epfNo = epfNo;
        this.joinedDate = joinedDate;
        this.employeeRegistrationPanel = employeeRegistrationPanel;
        initComponents();

        System.out.println("=== ResignTableDFrame Debug Info ===");
        System.out.println("Employee ID: " + employeeId);
        System.out.println("EPF No: " + epfNo);
        System.out.println("Joined Date: " + joinedDate);
        System.out.println("===================================");
    }

    /**
     * ✅ Calculate service duration between two dates Returns format: "X Years,
     * Y Months, Z Days"
     */
    private String calculateServiceDuration(String startDate, String endDate) {
        try {
            // Validate inputs
            if (startDate == null || startDate.trim().isEmpty() || startDate.equalsIgnoreCase("null")) {
                System.err.println("❌ ERROR: startDate is null or empty");
                return "N/A - Missing Start Date";
            }

            if (endDate == null || endDate.trim().isEmpty() || endDate.equalsIgnoreCase("null")) {
                System.err.println("❌ ERROR: endDate is null or empty");
                return "N/A - Missing End Date";
            }

            System.out.println("📅 Start Date: " + startDate);
            System.out.println("📅 End Date: " + endDate);

            // Try multiple date formats
            LocalDate start = null;
            LocalDate end = null;

            // Format 1: yyyy-MM-dd
            try {
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                start = LocalDate.parse(startDate.trim(), formatter1);
                end = LocalDate.parse(endDate.trim(), formatter1);
            } catch (Exception e1) {
                // Format 2: dd-MM-yyyy
                try {
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    start = LocalDate.parse(startDate.trim(), formatter2);
                    end = LocalDate.parse(endDate.trim(), formatter2);
                } catch (Exception e2) {
                    // Format 3: dd/MM/yyyy
                    try {
                        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        start = LocalDate.parse(startDate.trim(), formatter3);
                        end = LocalDate.parse(endDate.trim(), formatter3);
                    } catch (Exception e3) {
                        System.err.println("❌ ERROR: Cannot parse dates. Start: " + startDate + ", End: " + endDate);
                        e3.printStackTrace();
                        return "Invalid Date Format";
                    }
                }
            }

            if (start == null || end == null) {
                return "Date Parsing Failed";
            }

            // Calculate period
            Period period = Period.between(start, end);

            int years = period.getYears();
            int months = period.getMonths();
            int days = period.getDays();

            System.out.println("⏳ Calculated Duration: " + years + " years, " + months + " months, " + days + " days");

            StringBuilder duration = new StringBuilder();

            if (years > 0) {
                duration.append(years).append(" Year").append(years > 1 ? "s" : "");
            }
            if (months > 0) {
                if (duration.length() > 0) {
                    duration.append(", ");
                }
                duration.append(months).append(" Month").append(months > 1 ? "s" : "");
            }
            if (days > 0) {
                if (duration.length() > 0) {
                    duration.append(", ");
                }
                duration.append(days).append(" Day").append(days > 1 ? "s" : "");
            }

            String result = duration.length() > 0 ? duration.toString() : "0 Days";
            System.out.println("✅ Final Duration String: " + result);
            return result;

        } catch (Exception e) {
            System.err.println("❌ EXCEPTION in calculateServiceDuration:");
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Reign Details");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Resign Type");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Reason");

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Delete");
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
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(143, 143, 143))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(198, 198, 198)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                                    .addComponent(jTextField1)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(213, 213, 213)
                        .addComponent(jButton1)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Validate inputs
        String resignType = jTextField1.getText().trim();
        String reason = jTextField3.getText().trim();

        if (resignType.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter Resign Type!",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            jTextField1.requestFocus();
            return;
        }

        if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter Reason!",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            jTextField3.requestFocus();
            return;
        }

        // Confirm action
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to resign this employee?\n"
                + "EPF No: " + epfNo + "\n"
                + "Resign Type: " + resignType + "\n"
                + "Reason: " + reason,
                "Confirm Resignation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;

        try {
            conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // ✅ Get today's date for resignation
            LocalDate today = LocalDate.now();
            String resignDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // ✅ Validate joined date before calculation
            if (joinedDate == null || joinedDate.trim().isEmpty() || joinedDate.equalsIgnoreCase("null")) {
                JOptionPane.showMessageDialog(this,
                        "Cannot resign employee: Joined Date is missing!\n"
                        + "Please update the employee record with a valid Joined Date.",
                        "Missing Data",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ✅ Calculate service duration automatically
            System.out.println("\n=== Starting Service Duration Calculation ===");
            System.out.println("Joined Date from Constructor: " + joinedDate);
            System.out.println("Resign Date (Today): " + resignDate);

            String serviceDuration = calculateServiceDuration(joinedDate, resignDate);

            System.out.println("Final Service Duration: " + serviceDuration);
            System.out.println("===========================================\n");

            // 1. Insert into resignation table with auto-calculated service_duration
            String insertSql = "INSERT INTO resignation (employee_id, resign_type, resign_date, reason, service_duration) VALUES (?, ?, ?, ?, ?)";
            psInsert = conn.prepareStatement(insertSql);
            psInsert.setInt(1, employeeId);
            psInsert.setString(2, resignType);
            psInsert.setString(3, resignDate);
            psInsert.setString(4, reason);
            psInsert.setString(5, serviceDuration); // ✅ Auto-calculated duration

            int insertResult = psInsert.executeUpdate();

            if (insertResult > 0) {
                // 2. Update employee status to inactive
                String updateSql = "UPDATE employee SET status = 'inactive' WHERE id = ?";
                psUpdate = conn.prepareStatement(updateSql);
                psUpdate.setInt(1, employeeId);

                int updateResult = psUpdate.executeUpdate();

                if (updateResult > 0) {
                    conn.commit(); // Commit transaction

                    JOptionPane.showMessageDialog(this,
                            "Employee resigned successfully!\n\n"
                            + "EPF No: " + epfNo + "\n"
                            + "Resign Date: " + resignDate + "\n"
                            + "Service Duration: " + serviceDuration,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Refresh the parent table
                    if (employeeRegistrationPanel != null) {
                        employeeRegistrationPanel.refreshTable();
                    }

                    // Close this dialog
                    dispose();
                } else {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this,
                            "Failed to update employee status!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                conn.rollback();
                JOptionPane.showMessageDialog(this,
                        "Failed to save resignation details!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            JOptionPane.showMessageDialog(this,
                    "Database error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();

        } catch (Exception ex) {
            Logger.getLogger(ResignTableDFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Close resources
            try {
                if (psInsert != null) {
                    psInsert.close();
                }
                if (psUpdate != null) {
                    psUpdate.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
            java.util.logging.Logger.getLogger(ResignTableDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ResignTableDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ResignTableDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ResignTableDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Test with sample data
                EmployeeRegistration empReg = new EmployeeRegistration();
                ResignTableDFrame dialog = new ResignTableDFrame(new javax.swing.JFrame(), true, 1, "EPF001", "2020-01-15", empReg);
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
