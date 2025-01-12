package io.github.unisim.building;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import io.github.unisim.GameState;
import io.github.unisim.Point;
import io.github.unisim.world.SatisfactionCalculator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Manage the buildings placed in the world and methods common to all buildings.
 */
public class BuildingManager {
     // create a list of buildings which will be sorted by a height metric derived from
    // the locations of the corners of the buildings.
    private ArrayList<Building> buildings = new ArrayList<>();
    private Map<BuildingType, Integer> buildingCounts = new HashMap<>();
    private Matrix4 isoTransform;
    private Building previewBuilding;
    private boolean masterBuilderAchieved = false;

    public BuildingManager(Matrix4 isoTransform) {
        this.isoTransform = isoTransform;
    }

    /**
     * Determines if a region on the map is composed solely of buildable tiles.
     *
     * @param btmLeft   - The co-ordinates of the bottom left corner of the search region
     * @param topRight  - The co-ordinates of the top right corner of the search region
     * @param tileLayer - A reference to the map layer containing all terrain tiles
     * @return - true if the region is made solely of buildable tiles, false otherwise
     */
    public boolean isBuildable(Point btmLeft, Point topRight, TiledMapTileLayer tileLayer) {
        boolean buildable = true;
        for (int x = btmLeft.x; x <= topRight.x && buildable; x++) {
            for (int y = btmLeft.y; y <= topRight.y && buildable; y++) {
                Cell currentCell = tileLayer.getCell(x, y);
                if (currentCell == null) {
                    buildable = false;
                    continue;
                }

                TiledMapTile currentTile = currentCell.getTile();
                if (!tileBuildable(currentTile)) {
                    buildable = false;
                }
            }
        }
        if (!buildable) {
            return false;
        }

        for (Building building : buildings) {
            if (!(building.location.x > topRight.x
                || building.location.x + building.size.x - 1 < btmLeft.x
                || building.location.y > topRight.y
                || building.location.y + building.size.y - 1 < btmLeft.y)
            ) {
                if (building == previewBuilding) {
                    continue;
                }
                buildable = false;
                break;
            }
        }

        return buildable;
    }

    private static boolean tileBuildable(TiledMapTile tile) {
        return GameState.buildableTiles.contains(tile.getId());
    }

    public void render(SpriteBatch batch) {
        for (Building building : buildings) {
            drawBuilding(building, batch);
        }
    }

    public int placeBuilding(Building building) {
        if (GameState.paused) {
            return -1;
        }
        if (building == previewBuilding) {
            return -1; // Ignore preview building
        }

        int buildingHeightLeftSide = building.location.y - building.location.x;
        int buildingHeightRightSide = buildingHeightLeftSide + building.size.y - building.size.x + 1;
        Point leftCorner = building.location;

        int i = 0;
        while (i < buildings.size()) {
            Building other = buildings.get(i);
            int otherHeightLeftSide = other.location.y - other.location.x;
            int leftDistance = Math.abs(leftCorner.x - other.location.x - other.size.x + 1)
                + Math.abs(leftCorner.y - other.location.y - other.size.y + 1);

            if (leftDistance < Math.min(building.size.x + building.size.y, other.size.x + other.size.y)) {
                int otherHeightRightSide = otherHeightLeftSide + other.size.y - other.size.x + 1;
                if (otherHeightRightSide > buildingHeightLeftSide) {
                    i++;
                    continue;
                } else {
                    break;
                }
            }

            if (otherHeightLeftSide > buildingHeightRightSide) {
                i++;
            } else {
                break;
            }
        }
        buildings.add(i, building);
        updateCounters(building);

        // NEW TROUBLESHOOTING: Log the placed building's type
        System.out.println("Placed building of type: " + building.type);

        // NEW TROUBLESHOOTING: Log building counts after placement
        for (Map.Entry<BuildingType, Integer> entry : buildingCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        SatisfactionCalculator.calculateAndUpdateSatisfaction(building, this);

        // NEW TROUBLESHOOTING: Call the achievement check
        checkMasterBuilder();

        return i;
    }

    private void updateCounters(Building building) {
        if (building == previewBuilding) {
            return;
        }
        buildingCounts.put(building.type, buildingCounts.getOrDefault(building.type, 0) + 1);

        // NEW TROUBLESHOOTING: Log counter updates
        System.out.println("Updated counter for " + building.type + ": " + buildingCounts.get(building.type));
    }

    public int getBuildingCount(BuildingType type) {
        return buildingCounts.getOrDefault(type, 0);
    }

    public void setPreviewBuilding(Building previewBuilding) {
        if (this.previewBuilding != null) {
            buildings.remove(this.previewBuilding);
        }
        this.previewBuilding = previewBuilding;
        if (previewBuilding != null) {
            buildings.add(previewBuilding);
        }
    }

    public void drawBuilding(Building building, SpriteBatch batch) {
        Vector3 btmLeftPos = new Vector3(
            (float) building.location.x + building.textureOffset.x,
            (float) building.location.y + building.textureOffset.y,
            0f
        );
        Vector3 btmRightPos = new Vector3(btmLeftPos).add(new Vector3(building.size.x - 1, 0f, 0f));
        btmLeftPos.mul(isoTransform);
        btmRightPos.mul(isoTransform);
        batch.draw(
            building.texture,
            btmLeftPos.x, btmRightPos.y,
            building.texture.getWidth() * building.textureScale,
            building.texture.getHeight() * building.textureScale,
            0, 0, building.texture.getWidth(), building.texture.getHeight(),
            building.flipped, false
        );
    }

    public double calculateNearestDistance(Building newBuilding, BuildingType targetType) {
        double nearestDistance = Double.MAX_VALUE;
        for (Building building : buildings) {
            if (building.type != targetType) {
                continue;
            }
            double distance = Math.sqrt(
                Math.pow(newBuilding.location.x - building.location.x, 2) +
                    Math.pow(newBuilding.location.y - building.location.y, 2)
            );
            if (distance < nearestDistance) {
                nearestDistance = distance;
            }
        }
        return nearestDistance == Double.MAX_VALUE ? 50 : nearestDistance;
    }

    public void checkMasterBuilder() {
        System.out.println("Checking Master Builder achievement..."); // NEW TROUBLESHOOTING

        boolean hasEating = getBuildingCount(BuildingType.EATING) > 0;
        boolean hasLearning = getBuildingCount(BuildingType.LEARNING) > 0;
        boolean hasRecreation = getBuildingCount(BuildingType.RECREATION) > 0;
        boolean hasSleeping = getBuildingCount(BuildingType.SLEEPING) > 0;

        // NEW TROUBLESHOOTING: Log building type counts
        System.out.println("EATING count: " + getBuildingCount(BuildingType.EATING));
        System.out.println("LEARNING count: " + getBuildingCount(BuildingType.LEARNING));
        System.out.println("RECREATION count: " + getBuildingCount(BuildingType.RECREATION));
        System.out.println("SLEEPING count: " + getBuildingCount(BuildingType.SLEEPING));

        if (hasEating && hasLearning && hasRecreation && hasSleeping) {
            if (!masterBuilderAchieved) {
                completeAchievement("Master Builder");
                masterBuilderAchieved = true; // Set the achievement flag to true
            }
        }
    }

    private void completeAchievement(String title) {
        System.out.println("Achievement unlocked: " + title); // Log the unlocked achievement
    }
}
