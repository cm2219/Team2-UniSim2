package io.github.unisim.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.unisim.GameState;
import java.util.List;

/**
 * Menu displayed when the game timer runs out.
 * - Saves the player's final score to the leaderboard.
 * - Displays the top 5 scores.
 */
public class GameOverMenu {
    private Stage stage;                      
    private Skin skin;                        
    private ShapeActor bar = new ShapeActor(GameState.UISecondaryColour);
    private Table table;                      
    private TextButton mainMenuButton;        
    private Label topScoresLabel;             
    private Cell<TextButton> buttonCell;
    private InputMultiplexer inputMultiplexer = new InputMultiplexer();

    private LeaderboardManager leaderboardManager; // Manages leaderboard operations.

    /**
     * Creates a new GameOverMenu, saves the final score, and displays the leaderboard.
     *
     * @param finalScore The player's final score to save.
     */
    public GameOverMenu(int finalScore) {
        stage = new Stage(new ScreenViewport());
        table = new Table();
        skin = GameState.defaultSkin;

        // Initialize the LeaderboardManager
        leaderboardManager = LeaderboardManager.getInstance();

        // Save the final score to the leaderboard
        String playerName = "PLACEHOLDER"; // TODO: Replace with actual player input for name
        leaderboardManager.saveScore(finalScore, playerName);

        // Retrieve the top 5 scores and format them into a string
        String topScoresText = getFormattedTopScores();

        // Label to display the leaderboard
        topScoresLabel = new Label("Top 5 Scores:\n" + topScoresText, skin);

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
        table.add(new Label("Your Final Score: " + finalScore, skin)).padBottom(20);
        table.row();
        table.add(topScoresLabel).padBottom(20);
        table.row();
        buttonCell = table.add(mainMenuButton).width(200).height(60);

        // Add the table and bar to the stage
        stage.addActor(bar);
        stage.addActor(table);

        // Input processors for handling user interaction
        inputMultiplexer.addProcessor(GameState.fullscreenInputProcessor);
        inputMultiplexer.addProcessor(stage);
    }

    /**
     * Retrieves and formats the top 5 scores for display.
     *
     * @return A string containing the formatted top scores.
     */
    private String getFormattedTopScores() {
        StringBuilder formattedScores = new StringBuilder();
        List<String> topScores = leaderboardManager.getTopScores();

        for (String score : topScores) {
            formattedScores.append(score).append("\n");
        }

        return formattedScores.toString();
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

