package Tetris.Panels;

import Tetris.GameMode;

import javax.swing.*;

public class PanelDifficult extends JPanel  {

    public JButton[] diffButtons;
    public JButton backButton;
    public PanelDifficult() {
        GameMode.Difficult[] diffs = GameMode.Difficult.values();
        int len = diffs.length;
        diffButtons = new JButton[len];
        for (int i = 0; i < len; i++) {
            diffButtons[i] = Stylization.getButton(diffs[i].toString());
        }

        Box box = new Box(BoxLayout.Y_AXIS);
        box.add(Box.createVerticalStrut(150));
        for (JButton diffButton : diffButtons) {
            box.add(Box.createVerticalStrut(20));
            box.add(diffButton);
        }

        backButton = Stylization.getButton("BACK");

        box.add(Box.createVerticalStrut(50));
        box.add(backButton);
        add(box);
    }


}
