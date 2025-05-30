import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Backend backend = new Backend(); // Initialize the backend
                String targetWord = backend.getTargetWord(); // Get the generated word
                new Wordle(backend);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error initializing the game: " + e.getMessage());
            }
        });
    }
}