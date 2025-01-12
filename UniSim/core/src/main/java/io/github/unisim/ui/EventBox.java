package io.github.unisim.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.unisim.GameState;
import io.github.unisim.Timer;
import io.github.unisim.Event;

/**
 * Create a Title bar with basic info.
 */
public class EventBox {
    private ShapeActor bar;
    private Table eventTable = new Table();
    private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    private Label titleLabel = new Label("Tutorial", skin);
    private Label descriptionLabel = new Label("Welcome to your university, select a building from the bottom to place it. Be careful to manage funds", skin);
    private Label moneyLabel = new Label("", skin);
    private Label pointsLabel = new Label("", skin);
    private Timer timer;
    private Cell<Label> titleLabelCell;
    private Cell<Label> descriptionLabelCell;
    private Cell<Label> moneyLabelCell;
    private Cell<Label> pointsLabelCell;
    private int currentEventNumber;
    private Event currentEvent;

    /**
     * Create a new eventBox and draws its' components onto the provided stage.

     //     * @param stage - The stage on which to draw the eventBox.
     */
    public EventBox(Stage stage, Timer timer) {
        this.timer = timer;
        currentEventNumber = 0;

        // Event Table
        titleLabelCell = eventTable.add(titleLabel);
        titleLabel.setWrap(true);
        eventTable.row();
        descriptionLabelCell = eventTable.add(descriptionLabel);
        descriptionLabel.setWrap(true);
        eventTable.row();
        moneyLabelCell = eventTable.add(moneyLabel);
        eventTable.row();
        pointsLabelCell = eventTable.add(pointsLabel);

        //add actors
        bar = new ShapeActor(Color.SLATE);
        stage.addActor(bar);
        stage.addActor(eventTable);
    }

    /**
     * Called when the UI needs to be updated, usually on every frame.
     */
    public void update() {
        if (timer.getEventNumber() != currentEventNumber) {
            currentEvent = new Event(timer.getEventNumber(), GameState.satisfaction);
            titleLabel.setText(currentEvent.getEventTitle());
            descriptionLabel.setText(currentEvent.getEventDescription());
            moneyLabel.setText("Money: " + currentEvent.getEventMoney());
            pointsLabel.setText("Satisfaction: " + currentEventNumber);

            currentEventNumber = timer.getEventNumber();
            GameState.updateBalance((currentEvent.getEventMoney()));
            GameState.increaseSatisfaction(currentEvent.getEventPoints());
        }
    }

    /**
     * Update the bounds of the background & table actors to fit the new size of the screen.

     * @param width - The new width of the screen in pixels.
     * @param height - The enw height of the screen in pixels.
     */
    public void resize(int width, int height) {
        bar.setBounds(0, height * 0.635f, width * 0.22f, height * 0.315f);
        eventTable.setBounds(width * 0.01f, height * 0.77f, width * 0.2f, height * 0.05f);


        titleLabel.setFontScale(height * 0.002f);
        titleLabelCell.width(width * 0.2f);
        titleLabelCell.padBottom(height * 0.02f);
        descriptionLabel.setFontScale(height * 0.0015f);
        descriptionLabelCell.width(width * 0.2f);
        descriptionLabelCell.padBottom(height * 0.02f);
        moneyLabel.setFontScale(height * 0.002f);
        moneyLabelCell.width(width * 0.2f);
        moneyLabelCell.padBottom(height * 0.02f);
        pointsLabel.setFontScale(height * 0.002f);
        pointsLabelCell.width(width * 0.2f);


    }

    public void reset() {
        titleLabel.setText("Tutorial");
        descriptionLabel.setText("Welcome to your university, select a building from the bottom to place it. Be careful to manage funds");
        moneyLabel.setText("");
        pointsLabel.setText("");
        currentEventNumber = 0;
    }
}
