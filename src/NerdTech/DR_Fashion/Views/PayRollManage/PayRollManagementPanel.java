package NerdTech.DR_Fashion.Views.PayRollManage;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PayRollManagementPanel extends javax.swing.JPanel {

    public PayRollManagementPanel() {
        initComponents();
        loadEmployeeNames();
    }

    private void loadEmployeeNames() {
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pst = con.prepareStatement("SELECT fname, lname FROM employee"); ResultSet rs = pst.executeQuery()) {

            DefaultTableModel tableModel = (DefaultTableModel) model.getModel();
            tableModel.setRowCount(0); // Clear existing data

            while (rs.next()) {
                String fullName = rs.getString("fname") + " " + rs.getString("lname");
                tableModel.addRow(new Object[]{fullName});
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employee names: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        model = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(1257, 0));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("PayRoll Management");

        model.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        model.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        model.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                modelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(model);
        if (model.getColumnModel().getColumnCount() > 0) {
            model.getColumnModel().getColumn(0).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1133, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void modelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modelMouseClicked
        if (evt.getClickCount() == 2) { // double-click
            int selectedRow = model.getSelectedRow();
            if (selectedRow != -1) {
                String fullName = model.getValueAt(selectedRow, 0).toString();
                openBillingDialog(fullName);
            }
        }

    }//GEN-LAST:event_modelMouseClicked

    private void openBillingDialog(String fullName) {
        try (Connection con = DatabaseConnection.getConnection()) {

            String sql = "SELECT e.fname, e.lname, e.email, e.nic, e.mobile, r.position AS role_name, e.id AS emp_id "
                    + "FROM employee e "
                    + "LEFT JOIN role r ON e.role_id = r.id "
                    + "WHERE CONCAT(e.fname, ' ', e.lname) = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, fullName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String name = rs.getString("fname") + " " + rs.getString("lname");
                String email = rs.getString("email");
                String nic = rs.getString("nic");
                String mobile = rs.getString("mobile");
                String role = rs.getString("role_name");
                int empId = rs.getInt("emp_id");

                // ✅ Open BillingDFrame and set the data
                BillingDFrame billing = new BillingDFrame(null, true);

                billing.setEmployeeData(name, email, nic, mobile, role, empId);

                billing.setLocationRelativeTo(this);
                billing.setVisible(true);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + ex.getMessage());
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    // End of variables declaration//GEN-END:variables
}
