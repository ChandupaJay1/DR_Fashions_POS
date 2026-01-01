/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage.IncentivePanel;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.LoadingPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

public class IncentivePanel extends javax.swing.JPanel {

    private LoadingPanel loadingPanel;
    private JPanel contentPanel; // Panel to hold main content
    private JPanel containerPanel; // Container with CardLayout
    private CardLayout cardLayout;

    public IncentivePanel() {
        // Setup container with CardLayout BEFORE initComponents
        cardLayout = new CardLayout();
        containerPanel = new JPanel(cardLayout);

        initComponents();

        // Setup loading panel
        loadingPanel = new LoadingPanel("Loading Incentive Data");

        // Create content panel with main components
        contentPanel = new JPanel();
        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
                contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 1049, Short.MAX_VALUE))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1477, Short.MAX_VALUE))
                                .addContainerGap())
        );
        contentLayout.setVerticalGroup(
                contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(179, Short.MAX_VALUE))
        );

        // Add panels to card layout
        containerPanel.add(contentPanel, "CONTENT");
        containerPanel.add(loadingPanel, "LOADING");

        // Set main layout
        setLayout(new BorderLayout());
        add(containerPanel, BorderLayout.CENTER);

        // Show content by default
        cardLayout.show(containerPanel, "CONTENT");

        // Load data in background
        loadEmployeeDataAsync();

        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && jTable1.getSelectedRow() != -1) {
                    int row = jTable1.getSelectedRow();
                    String epfNo = jTable1.getValueAt(row, 0).toString();
                    String name = jTable1.getValueAt(row, 1).toString();

                    java.awt.Frame parentFrame = (java.awt.Frame) SwingUtilities.getWindowAncestor(IncentivePanel.this);
                    System.out.println("🔓 Opening dialog for EPF: " + epfNo);

                    AddIncentiveDFrame dialog = new AddIncentiveDFrame(parentFrame, true, epfNo, name, IncentivePanel.this);

                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            System.out.println("🔄 Dialog closed - Refreshing data...");
                            loadEmployeeDataAsync();
                        }
                    });

                    dialog.setVisible(true);
                }
            }
        });
    }

    /**
     * Load employee data asynchronously using SwingWorker Prevents UI freezing
     * and shows loading indicator
     */
    public void loadEmployeeDataAsync() {
        // Show loading panel
        SwingUtilities.invokeLater(() -> {
            cardLayout.show(containerPanel, "LOADING");
            loadingPanel.setMessage("Loading Employee Data");
        });

        SwingWorker<TableData, Void> worker = new SwingWorker<TableData, Void>() {
            @Override
            protected TableData doInBackground() throws Exception {
                System.out.println("\n=== Loading Employee Data (Background) ===");
                return loadEmployeeDataFromDatabase();
            }

            @Override
            protected void done() {
                try {
                    TableData data = get();
                    updateTableUI(data);

                    System.out.println("Total employees loaded: " + data.rows.size());
                    System.out.println("=========================\n");

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(IncentivePanel.this,
                            "Error loading employee data: " + e.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                } finally {
                    // Show content panel
                    SwingUtilities.invokeLater(() -> {
                        cardLayout.show(containerPanel, "CONTENT");
                    });
                }
            }
        };

        worker.execute();
    }

    /**
     * Load all data from database in one optimized query Uses LEFT JOIN to get
     * employee and incentive data together
     */
    private TableData loadEmployeeDataFromDatabase() throws SQLException, Exception {
        TableData tableData = new TableData();

        // ⭐ OPTIMIZED: Single query with LEFT JOIN - gets all data at once
        String query = """
            SELECT 
                e.epf_no AS 'EPF No',
                CONCAT(e.fname, ' ', e.surname) AS 'Name',
                s.section_name AS 'Section',
                d.title AS 'Designation',
                e.nic AS 'NIC',
                COALESCE(MAX(i.attendance_incentive), 0) AS 'Attendance',
                COALESCE(MAX(i.grading_incentive), 0) AS 'Grading',
                COALESCE(MAX(i.production1_incentive), 0) AS 'Production1',
                COALESCE(MAX(i.production2_incentive), 0) AS 'Production2',
                COALESCE(MAX(i.total_incentive), 0) AS 'Total'
            FROM employee e
            INNER JOIN section s ON e.section_id = s.id
            INNER JOIN designation d ON e.designation_id = d.id
            LEFT JOIN attendence a ON e.id = a.employee_id 
                AND MONTH(a.attendance_date) = MONTH(CURRENT_DATE()) 
                AND YEAR(a.attendance_date) = YEAR(CURRENT_DATE())
            LEFT JOIN incentive i ON a.id = i.attendence_id
            GROUP BY e.epf_no, e.fname, e.surname, s.section_name, d.title, e.nic
            ORDER BY e.epf_no
        """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String epfNo = rs.getString("EPF No");
                double attendance = rs.getDouble("Attendance");
                double grading = rs.getDouble("Grading");
                double production1 = rs.getDouble("Production1");
                double production2 = rs.getDouble("Production2");
                double total = rs.getDouble("Total");

                // Debug output for non-zero incentives
                if (attendance > 0 || grading > 0 || production1 > 0 || production2 > 0) {
                    System.out.println("📊 EPF " + epfNo + " - Att: " + attendance
                            + ", Grad: " + grading
                            + ", Prod1: " + production1
                            + ", Prod2: " + production2
                            + ", Total: " + total);
                }

                Object[] row = {
                    epfNo,
                    rs.getString("Name"),
                    rs.getString("Section"),
                    rs.getString("Designation"),
                    rs.getString("NIC"),
                    formatIncentive(attendance),
                    formatIncentive(grading),
                    formatIncentive(production1),
                    formatIncentive(production2),
                    formatIncentive(total)
                };

                tableData.rows.add(row);
            }
        }

        return tableData;
    }

    /**
     * Update table UI on EDT with loaded data
     */
    private void updateTableUI(TableData data) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear table

        for (Object[] row : data.rows) {
            model.addRow(row);
        }

        model.fireTableDataChanged();
        jTable1.updateUI();
    }

    /**
     * Format incentive values for display
     */
    private String formatIncentive(double value) {
        if (value == 0) {
            return "";
        }
        return String.format("%.2f", value);
    }

    /**
     * Helper class to hold table data
     */
    private static class TableData {

        java.util.List<Object[]> rows = new java.util.ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Incentive Details");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "EPF No", "Name", "Section", "Designation", "NIC", "Attendance Incentive", "Grading Incentive", "Production Incentive I", "Production Incentive II", "Total Incentive"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 1049, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1477, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(179, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
