/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Accesories;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import static NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection;
import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.PreparedStatement;

/**
 *
 * @author MG_Pathum
 */
public class UpdateAccesoriesDFrame extends javax.swing.JDialog {

    /**
     * Creates new form UpdateAccesoriesDFrame
     */
    public UpdateAccesoriesDFrame(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadTypes();

        // Live update Available Quantity
        javax.swing.event.DocumentListener listener = new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateAvailableQty();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateAvailableQty();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateAvailableQty();
            }
        };

        jTextField5.getDocument().addDocumentListener(listener);
        jTextField7.getDocument().addDocumentListener(listener);
        jTextField8.setEnabled(false);
    }

    private void loadTypes() {
        try {
            Connection con = getConnection();
            String sql = "SELECT type_id, type_name FROM type ORDER BY type_name";
            java.sql.Statement stmt = con.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);

            jComboBox1.removeAllItems();

            while (rs.next()) {
                jComboBox1.addItem(rs.getString("type_name"));
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading types: " + e.getMessage());
        }
    }

    private int getTypeIdByName(String typeName) {
        try {
            Connection con = getConnection();
            String sql = "SELECT type_id FROM type WHERE type_name = ?";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, typeName);
            java.sql.ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int typeId = rs.getInt("type_id");
                con.close();
                return typeId;
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String getTypeNameById(int typeId) {
        try {
            Connection con = getConnection();
            String sql = "SELECT type_name FROM type WHERE type_id = ?";
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, typeId);
            java.sql.ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String typeName = rs.getString("type_name");
                con.close();
                return typeName;
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateAvailableQty() {
        try {
            int stockQty = Integer.parseInt(jTextField5.getText());
            int totalIssued = Integer.parseInt(jTextField7.getText());
            int availableQty = stockQty - totalIssued;
            jTextField8.setText(String.valueOf(availableQty));
        } catch (NumberFormatException e) {
            jTextField8.setText("0");
        }
    }

    // Updated setData method with name parameter
    public void setData(String id, String orderNo, String name, String colourName, String size,
            String stockQty, String uom, String totalIssued,
            String availableQty, String unitPrice, String issueDate, int typeId) {
        jTextField9.setText(id);           // ID
        jTextField2.setText(orderNo);      // Order Number
        jTextField10.setText(name);        // Name (jTextField10)
        jTextField3.setText(colourName);   // Colour Name
        jTextField4.setText(size);         // Size
        jTextField5.setText(stockQty);     // Stock Quantity
        jTextField6.setText(uom);          // UOM
        jTextField7.setText(totalIssued);  // Total Issued
        jTextField8.setText(availableQty); // Available Quantity
        jTextField1.setText(unitPrice);    // Unit Price

        // Set the correct type in combo box
        String typeName = getTypeNameById(typeId);
        if (typeName != null) {
            jComboBox1.setSelectedItem(typeName);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        btnUpdateActionPerformed = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Update Items");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel2.setText("Order Number");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel3.setText("Colour Name");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel4.setText("Size");

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel5.setText("Stock Quantity");

        jTextField2.setBackground(new java.awt.Color(211, 211, 211));
        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(0, 0, 0));

        jTextField3.setBackground(new java.awt.Color(211, 211, 211));
        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(0, 0, 0));

        jTextField4.setBackground(new java.awt.Color(211, 211, 211));
        jTextField4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField4.setForeground(new java.awt.Color(0, 0, 0));

        jLabel12.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel12.setText("ID");

        jTextField9.setBackground(new java.awt.Color(211, 211, 211));
        jTextField9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField9.setForeground(new java.awt.Color(0, 0, 0));

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel7.setText("Name");

        jTextField10.setBackground(new java.awt.Color(255, 255, 255));
        jTextField10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField10.setForeground(new java.awt.Color(0, 0, 0));
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel12))
                        .addGap(232, 232, 232)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField3)
                            .addComponent(jTextField2)
                            .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(125, 125, 125)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(295, 295, 295)
                                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(0, 90, Short.MAX_VALUE))
        );

        btnUpdateActionPerformed.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        btnUpdateActionPerformed.setText("Update");
        btnUpdateActionPerformed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformedActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel13.setText("Type");

        jComboBox1.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox1.setForeground(new java.awt.Color(0, 0, 0));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel6.setText("UOM");

        jTextField6.setBackground(new java.awt.Color(255, 255, 255));
        jTextField6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField6.setForeground(new java.awt.Color(0, 0, 0));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTextField7.setBackground(new java.awt.Color(255, 255, 255));
        jTextField7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField7.setForeground(new java.awt.Color(0, 0, 0));
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel9.setText("Total Issued");

        jLabel10.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel10.setText("Available Quantity");

        jTextField8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel11.setText("Unit Price");

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(0, 0, 0));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField5.setBackground(new java.awt.Color(255, 255, 255));
        jTextField5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField5.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(238, 238, 238))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(266, 266, 266)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addComponent(jTextField7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13))
                .addGap(99, 99, 99)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextField5)
                    .addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(53, 53, 53))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnUpdateActionPerformed, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(657, 657, 657))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(14, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(185, Short.MAX_VALUE)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))
                    .addComponent(jLabel13))
                .addGap(37, 37, 37)
                .addComponent(btnUpdateActionPerformed, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(147, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void updateAccessories() {
        try (Connection con = getConnection()) {
            String sql = "UPDATE accesories SET "
                    + "stock_qty = ?, "
                    + "uom = ?, "
                    + "total_issued = ?, "
                    + "available_qty = ?, "
                    + "unit_price = ?, "
                    + "issued_date = CURRENT_DATE "
                    + "WHERE id = ?";

            java.sql.PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(jTextField5.getText()));
            pstmt.setString(2, jTextField6.getText());
            pstmt.setInt(3, Integer.parseInt(jTextField7.getText()));
            pstmt.setInt(4, Integer.parseInt(jTextField8.getText()));
            pstmt.setDouble(5, Double.parseDouble(jTextField1.getText()));
            pstmt.setInt(6, Integer.parseInt(jTextField9.getText()));

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Accessory updated successfully!");
                this.dispose(); // Close dialog
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Update failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

    }


    private void btnUpdateActionPerformedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformedActionPerformed
        try {
            if (jComboBox1.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please select a type!");
                return;
            }

            String selectedTypeName = (String) jComboBox1.getSelectedItem();
            int typeId = getTypeIdByName(selectedTypeName);

            if (typeId == -1) {
                JOptionPane.showMessageDialog(this, "Invalid type selected!");
                return;
            }

            // Get name from jTextField10
            String name = jTextField10.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name!");
                return;
            }

            Connection con = DatabaseConnection.getConnection();
            // Updated SQL to include name field
            String sql = "UPDATE accesories SET name=?, stock_qty=?, uom=?, total_issued=?, available_qty=?, unit_price=?, type_id=?, issued_date=CURDATE() WHERE id=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, name);  // Add name parameter
            pst.setInt(2, Integer.parseInt(jTextField5.getText()));
            pst.setString(3, jTextField6.getText());
            pst.setInt(4, Integer.parseInt(jTextField7.getText()));
            pst.setInt(5, Integer.parseInt(jTextField8.getText()));
            pst.setDouble(6, Double.parseDouble(jTextField1.getText()));
            pst.setInt(7, typeId);
            pst.setInt(8, Integer.parseInt(jTextField9.getText()));

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Record updated successfully!");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed!");
            }

            con.close();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid number input!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnUpdateActionPerformedActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UpdateAccesoriesDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateAccesoriesDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateAccesoriesDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateAccesoriesDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UpdateAccesoriesDFrame dialog = new UpdateAccesoriesDFrame(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnUpdateActionPerformed;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
