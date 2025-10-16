package NerdTech.DR_Fashion.Views;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {

    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JLabel titleLabel;
    private JLabel syncStatusLabel;

    public SplashScreen() {
        initComponents();
    }

    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(45, 52, 54));
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(52, 68, 243), 3));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(45, 52, 54));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        // Logo/Title
        titleLabel = new JLabel("DR Fashions");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Management System");
        subtitleLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(178, 190, 195));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(Box.createVerticalStrut(30));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(subtitleLabel);
        titlePanel.add(Box.createVerticalStrut(20));

        // Status label
        statusLabel = new JLabel("Initializing...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(178, 190, 195));

        // Sync status label (additional detail)
        syncStatusLabel = new JLabel("", SwingConstants.CENTER);
        syncStatusLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        syncStatusLabel.setForeground(new Color(99, 205, 218)); // Cyan color

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(400, 25));
        progressBar.setBackground(new Color(45, 52, 54));
        progressBar.setForeground(new Color(52, 68, 243));
        progressBar.setBorderPainted(false);

        // Center panel for progress bar
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(45, 52, 54));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(statusLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(syncStatusLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(progressBar);
        centerPanel.add(Box.createVerticalStrut(30));

        // Add to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Window settings
        setContentPane(mainPanel);
        setSize(500, 350);
        setLocationRelativeTo(null);
    }

    public void setStatus(String status) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(status));
    }

    public void setSyncStatus(String status) {
        SwingUtilities.invokeLater(() -> syncStatusLabel.setText(status));
    }

    public void setProgress(int value) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setIndeterminate(false);
            progressBar.setValue(value);
        });
    }

    public void setIndeterminate(boolean indeterminate) {
        SwingUtilities.invokeLater(() -> progressBar.setIndeterminate(indeterminate));
    }

    public void showSplash() {
        setVisible(true);
    }

    public void closeSplash() {
        setVisible(false);
        dispose();
    }
}
