package Tetris.Panels;

import Tetris.ScoreTable;

import javax.swing.*;

public class PanelScore extends JPanel {
    public JButton backButton;
    private Box layout;

    public PanelScore(JLabel[] leaders) {
        backButton = Stylization.getButton("BACK");
        updateScore(leaders);
    }

    public void updateScore(JLabel[] leaders) {
        if (layout != null)
            remove(layout);
        layout = new Box(BoxLayout.Y_AXIS);
        layout.add(Box.createVerticalStrut(50));
        int len = Math.min(leaders.length, 15);
        for (int i = 0; i < len; i++) {
            layout.add(leaders[i]);
            layout.add(Box.createVerticalStrut(10));
        }
        layout.add(backButton);
        add(layout);
    }
}
