package dovsynnikov;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;

public class Life {

    private static int widthField = 700;
    private static int heightField = 700;

    private static int centerX = 0;
    private static int centerY = 0;

    private static HashSet<Cell> currentLife = new HashSet<>();
    private static HashSet<Cell> nextStepLife = new HashSet<>();
    private static HashSet<Cell> neighborhoodOfCells = new HashSet<>();

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(700, 700);
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

            drawCurrentLife();
            change();
            moveField();

            //задержка 0.5 сек
            StdDraw.pause(500);
        }
    }

    private static void moveField() {

        char key = ' ';

        if (StdDraw.hasNextKeyTyped()) {

            key = StdDraw.nextKeyTyped();
        }

        if (widthField > 2 && widthField < 16000000) {

            switch (key) {

                case '-':
                    widthField *= 2;
                    heightField *= 2;
                    break;

                case '+':
                    widthField /= 2;
                    heightField /= 2;
                    break;

                case 's':
                    centerY -= heightField / 3;
                    break;

                case 'w':
                    centerY += heightField / 3;
                    break;

                case 'a':
                    centerX -= widthField / 3;
                    break;

                case 'd':
                    centerX += widthField / 3;
                    break;
            }
        }
    }


    private static void drawCurrentLife() {

        StdDraw.clear();

        StdDraw.setXscale(centerX - (widthField / 2), centerX + (widthField / 2));
        StdDraw.setYscale(centerY - (heightField / 2), centerY + (heightField / 2));

        for (Cell c : currentLife) {

            StdDraw.filledSquare(c.x, c.y, 0.1);
        }

        StdDraw.show();
    }

    private static int countNeighboursAndRememberNeighborhood(Cell c) {

        int cnt = 0;

        Cell[] neighboursCell = Cell.neighbours(c);
        for (Cell nbCell : neighboursCell) {

            if (currentLife.contains(nbCell)) {
                cnt++;
            } else {
                neighborhoodOfCells.add(nbCell);
            }
        }

        return cnt;
    }

    private static int countNeighboursForRevival(Cell c) {

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

            int nb = countNeighboursAndRememberNeighborhood(c);

            if (((nb == 2) || (nb == 3))) {
                nextStepLife.add(c);
            }
        }

        //проверка клеток, которые могут возродиться
        for (Cell c : neighborhoodOfCells) {

            if (countNeighboursForRevival(c) == 3) {
                nextStepLife.add(c);
            }
        }

        currentLife.clear();
        tmp = currentLife;
        currentLife = nextStepLife;
        nextStepLife = tmp;
    }
}