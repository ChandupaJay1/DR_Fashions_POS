/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.DashboardP;

import NerdTech.DR_Fashion.Views.DashboardP.CardPanel;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
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

    public DashboardPanel() {
        setPreferredSize(new Dimension(1374, 686)); // fixed size
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // 🔹 Top "Dashboard" label
        JLabel dashboardTitle = new JLabel("Dashboard");
        dashboardTitle.setFont(new Font("JetBrains Mono", Font.BOLD, 28));
        dashboardTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 10));

        // 🔹 Card holder panel - center area
        JPanel cardHolder = new JPanel();
        cardHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 30));
        cardHolder.setOpaque(false);

        // Card 1: Total Employees
        CardPanel totalCard = new CardPanel(new Color(66, 133, 244)); // blue
        setupCard(totalCard, "👥", "Total Employees", "8");

        // Card 2: Present Today
        CardPanel presentCard = new CardPanel(new Color(15, 157, 88)); // green
        setupCard(presentCard, "✅", "Present Today", "1");

        // Card 3: Absent Today
        CardPanel absentCard = new CardPanel(new Color(219, 68, 55)); // red
        setupCard(absentCard, "❌", "Absent Today", "1");

        // Add cards to cardHolder
        cardHolder.add(totalCard);
        cardHolder.add(presentCard);
        cardHolder.add(absentCard);

        // 🔹 Add to this panel
        add(dashboardTitle, BorderLayout.NORTH);
        add(cardHolder, BorderLayout.CENTER);

    }

    private void setupCard(CardPanel card, String icon, String title, String count) {
        card.setLayout(new BorderLayout(5, 5));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JLabel countLabel = new JLabel(count, SwingConstants.CENTER);
        countLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 40));
        countLabel.setForeground(Color.WHITE);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(titleLabel, BorderLayout.CENTER);
        card.add(countLabel, BorderLayout.SOUTH);
    }

    private void setupCard(JPanel card, String title, String iconText, Color bgColor, JLabel countLabel) {
        // Clear existing content
        card.removeAll();
        card.setLayout(new BorderLayout(15, 15));
        card.setBackground(bgColor);

        // Rounded border with shadow effect
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2),
                BorderFactory.createEmptyBorder(35, 30, 35, 30)
        ));

        // Top section - Icon and Title
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        // Icon
        JLabel iconLabel = new JLabel(iconText);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
        iconLabel.setForeground(new Color(255, 255, 255, 200));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 17));
        titleLabel.setForeground(new Color(255, 255, 255, 240));

        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Center section - Count (large number)
        countLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 75));
        countLabel.setForeground(Color.WHITE);
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Bottom section - Context label
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        JLabel contextLabel = new JLabel("Total Count");
        contextLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        contextLabel.setForeground(new Color(255, 255, 255, 200));
        bottomPanel.add(contextLabel);

        // Add all sections to card
        card.add(topPanel, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        // Refresh the card
        card.revalidate();
        card.repaint();
    }

    private void loadDashboardData() {
        try {
            // Get counts from database
            int totalEmployees = getTotalEmployees();
            int presentToday = getPresentToday();
            int absentToday = getAbsentToday();

            // Update labels with null checks
            if (employeeCountLabel != null) {
                employeeCountLabel.setText(String.valueOf(totalEmployees));
            }
            if (attendanceCountLabel != null) {
                attendanceCountLabel.setText(String.valueOf(presentToday));
            }
            if (absenceCountLabel != null) {
                absenceCountLabel.setText(String.valueOf(absentToday));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading dashboard data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getTotalEmployees() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM employee";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception ex) {
            Logger.getLogger(DashboardPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private int getPresentToday() throws SQLException {
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        String query = "SELECT COUNT(*) as total FROM attendence WHERE attendance_date = ? AND status = 'Present'";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDate(1, today);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DashboardPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    private int getAbsentToday() throws SQLException {
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        String query = "SELECT COUNT(*) as total FROM attendence WHERE attendance_date = ? AND status = 'Absent'";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDate(1, today);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DashboardPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
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
