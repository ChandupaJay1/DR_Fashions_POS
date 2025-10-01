package NerdTech.DR_Fashion.Views.DashboardP;

import java.awt.*;
import javax.swing.*;

public class CardPanel extends JPanel {
    private Color backgroundColor;

    public CardPanel(Color bgColor) {
        this.backgroundColor = bgColor;
        setOpaque(false);
        setPreferredSize(new Dimension(220, 200)); // adjust as needed
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // rounded corners
        super.paintComponent(g);
    }
}
