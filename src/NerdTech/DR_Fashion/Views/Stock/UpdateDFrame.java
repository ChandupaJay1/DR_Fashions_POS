/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

public class UpdateDFrame extends javax.swing.JDialog {

    private StockPanel parentPanel;
    private String oldName;

    public UpdateDFrame(java.awt.Frame parent, boolean modal, StockPanel parentPanel,
            String name, String stockQty, String material, String receivedDate,
            String issuedDate, String totalIssued, String availableQty, String unitPrice) {
        super(parent, modal);
        initComponents();

        this.parentPanel = parentPanel;
        this.oldName = name;

        Name.setText(name);
        SQty.setText(availableQty); // ✅ Stock Qty field loads Available Qty from table
        Materials.setText(material);
        TIssued.setText("");         // ✅ Total issued empty
        AQty.setText("");            // ✅ Available Qty empty
        UPrice.setText(unitPrice);

        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            RDate.setDate(sdf.parse(receivedDate));
            IDate.setDate(sdf.parse(issuedDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SQty.setEnabled(false);
        RDate.setEnabled(false);
        AQty.setEnabled(false);

        TIssued.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateAvailable() {
                try {
                    int stockQty = Integer.parseInt(SQty.getText().trim());
                    int totalIssued = Integer.parseInt(TIssued.getText().trim());
                    int available = stockQty - totalIssued;
                    if (available < 0) {
                        available = 0;
                    }
                    AQty.setText(String.valueOf(available));
                } catch (NumberFormatException e) {
                    AQty.setText("");
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateAvailable();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateAvailable();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateAvailable();
            }
        });
    }

    private void updateStockInDatabase() {
        String name = Name.getText().trim();
        String material = Materials.getText().trim();
        String totalIssued = TIssued.getText().trim();
        String availableQty = AQty.getText().trim();
        String unitPrice = UPrice.getText().trim();
        java.util.Date issuedDate = IDate.getDate();

        if (name.isEmpty() || material.isEmpty() || totalIssued.isEmpty() || unitPrice.isEmpty() || issuedDate == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields properly!");
            return;
        }

        // 🟢 FIX: Use correct column names in your DB
        String sql = "UPDATE stock SET name=?, material=?, issued_date=?, total_issued=?, available_qty=?, unit_price=?, last_modified=NOW() WHERE name=?";

        try (Connection conn = NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, material);
            ps.setTimestamp(3, new java.sql.Timestamp(issuedDate.getTime()));
            ps.setString(4, totalIssued);
            ps.setString(5, availableQty);
            ps.setString(6, unitPrice);
            ps.setString(7, oldName);

            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "✅ Stock updated successfully!");
                if (parentPanel != null) {
                    parentPanel.loadStockData(); // refresh table
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ No matching record found to update!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error while updating: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        SQty = new javax.swing.JTextField();
        Materials = new javax.swing.JTextField();
        Name = new javax.swing.JTextField();
        AQty = new javax.swing.JTextField();
        UPrice = new javax.swing.JTextField();
        TIssued = new javax.swing.JTextField();
        RDate = new com.toedter.calendar.JDateChooser();
        IDate = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update Stock");
        setPreferredSize(new java.awt.Dimension(1233, 505));

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Name");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Materials");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Stock Qty");

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Recieved Date");

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Issued Date");

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Total  Issued");

        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel8.setText("Available Qty");

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel9.setText("Unit Price");

        SQty.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        Materials.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        Materials.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MaterialsActionPerformed(evt);
            }
        });

        Name.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        AQty.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        UPrice.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        TIssued.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        TIssued.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TIssuedActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Update Stock");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Update Stock");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(861, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jButton1)
                            .addGap(40, 40, 40)
                            .addComponent(jButton2)
                            .addGap(3, 3, 3))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel8))
                                    .addGap(155, 155, 155)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(AQty, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel6))
                                    .addGap(177, 177, 177)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(IDate, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                        .addComponent(Materials))))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addComponent(jLabel7)
                                .addComponent(jLabel5))
                            .addGap(86, 86, 86)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(TIssued, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(UPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(SQty, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(RDate, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(392, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(117, 117, 117)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(Materials, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel6)
                                .addComponent(IDate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(23, 23, 23)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(AQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(SQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(RDate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(TIssued, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(20, 20, 20)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(UPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGap(102, 102, 102)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton2))
                    .addContainerGap(23, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void MaterialsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MaterialsActionPerformed
        this.dispose();
    }//GEN-LAST:event_MaterialsActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        updateStockInDatabase();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void TIssuedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TIssuedActionPerformed

    }//GEN-LAST:event_TIssuedActionPerformed

    /**
     * @param args the command line arguments
     */
    // public static void main(String args[]) {
//     java.awt.EventQueue.invokeLater(new Runnable() {
//         public void run() {
//             UpdateDFrame dialog = new UpdateDFrame(new javax.swing.JFrame(), true);
//             dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                 @Override
//                 public void windowClosing(java.awt.event.WindowEvent e) {
//                     System.exit(0);
//                 }
//             });
//             dialog.setVisible(true);
//         }
//     });
// }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AQty;
    private com.toedter.calendar.JDateChooser IDate;
    private javax.swing.JTextField Materials;
    private javax.swing.JTextField Name;
    private com.toedter.calendar.JDateChooser RDate;
    private javax.swing.JTextField SQty;
    private javax.swing.JTextField TIssued;
    private javax.swing.JTextField UPrice;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
