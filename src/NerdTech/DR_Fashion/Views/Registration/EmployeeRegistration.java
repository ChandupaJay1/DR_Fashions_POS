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

        jScrollPane1 = new javax.swing.JScrollPane();
        model = new javax.swing.JTable();

// Scroll policies සැකසීම
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        model.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
// ... අනෙකුත් code එක
        jScrollPane1.setViewportView(model);

    }

    private void setupTableModel() {
        model.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "EPF No", "Name with Initial", "First Name", "Initials", "Surname",
                    "Gender", "DOB", "NIC", "Mobile", "Father", "Mother",
                    "Religion", "Recruited Date", "As Today", "Confirmation Date",
                    "Service End Date", "Days to Service End", "Electroate",
                    "Permanent Address", "Current Address", "Nominee",
                    "Married Status", "District", "Race", "Designation",
                    "Capacity", "Section", "Joined Date"
                }
        ) {
            boolean[] canEdit = new boolean[28];

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        // Auto resize mode සැකසීම
        model.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    }

    private void showLoading(String message) {
        removeAll();
        loadingPanel = new LoadingPanel(message);
        add(loadingPanel, BorderLayout.CENTER);
        revalidate();
        repaint();

        setupTableModel();

        if (model.getColumnModel().getColumnCount() > 0) {
            // Column widths වඩාත් සුදුසු ලෙස සැකසීම
            int[] columnWidths = {
                80, // EPF No
                150, // Name with Initial  
                120, // First Name
                60, // Initials
                120, // Surname
                80, // Gender
                100, // DOB
                150, // NIC
                120, // Mobile
                120, // Father
                120, // Mother
                100, // Religion
                120, // Recruited Date
                100, // As Today
                120, // Confirmation Date
                120, // Service End Date
                140, // Days to Service End
                100, // Electroate
                200, // Permanent Address
                200, // Current Address
                120, // Nominee
                120, // Married Status
                100, // District
                80, // Race
                150, // Designation
                120, // Capacity
                100, // Section
                120 // Joined Date
            };

            for (int i = 0; i < model.getColumnModel().getColumnCount(); i++) {
                model.getColumnModel().getColumn(i).setResizable(true);
                if (i < columnWidths.length) {
                    model.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
                    model.getColumnModel().getColumn(i).setMinWidth(50); // අවම width
                }
            }

            // Auto resize off කිරීම
            model.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
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
        javax.swing.table.DefaultTableModel tableModel = (javax.swing.table.DefaultTableModel) model.getModel();
        javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> rowSorter
                = new javax.swing.table.TableRowSorter<>(tableModel);
        model.setRowSorter(rowSorter);

        searchTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filterTable() {
                String searchText = searchTextField.getText().trim();

                if (searchText.isEmpty()) {
                    rowSorter.setRowFilter(null);
                    return;
                }

                // Search across all columns, case-insensitive
                javax.swing.RowFilter<javax.swing.table.DefaultTableModel, Object> rf
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
                rowSorter.setRowFilter(rf);
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

    private void loadEmployees() {
        DefaultTableModel tableModel = (DefaultTableModel) model.getModel();
        tableModel.setRowCount(0);

        String query = "SELECT "
                + "e.epf_no, e.name_with_initial, e.fname, e.initials, e.surname, "
                + "e.gender, e.dob, e.nic, e.mobile, e.father, e.mother, "
                + "e.religion, e.recruited_date, e.as_today, e.confirmation_date, "
                + "e.service_end_date, e.date_to_service_end, e.electroate, "
                + "e.permanate_address, e.current_address, e.nominee, "
                + "e.married_status, e.district, e.race, "
                + "d.title AS designation_title, "
                + "c.name AS capacity_name, "
                + "s.section_name, "
                + "e.joined_date "
                + "FROM employee e "
                + "LEFT JOIN designation d ON e.designation_id = d.id "
                + "LEFT JOIN capacity c ON e.capacity_id = c.id "
                + "LEFT JOIN section s ON e.section_id = s.id "
                + "WHERE e.status = 'active' "
                + "ORDER BY e.epf_no";

        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("epf_no"),
                    rs.getString("name_with_initial"),
                    rs.getString("fname"),
                    rs.getString("initials"),
                    rs.getString("surname"),
                    rs.getString("gender"),
                    rs.getString("dob"),
                    rs.getString("nic"),
                    rs.getString("mobile"),
                    rs.getString("father"),
                    rs.getString("mother"),
                    rs.getString("religion"),
                    rs.getString("recruited_date"),
                    rs.getString("as_today"), // ✅ As Today
                    rs.getString("confirmation_date"), // ✅ Confirmation Date
                    rs.getString("service_end_date"),
                    rs.getString("date_to_service_end"),
                    rs.getString("electroate"),
                    rs.getString("permanate_address"),
                    rs.getString("current_address"),
                    rs.getString("nominee"),
                    rs.getString("married_status"),
                    rs.getString("district"),
                    rs.getString("race"),
                    rs.getString("designation_title"),
                    rs.getString("capacity_name"),
                    rs.getString("section_name"),
                    rs.getString("joined_date")
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
                "epf_no", "Name with Initial", "Fname", "Initials", "Surname", "DOB", "NIC", "Gender", "mobile", "Father", "Mother", "Religion", "Recruited Date", "As Today", "Confirmation Date", "Service End Date", "Date To Service_end", "Permanate Address", "Current Address", "elctroate", "Nominee", "Married Status", "District", "Race", "Designation", "Capacity", "Section", "Joined Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, true, false, true, true, true, false, false, false, false, false, false, false, false, false, false, false
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
            model.getColumnModel().getColumn(5).setPreferredWidth(100);
            model.getColumnModel().getColumn(6).setResizable(false);
            model.getColumnModel().getColumn(6).setPreferredWidth(150);
            model.getColumnModel().getColumn(7).setResizable(false);
            model.getColumnModel().getColumn(7).setPreferredWidth(100);
            model.getColumnModel().getColumn(8).setResizable(false);
            model.getColumnModel().getColumn(8).setPreferredWidth(150);
            model.getColumnModel().getColumn(9).setResizable(false);
            model.getColumnModel().getColumn(9).setPreferredWidth(200);
            model.getColumnModel().getColumn(10).setResizable(false);
            model.getColumnModel().getColumn(10).setPreferredWidth(200);
            model.getColumnModel().getColumn(11).setResizable(false);
            model.getColumnModel().getColumn(15).setPreferredWidth(150);
            model.getColumnModel().getColumn(16).setPreferredWidth(150);
            model.getColumnModel().getColumn(17).setResizable(false);
            model.getColumnModel().getColumn(17).setPreferredWidth(200);
            model.getColumnModel().getColumn(18).setResizable(false);
            model.getColumnModel().getColumn(18).setPreferredWidth(200);
            model.getColumnModel().getColumn(19).setResizable(false);
            model.getColumnModel().getColumn(20).setResizable(false);
            model.getColumnModel().getColumn(20).setPreferredWidth(100);
            model.getColumnModel().getColumn(21).setResizable(false);
            model.getColumnModel().getColumn(21).setPreferredWidth(100);
            model.getColumnModel().getColumn(22).setResizable(false);
            model.getColumnModel().getColumn(22).setPreferredWidth(100);
            model.getColumnModel().getColumn(23).setResizable(false);
            model.getColumnModel().getColumn(23).setPreferredWidth(100);
            model.getColumnModel().getColumn(24).setResizable(false);
            model.getColumnModel().getColumn(24).setPreferredWidth(100);
            model.getColumnModel().getColumn(25).setResizable(false);
            model.getColumnModel().getColumn(26).setResizable(false);
            model.getColumnModel().getColumn(26).setPreferredWidth(100);
            model.getColumnModel().getColumn(27).setResizable(false);
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
                        .addGap(48, 48, 48)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(AllEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
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
            // Get employee data from selected row - CORRECT COLUMN INDEXES
            String epfNo = model.getValueAt(selectedRow, 0).toString();           // EPF No
            String nameWithInitial = model.getValueAt(selectedRow, 1).toString(); // Name with Initial
            String firstName = model.getValueAt(selectedRow, 2).toString();       // First Name
            String initials = model.getValueAt(selectedRow, 3).toString();        // Initials
            String surname = model.getValueAt(selectedRow, 4).toString();         // Surname
            String gender = model.getValueAt(selectedRow, 5).toString();          // Gender
            String dob = model.getValueAt(selectedRow, 6).toString();             // DOB
            String nic = model.getValueAt(selectedRow, 7).toString();             // NIC
            String mobile = model.getValueAt(selectedRow, 8).toString();          // Mobile
            String father = model.getValueAt(selectedRow, 9).toString();          // Father
            String mother = model.getValueAt(selectedRow, 10).toString();         // Mother
            String religion = model.getValueAt(selectedRow, 11).toString();       // Religion
            String electroate = model.getValueAt(selectedRow, 17).toString();     // Electroate
            String permanentAddress = model.getValueAt(selectedRow, 18).toString(); // Permanent Address
            String currentAddress = model.getValueAt(selectedRow, 19).toString(); // Current Address
            String nominee = model.getValueAt(selectedRow, 20).toString();        // Nominee
            String marriedStatus = model.getValueAt(selectedRow, 21).toString();  // Married Status
            String district = model.getValueAt(selectedRow, 22).toString();       // District
            String race = model.getValueAt(selectedRow, 23).toString();           // Race
            String designationTitle = model.getValueAt(selectedRow, 24).toString(); // Designation
            String capacityName = model.getValueAt(selectedRow, 25).toString();   // Capacity
            String sectionName = model.getValueAt(selectedRow, 26).toString();    // Section

            // Convert married status to ID
            int marriedStatusId = convertMarriedStatusToId(marriedStatus);

            // Get IDs from database
            int designationId = getDesignationId(designationTitle);
            int capacityId = getCapacityId(capacityName);
            int sectionId = getSectionId(sectionName);

            // Open update frame - ADD RELIGION PARAMETER
            UpdateEmployeeFrame updateFrame = new UpdateEmployeeFrame(
                    this,
                    epfNo,
                    firstName,
                    initials,
                    nameWithInitial,
                    dob,
                    nic,
                    mobile,
                    father,
                    mother,
                    currentAddress,
                    permanentAddress,
                    electroate,
                    nominee,
                    marriedStatusId,
                    district,
                    race,
                    gender,
                    designationId,
                    capacityId,
                    sectionId,
                    religion // ✅ Add religion parameter
            );

            updateFrame.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading employee data for update: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private int getDesignationId(String titleName) {
        String query = "SELECT id FROM designation WHERE title = ?";
        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, titleName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getCapacityId(String capacityName) {
        String query = "SELECT id FROM capacity WHERE name = ?";
        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, capacityName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getSectionId(String sectionName) {
        String query = "SELECT id FROM section WHERE section_name = ?";
        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sectionName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Default value if not found
    }

    private int convertMarriedStatusToId(String status) {
        if (status == null || status.trim().isEmpty()) {
            return 1; // Default to Married
        }

        String normalized = status.trim().toLowerCase();
        switch (normalized) {
            case "married":
                return 1;
            case "unmarried":
            case "single":
                return 2;
            default:
                return 1; // Default to Married
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
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to resign.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String epfNo = model.getValueAt(selectedRow, 0).toString();

            // ✅ FIXED: Get joined_date from DATABASE instead of table
            String joinedDate = getJoinedDateFromDatabase(epfNo);

            // Debug
            System.out.println("=== Debug Info from Database ===");
            System.out.println("Selected Row: " + selectedRow);
            System.out.println("EPF No: " + epfNo);
            System.out.println("Joined Date from DB: " + joinedDate);
            System.out.println("============================");

            // Validate joined date
            if (joinedDate == null || joinedDate.trim().isEmpty() || joinedDate.equalsIgnoreCase("null")) {
                JOptionPane.showMessageDialog(this,
                        "Joined Date is not available for this employee!\n"
                        + "Please update the employee record with a valid joined date.",
                        "Missing Data",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int employeeId = getEmployeeIdByEpf(epfNo);

            if (employeeId == 0) {
                JOptionPane.showMessageDialog(this,
                        "Employee ID not found for EPF: " + epfNo,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);

            ResignTableDFrame resignFrame = new ResignTableDFrame(
                    (java.awt.Frame) parentWindow,
                    true,
                    employeeId,
                    epfNo,
                    joinedDate,
                    this
            );

            resignFrame.setLocationRelativeTo(this);
            resignFrame.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error opening Resign Dialog: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private String getJoinedDateFromDatabase(String epfNo) {
        String query = "SELECT joined_date FROM employee WHERE epf_no = ?";
        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, epfNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Get date and convert to yyyy-MM-dd format
                java.sql.Date sqlDate = rs.getDate("joined_date");
                if (sqlDate != null) {
                    return sqlDate.toString(); // Returns yyyy-MM-dd format
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to get employee_id from database
    private int getEmployeeIdByEpf(String epfNo) {
        String query = "SELECT id FROM employee WHERE epf_no = ?";
        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, epfNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if not found
    }

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
