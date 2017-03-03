package model;

import control.EstimatorInterface;

public class Estimator implements EstimatorInterface {
    private int rows, cols, head, S;
    private double[][] T, O, forward;

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
        forward = new double[S][S];
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

        // Update proparbility distribution, forward matrix
        int [] e = getCurrentReading();
        double [][] T_trans = transposeMatrix(T);
        double [][] temp = new double[S][S];
        double [][] next_forward = new double[S][S];
        temp = multiplyMatrix(O, T_trans);
        forward = multiplyMatrix(temp, forward);


    }

    public int[] getCurrentTruePosition() {
        return new int[0];
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
                System.out.println(nextPos);
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
                            prob = 0.3 / (head - walls[1] - 1);
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
            return diff == cols;
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