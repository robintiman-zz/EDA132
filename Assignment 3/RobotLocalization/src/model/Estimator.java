package model;

import control.EstimatorInterface;

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
//        fillObservationMatrix();

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

    //  The sensor reports
    // - the true location L with probability 0.1
    // - any of the n_Ls ∈ {3, 5, 8} existing surrounding fields L_s with probability 0.05 each.
    // - any of the n_Ls2 ∈ {5, 6, 7, 9, 11, 16} existing “secondary” surrounding fields L_s2 with probability 0.025 each
    // - "nothing" with probability 1.0 - 0.1 - n_Ls*0.05 - n_Ls2*0.025
    private int[] sensor() {
        int truepos = convertToLinearPos(truePos[0], truePos[1]);
        int[] walls = encounteringWalls(truepos, -1);
        int n_Ls, n_Ls2;
        int nbrOfSurroundingWalls = walls[1];
        if (nbrOfSurroundingWalls == 2) {
            // This is hard-coded for now and assumes a 4x4 grid.
            n_Ls = FIRSTCORNER;
            n_Ls2 = 5;
        } else if (nbrOfSurroundingWalls == 1) {
            n_Ls = FIRSTWALL;
            n_Ls2 = 6;
        } else {
            n_Ls = FIRSTNOWALL;
            n_Ls2 = 7;
        }
        double probForSurrField = 0.05 * n_Ls;
        double probFor2SurrField = 0.025 * n_Ls2;
        double probForTruePos = 0.1;
        Random rand = new Random();
        double reading = rand.nextDouble();
        reading = 0.3;
        if (reading <= probForTruePos) {
            return truePos;
        } else if (reading <= probForTruePos + probForSurrField) {
            // Return some pos in the directly surrounding field.
            return pickFromSurroundingField(n_Ls, 1);
        } else if (reading <= probForTruePos + probForSurrField + probFor2SurrField) {
            // Return some pos in the second surrounding field.
            return pickFromSurroundingField(n_Ls2, 2);
        } else {
            // Return nothing (null)
            return null;
        }

    }

    private int[] pickFromSurroundingField(int n, int level) {
        int x, y, rangeFrom, rangeTo;
        if (level == 1) {
            rangeFrom = -1;
            rangeTo = 2;
        } else {
            rangeFrom = -2;
            rangeTo = 3;
        }
        ArrayList<int[]> toPickFrom = new ArrayList<>(n);
        for (int j = rangeFrom; j < rangeTo; j++) {
            x = truePos[0] + j;
            if (x > rows || x < 0) continue; // Out of bounds
            for (int i = -1; i < 2; i++) {
                y = truePos[1] + i;
                if (level == 2) {
                    if (Math.abs(x - truePos[0]) < 2 && Math.abs(y - truePos[1]) < 2) continue;
                }
                if (x == truePos[0] && y == truePos[1]) continue; // We don't want to add the true location.
                if (y <= cols && y >= 0) {
                    int[] point = {x, y};
                    toPickFrom.add(point);
                }

            }
        }
        Random rand = new Random();
        return toPickFrom.get(rand.nextInt(n));
    }

    /**
     * Fills the O matrix
     * @param reading True if the sensor found something, False otherwise.
     */
    private void fillObservationMatrix(boolean reading) {
        int pos, realPos;
        double prob;
        for (int i = 0; i < O[0].length; i += head) {
            pos = i / head;
            realPos = convertToLinearPos(truePos[0], truePos[1]);
//            if (reading) {
//                if (pos == realPos) {
//                    prob = 0.1;
//                } else if () {
//
//                }
//
//            } else {
//
//            }
//            O[i][i] = prob;
        }
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

    private double[][] transposeMatrix(double [][] m) {
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    private double[][] multiplyMatrix(double[][] a, double[][] b){
        int size = a.length;
        double[][] c = new double[size][size];
        int i,j,k;
        for (i = 0; i < size; i++) {
            for (j = 0; j < 2; j++) {
                c[i][j] = 0.00000;
            }
        }
        for(i=0; i < size; i++){
            for(j = 0; j < size; j++){
                for (k = 0; k < size; k++){
                    c[i][j]+= (a[i][k] * b[k][j]);
                }

            }
        }
        return c;
    }

    private double [] multiplyMatrixWithVector (double[][] a, double[] b)   {
        int i, j;
        double temp = 0;
        double[] result = new double[S];

        for (i = 0; i < a.length; i++)  {
            for (j = 0; j < a.length; j++)   {
                temp += a[i][j] * b[j];
            }
            result[i] = temp;
            temp = 0;
        }
        return result;
    }

    private int getMatrixIndex(int x, int y, int h) {
        return rows * cols * x + cols * y + h;
    }

    private double[][] normalize(double [][] a) {
        int i, j;
        double alpha = 0;
        for(i = 0; i < a.length; i++) {
            for(j = 0; j < a[0].length; j++) {
                alpha =+ a[i][j];
            }
        }

        return matrixMulConst(a, alpha);
    }

    private double[][] matrixMulConst(double[][] a, double b)   {
        int i, j;
        double[][] result = new double[a.length][a[0].length];
        for(i = 0; i < a.length; i++)   {
            for(j =0; j < a[0].length; j++) {
                result[i][j] = a[i][j] * b;


            }
        }
        return result;
    }
}