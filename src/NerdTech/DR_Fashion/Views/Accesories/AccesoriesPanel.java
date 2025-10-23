/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package NerdTech.DR_Fashion.Views.Accesories;

import NerdTech.DR_Fashion.Views.LoadingPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import static NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection.getConnection;
import NerdTech.DR_Fashion.Views.Dashboard;
import NerdTech.DR_Fashion.Views.Stock.StockPanel;
import com.formdev.flatlaf.FlatDarkLaf;

/**
 *
 * @author MG_Pathum
 */
public class AccesoriesPanel extends javax.swing.JPanel {

    private LoadingPanel loadingPanel;
    private boolean isInitialized = false;
    private TableRowSorter<javax.swing.table.DefaultTableModel> sorter;

    public AccesoriesPanel() {
        FlatDarkLaf.setup();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1237, 686));
        setBackground(new Color(50, 50, 50));
        showLoading("Connecting to Database");
        loadContentInBackground();
    }

    private void showLoading(String message) {
        removeAll();
        loadingPanel = new LoadingPanel(message);
        add(loadingPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void loadContentInBackground() {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("Loading accessories data");
                Thread.sleep(300);
                try (Connection conn = getConnection()) {
                    publish("Fetching records");
                    Thread.sleep(300);
                }
                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                if (loadingPanel != null && !chunks.isEmpty()) {
                    loadingPanel.setMessage(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                    showActualContent();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("Failed to load data: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void showActualContent() {
        removeAll();
        initComponents();
        sorter = new TableRowSorter<>((javax.swing.table.DefaultTableModel) model.getModel());
        model.setRowSorter(sorter);

        searchTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            private void search() {
                String text = searchTextField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        loadAccessories();
        isInitialized = true;
        revalidate();
        repaint();
    }

    private void showError(String errorMessage) {
        removeAll();
        JPanel errorPanel = new JPanel(new GridBagLayout());
        errorPanel.setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);

        JLabel errorIcon = new JLabel("⚠️");
        errorIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        errorPanel.add(errorIcon, gbc);

        gbc.gridy = 1;
        JLabel errorLabel = new JLabel("Connection Failed");
        errorLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 24));
        errorLabel.setForeground(new Color(239, 68, 68));
        errorPanel.add(errorLabel, gbc);

        gbc.gridy = 2;
        JLabel errorMsg = new JLabel(errorMessage);
        errorMsg.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        errorMsg.setForeground(new Color(100, 116, 139));
        errorPanel.add(errorMsg, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 0, 0);
        JButton retryBtn = new JButton("Retry");
        retryBtn.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        retryBtn.addActionListener(e -> {
            showLoading("Reconnecting");
            loadContentInBackground();
        });
        errorPanel.add(retryBtn, gbc);

        add(errorPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void loadAccessories() {
        try {
            Connection con = getConnection();
            String sql = "SELECT a.*, t.type_name FROM accesories a "
                    + "LEFT JOIN type t ON a.type_id = t.type_id "
                    + "WHERE (a.status != 'deactivated' OR a.status IS NULL)";
            java.sql.Statement stmt = con.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);

            javax.swing.table.DefaultTableModel tableModel = (javax.swing.table.DefaultTableModel) model.getModel();
            tableModel.setRowCount(0);

            while (rs.next()) {
                String formattedPrice = String.format("Rs. %.2f", rs.getDouble("unit_price"));
                String typeName = rs.getString("type_name");
                if (typeName == null) {
                    typeName = "N/A";
                }

                // Get name from database (add default if null)
                String name = rs.getString("name");
                if (name == null) {
                    name = "N/A";
                }

                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("order_no"),
                    name, // Name column added here
                    rs.getString("colour_name"),
                    rs.getString("size"),
                    rs.getInt("stock_qty"),
                    rs.getString("uom"),
                    rs.getDate("received_date").toString(),
                    rs.getDate("issued_date").toString(),
                    rs.getInt("total_issued"),
                    rs.getInt("available_qty"),
                    formattedPrice,
                    typeName
                };

                tableModel.addRow(row);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No accessories found in database.");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading accessories: " + e.getMessage());
        }
    }

    public void refreshTable() {
        if (!isInitialized) {
            return;
        }
        showLoading("Refreshing accessories data");
        loadContentInBackground();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        model = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1257, 0));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Accesories");

        model.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        model.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "id", "Order Num", "Name", "Colour Name", "Size", "Stock Quantity", "UOM", "Recieve Date", "Issued Date", "Totall Issued", "Available Quantiy", "Unit Price", "Type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(model);
        if (model.getColumnModel().getColumnCount() > 0) {
            model.getColumnModel().getColumn(0).setResizable(false);
            model.getColumnModel().getColumn(1).setResizable(false);
            model.getColumnModel().getColumn(2).setResizable(false);
            model.getColumnModel().getColumn(3).setResizable(false);
            model.getColumnModel().getColumn(4).setResizable(false);
            model.getColumnModel().getColumn(4).setPreferredWidth(10);
            model.getColumnModel().getColumn(5).setResizable(false);
            model.getColumnModel().getColumn(5).setPreferredWidth(50);
            model.getColumnModel().getColumn(6).setResizable(false);
            model.getColumnModel().getColumn(6).setPreferredWidth(30);
            model.getColumnModel().getColumn(7).setResizable(false);
            model.getColumnModel().getColumn(7).setPreferredWidth(100);
            model.getColumnModel().getColumn(8).setResizable(false);
            model.getColumnModel().getColumn(8).setPreferredWidth(100);
            model.getColumnModel().getColumn(9).setResizable(false);
            model.getColumnModel().getColumn(10).setResizable(false);
            model.getColumnModel().getColumn(11).setResizable(false);
            model.getColumnModel().getColumn(11).setPreferredWidth(100);
            model.getColumnModel().getColumn(12).setResizable(false);
        }

        jButton1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton1.setText("Delete");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton4.setText("Update");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        jLabel2.setText("Search");

        searchTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTextFieldActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Go to Stock");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1453, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(217, 217, 217)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(228, 228, 228)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        AddAccesoriesDFrame addAccesories = new AddAccesoriesDFrame((java.awt.Frame) parentWindow, true);
        addAccesories.setVisible(true);
        loadAccessories();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedRow = model.getSelectedRow();
        if (selectedRow >= 0) {
            String id = model.getValueAt(selectedRow, 0).toString();

            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this record?",
                    "Confirm Delete",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                try {
                    Connection con = getConnection();
                    // Database එකෙන් DELETE කරනවා වෙනුවට UPDATE කරනවා
                    String sql = "UPDATE accesories SET status = 'deactivated' WHERE id = ?";
                    java.sql.PreparedStatement pst = con.prepareStatement(sql);
                    pst.setInt(1, Integer.parseInt(id));
                    int deleted = pst.executeUpdate();

                    if (deleted > 0) {
                        javax.swing.JOptionPane.showMessageDialog(this, "Record deleted successfully.");
                        loadAccessories();
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(this, "Failed to delete record.");
                    }

                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        UpdateAccesoriesDFrame updateAccesories = new UpdateAccesoriesDFrame((java.awt.Frame) parentWindow, true);

        String issueDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

        int selectedRow = model.getSelectedRow();
        if (selectedRow >= 0) {
            String id = model.getValueAt(selectedRow, 0).toString();
            String orderNo = model.getValueAt(selectedRow, 1).toString();
            String name = model.getValueAt(selectedRow, 2).toString();  // Get name from column 2
            String colourName = model.getValueAt(selectedRow, 3).toString();  // Index changed to 3
            String size = model.getValueAt(selectedRow, 4).toString();  // Index changed to 4
            String stockQty = model.getValueAt(selectedRow, 10).toString();  // Index changed to 10
            String uom = model.getValueAt(selectedRow, 6).toString();  // Index changed to 6
            String totalIssued = "";
            String availableQty = "";
            String unitPrice = model.getValueAt(selectedRow, 11).toString().replace("Rs. ", "");  // Index changed to 11

            int typeId = 0;
            try {
                Connection con = getConnection();
                String sql = "SELECT type_id FROM accesories WHERE id = ?";
                java.sql.PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(id));
                java.sql.ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    typeId = rs.getInt("type_id");
                }
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            updateAccesories.setData(
                    id, orderNo, name, colourName, size, // Added name parameter
                    stockQty, uom, totalIssued, availableQty, unitPrice, issueDate, typeId
            );

            updateAccesories.setVisible(true);
            loadAccessories();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a row to update.");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void searchTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTextFieldActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        final Dashboard dashboard = (Dashboard) javax.swing.SwingUtilities.getWindowAncestor(this);

        if (dashboard != null) {
            dashboard.loadPanel("Stock", new Dashboard.PanelLoader() {
                public javax.swing.JPanel loadPanel() throws Exception {
                    return new StockPanel();
                }
            });
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error: Cannot access Dashboard!",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    // ඔබේ main JFrame class එකේ හෝ main method එකේ
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            AccesoriesPanel frame = new AccesoriesPanel();
            frame.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables
}
