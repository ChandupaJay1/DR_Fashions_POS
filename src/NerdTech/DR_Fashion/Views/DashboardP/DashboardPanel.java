/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.DashboardP;

import NerdTech.DR_Fashion.Views.DashboardP.CardPanel;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.LoadingPanel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardPanel extends javax.swing.JPanel {

    private JLabel employeeCountLabel;
    private JLabel attendanceCountLabel;
    private JLabel absenceCountLabel;
    private Timer refreshTimer;
    private LoadingPanel loadingPanel;
    private boolean isInitialized = false;

    public DashboardPanel() {
        setPreferredSize(new Dimension(1257, 686));
        setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout());

        showLoading("Loading Dashboard Data");
        loadContentInBackground();
    }

    private void showLoading(String message) {
        removeAll();
        loadingPanel = new LoadingPanel(message);
        add(loadingPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void loadContentInBackground() {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            private int totalEmp = 0;
            private int present = 0;
            private int absent = 0;

            @Override
            protected Void doInBackground() {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    publish("Fetching employees");
                    totalEmp = getTotalEmployees(conn);

                    publish("Fetching present count");
                    present = getPresentToday(conn);

                    publish("Fetching absent count");
                    absent = getAbsentToday(conn);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                if (loadingPanel != null && !chunks.isEmpty()) {
                    loadingPanel.setMessage(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done() {
                showActualContent(totalEmp, present, absent);
            }
        };
        worker.execute();
    }

    private void showActualContent(int totalEmp, int present, int absent) {
        removeAll();
        createDashboardUI();

        employeeCountLabel.setText(String.valueOf(totalEmp));
        attendanceCountLabel.setText(String.valueOf(present));
        absenceCountLabel.setText(String.valueOf(absent));

        // Auto-refresh
        refreshTimer = new Timer(5000, e -> loadDashboardData());
        refreshTimer.start();

        isInitialized = true;
        revalidate();
        repaint();
    }

    private void createDashboardUI() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 48));
        titleLabel.setForeground(new Color(30, 41, 59));

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(203, 213, 225));

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(separator, BorderLayout.SOUTH);

        JPanel cardsPanel = new JPanel();
        cardsPanel.setOpaque(false);
        cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 60));

        employeeCountLabel = new JLabel("0");
        attendanceCountLabel = new JLabel("0");
        absenceCountLabel = new JLabel("0");

        JPanel employeeCard = createModernCard("Total Employees", "👥",
                new Color(59, 130, 246), employeeCountLabel);
        JPanel presentCard = createModernCard("Present Today", "✓",
                new Color(34, 197, 94), attendanceCountLabel);
        JPanel absentCard = createModernCard("Absent Today", "✗",
                new Color(239, 68, 68), absenceCountLabel);

        cardsPanel.add(employeeCard);
        cardsPanel.add(presentCard);
        cardsPanel.add(absentCard);

        add(topPanel, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createModernCard(String title, String icon, Color bgColor, JLabel countLabel) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(350, 220));
        card.setLayout(new BorderLayout(15, 15));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setForeground(new Color(255, 255, 255, 180));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        titleLabel.setForeground(new Color(255, 255, 255, 230));

        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        countLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 72));
        countLabel.setForeground(Color.WHITE);
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JLabel contextLabel = new JLabel("Total Count");
        contextLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        contextLabel.setForeground(new Color(255, 255, 255, 180));
        bottomPanel.add(contextLabel);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private void loadDashboardData() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private int totalEmp = 0;
            private int present = 0;
            private int absent = 0;

            @Override
            protected Void doInBackground() {
                try (Connection conn = DatabaseConnection.getConnection()) {
                    totalEmp = getTotalEmployees(conn);
                    present = getPresentToday(conn);
                    absent = getAbsentToday(conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                employeeCountLabel.setText(String.valueOf(totalEmp));
                attendanceCountLabel.setText(String.valueOf(present));
                absenceCountLabel.setText(String.valueOf(absent));
            }
        };
        worker.execute();
    }

    // ==== Database Queries ====
    private int getTotalEmployees(Connection conn) throws SQLException {
        // Only count active employees
        String query = "SELECT COUNT(*) as total FROM employee WHERE status = 'active'";
        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    private int getPresentToday(Connection conn) throws SQLException {
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        String query = "SELECT COUNT(*) as total FROM attendence WHERE attendance_date = ? AND status = 'Present'";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, today);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

    private int getAbsentToday(Connection conn) throws SQLException {
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        String query = "SELECT COUNT(*) as total FROM attendence WHERE attendance_date = ? AND status = 'Absent'";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, today);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

// ✅ Clean up timer when panel is removed
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Dashboard");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 487, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 467, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(346, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
