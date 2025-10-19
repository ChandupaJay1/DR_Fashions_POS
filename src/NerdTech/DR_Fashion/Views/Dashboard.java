/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package NerdTech.DR_Fashion.Views;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.DatabaseConnection.BidirectionalDatabaseSync;
import NerdTech.DR_Fashion.Views.Registration.EmployeeRegistration;
import NerdTech.DR_Fashion.Views.Accesories.AccesoriesPanel;
import NerdTech.DR_Fashion.Views.Attendence.AttendencePanel;
import NerdTech.DR_Fashion.Views.Backup.BackupPanel;
import NerdTech.DR_Fashion.Views.DashboardP.DashboardPanel;
import NerdTech.DR_Fashion.Views.PayRollManage.PayRollManagementPanel;
import NerdTech.DR_Fashion.Views.Shipment.ShipmentPanel;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import NerdTech.DR_Fashion.Views.DashboardP.EmployeeSectionStatsPanel;
import NerdTech.DR_Fashion.Views.Stock.StockPanel;
import java.sql.SQLException;

/**
 *
 * @author MG_Pathum
 */
public class Dashboard extends javax.swing.JFrame {

    private LoadingPanel loadingPanel;

    private String role;

    public Dashboard(String full_name, String role) {
        initComponents();
        this.role = role;
        startClock();
        DisplayLabel.setText("Hi, " + full_name);
        setAccessByRole();
        loadDashboardPanelByDefault();

    }

    private void setAccessByRole() {
        switch (role.toLowerCase()) {
            case "admin" -> {
                // Admin can access everything
                jButton2.setEnabled(true); // Dashboard
                jButton6.setEnabled(true); // Registration
                jButton1.setEnabled(true); // Attendance
                jButton8.setEnabled(true); // Accessories
                jButton3.setEnabled(true); // Stock
                jButton4.setEnabled(true); // Backup
            }
            case "hr" -> {  // Changed from "HR_Manager" to "hr_manager"
                // Manager limited access
                jButton2.setEnabled(true);
                jButton6.setEnabled(true);
                jButton1.setEnabled(false);
                jButton8.setEnabled(false);
                jButton3.setEnabled(false);
                jButton4.setEnabled(false); // Cannot backup
                jButton7.setEnabled(false);
                jButton10.setEnabled(false);
            }
            case "stores" -> {  // Changed from "Stores" to "stores"
                // Employee minimal access
                jButton2.setEnabled(true);
                jButton1.setEnabled(false);
                jButton6.setEnabled(false);
                jButton8.setEnabled(false);
                jButton3.setEnabled(true);
                jButton4.setEnabled(false);
                jButton7.setEnabled(false);
                jButton10.setEnabled(false);
            }
            default -> {
                // If unknown role
                JOptionPane.showMessageDialog(this, "Unknown role: " + role);
            }
        }
    }

    /**
     * Shows loading indicator and loads panel in background
     */
    private void loadPanelWithLoading(String panelName, PanelLoader loader) {

        currentPanelName = panelName;

        // Show loading immediately
        LoaderPanel.removeAll();
        loadingPanel = new LoadingPanel("Loading " + panelName);
        LoaderPanel.setLayout(new BorderLayout());
        LoaderPanel.add(loadingPanel, BorderLayout.CENTER);
        LoaderPanel.revalidate();
        LoaderPanel.repaint();

        // Load actual panel in background
        SwingWorker<JPanel, Void> worker = new SwingWorker<JPanel, Void>() {
            @Override
            protected JPanel doInBackground() throws Exception {
                // Simulate minimum loading time for smooth UX
                Thread.sleep(300);
                return loader.loadPanel();
            }

            @Override
            protected void done() {
                try {
                    JPanel panel = get();
                    LoaderPanel.removeAll();
                    LoaderPanel.setLayout(new BorderLayout());
                    LoaderPanel.add(panel, BorderLayout.CENTER);
                    LoaderPanel.revalidate();
                    LoaderPanel.repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showErrorPanel("Failed to load " + panelName);
                }
            }
        };
        worker.execute();
    }

