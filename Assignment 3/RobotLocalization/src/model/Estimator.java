package model;

import control.EstimatorInterface;

import java.net.NoRouteToHostException;
import java.util.Arrays;

/**
 * Created by robintiman on 2017-03-01.
 */
public class Estimator implements EstimatorInterface {
    private int rows, cols, head, state;
    private Position[][] T;
    private static final int NORTH = 0;
    private static final int WEST = 1;
    private static final int SOUTH = 2;
    private static final int EAST = 3;

    public Estimator(int rows, int cols, int head) {
        this.rows = rows;
        this.cols = cols;
        this.head = head;
        double[][] T = new double[64][64];
        T = createTransitionMatrix();

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


    /**
     * Returns the transition matrix for the given dimensions
     * Assumes four directions
     * @return The transition matrix of size S*S where S = rows*cols*4 (from number of directions
     * */
    private double[][] createTransitionMatrix() {
        int size = rows * cols * 4;
        int dir = 0;
        double[][] T = new double[size][size];
        for (int x = 0; x < size; x++) {
            dir = x % 4;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; i++) {
                    Position temp = new Position(i, j);
                    double[] p = temp.getProb(dir);
                    for (int k = 0; k < 4; k++) {
                        T[x][i*16 + j * 4 + k] = p[k];
                    }
                }
            }
        }
        return T;
    }


    private class Position {
        private double[] probs;
        private int x, y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
            probs = new double[head];
        }

        /**
         * Returns the probability of being in state (x, y, dir)
         *
         * @param dir - The direction
         * @return The transitions probability
         */
        public double[] getProb(int dir) {
            double[] probs = new double[4];
            boolean northPos = false;
            boolean westPos = false;
            boolean southPos = false;
            boolean eastPos = false;
            Arrays.fill(probs, 0);
            int n = 0;
            switch (dir) {

                case NORTH:

                    if(x != 0) {
                        northPos = true;
                        probs[NORTH] = 0.7;
                    } else {
                        probs[NORTH] = 0;
                    }

                    if(y != cols)   {
                        westPos = true;
                        n++;
                    }

                    if(x != rows)   {
                        southPos = true;
                        n++;
                    }

                    if(y != 0)  {
                        eastPos = true;
                        n++;
                    }

                    if(westPos) probs[WEST] = probs[NORTH]/n;
                    if(southPos) probs[SOUTH] = probs[NORTH]/n;
                    if(eastPos) probs[EAST] = probs[NORTH]/n;


                case WEST:

                    if(x != 0) {
                        northPos = true;
                        n++;
                    }

                    if(y != cols)   {
                        westPos = true;
                        probs[WEST] = 0.7;
                    } else {
                        probs[WEST] = 0;
                    }

                    if(x != rows)   {
                        southPos = true;
                        n++;
                    }

                    if(y != 0)  {
                        eastPos = true;
                        n++;
                    }

                    if(northPos) probs[NORTH] = probs[WEST]/n;
                    if(southPos) probs[SOUTH] = probs[WEST]/n;
                    if(eastPos) probs[EAST] = probs[WEST]/n;

                    case SOUTH:

                        if(x != 0) {
                            northPos = true;
                            n++;
                        }

                        if(y != cols)   {
                            westPos = true;
                            n++;
                        }

                        if(x != rows)   {
                            southPos = true;
                            probs[SOUTH] = 0.7;
                        } else {
                            probs[SOUTH] = 0;
                        }
                        if(y != 0)  {
                            eastPos = true;
                            n++;
                        }

                        if(westPos) probs[WEST] = probs[SOUTH]/n;
                        if(northPos) probs[NORTH] = probs[SOUTH]/n;
                    if(eastPos) probs[EAST] = probs[SOUTH]/n;

                    case EAST:

                    if(x != 0) {
                        northPos = true;
                        n++;
                    }

                        if(y != cols)   {
                            westPos = true;
                            n++;
                        }

                        if(x != rows)   {
                            southPos = true;
                            n++;
                        }

                        if(y != 0)  {
                            eastPos = true;
                            probs[EAST] = 0.7;
                    } else { probs[EAST] = 0; }


                    if(westPos) probs[WEST] = probs[EAST]/n;
                    if(southPos) probs[SOUTH] = probs[EAST]/n;
                    if(northPos) probs[NORTH] = probs[EAST]/n;

            }

        return probs;
        }

        /**
         * Updates the transition probabilities for this position
         */
        public void updateProbs() {
            // Do something
        }


        }
    }


