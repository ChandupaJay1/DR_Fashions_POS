/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Registration;

import NerdTech.DR_Fashion.Views.Accesories.AddAccesoriesDFrame;
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
        initComponents();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1257, 686));
        setBackground(new Color(50, 50, 50));

        showLoading("Connecting to Database");
        loadContentInBackground();

        setupTableModel();
    }

    private void setupTableModel() {
        model.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "EPF No", "Name with Initial", "First Name", "Last Name", "DOB",
                    "NIC", "Gender", "Mobile", "Father", "Mother", "Permanent Address",
                    "Current Address", "Nominee", "Married Status", "District",
                    "Race", "Designation", "Title", "Section" // ✅ NOW 19 columns
                }
        ) {
            boolean[] canEdit = new boolean[19];  // ✅ Changed to 19

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
    }

    private void showLoading(String message) {
        removeAll();
        loadingPanel = new LoadingPanel(message);
        add(loadingPanel, BorderLayout.CENTER);
        revalidate();
        repaint();

        setupTableModel();

        if (model.getColumnModel().getColumnCount() > 0) {
            for (int i = 0; i < model.getColumnModel().getColumnCount(); i++) {
                model.getColumnModel().getColumn(i).setResizable(false);
            }
            model.getColumnModel().getColumn(2).setPreferredWidth(120); // First Name
            model.getColumnModel().getColumn(3).setPreferredWidth(120); // Last Name
            model.getColumnModel().getColumn(9).setPreferredWidth(180); // Permanent Address
            model.getColumnModel().getColumn(10).setPreferredWidth(180); // Current Address
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

        // ✅ Query - fetch both capacity AND title separately
        String query = "SELECT e.epf_no, e.name_with_initial, e.fname, e.lname, e.dob, "
                + "e.nic, e.gender, e.mobile, e.father, e.mother, "
                + "e.permanate_address, e.current_address, e.nominee, "
                + "e.married_status, e.district, e.race, "
                + "d.capacity AS designation, d.title AS title, s.section_name, " // ✅ BOTH columns
                + "e.designation_id, e.section_id " // ✅ IDs for UPDATE
                + "FROM employee e "
                + "LEFT JOIN designation d ON e.designation_id = d.id "
                + "LEFT JOIN section s ON e.section_id = s.id "
                + "WHERE e.status = 'active' "
                + "ORDER BY e.epf_no";

        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // ✅ NOW 19 columns match table model
                tableModel.addRow(new Object[]{
                    rs.getString("epf_no"), // 0
                    rs.getString("name_with_initial"), // 1
                    rs.getString("fname"), // 2
                    rs.getString("lname"), // 3
                    rs.getString("dob"), // 4
                    rs.getString("nic"), // 5
                    rs.getString("gender"), // 6
                    rs.getString("mobile"), // 7
                    rs.getString("father"), // 8
                    rs.getString("mother"), // 9
                    rs.getString("permanate_address"), // 10
                    rs.getString("current_address"), // 11
                    rs.getString("nominee"), // 12
                    rs.getString("married_status"), // 13
                    rs.getString("district"), // 14
                    rs.getString("race"), // 15
                    rs.getString("designation"), // 16 - CAPACITY
                    rs.getString("title"), // 17 - TITLE ✅
                    rs.getString("section_name") // 18
                });
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No active employees found in database.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load employee data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        if (!isInitialized) {
            return;
        }
        showLoading("Refreshing employee data");
        loadContentInBackground();
    }

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
        AllEmployee = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Employee Registration");

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
        if (model.getColumnModel().getColumnCount() > 0) {
            model.getColumnModel().getColumn(0).setResizable(false);
            model.getColumnModel().getColumn(1).setResizable(false);
            model.getColumnModel().getColumn(1).setPreferredWidth(100);
            model.getColumnModel().getColumn(2).setResizable(false);
            model.getColumnModel().getColumn(2).setPreferredWidth(150);
            model.getColumnModel().getColumn(3).setResizable(false);
            model.getColumnModel().getColumn(3).setPreferredWidth(100);
            model.getColumnModel().getColumn(4).setResizable(false);
            model.getColumnModel().getColumn(4).setPreferredWidth(100);
            model.getColumnModel().getColumn(5).setResizable(false);
            model.getColumnModel().getColumn(5).setPreferredWidth(150);
            model.getColumnModel().getColumn(6).setResizable(false);
            model.getColumnModel().getColumn(6).setPreferredWidth(100);
            model.getColumnModel().getColumn(7).setResizable(false);
            model.getColumnModel().getColumn(7).setPreferredWidth(150);
            model.getColumnModel().getColumn(8).setResizable(false);
            model.getColumnModel().getColumn(8).setPreferredWidth(200);
            model.getColumnModel().getColumn(9).setResizable(false);
            model.getColumnModel().getColumn(9).setPreferredWidth(200);
            model.getColumnModel().getColumn(10).setResizable(false);
            model.getColumnModel().getColumn(10).setPreferredWidth(200);
            model.getColumnModel().getColumn(11).setResizable(false);
            model.getColumnModel().getColumn(11).setPreferredWidth(200);
            model.getColumnModel().getColumn(12).setResizable(false);
            model.getColumnModel().getColumn(12).setPreferredWidth(100);
            model.getColumnModel().getColumn(13).setResizable(false);
            model.getColumnModel().getColumn(13).setPreferredWidth(100);
            model.getColumnModel().getColumn(14).setResizable(false);
            model.getColumnModel().getColumn(14).setPreferredWidth(100);
            model.getColumnModel().getColumn(15).setResizable(false);
            model.getColumnModel().getColumn(15).setPreferredWidth(100);
            model.getColumnModel().getColumn(16).setResizable(false);
            model.getColumnModel().getColumn(16).setPreferredWidth(100);
            model.getColumnModel().getColumn(17).setResizable(false);
            model.getColumnModel().getColumn(18).setResizable(false);
            model.getColumnModel().getColumn(18).setPreferredWidth(100);
        }

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Resign");
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

        AllEmployee.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        AllEmployee.setText("All Employee");
        AllEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AllEmployeeActionPerformed(evt);
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
                        .addGap(39, 39, 39)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(AllEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AllEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to update.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // ✅ Get data from selected row - CORRECT INDICES with Title column
            String epfNo = model.getValueAt(selectedRow, 0).toString();     // 0
            String nameInitial = model.getValueAt(selectedRow, 1).toString(); // 1
            String fname = model.getValueAt(selectedRow, 2).toString();     // 2
            String lname = model.getValueAt(selectedRow, 3).toString();     // 3
            String dob = model.getValueAt(selectedRow, 4).toString();       // 4
            String nic = model.getValueAt(selectedRow, 5).toString();       // 5
            String gender = model.getValueAt(selectedRow, 6).toString();    // 6
            String mobile = model.getValueAt(selectedRow, 7).toString();    // 7
            String father = model.getValueAt(selectedRow, 8).toString();    // 8
            String mother = model.getValueAt(selectedRow, 9).toString();    // 9
            String permAddr = model.getValueAt(selectedRow, 10).toString(); // 10
            String currAddr = model.getValueAt(selectedRow, 11).toString(); // 11
            String nominee = model.getValueAt(selectedRow, 12).toString();  // 12
            String marriedStatus = model.getValueAt(selectedRow, 13).toString(); // 13
            String district = model.getValueAt(selectedRow, 14).toString(); // 14
            String race = model.getValueAt(selectedRow, 15).toString();     // 15
            // Column 16 = Designation (capacity) - not needed
            // Column 17 = Title - not needed
            // Column 18 = Section - not needed

            // ✅ Database query to get IDs
            try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection()) {
                String query = "SELECT e.designation_id, e.section_id, e.married_status "
                        + "FROM employee e "
                        + "WHERE e.nic = ?";

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, nic);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String marriedStatusEnum = rs.getString("married_status");
                    int marriedStatusId = convertMarriedStatusToId(marriedStatusEnum);
                    int designationId = rs.getInt("designation_id");
                    int sectionId = rs.getInt("section_id");

                    System.out.println("=== Opening UpdateEmployeeFrame ===");
                    System.out.println("Designation ID: " + designationId);
                    System.out.println("Section ID: " + sectionId);

                    // ✅ Open UpdateEmployeeFrame
                    UpdateEmployeeFrame updateFrame = new UpdateEmployeeFrame(
                            this,
                            epfNo,
                            fname,
                            lname,
                            nameInitial,
                            dob,
                            nic,
                            mobile,
                            father,
                            mother,
                            currAddr,
                            permAddr,
                            nominee,
                            marriedStatusId,
                            district,
                            race,
                            gender,
                            designationId, // ✅ This should be correct now
                            sectionId
                    );
                    updateFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Employee details not found in database.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                rs.close();
                stmt.close();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading employee details: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private int convertMarriedStatusToId(String status) {
        if (status == null || status.trim().isEmpty()) {
            return 1;
        }

        String normalized = status.trim();
        switch (normalized) {
            case "Married":
                return 1;
            case "Unmarried":
            case "Single":
                return 2;
            default:
                System.out.println("Unknown married status ENUM: " + status + ", defaulting to 1");
                return 1;
        }
    }

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

        String nic = model.getValueAt(selectedRow, 5).toString();
        String fname = model.getValueAt(selectedRow, 2).toString();
        String lname = model.getValueAt(selectedRow, 3).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to deactivate " + fname + " " + lname + "?",
                "Confirm Deactivate", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection()) {
            String sql = "UPDATE employee SET status = 'inactive' WHERE nic = ? AND status = 'active'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nic);

            int updated = ps.executeUpdate();

            if (updated > 0) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Employee deactivated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Employee is already inactive or not found.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deactivating employee: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void AllEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AllEmployeeActionPerformed
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        AllEmployeeDFrame allEmployeeDFrame = new AllEmployeeDFrame((java.awt.Frame) parentWindow, true);
        allEmployeeDFrame.setVisible(true);
    }//GEN-LAST:event_AllEmployeeActionPerformed

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
    private javax.swing.JButton AllEmployee;
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
