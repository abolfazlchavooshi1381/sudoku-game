import java.awt.*;
import java.io.*;

public class SudokuSolver {

    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";


    public static void main(String[] args) throws IOException {
        int[][] sudokuTable = readSudokuTable(INPUT_FILE);
        long startTime = System.nanoTime();
        boolean solved = solveSudoku(sudokuTable);
        if (solved) {
            System.out.println("\nSudoku Solved");
            long endTime = System.nanoTime();
            double elapsedTime = (endTime - startTime) / 1e6;
            writeOutput(sudokuTable, elapsedTime, OUTPUT_FILE);

            Desktop.getDesktop().open(new File(OUTPUT_FILE));
        } else {
            System.out.println("No solution exists");
        }
    }

    private static int[][] readSudokuTable(String fileName) {
        int[][] sudokuTable = new int[9][9];
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            for (int i = 0; i < 9; i++) {
                String[] line = reader.readLine().split(" ");
                for (int j = 0; j < 9; j++) {
                    sudokuTable[i][j] = Integer.parseInt(line[j]);
                }
            }
        } catch (IOException e) {
            System.out.println("Input file not found");
            System.exit(1);
        }
        return sudokuTable;
    }

    private static boolean solveSudoku(int[][] sudokuTable) {
        int row = -1;
        int col = -1;
        boolean isEmpty = true;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudokuTable[i][j] == 0) {
                    row = i;
                    col = j;
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty) {
                break;
            }
        }

        if (isEmpty) {
            return true;
        }

        for (int number = 1; number <= 9; number++) {
            if (isSafe(sudokuTable, row, col, number)) {
                sudokuTable[row][col] = number;
                if (solveSudoku(sudokuTable)) {
                    return true;
                } else {
                    sudokuTable[row][col] = 0;
                }
            }
        }
        return false;
    }

    private static boolean isSafe(int[][] sudokuTable, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (sudokuTable[row][i] == num || sudokuTable[i][col] == num) {
                return false;
            }
        }

        int boxRow = row - row % 3;
        int boxCol = col - col % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (sudokuTable[boxRow + i][boxCol + j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void writeOutput(int[][] sudokuTable, double elapsedTime, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            for (int i = 0; i < 9; i++) {
                if (i % 3 == 0 && i != 0)
                    fileWriter.write("\n");
                for (int j = 0; j < 9; j++) {
                    if (j % 3 == 0)
                        fileWriter.write(" ");
                    fileWriter.write(sudokuTable[i][j] + " ");
                }
                fileWriter.write("\n");
            }
            fileWriter.write("\n----------------------\n");
            fileWriter.write("\nExecution time: " + elapsedTime + " ms\n");
        } catch (IOException e) {
            System.out.println("Error writing output file");
        }
    }
}