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
        javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> rowSorter
                = new javax.swing.table.TableRowSorter<>((javax.swing.table.DefaultTableModel) model.getModel());
        model.setRowSorter(rowSorter);

        searchTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            private void filter() {
                String searchText = searchTextField.getText();
                if (searchText.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(javax.swing.RowFilter.regexFilter("(?i)" + searchText));
                }
            }
        });
    }

    private void loadInactiveEmployees() {
        javax.swing.table.DefaultTableModel tableModel
                = (javax.swing.table.DefaultTableModel) model.getModel();
        tableModel.setRowCount(0);

        String query = "SELECT e.epf_no, e.name_with_initial, e.fname, e.lname, e.dob, "
                + "e.nic, e.mobile, e.father, e.mother, "
                + "e.permanate_address, e.current_address, e.nominee, "
                + "e.married_status, e.district, e.race, "
                + "d.title AS designation, s.section_name "
                + "FROM employee e "
                + "LEFT JOIN designation d ON e.designation_id = d.id "
                + "LEFT JOIN section s ON e.section_id = s.id "
                + "WHERE e.status = 'inactive' "
                + "ORDER BY e.epf_no";

        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("epf_no"), // 0
                    rs.getString("name_with_initial"), // 1
                    rs.getString("fname"), // 2
                    rs.getString("lname"), // 3
                    rs.getString("dob"), // 4
                    rs.getString("nic"), // 5
                    rs.getString("mobile"), // 6
                    rs.getString("father"), // 7
                    rs.getString("mother"), // 8
                    rs.getString("permanate_address"), // 9
                    rs.getString("current_address"), // 10
                    rs.getString("nominee"), // 11
                    rs.getString("married_status"), // 12
                    rs.getString("district"), // 13
                    rs.getString("race"), // 14
                    rs.getString("designation"), // 15
                    rs.getString("section_name") // 16
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
                "epf_no", "Name with Initial", "Fname", "Lname", "DOB", "NIC", "mobile", "Father", "Mother", "Permanate Address", "Current Address", "Nominee", "Married Status", "District", "Race", "Designation", "Section"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(model);
        if (model.getColumnModel().getColumnCount() > 0) {
            model.getColumnModel().getColumn(0).setResizable(false);
            model.getColumnModel().getColumn(1).setResizable(false);
            model.getColumnModel().getColumn(2).setResizable(false);
            model.getColumnModel().getColumn(3).setResizable(false);
            model.getColumnModel().getColumn(4).setResizable(false);
            model.getColumnModel().getColumn(5).setResizable(false);
            model.getColumnModel().getColumn(6).setResizable(false);
            model.getColumnModel().getColumn(7).setResizable(false);
            model.getColumnModel().getColumn(7).setPreferredWidth(200);
            model.getColumnModel().getColumn(8).setResizable(false);
            model.getColumnModel().getColumn(8).setPreferredWidth(200);
            model.getColumnModel().getColumn(9).setResizable(false);
            model.getColumnModel().getColumn(10).setResizable(false);
            model.getColumnModel().getColumn(11).setResizable(false);
            model.getColumnModel().getColumn(12).setResizable(false);
            model.getColumnModel().getColumn(13).setResizable(false);
            model.getColumnModel().getColumn(14).setResizable(false);
            model.getColumnModel().getColumn(15).setResizable(false);
            model.getColumnModel().getColumn(16).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(1081, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 802, Short.MAX_VALUE)
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1531, Short.MAX_VALUE)
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
                    .addGap(63, 63, 63)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(100, Short.MAX_VALUE)))
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

        // NIC use කරන එක වඩා safe (column 5)
        String nic = model.getValueAt(selectedRow, 5).toString();
        String fname = model.getValueAt(selectedRow, 2).toString();
        String lname = model.getValueAt(selectedRow, 3).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to activate " + fname + " " + lname + "?",
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

    /**
     * @param args the command line arguments
     */
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
