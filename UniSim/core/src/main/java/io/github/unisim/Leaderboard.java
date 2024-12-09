import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.TreeMap;

public class Leaderboard {
    private TreeMap<Integer, String> sortedScores;

    // Constructor
    public Leaderboard() {
        this.sortedScores = new TreeMap<>(Collections.reverseOrder()); // Descending order of scores
    }

    // Load leaderboard from a CSV file
    public void loadFromCsvFile(String fileName) {
        File file = new File(fileName);

        // Check if the file exists
        if (!file.exists()) {
            System.out.println("CSV file not found: " + fileName);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            // Read each line from the file
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip the header row
                }

                // Split the line into name and score
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());

                    // Add to sortedScores map
                    sortedScores.put(score, name);
                }
            }

            // Ensure only the top 5 scores are retained
            while (sortedScores.size() > 5) {
                sortedScores.pollLastEntry(); // Removes the smallest score
            }

            System.out.println("Leaderboard successfully loaded from " + fileName);

        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid score format in the file. Please check the file content.");
        }
    }

    // Update leaderboard with a new score
    public TreeMap<Integer, String> updateScores(int newScore, String newName) {
        // Add the new score and player name to the map
        sortedScores.put(newScore, newName);

        // If the leaderboard exceeds 5 entries, remove the lowest score
        if (sortedScores.size() > 5) {
            sortedScores.pollLastEntry(); // Removes the entry with the smallest key
        }

        return sortedScores;
    }

    // Save leaderboard to a CSV file
    public void toCsvFile() {
        File file = new File("leaderboard.csv");

        try {
            // Delete existing file
            if (file.exists()) {
                if (!file.delete()) {
                    System.out.println("Failed to delete the existing file.");
                    return;
                }
            }

            // Create a new file and write scores
            try (FileWriter writer = new FileWriter(file)) {
                // Write the header row
                writer.write("Player Name,Score\n");

                // Write each entry
                for (var entry : sortedScores.entrySet()) {
                    writer.write(entry.getValue() + "," + entry.getKey() + "\n");
                }
            }

            System.out.println("Leaderboard successfully saved to leaderboard.csv.");

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    // Display leaderboard for testing
    public void displayLeaderboard() {
        System.out.println("Leaderboard:");
        int rank = 1;
        for (var entry : sortedScores.entrySet()) {
            System.out.println(rank + ". " + entry.getValue() + " - " + entry.getKey());
            rank++;
        }
    }

    /*
     * // Main method for testing
     * public static void main(String[] args) {
     * Leaderboard leaderboard = new Leaderboard();
     * 
     * // Load scores from a CSV file
     * leaderboard.loadFromCsvFile("leaderboard.csv");
     * 
     * // Display the leaderboard after loading
     * // leaderboard.displayLeaderboard();
     * 
     * // Update leaderboard with new scores
     * // leaderboard.updateScores(1000, "A");
     * // leaderboard.updateScores(150, "Bob");
     * leaderboard.updateScores(3300, "B");
     * leaderboard.updateScores(2200, "C");
     * leaderboard.updateScores(2250, "D");
     * leaderboard.updateScores(4000, "E");
     * leaderboard.updateScores(4000, "F");
     * 
     * // Save the updated leaderboard to a CSV file
     * leaderboard.toCsvFile();
     * }
     */
}