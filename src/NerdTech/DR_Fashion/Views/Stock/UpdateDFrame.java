/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Stock;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

public class UpdateDFrame extends javax.swing.JDialog {

    private final StockPanel stockPanel;
    private final String oldColour;
    private final String oldMaterial;

    public UpdateDFrame(java.awt.Frame parent, boolean modal, StockPanel stockPanel,
            String colour, String stockQty, String previousStockQty, String material,
            String receivedDate, String receivedQty, String availableQty, String unitPrice,
            String workOrderNo) {
        super(parent, modal);
        this.stockPanel = stockPanel;
        this.oldColour = colour;
        this.oldMaterial = material;
        initComponents();

        // ✅ Set initial values
        jTextField1.setText(colour);        // Colour field
        SQty.setText(stockQty);             // Stock Qty
        Materials.setText(material);
        jTextField2.setText(previousStockQty); // Previous Stock Qty
        TIssued.setText(receivedQty);       // Received Qty
        AQty.setText(availableQty);         // Available Qty
        UPrice.setText(unitPrice);
        jTextField9.setText(workOrderNo != null ? workOrderNo : ""); // Work Order No

        // ✅ Set received date
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            RDate.setDate(sdf.parse(receivedDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Disable fields that should be read-only
        jTextField2.setEnabled(false); // Previous Stock Qty is auto-filled

        // ✅ Create a document listener to update Available Qty
        javax.swing.event.DocumentListener updateAvailableListener = new javax.swing.event.DocumentListener() {
            private void updateAvailable() {
                try {
                    String prevStockText = jTextField2.getText().trim();
                    String receivedQtyText = TIssued.getText().trim();
                    String stockQtyText = SQty.getText().trim();

                    int previousStock = prevStockText.isEmpty() ? 0 : Integer.parseInt(prevStockText);
                    int receivedQty = receivedQtyText.isEmpty() ? 0 : Integer.parseInt(receivedQtyText);
                    int stockQty = stockQtyText.isEmpty() ? 0 : Integer.parseInt(stockQtyText);

                    // ✅ Same logic as AddStockDFrame
                    int available;

                    if (stockQty == 0) {
                        // Stock Qty හිස් නම්: Available = Previous + Received
                        available = previousStock + receivedQty;
                    } else {
                        // Stock Qty තියෙනවා නම්: Available = Stock - (Previous + Received)
                        available = stockQty - (previousStock + receivedQty);
                    }

                    if (available < 0) {
                        available = 0;
                    }

                    AQty.setText(String.valueOf(available));

                } catch (NumberFormatException e) {
                    AQty.setText("0");
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
        };

        // ✅ Add listener to all three fields
        jTextField2.getDocument().addDocumentListener(updateAvailableListener);
        TIssued.getDocument().addDocumentListener(updateAvailableListener);
        SQty.getDocument().addDocumentListener(updateAvailableListener);
    }

    private void updateStockInDatabase() {
        String colour = jTextField1.getText().trim();
        String material = Materials.getText().trim();
        String stockQty = SQty.getText().trim();
        String availableQty = AQty.getText().trim();
        String receivedQty = TIssued.getText().trim();
        String unitPrice = UPrice.getText().trim();
        String previousStockQty = jTextField2.getText().trim();
        String workOrderNo = jTextField9.getText().trim();

        java.util.Date receivedDateUtil = RDate.getDate();

        if (colour.isEmpty() || material.isEmpty() || stockQty.isEmpty() || unitPrice.isEmpty() || receivedDateUtil == null) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE stock SET colour=?, stock_qty=?, previous_stock_qty=?, material=?, received_date=?, recieved_qty=?, available_qty=?, unit_price=?, work_order_no=? "
                    + "WHERE colour=? AND material=? AND status='active'";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, colour);
            ps.setString(2, stockQty);
            ps.setString(3, previousStockQty.isEmpty() ? "0" : previousStockQty);
            ps.setString(4, material);
            ps.setString(5, new java.text.SimpleDateFormat("yyyy-MM-dd").format(receivedDateUtil));
            ps.setString(6, receivedQty.isEmpty() ? "0" : receivedQty);
            ps.setString(7, availableQty.isEmpty() ? "0" : availableQty);
            ps.setString(8, unitPrice);
            ps.setString(9, workOrderNo.isEmpty() ? null : workOrderNo);
            ps.setString(10, oldColour);
            ps.setString(11, oldMaterial);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "✅ Stock updated successfully!");
                this.dispose();

                if (stockPanel != null) {
                    stockPanel.loadStockData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ No matching record found to update!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        SQty = new javax.swing.JTextField();
        Materials = new javax.swing.JTextField();
        AQty = new javax.swing.JTextField();
        UPrice = new javax.swing.JTextField();
        TIssued = new javax.swing.JTextField();
        RDate = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update Stock");

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

        jLabel12.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel12.setText("Work Order No :");

        jTextField9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Materials");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Stock Qty");

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Recieved Date");

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Previous Stock Qty");

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Recieved Qty");

        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel8.setText("Available Qty");

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel9.setText("Unit Price");

        SQty.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        SQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SQtyActionPerformed(evt);
            }
        });

        Materials.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        Materials.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MaterialsActionPerformed(evt);
            }
        });

        AQty.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        UPrice.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        TIssued.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        TIssued.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TIssuedActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel10.setText("Colour");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(803, 803, 803)
                        .addComponent(jButton1)
                        .addGap(40, 40, 40)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1)
                        .addGap(431, 431, 431)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addComponent(jLabel10)
                        .addComponent(jLabel8)
                        .addComponent(jLabel5))
                    .addGap(156, 156, 156)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                        .addComponent(Materials, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                        .addComponent(AQty, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                        .addComponent(RDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(107, 107, 107)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel9)
                        .addComponent(jLabel7)
                        .addComponent(jLabel4)
                        .addComponent(jLabel6))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(SQty, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                        .addComponent(TIssued, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                        .addComponent(UPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                        .addComponent(jTextField2))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel12)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(324, 324, 324)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(127, 127, 127)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(SQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(Materials, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(TIssued, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5))
                            .addGap(20, 20, 20)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(AQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9)
                                .addComponent(UPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(RDate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(127, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        updateStockInDatabase();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void SQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SQtyActionPerformed

    private void MaterialsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MaterialsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MaterialsActionPerformed

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
    private javax.swing.JTextField Materials;
    private com.toedter.calendar.JDateChooser RDate;
    private javax.swing.JTextField SQty;
    private javax.swing.JTextField TIssued;
    private javax.swing.JTextField UPrice;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
