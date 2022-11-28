package SearchModule;

public class MathUtils { // same methods as in matmult module from in 1405
    public static double[][] scalarMult (double[][]a, double scale) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = a[i][j] * scale;
            }
        }
        return a;
    }
    public static double euclidDist (double[][]a, double[][]b) {
        double dist = 0.0;
        if (a[0].length == b[0].length) {
            for (int i = 0; i < a[0].length; i++) {
                dist += Math.pow(a[0][i] - b[0][i], 2);
            }
            return Math.pow(dist, 0.5);
        }
        return dist;
    }
    public static double[][] matMult (double[][] a, double[][] b) {
        double[][] multiMatrix = new double[a.length][b[0].length];
        if (a[0].length == b.length) {
            for (int i = 0; i < a.length; i ++){
                for (int j = 0; j < b[0].length; j ++) {
                    for (int k = 0; k < b.length; k ++) {
                        multiMatrix[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
            return multiMatrix;
        } else{
            return a;
        }
    }
    public static Double vectorMagnitude (double[] vector) {
        double magnitude = 0;
        for (double e : vector) {
            magnitude += Math.pow(e, 2);
        }
        return Math.sqrt(magnitude);
    }
    public static double dotProduct (double[] a, double[]b) {
        double dotProduct = 0;
        if (a.length != b.length) {
            return dotProduct;
        }
        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
        }
        return dotProduct;
    }
}
