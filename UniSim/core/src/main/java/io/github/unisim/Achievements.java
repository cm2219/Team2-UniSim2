import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;
import io.github.unisim.building.BuildingManager;
import io.github.unisim.building.BuildingType;
import io.github.unisim.PlayerBalance;
import io.github.unisim.satisfaction.SatisfactionCalculator;

public class Achievements {
    private static class AchievementData {
        String description;
        boolean achieved;
        int effect;

        public AchievementData(String description, boolean achieved, int effect) {
            this.description = description;
            this.achieved = achieved;
            this.effect = effect;
        }
    }

    private final TreeMap<String, AchievementData> achievements;

    public Achievements() {
        this.achievements = new TreeMap<>();
        initializeDefaultAchievements();
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
            System.out.println("Achievement unlocked: " + title);
        }
    }

    // Method to check if all building types have been placed
    public void checkMasterBuilder(BuildingManager buildingManager) {
        boolean hasEating = buildingManager.getBuildingCount(BuildingType.EATING) > 0;
        boolean hasLearning = buildingManager.getBuildingCount(BuildingType.LEARNING) > 0;
        boolean hasRecreation = buildingManager.getBuildingCount(BuildingType.RECREATION) > 0;
        boolean hasSleeping = buildingManager.getBuildingCount(BuildingType.SLEEPING) > 0;

        if (hasEating && hasLearning && hasRecreation && hasSleeping) {
            completeAchievement("Master Builder");
        }
    }

    // Method to check score-based achievements
    public void checkScoreAchievements() {
        double satisfactionScore = satisfactionCalculator.getSatisfaction();
        
        if (satisfactionScore >= 80.0) {
            completeAchievement("High Scorer");
        }
        if (satisfactionScore < 10.0) {
            completeAchievement("Rock Bottom");
        }
    }

    //Check balance related achievements.
    public void checkMoneyBalance(PlayerBalance playerBalance) {
        if (playerBalance.getBalance() == 0) {
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

    // Main method for testing
    public static void main(String[] args) {
        Achievements achievements = new Achievements();
        
        // Display initial achievements
        achievements.displayAchievements();
        
        // Test completing some achievements
        achievements.checkScoreAchievements(85.0);  // Should unlock High Scorer
        achievements.checkMoneyBalance(0);   // Should unlock Perfect Balance
        
        // Test building placement
        boolean[] buildingTypes = {true, true, true, true}; // Example with 4 building types
        achievements.checkMasterBuilder(buildingTypes);
        
        // Display updated achievements
        System.out.println("\nAfter completing some achievements:");
        achievements.displayAchievements();
        
        // Save to CSV
        achievements.toCsvFile();
    }
}