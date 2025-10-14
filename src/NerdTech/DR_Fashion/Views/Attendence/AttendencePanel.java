/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Attendence;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.LoadingPanel;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author MG_Pathum
 */
public class AttendencePanel extends javax.swing.JPanel {

    private LoadingPanel loadingPanel;
    private boolean isInitialized = false;
    private TableRowSorter<DefaultTableModel> sorter;

    public AttendencePanel() {
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1257, 686));
        setBackground(new Color(50, 50, 50));  // FlatMacDarkLaf Panel background

        showLoading("Connecting to Database");
        loadContentInBackground();
    }

    private void showLoading(String message) {
        removeAll();
        loadingPanel = new LoadingPanel(message);
        add(loadingPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void loadContentInBackground() {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {

            @Override
            protected Void doInBackground() throws Exception {
                publish("Loading attendance data");
                Thread.sleep(300);

                try (Connection conn = DatabaseConnection.getConnection()) {
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
        loadEmployeesToTable();
        setupSearch();
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

    private void setupSearch() {
        DefaultTableModel model = (DefaultTableModel) AttendenceTable.getModel();
        sorter = new TableRowSorter<>(model);
        AttendenceTable.setRowSorter(sorter);

        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                String text = searchTextField.getText().trim();
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
    }

    private void loadEmployeesToTable() {
        DefaultTableModel model = (DefaultTableModel) AttendenceTable.getModel();
        model.setRowCount(0);

        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        String query = "SELECT e.id, CONCAT(e.fname, ' ', e.lname) AS full_name, "
                + "a.status, a.attendance_date, a.come_in, a.come_off "
                + "FROM employee e "
                + "LEFT JOIN attendence a ON e.id = a.employee_id AND a.attendance_date = ? "
                + "WHERE e.status = 'active' "
                + "ORDER BY e.fname";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDate(1, today);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("full_name");
                String status = rs.getString("status");
                java.sql.Date date = rs.getDate("attendance_date");
                java.sql.Time comeIn = rs.getTime("come_in");
                java.sql.Time comeOff = rs.getTime("come_off");

                String statusDisplay = (status != null) ? status : "";
                String dateDisplay = (date != null) ? date.toString() : "";
                String comeInDisplay = "";
                String comeOffDisplay = "";

                if ("Absent".equalsIgnoreCase(status)) {
                    comeInDisplay = "Absent";
                    comeOffDisplay = "Absent";
                } else {
                    comeInDisplay = (comeIn != null) ? timeFormat.format(comeIn) : "";
                    comeOffDisplay = (comeOff != null) ? timeFormat.format(comeOff) : "";
                }

                model.addRow(new Object[]{name, statusDisplay, dateDisplay, comeInDisplay, comeOffDisplay});
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No employees found in database.");
            }

        } catch (Exception ex) {
            Logger.getLogger(AttendencePanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error loading employees: " + ex.getMessage());
        }
    }

    private int getEmployeeIdByName(String name) {
        String query = "SELECT id FROM employee WHERE CONCAT(fname, ' ', lname) = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception ex) {
            Logger.getLogger(AttendencePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        searchTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AttendenceTable = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1257, 0));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Attendence Sheet");

        searchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextFieldActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Search");

        AttendenceTable.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        AttendenceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Name", "Status", "Date", "come In", "Come Off"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(AttendenceTable);
        if (AttendenceTable.getColumnModel().getColumnCount() > 0) {
            AttendenceTable.getColumnModel().getColumn(0).setResizable(false);
            AttendenceTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            AttendenceTable.getColumnModel().getColumn(1).setResizable(false);
            AttendenceTable.getColumnModel().getColumn(2).setResizable(false);
            AttendenceTable.getColumnModel().getColumn(3).setResizable(false);
            AttendenceTable.getColumnModel().getColumn(4).setResizable(false);
        }

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton2.setText("Present");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton3.setText("Absent");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton6.setText("Short Leave");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton1.setText("Leave");
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(144, 144, 144)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(140, 140, 140)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveOrUpdateAttendance(int employeeId, String status, java.sql.Date date,
            java.sql.Time comeIn, java.sql.Time comeOff, boolean forceNullTimes) {

        String selectSql = "SELECT id, status, come_in, come_off FROM attendence WHERE employee_id = ? AND attendance_date = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement selectPs = conn.prepareStatement(selectSql)) {

            selectPs.setInt(1, employeeId);
            selectPs.setDate(2, date);
            ResultSet rs = selectPs.executeQuery();

            if (rs.next()) {
                int attendId = rs.getInt("id");
                String existingStatus = rs.getString("status");
                java.sql.Time existingComeIn = rs.getTime("come_in");
                java.sql.Time existingComeOff = rs.getTime("come_off");

                String finalStatus = (status != null) ? status : existingStatus;
                java.sql.Time finalComeIn;
                java.sql.Time finalComeOff;

                if (forceNullTimes) {
                    finalComeIn = null;
                    finalComeOff = null;
                } else {
                    finalComeIn = (comeIn != null) ? comeIn : existingComeIn;
                    finalComeOff = (comeOff != null) ? comeOff : existingComeOff;
                }

                String updateSql = "UPDATE attendence SET status = ?, come_in = ?, come_off = ? WHERE id = ?";
                try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                    updatePs.setString(1, finalStatus);

                    if (finalComeIn != null) {
                        updatePs.setTime(2, finalComeIn);
                    } else {
                        updatePs.setNull(2, java.sql.Types.TIME);
                    }

                    if (finalComeOff != null) {
                        updatePs.setTime(3, finalComeOff);
                    } else {
                        updatePs.setNull(3, java.sql.Types.TIME);
                    }

                    updatePs.setInt(4, attendId);
                    updatePs.executeUpdate();
                }

            } else {
                String insertSql = "INSERT INTO attendence (employee_id, status, attendance_date, come_in, come_off) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    insertPs.setInt(1, employeeId);
                    insertPs.setString(2, status);
                    insertPs.setDate(3, date);

                    if (comeIn != null) {
                        insertPs.setTime(4, comeIn);
                    } else {
                        insertPs.setNull(4, java.sql.Types.TIME);
                    }

                    if (comeOff != null) {
                        insertPs.setTime(5, comeOff);
                    } else {
                        insertPs.setNull(5, java.sql.Types.TIME);
                    }

                    insertPs.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "DB error: " + e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(AttendencePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = AttendenceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row.");
            return;
        }

        int modelRow = AttendenceTable.convertRowIndexToModel(selectedRow);
        String name = (String) AttendenceTable.getModel().getValueAt(modelRow, 0);
        int empId = getEmployeeIdByName(name);
        if (empId == -1) {
            JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }

        String currentStatus = (String) AttendenceTable.getModel().getValueAt(modelRow, 1);
        if ("Absent".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this,
                    "Cannot mark as Present. This employee is already marked Absent.");
            return;
        }

        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());
        java.sql.Time sqlTime = new java.sql.Time(now.getTime());

        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        String timeStr = fmt.format(now);

        DefaultTableModel model = (DefaultTableModel) AttendenceTable.getModel();
        model.setValueAt("Present", modelRow, 1);
        model.setValueAt(sqlDate.toString(), modelRow, 2);
        model.setValueAt(timeStr, modelRow, 3);
        model.setValueAt("", modelRow, 4);

        saveOrUpdateAttendance(empId, "Present", sqlDate, sqlTime, null, false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = AttendenceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row.");
            return;
        }

        int modelRow = AttendenceTable.convertRowIndexToModel(selectedRow);
        String name = (String) AttendenceTable.getModel().getValueAt(modelRow, 0);
        int empId = getEmployeeIdByName(name);

        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());

        DefaultTableModel model = (DefaultTableModel) AttendenceTable.getModel();
        model.setValueAt("Absent", modelRow, 1);
        model.setValueAt(sqlDate.toString(), modelRow, 2);
        model.setValueAt("Absent", modelRow, 3);
        model.setValueAt("Absent", modelRow, 4);

        saveOrUpdateAttendance(empId, "Absent", sqlDate, null, null, true);
    }

    private void jButton4ActionPerformed() {
        int selectedRow = AttendenceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row.");
            return;
        }

        int modelRow = AttendenceTable.convertRowIndexToModel(selectedRow);
        String name = (String) AttendenceTable.getModel().getValueAt(modelRow, 0);
        int empId = getEmployeeIdByName(name);
        if (empId == -1) {
            JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }

        String currentStatus = (String) AttendenceTable.getModel().getValueAt(modelRow, 1);
        if (!"Present".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this,
                    "Cannot mark Leave. Employee must be marked as Present first.");
            return;
        }

        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());
        java.sql.Time offTime = new java.sql.Time(now.getTime());

        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        String timeStr = fmt.format(now);

        String existingComeInStr = (String) AttendenceTable.getModel().getValueAt(modelRow, 3);
        java.sql.Time comeInTime = null;
        try {
            if (existingComeInStr != null && !existingComeInStr.isEmpty()
                    && !existingComeInStr.equals("Absent")) {
                java.util.Date parsedTime = fmt.parse(existingComeInStr);
                comeInTime = new java.sql.Time(parsedTime.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DefaultTableModel model = (DefaultTableModel) AttendenceTable.getModel();
        model.setValueAt("Present", modelRow, 1);
        model.setValueAt(timeStr, modelRow, 4);

        saveOrUpdateAttendance(empId, "Present", sqlDate, comeInTime, offTime, false);
    }//GEN-LAST:event_jButton3ActionPerformed


    private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTextFieldActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int selectedRow = AttendenceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row.");
            return;
        }

        int modelRow = AttendenceTable.convertRowIndexToModel(selectedRow);
        String name = (String) AttendenceTable.getModel().getValueAt(modelRow, 0);
        int empId = getEmployeeIdByName(name);
        if (empId == -1) {
            JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }

        String currentStatus = (String) AttendenceTable.getModel().getValueAt(modelRow, 1);
        if (!"Present".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this,
                    "Cannot mark Short Leave. Employee must be marked as Present first.");
            return;
        }

        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());
        java.sql.Time offTime = new java.sql.Time(now.getTime());

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String timeStr = formatter.format(now);

        String existingComeInStr = (String) AttendenceTable.getModel().getValueAt(modelRow, 3);
        java.sql.Time comeInTime = null;
        try {
            if (existingComeInStr != null && !existingComeInStr.isEmpty()
                    && !existingComeInStr.equals("Absent")) {
                java.util.Date parsedTime = formatter.parse(existingComeInStr);
                comeInTime = new java.sql.Time(parsedTime.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DefaultTableModel model = (DefaultTableModel) AttendenceTable.getModel();
        model.setValueAt("Short Leave", modelRow, 1);
        model.setValueAt(sqlDate.toString(), modelRow, 2);
        model.setValueAt(existingComeInStr, modelRow, 3);
        model.setValueAt(timeStr, modelRow, 4);

        saveOrUpdateAttendance(empId, "Short Leave", sqlDate, comeInTime, offTime, false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedRow = AttendenceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row.");
            return;
        }

        int modelRow = AttendenceTable.convertRowIndexToModel(selectedRow);
        String name = (String) AttendenceTable.getModel().getValueAt(modelRow, 0);
        int empId = getEmployeeIdByName(name);
        if (empId == -1) {
            JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }

        String currentStatus = (String) AttendenceTable.getModel().getValueAt(modelRow, 1);
        if (!"Present".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this,
                    "Cannot mark Leave. Employee must be marked as Present first.");
            return;
        }

        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());
        java.sql.Time offTime = new java.sql.Time(now.getTime());

        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        String timeStr = fmt.format(now);

        String existingComeInStr = (String) AttendenceTable.getModel().getValueAt(modelRow, 3);
        java.sql.Time comeInTime = null;
        try {
            if (existingComeInStr != null && !existingComeInStr.isEmpty()
                    && !existingComeInStr.equals("Absent")) {
                java.util.Date parsedTime = fmt.parse(existingComeInStr);
                comeInTime = new java.sql.Time(parsedTime.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DefaultTableModel model = (DefaultTableModel) AttendenceTable.getModel();
        model.setValueAt("Present", modelRow, 1);
        model.setValueAt(timeStr, modelRow, 4);

        saveOrUpdateAttendance(empId, "Present", sqlDate, comeInTime, offTime, false);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AttendenceTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables
}
