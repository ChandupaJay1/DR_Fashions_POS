/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Registration;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.sql.*;
import javax.swing.JOptionPane;

public class AllEmployeeDFrame extends javax.swing.JDialog {

    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;

    /**
     * Constructor - Initialize the dialog and load employee data
     */
    public AllEmployeeDFrame(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadEmployeeData("ALL");
        setupSearchListener();
    }

    private void loadEmployeeData(String status) {
        tableModel = (DefaultTableModel) model.getModel();
        tableModel.setRowCount(0);

        try {
            Connection conn = DatabaseConnection.getConnection();

            // හැම employee කෙනෙක්ගේම හැම data එකක්ම ගන්න query
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
                    + "LEFT JOIN resignation r ON e.id = r.employee_id ";

            // Status අනුව filter කරන්න
            if (!"ALL".equals(status)) {
                query += "WHERE e.status = ? ";
            }

            query += "ORDER BY e.epf_no";

            PreparedStatement pst = conn.prepareStatement(query);
            if (!"ALL".equals(status)) {
                pst.setString(1, status);
            }

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] row = {
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
                };
                tableModel.addRow(row);
            }

            // Setup table row sorter for filtering
            rowSorter = new TableRowSorter<>(tableModel);
            model.setRowSorter(rowSorter);

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading employee data: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupSearchListener() {
        searchTextField.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }
        }
        );
    }

    private void performSearch() {
        String searchText = searchTextField.getText().trim();

        if (rowSorter == null) {
            rowSorter = new TableRowSorter<>(tableModel);
            model.setRowSorter(rowSorter);
        }

        if (searchText.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            try {
                // Case-insensitive search across ALL columns
                RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
                    @Override
                    public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                        for (int i = 0; i < entry.getValueCount(); i++) {
                            Object value = entry.getValue(i);
                            if (value != null && value.toString().toLowerCase().contains(searchText.toLowerCase())) {
                                return true;
                            }
                        }
                        return false;
                    }
                };
                rowSorter.setRowFilter(rf);
            } catch (Exception e) {
                rowSorter.setRowFilter(null);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        model = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("All Employees");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Search");

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
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1113, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1791, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(573, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(64, 64, 64)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AllEmployeeDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AllEmployeeDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AllEmployeeDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AllEmployeeDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AllEmployeeDFrame dialog = new AllEmployeeDFrame(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables
}
