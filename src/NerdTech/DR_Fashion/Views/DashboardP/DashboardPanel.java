package NerdTech.DR_Fashion.Views.DashboardP;

import NerdTech.DR_Fashion.Views.DashboardP.CardPanel;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.LoadingPanel;
import com.formdev.flatlaf.FlatDarkLaf;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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

    // Tables
    private JTable sectionStatsTable;
    private DefaultTableModel sectionTableModel;

    private JTable capacityStatsTable;
    private DefaultTableModel capacityTableModel;

    private JTable designationStatsTable;
    private DefaultTableModel designationTableModel;

    public DashboardPanel() {

        setPreferredSize(new Dimension(1257, 686));
        setBackground(new Color(50, 50, 50));
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

        // Load all statistics
        loadSectionStats();
        loadCapacityStats();
        loadDesignationStats();

        // Auto-refresh
        refreshTimer = new Timer(5000, e -> {
            loadDashboardData();
            loadSectionStats();
            loadCapacityStats();
            loadDesignationStats();
        });
        refreshTimer.start();

        isInitialized = true;
        revalidate();
        repaint();
    }

    private void createDashboardUI() {
        // Main container with scroll
        JPanel mainContainer = new JPanel(new BorderLayout(0, 20));
        mainContainer.setOpaque(false);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top section with title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(100, 100, 100));

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(separator, BorderLayout.SOUTH);

        // --- Cards Panel Section ---
        JPanel cardsPanel = new JPanel();
        cardsPanel.setOpaque(false);
        cardsPanel.setLayout(new GridLayout(1, 3, 40, 0)); // equal spacing 3 columns
        cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        employeeCountLabel = new JLabel("0");
        attendanceCountLabel = new JLabel("0");
        absenceCountLabel = new JLabel("0");

// Cards
        JPanel employeeCard = createModernCard("Total Employees", "👥",
                new Color(59, 130, 246), employeeCountLabel);
        JPanel presentCard = createModernCard("Present Today", "✓",
                new Color(34, 197, 94), attendanceCountLabel);
        JPanel absentCard = createModernCard("Absent Today", "✗",
                new Color(239, 68, 68), absenceCountLabel);

// Ensure consistent sizing
        Dimension cardSize = new Dimension(360, 160);
        employeeCard.setPreferredSize(cardSize);
        presentCard.setPreferredSize(cardSize);
        absentCard.setPreferredSize(cardSize);

        cardsPanel.add(employeeCard);
        cardsPanel.add(presentCard);
        cardsPanel.add(absentCard);

// Center the cards panel horizontally
        JPanel cardsWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        cardsWrapper.setOpaque(false);
        cardsWrapper.add(cardsPanel);

        // Tables Container - show 3 tables side by side
        JPanel tablesContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        tablesContainer.setOpaque(false);
        tablesContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        tablesContainer.add(createCapacityStatsTable());   // 1st - Capacity
        tablesContainer.add(createSectionStatsTable());    // 2nd - Section
        tablesContainer.add(createDesignationStatsTable()); // 3rd - Designation

