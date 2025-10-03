package NerdTech.DR_Fashion.Views;

import javax.swing.*;
import java.awt.*;

/**
 * Reusable Loading Panel with animated spinner Shows "Connecting..." or custom
 * message while data loads
 */
public class LoadingPanel extends JPanel {

    private JLabel messageLabel;
    private JLabel spinnerLabel;
    private Timer animationTimer;
    private int dotCount = 0;
    private String baseMessage;

    public LoadingPanel() {
        this("Loading");
    }

    public LoadingPanel(String message) {
        this.baseMessage = message;
        initComponents();
        startAnimation();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);

        // Animated spinner icon
        spinnerLabel = new JLabel("⟳");
        spinnerLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        spinnerLabel.setForeground(new Color(59, 130, 246));
        add(spinnerLabel, gbc);

        // Message label with animated dots
        gbc.gridy = 1;
        messageLabel = new JLabel(baseMessage + "...");
        messageLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        messageLabel.setForeground(new Color(71, 85, 105));
        add(messageLabel, gbc);

        // Subtitle
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        JLabel subtitleLabel = new JLabel("Please wait");
        subtitleLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(148, 163, 184));
        add(subtitleLabel, gbc);
    }

    private void startAnimation() {
        // Rotate spinner and animate dots
        animationTimer = new Timer(300, e -> {
            // Animate dots
            dotCount = (dotCount + 1) % 4;
            String dots = ".".repeat(dotCount == 0 ? 3 : dotCount);
            messageLabel.setText(baseMessage + dots + " ".repeat(3 - dots.length()));

            // Rotate spinner emoji (simulate rotation with different symbols)
            String[] spinners = {"⟳", "⟲", "⟳", "⟲"};
            spinnerLabel.setText(spinners[dotCount]);
        });
        animationTimer.start();
    }

    public void setMessage(String message) {
        this.baseMessage = message;
        messageLabel.setText(message + "...");
    }

    public void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        stopAnimation();
    }
}
