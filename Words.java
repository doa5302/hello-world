import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Words {
    private String filePath;

    // Constructor to initialize with the correct file path
    public Words() {
        this.filePath = "5-words.txt";
    }

    // Reads a random word from the file
    public String getRandomWord() throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
        }
        if (words.isEmpty()) {
            throw new IOException("The file is empty or not found.");
        }
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    // Writes data to the file
    public void writeToFile(String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.newLine();
        }
    }
}


