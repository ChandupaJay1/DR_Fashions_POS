package NerdTech.DR_Fashion.Views.Shipment.Cutting;

import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.Views.Shipment.ShipmentPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.*;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MG_Pathum
 */
public class CuttingPanel extends javax.swing.JPanel {

    public CuttingPanel() {
        initComponents();
        loadCuttingData();
        addDoubleClickListener(); // Add this line
    }

    // Add this method to handle double clicks
    private void addDoubleClickListener() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double click detected
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow != -1) {
                        editSelectedRow(selectedRow);
                    }
                }
            }
        });
    }

    private void editSelectedRow(int selectedRow) {
        try {
            int cuttingId = (int) jTable1.getValueAt(selectedRow, 4); // cutting_id is at column 4

            java.awt.Frame parentFrame = (java.awt.Frame) SwingUtilities.getWindowAncestor(this);
            AddCuttingDFrame dialog = new AddCuttingDFrame(parentFrame, true, cuttingId, this, true);
            dialog.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error opening edit dialog: " + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadCuttingData() {
        try {
            System.out.println("Starting loadCuttingData...");

            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Database connection established: " + (conn != null));

            // ✅ නිවැරදි JOIN query - shipment details සමග cutting details එකට ගන්න
            String query = "SELECT "
                    + "s.order_no as order_number, "
                    + "s.fabric_inhouse_date, "
                    + "s.buyer_name, "
                    + "s.yardage_fabric as yardage, "
                    + "c.id as cutting_id, "
                    + "c.shipment_id, "
                    + "c.style, "
                    + "c.roll, "
                    + "c.cut_no, "
                    + "c.fabric_issued, "
                    + "c.damage_return, "
                    + "c.roll_sort, "
                    + "c.end_fabric, "
                    + "c.balance, "
                    + "c.total_used, "
                    + "c.cut_pcs, "
                    + "c.consumption, "
                    + "c.width, "
                    + "c.remark "
                    + "FROM cutting c "
                    + "INNER JOIN shipment s ON c.shipment_id = s.id"; // ✅ INNER JOIN use කරන්න

            System.out.println("Executing main query: " + query);

            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); // Clear existing data

            int rowCount = 0;

            while (rs.next()) {
                rowCount++;
                Object[] row = {
                    rs.getString("order_number"),
                    rs.getDate("fabric_inhouse_date"),
                    rs.getString("buyer_name"),
                    rs.getString("yardage"),
                    rs.getInt("cutting_id"),
                    rs.getString("style"),
                    rs.getString("roll"),
                    rs.getString("cut_no"),
                    rs.getString("fabric_issued"),
                    rs.getString("damage_return"),
                    rs.getString("roll_sort"),
                    rs.getString("end_fabric"),
                    rs.getString("balance"),
                    rs.getString("total_used"),
                    rs.getString("cut_pcs"),
                    rs.getString("consumption"),
                    rs.getString("width"),
                    rs.getString("remark")
                };
                model.addRow(row);
            }

            // Close connections
            rs.close();
            pst.close();
            conn.close();

            System.out.println("Loaded " + rowCount + " cutting records successfully");

            if (rowCount == 0) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "No cutting records found in the database. Please add some cutting records first.",
                        "No Data",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error loading cutting data: " + e.getMessage(),
                    "Database Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add button functionality
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Add new cutting record
        showAddEditDialog(null);
    }

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Delete selected cutting record
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Please select a record to delete",
                    "No Selection",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this cutting record?",
                "Confirm Delete",
                javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            try {
                int cuttingId = (int) jTable1.getValueAt(selectedRow, 4); // cutting_id is at column 4

                Connection conn = DatabaseConnection.getConnection();
                String query = "DELETE FROM cutting WHERE id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, cuttingId);

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Record deleted successfully!");
                    loadCuttingData(); // Refresh the table
                }

                pst.close();
                conn.close();

            } catch (Exception e) {
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Error deleting record: " + e.getMessage(),
                        "Database Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Print functionality
        javax.swing.JOptionPane.showMessageDialog(this,
                "Print functionality will be implemented here",
                "Print",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Back to ShipmentPanel
        switchToShipmentPanel();
    }

    private void switchToShipmentPanel() {
        try {
            // Get the parent container
            java.awt.Container parent = this.getParent();

            // Remove current CuttingPanel
            parent.remove(this);

            // Create and add ShipmentPanel
            ShipmentPanel shipmentPanel = new ShipmentPanel();
            parent.add(shipmentPanel);

            // Refresh the container
            parent.revalidate();
            parent.repaint();

            System.out.println("Switched back to ShipmentPanel successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error switching to Shipment Panel: " + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddEditDialog(Integer cuttingId) {
        try {
            // Get the selected shipment for new records
            Integer shipmentId = null;
            if (cuttingId == null) {
                // For new records, get the first available shipment
                Connection conn = DatabaseConnection.getConnection();
                String query = "SELECT id FROM shipment LIMIT 1";
                PreparedStatement pst = conn.prepareStatement(query);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    shipmentId = rs.getInt("id");
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "No shipment records found. Please add shipment first.",
                            "No Data",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    rs.close();
                    pst.close();
                    conn.close();
                    return;
                }

                rs.close();
                pst.close();
                conn.close();
            }

            java.awt.Frame parentFrame = (java.awt.Frame) SwingUtilities.getWindowAncestor(this);

            AddCuttingDFrame dialog;
            if (cuttingId == null) {
                // New record - shipmentId use කරන්න
                dialog = new AddCuttingDFrame(parentFrame, true, shipmentId, this, false);
            } else {
                // Existing record - cuttingId use කරන්න  
                dialog = new AddCuttingDFrame(parentFrame, true, cuttingId, this, true);
            }

            dialog.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error opening dialog: " + e.getMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
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

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Cutting Details");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Order Number", "Fabric Inhouse Date", "Buyer Name", "Yardage", "Shipment Id", "Style", "Roll", "Cut No", "Fabric Issued", "Damage Return", "Roll Sort", "End Fabric", "Balance", "Total Used", "Cut Pcs", "Consumption", "Width", "Remark"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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
        jButton2.setText("Delete");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Print");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(330, 330, 330)
                        .addComponent(jButton2)
                        .addGap(403, 403, 403)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1508, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addButtonActionPerformed(evt);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        deleteButtonActionPerformed(evt);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            // Print functionality with A4 report
            printCuttingReport();

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error printing report: " + e.getMessage(),
                    "Print Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void printCuttingReport() {
        try {
            // Create a printer job
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Cutting Details Report");

            // Set page format for A4
            PageFormat pf = job.defaultPage();
            Paper paper = new Paper();

            // A4 size in points (1 inch = 72 points)
            double width = 595;   // 8.27 inches * 72 = 595 points
            double height = 842;  // 11.69 inches * 72 = 842 points

            paper.setSize(width, height);

            // Set smaller margins to fit more content
            double margin = 36; // 0.5 inch margins
            paper.setImageableArea(margin, margin, width - 2 * margin, height - 2 * margin);

            pf.setPaper(paper);
            pf.setOrientation(PageFormat.LANDSCAPE); // ✅ LANDSCAPE for more columns

            // Create printable component
            job.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
                        throws PrinterException {

                    if (pageIndex > 0) {
                        return NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    // Set font and color
                    g2d.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
                    g2d.setColor(Color.BLACK);

                    int y = 40; // Starting Y position

                    // Title
                    String title = "CUTTING DETAILS REPORT";
                    Font titleFont = new Font("JetBrains Mono", Font.BOLD, 20);
                    g2d.setFont(titleFont);
                    int titleWidth = g2d.getFontMetrics().stringWidth(title);
                    int centerX = (int) (pageFormat.getImageableWidth() - titleWidth) / 2;
                    g2d.drawString(title, centerX, y);
                    y += 30;

                    // Date
                    g2d.setFont(new Font("JetBrains Mono", Font.PLAIN, 10));
                    String date = "Generated on: " + new java.util.Date();
                    g2d.drawString(date, 0, y);
                    y += 25;

                    // Table header - ALL columns from your table
                    g2d.setFont(new Font("JetBrains Mono", Font.BOLD, 8));
                    String[] headers = {
                        "Order No", "Fabric Inhouse", "Buyer", "Yardage",
                        "Style", "Roll", "Cut No", "Fabric Issued",
                        "Damage Return", "Roll Sort", "End Fabric",
                        "Balance", "Total Used", "Cut Pcs", "Consumption", "Width", "Remark"
                    };

                    // Adjusted column widths for landscape mode
                    int[] columnWidths = {60, 70, 80, 50, 50, 40, 50, 60,
                        60, 50, 50, 50, 50, 50, 50, 40, 60};

                    int tableWidth = 0;
                    for (int width : columnWidths) {
                        tableWidth += width;
                    }

                    // Draw table header
                    int x = 0;
                    g2d.setColor(new Color(200, 200, 200));
                    g2d.fillRect(x, y, tableWidth, 20);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y, tableWidth, 20);

                    x = 0;
                    for (int i = 0; i < headers.length; i++) {
                        g2d.drawRect(x, y, columnWidths[i], 20);

                        // Center text in header
                        FontMetrics fm = g2d.getFontMetrics();
                        String headerText = headers[i];
                        int textWidth = fm.stringWidth(headerText);

                        // If text is too long, truncate it
                        if (textWidth > columnWidths[i] - 4) {
                            while (textWidth > columnWidths[i] - 4 && headerText.length() > 3) {
                                headerText = headerText.substring(0, headerText.length() - 1);
                                textWidth = fm.stringWidth(headerText + "...");
                            }
                            headerText = headerText + "...";
                        }

                        int textX = x + (columnWidths[i] - textWidth) / 2;
                        int textY = y + 13;
                        g2d.drawString(headerText, textX, textY);
                        x += columnWidths[i];
                    }
                    y += 20;

                    // Table data
                    g2d.setFont(new Font("JetBrains Mono", Font.PLAIN, 7));
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    int rowCount = model.getRowCount();

                    for (int row = 0; row < rowCount; row++) {
                        if (y > pageFormat.getImageableHeight() - 30) {
                            // If running out of space, create new page
                            return PAGE_EXISTS;
                        }

                        x = 0;
                        g2d.setColor(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                        g2d.fillRect(x, y, tableWidth, 18);
                        g2d.setColor(Color.BLACK);
                        g2d.drawRect(x, y, tableWidth, 18);

                        // Order No (column 0)
                        g2d.drawRect(x, y, columnWidths[0], 18);
                        drawCellText(g2d, model.getValueAt(row, 0), x, y, columnWidths[0], 18);
                        x += columnWidths[0];

                        // Fabric Inhouse Date (column 1)
                        g2d.drawRect(x, y, columnWidths[1], 18);
                        Object fabricDate = model.getValueAt(row, 1);
                        String dateStr = (fabricDate instanceof java.sql.Date)
                                ? ((java.sql.Date) fabricDate).toString()
                                : (fabricDate != null ? fabricDate.toString() : "");
                        drawCellText(g2d, dateStr, x, y, columnWidths[1], 18);
                        x += columnWidths[1];

                        // Buyer Name (column 2)
                        g2d.drawRect(x, y, columnWidths[2], 18);
                        drawCellText(g2d, model.getValueAt(row, 2), x, y, columnWidths[2], 18);
                        x += columnWidths[2];

                        // Yardage (column 3)
                        g2d.drawRect(x, y, columnWidths[3], 18);
                        drawCellText(g2d, model.getValueAt(row, 3), x, y, columnWidths[3], 18);
                        x += columnWidths[3];

                        // Style (column 5)
                        g2d.drawRect(x, y, columnWidths[4], 18);
                        drawCellText(g2d, model.getValueAt(row, 5), x, y, columnWidths[4], 18);
                        x += columnWidths[4];

                        // Roll (column 6)
                        g2d.drawRect(x, y, columnWidths[5], 18);
                        drawCellText(g2d, model.getValueAt(row, 6), x, y, columnWidths[5], 18);
                        x += columnWidths[5];

                        // Cut No (column 7)
                        g2d.drawRect(x, y, columnWidths[6], 18);
                        drawCellText(g2d, model.getValueAt(row, 7), x, y, columnWidths[6], 18);
                        x += columnWidths[6];

                        // Fabric Issued (column 8)
                        g2d.drawRect(x, y, columnWidths[7], 18);
                        drawCellText(g2d, model.getValueAt(row, 8), x, y, columnWidths[7], 18);
                        x += columnWidths[7];

                        // Damage Return (column 9)
                        g2d.drawRect(x, y, columnWidths[8], 18);
                        drawCellText(g2d, model.getValueAt(row, 9), x, y, columnWidths[8], 18);
                        x += columnWidths[8];

                        // Roll Sort (column 10)
                        g2d.drawRect(x, y, columnWidths[9], 18);
                        drawCellText(g2d, model.getValueAt(row, 10), x, y, columnWidths[9], 18);
                        x += columnWidths[9];

                        // End Fabric (column 11)
                        g2d.drawRect(x, y, columnWidths[10], 18);
                        drawCellText(g2d, model.getValueAt(row, 11), x, y, columnWidths[10], 18);
                        x += columnWidths[10];

                        // Balance (column 12)
                        g2d.drawRect(x, y, columnWidths[11], 18);
                        drawCellText(g2d, model.getValueAt(row, 12), x, y, columnWidths[11], 18);
                        x += columnWidths[11];

                        // Total Used (column 13)
                        g2d.drawRect(x, y, columnWidths[12], 18);
                        drawCellText(g2d, model.getValueAt(row, 13), x, y, columnWidths[12], 18);
                        x += columnWidths[12];

                        // Cut Pcs (column 14)
                        g2d.drawRect(x, y, columnWidths[13], 18);
                        drawCellText(g2d, model.getValueAt(row, 14), x, y, columnWidths[13], 18);
                        x += columnWidths[13];

                        // Consumption (column 15)
                        g2d.drawRect(x, y, columnWidths[14], 18);
                        drawCellText(g2d, model.getValueAt(row, 15), x, y, columnWidths[14], 18);
                        x += columnWidths[14];

                        // Width (column 16)
                        g2d.drawRect(x, y, columnWidths[15], 18);
                        drawCellText(g2d, model.getValueAt(row, 16), x, y, columnWidths[15], 18);
                        x += columnWidths[15];

                        // Remark (column 17)
                        g2d.drawRect(x, y, columnWidths[16], 18);
                        drawCellText(g2d, model.getValueAt(row, 17), x, y, columnWidths[16], 18);

                        y += 18;
                    }

                    // Summary
                    y += 15;
                    g2d.setFont(new Font("JetBrains Mono", Font.BOLD, 10));
                    g2d.drawString("Total Records: " + rowCount, 0, y);

                    return PAGE_EXISTS;
                }

                // Helper method to draw text in cells with truncation if needed
                private void drawCellText(Graphics2D g2d, Object value, int x, int y, int width, int height) {
                    if (value == null) {
                        return;
                    }

                    String text = value.toString();
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(text);

                    // If text is too long, truncate it
                    if (textWidth > width - 4) {
                        while (textWidth > width - 4 && text.length() > 3) {
                            text = text.substring(0, text.length() - 1);
                            textWidth = fm.stringWidth(text + "...");
                        }
                        text = text + "...";
                    }

                    int textX = x + 2;
                    int textY = y + 12;
                    g2d.drawString(text, textX, textY);
                }
            }, pf);

            // Show print dialog
            if (job.printDialog()) {
                job.print();
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Report printed successfully!",
                        "Print Success",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error printing: " + e.getMessage(),
                    "Print Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        backButtonActionPerformed(evt);
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
