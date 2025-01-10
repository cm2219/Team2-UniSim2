package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.unisim.GameState;
import io.github.unisim.Timer;
import io.github.unisim.world.UiInputProcessor;
import io.github.unisim.world.World;
import io.github.unisim.world.WorldInputProcessor;

/**
 * Game screen where the main game is rendered and controlled.
 * Supports pausing the game with a pause menu.
 */
public class GameScreen implements Screen {
  private World world = new World();
  private Stage stage = new Stage(new ScreenViewport());
  private InfoBar infoBar;
  private BuildingMenu buildingMenu;
  private Timer timer;
  private InputProcessor uiInputProcessor = new UiInputProcessor(stage);
  private InputProcessor worldInputProcessor = new WorldInputProcessor(world);
  private InputMultiplexer inputMultiplexer = new InputMultiplexer();
  private GameOverMenu gameOverMenu = new GameOverMenu();

  /**
   * Initializes the game screen components.
   *
   * {@link Timer} for game countdown.
   * {@link InfoBar} for displaying game information.
   * {@link BuildingMenu} for interacting with buildings.
   * Input processors for hanlding player inputs
   */
  public GameScreen() {
    timer = new Timer(30_000, 9);
    infoBar = new InfoBar(stage, timer, world);
    buildingMenu = new BuildingMenu(stage, world);

    inputMultiplexer.addProcessor(GameState.fullscreenInputProcessor);
    inputMultiplexer.addProcessor(stage);
    inputMultiplexer.addProcessor(uiInputProcessor);
    inputMultiplexer.addProcessor(worldInputProcessor);
  }

  @Override
  public void show() {
  }

    /**
     * Renders the game screen and updates its state.
     *
     * Game updates when not paused or in a game-over state.
     * UI updates and drawing.
     * Zoom and pan effects when the game is over.
     *
     * @param delta Time in seconds since the last frame.
     */
  @Override
  public void render(float delta) {
    world.render();
    float dt = Gdx.graphics.getDeltaTime();

    if (!GameState.paused && !GameState.gameOver) {
      if (!timer.tick(dt * 1000)) {
        GameState.gameOver = true;
        Gdx.input.setInputProcessor(gameOverMenu.getInputProcessor());
      }
    }

    stage.act(dt);
    infoBar.update();

    if (!GameState.paused) {
        buildingMenu.update();
    }

    stage.draw();

    if (GameState.gameOver) {
      world.zoom((world.getMaxZoom() - world.getZoom()) * 2f);
      world.pan((150 - world.getCameraPos().x) / 10, -world.getCameraPos().y / 10);
      gameOverMenu.render(delta);
    }
  }

  /**
   * Adjusts the layout and components when the screen size changes.
   *
   * @param width  New screen width in pixels.
   * @param height New screen height in pixels.
   */
  @Override
  public void resize(int width, int height) {
    world.resize(width, height);
    stage.getViewport().update(width, height, true);
    infoBar.resize(width, height);
    buildingMenu.resize(width, height);
    gameOverMenu.resize(width, height);
  }

  /**
   * Called when the game is paused.
   */
  @Override
  public void pause() {
  }

  /**
   * Resumes the game and resets components if the game is over.
   */
  @Override
  public void resume() {
    Gdx.input.setInputProcessor(inputMultiplexer);

    if (GameState.gameOver) {
      GameState.gameOver = false;
      GameState.paused = true;
      timer.reset();
      world.reset();
      infoBar.reset();
      buildingMenu.reset();
    }
  }

  @Override
  public void hide() {
  }

  /**
   * Cleans up resources used by the game screen.
   */
  @Override
  public void dispose() {
    world.dispose();
    stage.dispose();
  }
}
