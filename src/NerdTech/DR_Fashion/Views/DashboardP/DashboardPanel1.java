package NerdTech.DR_Fashion.Views.DashboardP;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardPanel1 extends javax.swing.JPanel {
    
    private JLabel employeeCountLabel;
    private JLabel attendanceCountLabel;
    private JLabel absenceCountLabel;
    private Timer refreshTimer;
    
    public DashboardPanel1() {
        initComponents();
    }
    
    private void loadDashboardData() {
        try {
            int totalEmployees = getTotalEmployees();
            int presentToday = getPresentToday();
            int absentToday = getAbsentToday();
            
            // Update labels
            employeeCountLabel.setText(String.valueOf(totalEmployees));
            attendanceCountLabel.setText(String.valueOf(presentToday));
            absenceCountLabel.setText(String.valueOf(absentToday));
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading dashboard data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int getTotalEmployees() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM employee";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception ex) {
            Logger.getLogger(DashboardPanel1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    private int getPresentToday() throws SQLException {
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        String query = "SELECT COUNT(*) as total FROM attendence WHERE attendance_date = ? AND status = 'Present'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setDate(1, today);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DashboardPanel1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    private int getAbsentToday() throws SQLException {
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        String query = "SELECT COUNT(*) as total FROM attendence WHERE attendance_date = ? AND status = 'Absent'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setDate(1, today);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DashboardPanel1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    private JPanel createStatsCard(String title, Color bgColor, Color textColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 3),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 24));
        titleLabel.setForeground(textColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Count label
        JLabel countLabel = new JLabel("0");
        countLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 72));
        countLabel.setForeground(textColor);
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Store reference based on title
        if (title.equals("Total Employees")) {
            employeeCountLabel = countLabel;
        } else if (title.equals("Present Today")) {
            attendanceCountLabel = countLabel;
        } else if (title.equals("Absent Today")) {
            absenceCountLabel = countLabel;
        }
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);
        
        return card;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        
        // Create stats cards with different colors
        JPanel employeeCard = createStatsCard("Total Employees", 
            new Color(66, 133, 244), Color.WHITE);  // Blue
        JPanel attendanceCard = createStatsCard("Present Today", 
            new Color(52, 168, 83), Color.WHITE);   // Green
        JPanel absenceCard = createStatsCard("Absent Today", 
            new Color(234, 67, 53), Color.WHITE);   // Red

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36));
        jLabel1.setText("Dashboard");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(employeeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(attendanceCard, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(absenceCard, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(employeeCard, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(attendanceCard, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(absenceCard, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        // Load data after all components are initialized
        loadDashboardData();
        
        // Start auto-refresh timer AFTER everything is initialized
        refreshTimer = new Timer(30000, e -> loadDashboardData());
        refreshTimer.start();
    }

    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
}