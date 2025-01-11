package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.unisim.GameState;
import io.github.unisim.Leaderboard;

import java.util.List;

public class LeaderboardScreen implements Screen {
    private Stage stage;
    private Table table;
    private Skin skin;
    private TextButton backButton;
    private Label topScoresLabel;
    private Cell<TextButton> buttonCell;


    private Leaderboard leaderboard;

    /**
     * Initializes the leaderboard screen with its components.
     *
     * A table layout for organizing UI elements.
     * A "Back" button for returning to the start screen.
     * Integration with the {@link GameState} to manage navigation.
     */
    public LeaderboardScreen() {
        stage = new Stage();
        table = new Table();
        skin = GameState.defaultSkin;

        // Initialize the LeaderboardManager
        leaderboard = Leaderboard.getInstance();

        // Label to display the leaderboard
        topScoresLabel = new Label("Top 5 Scores:\n", skin);

        // Return to Main Menu button
        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                // Switch to the Start Menu screen
                GameState.currentScreen = GameState.startScreen;
            }
        });

        // Arrange UI components using the table layout
        table.setFillParent(true); // Table fills the entire stage
        table.center();
        table.add(topScoresLabel).padBottom(20);
        table.row();
        buttonCell = table.add(backButton).width(200).height(60);

        // Add the table to the stage
        stage.addActor(table);

        // Initial display of the leaderboard
        updateLeaderboardDisplay();
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
     * Called when the screen becomes visible.
     */
    @Override
    public void show() {}

    /**
     * Renders the leaderboard screen and handles updates.
     *
     * Clears the screen and sets the background color.
     * Updates and draws the stage containing UI elements.
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(GameState.UISecondaryColour);

        stage.act(delta);
        stage.draw();
        updateLeaderboardDisplay();
    }

    /**
     * Adjusts the screen layout when the window size changes.
     *
     * @param width  The new screen width in pixels.
     * @param height The new screen height in pixels.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
