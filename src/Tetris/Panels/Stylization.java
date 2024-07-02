package Tetris.Panels;

import javax.swing.*;
import java.awt.*;

public class Stylization {

    static Font font = new Font("Lucida Sans Unicode",Font.BOLD,14);
    public static JButton getButton(String label) {
        JButton button = new JButton(label);
        button.setFont(font);
        button.setBackground(new Color(0x78DCB1));
        button.setForeground(new Color(0x083B28));
        button.setMinimumSize(new Dimension(200,40));
        button.setMaximumSize(new Dimension(200,40));
        return button;
    }

    public static JLabel getLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setMinimumSize(new Dimension(200,50));
        label.setMaximumSize(new Dimension(200,50));

        return label;
    }

    public static JTextArea getTextArea(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(font);
        textArea.setEditable(false);
        textArea.setBackground(new Color(0,0,0,0));
        textArea.setForeground(Color.WHITE);
        return textArea;
    }

    public static void lockButton(JButton button) {
        button.setBackground(button.getBackground().darker());
        button.setForeground(button.getForeground().darker());
    }

    public static void unlockButton(JButton button) {
        button.setBackground(button.getBackground().brighter());
        button.setForeground(button.getForeground().brighter());
    }
}
