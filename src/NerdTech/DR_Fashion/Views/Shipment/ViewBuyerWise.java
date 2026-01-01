/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Shipment;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MG_Pathum
 */
public class ViewBuyerWise extends javax.swing.JDialog {

    private String buyerName;

    /**
     * Creates new form ViewBuyerWise
     */
    public ViewBuyerWise(java.awt.Frame parent, boolean modal, String buyerName) {
        super(parent, modal);
        this.buyerName = buyerName;
        initComponents();
        jLabel1.setText("View Buyerwise - " + buyerName);
        loadBuyerWiseData();
    }

    private void loadBuyerWiseData() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        String query = """
        SELECT 
            order_no,
            fabric_inhouse_date,
            buyer_name,
            roll_cut_qty,
            yardage_fabric,
            yardage_pocketing,
            sub_garment,
            style,
            fabric_use,
            brand,
            cut_pcs,
            line_in_qty,
            month,
            shipment_qty,
            shipment_date,
            dr_invoice_no,
            next_order_balance,
            remark,
            status,
            payment_method
        FROM shipment
        WHERE buyer_name = ?
        ORDER BY id DESC
    """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, buyerName);
            ResultSet rs = ps.executeQuery();

            int rowCount = 0;

            while (rs.next()) {
                rowCount++;
                model.addRow(new Object[]{
                    rs.getString("order_no"),
                    rs.getString("fabric_inhouse_date"),
                    rs.getString("buyer_name"),
                    rs.getString("roll_cut_qty"),
                    rs.getString("yardage_fabric"),
                    rs.getString("yardage_pocketing"),
                    rs.getString("sub_garment"),
                    rs.getString("style"),
                    rs.getString("fabric_use"),
                    rs.getString("brand"),
                    rs.getString("cut_pcs"),
                    rs.getString("line_in_qty"),
                    rs.getString("month"),
                    rs.getString("shipment_qty"),
                    rs.getString("shipment_date"),
                    rs.getString("dr_invoice_no"),
                    rs.getString("next_order_balance"),
                    rs.getString("remark"),
                    rs.getString("status"),
                    rs.getString("payment_method")
                });
            }

            if (rowCount == 0) {
                JOptionPane.showMessageDialog(this,
                        "No shipment records found for buyer: " + buyerName,
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("Loaded " + rowCount + " records for buyer: " + buyerName);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Connection Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("View Buyerwise");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Order Number", "Fabric Inhouse Date", "Buyer Name", "Roll Cut Qty", "Yardage Fabric", "Yardage Pocketing", "Sub Garment", "Style", "Fabric Use", "Brand", "Cut Pcs", "Line In Qty", "Month", "Shipment Qty", "Shipment Date", "Dr Invoice No", "Next Order Balance", "Remark", "Status", "Payment Method"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1424, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
