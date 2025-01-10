package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.unisim.GameState;

public class AchievementsScreen implements Screen {
    private Stage stage;
    private Table table;
    private Skin skin;
    private TextButton backButton;

    /**
     * Sets up the achievements screen with a "Back" button and layout.
     * Initializes the stage, table, and UI components.
     */
    public AchievementsScreen() {
        stage = new Stage();
        table = new Table();
        skin = GameState.defaultSkin;

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                GameState.currentScreen = GameState.startScreen;
            }
        });

        table.setFillParent(true);
        table.center();
        table.add(backButton).center().width(200).height(60);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {}

    /**
     * Renders the screen and updates the stage.
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(GameState.UISecondaryColour);

        stage.act(delta);
        stage.draw();
    }

    /**
     * Adjusts the layout when the screen size changes.
     *
     * @param width  New screen width in pixels.
     * @param height New screen height in pixels.
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

    /**
     * Cleans up resources used by the achievements screen.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
