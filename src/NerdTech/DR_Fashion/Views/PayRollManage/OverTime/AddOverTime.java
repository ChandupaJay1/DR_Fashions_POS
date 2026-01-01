/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage.OverTime;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddOverTime extends javax.swing.JDialog {

    private String epfNo;
    private String name;
    private OverTimePanel parentPanel;
    private int selectedRow;

    /**
     * Creates new form AddOverTime with current values
     */
    public AddOverTime(java.awt.Frame parent, boolean modal, String epfNo, String name,
            OverTimePanel parentPanel, int selectedRow,
            double currentExtra, double currentTrible,
            double currentNormalAmount, double currentExtraAmount, double currentTribleAmount) {
        super(parent, modal);
        this.epfNo = epfNo;
        this.name = name;
        this.parentPanel = parentPanel;
        this.selectedRow = selectedRow;

        System.out.println("🔷 AddOverTime Dialog Constructor Called");
        System.out.println("  EPF: " + epfNo + ", Name: " + name);
        System.out.println("  Row: " + selectedRow);
        System.out.println("  Parent Panel: " + (parentPanel != null ? "NOT NULL ✓" : "NULL ✗"));

        initComponents();
        setupLabels();

        // Pre-fill fields with current values
        jTextField1.setText(currentExtra > 0 ? String.valueOf(currentExtra) : "");
        jTextField2.setText(currentTrible > 0 ? String.valueOf(currentTrible) : "");
        jTextField3.setText(currentNormalAmount > 0 ? String.valueOf(currentNormalAmount) : "");
        jTextField4.setText(currentExtraAmount > 0 ? String.valueOf(currentExtraAmount) : "");
        jTextField5.setText(currentTribleAmount > 0 ? String.valueOf(currentTribleAmount) : "");

        System.out.println("✅ Dialog initialized successfully\n");

        // Set dialog location to center of parent
        setLocationRelativeTo(parent);

        // Auto calculation setup කරන්න
        setupAutoCalculation();

        // Dialog open වෙද්දිම amounts calculate කරන්න
        calculateAmounts();

        // Ensure dialog shows fully and nicely centered
        pack();
        setSize(1328, 500); // You can adjust width and height
        setLocationRelativeTo(parent);

    }

    /**
     * Auto calculation setup කිරීම
     */
    private void setupAutoCalculation() {
        // Normal hours field එකට listener එකතු කිරීම
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateAmounts();
            }
        });

        // Extra hours field එකට listener එකතු කිරීම
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateAmounts();
            }
        });

        // Trible hours field එකට listener එකතු කිරීම
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateAmounts();
            }
        });
    }

    /**
     * Amounts ගණනය කිරීමේ method එක
     */
    private void calculateAmounts() {
        try {
            // Basic salary එක database එකෙන් ගන්න
            double basicSalary = getBasicSalaryFromDatabase();
            if (basicSalary == 0) {
                return; // Basic salary නැත්නම් stop කරන්න
            }

            // Hourly rate ගණනය කිරීම (මාසයේ පැය 200ක් ලෙස)
            double hourlyRate = basicSalary / 200;

            // Normal Amount ගණනය කිරීම (1.5 ගුණයක්)
            if (!jTextField6.getText().trim().isEmpty()) {
                double normalHours = Double.parseDouble(jTextField6.getText().trim());
                double normalAmount = hourlyRate * 1.5 * normalHours;
                jTextField3.setText(String.format("%.2f", normalAmount));
            } else {
                jTextField3.setText("0.00");
            }

            // Extra Amount ගණනය කිරීම (2 ගුණයක්)
            if (!jTextField1.getText().trim().isEmpty()) {
                double extraHours = Double.parseDouble(jTextField1.getText().trim());
                double extraAmount = hourlyRate * 2 * extraHours;
                jTextField4.setText(String.format("%.2f", extraAmount));
            } else {
                jTextField4.setText("0.00");
            }

            // Trible Amount ගණනය කිරීම (3 ගුණයක්)
            if (!jTextField2.getText().trim().isEmpty()) {
                double tribleHours = Double.parseDouble(jTextField2.getText().trim());
                double tribleAmount = hourlyRate * 3 * tribleHours;
                jTextField5.setText(String.format("%.2f", tribleAmount));
            } else {
                jTextField5.setText("0.00");
            }

        } catch (NumberFormatException e) {
            // පැය ගණන ඇතුලත් කර නැති විට හෝ invalid numbers
            jTextField3.setText("0.00");
            jTextField4.setText("0.00");
            jTextField5.setText("0.00");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error calculating amounts: " + e.getMessage(),
                    "Calculation Error",
                    JOptionPane.ERROR_MESSAGE);
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
            pst.setString(1, epfNo);
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

    private void setupLabels() {
        // Dialog title එකට EPF No සහ Name add කරන්න
        setTitle("Add Over Time Details - " + name + " (EPF: " + epfNo + ")");

        // Labels update කරන්න
        jLabel2.setText("Extra Hours:");
        jLabel3.setText("Trible Hours:");
        jLabel4.setText("Normal Amount (Rs):");
        jLabel5.setText("Extra Amount (Rs):");
        jLabel6.setText("Trible Amount (Rs):");
        jLabel7.setText("Normal Hours:");

        setupEmergencyButtonFix();

    }

    private void setupEmergencyButtonFix() {
        System.out.println("🚨 SETTING UP EMERGENCY BUTTON FIX!");

        // Remove all existing listeners
        for (java.awt.event.ActionListener al : jButton1.getActionListeners()) {
            jButton1.removeActionListener(al);
        }

        // Add fresh listener
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.out.println("🚨 EMERGENCY BUTTON WORKING!");
                saveOvertimeData();
            }
        });

        System.out.println("✅ Emergency button fix applied!");
    }

    /**
     * Save overtime data method (Fixed for attendence_id)
     */
    private void saveOvertimeData() {
        System.out.println("💾 SAVE METHOD TRIGGERED!");

        try {
            double normal = parseDouble(jTextField6.getText());
            double extra = parseDouble(jTextField1.getText());
            double trible = parseDouble(jTextField2.getText());
            double normalAmount = parseDouble(jTextField3.getText());
            double extraAmount = parseDouble(jTextField4.getText());
            double tribleAmount = parseDouble(jTextField5.getText());

            // Validate
            if (normal < 0 || extra < 0 || trible < 0) {
                JOptionPane.showMessageDialog(this, "Negative values not allowed!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ✅ Find attendence_id
            int attendenceId = -1;
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(
                    "SELECT id FROM attendence WHERE employee_id = (SELECT id FROM employee WHERE epf_no = ?) ORDER BY id DESC LIMIT 1")) {

                ps.setString(1, epfNo);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    attendenceId = rs.getInt("id");
                }
                rs.close();
            }

            if (attendenceId == -1) {
                JOptionPane.showMessageDialog(this,
                        "No attendance record found for EPF: " + epfNo + "\nPlease mark attendance first!",
                        "Attendance Missing", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ Calculate totals
            double totalHours = normal + extra + trible;
            double totalAmount = normalAmount + extraAmount + tribleAmount;

            // ✅ Update existing overtime - CORRECT COLUMN NAMES
            Connection conn = DatabaseConnection.getConnection();
            String sql = """
            UPDATE overtime 
            SET normal=?, extra=?, trible=?, 
                normal_amount=?, extra_amount=?, trible_amount=?,
                total=?, total_amount=?
            WHERE attendence_id=?
        """;
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, normal);
            pst.setDouble(2, extra);
            pst.setDouble(3, trible);
            pst.setDouble(4, normalAmount);
            pst.setDouble(5, extraAmount);
            pst.setDouble(6, tribleAmount);
            pst.setDouble(7, totalHours);
            pst.setDouble(8, totalAmount);
            pst.setInt(9, attendenceId);

            int rows = pst.executeUpdate();
            pst.close();
            conn.close();

            if (rows > 0) {
                System.out.println("✅ Overtime record updated successfully for attendance_id: " + attendenceId);
            } else {
                System.out.println("⚠ No existing record found — inserting new overtime entry...");

                // ✅ Insert new record - CORRECT COLUMN NAMES
                conn = DatabaseConnection.getConnection();
                sql = """
                INSERT INTO overtime (attendence_id, normal, extra, trible, 
                                      normal_amount, extra_amount, trible_amount, total, total_amount)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
                pst = conn.prepareStatement(sql);
                pst.setInt(1, attendenceId);
                pst.setDouble(2, normal);
                pst.setDouble(3, extra);
                pst.setDouble(4, trible);
                pst.setDouble(5, normalAmount);
                pst.setDouble(6, extraAmount);
                pst.setDouble(7, tribleAmount);
                pst.setDouble(8, totalHours);
                pst.setDouble(9, totalAmount);
                pst.executeUpdate();
                pst.close();
                conn.close();
                System.out.println("✅ New overtime record inserted successfully!");
            }

            // ✅ Refresh parent table if available
            if (parentPanel != null) {
                parentPanel.updateOvertimeData(selectedRow, normal, extra, trible, normalAmount, extraAmount, tribleAmount);
            }

            JOptionPane.showMessageDialog(this, "Overtime data saved successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            this.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saving data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Simple double parser
     */
    private double parseDouble(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return 0.0;
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
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Add Over Time Details");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Extra");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Trible");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Normal Amount");

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Extra Amount");

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Trible Amount");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Normal");

        jTextField6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                                    .addComponent(jTextField6))))
                        .addGap(108, 108, 108)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2))
                        .addGap(96, 96, 96)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                            .addComponent(jTextField3)
                            .addComponent(jTextField1))))
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(515, 515, 515))
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
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.out.println("🎯🎯🎯 SAVE BUTTON CLICKED! 🎯🎯🎯");
        saveOvertimeData();
    }//GEN-LAST:event_jButton1ActionPerformed
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddOverTime dialog = new AddOverTime(
                        new javax.swing.JFrame(),
                        true,
                        "123",
                        "Test Name",
                        null,
                        0,
                        0.0, 0.0, 0.0, 0.0, 0.0
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

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        // Additional verification when window opens
        System.out.println("🪟 Dialog window opened - Save button ready!");
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
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
