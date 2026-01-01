package NerdTech.DR_Fashion.Views;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import NerdTech.DR_Fashion.DatabaseConnection.DatabaseConnection;
import NerdTech.DR_Fashion.DatabaseConnection.FullDatabaseSync;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;

public class LoginForm extends javax.swing.JFrame {

    private static final String VERSION = "v1.0";
    private JLabel versionLabel;
    private JLabel capsLockWarning;
    private JLabel copyrightLabel;

    public LoginForm() {
        initComponents();
        init();
        enhanceUI();
    }

    class User {

        String username;
        String fullName;
        String role;

        public User(String username, String fullName, String role) {
            this.username = username;
            this.fullName = fullName;
            this.role = role;
        }
    }

    public void init() {
        java.net.URL imageUrl = getClass().getResource("/NerdTech/DR_Fashion/img/logo.png");
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image scaledImage = icon.getImage().getScaledInstance(LoginImage.getWidth(), LoginImage.getHeight(), Image.SCALE_SMOOTH);
            LoginImage.setIcon(new ImageIcon(scaledImage));
        } else {
            System.err.println("Image not found: /NerdTech/DR_Fashion/img/logo.png");
        }
    }

    // නව enhanceUI() method - මේක ඔයාගේ පරණ එක replace කරන්න
    private void enhanceUI() {
        // Version label - jPanel1 එකේ top right corner එකට
        versionLabel = new JLabel(VERSION);
        versionLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        versionLabel.setForeground(new Color(150, 150, 150));

        // Copyright label - bottom center එකට (main content pane එකේ)
        copyrightLabel = new JLabel("© 2025 NerdTech™ Software Development. All Rights Reserved");
        copyrightLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 10));
        copyrightLabel.setForeground(new Color(120, 120, 120));
        copyrightLabel.setHorizontalAlignment(JLabel.CENTER);

        // Caps Lock warning label - jPanel2 එකේ password field එකට උඩින්
        capsLockWarning = new JLabel("");
        capsLockWarning.setFont(new Font("JetBrains Mono", Font.PLAIN, 11));
        capsLockWarning.setForeground(new Color(255, 153, 0));
        capsLockWarning.setHorizontalAlignment(JLabel.CENTER);

        // Window listener to position labels after window is fully created
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent evt) {
                positionCustomLabels();
            }
        });

        // Component listener for resize events
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                positionCustomLabels();
            }
        });

        // Enhanced button styling
        SignInBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        SignInBtn.setFocusPainted(false);
        SignInBtn.setBorderPainted(false);

        // Button hover effect
        SignInBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SignInBtn.setBackground(new Color(42, 58, 233));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SignInBtn.setBackground(new Color(52, 68, 243));
            }
        });

        // Enhanced text field borders
        Border defaultBorder = BorderFactory.createLineBorder(new Color(200, 200, 200), 1);
        Border focusBorder = BorderFactory.createLineBorder(new Color(52, 68, 243), 2);

        usernameField.setBorder(BorderFactory.createCompoundBorder(
                defaultBorder,
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        passwordField.setBorder(BorderFactory.createCompoundBorder(
                defaultBorder,
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Focus listeners for animated borders
        usernameField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                        focusBorder, BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                        defaultBorder, BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });

        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                        focusBorder, BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                        defaultBorder, BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });

        // Caps Lock detection
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    boolean capsLockOn = java.awt.Toolkit.getDefaultToolkit()
                            .getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK);
                    if (capsLockOn) {
                        capsLockWarning.setText("⚠ Caps Lock is ON");
                    } else {
                        capsLockWarning.setText("");
                    }
                } catch (UnsupportedOperationException ex) {
                    // Caps lock detection not supported on this system
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyPressed(e);  // Check on release too
            }
        });

        // Enter key support
        usernameField.addActionListener(evt -> passwordField.requestFocus());
        passwordField.addActionListener(evt -> SignInBtnActionPerformed(null));
    }

    private void positionCustomLabels() {
        if (!isVisible()) {
            return;
        }

        // Remove labels if already added
        if (versionLabel != null && versionLabel.getParent() != null) {
            versionLabel.getParent().remove(versionLabel);
        }
        if (copyrightLabel != null && copyrightLabel.getParent() != null) {
            copyrightLabel.getParent().remove(copyrightLabel);
        }
        if (capsLockWarning != null && capsLockWarning.getParent() != null) {
            capsLockWarning.getParent().remove(capsLockWarning);
        }

        // Use LayeredPane for absolute positioning over GroupLayout
        javax.swing.JLayeredPane layeredPane = getLayeredPane();

        // Create labels if they don't exist
        if (versionLabel == null) {
            versionLabel = new JLabel(VERSION);
            versionLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            versionLabel.setForeground(new Color(150, 150, 150));
        }

        if (copyrightLabel == null) {
            copyrightLabel = new JLabel("© 2025 NerdTech™ Software Development. All Rights Reserved");
            copyrightLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 10));
            copyrightLabel.setForeground(new Color(120, 120, 120));
            copyrightLabel.setHorizontalAlignment(JLabel.CENTER);
        }

        if (capsLockWarning == null) {
            capsLockWarning = new JLabel("");
            capsLockWarning.setFont(new Font("JetBrains Mono", Font.PLAIN, 11));
            capsLockWarning.setForeground(new Color(255, 153, 0));
            capsLockWarning.setHorizontalAlignment(JLabel.CENTER);
        }

        // Position version at top-right corner
        if (versionLabel != null) {
            int versionX = getWidth() - 80; // Right side with some margin
            int versionY = 539; // Top margin
            versionLabel.setBounds(versionX, versionY, 100, 30);
            layeredPane.add(versionLabel, javax.swing.JLayeredPane.PALETTE_LAYER);
        }

        // Position copyright at bottom center
        if (copyrightLabel != null) {
            int copyrightY = getHeight() - 26;
            copyrightLabel.setBounds(0, copyrightY, getWidth(), 20);
            layeredPane.add(copyrightLabel, javax.swing.JLayeredPane.PALETTE_LAYER);
        }

        // Position caps lock warning below password field
        if (capsLockWarning != null && passwordField != null) {
            java.awt.Point pwdLoc = javax.swing.SwingUtilities.convertPoint(
                    passwordField.getParent(), passwordField.getX(), passwordField.getY(), layeredPane);
            int warningX = pwdLoc.x;
            int warningY = pwdLoc.y + passwordField.getHeight() + 5;
            capsLockWarning.setBounds(warningX, warningY, passwordField.getWidth(), 20);
            layeredPane.add(capsLockWarning, javax.swing.JLayeredPane.PALETTE_LAYER);
        }

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        SignInBtn = new javax.swing.JButton();
        passwordField = new javax.swing.JPasswordField();
        LoginImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LogIn Form");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 36)); // NOI18N
        jLabel1.setText("Welcome to DR Fashions");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(221, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(78, 78, 78))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5), javax.swing.BorderFactory.createTitledBorder(null, "LogIn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("JetBrains Mono", 0, 24)))))); // NOI18N

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel2.setText("User Name");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        jLabel3.setText("Password");

        usernameField.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        usernameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameFieldActionPerformed(evt);
            }
        });

        SignInBtn.setBackground(new java.awt.Color(52, 68, 243));
        SignInBtn.setFont(new java.awt.Font("JetBrains Mono", 0, 24)); // NOI18N
        SignInBtn.setForeground(new java.awt.Color(255, 255, 255));
        SignInBtn.setText("Sign In");
        SignInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SignInBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(114, 114, 114)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(usernameField, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                            .addComponent(passwordField)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(SignInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(passwordField)
                        .addGap(2, 2, 2)))
                .addGap(66, 66, 66)
                .addComponent(SignInBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(118, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LoginImage, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 80, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(LoginImage, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void usernameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameFieldActionPerformed

    }//GEN-LAST:event_usernameFieldActionPerformed

    private User authenticateUser(String username, String password) {
        String sql = "SELECT full_name, role FROM user WHERE name = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(username, rs.getString("full_name"), rs.getString("role"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    private void SignInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SignInBtnActionPerformed
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password are required.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Show loading state
        SignInBtn.setEnabled(false);
        SignInBtn.setText("Signing In...");

        User user = authenticateUser(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful as " + user.fullName + " (" + user.role + ")",
                    "Welcome", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new Dashboard(user.fullName, user.role).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            SignInBtn.setEnabled(true);
            SignInBtn.setText("Sign In");
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }//GEN-LAST:event_SignInBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            FlatMacDarkLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SplashScreen splash = new SplashScreen();
        splash.showSplash();

        new Thread(() -> {
            try {
                splash.setStatus("🔌 Initializing...");
                Thread.sleep(300);

                splash.setStatus("🔗 Connecting to local database...");
                Connection localConn = DatabaseConnection.getConnection();

                if (localConn == null || localConn.isClosed()) {
                    throw new SQLException("Local database connection failed");
                }

                splash.setStatus("✅ Local database connected!");
                Thread.sleep(500);

                splash.setStatus("🌐 Checking online connection...");
                Connection onlineConn = null;
                boolean onlineAvailable = false;

                try {
                    onlineConn = DatabaseConnection.getOnlineConnection();
                    if (onlineConn != null && !onlineConn.isClosed()) {
                        onlineAvailable = true;
                        splash.setStatus("✅ Online database connected!");
                        Thread.sleep(500);
                        onlineConn.close();
                    }
                } catch (Exception e) {
                    splash.setStatus("⚠️ Online database unavailable");
                    splash.setSyncStatus("Will work in offline mode");
                    Logger.getLogger(LoginForm.class.getName()).log(Level.WARNING,
                            "Online database not available", e);
                    Thread.sleep(1000);
                }

                if (onlineAvailable) {
                    splash.setStatus("🔄 Syncing databases...");
                    splash.setSyncStatus("Starting bidirectional sync...");

                    FullDatabaseSync.setStatusCallback(new FullDatabaseSync.SyncStatusCallback() {
                        @Override
                        public void onStatusChange(String status) {
                            SwingUtilities.invokeLater(() -> {
                                splash.setSyncStatus(status);
                            });
                        }
                    });

                    try {
                        boolean syncSuccess = FullDatabaseSync.performFullSync();

                        if (syncSuccess) {
                            splash.setStatus("✅ Sync completed!");
                            splash.setSyncStatus("All data synchronized successfully");
                            Thread.sleep(800);
                        } else {
                            splash.setStatus("⚠️ Sync completed with warnings");
                            splash.setSyncStatus("Some data may not be synced");
                            Thread.sleep(1000);
                        }
                    } catch (Exception syncEx) {
                        splash.setStatus("⚠️ Sync failed");
                        splash.setSyncStatus("Continuing in offline mode");
                        Logger.getLogger(LoginForm.class.getName()).log(Level.WARNING,
                                "Sync failed", syncEx);
                        Thread.sleep(1000);
                    }
                }

                localConn.close();

                splash.setStatus("🚀 Starting application...");
                Thread.sleep(300);

                SwingUtilities.invokeLater(() -> {
                    splash.closeSplash();
                    new LoginForm().setVisible(true);
                });

            } catch (SQLException sqlEx) {
                splash.setStatus("❌ Database Error!");
                splash.setSyncStatus("Cannot connect to local database");
                sqlEx.printStackTrace();

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null,
                            "❌ Critical Database Error!\n\n"
                            + "Error: " + sqlEx.getMessage() + "\n\n"
                            + "Cannot connect to local MySQL database.\n\n"
                            + "Possible solutions:\n"
                            + "• Ensure local MySQL server is running\n"
                            + "• Check database credentials in DatabaseConnection.java\n"
                            + "• Verify database 'dr_fashion' exists\n"
                            + "• Check MySQL port (default: 3306)\n\n"
                            + "Application cannot start without database.",
                            "Critical Error",
                            JOptionPane.ERROR_MESSAGE);
                    splash.closeSplash();
                    System.exit(1);
                });

            } catch (Exception ex) {
                splash.setStatus("❌ Error occurred!");
                splash.setSyncStatus(ex.getMessage());
                ex.printStackTrace();

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null,
                            "⚠️ Application Error!\n\n"
                            + "Error: " + ex.getMessage() + "\n\n"
                            + "The application will try to continue.",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);
                    splash.closeSplash();
                    new LoginForm().setVisible(true);
                });
            }
        }).start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LoginImage;
    private javax.swing.JButton SignInBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:variables
}
