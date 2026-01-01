/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Shipment.Cutting;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.Shipment.ShipmentPanel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author MG_Pathum
 */
public class OrderNoPanel extends javax.swing.JPanel {

    /**
     * Creates new form OrderNoPanel
     */
    public OrderNoPanel() {
        initComponents();
        loadOrderData();
        setupTableDoubleClick();
    }

    /**
     * Setup double click listener to navigate to CuttingPanel with filtered
     * data
     */
    private void setupTableDoubleClick() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double click
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow != -1) {
                        // Get order number from the selected row
                        String orderNo = jTable1.getValueAt(selectedRow, 0) != null
                                ? jTable1.getValueAt(selectedRow, 0).toString()
                                : "";

                        if (!orderNo.isEmpty()) {
                            // Navigate to CuttingPanel with this order number
                            navigateToCuttingPanelWithFilter(orderNo);
                        } else {
                            JOptionPane.showMessageDialog(OrderNoPanel.this,
                                    "Order number is empty for this record",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    /**
     * Navigate to CuttingPanel and load specific order's cutting details
     */
    private void navigateToCuttingPanelWithFilter(String orderNo) {
        try {
            // Get parent container
            java.awt.Container parent = this.getParent();

            if (parent != null) {
                parent.removeAll();

                // Create CuttingPanel with filtered order number
                CuttingPanel cuttingPanel = new CuttingPanel(orderNo);

                parent.setLayout(new java.awt.BorderLayout());
                parent.add(cuttingPanel, java.awt.BorderLayout.CENTER);

                parent.revalidate();
                parent.repaint();

                System.out.println("Navigated to CuttingPanel with Order No: " + orderNo);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error navigating to Cutting Panel: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load order data from shipment table
     */
    private void loadOrderData() {
        try {
            // Get database connection using DatabaseConnection class
            Connection conn = DatabaseConnection.getConnection();

            String query = "SELECT order_no, fabric_inhouse_date FROM shipment ORDER BY id DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); // Clear existing rows

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            while (rs.next()) {
                String orderNo = rs.getString("order_no");
                java.sql.Date fabricDate = rs.getDate("fabric_inhouse_date");
                String formattedDate = fabricDate != null ? dateFormat.format(fabricDate) : "";

                model.addRow(new Object[]{orderNo, formattedDate});
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refresh table data
     */
    public void refreshTable() {
        loadOrderData();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Order No & Fabric Inhouse Date");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Order No", "Fabric Inhouse Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
        }

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Back");
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
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 693, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 588, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(589, 589, 589)
                .addComponent(jButton1)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        backToShipmentPanel();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void backToShipmentPanel() {
        try {
            // Get parent container
            java.awt.Container parent = this.getParent();

            if (parent != null) {
                parent.removeAll();

                // Create new ShipmentPanel instance without filter
                ShipmentPanel shipmentPanel = new ShipmentPanel();

                parent.setLayout(new java.awt.BorderLayout());
                parent.add(shipmentPanel, java.awt.BorderLayout.CENTER);

                parent.revalidate();
                parent.repaint();

                System.out.println("Navigated back to ShipmentPanel successfully!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error navigating back: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
