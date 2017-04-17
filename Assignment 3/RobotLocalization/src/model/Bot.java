package model;

import java.util.ArrayList;
import java.util.Random;

public class Bot {
    private int x, y, h, rows, cols;
    private Random rand;
    // These are the same as in the Estimator class. Not the perfect solution but it's fine for now.
    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;

    public Bot(int x, int y, int rows, int cols, int head) {
        this.x = x;
        this.y = y;
        rand = new Random();
        h = rand.nextInt(head);
        this.rows = rows;
        this.cols = cols;
        this.rows = rows;
    }

    public void move() {
        boolean[] walls = checkWalls();
        ArrayList<Integer> headings = new ArrayList<>(); // Possible new headings
        for (int i = 0; i < walls.length; i++) {
            if (!walls[i] && i != h) headings.add(i);
        }
        double prob = rand.nextDouble();
        if (!walls[h]) {
            // Not encountering a wall
            if (prob < 0.3) {
                h = headings.get(rand.nextInt(headings.size()));
            }
        } else {
            h = headings.get(rand.nextInt(headings.size()));
        }

        // Move the bot
        if (h == EAST) {
            y++;
        } else if (h == WEST) {
            y--;
        } else if (h == NORTH) {
            x--;
        } else {
            x++;
        }
    }

    /**
     * Checks if the bot is encountering any walls
     * @return True if wall is encountered, false otherwise
     */
    private boolean[] checkWalls() {
        // Check if the bot is encountering a wall
        boolean upperWall = x == 0;
        boolean leftWall = y == 0;
        boolean lowerWall = x == rows - 1;
        boolean rightWall = y == cols - 1;
        boolean[] walls = {upperWall, rightWall, lowerWall, leftWall};
        return walls;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
