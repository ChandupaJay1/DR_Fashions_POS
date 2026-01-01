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
import java.text.SimpleDateFormat;
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
        populateStatusComboBox();

        System.out.println("=== ResignTableDFrame Debug Info ===");
        System.out.println("Employee ID: " + employeeId);
        System.out.println("EPF No: " + epfNo);
        System.out.println("Joined Date: " + joinedDate);
        System.out.println("===================================");
    }

    /**
     * ✅ ComboBox එකට Status values add කරන්න
     */
    private void populateStatusComboBox() {
        statusComboBox.removeAllItems();
        statusComboBox.addItem("Inactive");
        statusComboBox.addItem("Pending");
    }

    /**
     * ✅ Calculate service duration between two dates Returns format: "X Years,
     * Y Months, Z Days"
     */
    private String calculateServiceDuration(String startDate, String endDate) {
        try {
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

            LocalDate start = null;
            LocalDate end = null;

            // Try multiple date formats
            try {
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                start = LocalDate.parse(startDate.trim(), formatter1);
                end = LocalDate.parse(endDate.trim(), formatter1);
            } catch (Exception e1) {
                try {
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    start = LocalDate.parse(startDate.trim(), formatter2);
                    end = LocalDate.parse(endDate.trim(), formatter2);
                } catch (Exception e2) {
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
        resignTypeTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        reasonTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        resignDateChooser = new com.toedter.calendar.JDateChooser();
        statusComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Reign Details");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Resign Type");

        resignTypeTextField.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Reason");

        reasonTextField.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Delete");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Resign Date");

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Status");

        statusComboBox.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addComponent(jLabel2)
                                            .addGap(143, 143, 143))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel4)
                                            .addGap(198, 198, 198)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(257, 257, 257)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(reasonTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                                    .addComponent(resignTypeTextField)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(214, 214, 214)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(202, 202, 202)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(statusComboBox, 0, 299, Short.MAX_VALUE)
                            .addComponent(resignDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(resignTypeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(reasonTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(resignDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // ✅ Validate inputs
        String resignType = resignTypeTextField.getText().trim();
        String reason = reasonTextField.getText().trim();
        java.util.Date selectedDate = resignDateChooser.getDate();
        String selectedStatus = (String) statusComboBox.getSelectedItem();

        if (resignType.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter Resign Type!",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            resignTypeTextField.requestFocus();
            return;
        }

        if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter Reason!",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            reasonTextField.requestFocus();
            return;
        }

        // ✅ Validate date selection
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a Resign Date!",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ✅ Validate status selection
        if (selectedStatus == null || selectedStatus.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select a Status!",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ✅ Convert date to string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String resignDate = sdf.format(selectedDate);

        // Confirm action
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to resign this employee?\n"
                + "EPF No: " + epfNo + "\n"
                + "Resign Type: " + resignType + "\n"
                + "Resign Date: " + resignDate + "\n"
                + "Status: " + selectedStatus + "\n"
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
            System.out.println("Resign Date (Selected): " + resignDate);

            String serviceDuration = calculateServiceDuration(joinedDate, resignDate);

            System.out.println("Final Service Duration: " + serviceDuration);
            System.out.println("===========================================\n");

            // 1. ✅ Insert into resignation table with selected status
            String insertSql = "INSERT INTO resignation (employee_id, resign_type, resign_date, reason, service_duration, status) VALUES (?, ?, ?, ?, ?, ?)";
            psInsert = conn.prepareStatement(insertSql);
            psInsert.setInt(1, employeeId);
            psInsert.setString(2, resignType);
            psInsert.setString(3, resignDate);
            psInsert.setString(4, reason);
            psInsert.setString(5, serviceDuration);
            psInsert.setString(6, selectedStatus); // ✅ Selected status save වෙනවා

            int insertResult = psInsert.executeUpdate();

            if (insertResult > 0) {
                // 2. ✅ Update employee status based on selected status in resignation
                String employeeStatus = "";

                // Select කරන status එක අනුව employee status set කරන්න
                if (selectedStatus.equalsIgnoreCase("Inactive")) {
                    employeeStatus = "inactive";
                } else if (selectedStatus.equalsIgnoreCase("Pending")) {
                    employeeStatus = "pending";
                } else {
                    employeeStatus = "inactive"; // Default
                }

                String updateSql = "UPDATE employee SET status = ? WHERE id = ?";
                psUpdate = conn.prepareStatement(updateSql);
                psUpdate.setString(1, employeeStatus);
                psUpdate.setInt(2, employeeId);

                int updateResult = psUpdate.executeUpdate();

                if (updateResult > 0) {
                    conn.commit(); // Commit transaction

                    JOptionPane.showMessageDialog(this,
                            "Employee resigned successfully!\n\n"
                            + "EPF No: " + epfNo + "\n"
                            + "Resign Date: " + resignDate + "\n"
                            + "Resignation Status: " + selectedStatus + "\n"
                            + "Employee Status: " + employeeStatus + "\n"
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

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField reasonTextField;
    private com.toedter.calendar.JDateChooser resignDateChooser;
    private javax.swing.JTextField resignTypeTextField;
    private javax.swing.JComboBox<String> statusComboBox;
    // End of variables declaration//GEN-END:variables
}
