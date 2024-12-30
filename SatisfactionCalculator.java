package io.github.unisim.satisfaction;

import io.github.unisim.building.Building;
import io.github.unisim.building.BuildingManager;
import io.github.unisim.building.BuildingType;
import io.github.unisim.GameState;

import java.util.Map;

public class SatisfactionCalculator {

    /**
     *
     * @param newBuilding
     * @param buildingsManager
     */
    public static void calculateAndUpdateSatisfaction(Building newBuilding, BuildingManager buildingsManager) {
        int baseSatisfaction = 5;

        boolean isFirstBuilding = buildingsManager.getBuildingCount(BuildingType.LEARNING) == 0 &&
            buildingsManager.getBuildingCount(BuildingType.RECREATION) == 0 &&
            buildingsManager.getBuildingCount(BuildingType.EATING) == 0;

        if (isFirstBuilding) {
            GameState.increaseSatisfaction(baseSatisfaction);
            System.out.println("Building: " + newBuilding.name + ", Base: " + baseSatisfaction +
                ", Bonus: 0, Final: " + baseSatisfaction);
            return;
        }

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
                distanceBonus = (weight * 20) / Math.sqrt(1 + nearestDistance);
            }
            totalDistanceBonus += distanceBonus;
        }

        totalDistanceBonus = Math.min(totalDistanceBonus, 10); // 防止 Bonus 过高

        int finalIncrease = (int) (baseSatisfaction + totalDistanceBonus);

        GameState.increaseSatisfaction(finalIncrease);

        System.out.println("Building: " + newBuilding.name + ", Base: " + baseSatisfaction +
            ", Bonus: " + totalDistanceBonus + ", Final: " + finalIncrease);
    }

    public double getSatisfaction() {
        return GameState.satisfaction;
    }
}
