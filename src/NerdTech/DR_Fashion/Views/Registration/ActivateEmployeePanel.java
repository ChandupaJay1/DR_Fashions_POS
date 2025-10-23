/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Registration;

import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;

/**
 *
 * @author MG_Pathum
 */
public class ActivateEmployeePanel extends javax.swing.JDialog {

    private EmployeeRegistration employeeRegistrationPanel;

    public ActivateEmployeePanel(java.awt.Frame parent, boolean modal, EmployeeRegistration employeeRegistrationPanel) {
        super(parent, modal);
        this.employeeRegistrationPanel = employeeRegistrationPanel;
        initComponents();
        loadInactiveEmployees();
        setupSearchFilter();
    }

    private void setupSearchFilter() {
        // Create and attach a row sorter
        javax.swing.table.DefaultTableModel tableModel = (javax.swing.table.DefaultTableModel) model.getModel();
        javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> rowSorter
                = new javax.swing.table.TableRowSorter<>(tableModel);
        model.setRowSorter(rowSorter);

        // Add a document listener for real-time filtering
        searchTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filterTable() {
                String searchText = searchTextField.getText().trim();

                if (searchText.isEmpty()) {
                    rowSorter.setRowFilter(null);
                    return;
                }

                // Custom filter — searches all columns, case-insensitive, no regex issues
                javax.swing.RowFilter<javax.swing.table.DefaultTableModel, Object> rowFilter
                        = new javax.swing.RowFilter<javax.swing.table.DefaultTableModel, Object>() {
                    @Override
                    public boolean include(Entry<? extends javax.swing.table.DefaultTableModel, ? extends Object> entry) {
                        for (int i = 0; i < entry.getValueCount(); i++) {
                            Object value = entry.getValue(i);
                            if (value != null && value.toString().toLowerCase().contains(searchText.toLowerCase())) {
                                return true;
                            }
                        }
                        return false;
                    }
                };

                rowSorter.setRowFilter(rowFilter);
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }
        });
    }

    private void loadInactiveEmployees() {
        javax.swing.table.DefaultTableModel tableModel
                = (javax.swing.table.DefaultTableModel) model.getModel();
        tableModel.setRowCount(0);

        // Employee table එකේ inactive status එක තියෙන employees විතරක් ගන්න
        String query = "SELECT "
                + "e.epf_no, " // 0
                + "e.name_with_initial, " // 1
                + "e.fname, " // 2
                + "e.initials, " // 3
                + "e.surname, " // 4
                + "e.dob, " // 5
                + "e.nic, " // 6
                + "e.gender, " // 7
                + "e.mobile, " // 8
                + "e.father, " // 9
                + "e.mother, " // 10
                + "e.religion, " // 11
                + "e.recruited_date, " // 12
                + "e.as_today, " // 13
                + "e.confirmation_date, " // 14
                + "e.service_end_date, " // 15
                + "e.date_to_service_end, " // 16
                + "e.permanate_address, " // 17
                + "e.current_address, " // 18
                + "e.electroate, " // 19
                + "e.nominee, " // 20
                + "e.married_status, " // 21
                + "e.district, " // 22
                + "e.race, " // 23
                + "d.title AS designation, " // 24
                + "c.name AS capacity, " // 25
                + "s.section_name, " // 26
                + "e.joined_date, " // 27
                + "CONCAT(e.fname, ' ', e.surname) AS employee, " // 28
                + "r.resign_type, " // 29
                + "r.resign_date, " // 30
                + "r.reason, " // 31
                + "r.service_duration " // 32
                + "FROM employee e "
                + "LEFT JOIN designation d ON e.designation_id = d.id "
                + "LEFT JOIN section s ON e.section_id = s.id "
                + "LEFT JOIN capacity c ON e.capacity_id = c.id "
                + "LEFT JOIN resignation r ON e.id = r.employee_id "
                + "WHERE e.status = 'inactive' " // මෙතන නිවැරදි කරන්න
                + "ORDER BY e.epf_no";

        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("epf_no"), // 0
                    rs.getString("name_with_initial"), // 1
                    rs.getString("fname"), // 2
                    rs.getString("initials"), // 3
                    rs.getString("surname"), // 4
                    rs.getDate("dob"), // 5
                    rs.getString("nic"), // 6
                    rs.getString("gender"), // 7
                    rs.getString("mobile"), // 8
                    rs.getString("father"), // 9
                    rs.getString("mother"), // 10
                    rs.getString("religion"), // 11
                    rs.getDate("recruited_date"), // 12
                    rs.getString("as_today"), // 13
                    rs.getDate("confirmation_date"), // 14
                    rs.getDate("service_end_date"), // 15
                    rs.getString("date_to_service_end"), // 16
                    rs.getString("permanate_address"), // 17
                    rs.getString("current_address"), // 18
                    rs.getString("electroate"), // 19
                    rs.getString("nominee"), // 20
                    rs.getString("married_status"), // 21
                    rs.getString("district"), // 22
                    rs.getString("race"), // 23
                    rs.getString("designation"), // 24
                    rs.getString("capacity"), // 25
                    rs.getString("section_name"), // 26
                    rs.getDate("joined_date"), // 27
                    rs.getString("employee"), // 28
                    rs.getString("resign_type"), // 29 - Resignation table එකෙන්
                    rs.getDate("resign_date"), // 30 - Resignation table එකෙන්
                    rs.getString("reason"), // 31 - Resignation table එකෙන්
                    rs.getString("service_duration") // 32 - Resignation table එකෙන්
                });
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No inactive employees found.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load inactive employees: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
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
            new Object [][] {

            },
            new String [] {
                "epf_no", "Name with Initial", "Fname", "Initials", "Surname", "DOB", "NIC", "Gender", "mobile", "Father", "Mother", "Religion", "Recruited Date", "As Today", "Confirmation Date", "Service End Date", "Date To Service_end", "Permanate Address", "Current Address", "elctroate", "Nominee", "Married Status", "District", "Race", "Designation", "Capacity", "Section", "Joined Date", "Employee", "Resign Type", "Resign Date", "Reason", "Service Duration"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, true, false, true, true, true, false, false, false, false, false, false, false, false, false, false, false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(model);

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
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
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
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        int selectedRow = model.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to activate.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // NIC use කරන එක වඩා safe (column 6)
        String nic = model.getValueAt(selectedRow, 6).toString();
        String fname = model.getValueAt(selectedRow, 2).toString();
        String surname = model.getValueAt(selectedRow, 4).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to activate " + fname + " " + surname + "?",
                "Confirm Activation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection()) {
            // NIC use කරනවා - වඩා accurate
            String sql = "UPDATE employee SET status = 'active' WHERE nic = ? AND status = 'inactive'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nic);

            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this,
                        "Employee activated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                employeeRegistrationPanel.refreshTable();
                loadInactiveEmployees();  // Refresh this table too
            } else {
                JOptionPane.showMessageDialog(this,
                        "Employee not found or already active.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Activation failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) {
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EmployeeRegistration empReg = new EmployeeRegistration(); // mock panel eka
                ActivateEmployeePanel dialog = new ActivateEmployeePanel(new javax.swing.JFrame(), true, empReg);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables
}
