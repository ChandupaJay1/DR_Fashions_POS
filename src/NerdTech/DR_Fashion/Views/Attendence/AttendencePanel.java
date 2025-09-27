/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Attendence;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author MG_Pathum
 */
public class AttendencePanel extends javax.swing.JPanel {

    /**
     * Creates new form AttendencePanel
     */
    public AttendencePanel() {
        initComponents();
        loadNamesFromDatabase();
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>((DefaultTableModel) AttendenceTable.getModel());
        AttendenceTable.setRowSorter(rowSorter);

        searchTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
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

            private void filterTable() {
                String searchText = searchTextField.getText().trim();
                if (searchText.length() == 0) {
                    rowSorter.setRowFilter(null); // show all
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)^" + searchText, 0)); // search by first column (Name)
                }
            }
        });

    }

    private int getEmployeeIdByName(String fname) {
        String query = "SELECT id FROM employee WHERE fname = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, fname);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private boolean confirmOverwriteIfNeeded(String currentStatus, String newStatus) {
        if (currentStatus != null && !currentStatus.isEmpty() && !currentStatus.equalsIgnoreCase(newStatus)) {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "This employee is already marked as '" + currentStatus + "'. Do you want to overwrite it with '" + newStatus + "'?",
                    "Confirm Status Change",
                    javax.swing.JOptionPane.YES_NO_OPTION);

            return confirm == javax.swing.JOptionPane.YES_OPTION;
        }
        return true; // No need to confirm if status is empty or same
    }

    private void loadNamesFromDatabase() {
        DefaultTableModel model = (DefaultTableModel) AttendenceTable.getModel();
        model.setRowCount(0); // clear table rows

        java.util.Date today = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(today.getTime());

        String query = """
        SELECT 
            e.fname, 
            a.status, 
            a.attendance_date, 
            a.come_in, 
            a.come_off
        FROM 
            employee e
        LEFT JOIN 
            attendence a 
        ON 
            e.id = a.employee_id AND a.attendance_date = ?
        ORDER BY 
            e.fname
    """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDate(1, sqlDate);
            ResultSet rs = ps.executeQuery();

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            while (rs.next()) {
                String fname = rs.getString("fname");
                java.sql.Date date = rs.getDate("attendance_date");

                if (date == null) {
                    // 🔁 No attendance record for today -> show only name
                    model.addRow(new Object[]{
                        fname,
                        "", // status
                        "", // date
                        "", // come in
                        "" // come off
                    });
                } else {
                    // 🔁 Existing attendance record
                    String status = rs.getString("status");
                    java.sql.Time comeIn = rs.getTime("come_in");
                    java.sql.Time comeOff = rs.getTime("come_off");

                    String formattedDate = date.toString();
                    String formattedComeIn = (comeIn != null && !comeIn.toString().equals("00:00:00")) ? timeFormat.format(comeIn) : "";
                    String formattedComeOff = (comeOff != null) ? timeFormat.format(comeOff) : "";

                    model.addRow(new Object[]{
                        fname,
                        (status != null) ? status : "",
                        formattedDate,
                        formattedComeIn,
                        formattedComeOff
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error loading attendance data: " + e.getMessage());
        }
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
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1237, 0));

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

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton4.setText("Leave");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton5.setText("Short Leave");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
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
                        .addGap(136, 136, 136)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(145, 145, 145)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveOrUpdateAttendance(int employeeId, String status, java.sql.Date date, java.sql.Time comeIn, java.sql.Time comeOff) {
        String selectSql = "SELECT id, status, come_in, come_off FROM attendence WHERE employee_id = ? AND attendance_date = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement selectPs = conn.prepareStatement(selectSql)) {

            selectPs.setInt(1, employeeId);
            selectPs.setDate(2, date);
            ResultSet rs = selectPs.executeQuery();

            if (rs.next()) {
                // Record exists -> merge and update
                int attendId = rs.getInt("id");

                String existingStatus = rs.getString("status");
                java.sql.Time existingComeIn = rs.getTime("come_in");
                java.sql.Time existingComeOff = rs.getTime("come_off");

                // Merge values: keep old if new is null
                String finalStatus = (status != null) ? status : existingStatus;
                java.sql.Time finalComeIn = (comeIn != null) ? comeIn : existingComeIn;
                java.sql.Time finalComeOff = (comeOff != null) ? comeOff : existingComeOff;

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
                // Insert new
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
            javax.swing.JOptionPane.showMessageDialog(this, "DB error: " + e.getMessage());
        }
    }


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = AttendenceTable.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a row.");
            return;
        }
        String name = (String) AttendenceTable.getValueAt(selectedRow, 0);
        int empId = getEmployeeIdByName(name);
        if (empId == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }

        String currentStatus = (String) AttendenceTable.getValueAt(selectedRow, 1);
        if ("Absent".equalsIgnoreCase(currentStatus)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Cannot mark as Present. This employee is already marked Absent.");
            return;
        }

        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());
        java.sql.Time sqlTime = new java.sql.Time(now.getTime()); // come_in

        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        String timeStr = fmt.format(now);

        AttendenceTable.setValueAt("Present", selectedRow, 1);
        AttendenceTable.setValueAt(sqlDate.toString(), selectedRow, 2);
        AttendenceTable.setValueAt(timeStr, selectedRow, 3);
        AttendenceTable.setValueAt("", selectedRow, 4);

        // use update/insert logic
        saveOrUpdateAttendance(empId, "Present", sqlDate, sqlTime, null);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = AttendenceTable.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a row.");
            return;
        }

        String name = (String) AttendenceTable.getValueAt(selectedRow, 0);
        int empId = getEmployeeIdByName(name);
        if (empId == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }

        String currentStatus = (String) AttendenceTable.getValueAt(selectedRow, 1);
        if (!confirmOverwriteIfNeeded(currentStatus, "Absent")) {
            return;
        }

        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());

        // ✅ UI Table Update: Show "Absent" in all relevant columns
        AttendenceTable.setValueAt("Absent", selectedRow, 1); // Status
        AttendenceTable.setValueAt(sqlDate.toString(), selectedRow, 2); // Date
        AttendenceTable.setValueAt("Absent", selectedRow, 3); // Come In
        AttendenceTable.setValueAt("Absent", selectedRow, 4); // Come Off

        // ✅ Save nulls to DB, not "Absent" string (DB can't take string in TIME fields)
        saveOrUpdateAttendance(empId, "Absent", sqlDate, null, null);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int selectedRow = AttendenceTable.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a row.");
            return;
        }
        String name = (String) AttendenceTable.getValueAt(selectedRow, 0);
        int empId = getEmployeeIdByName(name);
        if (empId == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }

        String currentStatus = (String) AttendenceTable.getValueAt(selectedRow, 1);
        if (!confirmOverwriteIfNeeded(currentStatus, "Leave")) {
            return;
        }

        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());
        java.sql.Time offTime = new java.sql.Time(now.getTime());

        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        String timeStr = fmt.format(now);

        // Keep status, date, come_in as already there if present
        // Update come_off
        AttendenceTable.setValueAt(timeStr, selectedRow, 4);

        saveOrUpdateAttendance(empId, null, sqlDate, null, offTime);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int selectedRow = AttendenceTable.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a row.");
            return;
        }

        String name = (String) AttendenceTable.getValueAt(selectedRow, 0);
        int empId = getEmployeeIdByName(name);
        if (empId == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }

        String currentStatus = (String) AttendenceTable.getValueAt(selectedRow, 1);
        if (!confirmOverwriteIfNeeded(currentStatus, "Short Leave")) {
            return;
        }

        java.util.Date now = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(now.getTime());
        java.sql.Time offTime = new java.sql.Time(now.getTime());

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String timeStr = formatter.format(now);

        // ----------------------------
        // 🛠️ NEW: Keep previous 'Come In' time
        String existingComeInStr = (String) AttendenceTable.getValueAt(selectedRow, 3);
        java.sql.Time comeInTime = null;
        try {
            if (existingComeInStr != null && !existingComeInStr.isEmpty()) {
                java.util.Date parsedTime = formatter.parse(existingComeInStr);
                comeInTime = new java.sql.Time(parsedTime.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ----------------------------
        // Update table UI
        AttendenceTable.setValueAt("Short Leave", selectedRow, 1);
        AttendenceTable.setValueAt(sqlDate.toString(), selectedRow, 2);
        // Don't clear come in
        // AttendenceTable.setValueAt("", selectedRow, 3);
        AttendenceTable.setValueAt(existingComeInStr, selectedRow, 3);
        AttendenceTable.setValueAt(timeStr, selectedRow, 4);

        // Save/update to DB
        saveOrUpdateAttendance(empId, "Short Leave", sqlDate, comeInTime, offTime);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AttendenceTable;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables
}
