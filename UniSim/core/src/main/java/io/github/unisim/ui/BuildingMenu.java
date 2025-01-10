package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import io.github.unisim.GameState;
import io.github.unisim.Point;
import io.github.unisim.building.Building;
import io.github.unisim.building.BuildingType;
import io.github.unisim.world.World;
import java.util.ArrayList;

/**
 * Menu used to place buildings in the world by clicking and dragging them
 * from the list onto the map.
 */
@SuppressWarnings({"MemberName", "AbbreviationAsWordInName"})
public class BuildingMenu {
  private World world;
  private ShapeActor bar = new ShapeActor(GameState.UISecondaryColour);
  private Table table;
  private ArrayList<Building> buildings = new ArrayList<>();
  private ArrayList<Image> buildingImages = new ArrayList<>();
  private ArrayList<String> buildingCosts = new ArrayList<>();
  private Label buildingInfoLabel = new Label(
      "", new Skin(Gdx.files.internal("ui/uiskin.json"))
  );
  private Table buildingInfoTable = new Table();

  /**
   * Create a Building Menu and attach its actors and components to the provided stage.
   * Also handles drawing buildings and their flipped variants

   * @param stage - The stage on which to draw the menu.
   */
  public BuildingMenu(Stage stage, World world) {
    this.world = world;
    // Set building images and sizes
    buildings.add(new Building(
        new Texture(Gdx.files.internal("buildings/restaurant.png")),
        0.01f,
        new Vector2(0.35f, -0.9f),
        new Point(),
        new Point(3, 3),
        false,
        BuildingType.EATING,
        "Canteen",
        1600
    ));
    buildings.add(new Building(
        new Texture(Gdx.files.internal("buildings/library.png")),
        0.0075f,
        new Vector2(1.8f, -4.6f),
        new Point(),
        new Point(20, 12),
        false,
        BuildingType.LEARNING,
        "Library",
        1800
    ));
    buildings.add(new Building(
        new Texture(Gdx.files.internal("buildings/basketballCourt.png")),
        0.0025f,
        new Vector2(1f, -2.4f),
        new Point(),
        new Point(6, 9),
        false,
        BuildingType.RECREATION,
        "Basketball Court",
        750
    ));
    buildings.add(new Building(
        new Texture(Gdx.files.internal("buildings/studentHousing.png")),
        0.108f,
        new Vector2(1.4f, -2.8f),
        new Point(),
        new Point(11, 11),
        false,
        BuildingType.SLEEPING,
        "Student Accomodation",
        1200
    ));
    buildings.add(new Building(
        new Texture(Gdx.files.internal("buildings/swimmingPool.png")),
        0.0025f,
        new Vector2(1f, -2.4f),
        new Point(),
        new Point(6, 9),
        false,
        BuildingType.RECREATION,
        "Swimming Pool",
        900
    ));

    table = new Table();
    // Add buldings to the table
    for (int i = 0; i < buildings.size(); i++) {
      final int buildingIndex = i;
      Building building = buildsings.get(i);

      //Name row
      table.row();
      Label nameLabel = new Label(building.name, new Skin(Gdx.files.internal("ui/uiskin.json")));
      table.add(nameLabel).pad(10).align(Align.center);

      //Image row
      Image buildingImage = new Image(building.texture);
      buildingImages.add(buildingImage);
      buildingImages.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent e, float x, float y) {
          if (world.selectedBuilding == buildings.get(buildingIndex)) {
            world.selectedBuilding = null;
          } else {
            world.selectedBuilding = buildings.get(buildingIndex);
            buildingInfoLabel.setText(world.selectedBuilding.name + " - Press 'R' to rotate");
            if (world.selectedBuilding.flipped) {
              world.selectedBuilding.flipped = false;
              int temp = world.selectedBuilding.size.x;
              world.selectedBuilding.size.x = world.selectedBuilding.size.y;
              world.selectedBuilding.size.y = temp;
              world.selectedBuildingUpdated = true;
            }
          }
        }
      });
      table.row();
      table.add(buildingImage).pad(10).align(Align.center);
    }
    //Build cost row
    Label buildCostLabel = new Label("$ " + building.buildCost, new Skin(Gdx.files.internal("ui/uiskin.json")));
    table.row();
    table.add(buildCostLabel).pad(10).align(Align.center);

    buildingInfoTable.add(buildingInfoLabel).expandX().align(Align.center);

    stage.addActor(bar);
    stage.addActor(table);
    stage.addActor(buildingInfoTable);
  }

  /**
   * Called when the window is resized, scales the building menu images with the window size.

   * @param width - The new width of the window in pixels
   * @param height - The new height of the window in pixels
   */
  @SuppressWarnings("unchecked")
  public void resize(int width, int height) {
    table.setBounds(0, 0, width, height * 0.2f); //old val: 0.01f
    bar.setBounds(0, 0, width, height * 0.2f); //old val: 0.01f
    buildingInfoTable.setBounds(0, height * 0.2f, width, height * 0.05f); //old val: 0.1f, 0.05f

    // we must perform an unchecked type conversion here
    // this is acceptable as we know our table only contains instances of Actors
    for (Cell<Actor> cell : table.getCells()) {
      Image buildingImage = (Image) (cell.getActor());
      Vector2 textureSize = new Vector2(buildingImage.getWidth(), buildingImage.getHeight());
      cell.width(
          height * 0.1f * (textureSize.x < textureSize.y ? textureSize.x / textureSize.y : 1)
      ).height(
          height * 0.1f * (textureSize.y < textureSize.x ? textureSize.y / textureSize.x : 1)
      );
    }

    buildingInfoLabel.setFontScale(height * 0.0015f);
  }

  /**
   * Called when the building menu needs to be redrawn with new values in the labels.
   */
  public void update() {
    if (GameState.gameOver) {
      buildingInfoLabel.setText("Game Over!");
    } else if (world.selectedBuilding == null) {
      buildingInfoLabel.setText("");
      
    }
  }

  public void reset() {
    buildingInfoLabel.setText("");
  }
}
