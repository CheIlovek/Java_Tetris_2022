package Tetris;

import Tetris.Panels.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

class GameUI {

    private PanelMenu panelMenu;
    private PanelGame panelGame;
    private PanelDifficult panelDifficult;
    private PanelScore panelScore;
    private PanelNewScore panelNewScore;
    private PanelAbout panelAbout;

    private ScoreTable scoreTable;

    private JFrame frame;
    final GameMode gameMode;

    public GameUI(GameMode gm, KeyListener listener) {
        gameMode = gm;
        init(listener);
    }
    public void init(KeyListener listener) {
        scoreTable = new ScoreTable("D:\\Bonch\\3 year\\JAVA\\lab7\\out\\artifacts\\lab7_jar\\Scoreboard.txt");
        panelGame =      new PanelGame(listener);
        Rectangle size = panelGame.getBounds();
        int h = size.height + 38;
        int w = size.width + 16;
        frame = getFrame(w,h);

        panelMenu =      getMenuPanel();
        panelDifficult = getDiffPanel();
        panelScore =     getScorePanel();
        panelNewScore =  getPanelNewScore();
        panelAbout = getPanelAbout();
        JLayeredPane lp = new JLayeredPane();

        lp.setBounds(size);

        lp.add(panelGame, JLayeredPane.DEFAULT_LAYER);
        lp.add(panelMenu, JLayeredPane.PALETTE_LAYER);
        lp.add(panelDifficult, JLayeredPane.PALETTE_LAYER);
        lp.add(panelScore, JLayeredPane.PALETTE_LAYER);
        lp.add(panelNewScore, JLayeredPane.PALETTE_LAYER);
        lp.add(panelAbout, JLayeredPane.PALETTE_LAYER);
        frame.add(lp);
        panelGame.setPause(true);
        frame.revalidate();
    }


    private PanelMenu getMenuPanel() {
        PanelMenu panel = new PanelMenu();
        int w = frame.getWidth();
        int h = frame.getHeight();
        panel.setBounds(0,0,w,h);
        panel.setOpaque(false);
        panel.quitButton.addActionListener(e ->
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
        panel.playButton.addActionListener(e ->
            gameMode.changePauseState());
        panel.diffButton.addActionListener(e -> {
            if (!panel.isInProgress())
                changeWindows(panelMenu, panelDifficult);
        });
        panel.scoreButton.addActionListener(e ->
            changeWindows(panelMenu, panelScore));
        panel.aboutButton.addActionListener(e ->
            changeWindows(panelMenu, panelAbout));
        return panel;
    }



    private PanelDifficult getDiffPanel() {
        PanelDifficult panel = new PanelDifficult();
        panel.setVisible(false);
        panel.setOpaque(false);
        int w = frame.getWidth();
        int h = frame.getHeight();
        panel.setBounds(0,0,w,h);

        GameMode.Difficult[] diffs = GameMode.Difficult.values();
        JButton[] buttons = panel.diffButtons;
        for (int i = 0; i < buttons.length; i++) {
            int finalI = i;
            buttons[i].addActionListener(e -> {
                gameMode.changeDifficult(diffs[finalI]);
                panelMenu.setDifficult(diffs[finalI]);
                panel.backButton.doClick();
            });
        }

        panel.backButton.addActionListener(e -> changeWindows(panelDifficult, panelMenu));

        return panel;
    }

    private PanelScore getScorePanel() {
        PanelScore panel = new PanelScore(scoreTable.getLeaders());
        panel.setVisible(false);
        panel.setOpaque(false);
        int w = frame.getWidth();
        int h = frame.getHeight();
        panel.setBounds(0,0,w,h);
        panel.backButton.addActionListener(e -> changeWindows(panelScore, panelMenu));
        return panel;
    }

    private PanelNewScore getPanelNewScore() {
        PanelNewScore panel = new PanelNewScore();
        panel.setVisible(false);
        panel.setOpaque(false);
        int w = frame.getWidth();
        int h = frame.getHeight();
        panel.setBounds(0,0,w,h);
        panel.confirmButton.addActionListener(e -> {
            String name = panel.getName();
            int score = panel.getScore();
            if (!name.equals("")) {
                scoreTable.addNewScore(name, score);
                this.panelScore.updateScore(scoreTable.getLeaders());
            }
            changeWindows(panelNewScore, panelMenu);

        });
        return panel;
    }

    private PanelAbout getPanelAbout() {
        PanelAbout panel = new PanelAbout();
        panel.setVisible(false);
        panel.setOpaque(false);
        int w = frame.getWidth();
        int h = frame.getHeight();
        panel.setBounds(0,0,w,h);
        panel.backButton.addActionListener(e -> changeWindows(panelAbout, panelMenu));
        return panel;
    }


    private void changeWindows(JPanel prev, JPanel next) {
        prev.setVisible(false);
        next.setVisible(true);
    }

    public void updateGameField(Color[][] field) {
        panelGame.updateField(field);
        frame.repaint();
    }

    public void updateNextFigure(Color[][] field) {
        panelGame.updateNextFigure(field);
        frame.repaint();
    }

    public void startGame() {
        panelMenu.setVisible(false);
        panelGame.setPause(false);
        panelMenu.setInProgress(true);
    }

    public void pauseGame() {
        panelMenu.setVisible(true);
        panelGame.setPause(true);
    }

    static JFrame getFrame(int w, int h) {
        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        frame.setBounds(
                dim.width/2 - w/2, dim.height/2 - h/2,
                w, h);
        frame.setTitle("TETRIS");
        return frame;
    }

    public void updateScore(int score) {
        panelGame.setScore(score);
    }


    public void gameOver(int score) {
        panelGame.setScore(0);
        panelGame.setPause(true);
        panelMenu.setInProgress(false);
        panelNewScore.changeScore(score);
        panelNewScore.setVisible(true);

    }
}
