package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import io.github.unisim.GameState;
import io.github.unisim.Timer;
import io.github.unisim.building.BuildingType;
import io.github.unisim.world.World;

/**
 * Create a Title bar with basic info.
 */
public class InfoBar {
  private ShapeActor bar;
  private Table infoTable = new Table();
  private Table titleTable = new Table();
  private Table buildingCountersTable = new Table();
  private Label[] buildingCounterLabels = new Label[4];
  private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
  private Label scoreLabel = new Label("86%", skin);
  private Label titleLabel = new Label("UniSim", skin);
  private Label timerLabel;
  private Texture pauseTexture = new Texture("ui/pause.png");
  private Texture playTexture = new Texture("ui/play.png");
  private Image pauseImage = new Image(pauseTexture);
  private Image playImage = new Image(playTexture);
  private Timer timer;
  private Cell<Label> timerLabelCell;
  private Cell<Label> scoreLabelCell;
  private Cell<Image> pauseButtonCell;
  private Cell<Table> buildingCountersTableCell;
  private Cell[] buildingCounterCells;
  private World world;
    private Label eventTitleLabel = new Label("", skin);
    private Label eventDescriptionLabel = new Label("", skin);


  /**
   * Create a new infoBar and draws its' components onto the provided stage.

   * @param stage - The stage on which to draw the InfoBar.
   */
  public InfoBar(Stage stage, Timer timer, World world) {
    this.timer = timer;
    this.world = world;
    buildingCounterCells = new Cell[4];

    // Building counter table
    for (int i = 0; i < 4; i++) {
      buildingCounterLabels[i] = new Label("", skin);
    }
    buildingCounterCells[0] = buildingCountersTable.add(buildingCounterLabels[0]);
    buildingCounterCells[1] = buildingCountersTable.add(buildingCounterLabels[1]);
    buildingCountersTable.row();
    buildingCounterCells[2] = buildingCountersTable.add(buildingCounterLabels[2]);
    buildingCounterCells[3] = buildingCountersTable.add(buildingCounterLabels[3]);

    // Info Table
    timerLabel = new Label(timer.getRemainingTime(), skin);
    infoTable.center().center();
    pauseButtonCell = infoTable.add(playImage).align(Align.center);
    timerLabelCell = infoTable.add(timerLabel).align(Align.center);
    scoreLabelCell = infoTable.add(scoreLabel).align(Align.center);
    buildingCountersTableCell = infoTable.add(buildingCountersTable).expandX().align(Align.right);

    //event labels to table
      infoTable.row();
      infoTable.add(eventTitleLabel).colspan(4).align(Align.left).padTop(10);
      infoTable.row();
      infoTable.add(eventDescriptionLabel).colspan(4).align(Align.left).padTop(5);



      // Pause button
    pauseImage.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        GameState.paused = true;
        pauseButtonCell.setActor(playImage);
      }
    });

    // Play button
    playImage.addListener(new ClickListener() {
      @Override
      public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
        GameState.paused = false;
        pauseButtonCell.setActor(pauseImage);
      }
    });

    titleTable.add(titleLabel).expandX().align(Align.center);

    bar = new ShapeActor(GameState.UIPrimaryColour);
    stage.addActor(bar);
    stage.addActor(infoTable);
    stage.addActor(titleTable);
  }

    /**
     * Sets the event title in the InfoBar.
     *
     * @param title The title of the event.
     */
    public void setEventTitle(String title) {
        eventTitleLabel.setText("Event: " + title);
    }

    /**
     * Retrieves the current event title from the InfoBar.
     *
     * @return The current event title.
     */
    public String getEventTitle() {
        return eventTitleLabel.getText().toString();
    }

    /**
     * Sets the event description in the InfoBar.
     *
     * @param description The description of the event.
     */
    public void setEventDescription(String description) {
        eventDescriptionLabel.setText(description);
    }



  /**
   * Called when the UI needs to be updated, usually on every frame.
   */
  public void update() {
    timerLabel.setText(timer.getRemainingTime());
    buildingCounterLabels[0].setText("Recreation: "
        + Integer.toString(world.getBuildingCount(BuildingType.RECREATION)));
    buildingCounterLabels[1].setText("Learning: "
        + Integer.toString(world.getBuildingCount(BuildingType.LEARNING)));
    buildingCounterLabels[2].setText("Eating: "
        + Integer.toString(world.getBuildingCount(BuildingType.EATING)));
    buildingCounterLabels[3].setText("Sleeping: "
        + Integer.toString(world.getBuildingCount(BuildingType.SLEEPING)));
    pauseButtonCell.setActor(GameState.paused ? playImage : pauseImage);
  }

  /**
   * Update the bounds of the background & table actors to fit the new size of the screen.

   * @param width - The new width of the screen in pixels.
   * @param height - The enw height of the screen in pixels.
   */
  public void resize(int width, int height) {

      float infoBarHeight = height * 0.1f;
      bar.setBounds(0, height - infoBarHeight, width, infoBarHeight);
      infoTable.setBounds(0, height - infoBarHeight, width, infoBarHeight);
      titleTable.setBounds(0, height - infoBarHeight, width, infoBarHeight);

      float counterTableWidth = infoBarHeight * 2.7f;
      buildingCountersTableCell.width(counterTableWidth).height(infoBarHeight);
      for (int i = 0; i < 4; i++) {
          buildingCounterLabels[i].setFontScale(infoBarHeight * 0.015f); // Adjust font size
          buildingCounterCells[i].width(counterTableWidth * 0.5f).height(infoBarHeight * 0.25f);
      }

      timerLabel.setFontScale(infoBarHeight * 0.02f); // Adjust timer label font size
      timerLabelCell.width(infoBarHeight * 0.8f).height(infoBarHeight);
      timerLabelCell.padLeft(infoBarHeight * 0.05f);

      scoreLabel.setFontScale(infoBarHeight * 0.02f); // Adjust score label font size
      scoreLabelCell.width(infoBarHeight * 0.4f).height(infoBarHeight);
      scoreLabelCell.padLeft(Math.min(width, infoBarHeight * 20) * 0.14f);

      pauseButtonCell.width(infoBarHeight * 0.3f).height(infoBarHeight * 0.3f)
          .padLeft(infoBarHeight * 0.1f).padRight(infoBarHeight * 0.1f);

      titleLabel.setFontScale(infoBarHeight * 0.005f); // Adjust title label font size
  }


    public void reset() {
    pauseButtonCell.setActor(playImage);
  }
}
