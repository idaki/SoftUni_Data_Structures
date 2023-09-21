package implementations;

public class TheMatrix {
    private char[][] matrix;
    private char fillChar;
    private char toBeReplaced;
    private int startRow;
    private int startCol;

    public TheMatrix(char[][] matrix, char fillChar, int startRow, int startCol) {
        this.matrix = matrix;
        this.fillChar = fillChar;
        this.startRow = startRow;
        this.startCol = startCol;
        if (!isValidStart(startRow, startCol)) {
            throw new IllegalArgumentException("Invalid starting coordinates");
        }
        this.toBeReplaced = this.matrix[this.startRow][this.startCol];
    }

    public void solve() {
        fillMatrix(this.startRow, this.startCol);
    }

    private void fillMatrix(int row, int col) {
        if (outOfBoundIndex(row, col) || this.matrix[row][col] != toBeReplaced) {
            return;
        }
        this.matrix[row][col] = this.fillChar;
        fillMatrix(row + 1, col);
        fillMatrix(row, col + 1);
        fillMatrix(row - 1, col);
        fillMatrix(row, col - 1);
    }

    private boolean outOfBoundIndex(int row, int col) {
        return row < 0 || row >= matrix.length || col < 0 || col >= matrix[row].length;
    }

    private boolean isValidStart(int row, int col) {
        return row >= 0 && row < matrix.length && col >= 0 && col < matrix[row].length;
    }

    public String toOutputString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < this.matrix.length; r++) {
            for (int c = 0; c < this.matrix[r].length; c++) {
                sb.append(this.matrix[r][c]);
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}

