package NerdTech.DR_Fashion.Views.Shipment;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import javax.swing.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author MG_Pathum
 */
public class AddShipmentDFrame extends javax.swing.JDialog {

    private String currentOrderNo = null; // To track if editing existing record

    // Constructor for adding new shipment
    public AddShipmentDFrame(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadComboBoxData();
        setupButtonListener();
        // Set title for Add mode
        jLabel1.setText("Add Shipment");
    }

    // Constructor for editing existing shipment
    public AddShipmentDFrame(java.awt.Frame parent, boolean modal,
            String orderNo, String fabricInhouseDate, String buyerName, String rollCutQty,
            String yardageFabric, String yardagePocketing, String subGarment, String style,
            String fabricUse, String brand, String cutPcs, String lineInQty, String month,
            String shipmentQty, String shipmentDate, String drInvoiceNo, String nextOrderBalance,
            String remark, String status, String paymentMethod) {

        super(parent, modal);
        initComponents();
        loadComboBoxData();
        setupButtonListener();

        // Set title for Update mode
        jLabel1.setText("Update Shipment");

        // Set current order number for update
        this.currentOrderNo = orderNo;

        // Fill form with existing data
        jTextField1.setText(orderNo);
        jTextField3.setText(buyerName);
        jTextField4.setText(rollCutQty);
        jTextField5.setText(yardageFabric);
        jTextField6.setText(yardagePocketing);
        jTextField7.setText(subGarment);
        jTextField8.setText(style);
        jTextField9.setText(fabricUse);
        jTextField10.setText(brand);
        jTextField2.setText(cutPcs);
        jTextField11.setText(lineInQty);
        jTextField13.setText(shipmentQty);
        jTextField15.setText(drInvoiceNo);
        jTextField16.setText(nextOrderBalance);
        jTextField17.setText(remark);

        // Set dates - Handle multiple date formats
        try {
            if (fabricInhouseDate != null && !fabricInhouseDate.isEmpty()) {
                java.util.Date date1 = parseDateFlexible(fabricInhouseDate);
                if (date1 != null) {
                    jDateChooser1.setDate(date1);
                }
            }

            if (month != null && !month.isEmpty()) {
                String monthValue = month.contains("-") ? month.split("-")[1] : month.split("/")[1];
                jComboBox1.setSelectedItem(getMonthName(Integer.parseInt(monthValue)));
            }

            if (shipmentDate != null && !shipmentDate.isEmpty()) {
                java.util.Date date2 = parseDateFlexible(shipmentDate);
                if (date2 != null) {
                    jDateChooser2.setDate(date2);
                }
            }
        } catch (Exception e) {
            System.err.println("Date parsing error: " + e.getMessage());
            e.printStackTrace();
        }

        // Set combo boxes
        if (status != null && !status.isEmpty()) {
            jComboBox2.setSelectedItem(status);
        }
        if (paymentMethod != null && !paymentMethod.isEmpty()) {
            jComboBox3.setSelectedItem(paymentMethod);
        }

        // Change button text to Update
        jButton1.setText("Update");
    }

