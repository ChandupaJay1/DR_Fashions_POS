/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.ViewBuyerStock;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;  // ✅ මේක add කරන්න
import java.awt.event.MouseEvent;

public class ViewBuyerStock extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private String selectedBuyerName;

    public ViewBuyerStock() {
        initComponents();
        initializeTable();
        loadBuyerStockData();
    }

    public ViewBuyerStock(String buyerName) {
        initComponents();
        this.selectedBuyerName = buyerName;
        initializeTable();
        loadBuyerStockDataByBuyer();
        jLabel1.setText("View Buyer Stock - " + buyerName);
    }

    private void initializeTable() {
        String[] columnNames = {
            "ID", // Column 0: Hidden
            "Buyer Name", // Column 1
            "Colour", // Column 2
            "Stock Qty", // Column 3
            "Brand Name", // Column 4
            "Size", // Column 5
            "Material", // Column 6
            "Received Date", // Column 7
            "Issued Date", // Column 8
            "Total Issued", // Column 9
            "Available Qty", // Column 10
            "Unit Price", // Column 11
            "Work Order No" // Column 12 ⭐ NEW
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jTable1.setModel(tableModel);

        // Hide ID column
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setWidth(0);
    }

    private void loadBuyerStockData() {
        try {
            tableModel.setRowCount(0);
            ResultSet rs = DatabaseConnection.getBuyerStockData();

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("id"), // Column 0
                    rs.getString("buyer_name"), // Column 1
                    rs.getString("colour"), // Column 2
                    rs.getInt("stock_qty"), // Column 3
                    rs.getString("brand_name"), // Column 4
                    rs.getDouble("size"), // Column 5
                    rs.getString("material"), // Column 6
                    rs.getDate("received_date"), // Column 7
                    rs.getDate("issued_date"), // Column 8
                    rs.getInt("total_issued"), // Column 9
                    rs.getInt("available_qty"), // Column 10
                    String.format("Rs. %.2f", rs.getDouble("unit_price")), // Column 11
                    rs.getString("work_order_no") // Column 12 ⭐ NEW
                };
                tableModel.addRow(row);
            }

            closeResources(rs);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading buyer stock data: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadBuyerStockDataByBuyer() {
        try {
            tableModel.setRowCount(0);

            if (selectedBuyerName != null && !selectedBuyerName.isEmpty()) {
                ResultSet rs = DatabaseConnection.getBuyerStockDataByBuyerName(selectedBuyerName);

                while (rs.next()) {
                    Object[] row = new Object[]{
                        rs.getInt("id"), // Column 0
                        rs.getString("buyer_name"), // Column 1
                        rs.getString("colour"), // Column 2
                        rs.getInt("stock_qty"), // Column 3
                        rs.getString("brand_name"), // Column 4
                        rs.getDouble("size"), // Column 5
                        rs.getString("material"), // Column 6
                        rs.getDate("received_date"), // Column 7
                        rs.getDate("issued_date"), // Column 8
                        rs.getInt("total_issued"), // Column 9
                        rs.getInt("available_qty"), // Column 10
                        String.format("Rs. %.2f", rs.getDouble("unit_price")), // Column 11
                        rs.getString("work_order_no") // Column 12 ⭐ NEW
                    };
                    tableModel.addRow(row);
                }

                closeResources(rs);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading buyer stock data for " + selectedBuyerName + ": " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

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

    public void refreshTable() {
        if (selectedBuyerName != null) {
            loadBuyerStockDataByBuyer();
        } else {
            loadBuyerStockData();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Go to Buyer Accesories");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Buyer Name", "Colour", "Brand Name", "Work Order No :", "Size", "Stock Qty", "Material", "Recieved Date", "Issued Date", "Total Issued", "Available Qty", "Unit Price"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Update Buyer Stock");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton4.setText("Delete Buyer Stock");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("View Buyer Stock");

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Add Buyer Stock");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(157, 157, 157)
                        .addComponent(jButton4)
                        .addGap(249, 249, 249)
                        .addComponent(jButton3))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton2)
                    .addComponent(jButton4)
                    .addComponent(jButton1))
                .addGap(18, 18, 18))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        AddBuyerStockDFrame addDialog = new AddBuyerStockDFrame(this);

        if (selectedBuyerName != null && !selectedBuyerName.isEmpty()) {
            addDialog.jTextField1.setText(selectedBuyerName);
        }

        addDialog.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a stock record to update!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // ✅ Updated column indices with work_order_no
            int stockId = (int) tableModel.getValueAt(selectedRow, 0);           // Column 0
            String buyerName = (String) tableModel.getValueAt(selectedRow, 1);   // Column 1
            String colour = (String) tableModel.getValueAt(selectedRow, 2);      // Column 2
            int stockQty = (int) tableModel.getValueAt(selectedRow, 3);          // Column 3
            String brandName = (String) tableModel.getValueAt(selectedRow, 4);   // Column 4
            double size = (double) tableModel.getValueAt(selectedRow, 5);        // Column 5
            String material = (String) tableModel.getValueAt(selectedRow, 6);    // Column 6
            java.sql.Date receivedDate = (java.sql.Date) tableModel.getValueAt(selectedRow, 7);  // Column 7
            java.sql.Date issuedDate = (java.sql.Date) tableModel.getValueAt(selectedRow, 8);    // Column 8
            int totalIssued = (int) tableModel.getValueAt(selectedRow, 9);       // Column 9
            int availableQty = (int) tableModel.getValueAt(selectedRow, 10);     // Column 10

            String unitPriceStr = (String) tableModel.getValueAt(selectedRow, 11); // Column 11
            unitPriceStr = unitPriceStr.replace("Rs.", "").replace(",", "").trim();
            double unitPrice = Double.parseDouble(unitPriceStr);

            String workOrderNo = (String) tableModel.getValueAt(selectedRow, 12); // Column 12 ⭐ NEW

            // ✅ Open update dialog with work_order_no
            UpdateBuyerStockDFrame updateDialog = new UpdateBuyerStockDFrame(
                    this,
                    stockId,
                    buyerName,
                    colour,
                    stockQty,
                    brandName,
                    size,
                    material,
                    receivedDate,
                    issuedDate,
                    totalIssued,
                    availableQty,
                    unitPrice,
                    workOrderNo // ⭐ NEW
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
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a stock record to delete!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int stockId = (int) tableModel.getValueAt(selectedRow, 0);
            String buyerName = (String) tableModel.getValueAt(selectedRow, 1);
            String colour = (String) tableModel.getValueAt(selectedRow, 2);
            String material = (String) tableModel.getValueAt(selectedRow, 6);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to DEACTIVATE this stock record?\n\n"
                    + "Buyer: " + buyerName + "\n"
                    + "Colour: " + colour + "\n"
                    + "Material: " + material + "\n\n"
                    + "This record will be hidden from view but kept in the database.",
                    "Confirm Deactivation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                deactivateStock(stockId, buyerName, colour, material);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error deactivating stock record: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void deactivateStock(int stockId, String buyerName, String colour, String material) {
        try {
            boolean success = DatabaseConnection.deactivateBuyerStock(stockId);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Stock record deactivated successfully!\n\n"
                        + "Buyer: " + buyerName + "\n"
                        + "Colour: " + colour + "\n"
                        + "Material: " + material,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to deactivate stock record!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error deactivating stock record: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            java.awt.Window parentWindow = javax.swing.SwingUtilities.getWindowAncestor(this);

            if (parentWindow != null) {
                NerdTech.DR_Fashion.Views.ViewBuyerAccesories.ViewBuyerAccesoriesPanel accessoriesPanel;

                if (selectedBuyerName != null && !selectedBuyerName.isEmpty()) {
                    accessoriesPanel = new NerdTech.DR_Fashion.Views.ViewBuyerAccesories.ViewBuyerAccesoriesPanel(selectedBuyerName);
                } else {
                    accessoriesPanel = new NerdTech.DR_Fashion.Views.ViewBuyerAccesories.ViewBuyerAccesoriesPanel();
                }

                javax.swing.JPanel loadingPanel = null;

                try {
                    java.lang.reflect.Field field = parentWindow.getClass().getDeclaredField("LoadingPanel");
                    field.setAccessible(true);
                    loadingPanel = (javax.swing.JPanel) field.get(parentWindow);
                } catch (NoSuchFieldException e) {
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
                    loadingPanel.removeAll();
                    loadingPanel.setLayout(new java.awt.BorderLayout());
                    loadingPanel.add(accessoriesPanel, java.awt.BorderLayout.CENTER);
                    loadingPanel.revalidate();
                    loadingPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Could not find LoadingPanel to load accessories view!",
                            "Navigation Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading Buyer Accessories panel: " + e.getMessage(),
                    "Navigation Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButton1;
    public javax.swing.JButton jButton2;
    public javax.swing.JButton jButton3;
    public javax.swing.JButton jButton4;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JSeparator jSeparator1;
    public javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