    private void showErrorPanel(String message) {
        LoaderPanel.removeAll();
        JPanel errorPanel = new JPanel(new BorderLayout());
        JLabel errorLabel = new JLabel(message, SwingConstants.CENTER);
        errorLabel.setFont(new java.awt.Font("JetBrains Mono", 1, 18));
        errorPanel.add(errorLabel, BorderLayout.CENTER);
        LoaderPanel.add(errorPanel, BorderLayout.CENTER);
        LoaderPanel.revalidate();
        LoaderPanel.repaint();
    }

    private void loadDashboardPanelByDefault() {
        loadPanelWithLoading("Dashboard", () -> new DashboardPanel());
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
            SimpleDateFormat stf = new SimpleDateFormat("hh:mm:ss a");
            Date.setText(sdf.format(new Date()));
            Time.setText(stf.format(new Date()));
        });
        timer.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Date = new javax.swing.JLabel();
        Time = new javax.swing.JLabel();
        DisplayLabel = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButtonSync = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        LoaderPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(244, 234, 225));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Dashboard");

        Date.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        Date.setForeground(new java.awt.Color(204, 51, 0));

        Time.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        Time.setForeground(new java.awt.Color(0, 51, 255));

        DisplayLabel.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        DisplayLabel.setForeground(new java.awt.Color(0, 0, 0));

        jButton5.setBackground(new java.awt.Color(211, 47, 47));
        jButton5.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Logout");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton9.setText("Refresh");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButtonSync.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButtonSync.setText("Sync");
        jButtonSync.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSyncActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(DisplayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 569, Short.MAX_VALUE)
                .addComponent(Date, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(Time, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonSync, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DisplayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Time, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Date, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonSync, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel3.setLayout(new java.awt.GridLayout(8, 1, 5, 10));

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 15)); // NOI18N
        jButton2.setText("Dashboard");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2);

        jButton6.setFont(new java.awt.Font("JetBrains Mono", 1, 15)); // NOI18N
        jButton6.setText("Registration");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton6);

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 15)); // NOI18N
        jButton1.setText("Attendence");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        jButton7.setFont(new java.awt.Font("JetBrains Mono", 1, 14)); // NOI18N
        jButton7.setText("PayRoll Management");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton7);

        jButton10.setFont(new java.awt.Font("JetBrains Mono", 1, 15)); // NOI18N
        jButton10.setText("Shipments");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton10);

        jButton8.setFont(new java.awt.Font("JetBrains Mono", 1, 15)); // NOI18N
        jButton8.setText("Accesories");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton8);

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 15)); // NOI18N
        jButton3.setText("Stock");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3);

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 15)); // NOI18N
        jButton4.setText("Database Backup");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4);

        javax.swing.GroupLayout LoaderPanelLayout = new javax.swing.GroupLayout(LoaderPanel);
        LoaderPanel.setLayout(LoaderPanelLayout);
        LoaderPanelLayout.setHorizontalGroup(
            LoaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        LoaderPanelLayout.setVerticalGroup(
            LoaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 763, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(LoaderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 763, Short.MAX_VALUE)
                    .addComponent(LoaderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        loadPanelWithLoading("Registration", () -> new EmployeeRegistration());
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadPanelWithLoading("Attendance", () -> new AttendencePanel());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        loadPanelWithLoading("Accessories", () -> new AccesoriesPanel());
    }//GEN-LAST:event_jButton8ActionPerformed


    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        loadPanelWithLoading("Backup", () -> new BackupPanel());

    }//GEN-LAST:event_jButton4ActionPerformed


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        loadPanelWithLoading("Dashboard", () -> new DashboardPanel());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        new LoginForm().setVisible(true);
        this.dispose();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        loadPanelWithLoading("PayRollManagement", () -> new PayRollManagementPanel());
    }//GEN-LAST:event_jButton7ActionPerformed

    private String currentPanelName = "Dashboard";

    private void refreshCurrentPanel() {
        switch (currentPanelName) {
            case "Dashboard" ->
                loadPanelWithLoading("Dashboard", () -> new DashboardPanel());
            case "Registration" ->
                loadPanelWithLoading("Registration", () -> new EmployeeRegistration());
            case "Attendance" ->
                loadPanelWithLoading("Attendance", () -> new AttendencePanel());
            case "Accessories" ->
                loadPanelWithLoading("Accessories", () -> new AccesoriesPanel());
            case "Backup" ->
                loadPanelWithLoading("Backup", () -> new BackupPanel());
            case "PayRollManagement" ->
                loadPanelWithLoading("PayRollManagement", () -> new PayRollManagementPanel());
            case "Stock" ->
                loadPanelWithLoading("Stock", () -> new StockPanel());
            default ->
                showErrorPanel("Cannot refresh this panel.");
        }
    }


    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        refreshCurrentPanel();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        loadPanelWithLoading("Shipment", () -> new ShipmentPanel());
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButtonSyncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSyncActionPerformed
        // Disable button to prevent multiple clicks
        jButtonSync.setEnabled(false);
        jButtonSync.setText("Syncing...");

        // Show temporary loading panel
        JPanel syncLoadingPanel = new JPanel(new BorderLayout());
        syncLoadingPanel.add(new LoadingPanel("Syncing database..."), BorderLayout.CENTER);
        LoaderPanel.removeAll();
        LoaderPanel.setLayout(new BorderLayout());
        LoaderPanel.add(syncLoadingPanel, BorderLayout.CENTER);
        LoaderPanel.revalidate();
        LoaderPanel.repaint();

        // Background sync
        SwingWorker<Boolean, String> syncWorker = new SwingWorker<Boolean, String>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // Small delay for UX
                Thread.sleep(300);

                // Set callback for sync status updates
                BidirectionalDatabaseSync.setStatusCallback(
                        new BidirectionalDatabaseSync.SyncStatusCallback() {
                    @Override
                    public void onStatusChange(String status) {
                        publish(status);  // Send to process() method
                    }
                }
                );

                // Perform bidirectional sync
                boolean success = BidirectionalDatabaseSync.performFullSync();
                return success;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                // Update UI with sync status (optional - show in loading panel)
                if (!chunks.isEmpty()) {
                    String latestStatus = chunks.get(chunks.size() - 1);
                    System.out.println("[SYNC STATUS] " + latestStatus);
                }
            }

            @Override
            protected void done() {
                try {
                    Boolean success = get(); // Get result and check for exceptions

                    if (success) {
                        JOptionPane.showMessageDialog(Dashboard.this,
                                "✅ Sync completed successfully!\n\n"
                                + "All data has been synchronized between local and online databases.",
                                "Sync Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(Dashboard.this,
                                "⚠️ Sync completed with warnings!\n\n"
                                + "Some data may not have been synchronized properly.\n"
                                + "Please check your internet connection and try again.",
                                "Sync Warning",
                                JOptionPane.WARNING_MESSAGE);
                    }

                } catch (java.util.concurrent.ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    ex.printStackTrace();

                    String errorMsg;
                    if (cause instanceof SQLException) {
                        errorMsg = "Database connection error: " + cause.getMessage();
                    } else if (cause instanceof java.net.ConnectException) {
                        errorMsg = "Cannot connect to online database.\nPlease check your internet connection.";
                    } else {
                        errorMsg = cause != null ? cause.getMessage() : ex.getMessage();
                    }

                    JOptionPane.showMessageDialog(Dashboard.this,
                            "❌ Sync failed!\n\n" + errorMsg + "\n\n"
                            + "Possible solutions:\n"
                            + "• Check your internet connection\n"
                            + "• Verify online database credentials\n"
                            + "• Ensure online MySQL server is running\n"
                            + "• Check firewall settings",
                            "Sync Error",
                            JOptionPane.ERROR_MESSAGE);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Dashboard.this,
                            "⚠️ Sync was interrupted!",
                            "Sync Interrupted",
                            JOptionPane.WARNING_MESSAGE);

                } finally {
                    // Restore Dashboard panel after sync
                    loadDashboardPanelByDefault();
                    jButtonSync.setEnabled(true);
                    jButtonSync.setText("Sync");
                }
            }
        };

        syncWorker.execute();
    }//GEN-LAST:event_jButtonSyncActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        loadPanelWithLoading("Stock", () -> new StockPanel());
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    @FunctionalInterface
    private interface PanelLoader {

        JPanel loadPanel() throws Exception;
    }

    public static void main(String args[]) {
        FlatMacLightLaf.setup();
        String name = "John Doe";
        String role = "admin"; // or "employee" / "manager"

        java.awt.EventQueue.invokeLater(() -> new Dashboard(name, role).setVisible(true));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Date;
    private javax.swing.JLabel DisplayLabel;
    private javax.swing.JPanel LoaderPanel;
    private javax.swing.JLabel Time;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonSync;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
