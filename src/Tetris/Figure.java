package Tetris;

import java.awt.*;

class Figure {
    private final Point globalCords;
    public Color color;
    private final Point[] figureShape;

    public Figure(int x, int y, Point[] figureShape, Color c) {
        this.figureShape = new Point[figureShape.length];
        for (int i = 0; i < figureShape.length; i++) {
            this.figureShape[i] = (Point) figureShape[i].clone();
        }
        globalCords = new Point(x,y);
        color = c;
    }

    public Point[] getBlockCords() {
        Point[] blockCords = new Point[figureShape.length];
        for (int i = 0; i < blockCords.length; i++) {
            blockCords[i] =
                    new Point(figureShape[i].x + globalCords.x,
                              figureShape[i].y + globalCords.y);
        }
        return blockCords;
    }

    public void moveTo(int x, int y) {
        globalCords.x = x;
        globalCords.y = y;
    }

    public void moveOn(int x, int y) {
        globalCords.x += x;
        globalCords.y += y;
    }

    public void moveDown() {
        globalCords.y++;
    }

    public void moveRight() {
        globalCords.x++;
    }

    public void moveLeft() {
        globalCords.x--;
    }

    public void rotateLeft() {
        // Проверка на КУБ
        if (figureShape[0].x == 0 && figureShape[0].y == 0)
            return;

        Point center = figureShape[0];
        for (int i = 1; i < figureShape.length; i++) {
            int buff = center.x + figureShape[i].y - center.y;
            figureShape[i].y = center.y - figureShape[i].x + center.x;
            figureShape[i].x = buff;
        }
    }

    public void rotateRight() {
        // Проверка на КУБ
        if (figureShape[0].x == 0 && figureShape[0].y == 0)
            return;

        Point center = figureShape[0];
        for (int i = 1; i < figureShape.length; i++) {
            int buff = center.x - figureShape[i].y + center.y;
            figureShape[i].y = center.y + figureShape[i].x - center.x;
            figureShape[i].x = buff;

        }
    }

}
