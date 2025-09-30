/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Backup;

/**
 *
 * @author MG_Pathum
 */
public class BackupPanel extends javax.swing.JPanel {

    /**
     * Creates new form BackupPanel
     */
    public BackupPanel() {
        initComponents();
    }

    private void backupEmployeeData(String absolutePath) {
        try (
                java.sql.Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(
                "SELECT e.fname, e.lname, e.email, e.dob, e.nic, e.mobile, r.position "
                + "FROM employee e JOIN role r ON e.role_id = r.id"
        )) {
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Employees");

            // Create Header Row
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] columns = {"First Name", "Last Name", "Email", "DOB", "NIC", "Mobile", "Role"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            // Fill Data
            int rowIndex = 1;
            while (rs.next()) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rs.getString("fname"));
                row.createCell(1).setCellValue(rs.getString("lname"));
                row.createCell(2).setCellValue(rs.getString("email"));
                row.createCell(3).setCellValue(rs.getString("dob"));
                row.createCell(4).setCellValue(rs.getString("nic"));
                row.createCell(5).setCellValue(rs.getString("mobile"));
                row.createCell(6).setCellValue(rs.getString("position"));
            }

            // Autosize columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(absolutePath)) {
                workbook.write(fileOut);
            }

            workbook.close();

            javax.swing.JOptionPane.showMessageDialog(this, "Employee data exported successfully to:\n" + absolutePath);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error exporting to Excel: " + e.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backupAccesoriesData(String absolutePath) {
        try (
                java.sql.Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(
                "SELECT id, order_no, colour_name, size, stock_qty, uom, received_date, "
                + "issued_date, total_issued, available_qty, unit_price FROM accesories"
        )) {
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Accessories");

            // Create Header Row
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] columns = {
                "ID",
                "Order No",
                "Colour Name",
                "Size",
                "Stock Quantity",
                "UOM",
                "Received Date",
                "Issued Date",
                "Total Issued",
                "Available Quantity",
                "Unit Price"
            };

            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            // Fill Data
            int rowIndex = 1;
            while (rs.next()) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rs.getInt("id"));
                row.createCell(1).setCellValue(rs.getString("order_no"));
                row.createCell(2).setCellValue(rs.getString("colour_name"));
                row.createCell(3).setCellValue(rs.getString("size"));
                row.createCell(4).setCellValue(rs.getInt("stock_qty"));
                row.createCell(5).setCellValue(rs.getString("uom"));
                row.createCell(6).setCellValue(rs.getString("received_date"));
                row.createCell(7).setCellValue(rs.getString("issued_date"));
                row.createCell(8).setCellValue(rs.getInt("total_issued"));
                row.createCell(9).setCellValue(rs.getInt("available_qty"));
                row.createCell(10).setCellValue(rs.getDouble("unit_price"));
            }

            // Autosize columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(absolutePath)) {
                workbook.write(fileOut);
            }

            workbook.close();

            javax.swing.JOptionPane.showMessageDialog(this,
                    "Accessories data exported successfully to:\n" + absolutePath);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error exporting to Excel: " + e.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backupStockData(String absolutePath) {
        try (
                java.sql.Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(
                "SELECT id, name, stock_qty, material, received_date, issued_date, "
                + "total_issued, available_qty, unit_price FROM stock"
        )) {
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Stock");

            // Create Header Row
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] columns = {
                "ID",
                "Name",
                "Stock Quantity",
                "Material",
                "Received Date",
                "Issued Date",
                "Total Issued",
                "Available Quantity",
                "Unit Price"
            };

            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            // Fill Data
            int rowIndex = 1;
            while (rs.next()) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rs.getInt("id"));
                row.createCell(1).setCellValue(rs.getString("name"));
                row.createCell(2).setCellValue(rs.getInt("stock_qty"));
                row.createCell(3).setCellValue(rs.getString("material"));
                row.createCell(4).setCellValue(rs.getString("received_date"));
                row.createCell(5).setCellValue(rs.getString("issued_date"));
                row.createCell(6).setCellValue(rs.getInt("total_issued"));
                row.createCell(7).setCellValue(rs.getInt("available_qty"));
                row.createCell(8).setCellValue(rs.getDouble("unit_price"));
            }

            // Autosize columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(absolutePath)) {
                workbook.write(fileOut);
            }

            workbook.close();

            javax.swing.JOptionPane.showMessageDialog(this,
                    "Stock data exported successfully to:\n" + absolutePath);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error exporting to Excel: " + e.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backupAllData(String absolutePath) {
        try (java.sql.Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection()) {
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();

            // ==================== SHEET 1: EMPLOYEES ====================
            try (java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(
                    "SELECT e.fname, e.lname, e.email, e.dob, e.nic, e.mobile, r.position "
                    + "FROM employee e JOIN role r ON e.role_id = r.id")) {

                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Employees");
                org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
                String[] columns = {"First Name", "Last Name", "Email", "DOB", "NIC", "Mobile", "Role"};

                for (int i = 0; i < columns.length; i++) {
                    header.createCell(i).setCellValue(columns[i]);
                }

                int rowIndex = 1;
                while (rs.next()) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(rs.getString("fname"));
                    row.createCell(1).setCellValue(rs.getString("lname"));
                    row.createCell(2).setCellValue(rs.getString("email"));
                    row.createCell(3).setCellValue(rs.getString("dob"));
                    row.createCell(4).setCellValue(rs.getString("nic"));
                    row.createCell(5).setCellValue(rs.getString("mobile"));
                    row.createCell(6).setCellValue(rs.getString("position"));
                }

                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            // ==================== SHEET 2: STOCK ====================
            try (java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(
                    "SELECT id, name, stock_qty, material, received_date, issued_date, "
                    + "total_issued, available_qty, unit_price FROM stock")) {

                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Stock");
                org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
                String[] columns = {
                    "ID", "Name", "Stock Quantity", "Material",
                    "Received Date", "Issued Date", "Total Issued",
                    "Available Quantity", "Unit Price"
                };

                for (int i = 0; i < columns.length; i++) {
                    header.createCell(i).setCellValue(columns[i]);
                }

                int rowIndex = 1;
                while (rs.next()) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(rs.getInt("id"));
                    row.createCell(1).setCellValue(rs.getString("name"));
                    row.createCell(2).setCellValue(rs.getInt("stock_qty"));
                    row.createCell(3).setCellValue(rs.getString("material"));
                    row.createCell(4).setCellValue(rs.getString("received_date"));
                    row.createCell(5).setCellValue(rs.getString("issued_date"));
                    row.createCell(6).setCellValue(rs.getInt("total_issued"));
                    row.createCell(7).setCellValue(rs.getInt("available_qty"));
                    row.createCell(8).setCellValue(rs.getDouble("unit_price"));
                }

                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            // ==================== SHEET 3: ACCESSORIES ====================
            try (java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(
                    "SELECT id, order_no, colour_name, size, stock_qty, uom, received_date, "
                    + "issued_date, total_issued, available_qty, unit_price FROM accesories")) {

                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Accessories");
                org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
                String[] columns = {
                    "ID", "Order No", "Colour Name", "Size",
                    "Stock Quantity", "UOM", "Received Date", "Issued Date",
                    "Total Issued", "Available Quantity", "Unit Price"
                };

                for (int i = 0; i < columns.length; i++) {
                    header.createCell(i).setCellValue(columns[i]);
                }

                int rowIndex = 1;
                while (rs.next()) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(rs.getInt("id"));
                    row.createCell(1).setCellValue(rs.getString("order_no"));
                    row.createCell(2).setCellValue(rs.getString("colour_name"));
                    row.createCell(3).setCellValue(rs.getString("size"));
                    row.createCell(4).setCellValue(rs.getInt("stock_qty"));
                    row.createCell(5).setCellValue(rs.getString("uom"));
                    row.createCell(6).setCellValue(rs.getString("received_date"));
                    row.createCell(7).setCellValue(rs.getString("issued_date"));
                    row.createCell(8).setCellValue(rs.getInt("total_issued"));
                    row.createCell(9).setCellValue(rs.getInt("available_qty"));
                    row.createCell(10).setCellValue(rs.getDouble("unit_price"));
                }

                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            // ==================== WRITE TO FILE ====================
            try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(absolutePath)) {
                workbook.write(fileOut);
            }

            workbook.close();

            javax.swing.JOptionPane.showMessageDialog(this,
                    "All data exported successfully to:\n" + absolutePath
                    + "\n\nSheets created:\n• Employees\n• Stock\n• Accessories",
                    "Backup Complete",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error exporting to Excel: " + e.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private int countRows(java.sql.Connection conn) {
        try (java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM employee")) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void backupDatabaseAsSQL(String absolutePath) {
        try {
            String dbName = "your_database_name";  // මෙතන database name එක දාන්න
            String dbUser = "root";  // database username
            String dbPassword = "";  // database password

            String command = String.format(
                    "mysqldump -u %s -p%s %s -r %s",
                    dbUser, dbPassword, dbName, absolutePath
            );

            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Database backup created successfully at:\n" + absolutePath,
                        "SQL Backup Complete",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Database backup failed!",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error creating SQL backup: " + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
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
        jLabel2 = new javax.swing.JLabel();
        EmployeeBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Backup System Data");

        EmployeeBtn.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        EmployeeBtn.setText("Backup Data");
        EmployeeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmployeeBtnActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Backup Employee Data");

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Backup Stock Data");

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton2.setText("Backup Data");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Backup Accesories Data");

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton3.setText("Backup Data");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel9.setText("Backup All Data");

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton4.setText("Backup Data");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(118, 118, 118)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(EmployeeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(141, 141, 141))
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(129, 129, 129)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(43, 43, 43)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(122, 122, 122)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(19, 19, 19)))
                        .addGap(133, 133, 133)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EmployeeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(62, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void EmployeeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmployeeBtnActionPerformed
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Save Employee Data as Excel File");

        // Default file name suggestion
        fileChooser.setSelectedFile(new java.io.File("employee_backup.xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();

            // Ensure file extension
            if (!path.toLowerCase().endsWith(".xlsx")) {
                path += ".xlsx";
            }

            // Call only once — no second chooser
            backupEmployeeData(path);
        }
    }//GEN-LAST:event_EmployeeBtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Save Stock Data as Excel File");

        // Default file name suggestion
        fileChooser.setSelectedFile(new java.io.File("stock_backup.xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();

            // Ensure file extension
            if (!path.toLowerCase().endsWith(".xlsx")) {
                path += ".xlsx";
            }

            // Call only once — no second chooser
            backupStockData(path);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Save Accesories Data as Excel File");

        // Default file name suggestion
        fileChooser.setSelectedFile(new java.io.File("accesories_backup.xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();

            // Ensure file extension
            if (!path.toLowerCase().endsWith(".xlsx")) {
                path += ".xlsx";
            }

            // Call only once — no second chooser
            backupAccesoriesData(path);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Save All Data as Excel File");
        fileChooser.setSelectedFile(new java.io.File("all_data_backup.xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();

            if (!path.toLowerCase().endsWith(".xlsx")) {
                path += ".xlsx";
            }

            backupAllData(path);
        }
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton EmployeeBtn;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
