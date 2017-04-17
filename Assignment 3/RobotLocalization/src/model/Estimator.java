package model;

import control.EstimatorInterface;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.Random;

//import static model.Matrix.multiplyMatrix;
//import static model.Matrix.transposeMatrix;

public class Estimator implements EstimatorInterface {
    private int rows, cols, head;
    private int[] truePos, reading;
    private double[][] T, O, forward, transpose;
    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;
    private Matrix matrix;
    private ArrayList<int[]> firstField, secondField;
    private Bot bot;
//

    public Estimator(int rows, int cols, int head) {
        this.rows = rows;
        this.cols = cols;
        this.head = head;
        firstField = new ArrayList<>();
        secondField = new ArrayList<>();
        matrix = new Matrix(rows, cols, head);
        bot = new Bot(1,5, rows, cols, head);
        int S = rows * cols * head;
        O = sensor(bot.getX(), bot.getY());
        reading = getReading();
        T = new double[S][S];
        forward = new double[S][1];
        initForward();
        initTransition();
        transpose = matrix.transpose(T);
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
        bot.move();
        O = sensor(bot.getX(), bot.getY());
        reading = getReading();
        double[][] temp = matrix.multiplyMatrix(O, transpose);

        temp = matrix.multiplyMatrix(temp, forward);
        System.out.println("update");
        forward = matrix.normalize(temp);
        int[] mostLikely = predictedPos();
        double dist = getDistance(bot.getX(), bot.getY(), mostLikely[0], mostLikely[1]);
        System.out.println("Distance: " + dist);
    }

    public int[] getCurrentTruePosition() {
        int[] truePos = {bot.getX(), bot.getY()};
        return truePos;
    }

    public int[] getCurrentReading() {
        return reading;
    }

    public double getCurrentProb(int x, int y) {
        double tot = 0;
        int state = matrix.stateIndex(x, y);
        for (int i = 0; i < 4; i++){
            tot += forward[state + i][0];
        }
        return tot;
    }


    public double getOrXY(int rX, int rY, int x, int y) {
        double[][] sensorMatrix = sensor(x, y);
        if (rX == -1 || rY == -1) {
            return 1 - 0.1 - firstField.size()*0.05 - secondField.size() * 0.025;
        } else {
            int state = matrix.stateIndex(rX, rY);
            return sensorMatrix[state][state] * head;
        }
    }

    public double getTProb(int x, int y, int h, int nX, int nY, int nH) {
        return T[matrix.stateIndex(x, y) + h][matrix.stateIndex(nX, nY) + nH];
    }

    // -------- PRIVATE METHODS --------

    private void initForward() {
        double prob = 1.0 / (rows * cols * head);
        System.out.println("init");
        for (int i = 0; i < forward.length; i++) {
            forward[i][0] = prob;
        }
    }

    /**
     * The sensor reports
     * the true location L with probability 0.1
     * any of the n_Ls ∈ {3, 5, 8} existing surrounding fields L_s with probability 0.05 each.
     * any of the n_Ls2 ∈ {5, 6, 7, 9, 11, 16} existing “secondary” surrounding fields L_s2 with probability 0.025 each
     * "nothing" with probability 1.0 - 0.1 - n_Ls*0.05 - n_Ls2*0.025
     */
    private double[][] sensor(int x, int y) {
        int S = rows*cols*head;
        double[][] sensor = new double[S][S];
        firstField.clear();
        secondField.clear();
        int[] xRange = {Math.max(x - 2, 0), Math.min(x + 2, rows - 1)};
        int[] yRange = {Math.max(y - 2, 0), Math.min(y + 2, cols - 1)};
        int[] point = new int[2];
        double dist, prob;
        // Fills the sensor (O) matrix
        for (int i = xRange[0]; i <= xRange[1]; i++) {
            for (int j = yRange[0]; j <= yRange[1]; j++) {
                point[0] = i;
                point[1] = j;
                dist = getDistance(x, y, i, j);
                if (dist < 0.01) { // Just to make sure that there isn't any rounding off errors
                    prob = 0.1;
                } else if (dist < 2) {
                    prob = 0.05;
                    firstField.add(point);
                } else {
                    prob = 0.025;
                    secondField.add(point);
                }
                int state = matrix.stateIndex(i, j);
                prob /= head;
                for (int s = state; s < state + head; s++) {
                    sensor[s][s] = prob;
                }
            }
        }
        return sensor;
    }

    private double getDistance(int x, int y, int a, int b) {
        return Math.sqrt(Math.pow(Math.abs(x - a), 2) + Math.pow(Math.abs(y - b), 2));
    }

    private int[] getReading() {
        // Returns a position according to the given probabilities
        Random rand = new Random();
        double p = rand.nextDouble();
        int[] sensPos = new int[2];
        double probFirstField = 0.05 * firstField.size();
        double probSecondField = 0.025 * secondField.size();
        double probForTruePos = 0.1;
        if (p <= probForTruePos) {
            sensPos[0] = bot.getX();
            sensPos[1] = bot.getY();
        } else if (p <= probForTruePos + probFirstField) {
            sensPos = firstField.get(rand.nextInt(firstField.size()));
        } else if (p <= probForTruePos + probFirstField + probSecondField) {
            sensPos = secondField.get(rand.nextInt(secondField.size()));
        } else {
            sensPos = null;
        }
        return sensPos;
    }

    private void initTransition() {
        // Each row represents a state and each column represents a state.
        double prob;
        int pos, nextPos;
        int dir, nextDir; // NORTH = 0, WEST = 1, SOUTH = 2, EAST = 3
        for (int i = 0; i < T.length; i++) {
            // Gets the current position and heading
            pos = i / head;
            dir = i % head;
            for (int j = 0; j < T[0].length; j++) {
                // Iterates through and adds transition probabilities for every other state.
                nextPos = j / head;
                nextDir = j % head;
                // What is the probability that the next position and direction will be nextPos and nextDir?
                if (nextStateIsPossible(pos, nextPos, nextDir)) {
                    if (dir == nextDir) {
                        prob = 0.7;
                    } else {
                        int[] walls = encounteringWalls(pos, dir);
                        boolean wallEncountered = walls[1] == 1;
                        if (wallEncountered) {
                            prob = 1.0 / (double)(head - walls[0]);
                        } else {
                            prob = 0.3 / (double)(head - walls[0] - 1);
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
        // The next heading needs to be possible
        // TODO Investigate this
//        if (walls[h] == -1) return false;
        if (p / cols != np / cols) {
            // p and np are on different rows.
            if (diff < 0) {
                return nh == SOUTH && diff == -cols;
            } else {
                return nh == NORTH && diff == cols;
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
            if (h == NORTH) {
                wallEncountered = 1;
            }
        }
        if ((p / cols != (p - 1) / cols) || p - 1 < 0) {
            walls++; // WEST
            if (h == WEST) {
                wallEncountered = 1;
            }
        }
        if (p + cols >= cols * rows) {
            walls++; // SOUTH
            if (h == SOUTH) {
                wallEncountered = 1;
            }
        }
        if (p / cols != (p + 1) / cols) {
            walls++; // EAST
            if (h == EAST) {
                wallEncountered = 1;
            }
        }
        int[] ret = {walls, wallEncountered};
        return ret;
    }

    private int[] predictedPos() {
        double highest = 0.0, current;
        int[] pos = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                current = getCurrentProb(i, j);
                if (current > highest) {
                    highest = current;
                    pos[0] = i;
                    pos[1] = j;
                }
            }
        }
        return pos;
    }
}