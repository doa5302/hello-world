import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Backend {
    private Words words;
    private String targetWord;
    private int currentRow;
    private Set<String> dictionary;

    public Backend() throws IOException {
        words = new Words();
        targetWord = words.getRandomWord().toUpperCase(); // Generate the target word
        currentRow = 1; // Start with the first row
        dictionary = loadDictionary(); // Load all words into a set for quick lookup
        System.out.println("Generated Word: " + targetWord);
    }

    public String getTargetWord() {
        return targetWord;
    }

    public boolean checkWord(String userWord) {
        return targetWord.equals(userWord);
    }

    public boolean isWordInDictionary(String word) {
        return dictionary.contains(word.toUpperCase());
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void moveToNextRow() {
        currentRow++;
    }

    public boolean isGameOver() {
        return currentRow > 5; // Game ends after 5 rows
    }

    private Set<String> loadDictionary() throws IOException {
        Set<String> wordSet = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("5-words.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordSet.add(line.trim().toUpperCase());
            }
        }
        return wordSet;
    }
}