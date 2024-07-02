package Tetris;

import java.awt.*;
import java.util.Arrays;

public class GameMode {
    private enum Figures {
        J (new Point[] {
                new Point(1, 1),
                new Point(0, 0),
                new Point(0, 1),
                new Point(2, 1)
        }, Color.CYAN),
        L (new Point[] {
                new Point(1, 1),
                new Point(0, 1),
                new Point(2, 0),
                new Point(2, 1)
        }, Color.ORANGE),
        O (new Point[] {
                new Point(0, 0),
                new Point(0, 1),
                new Point(1, 0),
                new Point(1, 1)
        }, Color.RED),
        S (new Point[] {
                new Point(1, 1),
                new Point(1, 0),
                new Point(2, 0),
                new Point(0, 1)

        }, new Color(255,53,105)),
        Z (new Point[] {
                new Point(1, 1),
                new Point(0, 0),
                new Point(1, 0),
                new Point(2, 1)
        }, Color.PINK),
        I (new Point[] {
                new Point(1, 0),
                new Point(0, 0),
                new Point(2, 0),
                new Point(3, 0)
        }, Color.GREEN),
        T (new Point[] {
                new Point(1, 1),
                new Point(0, 1),
                new Point(2, 1),
                new Point(1, 0)
        }, Color.BLUE);
        final Point[] states;
        final Color color;
        Figures(Point[] states, Color color) {
            this.states = states;
            this.color = color;
        }
    }
    public enum Difficult {
        EASY(100),
        NORMAL(200),
        HARD(300),
        IMPOSSIBLE(370);

        final int val;
        Difficult(int val) {
            this.val = val;
        }
    }

    private final int POINTS_PER_LINE = 100;
    private final int POINTS_FOR_TETRIS = 1000;
    private final int SPEED_FROM_SCORE = 1000; // Скорость увеличивается от счёта игрока, эт коэффициент перевода
    private int CURRENT_DIFFICULT = 10;
    private final int START_DELAY = 500;
    private final int FIELD_HEIGHT;
    private final int FIELD_WIDTH;

    private final int INPUT_MOVE_DELAY = 100;
    private final int INPUT_ROTATE_DELAY = 200;
    private Figure fallingFigure;
    private Figure nextFigure;
    private Color[][] field;
    private int speed;
    private int score;

    private boolean isSpeedIncreased = false;

    private final GameUI ui;
    private boolean gameOver;
    private boolean isPaused;

    public boolean isRotateRight = false;
    public boolean isRotateLeft = false;
    public boolean isMoveLeft = false;
    public boolean isMoveRight = false;

    private long lastTimeMovedRight = Long.MAX_VALUE;
    private long lastTimeMovedLeft = Long.MAX_VALUE;
    private long lastTimeRotatedRight = Long.MAX_VALUE;
    private long lastTimeRotatedLeft = Long.MAX_VALUE;

    public GameMode(int FIELD_HEIGHT,int FIELD_WIDTH) {
        GameController controller = new GameController(this);
        ui = new GameUI(this,controller);
        this.FIELD_WIDTH = FIELD_WIDTH;
        this.FIELD_HEIGHT = FIELD_HEIGHT;
        score = 0;
        speed = 1;
        startNewRound();
        new Thread(this::playGame).start();
        //new Thread(() -> ).start();
    }

    public void startNewRound() {
        gameOver = false;
        isPaused = true;
        score = 0;
        field = new Color[FIELD_HEIGHT][FIELD_WIDTH];
        nextFigure = genNextFigure(FIELD_WIDTH /2,0);
        changeFigure();
    }


    private Figure genNextFigure(int x, int y) {
        Figures[] figs = Figures.values();
        int index = (int) (Math.random() * figs.length);
        return new Figure(x,y,figs[index].states,figs[index].color);
    }

    private void changeFigure() {
        Figure buff = nextFigure;
        nextFigure = genNextFigure(0,0);
        buff.moveTo(FIELD_WIDTH/2,0);
        if (CanSpawnFallingFigure(buff))
            fallingFigure = buff;
        else
            fallingFigure = null;
        updateVisualNextFigure();
    }

