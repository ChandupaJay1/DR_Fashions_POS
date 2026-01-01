/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Backup;

import NerdTech.DR_Fashion.Views.LoadingPanel;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection;
import com.formdev.flatlaf.FlatDarkLaf;

/**
 *
 * @author MG_Pathum
 */
public class BackupPanel extends javax.swing.JPanel {

    private LoadingPanel loadingPanel;
    private boolean isInitialized = false;

    /**
     * Creates new form BackupPanel
     */
    public BackupPanel() {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1237, 686));
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
                publish("Initializing backup system");
                Thread.sleep(300);

                try (Connection conn = getConnection()) {
                    publish("Verifying database connection");
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
                    showError("Failed to load backup panel: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void showActualContent() {
        removeAll();
        initComponents();
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

    private void backupEmployeeData(String absolutePath) {
        try (
                java.sql.Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(
                "SELECT e.id, e.epf_no, e.name_with_initial, e.fname, e.initials, e.surname, "
                + "e.gender, e.dob, e.nic, e.mobile, e.status, e.father, e.mother, "
                + "e.service_end_date, e.date_to_service_end, e.electroate, "
                + "e.permanate_address, e.current_address, e.nominee, e.married_status, "
                + "e.district, e.race, e.joined_date, e.recruited_date, e.religion, "
                + "e.confirmation_date, e.as_today, "
                + "d.title as designation, s.section_name, c.name as capacity "
                + "FROM employee e "
                + "LEFT JOIN designation d ON e.designation_id = d.id "
                + "LEFT JOIN section s ON e.section_id = s.id "
                + "LEFT JOIN capacity c ON e.capacity_id = c.id"
        )) {

            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Employees");

            // Create Header Row
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] columns = {
                "ID", "EPF No", "Name with Initial", "First Name", "Initials", "Surname",
                "Gender", "DOB", "NIC", "Mobile", "Status", "Father", "Mother",
                "Service End Date", "Date to Service End", "Electroate",
                "Permanent Address", "Current Address", "Nominee", "Married Status",
                "District", "Race", "Joined Date", "Recruited Date", "Religion",
                "Confirmation Date", "As Today", "Designation", "Section", "Capacity"
            };

            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            // Fill Data
            int rowIndex = 1;
            while (rs.next()) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rs.getInt("id"));
                row.createCell(1).setCellValue(rs.getInt("epf_no"));
                row.createCell(2).setCellValue(rs.getString("name_with_initial"));
                row.createCell(3).setCellValue(rs.getString("fname"));
                row.createCell(4).setCellValue(rs.getString("initials"));
                row.createCell(5).setCellValue(rs.getString("surname"));
                row.createCell(6).setCellValue(rs.getString("gender"));
                row.createCell(7).setCellValue(rs.getString("dob"));
                row.createCell(8).setCellValue(rs.getString("nic"));
                row.createCell(9).setCellValue(rs.getString("mobile"));
                row.createCell(10).setCellValue(rs.getString("status"));
                row.createCell(11).setCellValue(rs.getString("father"));
                row.createCell(12).setCellValue(rs.getString("mother"));
                row.createCell(13).setCellValue(rs.getString("service_end_date"));
                row.createCell(14).setCellValue(rs.getString("date_to_service_end"));
                row.createCell(15).setCellValue(rs.getString("electroate"));
                row.createCell(16).setCellValue(rs.getString("permanate_address"));
                row.createCell(17).setCellValue(rs.getString("current_address"));
                row.createCell(18).setCellValue(rs.getString("nominee"));
                row.createCell(19).setCellValue(rs.getString("married_status"));
                row.createCell(20).setCellValue(rs.getString("district"));
                row.createCell(21).setCellValue(rs.getString("race"));
                row.createCell(22).setCellValue(rs.getString("joined_date"));
                row.createCell(23).setCellValue(rs.getString("recruited_date"));
                row.createCell(24).setCellValue(rs.getString("religion"));
                row.createCell(25).setCellValue(rs.getString("confirmation_date"));
                row.createCell(26).setCellValue(rs.getString("as_today"));
                row.createCell(27).setCellValue(rs.getString("designation"));
                row.createCell(28).setCellValue(rs.getString("section_name"));
                row.createCell(29).setCellValue(rs.getString("capacity"));
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
                    "Employee data exported successfully to:\n" + absolutePath);
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error exporting to Excel: " + e.getMessage(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backupAccesoriesData(String absolutePath) {
        try (
                java.sql.Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(
                "SELECT a.id, a.order_no, a.name, a.colour_name, a.size, a.stock_qty, "
                + "a.uom, a.received_date, a.issued_date, a.total_issued, a.available_qty, "
                + "a.status, a.unit_price, t.type_name "
                + "FROM accesories a "
                + "LEFT JOIN type t ON a.type_id = t.type_id"
        )) {

            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Accessories");

            // Create Header Row
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] columns = {
                "ID", "Order No", "Name", "Colour Name", "Size", "Stock Quantity",
                "UOM", "Received Date", "Issued Date", "Total Issued",
                "Available Quantity", "Status", "Unit Price", "Type"
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
                row.createCell(2).setCellValue(rs.getString("name"));
                row.createCell(3).setCellValue(rs.getString("colour_name"));
                row.createCell(4).setCellValue(rs.getString("size"));
                row.createCell(5).setCellValue(rs.getString("stock_qty"));
                row.createCell(6).setCellValue(rs.getString("uom"));
                row.createCell(7).setCellValue(rs.getString("received_date"));
                row.createCell(8).setCellValue(rs.getString("issued_date"));
                row.createCell(9).setCellValue(rs.getString("total_issued"));
                row.createCell(10).setCellValue(rs.getString("available_qty"));
                row.createCell(11).setCellValue(rs.getString("status"));
                row.createCell(12).setCellValue(rs.getString("unit_price"));
                row.createCell(13).setCellValue(rs.getString("type_name"));
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
                "SELECT id, colour, status, stock_qty, material, received_date, "
                + "issued_date, total_issued, available_qty, unit_price "
                + "FROM stock"
        )) {

            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Stock");

            // Create Header Row
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] columns = {
                "ID", "Colour", "Status", "Stock Quantity", "Material",
                "Received Date", "Issued Date", "Total Issued",
                "Available Quantity", "Unit Price"
            };

            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            // Fill Data
            int rowIndex = 1;
            while (rs.next()) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(rs.getInt("id"));
                row.createCell(1).setCellValue(rs.getString("colour"));
                row.createCell(2).setCellValue(rs.getString("status"));
                row.createCell(3).setCellValue(rs.getString("stock_qty"));
                row.createCell(4).setCellValue(rs.getString("material"));
                row.createCell(5).setCellValue(rs.getString("received_date"));
                row.createCell(6).setCellValue(rs.getString("issued_date"));
                row.createCell(7).setCellValue(rs.getString("total_issued"));
                row.createCell(8).setCellValue(rs.getString("available_qty"));
                row.createCell(9).setCellValue(rs.getString("unit_price"));
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
                    "SELECT e.id, e.epf_no, e.name_with_initial, e.fname, e.initials, e.surname, "
                    + "e.gender, e.dob, e.nic, e.mobile, e.status, "
                    + "d.title as designation, s.section_name, c.name as capacity "
                    + "FROM employee e "
                    + "LEFT JOIN designation d ON e.designation_id = d.id "
                    + "LEFT JOIN section s ON e.section_id = s.id "
                    + "LEFT JOIN capacity c ON e.capacity_id = c.id")) {

                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Employees");
                org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
                String[] columns = {
                    "ID", "EPF No", "Name with Initial", "First Name", "Initials", "Surname",
                    "Gender", "DOB", "NIC", "Mobile", "Status", "Designation", "Section", "Capacity"
                };

                for (int i = 0; i < columns.length; i++) {
                    header.createCell(i).setCellValue(columns[i]);
                }

                int rowIndex = 1;
                while (rs.next()) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(rs.getInt("id"));
                    row.createCell(1).setCellValue(rs.getInt("epf_no"));
                    row.createCell(2).setCellValue(rs.getString("name_with_initial"));
                    row.createCell(3).setCellValue(rs.getString("fname"));
                    row.createCell(4).setCellValue(rs.getString("initials"));
                    row.createCell(5).setCellValue(rs.getString("surname"));
                    row.createCell(6).setCellValue(rs.getString("gender"));
                    row.createCell(7).setCellValue(rs.getString("dob"));
                    row.createCell(8).setCellValue(rs.getString("nic"));
                    row.createCell(9).setCellValue(rs.getString("mobile"));
                    row.createCell(10).setCellValue(rs.getString("status"));
                    row.createCell(11).setCellValue(rs.getString("designation"));
                    row.createCell(12).setCellValue(rs.getString("section_name"));
                    row.createCell(13).setCellValue(rs.getString("capacity"));
                }

                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            // ==================== SHEET 2: STOCK ====================
            try (java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(
                    "SELECT id, colour, status, stock_qty, material, received_date, "
                    + "issued_date, total_issued, available_qty, unit_price FROM stock")) {

                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Stock");
                org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
                String[] columns = {
                    "ID", "Colour", "Status", "Stock Quantity", "Material",
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
                    row.createCell(1).setCellValue(rs.getString("colour"));
                    row.createCell(2).setCellValue(rs.getString("status"));
                    row.createCell(3).setCellValue(rs.getString("stock_qty"));
                    row.createCell(4).setCellValue(rs.getString("material"));
                    row.createCell(5).setCellValue(rs.getString("received_date"));
                    row.createCell(6).setCellValue(rs.getString("issued_date"));
                    row.createCell(7).setCellValue(rs.getString("total_issued"));
                    row.createCell(8).setCellValue(rs.getString("available_qty"));
                    row.createCell(9).setCellValue(rs.getString("unit_price"));
                }

                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            // ==================== SHEET 3: ACCESSORIES ====================
            try (java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery(
                    "SELECT a.id, a.order_no, a.name, a.colour_name, a.size, a.stock_qty, "
                    + "a.uom, a.received_date, a.issued_date, a.total_issued, a.available_qty, "
                    + "a.status, a.unit_price, t.type_name "
                    + "FROM accesories a LEFT JOIN type t ON a.type_id = t.type_id")) {

                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Accessories");
                org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
                String[] columns = {
                    "ID", "Order No", "Name", "Colour Name", "Size", "Stock Quantity",
                    "UOM", "Received Date", "Issued Date", "Total Issued",
                    "Available Quantity", "Status", "Unit Price", "Type"
                };

                for (int i = 0; i < columns.length; i++) {
                    header.createCell(i).setCellValue(columns[i]);
                }

                int rowIndex = 1;
                while (rs.next()) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(rs.getInt("id"));
                    row.createCell(1).setCellValue(rs.getString("order_no"));
                    row.createCell(2).setCellValue(rs.getString("name"));
                    row.createCell(3).setCellValue(rs.getString("colour_name"));
                    row.createCell(4).setCellValue(rs.getString("size"));
                    row.createCell(5).setCellValue(rs.getString("stock_qty"));
                    row.createCell(6).setCellValue(rs.getString("uom"));
                    row.createCell(7).setCellValue(rs.getString("received_date"));
                    row.createCell(8).setCellValue(rs.getString("issued_date"));
                    row.createCell(9).setCellValue(rs.getString("total_issued"));
                    row.createCell(10).setCellValue(rs.getString("available_qty"));
                    row.createCell(11).setCellValue(rs.getString("status"));
                    row.createCell(12).setCellValue(rs.getString("unit_price"));
                    row.createCell(13).setCellValue(rs.getString("type_name"));
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

    private void backupDatabaseToSQL(String sqlFilePath) {
        try (java.sql.Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); java.io.FileWriter writer = new java.io.FileWriter(sqlFilePath); java.io.BufferedWriter bw = new java.io.BufferedWriter(writer)) {

            java.sql.DatabaseMetaData metaData = conn.getMetaData();
            String databaseName = conn.getCatalog();

            // SQL Header
            bw.write("-- SQL Backup File\n");
            bw.write("-- Database: " + databaseName + "\n");
            bw.write("-- Backup Date: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + "\n");
            bw.write("-- =============================================\n\n");
            bw.write("SET FOREIGN_KEY_CHECKS = 0;\n\n");

            // දැන් database schema එකේ තියෙන හරි tables ඔක්කොම
            String[] tables = {
                "capacity",
                "designation",
                "section",
                "employee",
                "type",
                "stock",
                "accesories",
                "user",
                "attendence",
                "resignation",
                "salary",
                "salary_details",
                "bank_details",
                "registration_buyer",
                "bstock",
                "baccesories",
                "breturn_qty_stock",
                "breturn_qty_accessories",
                "monthly_payment",
                "per_day_salary"
            };

            for (String tableName : tables) {
                try {
                    bw.write("\n-- =============================================\n");
                    bw.write("-- Table: " + tableName + "\n");
                    bw.write("-- =============================================\n\n");

                    // DROP TABLE IF EXISTS
                    bw.write("DROP TABLE IF EXISTS `" + tableName + "`;\n\n");

                    // CREATE TABLE statement
                    try (java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE `" + tableName + "`")) {
                        if (rs.next()) {
                            bw.write(rs.getString(2) + ";\n\n");
                        }
                    }

                    // INSERT statements
                    try (java.sql.Statement stmt = conn.createStatement(); java.sql.ResultSet rs = stmt.executeQuery("SELECT * FROM `" + tableName + "`")) {

                        java.sql.ResultSetMetaData rsMetaData = rs.getMetaData();
                        int columnCount = rsMetaData.getColumnCount();

                        while (rs.next()) {
                            StringBuilder insertQuery = new StringBuilder();
                            insertQuery.append("INSERT INTO `").append(tableName).append("` VALUES (");

                            for (int i = 1; i <= columnCount; i++) {
                                Object value = rs.getObject(i);

                                if (value == null) {
                                    insertQuery.append("NULL");
                                } else if (value instanceof String
                                        || value instanceof java.sql.Date
                                        || value instanceof java.sql.Timestamp
                                        || value instanceof java.sql.Time) {
                                    String strValue = value.toString().replace("'", "''");
                                    insertQuery.append("'").append(strValue).append("'");
                                } else {
                                    insertQuery.append(value);
                                }

                                if (i < columnCount) {
                                    insertQuery.append(", ");
                                }
                            }

                            insertQuery.append(");\n");
                            bw.write(insertQuery.toString());
                        }
                    }

                    bw.write("\n");

                } catch (java.sql.SQLException e) {
                    // Table එකක් නැත්නම් skip කරලා continue වෙනවා
                    bw.write("-- Table '" + tableName + "' not found or error occurred\n\n");
                    System.err.println("Warning: Could not backup table '" + tableName + "': " + e.getMessage());
                }
            }

            bw.write("\nSET FOREIGN_KEY_CHECKS = 1;\n");
            bw.write("-- Backup completed successfully\n");

            javax.swing.JOptionPane.showMessageDialog(this,
                    "SQL Database backup completed successfully!\n\n"
                    + "File saved at:\n" + sqlFilePath,
                    "Backup Complete",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

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
        jPanel1 = new javax.swing.JPanel();
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

        setPreferredSize(new java.awt.Dimension(0, 763));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Backup System Data");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 477, 38));

        EmployeeBtn.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        EmployeeBtn.setText("Backup Data");
        EmployeeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmployeeBtnActionPerformed(evt);
            }
        });
        jPanel1.add(EmployeeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 199, 41));

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Backup Employee Data");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 70, -1, -1));
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 50, 477, 38));

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Backup Stock Data");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 70, -1, -1));

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton2.setText("Backup Data");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 100, 199, 41));
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, 477, 38));

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Backup Accesories Data");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 230, -1, -1));

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton3.setText("Backup Data");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, 199, 41));
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 220, 477, 38));

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel9.setText("Backup All Data");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 230, -1, -1));

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jButton4.setText("Backup Data");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 260, 199, 41));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(760, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        fileChooser.setDialogTitle("Save SQL Database Backup");
        fileChooser.setSelectedFile(new java.io.File("database_backup.sql"));

        // SQL file filter එකක් add කරන්න
        javax.swing.filechooser.FileNameExtensionFilter filter
                = new javax.swing.filechooser.FileNameExtensionFilter("SQL Files (*.sql)", "sql");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();

            if (!path.toLowerCase().endsWith(".sql")) {
                path += ".sql";
            }

            backupDatabaseToSQL(path);
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
