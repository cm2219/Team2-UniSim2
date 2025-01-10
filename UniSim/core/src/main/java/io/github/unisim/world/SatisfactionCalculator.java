package io.github.unisim.world;

import io.github.unisim.building.Building;
import io.github.unisim.building.BuildingManager;
import io.github.unisim.building.BuildingType;
import io.github.unisim.GameState;

import java.util.Map;

public class SatisfactionCalculator {

    /**
     * Calculates and updates the satisfaction score when a new building is placed.
     *
     * @param newBuilding The newly placed building.
     * @param buildingsManager The manager handling existing buildings and placement logic.
     */
    public static void calculateAndUpdateSatisfaction(Building newBuilding, BuildingManager buildingsManager) {
        // Base satisfaction for any new building placement.
        int baseSatisfaction = 5;

        // Check if this is the first building of its type.
        boolean isFirstBuilding = buildingsManager.getBuildingCount(BuildingType.LEARNING) == 0 &&
            buildingsManager.getBuildingCount(BuildingType.RECREATION) == 0 &&
            buildingsManager.getBuildingCount(BuildingType.EATING) == 0 &&
            buildingsManager.getBuildingCount(BuildingType.SLEEPING) == 0;

        // Directly increase satisfaction for the first building.
        if (isFirstBuilding) {
            GameState.increaseSatisfaction(baseSatisfaction);
            System.out.println("Building: " + newBuilding.name + ", Base: " + baseSatisfaction +
                ", Bonus: 0, Final: " + baseSatisfaction);
            return;
        }
        // Define weightings for distance-based bonuses.
        Map<String, Double> typeWeights = Map.of(
            "Library", 0.3,
            "Student Accomodation", 0.2,
            "Canteen", 0.25,
            "Basketball Court", 0.15,
            "Swimming Pool", 0.4
        );

        double totalDistanceBonus = 0.0;

        double nearestDistance = 0;
        for (Map.Entry<String, Double> entry : typeWeights.entrySet()) {
            String buildingName = entry.getKey();
            double weight = entry.getValue();
            nearestDistance = buildingsManager.calculateNearestDistance(newBuilding, buildingName);

            double distanceBonus = 0.0;
            if (nearestDistance != Double.MAX_VALUE) {
                // Calculate bonus inversely proportional to distance.
                distanceBonus = (weight * 35) / Math.sqrt(1 + nearestDistance);
            }
            totalDistanceBonus += distanceBonus;
        }
        // Cap the bonus to prevent excessive satisfaction increases.
        totalDistanceBonus = Math.min(totalDistanceBonus, 10);

        int finalIncrease = (int) (baseSatisfaction + totalDistanceBonus);

        // Update the game's satisfaction score.
        GameState.increaseSatisfaction(finalIncrease);

        System.out.println("Building: " + newBuilding.name + ", Base: " + baseSatisfaction +
            ", Bonus: " + totalDistanceBonus + ", Final: " + finalIncrease);
    }

}
