import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

class Matrix {
    private int rows;
    private int cols;
    private float[][] data;

    Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new float[rows][cols];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.data[i][j] = 0;
            }
        }
    }

    Matrix(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            this.rows = Integer.parseInt(scanner.next());
            this.cols = Integer.parseInt(scanner.next());
            this.data = new float[rows][cols];
            for (int r = 0; r < data.length; r++) {
                for (int c = 0; c < data[0].length; c++) {
                    data[r][c] = Float.parseFloat(scanner.next());
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    Matrix(float[][] matrix) {
        this.rows = matrix.length;
        this.cols = matrix[0].length;
        this.data = new float[rows][cols];
        for (int r = 0; r < rows; r++) {
            System.arraycopy(matrix[r], 0, this.data[r], 0, cols);
        }
    }

    Matrix(Matrix matrix) {
        this.rows = matrix.rows;
        this.cols = matrix.cols;
        this.data = new float[this.rows][this.cols];
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                this.data[r][c] = matrix.getData()[r][c];
            }
        }
    }

    static Matrix fromArray(float[] arr) {
        Matrix m = new Matrix(arr.length, 1);
        for (int i = 0; i < arr.length; i++) {
            m.data[i][0] = arr[i];
        }
        return m;
    }

    static Matrix subtract(Matrix a, Matrix b) {
        Matrix result = new Matrix(a.rows, a.cols);
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                result.data[i][j] = a.data[i][j] - b.data[i][j];
            }
        }
        return result;
    }

    float[] toArray() {
        float[] arr;
        if (rows >= cols) {
            arr = new float[rows];
            for (int i = 0; i < this.rows; i++) {
                arr[i] = this.data[i][0];
            }
        } else {
            arr = new float[cols];
            System.arraycopy(this.data[0], 0, arr, 0, this.cols);
        }
        return arr;
    }

    void randomize() {
        Random random = new Random();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.data[i][j] = random.nextFloat() * 2 - 1;
            }
        }
    }

    void add(Matrix n) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.data[i][j] += n.data[i][j];
            }
        }
    }

    void add(float n) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.data[i][j] += n;
            }
        }
    }

    static Matrix transpose(Matrix matrix) {
        Matrix result = new Matrix(matrix.cols, matrix.rows);
        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.cols; j++) {
                result.data[j][i] = matrix.data[i][j];
            }
        }
        return result;
    }

    static Matrix multiply(Matrix a, Matrix b) {
        // Matrix product
        if (a.cols != b.rows) {
            System.out.println("ERROR");
            return null;
        }
        Matrix result = new Matrix(a.rows, b.cols);
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                // Dot product of values in col
                float sum = 0;
                for (int k = 0; k < a.cols; k++) {
                    sum += a.data[i][k] * b.data[k][j];
                }
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    void multiply(Matrix n) {
        // hadamard product
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.data[i][j] *= n.data[i][j];
            }
        }
    }

    void multiply(float n) {
        // Scalar product
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.data[i][j] *= n;
            }
        }
    }

    void map(Map map) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.data[i][j] = map.map(this.data[i][j]);
            }
        }
    }

    static Matrix map(Matrix matrix, Map map) {
        Matrix result = new Matrix(matrix.rows, matrix.cols);
        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.cols; j++) {
                float val = matrix.data[i][j];
                result.data[i][j] = map.map(val);
            }
        }
        return result;
    }

    void print() {
        for (int i = 0; i < this.rows; i++) {
            System.out.println(Arrays.toString(this.data[i]));
        }
        System.out.println();
    }

    void importFromFile(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            int rows = Integer.parseInt(scanner.next());
            int cols = Integer.parseInt(scanner.next());
            if (this.rows != rows || this.cols != cols) {
                System.out.println("ERROR: this.rows != rows || this.cols != cols");
                return;
            }
            for (int r = 0; r < data.length; r++) {
                for (int c = 0; c < data[0].length; c++) {
                    data[r][c] = Float.parseFloat(scanner.next());
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void exportToFile(String fileName) {
        try {
            PrintWriter printWriter = new PrintWriter(fileName);
            printWriter.println(data.length + " " + data[0].length);
            for (float[] datum : data) {
                for (int c = 0; c < data[0].length; c++) {
                    printWriter.print(datum[c] + " ");
                }
                printWriter.println();
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public float[][] getData() {
        return data;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (float[] datum : data) {
            result.append(Arrays.toString(datum));
        }
        return result.toString();
    }
}
