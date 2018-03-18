package webservice.forecast.adaptive.util;

public class Util {

    public static void printMatrix(Double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print(i + 1 + " :");
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "      ");
            }
            System.out.println();
        }
    }

    public static void fillMatrixByZero(Double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = 0.0;
            }
        }
    }

    public static int summOfArray(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

}

