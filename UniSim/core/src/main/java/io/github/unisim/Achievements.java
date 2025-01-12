package io.github.unisim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;
import io.github.unisim.building.BuildingType;
import io.github.unisim.ui.InfoBar;
import io.github.unisim.world.SatisfactionCalculator;
import java.util.Map;
import java.util.HashMap;

public class Achievements {
    private static Achievements instance;
    public static class AchievementData {
        public String description;
        public boolean achieved;
        public int effect;
        private static Map<String, Boolean> achievedAchievements = new HashMap<>();

        public static boolean isAchieved(String title) {
            return achievedAchievements.getOrDefault(title, false);
        }

        public static void unlockAchievement(String title) {
            if (!achievedAchievements.containsKey(title)) {
                achievedAchievements.put(title, true);
            }
        }

        public AchievementData(String description, boolean achieved, int effect) {
            this.description = description;
            this.achieved = achieved;
            this.effect = effect;
        }

    }

    public final TreeMap<String, AchievementData> achievements;

    public Achievements() {
        achievements = new TreeMap<>();
        initializeDefaultAchievements();
    }
    //instance access
    public static Achievements getInstance(){
        if (instance == null){
            instance = new Achievements();
        }
        return instance;
    }

    private void initializeDefaultAchievements() {
        achievements.put("Perfect Balance",
            new AchievementData("Finish the game with exactly $0", false, 30));

        achievements.put("High Scorer",
            new AchievementData("Score over 80% in a game", false, 30));

        achievements.put("Rock Bottom",
            new AchievementData("Score less than 10% in a game", false, -20));

        achievements.put("Master Builder",
            new AchievementData("Place at least one of each building type", false, 10));
    }

    // Method to mark an achievement as completed
    public void completeAchievement(String title) {
        if (achievements.containsKey(title)) {
            AchievementData current = achievements.get(title);
            achievements.put(title, new AchievementData(
                current.description,
                true,  // Mark as achieved
                current.effect
            ));
//            System.out.println("Achievement unlocked: " + title);  // Log achievement unlock to the console
        }
    }

    //Method to check all achievements
    public void checkAllAchievements() {
        checkMasterBuilder();
        checkScoreAchievements();
        checkMoneyBalance();
    }

    // Method to check if all building types have been placed
    public void checkMasterBuilder() {
        boolean hasEating = GameState.buildingCounts.getOrDefault(BuildingType.EATING, 0) > 0;
        boolean hasLearning = GameState.buildingCounts.getOrDefault(BuildingType.LEARNING, 0) > 0;
        boolean hasRecreation = GameState.buildingCounts.getOrDefault(BuildingType.RECREATION, 0) > 0;
        boolean hasSleeping = GameState.buildingCounts.getOrDefault(BuildingType.SLEEPING, 0) > 0;

        ///trouble shooting
//        System.out.println("Building counts:");
//        System.out.println("Eating: " + hasEating);
//        System.out.println("Learning: " + hasLearning);
//        System.out.println("Recreation: " + hasRecreation);
//        System.out.println("Sleeping: " + hasSleeping);

        if (hasEating && hasLearning && hasRecreation && hasSleeping) {
            completeAchievement("Master Builder");
        }
    }

    // Method to check score-based achievements
    public void checkScoreAchievements() {

        if (GameState.gameOver == true) {
            double satisfactionScore = GameState.satisfaction;

            if (satisfactionScore >= 80.0) {
                completeAchievement("High Scorer");
            }
            if (satisfactionScore < 10.0) {
                completeAchievement("Rock Bottom");
            }
        }
    }

    //Check balance related achievements. Add if game over
    public void checkMoneyBalance() {
        if (GameState.balance == 0) {
            completeAchievement("Perfect Balance");
        }
    }

    public void loadFromCsvFile(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("CSV file not found: " + fileName);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length == 4) {
                    String title = parts[0].trim().replace("\"", "");
                    boolean achieved = Boolean.parseBoolean(parts[1].trim());
                    String description = parts[2].trim().replace("\"", "");
                    int effect = Integer.parseInt(parts[3].trim());

                    achievements.put(title, new AchievementData(description, achieved, effect));
                }
            }

            System.out.println("Achievements successfully loaded from " + fileName);

        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in the file. Please check the file content.");
        }
    }

    public void toCsvFile() {
        File file = new File("achievements.csv");

        try {
            if (file.exists() && !file.delete()) {
                System.out.println("Failed to delete the existing file.");
                return;
            }

            try (FileWriter writer = new FileWriter(file)) {
                // Header row
                writer.write("Title,Achieved,Description,Effect\n");

                // Write data
                for (var entry : achievements.entrySet()) {
                    writer.write(String.format("\"%s\",%s,\"%s\",%d\n",
                        entry.getKey(),
                        entry.getValue().achieved,
                        entry.getValue().description,
                        entry.getValue().effect));
                }
            }

            System.out.println("Achievements successfully saved to achievements.csv");

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public void displayAchievements() {
        System.out.println("\nAchievements:");
        System.out.println("Title | Achieved | Description | Effect");
        System.out.println("-----------------------------------------");
        for (var entry : achievements.entrySet()) {
            System.out.printf("%s | %s | %s | %d\n",
                entry.getKey(),
                entry.getValue().achieved,
                entry.getValue().description,
                entry.getValue().effect);
        }
    }
    /**
     * to calculate total value effect of achievements
     *
     */
    public int calculateAchievementEffects() {
        int totalEffect = 0;

        for (var entry : achievements.entrySet()) {
            if (entry.getValue().achieved) {
                totalEffect += entry.getValue().effect;
            }
        }
        System.out.println(totalEffect);
        return totalEffect;
    }

    //to rerturn achievements maap
    public TreeMap<String, AchievementData> getAchievements(){
        return achievements;
    }


    //return achievement details for gameover screen
    public TreeMap<String, String> getAchievementData() {
        TreeMap<String, String> details = new TreeMap<>();
        for (var entry : achievements.entrySet()) {
            var data = entry.getValue();
            String status = data.achieved ? "unlocked" : "locked";
            String detail = String.format("Title: %s, Description: %s, Effect: %d, Status: %s",
                entry.getKey(), data.description, data.effect, status);
            details.put(entry.getKey(), detail);
        }
        return details;
    }





}
