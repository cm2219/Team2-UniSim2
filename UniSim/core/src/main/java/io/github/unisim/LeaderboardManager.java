package io.github.unisim.ui;

import io.github.unisim.Leaderboard;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * A utility class to interact with the leaderboard system.
 * Integrates with Leaderboard.java to manage saving and retrieving scores.
 */
public class LeaderboardManager {
    private static LeaderboardManager instance;
    private Leaderboard leaderboard;  // Integrates with the Leaderboard class.

    private static final String LEADERBOARD_FILE = "leaderboard.csv"; // File name for leaderboard.

    /**
     * Private constructor for singularity.
     * Initializes the leaderboard by loading scores from file.
     */
    private LeaderboardManager() {
        leaderboard = new Leaderboard();
        leaderboard.loadFromCsvFile(LEADERBOARD_FILE);
    }

    /**
     * Ensures only one instance of LeaderboardManager exists.
     * @return The singleton instance of LeaderboardManager.
     */
    public static LeaderboardManager getInstance() {
        if (instance == null) {
            instance = new LeaderboardManager();
        }
        return instance;
    }

    /**
     * Saves a new score with a player name to the leaderboard.
     * Updates the leaderboard and writes it to the CSV file.
     *
     * @param score The player's score.
     * @param playerName The name of the player.
     */
    public void saveScore(int score, String playerName) {
        // Update the leaderboard with the new score.
        leaderboard.updateScores(score, playerName);

        // Save the updated leaderboard back to the CSV file.
        leaderboard.toCsvFile();
    }

    /**
     * Retrieves the top 5 scores from the leaderboard.
     * Converts them into a List<String> format for display.
     *
     * @return A list of formatted top scores, e.g., "1. PlayerName - Score".
     */
    public List<String> getTopScores() {
        List<String> formattedScores = new ArrayList<>();
        int rank = 1;

        // Retrieve the sorted scores map and format the top 5 entries.
        for (Map.Entry<Integer, String> entry : leaderboard.updateScores(0, "").entrySet()) {
            formattedScores.add(rank + ". " + entry.getValue() + " - " + entry.getKey());
            rank++;
        }

        return formattedScores;
    }
}

