package NerdTech.DR_Fashion.Views.Shipment.Cutting;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.awt.event.ItemEvent;
import javax.swing.*;
import java.sql.*;
import java.text.DecimalFormat;

/**
 *
 * @author MG_Pathum
 */
public class AddCuttingDFrame extends javax.swing.JDialog {

    private Integer cuttingId;
    private Integer shipmentId;
    private CuttingPanel cuttingPanel;
    private DecimalFormat df = new DecimalFormat("#.##");
    private boolean isEditing; // Add this flag

    // Constructor for adding new record
    public AddCuttingDFrame(java.awt.Frame parent, boolean modal, Integer shipmentId, CuttingPanel cuttingPanel) {
        super(parent, modal);
        this.shipmentId = shipmentId;
        this.cuttingPanel = cuttingPanel;
        this.isEditing = false; // This is for adding new record
        initComponents();
        setLocationRelativeTo(parent);
        updateUIForMode(); // Update UI based on mode
        loadOrderNumbers();
        setupAutoCalculations();
    }

    private void loadOrderNumbers() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT DISTINCT order_no FROM shipment ORDER BY order_no";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            jComboBox1.removeAllItems();
            jComboBox1.addItem("Select Order No"); // default item

            while (rs.next()) {
                jComboBox1.addItem(rs.getString("order_no"));
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading order numbers: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Constructor for both adding new record and editing existing record
    public AddCuttingDFrame(java.awt.Frame parent, boolean modal, Integer id, CuttingPanel cuttingPanel, boolean isEditing) {
        super(parent, modal);
        this.cuttingPanel = cuttingPanel;
        this.isEditing = isEditing; // Set the editing flag

        if (isEditing) {
            // Editing existing record
            this.cuttingId = id;
            initComponents();
            setLocationRelativeTo(parent);
            updateUIForMode(); // Update UI based on mode
            loadOrderNumbers();
            setupAutoCalculations();
            loadCuttingData();
        } else {
            // Adding new record
            this.shipmentId = id;
            initComponents();
            setLocationRelativeTo(parent);
            updateUIForMode(); // Update UI based on mode
            loadOrderNumbers();
            setupAutoCalculations();

            // Set fields as read-only
            jTextField8.setEditable(false);
            jTextField10.setEditable(false);
        }
    }

    // Add this method to update UI based on mode (Add/Edit)
    private void updateUIForMode() {
        if (isEditing) {
            setTitle("Update Cutting Details");
            jLabel1.setText("Update Cutting Details");
            jButton1.setText("Update");
        } else {
            setTitle("Add Cutting Details");
            jLabel1.setText("Add Cutting Details");
            jButton1.setText("Save");
        }
    }

    private void setupAutoCalculations() {
        // Add key listeners for auto-calculation
        jTextField13.addKeyListener(new java.awt.event.KeyAdapter() { // Fabric Issued
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateTotalUsed();
            }
        });

        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateTotalUsed();
            }
        });

        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateTotalUsed();
            }
        });

        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateTotalUsed();
            }
        });

        // ✅ Balance field එකට key listener එකතු කරන්න
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateTotalUsed();
            }
        });

        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateConsumption();
            }
        });

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    loadShipmentDetails();
                }
            }
        });

    }

    private void loadShipmentDetails() {
        try {
            String selectedOrderNo = (String) jComboBox1.getSelectedItem();

            if (selectedOrderNo == null || selectedOrderNo.equals("Select Order No")) {
                return; // Nothing selected
            }

            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM shipment WHERE order_no = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, selectedOrderNo);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // ✅ REMOVED: Auto-fill fields from shipment
                // jTextField1.setText(rs.getString("style")); // Style - REMOVED
                // jTextField9.setText(rs.getString("cut_pcs")); // Cut Pcs - REMOVED

                // Store the shipment ID for later use
                this.shipmentId = rs.getInt("id");

                System.out.println("Loaded shipment details - Shipment ID: " + rs.getInt("id"));

                // ✅ Only set shipment_id, don't auto-fill other fields
                // User will manually enter Style, Cut Pcs, etc.
            } else {
                System.out.println("No shipment found for order no: " + selectedOrderNo);
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading shipment details: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCuttingData() {
        try {
            Connection conn = DatabaseConnection.getConnection();

            // ✅ Get cutting record with shipment order_no
            String query = "SELECT c.*, s.order_no FROM cutting c "
                    + "INNER JOIN shipment s ON c.shipment_id = s.id "
                    + "WHERE c.id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, cuttingId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                jTextField1.setText(rs.getString("style"));
                jTextField2.setText(rs.getString("roll"));
                jTextField3.setText(rs.getString("cut_no"));
                jTextField13.setText(rs.getString("fabric_issued"));
                jTextField4.setText(rs.getString("damage_return"));
                jTextField5.setText(rs.getString("roll_sort"));
                jTextField6.setText(rs.getString("end_fabric"));
                jTextField7.setText(rs.getString("balance"));
                jTextField8.setText(rs.getString("total_used"));
                jTextField9.setText(rs.getString("cut_pcs"));
                jTextField10.setText(rs.getString("consumption"));
                jTextField11.setText(rs.getString("width"));
                jTextField12.setText(rs.getString("remark"));

                this.shipmentId = rs.getInt("shipment_id");

                // ✅ Set the order_no in combo box for editing
                String orderNo = rs.getString("order_no");
                if (orderNo != null) {
                    jComboBox1.setSelectedItem(orderNo);
                }
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading cutting data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateTotalUsed() {
        try {
            double fabricIssued = getDoubleValue(jTextField13.getText()); // Fabric Issued
            double damageReturn = getDoubleValue(jTextField4.getText());
            double rollSort = getDoubleValue(jTextField5.getText());
            double endFabric = getDoubleValue(jTextField6.getText());
            double balance = getDoubleValue(jTextField7.getText()); // ✅ Balance එක user input ලෙස ගන්න

            // ✅ නිවැරදි calculation: Fabric Issued - (Damage Return + Roll Sort + End Fabric + Balance)
            double totalUsed = fabricIssued - (damageReturn + rollSort + endFabric + balance);
            jTextField8.setText(df.format(totalUsed));

            // Auto calculate consumption
            calculateConsumption();

        } catch (Exception e) {
            // Ignore calculation errors for empty fields
        }
    }

    private void calculateBalance() {
        try {
            // First get the yardage from shipment table
            double yardage = getYardageFromShipment();
            double totalUsed = getDoubleValue(jTextField8.getText());

            // ✅ Balance = Yardage - Total Used
            double balance = yardage - totalUsed;
            jTextField7.setText(df.format(balance));

        } catch (Exception e) {
            // Ignore calculation errors
        }
    }

    private void calculateConsumption() {
        try {
            double totalUsed = getDoubleValue(jTextField8.getText());
            double cutPcs = getDoubleValue(jTextField9.getText());

            if (cutPcs > 0) {
                double consumption = totalUsed / cutPcs;
                jTextField10.setText(df.format(consumption));
            }

        } catch (Exception e) {
            // Ignore calculation errors
        }
    }

    private double getYardageFromShipment() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT yardage_fabric FROM shipment WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, shipmentId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return getDoubleValue(rs.getString("yardage_fabric"));
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private double getDoubleValue(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Validate required fields including order no
            if (jTextField1.getText().trim().isEmpty()
                    || jTextField2.getText().trim().isEmpty()
                    || jTextField3.getText().trim().isEmpty()
                    || jComboBox1.getSelectedItem() == null
                    || jComboBox1.getSelectedItem().equals("Select Order No")) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all required fields (Style, Roll, Cut No, Order No)",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst;

            if (cuttingId == null) {
                // Insert new record
                String query = "INSERT INTO cutting (shipment_id, style, roll, cut_no, fabric_issued, damage_return, "
                        + "roll_sort, end_fabric, balance, total_used, cut_pcs, consumption, width, remark) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = conn.prepareStatement(query);
                pst.setInt(1, shipmentId); // ✅ shipment_id from selected order
                pst.setString(2, jTextField1.getText()); // Style
                pst.setString(3, jTextField2.getText()); // Roll
                pst.setString(4, jTextField3.getText()); // Cut No
                pst.setString(5, jTextField13.getText()); // Fabric Issued
                pst.setString(6, jTextField4.getText()); // Damage Return
                pst.setString(7, jTextField5.getText()); // Roll Sort
                pst.setString(8, jTextField6.getText()); // End Fabric
                pst.setString(9, jTextField7.getText()); // Balance
                pst.setString(10, jTextField8.getText()); // Total Used
                pst.setString(11, jTextField9.getText()); // Cut Pcs
                pst.setString(12, jTextField10.getText()); // Consumption
                pst.setString(13, jTextField11.getText()); // Width
                pst.setString(14, jTextField12.getText()); // Remark
            } else {
                // Update existing record
                String query = "UPDATE cutting SET shipment_id=?, style=?, roll=?, cut_no=?, fabric_issued=?, damage_return=?, "
                        + "roll_sort=?, end_fabric=?, balance=?, total_used=?, cut_pcs=?, "
                        + "consumption=?, width=?, remark=? WHERE id=?";
                pst = conn.prepareStatement(query);
                pst.setInt(1, shipmentId); // ✅ shipment_id also update කරන්න
                pst.setString(2, jTextField1.getText());
                pst.setString(3, jTextField2.getText());
                pst.setString(4, jTextField3.getText());
                pst.setString(5, jTextField13.getText()); // Fabric Issued
                pst.setString(6, jTextField4.getText());
                pst.setString(7, jTextField5.getText());
                pst.setString(8, jTextField6.getText());
                pst.setString(9, jTextField7.getText());
                pst.setString(10, jTextField8.getText());
                pst.setString(11, jTextField9.getText());
                pst.setString(12, jTextField10.getText());
                pst.setString(13, jTextField11.getText());
                pst.setString(14, jTextField12.getText());
                pst.setInt(15, cuttingId);
            }

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                        cuttingId == null ? "Record added successfully!" : "Record updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Refresh the cutting panel
                if (cuttingPanel != null) {
                    cuttingPanel.loadCuttingData();
                }

                this.dispose();
            }

            pst.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saving record: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
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
        jTextField10 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Add Cutting Details");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Style");

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel3.setText("Roll");

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel4.setText("Cut No");

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel5.setText("Damage Return");

        jTextField4.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel6.setText("Roll Sort");

        jTextField5.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel7.setText("End Fabric");

        jTextField6.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel8.setText("Balance");

        jTextField7.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel9.setText("Total Used");

        jTextField8.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel10.setText("Cut Pcs");

        jTextField9.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jTextField10.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel11.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel11.setText("Consumption");

        jLabel12.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel12.setText("Width");

        jTextField11.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel13.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel13.setText("Remark");

        jTextField12.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel14.setText("Fabric Issued");

        jTextField13.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N

        jLabel15.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel15.setText("Order No");

        jComboBox1.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(576, 576, 576))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(119, 119, 119)
                        .addComponent(jLabel15)
                        .addGap(53, 53, 53)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel13)
                            .addComponent(jLabel11)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5))
                        .addGap(185, 185, 185)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                            .addComponent(jTextField6)
                            .addComponent(jTextField8)
                            .addComponent(jTextField10)
                            .addComponent(jTextField1)
                            .addComponent(jTextField3)
                            .addComponent(jTextField12))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12)
                            .addComponent(jLabel3)
                            .addComponent(jLabel14))
                        .addGap(211, 211, 211)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField13, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                            .addComponent(jTextField5)
                            .addComponent(jTextField7)
                            .addComponent(jTextField9)
                            .addComponent(jTextField11)
                            .addComponent(jTextField2))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel15)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveButtonActionPerformed(evt);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox1;
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
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
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