    private boolean CanSpawnFallingFigure(Figure in) {
        Point[] cords = in.getBlockCords();
        for (Point cord : cords)
            if (field[cord.y][cord.x] != null)
                return false;
        return true;
    }

    public void playGame()  {
        long prevUpdate = System.currentTimeMillis();
        while (true) {
            while (!gameOver) {
                if (isPaused) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ignore) {}
                    continue;
                }
                if (System.currentTimeMillis() - prevUpdate > START_DELAY - speed) {
                    prevUpdate = System.currentTimeMillis();
                    update();
                    updateVisualField();
                }
                checkInputs();
            }
            ui.gameOver(score);
            isPaused = true;
            startNewRound();
        }
    }

    private void checkInputs() {
        //while (true) {
            long curTime = System.currentTimeMillis();
            if (isMoveLeft && !isMoveRight && curTime - lastTimeMovedLeft > INPUT_MOVE_DELAY) {
                moveFigureLeft();
                lastTimeMovedLeft = curTime;
            }
            if (isMoveRight && !isMoveLeft && curTime - lastTimeMovedRight > INPUT_MOVE_DELAY) {
                moveFigureRight();
                lastTimeMovedRight = curTime;
            }
            if (isRotateLeft && !isRotateRight && curTime - lastTimeRotatedLeft > INPUT_ROTATE_DELAY) {
                rotateFigureLeft();
                lastTimeRotatedLeft = curTime;
            }
            if (isRotateRight && !isRotateLeft && curTime - lastTimeRotatedRight > INPUT_ROTATE_DELAY) {
                rotateFigureRight();
                lastTimeRotatedRight = curTime;
            }
        //}
    }


    private void updateVisualField() {
        Color[][] visualField = new Color[field.length][FIELD_WIDTH];
        for (int i = 0; i < field.length; i++)
            visualField[i] = Arrays.stream(field[i]).toArray(Color[]::new);
        if (fallingFigure != null) {
            Point[] cords = fallingFigure.getBlockCords();
            Color cFigure = fallingFigure.color;
            for (Point cord : cords)
                visualField[cord.y][cord.x] = cFigure;
        }
        ui.updateGameField(visualField);
    }

    private void updateVisualNextFigure() {
        Point[] cords = nextFigure.getBlockCords();
        Color[][] visualNextFigure = new Color[2][4];
        for (Point cord : cords)
            visualNextFigure[cord.y][cord.x] = nextFigure.color;
        ui.updateNextFigure(visualNextFigure);
    }

    private void update() {
        if (!moveFigureDown()) {
            Point[] cords = fallingFigure.getBlockCords();
            Color fColor = fallingFigure.color;
            for (Point cord : cords)
                field[cord.y][cord.x] = fColor;
            checkLines();
            changeFigure();
            if (fallingFigure == null)
                gameOver = true;
        }
    }

    public void changeDifficult(Difficult diff) {
        CURRENT_DIFFICULT = diff.val;
        speed = score / SPEED_FROM_SCORE + CURRENT_DIFFICULT;
    }

    // НАЧИНАТЬ С КОНЦА
    // ЕСЛИ ВСЯ ЛИНИЯ ПУСТА - ОФАЕМ
    private void checkLines() {
        int index = -1;
        int range = 0;

        for (int i = 0; i < field.length; i++) {
            int j;
            for (j = 0; j < field[i].length; j++) {
                if (field[i][j] == null)
                    break;
            }
            if (j == field[i].length) {
                if (index == -1)
                    index = i;
                range++;
            }
        }
        if (index != -1)
            deleteLines(index,range);
        updateScore(range);

    }

    private void updateScore(int deletedLines) {
        if (deletedLines != 0) {
            if (deletedLines == 4)
                score += POINTS_FOR_TETRIS;
            else
                score += POINTS_PER_LINE * deletedLines;
            speed = score / SPEED_FROM_SCORE + CURRENT_DIFFICULT;
            ui.updateScore(score);
        }
    }

    private void deleteLines(int index, int range) {
        if (index-1 < range)
            for (int i = index; i < index + range; i++) {
                field[i] = new Color[field[i].length];
            }
        for (int i = index-1; i >= 0; i--) {
            System.arraycopy(field[i], 0, field[i + range], 0, field[i].length);
        }
    }
    public void speedUpFalling(boolean isPressed) {
        if (isPressed && !isSpeedIncreased) {
            speed = START_DELAY - 20;
            isSpeedIncreased = true;
        } else if (!isPressed && isSpeedIncreased) {
            speed = score / SPEED_FROM_SCORE + CURRENT_DIFFICULT;
            isSpeedIncreased = false;
        }
    }

    public void changePauseState() {
        isPaused = !isPaused;
        if (isPaused)
            ui.pauseGame();
        else
            ui.startGame();
    }

    public void doMoveFigureLeft(boolean in) {
        if (isMoveLeft == in)
            return;
        isMoveLeft = in;
        if (in)
            lastTimeMovedLeft = 0;
        else
            lastTimeMovedLeft = Long.MAX_VALUE;
    }

    public void doMoveFigureRight(boolean in) {
        if (isMoveRight == in)
            return;
        isMoveRight = in;
        if (in)
            lastTimeMovedRight = 0;
        else
            lastTimeMovedRight = Long.MAX_VALUE;

    }

    public void doRotateFigureRight(boolean in) {
        if (isRotateRight == in)
            return;
        isRotateRight = in;
        if (in)
            lastTimeRotatedRight = 0;
        else
            lastTimeRotatedRight = Long.MAX_VALUE;

    }

    public void doRotateFigureLeft(boolean in) {
        if (isRotateLeft == in)
            return;
        isRotateLeft = in;
        if (in)
            lastTimeRotatedLeft = 0;
        else
            lastTimeRotatedLeft = Long.MAX_VALUE;

    }

    private boolean moveFigureDown() {
        Point[] figureBlocks =  fallingFigure.getBlockCords();
        for (Point block : figureBlocks) {
            if (block.y + 1 >= field.length ||
                    field[block.y+1][block.x] != null)
                return false;
        }
        fallingFigure.moveDown();
        return true;
    }

    public void moveFigureRight() {
        if (fallingFigure == null || isPaused)
            return;
        Point[] figureBlocks =  fallingFigure.getBlockCords();
        for (Point block : figureBlocks) {
            if (block.x + 1 >= field[block.y].length ||
                    field[block.y][block.x+1] != null)
                return;
        }
        fallingFigure.moveRight();
        updateVisualField();
    }

    public void moveFigureLeft() {
        if (fallingFigure == null || isPaused)
            return;
        Point[] figureBlocks =  fallingFigure.getBlockCords();
        for (Point block : figureBlocks) {
            if (block.x - 1 < 0 ||
                    field[block.y][block.x-1] != null)
                return;
        }
        fallingFigure.moveLeft();
        updateVisualField();
    }


    private boolean rotateCheck() {
        Point[] cords = fallingFigure.getBlockCords();
        boolean needToBeShifted = false;
        boolean canBePlaced = true;
        int shift = 0;
        for (Point cord : cords) {
            if (cord.x >= FIELD_WIDTH || cord.x < 0 ) {
                shift = cord.x < 0 ? -cord.x : -(cord.x - FIELD_WIDTH + 1);
                needToBeShifted = true;
            } else if (!needToBeShifted && (cord.y >= FIELD_HEIGHT || cord.y < 0 || field[cord.y][cord.x] != null))
                canBePlaced = false;
        }
        if (!needToBeShifted)
            return canBePlaced;

        fallingFigure.moveOn(shift,0);
        cords = fallingFigure.getBlockCords();
        for (Point cord : cords)
            if (field[cord.y][cord.x] != null) {
                fallingFigure.moveOn(-shift,0);
                return false;
            }
        return true;
    }

    public void rotateFigureRight() {
        if (fallingFigure == null || isPaused)
            return;
        fallingFigure.rotateRight();
        if (rotateCheck())
            updateVisualField();
        else
            fallingFigure.rotateLeft();
    }

    public void rotateFigureLeft() {
        if (fallingFigure == null || isPaused)
            return;
        fallingFigure.rotateLeft();
        if (rotateCheck())
            updateVisualField();
        else
            fallingFigure.rotateRight();
    }
}
