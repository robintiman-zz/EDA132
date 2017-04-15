package model;

/**
 * Created by robintiman on 2017-04-14.
 */
public class Matrix {

    public static double[][] transposeMatrix(double [][] m) {
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }

    public static double[][] multiplyMatrix(double[][] a, double[][] b){
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
                    c[i][j]+= (a[ i][k] * b[k][j]);
                }

            }
        }
        return c;
    }

    public static double [] multiplyMatrixWithVector (double[][] a, double[] b)   {
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

    public static int getMatrixIndex(int x, int y, int h) {
        return rows * cols * x + cols * y + h;
    }

    public static double[][] normalize(double [][] a) {
        int i, j;
        double alpha = 0;
        for(i = 0; i < a.length; i++) {
            for(j = 0; j < a[0].length; j++) {
                alpha =+ a[i][j];
            }
        }

        return matrixMulConst(a, alpha);
    }

    public static double[][] matrixMulConst(double[][] a, double b)   {
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
