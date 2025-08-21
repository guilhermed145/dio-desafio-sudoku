package UI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Square;

public class SudokuGUI extends JFrame {

    // Painel que representa o tabuleiro do jogo.
    private final JPanel boardPanel = new JPanel(new GridLayout(9, 9));
    // Lista bidimensional representando os espaços do tabuleiro.
    private final JTextField[][] squareGridUI;

    public SudokuGUI(Supplier<Boolean> newGame, Supplier<String> hasAnyErrors, Supplier<Boolean> cleanBoard) {
        this.squareGridUI = new JTextField[9][9];
        // Define o titulo da janela.
        setTitle("Sudoku");

        // Define que a aplicação deve ser encerrada ao fechar a janela.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Define o tamanho da janela.
        setSize(500, 560);

        // A janela deve ter um tamanho fixo.
        setResizable(false);

        // A janela deve se iniciar no centro da tela do dispositivo.
        setLocationRelativeTo(null);
        
        // Define a imagem logo.png como o ícone da janela.
        ImageIcon frameLogo = new ImageIcon("src/UI/logo.png");
        setIconImage(frameLogo.getImage());

        // Muda a cor de fundo da janela.
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
        // Define o tipo de layout da UI.
        setLayout(new BorderLayout());

        // Botão usado para iniciar um novo jogo.
        JButton newGameButton = new JButton("Novo Jogo");
        newGameButton.setFocusable(false);
        newGameButton.addActionListener(event -> newGame.get());

        // Botão usado para verificar a situação do tabuleiro.
        JButton verifyButton = new JButton("Verificar");
        verifyButton.setFocusable(false);
        verifyButton.addActionListener(event -> JOptionPane.showMessageDialog(this, hasAnyErrors.get(), "Verificação", JOptionPane.INFORMATION_MESSAGE));

        // Botão usado para limpar o tabuleiro.
        JButton cleanBoardButton = new JButton("Limpar");
        cleanBoardButton.setFocusable(false);
        cleanBoardButton.addActionListener(event -> cleanBoard.get());

        // Botão usado para sair do jogo.
        JButton exitButton = new JButton("Sair");
        exitButton.setFocusable(false);
        exitButton.addActionListener(event -> {System.exit(0);});

        // Painel que mostrará os botões do jogo.
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(newGameButton);
        buttonsPanel.add(verifyButton);
        buttonsPanel.add(cleanBoardButton);
        buttonsPanel.add(exitButton);

        // Criação dos espaços do tabuleiro.
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("SansSerif", Font.BOLD, 20));
                cell.setBackground(Color.WHITE);
                cell.setDocument(new NumberTextLimit());

                // Adiciona bordas para uma melhor visualização do tabuleiro.
                if (row == 8) 
                {
                    if (col % 3 == 0) {
                        cell.setBorder(BorderFactory.createMatteBorder(1, 2, 2, 1, Color.BLACK));
                    } else if (col == 8) {
                        cell.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.BLACK));
                    }
                    else {
                        cell.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, Color.BLACK));
                    }
                } else if (col == 8) {
                    if (row % 3 == 0) {
                        cell.setBorder(BorderFactory.createMatteBorder(2, 1, 1, 2, Color.BLACK));
                    } else {
                        cell.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 2, Color.BLACK));
                    }
                } else if (row % 3 == 0 && col % 3 == 0) {
                    cell.setBorder(BorderFactory.createMatteBorder(2, 2, 1, 1, Color.BLACK));
                } else if (row % 3 == 0) {
                    cell.setBorder(BorderFactory.createMatteBorder(2, 1, 1, 1, Color.BLACK));
                } else if (col % 3 == 0) {
                    cell.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 1, Color.BLACK));
                } else if (col % 4 == 0) {
                    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                } else 
                {
                    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
                squareGridUI[row][col] = cell;
                boardPanel.add(cell);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
        
        // Define a janela como visível.
        setVisible(true);
    }

    public int[][] getCellValues() {
        int[][]cellValues = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String cellText = squareGridUI[row][col].getText();
                if (cellText.isEmpty() || !cellText.matches("\\d")) {
                    cellValues[row][col] = 0;
                } else {
                    int value = Integer.parseInt(cellText);
                    cellValues[row][col] = value;
                }
            }
        }
        return cellValues;
    }

    public void setCellValues(List<List<Square>> squareGrid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (squareGrid.get(row).get(col).isFixed()) {
                    String cellValue = Integer.toString(squareGrid.get(row).get(col).getRealNumber());
                    squareGridUI[row][col].setText(cellValue);
                    setFixed(squareGridUI[row][col], true);
                } else {
                    squareGridUI[row][col].setText("");
                    setFixed(squareGridUI[row][col], false);
                }
            }
        }
    }

    public void setFixed(JTextField cell, boolean isFixed) {
        if (isFixed) {
            cell.setBackground(Color.LIGHT_GRAY);
            cell.setEditable(false);
            cell.setFocusable(false);
        } else {
            cell.setBackground(Color.WHITE);
            cell.setEditable(true);
            cell.setFocusable(true);
        }
    }

}
