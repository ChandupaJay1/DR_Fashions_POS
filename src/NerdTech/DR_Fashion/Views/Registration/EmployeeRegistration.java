/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Registration;

import NerdTech.DR_Fashion.Views.LoadingPanel;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MG_Pathum
 */
public class EmployeeRegistration extends javax.swing.JPanel {

    private LoadingPanel loadingPanel;
    private boolean isInitialized = false;
    private int selectedRow;

    public EmployeeRegistration() {
        initComponents(); // Initialize all GUI components first
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1257, 686));
        setBackground(new Color(245, 247, 250));

        showLoading("Connecting to Database");
        loadContentInBackground();

        model.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "First Name", "Last Name", "Email", "DOB", "NIC", "Mobile", "Role", "Status"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

    }

    private void showLoading(String message) {
        removeAll();
        loadingPanel = new LoadingPanel(message);
        add(loadingPanel, BorderLayout.CENTER);
        revalidate();
        repaint();

        model.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "First Name", "Last Name", "Email", "DOB", "NIC", "Mobile", "Role", "Status"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        if (model.getColumnModel().getColumnCount() > 0) {
            for (int i = 0; i < 8; i++) {
                model.getColumnModel().getColumn(i).setResizable(false);
            }
            model.getColumnModel().getColumn(2).setPreferredWidth(150); // Email column
        }

    }

    private void loadContentInBackground() {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {

            @Override
            protected Void doInBackground() throws Exception {
                publish("Loading employee data");
                Thread.sleep(300);

                try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection()) {
                    publish("Fetching records");
                    Thread.sleep(300);
                }

                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                if (loadingPanel != null && !chunks.isEmpty()) {
                    loadingPanel.setMessage(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                    showActualContent();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("Failed to load data: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void showActualContent() {
        removeAll();
        initComponents();
        loadEmployees();
        setupSearchFilter();
        isInitialized = true;
        revalidate();
        repaint();
    }

    private void showError(String errorMessage) {
        removeAll();

        JPanel errorPanel = new JPanel(new GridBagLayout());
        errorPanel.setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);

        JLabel errorIcon = new JLabel("⚠️");
        errorIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        errorPanel.add(errorIcon, gbc);

        gbc.gridy = 1;
        JLabel errorLabel = new JLabel("Connection Failed");
        errorLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 24));
        errorLabel.setForeground(new Color(239, 68, 68));
        errorPanel.add(errorLabel, gbc);

        gbc.gridy = 2;
        JLabel errorMsg = new JLabel(errorMessage);
        errorMsg.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        errorMsg.setForeground(new Color(100, 116, 139));
        errorPanel.add(errorMsg, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 0, 0);
        JButton retryBtn = new JButton("Retry");
        retryBtn.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        retryBtn.addActionListener(e -> {
            showLoading("Reconnecting");
            loadContentInBackground();
        });
        errorPanel.add(retryBtn, gbc);

        add(errorPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
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

    private void loadEmployees() {
        DefaultTableModel tableModel = (DefaultTableModel) model.getModel();
        tableModel.setRowCount(0);

        String query = "SELECT e.fname, e.lname, e.email, e.dob, e.nic, e.mobile, r.position, e.status "
                + "FROM employee e JOIN role r ON e.role_id = r.id";

        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("status");

                if ("active".equalsIgnoreCase(status)) { // active only
                    tableModel.addRow(new Object[]{
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("email"),
                        rs.getString("dob"),
                        rs.getString("nic"),
                        rs.getString("mobile"),
                        rs.getString("position")
                    // status column GUI එකට add කරන්නේ නෑ
                    });
                }
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No active employees found in database.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load employee data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshTable() {
        if (!isInitialized) {
            return;
        }
        showLoading("Refreshing employee data");
        loadContentInBackground();
    }

    public void addEmployeeToTable(String fName, String lName, String email,
            String dob, String nic, String mobile, String role) {
        if (!isInitialized) {
            return;
        }
        javax.swing.table.DefaultTableModel tableModel
                = (javax.swing.table.DefaultTableModel) model.getModel();
        tableModel.addRow(new Object[]{fName, lName, email, dob, nic, mobile, role});
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        model = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Employee Registration");

        model.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        model.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "First Name", "Last Name", "Email", "DOB", "NIC", "Mobile", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
            model.getColumnModel().getColumn(2).setPreferredWidth(150);
            model.getColumnModel().getColumn(3).setResizable(false);
            model.getColumnModel().getColumn(4).setResizable(false);
            model.getColumnModel().getColumn(5).setResizable(false);
            model.getColumnModel().getColumn(6).setResizable(false);
        }

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Activate Employee");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Add Employee");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Update Employee");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Search");

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton4.setText("Delete Employee");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(382, 382, 382)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        add(jPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        AddEmployeeFrame frame = new AddEmployeeFrame(this);
        frame.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = model.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(EmployeeRegistration.this,
                    "Please select a row to update.", "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fname = model.getValueAt(selectedRow, 0).toString();
        String lname = model.getValueAt(selectedRow, 1).toString();
        String email = model.getValueAt(selectedRow, 2).toString();
        String dob = model.getValueAt(selectedRow, 3).toString();
        String nic = model.getValueAt(selectedRow, 4).toString();
        String mobile = model.getValueAt(selectedRow, 5).toString();
        String roleName = model.getValueAt(selectedRow, 6).toString();
        NerdTech.DR_Fashion.RoleItems.RoleItem selectedRoleItem = null;

        try (java.sql.Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection()) {
            String sql = "SELECT id, position FROM role WHERE position = ?";
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roleName);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                selectedRoleItem = new NerdTech.DR_Fashion.RoleItems.RoleItem(
                        rs.getInt("id"), rs.getString("position"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error fetching role data: " + e.getMessage());
        }

        if (selectedRoleItem != null) {
            UpdateEmployeeFrame updateFrame = new UpdateEmployeeFrame(
                    EmployeeRegistration.this, fname, lname, email, dob, nic, mobile, selectedRoleItem);
            updateFrame.setVisible(true);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Role not found in database.", "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ActivateEmployeePanel activateDialog = new ActivateEmployeePanel(
                (JFrame) SwingUtilities.getWindowAncestor(this), true, this);
        activateDialog.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int selectedRow = model.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to deactivate.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fname = model.getValueAt(selectedRow, 0).toString();
        String lname = model.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to deactivate " + fname + " " + lname + "?",
                "Confirm Deactivate", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection()) {
            String sql = "UPDATE employee SET status = 'inactive' WHERE fname = ? AND lname = ? AND status = 'active'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fname);
            ps.setString(2, lname);

            int updated = ps.executeUpdate();

            if (updated > 0) {
                // Remove the row from table GUI
                javax.swing.table.DefaultTableModel tableModel = (javax.swing.table.DefaultTableModel) model.getModel();
                tableModel.removeRow(selectedRow);

                refreshTable();

                JOptionPane.showMessageDialog(this,
                        "Employee deactivated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Employee is already inactive or not found.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error deactivating employee: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    public static void main(String args[]) {
        FlatMacLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Employee Registration");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new EmployeeRegistration());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables
}