    // NEW METHOD: Flexible date parser to handle multiple formats
    private java.util.Date parseDateFlexible(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        // Try different date formats
        String[] formats = {
            "yyyy-MM-dd", // 2025-05-04
            "yyyy/MM/dd", // 2025/05/04
            "dd-MM-yyyy", // 04-05-2025
            "dd/MM/yyyy" // 04/05/2025
        };

        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                sdf.setLenient(false);
                return sdf.parse(dateStr);
            } catch (Exception e) {
                // Try next format
            }
        }

        System.err.println("Could not parse date: " + dateStr);
        return null;
    }

    private void setupButtonListener() {
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveShipment();
            }
        });
    }

    private void loadComboBoxData() {
        // Load months
        String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
        jComboBox1.setModel(new DefaultComboBoxModel<>(months));

        // Load status
        String[] statuses = {"Active", "Completed", "Pending", "Cancelled"};
        jComboBox2.setModel(new DefaultComboBoxModel<>(statuses));

        // Load payment methods
        String[] paymentMethods = {"Cash", "Bank Transfer", "Cheque", "Credit"};
        jComboBox3.setModel(new DefaultComboBoxModel<>(paymentMethods));
    }

    private String getMonthName(int monthNumber) {
        String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
        if (monthNumber >= 1 && monthNumber <= 12) {
            return months[monthNumber - 1];
        }
        return months[0];
    }

    private int getMonthNumber(String monthName) {
        String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(monthName)) {
                return i + 1;
            }
        }
        return 1;
    }

    private void saveShipment() {
        // Validate inputs
        if (jTextField1.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Order Number", "Validation Error", JOptionPane.WARNING_MESSAGE);
            jTextField1.requestFocus();
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();

            // Prepare date strings in yyyy-MM-dd format
            String fabricInhouseDateStr = null;
            if (jDateChooser1.getDate() != null) {
                fabricInhouseDateStr = new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate());
            }

            String monthStr = null;
            if (jComboBox1.getSelectedItem() != null) {
                int monthNum = getMonthNumber(jComboBox1.getSelectedItem().toString());
                int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                monthStr = String.format("%d-%02d-01", year, monthNum);
            }

            String shipmentDateStr = null;
            if (jDateChooser2.getDate() != null) {
                shipmentDateStr = new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser2.getDate());
            }

            if (currentOrderNo != null) {
                // UPDATE existing record
                String updateQuery = """
                    UPDATE shipment SET 
                        order_no = ?,
                        fabric_inhouse_date = ?,
                        buyer_name = ?,
                        roll_cut_qty = ?,
                        yardage_fabric = ?,
                        yardage_pocketing = ?,
                        sub_garment = ?,
                        style = ?,
                        fabric_use = ?,
                        brand = ?,
                        cut_pcs = ?,
                        line_in_qty = ?,
                        month = ?,
                        shipment_qty = ?,
                        shipment_date = ?,
                        dr_invoice_no = ?,
                        next_order_balance = ?,
                        remark = ?,
                        status = ?,
                        payment_method = ?
                    WHERE order_no = ?
                """;

                PreparedStatement ps = conn.prepareStatement(updateQuery);
                ps.setString(1, jTextField1.getText().trim());
                ps.setString(2, fabricInhouseDateStr);
                ps.setString(3, jTextField3.getText().trim());
                ps.setString(4, jTextField4.getText().trim());
                ps.setString(5, jTextField5.getText().trim());
                ps.setString(6, jTextField6.getText().trim());
                ps.setString(7, jTextField7.getText().trim());
                ps.setString(8, jTextField8.getText().trim());
                ps.setString(9, jTextField9.getText().trim());
                ps.setString(10, jTextField10.getText().trim());
                ps.setString(11, jTextField2.getText().trim());
                ps.setString(12, jTextField11.getText().trim());
                ps.setString(13, monthStr);
                ps.setString(14, jTextField13.getText().trim());
                ps.setString(15, shipmentDateStr);
                ps.setString(16, jTextField15.getText().trim());
                ps.setString(17, jTextField16.getText().trim());
                ps.setString(18, jTextField17.getText().trim());
                ps.setString(19, jComboBox2.getSelectedItem().toString());
                ps.setString(20, jComboBox3.getSelectedItem().toString());
                ps.setString(21, currentOrderNo); // WHERE condition uses ORIGINAL order number

                int result = ps.executeUpdate();

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Shipment updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update shipment", "Error", JOptionPane.ERROR_MESSAGE);
                }

                ps.close();

            } else {
                // INSERT new record
                String insertQuery = """
                    INSERT INTO shipment (
                        order_no, fabric_inhouse_date, buyer_name, roll_cut_qty,
                        yardage_fabric, yardage_pocketing, sub_garment, style,
                        fabric_use, brand, cut_pcs, line_in_qty, month,
                        shipment_qty, shipment_date, dr_invoice_no, next_order_balance,
                        remark, status, payment_method
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

                PreparedStatement ps = conn.prepareStatement(insertQuery);
                ps.setString(1, jTextField1.getText().trim());
                ps.setString(2, fabricInhouseDateStr);
                ps.setString(3, jTextField3.getText().trim());
                ps.setString(4, jTextField4.getText().trim());
                ps.setString(5, jTextField5.getText().trim());
                ps.setString(6, jTextField6.getText().trim());
                ps.setString(7, jTextField7.getText().trim());
                ps.setString(8, jTextField8.getText().trim());
                ps.setString(9, jTextField9.getText().trim());
                ps.setString(10, jTextField10.getText().trim());
                ps.setString(11, jTextField2.getText().trim());
                ps.setString(12, jTextField11.getText().trim());
                ps.setString(13, monthStr);
                ps.setString(14, jTextField13.getText().trim());
                ps.setString(15, shipmentDateStr);
                ps.setString(16, jTextField15.getText().trim());
                ps.setString(17, jTextField16.getText().trim());
                ps.setString(18, jTextField17.getText().trim());
                ps.setString(19, jComboBox2.getSelectedItem().toString());
                ps.setString(20, jComboBox3.getSelectedItem().toString());

                int result = ps.executeUpdate();

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Shipment saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose(); // Close dialog after successful save
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save shipment", "Error", JOptionPane.ERROR_MESSAGE);
                }

                ps.close();
            }

            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearForm() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jTextField10.setText("");
        jTextField11.setText("");
        jTextField13.setText("");
        jTextField15.setText("");
        jTextField16.setText("");
        jTextField17.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Add Shipment");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Order Number");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Fabric Inhouse Date");

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Buyer Name");

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Roll Cut Qty");

        jTextField4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Yardage Fabric");

        jTextField5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("Yardage Pocketing");

        jTextField6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel8.setText("Sub Garment");

        jTextField7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel9.setText("Style");

        jTextField8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel10.setText("Fabric Use");

        jTextField9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel11.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel11.setText("Brand");

        jTextField10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel12.setText("Cut Pcs");

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel13.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel13.setText("Line In Qty");

        jTextField11.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel14.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel14.setText("Month");

        jLabel15.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel15.setText("Shipment Qty");

        jTextField13.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel16.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel16.setText("Shipment Date");

        jLabel17.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel17.setText("Dr Invoice No");

        jTextField15.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel18.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel18.setText("Next Order Balance");

        jTextField16.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel19.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel19.setText("Remark");

        jTextField17.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel20.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel20.setText("Status");

        jLabel21.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel21.setText("Payment Method");

        jComboBox1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Save");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel20))
                                .addGap(216, 216, 216)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField1)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField5)
                                    .addComponent(jTextField7)
                                    .addComponent(jTextField9)
                                    .addComponent(jTextField2)
                                    .addComponent(jTextField16)
                                    .addComponent(jComboBox1, 0, 273, Short.MAX_VALUE)
                                    .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel19))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(185, 185, 185)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField15, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                                            .addComponent(jTextField13)
                                            .addComponent(jTextField11)
                                            .addComponent(jTextField4)
                                            .addComponent(jTextField6)
                                            .addComponent(jTextField8)
                                            .addComponent(jTextField10)
                                            .addComponent(jTextField17))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(664, 664, 664))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)
                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel15)
                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jComboBox1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(jLabel17)
                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19)
                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21))
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed

    }//GEN-LAST:event_jTextField10ActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddShipmentDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddShipmentDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddShipmentDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddShipmentDFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddShipmentDFrame dialog = new AddShipmentDFrame(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
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
