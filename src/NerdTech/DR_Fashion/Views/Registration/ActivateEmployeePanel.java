package NerdTech.DR_Fashion.Views.Registration;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author MG_Pathum
 */
public class ActivateEmployeePanel extends javax.swing.JDialog {

    private DefaultTableModel tableModel;
    private EmployeeRegistration parentPanel;

    // Updated constructor with correct parameters
    public ActivateEmployeePanel(java.awt.Frame parent, boolean modal, EmployeeRegistration parentPanel) {
        super(parent, modal);
        this.parentPanel = parentPanel;
        initComponents();
        initializeTable();
        loadResignationData();
        setupSearchFunctionality();
    }

    // Original constructor for backward compatibility
    public ActivateEmployeePanel(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initializeTable();
        loadResignationData();
        setupSearchFunctionality();
    }

    private void initializeTable() {
        tableModel = (DefaultTableModel) model.getModel();
        // Clear existing columns and add only the ones we need
        tableModel.setColumnCount(0);

        // Add columns for resignation data
        String[] columns = {
            "ID", "EPF No", "Employee Name", "Designation",
            "Resign Type", "Resign Date", "Reason",
            "Service Duration", "Status"
        };

        for (String column : columns) {
            tableModel.addColumn(column);
        }

        // Set the correct table model
        model.setModel(tableModel);
    }

    private void loadResignationData() {
        try {
            tableModel.setRowCount(0); // Clear existing data

            ResultSet rs = DatabaseConnection.getResignationData();
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getInt("epf_no"),
                    rs.getString("name_with_initial"),
                    rs.getString("designation_name"),
                    rs.getString("resign_type"),
                    rs.getDate("resign_date"),
                    rs.getString("reason"),
                    rs.getString("service_duration"),
                    rs.getString("status")
                };
                tableModel.addRow(row);
            }

            // Adjust column widths
            model.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
            model.getColumnModel().getColumn(1).setPreferredWidth(80);  // EPF No
            model.getColumnModel().getColumn(2).setPreferredWidth(150); // Employee Name
            model.getColumnModel().getColumn(3).setPreferredWidth(120); // Designation
            model.getColumnModel().getColumn(4).setPreferredWidth(100); // Resign Type
            model.getColumnModel().getColumn(5).setPreferredWidth(100); // Resign Date
            model.getColumnModel().getColumn(6).setPreferredWidth(200); // Reason
            model.getColumnModel().getColumn(7).setPreferredWidth(100); // Service Duration
            model.getColumnModel().getColumn(8).setPreferredWidth(80);  // Status

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading resignation data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void setupSearchFunctionality() {
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchEmployees();
            }
        });
    }

    private void searchEmployees() {
        String searchText = searchTextField.getText().trim();

        if (searchText.isEmpty()) {
            loadResignationData();
            return;
        }

        try {
            tableModel.setRowCount(0); // Clear existing data

            ResultSet rs = DatabaseConnection.getResignationDataByEmployeeName(searchText);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getInt("epf_no"),
                    rs.getString("name_with_initial"),
                    rs.getString("designation_name"),
                    rs.getString("resign_type"),
                    rs.getDate("resign_date"),
                    rs.getString("reason"),
                    rs.getString("service_duration"),
                    rs.getString("status")
                };
                tableModel.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching resignation data: " + e.getMessage(),
                    "Search Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void activateEmployee() {
        int selectedRow = model.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to activate.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int resignationId = (int) tableModel.getValueAt(selectedRow, 0);
            int epfNo = (int) tableModel.getValueAt(selectedRow, 1);
            String employeeName = (String) tableModel.getValueAt(selectedRow, 2);
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 8);

            // Check if already activated (since we're only loading pending/inactive, this should not happen)
            if ("active".equals(currentStatus)) {
                JOptionPane.showMessageDialog(this,
                        "Employee " + employeeName + " is already activated!",
                        "Already Activated",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to activate employee " + employeeName + " (EPF: " + epfNo + ")?\n\n"
                    + "This will:\n"
                    + "• Update employee status to 'active'\n"
                    + "• Update resignation status to 'active'",
                    "Confirm Activation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Update both employee and resignation status in a transaction
                boolean success = DatabaseConnection.activateEmployeeAndResignation(epfNo, resignationId);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Employee activated successfully!\n"
                            + "• Employee status updated to 'active'\n"
                            + "• Resignation status updated to 'active'",
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Refresh parent panel if available
                    if (parentPanel != null) {
                        parentPanel.refreshTable();
                    }

                    loadResignationData(); // This will reload and the activated record will disappear
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to activate employee.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error activating employee: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deactivateEmployee() {
        int selectedRow = model.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to deactivate.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int resignationId = (int) tableModel.getValueAt(selectedRow, 0);
            int epfNo = (int) tableModel.getValueAt(selectedRow, 1);
            String employeeName = (String) tableModel.getValueAt(selectedRow, 2);
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 8);

            // Check if already deactivated
            if ("inactive".equals(currentStatus)) {
                JOptionPane.showMessageDialog(this,
                        "Employee " + employeeName + " is already deactivated!",
                        "Already Deactivated",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to deactivate employee " + employeeName + " (EPF: " + epfNo + ")?\n\n"
                    + "This will:\n"
                    + "• Update resignation status to 'inactive'\n"
                    + "• Set employee status to 'inactive'",
                    "Confirm Deactivation", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Update both resignation and employee status in a transaction
                boolean success = DatabaseConnection.deactivateEmployeeAndResignation(resignationId, epfNo);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Employee deactivated successfully!\n"
                            + "• Resignation status updated to 'inactive'\n"
                            + "• Employee status set to 'inactive'",
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Refresh parent panel if available
                    if (parentPanel != null) {
                        parentPanel.refreshTable();
                    }

                    loadResignationData(); // This will reload and show the record as inactive
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to deactivate employee.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deactivating employee: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        model = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Activate Employees");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Search");

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Activate");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        model.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        model.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "EPF No", "Employee Name", "Designation", 
                "Resign Type", "Resign Date", "Reason", 
                "Service Duration", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, 
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, 
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(model);

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Inactive");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(1297, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1018, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1747, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 537, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(83, 83, 83)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                    .addGap(84, 84, 84)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        activateEmployee();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        deactivateEmployee();
    }//GEN-LAST:event_jButton3ActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ActivateEmployeePanel dialog = new ActivateEmployeePanel(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables
}