// Content panel (cards + tables)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(cardsPanel);
        contentPanel.add(Box.createVerticalStrut(40));
        contentPanel.add(tablesContainer);

        // Add to main container
        mainContainer.add(topPanel, BorderLayout.NORTH);
        mainContainer.add(contentPanel, BorderLayout.CENTER);

        add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createSectionStatsTable() {
        JPanel tableContainer = new JPanel(new BorderLayout(10, 10));
        tableContainer.setOpaque(false);
        tableContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JLabel tableTitle = new JLabel("📊 Employee Distribution by Section");
        tableTitle.setFont(new Font("JetBrains Mono", Font.BOLD, 18));
        tableTitle.setForeground(Color.WHITE);

        String[] columns = {"Section", "Male", "Female", "Total"};
        sectionTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        sectionStatsTable = createStyledTable(sectionTableModel);

        JScrollPane scrollPane = new JScrollPane(sectionStatsTable);
        scrollPane.setPreferredSize(new Dimension(1100, 250));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        scrollPane.getViewport().setBackground(new Color(60, 60, 60));

        tableContainer.add(tableTitle, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        return tableContainer;
    }

    private JPanel createCapacityStatsTable() {
        JPanel tableContainer = new JPanel(new BorderLayout(10, 10));
        tableContainer.setOpaque(false);
        tableContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JLabel tableTitle = new JLabel("⚙️ Employee Distribution by Capacity");
        tableTitle.setFont(new Font("JetBrains Mono", Font.BOLD, 18));
        tableTitle.setForeground(Color.WHITE);

        String[] columns = {"Capacity", "Male", "Female", "Total"};
        capacityTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        capacityStatsTable = createStyledTable(capacityTableModel);

        JScrollPane scrollPane = new JScrollPane(capacityStatsTable);
        scrollPane.setPreferredSize(new Dimension(1100, 250));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        scrollPane.getViewport().setBackground(new Color(60, 60, 60));

        tableContainer.add(tableTitle, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        return tableContainer;
    }

    private JPanel createDesignationStatsTable() {
        JPanel tableContainer = new JPanel(new BorderLayout(10, 10));
        tableContainer.setOpaque(false);
        tableContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JLabel tableTitle = new JLabel("👔 Employee Distribution by Designation");
        tableTitle.setFont(new Font("JetBrains Mono", Font.BOLD, 18));
        tableTitle.setForeground(Color.WHITE);

        String[] columns = {"Designation", "Male", "Female", "Total"};
        designationTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        designationStatsTable = createStyledTable(designationTableModel);

        JScrollPane scrollPane = new JScrollPane(designationStatsTable);
        scrollPane.setPreferredSize(new Dimension(1100, 250));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        scrollPane.getViewport().setBackground(new Color(60, 60, 60));

        tableContainer.add(tableTitle, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        return tableContainer;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Calibri", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(70, 70, 70));
        table.setBackground(new Color(60, 60, 60));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(80, 80, 80));
        table.setSelectionForeground(Color.WHITE);

        // Header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        header.setBackground(new Color(30, 58, 138));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Custom renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);

                int lastRow = tbl.getRowCount() - 1;

                if (row == lastRow && lastRow >= 0) {
                    c.setBackground(new Color(30, 58, 138));
                    c.setForeground(Color.WHITE);
                    c.setFont(c.getFont().deriveFont(Font.BOLD, 15f));
                } else {
                    c.setBackground(new Color(60, 60, 60));
                    c.setForeground(Color.WHITE);
                    c.setFont(c.getFont().deriveFont(Font.PLAIN, 14f));
                }

                if (column > 0) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }

                return c;
            }
        });

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        return table;
    }

    private void loadSectionStats() {
        sectionTableModel.setRowCount(0);

        String query = "SELECT "
                + "COALESCE(s.section_name, 'Unassigned') AS section, "
                + "SUM(CASE WHEN e.gender = 'Male' THEN 1 ELSE 0 END) AS male_count, "
                + "SUM(CASE WHEN e.gender = 'Female' THEN 1 ELSE 0 END) AS female_count, "
                + "COUNT(*) AS total "
                + "FROM employee e "
                + "LEFT JOIN section s ON e.section_id = s.id "
                + "WHERE e.status = 'active' "
                + "GROUP BY s.section_name "
                + "ORDER BY total DESC";

        loadStatsToTable(query, sectionTableModel);
    }

    private void loadCapacityStats() {
        capacityTableModel.setRowCount(0);

        String query = "SELECT "
                + "COALESCE(d.capacity, 'Unassigned') AS capacity, "
                + "SUM(CASE WHEN e.gender = 'Male' THEN 1 ELSE 0 END) AS male_count, "
                + "SUM(CASE WHEN e.gender = 'Female' THEN 1 ELSE 0 END) AS female_count, "
                + "COUNT(*) AS total "
                + "FROM employee e "
                + "LEFT JOIN designation d ON e.designation_id = d.id "
                + "WHERE e.status = 'active' "
                + "GROUP BY d.capacity "
                + "ORDER BY total DESC";

        loadStatsToTable(query, capacityTableModel);
    }

    private void loadDesignationStats() {
        designationTableModel.setRowCount(0);

        String query = "SELECT "
                + "COALESCE(d.title, 'Unassigned') AS designation, "
                + "SUM(CASE WHEN e.gender = 'Male' THEN 1 ELSE 0 END) AS male_count, "
                + "SUM(CASE WHEN e.gender = 'Female' THEN 1 ELSE 0 END) AS female_count, "
                + "COUNT(*) AS total "
                + "FROM employee e "
                + "LEFT JOIN designation d ON e.designation_id = d.id "
                + "WHERE e.status = 'active' "
                + "GROUP BY d.title "
                + "ORDER BY total DESC";

        loadStatsToTable(query, designationTableModel);
    }

    private void loadStatsToTable(String query, DefaultTableModel tableModel) {
        int totalMale = 0;
        int totalFemale = 0;
        int grandTotal = 0;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String category = rs.getString(1);
                int male = rs.getInt("male_count");
                int female = rs.getInt("female_count");
                int total = rs.getInt("total");

                tableModel.addRow(new Object[]{category, male, female, total});

                totalMale += male;
                totalFemale += female;
                grandTotal += total;
            }

            if (tableModel.getRowCount() > 0) {
                tableModel.addRow(new Object[]{"Total", totalMale, totalFemale, grandTotal});
            }

        } catch (SQLException e) {
            System.err.println("Error loading stats: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(DashboardPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JPanel createModernCard(String title, String icon, Color bgColor, JLabel countLabel) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(330, 160));
        card.setLayout(new BorderLayout(15, 15));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setForeground(new Color(255, 255, 255, 180));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        titleLabel.setForeground(new Color(255, 255, 255, 230));

        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        countLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 54));
        countLabel.setForeground(Color.WHITE);
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JLabel contextLabel = new JLabel("Total Count");
        contextLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 10));
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

    private int getTotalEmployees(Connection conn) throws SQLException {
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
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
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
