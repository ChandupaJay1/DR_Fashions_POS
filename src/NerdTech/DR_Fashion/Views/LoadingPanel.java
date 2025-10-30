package NerdTech.DR_Fashion.Views;

import javax.swing.*;
import java.awt.*;

/**
 * Reusable Loading Panel with animated spinner 
 * Shows "Connecting..." or custom message while data loads
 * Always centered in parent container
 * DARK THEME VERSION - ULTRA FAST ANIMATION
 */
public class LoadingPanel extends JPanel {

    private JLabel messageLabel;
    private JLabel spinnerLabel;
    private Timer animationTimer;
    private int dotCount = 0;
    private String baseMessage;
    private int rotationIndex = 0;

    // Dark theme colors
    private final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private final Color PRIMARY_COLOR = new Color(59, 130, 246);
    private final Color TEXT_COLOR = new Color(229, 231, 235);
    private final Color SUBTITLE_COLOR = new Color(156, 163, 175);

    public LoadingPanel() {
        this("Loading");
    }

    public LoadingPanel(String message) {
        this.baseMessage = message;
        initComponents();
        startAnimation();
    }

    private void initComponents() {
        // Use BorderLayout for better centering
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);  // Dark background

        // Create a center panel with GridBagLayout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setOpaque(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);

        // Animated spinner icon
        spinnerLabel = new JLabel("⚡");
        spinnerLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        spinnerLabel.setForeground(PRIMARY_COLOR);
        spinnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(spinnerLabel, gbc);

        // Message label with animated dots
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        messageLabel = new JLabel(baseMessage + "...", SwingConstants.CENTER);
        messageLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 18));
        messageLabel.setForeground(TEXT_COLOR);
        messageLabel.setPreferredSize(new Dimension(250, 30));
        centerPanel.add(messageLabel, gbc);

        // Subtitle
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 0, 0);
        JLabel subtitleLabel = new JLabel("Processing your data", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        subtitleLabel.setForeground(SUBTITLE_COLOR);
        centerPanel.add(subtitleLabel, gbc);

        // Progress bar for better UX
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 50, 0, 50);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Animated progress bar
        progressBar.setPreferredSize(new Dimension(300, 8));
        progressBar.setBackground(new Color(55, 65, 81));
        progressBar.setForeground(PRIMARY_COLOR);
        centerPanel.add(progressBar, gbc);

        // Add center panel to BorderLayout CENTER
        add(centerPanel, BorderLayout.CENTER);
    }

    private void startAnimation() {
        // ⚡ ULTRA FAST ANIMATION - 50ms for maximum speed
        animationTimer = new Timer(50, e -> { 
            // Super fast dots animation
            dotCount = (dotCount % 3) + 1;
            String dots = ".".repeat(dotCount);
            messageLabel.setText(baseMessage + dots);

            // Super fast spinner rotation with more symbols
            String[] spinners = {"⚡", "🌀", "💫", "🌟", "✨", "🔆"};
            rotationIndex = (rotationIndex + 1) % spinners.length;
            spinnerLabel.setText(spinners[rotationIndex]);
        });
        animationTimer.start();
    }

    /**
     * Change loading message dynamically
     */
    public void setMessage(String message) {
        this.baseMessage = message;
        SwingUtilities.invokeLater(() -> {
            messageLabel.setText(message + "...");
        });
    }

    /**
     * Stop animation - call this when data loading is complete
     */
    public void stopAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }

    /**
     * Complete the loading and show success message briefly
     */
    public void completeLoading() {
        stopAnimation();
        SwingUtilities.invokeLater(() -> {
            spinnerLabel.setText("✅");
            messageLabel.setText("Loading Complete!");
            messageLabel.setForeground(new Color(34, 197, 94)); // Green color for success
            
            // Change subtitle
            JLabel subtitleLabel = (JLabel) ((JPanel) getComponent(0)).getComponent(2);
            subtitleLabel.setText("Data loaded successfully");
            subtitleLabel.setForeground(new Color(34, 197, 94));
        });
    }

    /**
     * Show error state
     */
    public void showError(String errorMessage) {
        stopAnimation();
        SwingUtilities.invokeLater(() -> {
            spinnerLabel.setText("❌");
            messageLabel.setText("Loading Failed");
            messageLabel.setForeground(new Color(239, 68, 68)); // Red color for error
            
            // Change subtitle
            JLabel subtitleLabel = (JLabel) ((JPanel) getComponent(0)).getComponent(2);
            subtitleLabel.setText(errorMessage);
            subtitleLabel.setForeground(new Color(239, 68, 68));
        });
    }

    @Override
    public void removeNotify() {
        stopAnimation();
        super.removeNotify();
    }

    @Override
    public Dimension getPreferredSize() {
        // Ensure the panel takes full available space
        return new Dimension(1257, 686);
    }
}