import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Wordle {
    private JTextField[][] grid;
    private JPanel panel1;
    private Backend backend;

    public Wordle(Backend backend) {
        this.backend = backend;
        initializeUI();
    }

    private void initializeUI() {
        JFrame frame = new JFrame("Wordle Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);

        JPanel mainPanel = new JPanel(new BorderLayout());
        panel1 = new JPanel();
        panel1.setLayout(new GridLayout(5, 5, 5, 5));
        panel1.setPreferredSize(new Dimension(300, 300));

        Font font = new Font("Droid Sans Mono", Font.BOLD, 28);

        // Initialize the grid
        grid = new JTextField[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                grid[row][col] = createTextField(font, row, col);
                panel1.add(grid[row][col]);
            }
        }

        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(e -> handleEnterButton());

        JButton rerunButton = new JButton("Rerun");
        rerunButton.addActionListener(e -> restartGame(frame));

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(enterButton);
        buttonPanel.add(rerunButton);
        buttonPanel.add(exitButton);

        mainPanel.add(panel1, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private JTextField createTextField(Font font, int row, int col) {
        JTextField textField = new JTextField(1);
        textField.setFont(font);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setEnabled(row == backend.getCurrentRow() - 1); // Enable only the current row
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textField.getText().length() >= 1 || !Character.isLetter(e.getKeyChar())) {
                    e.consume();
                } else {
                    e.setKeyChar(Character.toUpperCase(e.getKeyChar()));
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    moveToNextField(row, col);
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    moveToPreviousField(row, col);
                }
            }
        });
        return textField;
    }

    private void moveToNextField(int row, int col) {
        if (col < 4) {
            grid[row][col + 1].requestFocus();
        }
    }

    private void moveToPreviousField(int row, int col) {
        if (col > 0) {
            grid[row][col - 1].requestFocus();
        }
    }

    private void handleEnterButton() {
        int currentRow = backend.getCurrentRow() - 1;
        StringBuilder userWord = new StringBuilder();

        for (int col = 0; col < 5; col++) {
            userWord.append(grid[currentRow][col].getText());
        }

        String enteredWord = userWord.toString().toUpperCase();

        if (enteredWord.length() != 5) {
            JOptionPane.showMessageDialog(null, "Please fill all 5 letters in the row.");
            return;
        }

        if (!backend.isWordInDictionary(enteredWord)) {
            JOptionPane.showMessageDialog(null, "The word does not exist in our dictionary.");
            return;
        }

        // Provide hints by coloring the grid
        colorHints(enteredWord, currentRow);

        if (backend.checkWord(enteredWord)) {
            JOptionPane.showMessageDialog(null, "Congratulations! You guessed the word.");
        } else if (backend.isGameOver()) {
            JOptionPane.showMessageDialog(null, "Game Over! The word was: " + backend.getTargetWord());
        } else {
            backend.moveToNextRow();
            updateGrid();
        }
    }

    private void colorHints(String enteredWord, int row) {
        String targetWord = backend.getTargetWord();
        boolean[] matched = new boolean[5]; // Track matched letters in the target word

        // First pass: Check for correct letters in the correct position (green)
        for (int col = 0; col < 5; col++) {
            if (enteredWord.charAt(col) == targetWord.charAt(col)) {
                grid[row][col].setBackground(Color.GREEN);
                matched[col] = true;
            } else {
                grid[row][col].setBackground(Color.WHITE); // Reset to default for unmatched
            }
        }

        // Second pass: Check for correct letters in the wrong position (yellow)
        for (int col = 0; col < 5; col++) {
            if (grid[row][col].getBackground() != Color.GREEN) {
                for (int targetCol = 0; targetCol < 5; targetCol++) {
                    if (!matched[targetCol] && enteredWord.charAt(col) == targetWord.charAt(targetCol)) {
                        grid[row][col].setBackground(Color.YELLOW);
                        matched[targetCol] = true;
                        break;
                    }
                }
            }
        }
    }

    private void updateGrid() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                grid[row][col].setEnabled(row == backend.getCurrentRow() - 1);
            }
        }
    }

    private void restartGame(JFrame frame) {
        frame.dispose();
        SwingUtilities.invokeLater(() -> {
            try {
                new Wordle(new Backend());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error restarting the game: " + e.getMessage());
            }
        });
    }
}