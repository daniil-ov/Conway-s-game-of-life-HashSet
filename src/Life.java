import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import static edu.princeton.cs.introcs.StdDraw.filledSquare;

public class Life {

    //начальные границы поля
    static int x1 = -20;
    static int x2 = 20;
    static int y1 = -20;
    static int y2 = 20;

    //коэфициенты для перемещения границ поля, в зависимости от масштаба
    static int k = 20;
    static int v = 20;

    static HashSet<Cell> currentLife = new HashSet<>();
    static HashSet<Cell> nextGeneration = new HashSet<>();
    static HashSet<Cell> checkRevival = new HashSet<>();

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(600, 600);
        StdDraw.setPenColor(Color.BLACK);

        createField();
        runLife();
    }

    private static void createField() {

        //глайдер
        currentLife.add(new Cell(0, 0));
        currentLife.add(new Cell(0, 1));
        currentLife.add(new Cell(0, 2));
        currentLife.add(new Cell(1, 2));
        currentLife.add(new Cell(2, 1));
    }

    private static void runLife() {

        //вывод в консоль начальных точек
        Iterator<Cell> i = currentLife.iterator();
        while (i.hasNext()) {
            System.out.println(i.next().toString());
        }

        while (true) {

            //задержка 0.5 сек
            try {

                Thread.sleep(500);

            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }

            drawCurrentLife();
            change();
            moveField();
        }
    }

    private static void moveField() {

        if (StdDraw.isKeyPressed(107) || StdDraw.isKeyPressed(187)) {

            x1 /= 2;
            x2 /= 2;
            y1 /= 2;
            y2 /= 2;
            k /= 2;
            v /= 2;
        }

        if ((StdDraw.isKeyPressed(109) || StdDraw.isKeyPressed(189)) && (x2 < 10e7)) {

            x1 *= 2;
            x2 *= 2;
            y1 *= 2;
            y2 *= 2;
            k *= 2;
            v *= 2;
        }

        if (StdDraw.isKeyPressed(40) || StdDraw.isKeyPressed(83)) {

            y1 -= v;
            y2 -= v;
        }
        if (StdDraw.isKeyPressed(38) || StdDraw.isKeyPressed(87)) {

            y1 += v;
            y2 += v;
        }

        if (StdDraw.isKeyPressed(39) || StdDraw.isKeyPressed(68)) {

            x1 += k;
            x2 += k;
        }

        if (StdDraw.isKeyPressed(37) || StdDraw.isKeyPressed(65)) {

            x1 -= k;
            x2 -= k;
        }
    }

    private static void drawCurrentLife() {

        StdDraw.clear();

        StdDraw.setXscale(x1, x2);
        StdDraw.setYscale(y1, y2);

        for (Cell c : currentLife) {

            filledSquare(c.x, c.y, 0.5);
            StdDraw.show();
        }
    }

    private static int countingNeighbours(Cell c) {

        int cnt = 0;

        Cell[] neighboursCell = Cell.neighbours(c);
        for (Cell nbCell : neighboursCell) {

            if (currentLife.contains(nbCell)) {
                cnt++;
            } else {
                checkRevival.add(nbCell);
            }
        }

        return cnt;
    }

    private static int countingNeighboursRevival(Cell c) {

        int cnt = 0;

        Cell[] neighboursCell = Cell.neighbours(c);
        for (Cell nbCell : neighboursCell) {

            if (currentLife.contains(nbCell)) {
                cnt++;
            }
        }

        return cnt;
    }

    private static void change() {

        HashSet<Cell> tmp;

        // проверка живых клеток
        for (Cell c : currentLife) {

            int nb = countingNeighbours(c);

            if (((nb == 2) || (nb == 3))) {
                nextGeneration.add(c);
            }
        }

        //проверка клеток, которые могут возродиться
        for (Cell c : checkRevival) {

            if (countingNeighboursRevival(c) == 3) {
                nextGeneration.add(c);
            }
        }

        currentLife.clear();
        tmp = currentLife;
        currentLife = nextGeneration;
        nextGeneration = tmp;
    }
}