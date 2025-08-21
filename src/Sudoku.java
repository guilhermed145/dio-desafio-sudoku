import UI.SudokuGUI;
import data.SudokuList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import model.Square;

public class Sudoku {

    public enum GameStatus {
        NON_STARTED,
        INCOMPLETE,
        COMPLETE
    };

    // Lista de espaços do tabuleiro.
    private final List<List<Square>> squareGrid;
    // Define quantos espaços serão fixos no começo do jogo.
    private final int fixedSquares = 36;
    // Suppliers que serão passados para os botões da interface gráfica.
    Supplier<Boolean> newGame = () -> {
        newGame();
        return true;
    };
    Supplier<String> hasAnyErrors = () -> {
        return hasAnyErrors();
    };
    Supplier<Boolean> cleanBoard = () -> {
        cleanBoard();
        return true;
    };
    // Interface gráfica do jogo.
    private final SudokuGUI sudokuGUI = new SudokuGUI(newGame, hasAnyErrors, cleanBoard);

    public Sudoku() {
        squareGrid = new ArrayList<>();
        for (int row = 0; row < 9; row++) {
            List<Square> squareRow = new ArrayList<>(9);
            for (int col = 0; col < 9; col++) {
                squareRow.add(new Square(false, 0));
            }
            squareGrid.add(squareRow);
        }
    }

    public List<List<Square>> getSquareGrid() {
        return squareGrid;
    }

    public Stream<Square> getSquareGridStream() {
        return squareGrid.stream().flatMap(Collection::stream);
    }

    public void newGame() {
        int[][] sudoku = SudokuList.getRandomSudoku();
        do { 
            Set<List<Integer>> fixedSquarePositions = getFixedSquarePositions(sudoku);
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    Square square = squareGrid.get(row).get(col);
                    square.setRealNumber(sudoku[row][col]);
                    if (fixedSquarePositions.contains(Arrays.asList(row, col))) {
                        square.setFixed(true);
                        square.setPlayerNumber(sudoku[row][col]);
                    } else {
                        square.setFixed(false);
                        square.setPlayerNumber(0);
                    }
                }
            }
        } while (!isSolvable());
        sudokuGUI.setCellValues(squareGrid);
    }

    private Set<List<Integer>> getFixedSquarePositions(int[][] sudoku) {
        Set<List<Integer>> fixedSquarePositions = new HashSet<>(); 
        Random random = new Random();
        List<Integer> randomFixedPosition;
        for (int i = 0; i < fixedSquares; i++) {
            do { 
                randomFixedPosition = Arrays.asList(
                    random.nextInt(sudoku.length),
                    random.nextInt(sudoku[0].length)
                );                
            } while (fixedSquarePositions.contains(randomFixedPosition));
            
            fixedSquarePositions.add(randomFixedPosition);
        }
        return fixedSquarePositions;
    }

    private boolean isSolvable() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (squareGrid.get(row).get(col).getPlayerNumber() != 0) continue;
                ArrayList<Integer> possibleNumbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
                // Verificar os numeros da mesma linha.
                for (int squareCol = 0; squareCol < 9; squareCol++) {
                    if (squareCol == col) continue;
                    Integer cellValue = squareGrid.get(row).get(squareCol).getPlayerNumber();
                    if (possibleNumbers.contains(cellValue)) {
                        possibleNumbers.remove(cellValue);
                    }
                }
                // Verificar os numeros da mesma coluna.
                for (int squareRow = 0; squareRow < 9; squareRow++) {
                    if (squareRow == row) continue;
                    Integer cellValue = squareGrid.get(squareRow).get(col).getPlayerNumber();
                    if (possibleNumbers.contains(cellValue)) {
                        possibleNumbers.remove(cellValue);
                    }
                }
                // Verificar os numeros do mesmo quadrado 3x3.
                int startRow = (row / 3) * 3;
                int startCol = (col / 3) * 3;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (row == startRow + i || col == startCol + j) {
                            continue;
                        }
                        Integer cellValue = squareGrid.get(startRow + i).get(startCol + j).getPlayerNumber();
                        if (possibleNumbers.contains(cellValue)) {
                            possibleNumbers.remove(cellValue);
                        }
                    }
                }
                // Se conseguiu adivinhar o número, volte do começo.
                if (possibleNumbers.size() == 1) {
                    squareGrid.get(row).get(col).setPlayerNumber(possibleNumbers.getFirst());
                    row = 0;
                    col = 0;
                }
            }
        }
        // Se tiver algum quadro sem resposta, não é solucionável.
        return !getSquareGridStream().anyMatch(square -> square.getPlayerNumber() == 0);
    }

    private void cleanBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (!squareGrid.get(row).get(col).isFixed()) {
                    squareGrid.get(row).get(col).setPlayerNumber(0);
                }
            }
        }
        sudokuGUI.setCellValues(squareGrid);
    }

    private void updateSquareValues() {
        int[][] cellValues = sudokuGUI.getCellValues();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                squareGrid.get(row).get(col).setPlayerNumber(cellValues[row][col]);
            }
        }
    }

    public GameStatus checkStatus() {
        if (getSquareGridStream().noneMatch(square -> !square.isFixed() && square.getPlayerNumber() != 0)) {
            return GameStatus.NON_STARTED;
        } 
        return getSquareGridStream().anyMatch(square -> square.getPlayerNumber() == 0) ? GameStatus.INCOMPLETE : GameStatus.COMPLETE;
    }

    public String hasAnyErrors() {
        updateSquareValues();
        if (checkStatus() == GameStatus.NON_STARTED) {
            return "Não iniciado.";
        }
        if (checkStatus() == GameStatus.COMPLETE) {
            if (getSquareGridStream().allMatch(square -> square.getPlayerNumber() == square.getRealNumber())) {
                return "Completo e sem erros.";
            }
            return "Completo e com erros.";
        }
        if (getSquareGridStream().filter(square -> square.getPlayerNumber() != 0).allMatch(square -> square.getPlayerNumber() == square.getRealNumber())) {
            return "Incompleto e sem erros por enquanto.";
        }
        return "Incompleto e com erros.";
    }

}