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

            String query;
            if ("ALL".equals(status)) {
                query = "SELECT e.epf_no, e.name_with_initial, e.fname, e.lname, e.dob, e.nic, e.gender, e.mobile, "
                        + "e.father, e.mother, e.permanate_address, e.current_address, e.nominee, e.married_status, "
                        + "e.district, e.race, d.title, s.section_name FROM employee e "
                        + "LEFT JOIN designation d ON e.designation_id = d.id "
                        + "LEFT JOIN section s ON e.section_id = s.id "
                        + "ORDER BY e.epf_no";
            } else {
                query = "SELECT e.epf_no, e.name_with_initial, e.fname, e.lname, e.dob, e.nic, e.gender, e.mobile, "
                        + "e.father, e.mother, e.permanate_address, e.current_address, e.nominee, e.married_status, "
                        + "e.district, e.race, d.title, s.section_name FROM employee e "
                        + "LEFT JOIN designation d ON e.designation_id = d.id "
                        + "LEFT JOIN section s ON e.section_id = s.id "
                        + "WHERE e.status = ? ORDER BY e.epf_no";
            }

            PreparedStatement pst = conn.prepareStatement(query);
            if (!"ALL".equals(status)) {
                pst.setString(1, status);
            }

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("epf_no"),
                    rs.getString("name_with_initial"),
                    rs.getString("fname"),
                    rs.getString("lname"),
                    rs.getString("dob"),
                    rs.getString("nic"),
                    rs.getString("gender"),
                    rs.getString("mobile"),
                    rs.getString("father"),
                    rs.getString("mother"),
                    rs.getString("permanate_address"),
                    rs.getString("current_address"),
                    rs.getString("nominee"),
                    rs.getString("married_status"),
                    rs.getString("district"),
                    rs.getString("race"),
                    rs.getString("title"), // Now directly from designation table
                    rs.getString("section_name") // Now directly from section table
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

        if (searchText.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            try {
                // Case-insensitive search across all columns
                RowFilter<DefaultTableModel, Integer> rowFilter
                        = RowFilter.regexFilter("(?i)" + searchText);
                rowSorter.setRowFilter(rowFilter);
            } catch (java.util.regex.PatternSyntaxException e) {
                rowSorter.setRowFilter(null);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        model = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        model.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        model.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "epf_no", "Name with Initial", "Fname", "Lname", "DOB", "NIC", "Gender", "mobile", "Father", "Mother", "Permanate Address", "Current Address", "Nominee", "Married Status", "District", "Race", "Designation", "Title", "Section"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(model);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("All Employees");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Search");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1605, Short.MAX_VALUE)
                        .addGap(10, 10, 10))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(79, 79, 79))))
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
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
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
