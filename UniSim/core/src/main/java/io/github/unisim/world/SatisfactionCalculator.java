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
            buildingsManager.getBuildingCount(BuildingType.EATING) == 0;

        // Directly increase satisfaction for the first building.
        if (isFirstBuilding) {
            GameState.increaseSatisfaction(baseSatisfaction);
            System.out.println("Building: " + newBuilding.name + ", Base: " + baseSatisfaction +
                ", Bonus: 0, Final: " + baseSatisfaction);
            return;
        }
        // Define weightings for distance-based bonuses.
        Map<BuildingType, Double> typeWeights = Map.of(
            BuildingType.LEARNING, 0.3,
            BuildingType.RECREATION, 0.3,
            BuildingType.EATING, 0.3
        );

        double totalDistanceBonus = 0.0;

        for (Map.Entry<BuildingType, Double> entry : typeWeights.entrySet()) {
            BuildingType type = entry.getKey();
            double weight = entry.getValue();
            double nearestDistance = buildingsManager.calculateNearestDistance(newBuilding, type);

            double distanceBonus = 0.0;
            if (nearestDistance != Double.MAX_VALUE) {
                // Calculate bonus inversely proportional to distance.
                distanceBonus = (weight * 20) / Math.sqrt(1 + nearestDistance);
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

    /**
     * Retrieves the current satisfaction score from the game state.
     *
     * @return The current satisfaction score.
     */
    public static double getSatisfaction() {
        return GameState.satisfaction;
    }
}
