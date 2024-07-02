package Tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class GameController implements KeyListener {
    final GameMode gameMode;

    public GameController(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> gameMode.doMoveFigureRight(true);
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> gameMode.doMoveFigureLeft(true);
            case KeyEvent.VK_UP, KeyEvent.VK_E -> gameMode.doRotateFigureRight(true);
            case KeyEvent.VK_Q -> gameMode.doRotateFigureLeft(true);
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> gameMode.speedUpFalling(true);
            case KeyEvent.VK_ESCAPE ->  gameMode.changePauseState();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> gameMode.doMoveFigureRight(false);
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> gameMode.doMoveFigureLeft(false);
            case KeyEvent.VK_UP, KeyEvent.VK_E -> gameMode.doRotateFigureRight(false);
            case KeyEvent.VK_Q -> gameMode.doRotateFigureLeft(false);
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> gameMode.speedUpFalling(false);
        }
    }
}
