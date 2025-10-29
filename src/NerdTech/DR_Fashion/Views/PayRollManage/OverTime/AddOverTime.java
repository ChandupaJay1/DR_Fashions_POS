/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage.OverTime;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
     * ⭐⭐⭐ SAVE BUTTON METHOD - 100% WORKING
     */
    /**
     * Save overtime data method
     */
    private void saveOvertimeData() {
        System.out.println("💾 SAVE METHOD TRIGGERED!");

        try {
            // 1. Get values from text fields
            double extra = parseDouble(jTextField1.getText());
            double trible = parseDouble(jTextField2.getText());
            double normalAmount = parseDouble(jTextField3.getText());
            double extraAmount = parseDouble(jTextField4.getText());
            double tribleAmount = parseDouble(jTextField5.getText());

            System.out.println("📊 Values captured:");
            System.out.println("  Extra Hours: " + extra);
            System.out.println("  Trible Hours: " + trible);
            System.out.println("  Normal Amount: " + normalAmount);
            System.out.println("  Extra Amount: " + extraAmount);
            System.out.println("  Trible Amount: " + tribleAmount);

            // 2. Validate
            if (extra < 0 || trible < 0 || normalAmount < 0 || extraAmount < 0 || tribleAmount < 0) {
                JOptionPane.showMessageDialog(this, "Negative values not allowed!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Check parent panel
            if (parentPanel == null) {
                JOptionPane.showMessageDialog(this, "Parent panel error!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. Update parent panel (this will now save to database too)
            System.out.println("🔄 Calling parent update method...");
            parentPanel.updateOvertimeData(selectedRow, extra, trible, normalAmount, extraAmount, tribleAmount);

            // 5. Success message
            JOptionPane.showMessageDialog(this,
                    "Overtime data saved successfully to database!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // 6. Close dialog
            this.dispose();

            System.out.println("✅ Save completed successfully!");

        } catch (Exception e) {
            System.err.println("❌ Save error: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error saving data: " + e.getMessage(),
                    "Error",
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addGap(86, 86, 86)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                            .addComponent(jTextField3)
                            .addComponent(jTextField5))
                        .addGap(108, 108, 108)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(107, 107, 107)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
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
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
