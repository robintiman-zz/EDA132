package model;

/**
 * Created by robintiman on 2017-04-14.
 */
public class Matrix {
    private int rows, cols, head;

    public Matrix(int rows, int cols, int head) {
        this.rows = rows;
        this.cols = cols;
        this.head = head;
    }

    public double[][] transpose(double [][] m) {
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    public double[][] multiplyMatrix(double[][] a, double[][] b){
        double[][] product = new double[a.length][b[0].length];
        for(int i = 0; i < a.length; i++){
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < a.length; k++) {
                    product[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return product;
    }
    //
//    public static double [] multiplyMatrixWithVector (double[][] a, double[] b)   {
//        int i, j;
//        double temp = 0;
//        double[] result = new double[S];
//
//        for (i = 0; i < a.length; i++)  {
//            for (j = 0; j < a.length; j++)   {
//                temp += a[i][j] * b[j];
//            }
//            result[i] = temp;
//            temp = 0;
//        }
//        return result;
//    }
//
    public int stateIndex(int x, int y) {
        return (x * cols + y) * head;
    }

    public double[][] normalize(double [][] a) {
        int i, j;
        double alpha = 0;
        for(i = 0; i < a.length; i++) {
            for(j = 0; j < a[0].length; j++) {
                alpha = Math.max(a[i][j], alpha);
            }
        }
        return mulitplyConst(a, 1.0 / ((alpha + 0.0001)*4.0));
    }

    public double[][] mulitplyConst(double[][] a, double b)   {
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
