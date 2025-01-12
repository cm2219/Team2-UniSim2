package io.github.unisim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.unisim.building.BuildingType;
import io.github.unisim.ui.GameScreen;
import io.github.unisim.ui.LeaderboardScreen;
import io.github.unisim.ui.StartMenuScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import io.github.unisim.ui.AchievementsScreen;


/**
 * Contains a collection of settings and references that should be available globally.
 */
public class GameState {
    public static Color UIPrimaryColour = new Color(0.250f, 0.326f, 0.865f, 1.0f);
    public static Color UISecondaryColour = new Color(0.722f, 0.646f, 0.953f, 1.0f);
    public static Skin defaultSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    public static InputProcessor fullscreenInputProcessor = new FullscreenInputProcessor();
    public static Screen gameScreen = new GameScreen();
    public static Screen leaderboardScreen = new LeaderboardScreen();
    public static Screen achievementsScreen = new AchievementsScreen();
    public static Screen startScreen = new StartMenuScreen();
    public static Screen currentScreen;
    // Create an unmodifiable set containing the IDs of all buildable tiles
    // we use a set to make searching more efficient
    public static Set<Integer> buildableTiles = Stream.of(
        14, 15).collect(Collectors.toUnmodifiableSet()
    );
    public static boolean paused = true;
    public static boolean gameOver = false;
    public static int satisfaction = 0; // the initial satisfaction score
    public static final int MAX_SATISFACTION = 100;
    public static int balance = 5000;
    public static Map<BuildingType, Integer> buildingCounts = new HashMap<>();
    /**
     * update the satisfaction, but only increasing
     * @param delta increasement
     */
    public static void increaseSatisfaction(int delta) {
        int previousSatisfaction = satisfaction;
        satisfaction += delta;
        if (satisfaction > MAX_SATISFACTION) {
            satisfaction = MAX_SATISFACTION;
        }
        System.out.println("Previous Satisfaction: " + previousSatisfaction +
            ", Delta: " + delta +
            ", New Satisfaction: " + satisfaction);
    }


    public static void updateBalance(int delta) {

        balance += delta;
        if (balance < 0) {
            balance = 0;
        }
    }

    public static void resetGameValues() {
        satisfaction = 0;
        balance = 5000;
        Map<BuildingType, Integer> buildingCounts = new HashMap<>();
    }

}
