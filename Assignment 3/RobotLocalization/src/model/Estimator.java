package model;

import control.EstimatorInterface;

public class Estimator implements EstimatorInterface {
    private int rows, cols, head;
    private double[][] T, O;
    private static final int NORTH = 0;
    private static final int WEST = 1;
    private static final int SOUTH = 2;
    private static final int EAST = 3;

    public Estimator(int rows, int cols, int head) {
        this.rows = rows;
        this.cols = cols;
        this.head = head;
        int S = rows * cols * head;
        T = new double[S][S];
        O = new double[S][S];
        fillTransitionMatrix();
        fillObservationMatrix();

        for (int i = 0; i < T.length; i++) {
            for (int j = 0; j < T[i].length; j++) {
                System.out.print(T[i][j] + " ");
            }
            System.out.println();
        }
//        state = new State();
    }

    public int getNumRows() {
        return rows;
    }

    public int getNumCols() {
        return cols;
    }

    public int getNumHead() {
        return head;
    }

    public void update() {

    }

    public int[] getCurrentTruePosition() {
        return new int[0];
    }

    public int[] getCurrentReading() {
        return new int[0];
    }

    public double getCurrentProb(int x, int y) {
        return 0;
    }

    public double getOrXY(int rX, int rY, int x, int y) {
        return 0;
    }

    public double getTProb(int x, int y, int h, int nX, int nY, int nH) {
        return 0;
    }

    // -------- PRIVATE METHODS --------

    private void fillObservationMatrix() {
        for (int i = 0; i < O[0].length; i++) {
            // TODO implement this shit.
        }
    }

    private void fillTransitionMatrix() {
        // Each row represents a state and each column represents a state.
        double prob = 0;
        int pos = 0, nextPos = 0;
        int dir = 0, nextDir = 0; // NORTH = 0, WEST = 1, SOUTH = 2, EAST = 3
        for (int i = 0; i < T[0].length; i++) {
            // Gets the current position and heading
            pos = i / head;
            dir = i % head;
            for (int j = 0; j < T[0].length; j++) {
                // Iterates through and adds transition probabilities for every other state.
                nextPos = j / head;
                nextDir = j % head;
                // What is the probability that the next position and direction will be nextPos and nextDir?
                if (nextPosIsPossible(pos, nextPos)) {
                    if (dir == nextDir) {
                        prob = 0.7;
                    } else {
                        int[] walls = encounteringWalls(pos, dir);
                        boolean wallEncountered = walls[0] == 1;
                        if (wallEncountered) {
                            prob = 1 / (head - walls[1]);
                        } else {
                            prob = 0.3 / (head - walls[1]) - 1;
                        }
                    }
                } else {
                    prob = 0;
                }
                T[i][j] = prob;
            }
        }
    }

    private boolean nextPosIsPossible(int p, int np) {
        int diff = Math.abs(p - np);
        if (p / cols != np / cols) {
            // p and np are on different rows.
            return diff == 4;
        }
        // They're on the same row.
        return diff == 1;
    }


    private int[] encounteringWalls(int p, int h) {
        int wallEncountered = 0; // 0 if false, 1 otherwise
        int walls = 0;
        if (p - cols < 0) {
            walls++; // NORTH
            if (h == NORTH) wallEncountered = 1;
        }
        if (p / cols != (p - 1) / cols) {
            walls++; // WEST
            if (h == WEST) wallEncountered = 1;
        }
        if (p + cols > cols * rows) {
            walls++; // SOUTH
            if (h == SOUTH) wallEncountered = 1;
        }
        if (p / cols != (p + 1) / cols) {
            walls++; // EAST
            if (h == EAST) wallEncountered = 1;
        }
        int [] ret = {wallEncountered, walls};
        return ret;
    }
}