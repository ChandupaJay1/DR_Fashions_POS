package NerdTech.DR_Fashion.Views.Bill;

import NerdTech.DR_Fashion.Views.Bill.AddBill;
import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BillPanel extends javax.swing.JPanel {

    private Integer filteredInvoiceId = null;
    private Integer filteredBuyerId = null;
    private String filteredBuyerName = null;
    private String filteredInvoiceNo = null;

    // Constructor 1: Show all bills
    public BillPanel() {
        this.filteredInvoiceId = null;
        this.filteredBuyerId = null;
        this.filteredBuyerName = null;
        this.filteredInvoiceNo = null;
        initComponents();
        fixBillTableColumns();  // මේක add කරන්න
        loadBillData();
        setupButtonActions();
        updateTitle();
    }

    // Constructor 2: Show bills for specific buyer (old constructor - keep for compatibility)
    public BillPanel(int buyerId, String buyerName) {
        this.filteredInvoiceId = null;
        this.filteredBuyerId = buyerId;
        this.filteredBuyerName = buyerName;
        this.filteredInvoiceNo = null;
        initComponents();
        fixBillTableColumns();  // මේක add කරන්න
        loadBillData();
        setupButtonActions();
        updateTitle();
    }

    private void fixBillTableColumns() {
        // Bill ID column එක සමග table model එක reset කරන්න
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Bill ID", "Date", "Description", "Size", "Qty", "Unit",
                    "Price", "Return Amount", "Total Amount", "Paid Amount",
                    "Balance", "Remark", "Payment Method"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // කිසිම cell එකක් edit කරන්න බෑ
            }
        };
        jTable1.setModel(model);

        // Bill ID column එක hide කරන්න (පළමු column එක)
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    // Constructor 3: Show bills for specific invoice (NEW - මේක තමයි අලුත constructor එක)
    // Constructor 3: Show bills for specific invoice
    public BillPanel(int invoiceId, int buyerId, String buyerName, String invoiceNo) {
        this.filteredInvoiceId = invoiceId;
        this.filteredBuyerId = buyerId;
        this.filteredBuyerName = buyerName;
        this.filteredInvoiceNo = invoiceNo;
        initComponents();
        fixBillTableColumns();  // ⬅️ මේ line එක add කරන්න!
        loadBillData();
        setupButtonActions();
        updateTitle();
    }

    // Update title based on filter
    private void updateTitle() {
        if (filteredInvoiceId != null && filteredInvoiceNo != null) {
            // Invoice specific view
            jLabel1.setText("Bill Details - " + filteredBuyerName + " - Invoice: " + filteredInvoiceNo);
        } else if (filteredBuyerId != null && filteredBuyerName != null) {
            // Buyer specific view (all invoices)
            jLabel1.setText("Bill Details - " + filteredBuyerName);
        } else {
            // All buyers view
            jLabel1.setText("Bill Details - All Buyers");
        }
    }

    // Setup Button Actions
    private void setupButtonActions() {
        // මේක empty තියෙනවා නම් remove කරන්න
        // Add button action listener දැනටමත් initComponents() එකේ තියෙනවා

        // Pay Button Action
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    handlePayment();
                } catch (Exception ex) {
                    Logger.getLogger(BillPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void handlePayment() throws Exception {
        // ✅ Invoice ID check කරන්න
        if (filteredInvoiceId == null) {
            JOptionPane.showMessageDialog(this,
                    "No invoice selected! Please select an invoice first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String totalText = jTextField1.getText().trim();
        String balanceText = jTextField2.getText().trim();
        String paymentText = jTextField3.getText().trim();

        if (balanceText.isEmpty() || balanceText.equals("0.00")) {
            JOptionPane.showMessageDialog(this,
                    "No balance to pay!",
                    "Payment",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (paymentText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter payment amount!",
                    "Payment Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double total = Double.parseDouble(totalText);
            double balance = Double.parseDouble(balanceText);
            double payment = Double.parseDouble(paymentText);

            if (payment <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Payment amount must be greater than 0!",
                        "Invalid Amount",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (payment > balance) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Payment amount (" + payment + ") is greater than balance (" + balance + ").\nDo you want to continue?",
                        "Confirm Overpayment",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Payment database එකට update කරන්න
            makePaymentToDatabase(payment);

            // Table refresh කරනවා
            refreshTable();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers!",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void makePaymentToDatabase(double paymentAmount) throws Exception {
        try {
            Connection conn = DatabaseConnection.getConnection();

            System.out.println("🔍 DEBUG - Payment Details:");
            System.out.println("   Invoice ID: " + filteredInvoiceId);
            System.out.println("   Payment Amount: " + paymentAmount);

            // Check if bills exist
            String checkQuery = "SELECT COUNT(*) as count FROM bill WHERE invoice_no_id = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkQuery);
            checkPs.setInt(1, filteredInvoiceId);
            ResultSet checkRs = checkPs.executeQuery();

            if (checkRs.next() && checkRs.getInt("count") == 0) {
                checkRs.close();
                checkPs.close();
                JOptionPane.showMessageDialog(this,
                        "No bills found for this invoice!",
                        "Payment Failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            checkRs.close();
            checkPs.close();

            // ✅ අන්තිම bill එක හොයා ගන්න
            String getLastBillQuery = "SELECT id, total_amount, paid_amount, balance FROM bill "
                    + "WHERE invoice_no_id = ? "
                    + "ORDER BY date DESC, id DESC "
                    + "LIMIT 1";

            PreparedStatement lastBillPs = conn.prepareStatement(getLastBillQuery);
            lastBillPs.setInt(1, filteredInvoiceId);
            ResultSet lastBillRs = lastBillPs.executeQuery();

            int lastBillId = -1;
            double currentTotal = 0.0;
            double currentPaid = 0.0;
            double currentBalance = 0.0;

            if (lastBillRs.next()) {
                lastBillId = lastBillRs.getInt("id");
                currentTotal = lastBillRs.getDouble("total_amount");
                currentPaid = lastBillRs.getDouble("paid_amount");
                currentBalance = lastBillRs.getDouble("balance");

                System.out.println("   Last Bill ID: " + lastBillId);
                System.out.println("   Current Total: " + currentTotal);
                System.out.println("   Current Paid: " + currentPaid);
                System.out.println("   Current Balance: " + currentBalance);
            }
            lastBillRs.close();
            lastBillPs.close();

            if (lastBillId == -1) {
                JOptionPane.showMessageDialog(this,
                        "Could not find the last bill!",
                        "Payment Failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ✅ Paid amount සහ balance විතරක් update කරන්න (Total එක වෙනස් නොකරන්න)
            double newPaid = currentPaid + paymentAmount;
            double newBalance = currentBalance - paymentAmount;

            String updateQuery = "UPDATE bill SET "
                    + "paid_amount = ?, "
                    + "balance = ? "
                    + "WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(updateQuery);
            ps.setDouble(1, newPaid);
            ps.setDouble(2, newBalance);
            ps.setInt(3, lastBillId);

            int updatedRows = ps.executeUpdate();
            ps.close();

            System.out.println("   New Paid: " + newPaid);
            System.out.println("   New Balance: " + newBalance);
            System.out.println("   Updated Rows: " + updatedRows);

            if (updatedRows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Payment of " + paymentAmount + " applied successfully!",
                        "Payment Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to update bill!",
                        "Payment Failed",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error applying payment: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBillData() {
        double lastBillTotalAmount = 0.0;
        double totalPaidAmount = 0.0;  // ⬅️ Database එකේ සියලුම paid amounts

        try {
            Connection conn = DatabaseConnection.getConnection();

            String query = "SELECT b.id, b.date, b.description, b.size, b.qty, b.unit, b.price, "
                    + "b.return_amount, b.total_amount, b.paid_amount, b.balance, "
                    + "b.remark, b.payment_method, bb.name AS buyer_name, i.invoice_no "
                    + "FROM bill b "
                    + "INNER JOIN invoice_no i ON b.invoice_no_id = i.id "
                    + "INNER JOIN bill_buyer bb ON i.bill_buyer_id = bb.id ";

            if (filteredInvoiceId != null) {
                query += "WHERE b.invoice_no_id = ? ";
            } else if (filteredBuyerId != null) {
                query += "WHERE i.bill_buyer_id = ? ";
            }

            query += "ORDER BY b.date DESC, b.id DESC";

            PreparedStatement ps = conn.prepareStatement(query);

            if (filteredInvoiceId != null) {
                ps.setInt(1, filteredInvoiceId);
            } else if (filteredBuyerId != null) {
                ps.setInt(1, filteredBuyerId);
            }

            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            int rowCount = 0;
            boolean isFirstRow = true;

            while (rs.next()) {
                // ✅ පළමු row එකේ total_amount
                if (isFirstRow) {
                    lastBillTotalAmount = rs.getDouble("total_amount");
                    isFirstRow = false;
                }

                // ✅ සියලුම bills වල paid_amount එකතු කරන්න
                totalPaidAmount += rs.getDouble("paid_amount");

                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("date"),
                    rs.getString("description"),
                    rs.getString("size"),
                    rs.getString("qty"),
                    rs.getString("unit"),
                    rs.getString("price"),
                    rs.getString("return_amount"),
                    rs.getString("total_amount"),
                    rs.getString("paid_amount"),
                    rs.getString("balance"),
                    rs.getString("remark"),
                    rs.getString("payment_method")
                };
                model.addRow(row);
                rowCount++;
            }

            rs.close();
            ps.close();

            // ✅ Balance = Total - සියලුම Paid Amounts
            double calculatedBalance = lastBillTotalAmount - totalPaidAmount;

            // ✅ Display values
            jTextField1.setText(String.format("%.2f", lastBillTotalAmount));   // Total
            jTextField2.setText(String.format("%.2f", calculatedBalance));     // Balance (Total - Paid)
            jTextField3.setText("");  // Clear Paid Amount field

            if (filteredInvoiceId != null && rowCount == 0) {
                JOptionPane.showMessageDialog(this,
                        "No bills found for invoice: " + filteredInvoiceNo,
                        "No Data",
                        JOptionPane.INFORMATION_MESSAGE);
                jTextField1.setText("0.00");
                jTextField2.setText("0.00");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading bill data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            jTextField1.setText("0.00");
            jTextField2.setText("0.00");
        }
    }

    // Delete Bill
    private void deleteBill(int billId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "DELETE FROM bill WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, billId);

            int result = ps.executeUpdate();
            ps.close();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Bill deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete bill!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                JOptionPane.showMessageDialog(this,
                        "Cannot delete this bill!\nThere are related records.",
                        "Delete Error - Foreign Key Constraint",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting bill: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting bill: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Refresh Table
    public void refreshTable() {
        loadBillData();
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
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Bill Details");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Date", "Description", "Size", "Qty", "Unit", "Price", "Return Amount", "Total Amount", "Paid Amount", "Balance", "Remark", "Payment Method"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Update");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton4.setText("Back");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jLabel2.setText("Total");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jLabel3.setText("Balance");

        jTextField2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jLabel4.setText("Paid Amount");

        jTextField3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N

        jButton5.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton5.setText("Pay");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
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
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(53, 53, 53)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(83, 83, 83)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(229, 229, 229)
                                .addComponent(jButton2)))
                        .addGap(44, 44, 44)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButton5)
                                .addGap(222, 222, 222)
                                .addComponent(jButton4))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(53, 53, 53)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel2)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            javax.swing.JFrame frame
                    = (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this);

            if (frame != null && frame instanceof NerdTech.DR_Fashion.Views.Dashboard) {

                NerdTech.DR_Fashion.Views.Dashboard dashboard
                        = (NerdTech.DR_Fashion.Views.Dashboard) frame;

                // 🔑 Invoice list panel load කරනවා
                dashboard.loadPanelWithLoading(
                        "Invoice No - " + filteredBuyerName,
                        () -> new NerdTech.DR_Fashion.Views.BillBuyer.Bill.RegisterInvoice.RegisterInvoiceNoPanel(
                                filteredBuyerId,
                                filteredBuyerName
                        )
                );

            } else {
                // fallback
                frame.setContentPane(
                        new NerdTech.DR_Fashion.Views.BillBuyer.Bill.RegisterInvoice.RegisterInvoiceNoPanel(
                                filteredBuyerId,
                                filteredBuyerName
                        )
                );
                frame.revalidate();
                frame.repaint();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading invoice panel: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void openUpdateBillDialog(int billId) {
        try {
            java.awt.Frame parentFrame = (java.awt.Frame) SwingUtilities.getWindowAncestor(this);
            UpdateBill updateDialog = new UpdateBill(parentFrame, true, this, billId);
            updateDialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening update bill: " + e.getMessage());
        }
    }


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a bill to update!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Hidden column (0) එකෙන් Bill ID එක ගන්න
        int billId = (int) jTable1.getValueAt(selectedRow, 0);

        // Update dialog open කරන්න
        openUpdateBillDialog(billId);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bill to delete!",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Hidden column එකෙන් Bill ID එක ගන්න
        int billId = (int) jTable1.getValueAt(selectedRow, 0);
        String description = (String) jTable1.getValueAt(selectedRow, 2); // Column 2 is description (0 is hidden ID)

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this bill?\nDescription: " + description,
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            deleteBill(billId);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            handlePayment();
        } catch (Exception ex) {
            Logger.getLogger(BillPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        openAddBillDialog();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void openAddBillDialog() {
        try {
            if (filteredInvoiceId == null) {
                JOptionPane.showMessageDialog(this,
                        "Please select an invoice first!",
                        "No Invoice Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (filteredBuyerId == null) {
                JOptionPane.showMessageDialog(this,
                        "Buyer information is missing!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.awt.Frame parentFrame = (java.awt.Frame) SwingUtilities.getWindowAncestor(this);

            AddBill addDialog = new AddBill(
                    parentFrame,
                    true,
                    this,
                    filteredInvoiceId,
                    filteredBuyerId,
                    filteredBuyerName,
                    filteredInvoiceNo
            );
            addDialog.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error opening add bill dialog: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
