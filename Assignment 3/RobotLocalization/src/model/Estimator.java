package model;

import control.EstimatorInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Estimator implements EstimatorInterface {

    private int rows, cols, head, S;
    private int[] truePos;
    private double[][] T, O, forward;
    private static final int NORTH = 0;
    private static final int WEST = 1;
    private static final int SOUTH = 2;
    private static final int EAST = 3;
    private static final int FIRSTCORNER = 3, FIRSTWALL = 5, FIRSTNOWALL = 8;
//    private static final int NOTHING = -1, FOUND = 1;

    public Estimator(int rows, int cols, int head) {
        this.rows = rows;
        this.cols = cols;
        this.head = head;
        truePos = new int[2];
        truePos[0] = 1;
        truePos[1] = 2;
        int S = rows * cols * head;
        T = new double[S][S];
        O = new double[S][S];
        fillTransitionMatrix();

        forward = new double[S][S];
        int[] reading = sensor();
        fillObservationMatrix();

//        for (int i = 0; i < T.length; i++) {
//            for (int j = 0; j < T[i].length; j++) {
//                System.out.print(T[i][j] + " ");
//            }
//            System.out.println();
//        }
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

        // Update propability distribution, forward matrix
        int [] e = getCurrentReading();
        double [][] T_trans = transposeMatrix(T);
        double [][] temp = new double[S][S];
        double [][] next_forward = new double[S][S];
        temp = multiplyMatrix(O, T_trans);
        forward = multiplyMatrix(temp, forward);


    }

    public int[] getCurrentTruePosition() {
        return truePos;
    }

    public int[] getCurrentReading() {
        return new int[0];
    }

    public double getCurrentProb(int x, int y) {
        //TODO WHEN UPDATE DONE
        double temp = 0;
        int firstPosIndex = getMatrixIndex(x, y, 0);
        for (int i = 0; i < 4; i++) {
            temp += O[firstPosIndex+i][firstPosIndex+i];
        }
        return temp;
    }


    public double getOrXY(int rX, int rY, int x, int y) {
        double temp = 0;
        int startPosIndex = getMatrixIndex(x, y, 0);
        int nextPosIndex = getMatrixIndex(rX, rY, 0);
        for (int i = 0; i < 4; i++) {
            temp += O[startPosIndex+i][nextPosIndex+i];
        }
        return temp;
    }

    public double getTProb(int x, int y, int h, int nX, int nY, int nH) {
        return T[getMatrixIndex(x, y, h)][getMatrixIndex(nX, nY ,nH)];
    }

    // -------- PRIVATE METHODS --------

    /**
     * The sensor reports
     * the true location L with probability 0.1
     * any of the n_Ls ∈ {3, 5, 8} existing surrounding fields L_s with probability 0.05 each.
     * any of the n_Ls2 ∈ {5, 6, 7, 9, 11, 16} existing “secondary” surrounding fields L_s2 with probability 0.025 each
     * "nothing" with probability 1.0 - 0.1 - n_Ls*0.05 - n_Ls2*0.025
     */
    private int[] sensor() {
        int x = truePos[0];
        int y = truePos[1];
        int S = rows*cols*head;
        O = new double[S][S];
        int[] xRange = {Math.max(x - 2, 0), Math.min(x + 2, rows)};
        int[] yRange = {Math.max(y - 2, 0), Math.min(y + 2, cols)};
        int[] point = new int[2];
        double dist, prob;
        ArrayList<int[]> firstField = new ArrayList<int[]>();
        ArrayList<int[]> secondField = new ArrayList<int[]>();
        // Fills the sensor (O) matrix
        for (int i = xRange[0]; i < xRange[1]; i++) {
            for (int j = yRange[0]; j < yRange[1]; j++) {
                point[0] = i;
                point[1] = j;
                dist = Math.sqrt(Math.pow(Math.abs(x - i), 2) + Math.pow(Math.abs(y - j), 2));
                if (dist < 0.001) { // Just to make sure that there isn't any rounding off errors
                    prob = 0.1;
                } else if (dist < 2) {
                    prob = 0.05;
                    firstField.add(point);
                } else {
                    prob = 0.025;
                    secondField.add(point);
                }
                int state = convertToLinearPos(i, j) * head;
                for (int s = state; s < state + head; s++) {
                    O[s][s] = prob;
                }
            }
        }
        // Returns a position according to the given probabilities
        Random rand = new Random();
        double p = rand.nextDouble();
        int[] sensPos = new int[2];
        double probFirstField = 0.05 * firstField.size();
        double probSecondField = 0.025 * secondField.size();
        double probForTruePos = 0.1;
        if (p <= probForTruePos) {
            sensPos[0] = x;
            sensPos[1] = y;
        } else if (p <= probForTruePos + probFirstField) {
            sensPos = firstField.get(rand.nextInt(firstField.size()));
        } else if (p <= probForTruePos + probFirstField + probSecondField) {
            sensPos = secondField.get(rand.nextInt(secondField.size()));
        } else {
            sensPos = null;
        }
        return sensPos;
    }

    private int getNbrOfSurroundingFields(int pos) {
        // We only want the number of walls so heading doesn't matter here.
        int[] walls = encounteringWalls(pos, -1);
        return 0;

    }

    private boolean inDirectlySurroundingFields(int pos, int realPos) {
        int diff = Math.abs(pos - realPos);
        return true;
    }

    /**
     * Converts given coordinates into a linear position of the room matrix.
     * @param x, y coordinates
     * @return The corresponding linear position.
     */
    private int convertToLinearPos(int x, int y) {
        return x * cols + y;
    }

    private void fillTransitionMatrix() {
        // Each row represents a state and each column represents a state.
        double prob;
        int pos, nextPos;
        int dir, nextDir; // NORTH = 0, WEST = 1, SOUTH = 2, EAST = 3
        for (int i = 0; i < T[0].length; i++) {
            // Gets the current position and heading
            pos = i / head;
            dir = i % head;
            for (int j = 0; j < T[0].length; j++) {
                // Iterates through and adds transition probabilities for every other state.
                nextPos = j / head;
                System.out.println(nextPos);
                nextDir = j % head;
                // What is the probability that the next position and direction will be nextPos and nextDir?
                if (nextStateIsPossible(pos, nextPos, nextDir)) {
                    if (dir == nextDir) {
                        prob = 0.7;
                    } else {
                        int[] walls = encounteringWalls(pos, dir);
                        boolean wallEncountered = walls[0] == 1;
                        if (wallEncountered) {
                            prob = 1.0 / (head - walls[1]);
                        } else {
                            prob = 0.3 / (head - walls[1] - 1);
                        }
                    }
                } else {
                    prob = 0.0;
                }
                T[i][j] = prob;
            }
        }
    }

    private boolean nextStateIsPossible(int p, int np, int nh) {
        int diff = p - np;
        if (p / cols != np / cols) {
            // p and np are on different rows.
            if (diff < 0) {
                return nh == SOUTH && diff == -4;
            } else {
                return nh == NORTH && diff == 4;
            }
        } else {
            // p and np are on the same row.
            if (diff < 0) {
                return nh == EAST && diff == -1;
            } else {
                return nh == WEST && diff == 1;
            }
        }
    }


    private int[] encounteringWalls(int p, int h) {
        int wallEncountered = 0; // 0 if false, 1 otherwise
        int walls = 0;
        int x = (p-1) / cols;
        if (p - cols < 0) {
            walls++; // NORTH
            if (h == NORTH) wallEncountered = 1;
        }
        if ((p / cols != (p - 1) / cols) || p - 1 < 0) {
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