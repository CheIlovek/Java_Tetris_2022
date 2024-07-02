package Tetris.Panels;

import javax.swing.*;

public class PanelAbout extends JPanel {

    public JButton backButton;
    public PanelAbout() {

        String text = """
                Created by: Sanya
                
                Contacts: example@mail.ru
                
                """;
        JTextArea label = Stylization.getTextArea(text);
        backButton = Stylization.getButton("BACK");
        Box box = new Box(BoxLayout.Y_AXIS);
        box.add(Box.createVerticalStrut(150));
        box.add(label);
        box.add(Box.createVerticalStrut(20));
        box.add(backButton);
        add(box);
    }
}
