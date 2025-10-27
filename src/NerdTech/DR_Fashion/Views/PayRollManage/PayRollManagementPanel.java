package NerdTech.DR_Fashion.Views.PayRollManage;

import NerdTech.DR_Fashion.Views.LoadingPanel;
import javax.swing.*;

public class PayRollManagementPanel extends javax.swing.JPanel {

    public PayRollManagementPanel() {
        initComponents();
        setupScrollBars(); // මේක add කරන්න
    }

    private void setupScrollBars() {
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Table එකේ auto resize off කරන්න
        model.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        model = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1542, 664));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("PayRoll Management");

        model.setFont(new java.awt.Font("JetBrains Mono", 0, 18)); // NOI18N
        model.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Name", "Status", "nullsdvds", "nullsdvds", "nullsdvds", "dsv", "dsvd", "sdvdsv", "dsvdsv", "dsvdsvds", "sdvvds", "sdvsdv", "sdvsdvds", "dsvdsv", "sdvds", "sdvdsvdsv", "sdvdsv", "sdvdsvd", "sdvdsv", "dsvdsvd", "sdvdsv", "sdvdsvd"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        model.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                modelMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(model);
        if (model.getColumnModel().getColumnCount() > 0) {
            model.getColumnModel().getColumn(0).setResizable(false);
            model.getColumnModel().getColumn(1).setResizable(false);
        }

        jButton2.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton2.setText("New User");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jButton3.setText("Incentive");
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1530, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(42, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void modelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_modelMouseClicked


    }//GEN-LAST:event_modelMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        loadNewUserPanel();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        loadIncentivePanel();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void loadIncentivePanel() {
        try {
            // 1️⃣ Create and show the loading panel
            LoadingPanel loadingPanel = new LoadingPanel("Loading Incentive Panel...");

            // 2️⃣ Get parent container (the main panel holder)
            java.awt.Container parent = this.getParent();
            if (parent != null) {
                // Remove current panel and show loading
                parent.remove(this);
                parent.add(loadingPanel);
                parent.revalidate();
                parent.repaint();

                // 3️⃣ Load IncentivePanel in background (non-blocking)
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Optional: small artificial delay to show loading screen
                        Thread.sleep(500);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            // Remove loading panel
                            parent.remove(loadingPanel);

                            // Load the IncentivePanel
                            NerdTech.DR_Fashion.Views.PayRollManage.IncentivePanel.IncentivePanel incentivePanel
                                    = new NerdTech.DR_Fashion.Views.PayRollManage.IncentivePanel.IncentivePanel();

                            // Add and refresh
                            parent.add(incentivePanel);
                            parent.revalidate();
                            parent.repaint();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(parent,
                                    "Error loading Incentive Panel: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                // Execute background task
                worker.execute();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading Incentive Panel!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadNewUserPanel() {
        try {
            // Show loading panel first
            LoadingPanel loadingPanel = new LoadingPanel("Loading New User Panel");

            // Get the parent container
            java.awt.Container parent = this.getParent();
            if (parent != null) {
                // Remove current panel and show loading
                parent.remove(this);
                parent.add(loadingPanel);
                parent.revalidate();
                parent.repaint();

                // Use SwingWorker to load NewUserPanel in background
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Simulate loading time (you can remove this in production)
                        Thread.sleep(500);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            // Remove loading panel and add NewUserPanel
                            parent.remove(loadingPanel);
                            NewUserPanel newUserPanel = new NewUserPanel();
                            parent.add(newUserPanel);
                            parent.revalidate();
                            parent.repaint();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(parent,
                                    "Error loading New User panel: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                worker.execute();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading New User panel!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable model;
    // End of variables declaration//GEN-END:variables
}
