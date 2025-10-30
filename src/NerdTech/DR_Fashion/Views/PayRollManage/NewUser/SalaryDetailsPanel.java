/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.PayRollManage.NewUser;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author MG_Pathum
 */
public class SalaryDetailsPanel extends javax.swing.JPanel {

    private String selectedEpfNo;
    private String selectedEmployeeName;

    public SalaryDetailsPanel() throws Exception {
        initComponents();
        loadSalaryData();
        setupTableDoubleClick();
        setupSearchListener();
    }

    private void setupTableDoubleClick() {
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = jTable1.getSelectedRow();
                    if (row != -1) {
                        selectedEpfNo = jTable1.getValueAt(row, 0).toString();
                        selectedEmployeeName = jTable1.getValueAt(row, 1).toString();

                        try {
                            openUpdateSalaryDialog();
                        } catch (Exception ex) {
                            Logger.getLogger(SalaryDetailsPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }

    private void setupSearchListener() {
        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    performSearch();
                } catch (Exception ex) {
                    Logger.getLogger(SalaryDetailsPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    performSearch();
                } catch (Exception ex) {
                    Logger.getLogger(SalaryDetailsPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    performSearch();
                } catch (Exception ex) {
                    Logger.getLogger(SalaryDetailsPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void performSearch() throws Exception {
        String searchText = jTextField1.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadSalaryData();
            return;
        }

        try {
            Connection connection = DatabaseConnection.getConnection();

            // Fixed query - removed attendance_incentive and production_incentive
            String query = "SELECT e.epf_no, e.fname, sec.section_name, d.title, e.nic, "
                    + "sal.basic_salary, sal.bra1, sal.bra2, "
                    + "sal.peoples_bank_account_no, sal.hnb_bank_account_no "
                    + "FROM employee e "
                    + "LEFT JOIN section sec ON e.section_id = sec.id "
                    + "LEFT JOIN designation d ON e.designation_id = d.id "
                    + "INNER JOIN salary sal ON e.id = sal.employee_id "
                    + "WHERE (e.status = 'active' OR e.status IS NULL) "
                    + "AND ("
                    + "LOWER(e.epf_no) LIKE ? OR "
                    + "LOWER(e.fname) LIKE ? OR "
                    + "LOWER(sec.section_name) LIKE ? OR "
                    + "LOWER(d.title) LIKE ? OR "
                    + "LOWER(e.nic) LIKE ?"
                    + ") "
                    + "ORDER BY e.epf_no";

            PreparedStatement statement = connection.prepareStatement(query);
            String searchPattern = "%" + searchText + "%";
            for (int i = 1; i <= 5; i++) {
                statement.setString(i, searchPattern);
            }

            ResultSet resultSet = statement.executeQuery();
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                String epfNo = resultSet.getString("epf_no");
                String name = resultSet.getString("fname");
                String section = resultSet.getString("section_name");
                String designation = resultSet.getString("title");
                String nic = resultSet.getString("nic");

                Double basicSalary = resultSet.getDouble("basic_salary");
                if (resultSet.wasNull()) {
                    basicSalary = null;
                }

                Double bra1 = resultSet.getDouble("bra1");
                if (resultSet.wasNull()) {
                    bra1 = null;
                }

                Double bra2 = resultSet.getDouble("bra2");
                if (resultSet.wasNull()) {
                    bra2 = null;
                }

                String peoplesBank = resultSet.getString("peoples_bank_account_no");
                String hnbBank = resultSet.getString("hnb_bank_account_no");

                model.addRow(new Object[]{
                    epfNo,
                    name,
                    section,
                    designation,
                    nic,
                    basicSalary,
                    bra1,
                    bra2,
                    peoplesBank,
                    hnbBank
                });
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error searching salary data: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openUpdateSalaryDialog() throws Exception {
        UpdateSalaryDFrame dialog = new UpdateSalaryDFrame(null, true, selectedEpfNo, selectedEmployeeName);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        // Refresh table data after dialog is closed
        loadSalaryData();
    }

    public void loadSalaryData() throws Exception {
        try {
            Connection connection = DatabaseConnection.getConnection();

            // Fixed query - removed attendance_incentive and production_incentive
            String query = "SELECT e.epf_no, e.fname, sec.section_name, d.title, e.nic, "
                    + "sal.basic_salary, sal.bra1, sal.bra2, "
                    + "sal.peoples_bank_account_no, sal.hnb_bank_account_no "
                    + "FROM employee e "
                    + "LEFT JOIN section sec ON e.section_id = sec.id "
                    + "LEFT JOIN designation d ON e.designation_id = d.id "
                    + "INNER JOIN salary sal ON e.id = sal.employee_id "
                    + "WHERE (e.status = 'active' OR e.status IS NULL) "
                    + "ORDER BY e.epf_no";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                String epfNo = resultSet.getString("epf_no");
                String name = resultSet.getString("fname");
                String section = resultSet.getString("section_name");
                String designation = resultSet.getString("title");
                String nic = resultSet.getString("nic");

                // Get salary details
                Double basicSalary = resultSet.getDouble("basic_salary");
                if (resultSet.wasNull()) {
                    basicSalary = null;
                }

                Double bra1 = resultSet.getDouble("bra1");
                if (resultSet.wasNull()) {
                    bra1 = null;
                }

                Double bra2 = resultSet.getDouble("bra2");
                if (resultSet.wasNull()) {
                    bra2 = null;
                }

                String peoplesBank = resultSet.getString("peoples_bank_account_no");
                String hnbBank = resultSet.getString("hnb_bank_account_no");

                model.addRow(new Object[]{
                    epfNo,
                    name,
                    section,
                    designation,
                    nic,
                    basicSalary,
                    bra1,
                    bra2,
                    peoplesBank,
                    hnbBank
                });
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading salary data: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Salary Details");

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Update Salary");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "EPF No", "Name", "Section", "Designation", "NIC", "Basic Salary", "BRA 1", "BRA 2", "Peoples Bank Account No", "Hnb Bank Account No"
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

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Go to New User Details");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 14)); // NOI18N
        jLabel2.setText("Search");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1123, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1465, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(49, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Navigate to New User Panel
        try {
            // Get the parent container (assumes it's using CardLayout or similar)
            java.awt.Container parent = this.getParent();

            if (parent != null) {
                // Remove current panel and add NewUserPanel
                parent.removeAll();
                parent.add(new NewUserPanel());
                parent.revalidate();
                parent.repaint();
            } else {
                // Alternative: Open in a new dialog if no parent container
                javax.swing.JDialog dialog = new javax.swing.JDialog();
                dialog.setTitle("New User Details");
                dialog.setModal(true);
                dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                dialog.add(new NewUserPanel());
                dialog.setSize(1400, 700);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading New User Details panel: " + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Update Salary Button - Open dialog for selected employee
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Please select an employee from the table first.",
                    "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedEpfNo = jTable1.getValueAt(selectedRow, 0).toString();
        selectedEmployeeName = jTable1.getValueAt(selectedRow, 1).toString();

        try {
            openUpdateSalaryDialog();
        } catch (Exception ex) {
            Logger.getLogger(SalaryDetailsPanel.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error opening update dialog: " + ex.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
