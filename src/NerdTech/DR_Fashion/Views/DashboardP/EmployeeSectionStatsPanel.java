package NerdTech.DR_Fashion.Views.DashboardP;

import java.awt.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class EmployeeSectionStatsPanel extends JPanel {

    private JTable statsTable;
    private DefaultTableModel tableModel;

    public EmployeeSectionStatsPanel() {
        initComponents();
        loadSectionStats();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Employee Distribution by Section");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 41, 59));
        titlePanel.add(titleLabel);

        // Table Model
        String[] columns = {"Section", "Male", "Female", "Total"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create Table
        statsTable = new JTable(tableModel);
        statsTable.setFont(new Font("Calibri", Font.PLAIN, 14));
        statsTable.setRowHeight(30);
        statsTable.setShowGrid(true);
        statsTable.setGridColor(new Color(226, 232, 240));
        statsTable.setSelectionBackground(new Color(219, 234, 254));
        statsTable.setSelectionForeground(new Color(30, 41, 59));

        // Header Style
        JTableHeader header = statsTable.getTableHeader();
        header.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        header.setBackground(new Color(30, 58, 138));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Center Align Numbers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        statsTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        statsTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        statsTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Column Widths
        statsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        statsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        statsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        statsTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Add to ScrollPane
        JScrollPane scrollPane = new JScrollPane(statsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

        // Add Components
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadSectionStats() {
        tableModel.setRowCount(0);

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

        int totalMale = 0;
        int totalFemale = 0;
        int grandTotal = 0;

        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String section = rs.getString("section");
                int male = rs.getInt("male_count");
                int female = rs.getInt("female_count");
                int total = rs.getInt("total");

                tableModel.addRow(new Object[]{section, male, female, total});

                totalMale += male;
                totalFemale += female;
                grandTotal += total;
            }

            // Add Total Row
            if (tableModel.getRowCount() > 0) {
                tableModel.addRow(new Object[]{"Total", totalMale, totalFemale, grandTotal});

                // Highlight total row
                int lastRow = tableModel.getRowCount() - 1;
                statsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                            boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                        if (row == lastRow) {
                            c.setBackground(new Color(191, 219, 254));
                            c.setFont(c.getFont().deriveFont(Font.BOLD));
                        } else {
                            c.setBackground(Color.WHITE);
                            c.setFont(c.getFont().deriveFont(Font.PLAIN));
                        }

                        if (column > 0) {
                            ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                        } else {
                            ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                        }

                        return c;
                    }
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading section statistics: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(EmployeeSectionStatsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refreshStats() {
        loadSectionStats();
    }
}
