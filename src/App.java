import javax.swing.UIManager;

public class App {
    public static void main(String[] args) throws Exception {

        try {
            // Define uma UI mais moderna.
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        Sudoku sudoku = new Sudoku();
        sudoku.newGame();
        
    }
}
