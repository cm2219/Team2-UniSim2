package io.github.unisim.ui;

import io.github.unisim.Leaderboard;
import io.github.unisim.Achievements;
import io.github.unisim.world.SatisfactionCalculator;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.unisim.GameState;

import java.util.List;

/**
 * Menu displayed when the game timer runs out.
 * - Allows the player to input their name.
 * - Saves the player's final score to the leaderboard.
 * - Displays the top 5 scores.
 */
public class GameOverMenu {
    private Stage stage;
    private Skin skin;
    private ShapeActor bar = new ShapeActor(GameState.UISecondaryColour);
    private Table table;
    private TextButton saveScoreButton;
    private TextButton mainMenuButton;
    private Label topScoresLabel;
    private Label instructionLabel;
    private Label AchievementsLabel;
    private TextField nameInputField;         // Text field for player name input
    private Cell<TextButton> buttonCell;
    private InputMultiplexer inputMultiplexer = new InputMultiplexer();

    private Leaderboard leaderboard; // Manages leaderboard operations.
    private SatisfactionCalculator satisfactionCalculator; // Calculates satisfaction score
    private Achievements achievements; // Manages achievements
    private int score; // Player's satisfaction score
    private int achievementTotal; // Total points from unlocked achievements
    private int finalScore; // Player's final score

    /**
     * Constructor for GameOverMenu.
     * - Initializes UI components and calculates the final score.
     */
    public GameOverMenu() {
        calculateFinalScore();
        System.out.println(finalScore);// Calculate the final score

        stage = new Stage(new ScreenViewport());
        table = new Table();
        skin = GameState.defaultSkin;

        // Initialize the LeaderboardManager
        leaderboard = Leaderboard.getInstance();

        // Instruction label
        instructionLabel = new Label("Enter your name to save your score:", skin);

        // Text field for player name input
        nameInputField = new TextField("", skin);
        nameInputField.setMessageText("Your Name"); // Placeholder text

        // Button to save the score
        saveScoreButton = new TextButton("Save Score", skin);
        saveScoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                String playerName = nameInputField.getText().trim();
                if (!playerName.isEmpty()) {
                    leaderboard.updateScores(finalScore, playerName); // Save the score to the leaderboard
                    leaderboard.toCsvFile(); // Save leaderboard to a CSV file
                    updateLeaderboardDisplay(); // Update the leaderboard display
                    instructionLabel.setText("Score saved!"); // Feedback to player
                } else {
                    instructionLabel.setText("Name cannot be empty. Try again.");
                }
            }
        });

        // Label to display the leaderboard
        topScoresLabel = new Label("Top 5 Scores:\n", skin);

        // Label to display achievements
        AchievementsLabel = new Label("Unlocked Achievements:\n", skin);
        updateAchievementsDisplay(achievements); // Display unlocked achievements

        // Return to Main Menu button
        mainMenuButton = new TextButton("Return to Main Menu", skin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Switch to the Start Menu screen
                GameState.currentScreen = GameState.startScreen;
            }
        });

        // Arrange UI components using the table layout
        table.setFillParent(true); // Table fills the entire stage
        table.center();
        table.add(new Label("Game Over!", skin)).padBottom(20);
        table.row();
        table.add(new Label("Your Final Score: " + finalScore, skin)).padBottom(20); // Display final score
        table.row();
        table.add(instructionLabel).padBottom(10);
        table.row();
        table.add(nameInputField).width(300).padBottom(20);
        table.row();
        table.add(saveScoreButton).width(200).height(50).padBottom(20);
        table.row();
        table.add(topScoresLabel).padBottom(20);
        table.row();
        table.add(AchievementsLabel).padBottom(20);
        table.row();
        buttonCell = table.add(mainMenuButton).width(200).height(60);

        // Add the table and bar to the stage
        stage.addActor(bar);
        stage.addActor(table);

        // Input processors for handling user interaction
        inputMultiplexer.addProcessor(GameState.fullscreenInputProcessor);
        inputMultiplexer.addProcessor(stage);

        // Initial display of the leaderboard
        updateLeaderboardDisplay();
    }

    /**
     * Calculates the final score by combining satisfaction and achievements.
     *Currently returns 0
     */
    private void calculateFinalScore() {
        satisfactionCalculator = new SatisfactionCalculator(); // Ensure satisfaction is properly initialized
        score = (int) satisfactionCalculator.getSatisfaction(); // Retrieve satisfaction score
        achievements = Achievements.getInstance(); // Ensure achievements are initialized
        achievementTotal = achievements.calculateAchievementEffects(); // Get achievement effects total
        finalScore = score + achievementTotal; // Combine satisfaction and achievement effects
        System.out.println("Final Score: " + finalScore); // Debug log for final score calculation
    }

    /**
     * Updates the achievements display with the unlocked achievements.
     */
    private void updateAchievementsDisplay(Achievements achievements) {
        StringBuilder formattedAchievements = new StringBuilder("Unlocked Achievements:\n");
        boolean achievementUnlocked = false; // Track if any achievements are unlocked.

        for (var entry : achievements.getAchievements().entrySet()) { // Iterate through achievements
            if (entry.getValue().achieved) { // Check if achievement is unlocked
                formattedAchievements.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue().description)
                    .append(" (")
                    .append(entry.getValue().effect)
                    .append(" points)\n");
                // Debug log unlocked achievements
                System.out.println("Achievement Unlocked: " + entry.getKey() + " - " + entry.getValue().description);
                achievementUnlocked = true; // Mark that at least one achievement is unlocked.
            }
        }

        // If no achievements are unlocked, display a specific message.
        if (!achievementUnlocked) {
            formattedAchievements.append("No achievements to display!\n");
        }

        AchievementsLabel.setText(formattedAchievements.toString()); // Updates achievements label
    }

    /**
     * Updates the leaderboard display with the latest top scores.
     */
    private void updateLeaderboardDisplay() {
        StringBuilder formattedScores = new StringBuilder("Top 5 Scores:\n");
        List<String> topScores = leaderboard.getFormattedTopScores();

        for (String score : topScores) {
            formattedScores.append(score).append("\n");
        }

        topScoresLabel.setText(formattedScores.toString());
    }

    /**
     * Renders the Game Over Menu.
     *
     * @param delta The time elapsed since the last frame.
     */
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    /**
     * Resizes the UI components when the window is resized.
     *
     * @param width  The new window width.
     * @param height The new window height.
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        table.setBounds(0, 0, width, height * 0.1f);
        bar.setBounds(0, 0, width, height * 0.1f);
        buttonCell.width(width * 0.3f).height(height * 0.1f);
    }

    /**
     * Provides the input processor for handling input events.
     *
     * @return The InputMultiplexer containing the input processors.
     */
    public InputProcessor getInputProcessor() {
        return inputMultiplexer;
    }
}
