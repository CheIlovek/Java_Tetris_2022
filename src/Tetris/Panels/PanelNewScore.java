package Tetris.Panels;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class PanelNewScore extends JPanel {

    private final JLabel scoreLabel;
    public JButton confirmButton;
    private final JTextField nicknameField;
    private int score;

    public PanelNewScore() {
        Box box = new Box(BoxLayout.Y_AXIS);

        scoreLabel =  Stylization.getLabel("");
        confirmButton = Stylization.getButton("CONFIRM");
        nicknameField = new JTextField();

        AbstractDocument doc = (AbstractDocument) nicknameField.getDocument();
        doc.setDocumentFilter(new Filter());

        box.add(Box.createVerticalStrut(200));
        box.add(scoreLabel);
        box.add(Box.createVerticalStrut(20));
        box.add(nicknameField);
        box.add(Box.createVerticalStrut(20));
        box.add(confirmButton);
        add(box);
    }

    public String getName() {
        return nicknameField.getText();
    }

    public int getScore() {
        return score;
    }
    public void changeScore(int score) {
        scoreLabel.setText("YOUR SCORE: " + score);
        this.score = score;
    }

    private class Filter extends DocumentFilter {

        @Override
        public void replace(FilterBypass fb, int offs, int length,
        String str, AttributeSet a) throws BadLocationException {
            super.replace(fb, offs, length,
                    str.replaceAll("[^0-9a-zA-Z_]+", ""), a);
        }
        @Override
        public void insertString(FilterBypass fb, int offs, String str,
                AttributeSet a) throws BadLocationException {
            super.insertString(fb, offs,
                    str.replaceAll("[^0-9a-zA-Z_]+", ""), a);
        }
    }
}

