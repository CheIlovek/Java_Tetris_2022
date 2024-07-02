package Tetris.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

public class PanelGame extends JPanel {

    static final int TILE = 32;
    private boolean isPaused = false;
    private JLabel score;
    FieldRender gameField;
    FieldRender nextFigure;

    public PanelGame(KeyListener keyListener) {
        setLayout(new BorderLayout());

        setFocusable(true);
        addKeyListener(keyListener);
        gameField = getFieldRender(10,20);
        Box generalLayout = new Box(BoxLayout.X_AXIS);
        JPanel rightLayout = getRightPart();

        generalLayout.add(gameField);
        generalLayout.add(rightLayout);
        generalLayout.add(Box.createHorizontalStrut(0));
        generalLayout.setBackground(Color.CYAN);

        add(generalLayout,BorderLayout.CENTER);
        setBounds(0,0,TILE*(10+4)+2,TILE*20+2);

    }

    public void setScore(int scoreVal) {
        score.setText("SCORE: " + scoreVal);
    }

    private JPanel getRightPart() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(new Color(0,0,0,0));
        panel.setMinimumSize(new Dimension(TILE*4,1000));
        panel.setMaximumSize(new Dimension(TILE*4,1000));
        JLabel figureLabel = getLabel("Next figure:");
        score = getLabel("SCORE: ");
        nextFigure = getFieldRender(4,2);
        panel.add(figureLabel);
        panel.add(nextFigure);
        panel.add(score);
        return panel;
    }

    private JLabel getLabel(String text) {
        JLabel label = Stylization.getLabel(text);
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(JLabel.LEFT);
        label.setHorizontalTextPosition(JLabel.LEFT);
        return label;
    }

    public void setPause(boolean in) {
        isPaused = in;
        gameField.isPaused = in;
        nextFigure.isPaused = in;
        repaint();
    }

    public void updateField(Color[][] field) {
        gameField.field = field;
        gameField.repaint();
    }

    public void updateNextFigure(Color[][] field) {
        nextFigure.field = field;
        nextFigure.repaint();
    }

    public FieldRender getFieldRender(int x, int y) {
        FieldRender fr = new FieldRender();
        fr.setMinimumSize(new Dimension(TILE*x+1,TILE*y+2));
        fr.setMaximumSize(new Dimension(TILE*x+1,TILE*y+2));
        fr.setPreferredSize(new Dimension(TILE*x+1,TILE*y+2));
        fr.field = new Color[y][x];
        return fr;
    }
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (isPaused)
            g2d.setColor(new Color(0, 0, 0, 200));
        else
            g2d.setColor(Color.WHITE);

        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    class FieldRender extends JPanel {
        public Color[][] field = new Color[0][0];
        public boolean isPaused = true;

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);
            for (int i = 0; i < field.length; i++)
                for (int j = 0; j < field[i].length; j++) {
                    Rectangle2D rect = new Rectangle2D.Double(j * TILE, i * TILE, TILE, TILE);
                    if (field[i][j] != null) {
                        Color c = isPaused ? field[i][j].darker().darker() : field[i][j];
                        g2d.setColor(c);
                        g2d.fill(rect);
                        g2d.setColor(Color.BLACK);
                    }
                    g2d.draw(rect);
                }
        }
    }

}
