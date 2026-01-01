package NerdTech.DR_Fashion.Views.PayRollManage.NewUser;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewUserPanel extends javax.swing.JPanel {

    private String selectedEpfNo;
    private String selectedEmployeeName;

    public NewUserPanel() {
        initComponents();
        loadEmployeeData();
        setupTableDoubleClick();
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
                            openAddNewUserDialog();
                        } catch (Exception ex) {
                            Logger.getLogger(NewUserPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });
    }

    private void openAddNewUserDialog() throws Exception {
        AddNewUserDFrame dialog = new AddNewUserDFrame(null, true, selectedEpfNo, selectedEmployeeName);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        // Refresh table data after dialog is closed
        loadEmployeeData();
    }

    private void loadEmployeeData() {
        try {
            Connection connection = DatabaseConnection.getConnection();

            // Load only employees who DON'T have salary details yet
            String query = "SELECT e.epf_no, e.fname, sec.section_name, d.title, e.nic "
                    + "FROM employee e "
                    + "LEFT JOIN section sec ON e.section_id = sec.id "
                    + "LEFT JOIN designation d ON e.designation_id = d.id "
                    + "LEFT JOIN salary sal ON e.id = sal.employee_id "
                    + "WHERE (e.status = 'active' OR e.status IS NULL) "
                    + "AND sal.id IS NULL " // Only show employees WITHOUT salary records
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

                // Add row to table with only the 5 basic columns
                model.addRow(new Object[]{
                    epfNo,
                    name,
                    section,
                    designation,
                    nic
                });
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading employee data: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading employee data: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("New Uesr Details");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "EPF No", "Name", "Section", "Designation", "NIC"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
        }

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Salary Details");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1414, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(598, 598, 598))
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
                .addGap(46, 46, 46)
                .addComponent(jButton1)
                .addContainerGap(52, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Navigate to Salary Details Panel
        try {
            // Get the parent container (assumes it's using CardLayout or similar)
            java.awt.Container parent = this.getParent();

            if (parent != null) {
                // Remove current panel and add SalaryDetailsPanel
                parent.removeAll();
                parent.add(new SalaryDetailsPanel());
                parent.revalidate();
                parent.repaint();
            } else {
                // Alternative: Open in a new dialog if no parent container
                javax.swing.JDialog dialog = new javax.swing.JDialog();
                dialog.setTitle("Salary Details");
                dialog.setModal(true);
                dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                dialog.add(new SalaryDetailsPanel());
                dialog.setSize(1400, 700);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading Salary Details panel: " + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
