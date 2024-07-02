package Tetris.Panels;

import Tetris.GameMode;

import javax.swing.*;
public class PanelMenu extends JPanel {

    public JButton playButton;
    public JButton diffButton;
    public JButton quitButton;
    public JButton scoreButton;
    public JButton aboutButton;
    public JLabel curDiff;

    private boolean isInProgress = false;
    public PanelMenu() {
        Box box = new Box(BoxLayout.Y_AXIS);

        playButton =    Stylization.getButton("PLAY");
        diffButton =    Stylization.getButton("CHANGE DIFFICULT");
        quitButton =    Stylization.getButton("EXIT");
        scoreButton =   Stylization.getButton("SCORE TABLE");
        aboutButton =   Stylization.getButton("ABOUT");
        curDiff =       Stylization.getLabel("DIFFICULT: EASY");

        box.add(Box.createVerticalStrut(180));
        box.add(playButton);
        box.add(curDiff);
        box.add(Box.createVerticalStrut(20));
        box.add(diffButton);
        box.add(Box.createVerticalStrut(20));
        box.add(scoreButton);
        box.add(Box.createVerticalStrut(20));
        box.add(aboutButton);
        box.add(Box.createVerticalStrut(20));
        box.add(quitButton);
        add(box);
    }
    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean state) {
        if (state == isInProgress)
            return;
        if (state)
            Stylization.lockButton(diffButton);
        else
            Stylization.unlockButton(diffButton);
        isInProgress = state;
    }

    public void setDifficult(GameMode.Difficult diff) {
        curDiff.setText("DIFFICULT: " + diff.toString());
    }
}
