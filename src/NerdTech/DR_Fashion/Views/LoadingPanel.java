package NerdTech.DR_Fashion.Views;

import javax.swing.*;
import java.awt.*;

/**
 * Reusable Loading Panel with animated spinner Shows "Connecting..." or custom
 * message while data loads Always centered in parent container
 */
public class LoadingPanel extends JPanel {

    private JLabel messageLabel;
    private JLabel spinnerLabel;
    private Timer animationTimer;
    private int dotCount = 0;
    private String baseMessage;
    private int rotationIndex = 0;

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
        setBackground(new Color(50, 50, 50));  // FlatMacDarkLaf Panel background

        // Create a center panel with GridBagLayout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);

        // Animated spinner icon
        spinnerLabel = new JLabel("⟳");
        spinnerLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        spinnerLabel.setForeground(new Color(59, 130, 246));
        spinnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(spinnerLabel, gbc);

        // Message label with animated dots
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        messageLabel = new JLabel(baseMessage + "...", SwingConstants.CENTER);
        messageLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        messageLabel.setForeground(new Color(71, 85, 105));
        messageLabel.setPreferredSize(new Dimension(300, 30)); // Fixed width for stable centering
        centerPanel.add(messageLabel, gbc);

        // Subtitle
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 0, 0);
        JLabel subtitleLabel = new JLabel("Please wait", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(148, 163, 184));
        centerPanel.add(subtitleLabel, gbc);

        // Add center panel to BorderLayout CENTER
        add(centerPanel, BorderLayout.CENTER);
    }

    private void startAnimation() {
        // Rotate spinner and animate dots
        animationTimer = new Timer(300, e -> {
            // Animate dots (1 to 3 dots)
            dotCount = (dotCount % 3) + 1;
            String dots = ".".repeat(dotCount);
            String spaces = " ".repeat(3 - dotCount);
            messageLabel.setText(baseMessage + dots + spaces);

            // Rotate spinner with more symbols for smoother animation
            String[] spinners = {"⟳", "⟲", "⟳", "⟲"};
            rotationIndex = (rotationIndex + 1) % spinners.length;
            spinnerLabel.setText(spinners[rotationIndex]);
        });
        animationTimer.start();
    }

    public void setMessage(String message) {
        this.baseMessage = message;
        SwingUtilities.invokeLater(() -> {
            messageLabel.setText(message + "...");
        });
    }

    public void stopAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
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
