/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Bill;

import NerdTech.DR_Fashion.Views.Bill.BillPanel;
import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.awt.event.*;
import java.util.Date;

/**
 *
 * @author MG_Pathum
 */
public class UpdateBill extends javax.swing.JDialog {

    private BillPanel parentPanel;
    private int billId;
    private int buyerId; // Buyer ID save කරන්න
    private double originalTotalAmount = 0.0;

    public UpdateBill(java.awt.Frame parent, boolean modal, BillPanel panel, int billId) {
        super(parent, modal);
        this.parentPanel = panel;
        this.billId = billId;
        initComponents();
        loadBillBuyers();
        loadPaymentMethods();
        loadBillData();
        setupCalculations();
        setLocationRelativeTo(parent);
    }

    private void loadBillBuyers() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT id, name FROM bill_buyer ORDER BY name";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            jComboBox1.removeAllItems();
            jComboBox1.addItem("-- Select Buyer --");

            while (rs.next()) {
                jComboBox1.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading buyers: " + e.getMessage());
        }
    }

    private void loadPaymentMethods() {
        jComboBox2.removeAllItems();
        jComboBox2.addItem("Cash");
        jComboBox2.addItem("Bank Transfer");
        jComboBox2.addItem("Cheque");
        jComboBox2.addItem("Credit");
    }

    private void loadBillData() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM bill WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Buyer ID save කරන්න
                this.buyerId = rs.getInt("bill_buyer_id");
                
                // Set buyer
                for (int i = 0; i < jComboBox1.getItemCount(); i++) {
                    String item = jComboBox1.getItemAt(i);
                    if (item.startsWith(buyerId + " - ")) {
                        jComboBox1.setSelectedIndex(i);
                        break;
                    }
                }

                // Set date
                String dateStr = rs.getString("date");
                if (dateStr != null && !dateStr.isEmpty()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = sdf.parse(dateStr);
                        jDateChooser1.setDate(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Set all fields
                jTextField2.setText(rs.getString("invoice_no"));
                jTextField2.setEditable(false);
                jTextField3.setText(rs.getString("description"));
                jTextField3.setEditable(false);
                jTextField4.setText(rs.getString("size"));
                jTextField4.setEditable(false);
                jTextField5.setText(rs.getString("qty"));
                jTextField5.setEditable(false);
                jTextField6.setText(rs.getString("unit"));
                jTextField6.setEditable(false);
                jTextField8.setText(rs.getString("price"));
                jTextField8.setEditable(false);
                jTextField9.setText(rs.getString("return_amount"));
                jTextField9.setEditable(false);
                jTextField10.setText(rs.getString("total_amount"));
                jTextField10.setEditable(false);
                jTextField11.setText(rs.getString("paid_amount"));
                jTextField12.setText(rs.getString("balance"));
                jTextField12.setEditable(false);
                jTextField13.setText(rs.getString("remark"));

                String totalAmtStr = rs.getString("total_amount");
                originalTotalAmount = totalAmtStr != null ? Double.parseDouble(totalAmtStr) : 0.0;

                String paymentMethod = rs.getString("payment_method");
                if (paymentMethod != null) {
                    jComboBox2.setSelectedItem(paymentMethod);
                }
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading bill data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupCalculations() {
        jTextField11.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateBalance();
            }
        });
    }

    private void calculateBalance() {
        try {
            double totalAmount = parseDouble(jTextField10.getText());
            double paidAmount = parseDouble(jTextField11.getText());
            double balance = totalAmount - paidAmount;

            jTextField12.setText(String.format("%.2f", balance));
        } catch (Exception e) {
            jTextField12.setText("0.00");
        }
    }

    private double parseDouble(String text) {
        try {
            return text.isEmpty() ? 0.0 : Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // මෙන්න ප්‍රධාන method එක - previous bills update කරන්න
    private void updatePreviousBillsBalance() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            // වත්මන් bill එකට පෙර ඇති bills වල balance 0 කරන්න
            String query = "UPDATE bill SET balance = 0 " +
                          "WHERE bill_buyer_id = ? AND id < ? AND balance != 0";
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, buyerId);
            ps.setInt(2, billId);
            
            int updatedRows = ps.executeUpdate();
            ps.close();
            
            if (updatedRows > 0) {
                System.out.println("Updated " + updatedRows + " previous bill(s) balance to 0");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error updating previous bills: " + e.getMessage());
        }
    }

    private void updateBill() {
        try {
            if (jComboBox1.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please select a buyer!");
                return;
            }

            if (jDateChooser1.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please select a date!");
                return;
            }

            if (jTextField2.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an Invoice Number!");
                return;
            }

            String selected = jComboBox1.getSelectedItem().toString();
            int buyerId = Integer.parseInt(selected.split(" - ")[0]);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(jDateChooser1.getDate());

            String invoiceNo = jTextField2.getText().trim();
            String description = jTextField3.getText();
            String size = jTextField4.getText();
            String qty = jTextField5.getText();
            String unit = jTextField6.getText();
            String price = jTextField8.getText();
            String returnAmount = jTextField9.getText().isEmpty() ? "0" : jTextField9.getText();
            String totalAmount = jTextField10.getText();
            String paidAmount = jTextField11.getText();
            String balance = jTextField12.getText();
            String remark = jTextField13.getText();
            String paymentMethod = jComboBox2.getSelectedItem().toString();

            Connection conn = DatabaseConnection.getConnection();
            String query = "UPDATE bill SET bill_buyer_id = ?, date = ?, invoice_no = ?, "
                    + "description = ?, size = ?, qty = ?, unit = ?, price = ?, "
                    + "return_amount = ?, total_amount = ?, paid_amount = ?, balance = ?, "
                    + "remark = ?, payment_method = ? WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, buyerId);
            ps.setString(2, date);
            ps.setString(3, invoiceNo);
            ps.setString(4, description);
            ps.setString(5, size);
            ps.setString(6, qty);
            ps.setString(7, unit);
            ps.setString(8, price);
            ps.setString(9, returnAmount);
            ps.setString(10, totalAmount);
            ps.setString(11, paidAmount);
            ps.setString(12, balance);
            ps.setString(13, remark);
            ps.setString(14, paymentMethod);
            ps.setInt(15, billId);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                double bal = Double.parseDouble(balance);
                
                // වත්මන් bill එක fully paid නම් previous bills balance 0 කරන්න
                if (bal == 0.0) {
                    updatePreviousBillsBalance();
                }
                
                String message = "Bill updated successfully!\n\n" +
                        "Total Amount: Rs. " + totalAmount + "\n" +
                        "Paid Amount: Rs. " + paidAmount + "\n" +
                        "Balance: Rs. " + balance;
                
                if (bal == 0.0) {
                    message += "\n\n✓ Bill fully paid!";
                    message += "\n✓ Previous bills balance cleared!";
                } else {
                    message += "\n\n⚠ Remaining balance: Rs. " + balance;
                }

                JOptionPane.showMessageDialog(this,
                        message,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                if (parentPanel != null) {
                    parentPanel.refreshTable();
                }

                dispose();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error updating bill: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Update Bill");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Buyer Name");

        jComboBox1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Date");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Invoice No");

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Description");

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Size");

        jTextField4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Qty");

        jTextField5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel8.setText("Unit");

        jTextField6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel9.setText("Price");

        jTextField8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel10.setText("Return Amount");

        jTextField9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel11.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel11.setText("Total Amount");

        jTextField10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel12.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel12.setText("Paid Amount");

        jTextField11.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel13.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel13.setText("Balance");

        jTextField12.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel14.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel14.setText("Remark");

        jTextField13.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel15.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel15.setText("payment_method");

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Update");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12)
                            .addComponent(jLabel14)
                            .addComponent(jLabel1))
                        .addGap(79, 79, 79)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                                    .addComponent(jTextField9)
                                    .addComponent(jTextField11)
                                    .addComponent(jTextField13)
                                    .addComponent(jTextField2)
                                    .addComponent(jTextField4))
                                .addGap(139, 139, 139)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel15))
                                .addGap(109, 109, 109)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField12)
                                    .addComponent(jTextField8)
                                    .addComponent(jTextField5)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField10)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox2, 0, 262, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(554, 554, 554))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(6, 6, 6))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)
                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        updateBill();
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UpdateBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateBill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UpdateBill dialog = new UpdateBill(new javax.swing.JFrame(), true, null, 1);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
