/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.ViewBuyerAccesories;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ViewBuyerAccesoriesPanel extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private String selectedBuyerName;

    /**
     * Default constructor (for backward compatibility)
     */
    public ViewBuyerAccesoriesPanel() {
        initComponents();
        initializeTable();
        loadBuyerAccessoriesData();
        setupSearchListener();
    }

    /**
     * New constructor with buyer name
     */
    public ViewBuyerAccesoriesPanel(String buyerName) {
        initComponents();
        this.selectedBuyerName = buyerName;
        initializeTable();
        loadBuyerAccessoriesDataByBuyer();
        setupSearchListener();

        // Update title to show which buyer's accessories are being viewed
        jLabel1.setText("View Buyer Accesories - " + buyerName);
    }

    // ✅ Setup Real-time Search Listener
    private void setupSearchListener() {
        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performAdvancedSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performAdvancedSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performAdvancedSearch();
            }
        });
    }

    // ✅ Advanced Search Method - searches across multiple columns
    private void performAdvancedSearch() {
        String searchText = jTextField1.getText().trim().toLowerCase();

        tableModel.setRowCount(0); // Clear table

        if (searchText.isEmpty()) {
            // Show all data if search is empty
            if (selectedBuyerName != null) {
                loadBuyerAccessoriesDataByBuyer();
            } else {
                loadBuyerAccessoriesData();
            }
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();

            // ✅ Build query based on whether we're filtering by buyer or not
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT ba.id, r.buyer_name, ba.order_no, c.colour_name, ba.size, ")
                    .append("ba.stock_qty, ba.uom, ba.received_date, ba.issued_date, ")
                    .append("ba.total_issued, ba.available_qty, ba.unit_price, t.type_name ")
                    .append("FROM buyer_accesories ba ")
                    .append("JOIN registration r ON ba.buyer_id = r.id ")
                    .append("JOIN colour c ON ba.colour_id = c.id ")
                    .append("JOIN type t ON ba.type_id = t.id ")
                    .append("WHERE ba.status = 'active' ");

            // Add buyer filter if specific buyer is selected
            if (selectedBuyerName != null && !selectedBuyerName.isEmpty()) {
                queryBuilder.append("AND r.buyer_name = ? ");
            }

            // Add search conditions for multiple columns
            queryBuilder.append("AND (")
                    .append("LOWER(r.buyer_name) LIKE ? OR ")
                    .append("LOWER(ba.order_no) LIKE ? OR ")
                    .append("LOWER(c.colour_name) LIKE ? OR ")
                    .append("LOWER(ba.size) LIKE ? OR ")
                    .append("CAST(ba.stock_qty AS CHAR) LIKE ? OR ")
                    .append("LOWER(ba.uom) LIKE ? OR ")
                    .append("CAST(ba.received_date AS CHAR) LIKE ? OR ")
                    .append("CAST(ba.issued_date AS CHAR) LIKE ? OR ")
                    .append("CAST(ba.available_qty AS CHAR) LIKE ? OR ")
                    .append("LOWER(t.type_name) LIKE ?)");

            PreparedStatement ps = conn.prepareStatement(queryBuilder.toString());

            String searchPattern = "%" + searchText + "%";
            int paramIndex = 1;

            // Set buyer name parameter if filtering by buyer
            if (selectedBuyerName != null && !selectedBuyerName.isEmpty()) {
                ps.setString(paramIndex++, selectedBuyerName);
            }

            // Set search pattern for all searchable columns (10 columns)
            for (int i = 0; i < 10; i++) {
                ps.setString(paramIndex++, searchPattern);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("id"),
                    rs.getString("buyer_name"),
                    rs.getString("order_no"),
                    rs.getString("colour_name"),
                    rs.getString("size"),
                    rs.getInt("stock_qty"),
                    rs.getString("uom"),
                    rs.getDate("received_date"),
                    rs.getDate("issued_date"),
                    rs.getInt("total_issued"),
                    rs.getInt("available_qty"),
                    String.format("Rs. %.2f", rs.getDouble("unit_price")),
                    rs.getString("type_name")
                };
                tableModel.addRow(row);
            }

            // Show message if no results found
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No results found for: " + searchText,
                        "Search Results",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            closeResources(rs);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error searching accessories: " + e.getMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void initializeTable() {
        String[] columnNames = {
            "ID", // Hidden column for database ID
            "Buyer Name",
            "Order No",
            "Colour",
            "Size",
            "Stock Qty",
            "UOM",
            "Received Date",
            "Issued Date",
            "Total Issued",
            "Available Qty",
            "Unit Price",
            "Type"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jTable1.setModel(tableModel);

        // Hide the ID column (but keep the data)
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setWidth(0);
    }

    /**
     * Load ALL buyer accessories data (no filter)
     */
    private void loadBuyerAccessoriesData() {
        try {
            tableModel.setRowCount(0);
            ResultSet rs = DatabaseConnection.getBuyerAccessoriesData();

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("id"), // Hidden ID column
                    rs.getString("buyer_name"), // You need to JOIN with registration table
                    rs.getString("order_no"),
                    rs.getString("colour_name"),
                    rs.getString("size"),
                    rs.getInt("stock_qty"),
                    rs.getString("uom"),
                    rs.getDate("received_date"),
                    rs.getDate("issued_date"),
                    rs.getInt("total_issued"),
                    rs.getInt("available_qty"),
                    String.format("Rs. %.2f", rs.getDouble("unit_price")),
                    rs.getString("type_name") // You need to JOIN with type table
                };
                tableModel.addRow(row);
            }

            closeResources(rs);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading buyer accessories data: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Load buyer accessories data FILTERED by buyer name
     */
    private void loadBuyerAccessoriesDataByBuyer() {
        try {
            tableModel.setRowCount(0);

            if (selectedBuyerName != null && !selectedBuyerName.isEmpty()) {
                ResultSet rs = DatabaseConnection.getBuyerAccessoriesDataByBuyerName(selectedBuyerName);

                while (rs.next()) {
                    Object[] row = new Object[]{
                        rs.getInt("id"),
                        rs.getString("buyer_name"),
                        rs.getString("order_no"),
                        rs.getString("colour_name"),
                        rs.getString("size"),
                        rs.getInt("stock_qty"),
                        rs.getString("uom"),
                        rs.getDate("received_date"),
                        rs.getDate("issued_date"),
                        rs.getInt("total_issued"),
                        rs.getInt("available_qty"),
                        String.format("Rs. %.2f", rs.getDouble("unit_price")),
                        rs.getString("type_name")
                    };
                    tableModel.addRow(row);
                }

                closeResources(rs);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading buyer accessories data for " + selectedBuyerName + ": " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Helper method to close database resources
     */
    private void closeResources(ResultSet rs) {
        try {
            if (rs != null) {
                java.sql.Statement stmt = rs.getStatement();
                if (stmt != null) {
                    java.sql.Connection conn = stmt.getConnection();
                    if (conn != null) {
                        conn.close();
                    }
                    stmt.close();
                }
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh table data
     */
    public void refreshTable() {
        if (selectedBuyerName != null) {
            loadBuyerAccessoriesDataByBuyer();
        } else {
            loadBuyerAccessoriesData();
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
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("View Buyer Accesories");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Buyer Name", "Order No", "Colour", "Size", "Stock Qty", "UOM", "Received Date", "Issued Date", "Total Issued", "Available Qty", "Unit Price", "Type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false, false, false, true, false, false
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
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
            jTable1.getColumnModel().getColumn(7).setResizable(false);
            jTable1.getColumnModel().getColumn(8).setResizable(false);
            jTable1.getColumnModel().getColumn(9).setResizable(false);
            jTable1.getColumnModel().getColumn(10).setResizable(false);
            jTable1.getColumnModel().getColumn(11).setResizable(false);
        }

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Add Buyer Accesory");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Update Buyer Accesory");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Delete Buyer Accesory");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton4.setText("Got to Buyer Stock ");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Search");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(132, 132, 132)
                        .addComponent(jButton2)
                        .addGap(141, 141, 141)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
                        .addComponent(jButton4))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(27, 27, 27)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(28, 28, 28))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Get selected row from table
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an accessory record to update!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Get data from correct column indices
            int accessoryId = (int) tableModel.getValueAt(selectedRow, 0);           // Column 0: ID (hidden)
            String buyerName = (String) tableModel.getValueAt(selectedRow, 1);       // Column 1: Buyer Name
            String orderNo = (String) tableModel.getValueAt(selectedRow, 2);         // Column 2: Order No
            String colour = (String) tableModel.getValueAt(selectedRow, 3);          // Column 3: Colour
            String size = (String) tableModel.getValueAt(selectedRow, 4);            // Column 4: Size
            int stockQty = (int) tableModel.getValueAt(selectedRow, 5);              // Column 5: Stock Qty
            String uom = (String) tableModel.getValueAt(selectedRow, 6);             // Column 6: UOM
            java.sql.Date receivedDate = (java.sql.Date) tableModel.getValueAt(selectedRow, 7);  // Column 7: Received Date
            java.sql.Date issuedDate = (java.sql.Date) tableModel.getValueAt(selectedRow, 8);    // Column 8: Issued Date
            int totalIssued = (int) tableModel.getValueAt(selectedRow, 9);           // Column 9: Total Issued
            int availableQty = (int) tableModel.getValueAt(selectedRow, 10);         // Column 10: Available Qty

            // Parse unit price (remove "Rs." and parse)
            String unitPriceStr = (String) tableModel.getValueAt(selectedRow, 11);   // Column 11: Unit Price
            unitPriceStr = unitPriceStr.replace("Rs.", "").replace(",", "").trim();
            double unitPrice = Double.parseDouble(unitPriceStr);

            String typeName = (String) tableModel.getValueAt(selectedRow, 12);       // Column 12: Type

            // Open update dialog with all values
            UpdateAccesoriesDFrame updateDialog = new UpdateAccesoriesDFrame(
                    this,
                    accessoryId,
                    buyerName,
                    orderNo,
                    colour,
                    size,
                    stockQty,
                    uom,
                    receivedDate,
                    issuedDate,
                    totalIssued,
                    availableQty,
                    unitPrice,
                    typeName
            );

            updateDialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error opening update dialog: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Navigate back to Buyer Stock Panel
        try {
            // Get the parent window (could be JFrame or JDialog)
            java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(this);

            if (parentWindow != null) {
                // Create ViewBuyerStock instance
                NerdTech.DR_Fashion.Views.ViewBuyerStock.ViewBuyerStock stockPanel;

                // If this panel has a selected buyer, pass it to stock panel
                if (selectedBuyerName != null && !selectedBuyerName.isEmpty()) {
                    stockPanel = new NerdTech.DR_Fashion.Views.ViewBuyerStock.ViewBuyerStock(selectedBuyerName);
                } else {
                    stockPanel = new NerdTech.DR_Fashion.Views.ViewBuyerStock.ViewBuyerStock();
                }

                // Get LoadingPanel from parent window
                javax.swing.JPanel loadingPanel = null;

                // Try to get LoadingPanel field from the parent window
                try {
                    java.lang.reflect.Field field = parentWindow.getClass().getDeclaredField("LoadingPanel");
                    field.setAccessible(true);
                    loadingPanel = (javax.swing.JPanel) field.get(parentWindow);
                } catch (NoSuchFieldException e) {
                    // If LoadingPanel field not found, try to find it in the content pane
                    if (parentWindow instanceof javax.swing.JFrame) {
                        javax.swing.JFrame frame = (javax.swing.JFrame) parentWindow;
                        java.awt.Container contentPane = frame.getContentPane();
                        if (contentPane instanceof javax.swing.JPanel) {
                            loadingPanel = (javax.swing.JPanel) contentPane;
                        }
                    } else if (parentWindow instanceof javax.swing.JDialog) {
                        javax.swing.JDialog dialog = (javax.swing.JDialog) parentWindow;
                        java.awt.Container contentPane = dialog.getContentPane();
                        if (contentPane instanceof javax.swing.JPanel) {
                            loadingPanel = (javax.swing.JPanel) contentPane;
                        }
                    }
                }

                if (loadingPanel != null) {
                    // Clear LoadingPanel and add new panel
                    loadingPanel.removeAll();
                    loadingPanel.setLayout(new java.awt.BorderLayout());
                    loadingPanel.add(stockPanel, java.awt.BorderLayout.CENTER);

                    // Refresh the panel
                    loadingPanel.revalidate();
                    loadingPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Could not find LoadingPanel to load stock view!",
                            "Navigation Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading Buyer Stock panel: " + e.getMessage(),
                    "Navigation Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // When "Add Buyer Accessory" button is clicked
        AddBuyerAccesoriesDFrame addDialog = new AddBuyerAccesoriesDFrame(this);

        // If this panel was opened with a specific buyer name, load it
        if (selectedBuyerName != null && !selectedBuyerName.isEmpty()) {
            addDialog.setBuyerName(selectedBuyerName);
        }

        addDialog.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Get selected row from table
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an accessory record to delete!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Get accessory details for confirmation
            int accessoryId = (int) tableModel.getValueAt(selectedRow, 0);           // Column 0: ID (hidden)
            String buyerName = (String) tableModel.getValueAt(selectedRow, 1);       // Column 1: Buyer Name
            String orderNo = (String) tableModel.getValueAt(selectedRow, 2);         // Column 2: Order No
            String colour = (String) tableModel.getValueAt(selectedRow, 3);          // Column 3: Colour
            String size = (String) tableModel.getValueAt(selectedRow, 4);            // Column 4: Size

            // Confirm deletion with user
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to DEACTIVATE this accessory record?\n\n"
                    + "Buyer: " + buyerName + "\n"
                    + "Order No: " + orderNo + "\n"
                    + "Colour: " + colour + "\n"
                    + "Size: " + size + "\n\n"
                    + "This record will be hidden from view but kept in the database.",
                    "Confirm Deactivation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // Deactivate in database (soft delete)
                deactivateAccessory(accessoryId, buyerName, orderNo, colour);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error deactivating accessory record: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void deactivateAccessory(int accessoryId, String buyerName, String orderNo, String colour) {
        try {
            boolean success = DatabaseConnection.deactivateAccessory(accessoryId);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Accessory record deactivated successfully!\n\n"
                        + "Buyer: " + buyerName + "\n"
                        + "Order No: " + orderNo + "\n"
                        + "Colour: " + colour,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshTable(); // Refresh the table after deactivation
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to deactivate accessory record!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error deactivating accessory record: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
