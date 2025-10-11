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
        setupButtonActions();
        customizeTableRenderer();
        resetTableIfNewDay();
        resetTableIfNewMonth();

    }

    private void resetTableIfNewMonth() {
        try {
            java.io.File file = new java.io.File("last_reset_month.txt");
            String currentMonth = java.time.YearMonth.now().toString(); // e.g., "2025-10"

            if (file.exists()) {
                java.util.Scanner sc = new java.util.Scanner(file);
                String lastMonth = sc.hasNextLine() ? sc.nextLine() : "";
                sc.close();

                if (!currentMonth.equals(lastMonth)) {
                    DefaultTableModel tableModel = (DefaultTableModel) model.getModel();
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        tableModel.setValueAt("", i, 1); // Reset status
                    }

                    try (java.io.FileWriter fw = new java.io.FileWriter(file)) {
                        fw.write(currentMonth);
                    }
                }

            } else {
                try (java.io.FileWriter fw = new java.io.FileWriter(file)) {
                    fw.write(currentMonth);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetTableIfNewDay() {
        try {
            java.io.File file = new java.io.File("last_reset.txt");
            String today = java.time.LocalDate.now().toString();

            if (file.exists()) {
                java.util.Scanner sc = new java.util.Scanner(file);
                String lastDate = sc.hasNextLine() ? sc.nextLine() : "";
                sc.close();

                // If last date != today → clear status column
                if (!today.equals(lastDate)) {
                    DefaultTableModel tableModel = (DefaultTableModel) model.getModel();
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        tableModel.setValueAt("", i, 1); // Clear status
                    }

                    // Update file with today's date
                    try (java.io.FileWriter fw = new java.io.FileWriter(file)) {
                        fw.write(today);
                    }
                }
            } else {
                // File not found → create and write today's date
                try (java.io.FileWriter fw = new java.io.FileWriter(file)) {
                    fw.write(today);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupButtonActions() {
        jButton1.addActionListener(e -> markAsPayed());
    }

    private void customizeTableRenderer() {
        model.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String status = (String) table.getValueAt(row, 1);

                if ("Payed".equalsIgnoreCase(status)) {
                    // ✅ Payed row → green background, black text
                    c.setBackground(new java.awt.Color(102, 255, 102));
                    c.setForeground(java.awt.Color.BLACK);
                } else if (isSelected) {
                    // ✅ Selected row → default selection color
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    // ✅ All other rows → default background and foreground
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }

                return c;
            }
        });
    }

    private void markAsPayed() {
        int selectedRow = model.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee first!");
            return;
        }

        String name = model.getValueAt(selectedRow, 0).toString();
        String status = "Payed";

        // ✅ Update table UI
        model.setValueAt(status, selectedRow, 1);
        model.setSelectionBackground(new java.awt.Color(102, 255, 102)); // Green color

        // ✅ Save to DB
        try (Connection con = DatabaseConnection.getConnection()) {

            String sql = "INSERT INTO monthly_payment (name, status, date) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, status);
            pst.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now())); // set current date

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Payment recorded for " + name);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving payment: " + ex.getMessage());
        }
    }

    private void loadEmployeeNames() {
        try (Connection con = DatabaseConnection.getConnection()) {
            PreparedStatement pst = con.prepareStatement(
                    "SELECT e.fname, e.lname, "
                    + "(SELECT status FROM monthly_payment WHERE name = CONCAT(e.fname, ' ', e.lname) AND MONTH(date) = MONTH(CURRENT_DATE()) AND YEAR(date) = YEAR(CURRENT_DATE()) LIMIT 1) AS status "
                    + "FROM employee e"
            );
            ResultSet rs = pst.executeQuery();

            DefaultTableModel tableModel = (DefaultTableModel) model.getModel();
            tableModel.setRowCount(0); // clear table

            while (rs.next()) {
                String fullName = rs.getString("fname") + " " + rs.getString("lname");
                String status = rs.getString("status");

                if (status == null) {
                    status = ""; // not paid
                }

                tableModel.addRow(new Object[]{fullName, status});
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
        jButton1 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1257, 0));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("PayRoll Management");

        model.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        model.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Name", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
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
            model.getColumnModel().getColumn(1).setResizable(false);
        }

        jButton1.setBackground(new java.awt.Color(102, 255, 102));
        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("Payed");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1245, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(499, 499, 499)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    // End of variables declaration//GEN-END:variables
}
