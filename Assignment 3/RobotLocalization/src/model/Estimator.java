package model;

import control.EstimatorInterface;

/**
 * Created by robintiman on 2017-03-01.
 */
public class Estimator implements EstimatorInterface {
    private int rows, cols, head, state;
    private Position[][] T;
    private static final int SOUTH = 0;
    private static final int NORTH = 1;
    private static final int EAST = 2;
    private static final int WEST = 3;

    public Estimator(int rows, int cols, int head) {
        this.rows = rows;
        this.cols = cols;
        this.head = head;
        T = new Position[rows][cols];
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

    private class Position {
        private double[] probs;

        public Position () {
            probs = new double[head];
        }

        /**
         * Returns the probability of being in state (x, y, dir)
         * @param dir - The direction
         * @return The transitions probability
         */
        public double getProb(int dir) {
            return probs[dir];
        }

        /**
         * Updates the transition probabilities for this position
         */
        public void updateProbs() {
            // Do something
        }
    }
}

