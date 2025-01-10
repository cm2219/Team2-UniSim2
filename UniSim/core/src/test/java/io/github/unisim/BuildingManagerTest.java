package io.github.unisim;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import io.github.unisim.building.Building;
import io.github.unisim.building.BuildingManager;
import io.github.unisim.building.BuildingType;

import io.github.unisim.world.World;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mockito;


public class BuildingManagerTest {
    private final BuildingManager bm = new BuildingManager(new Matrix4());

    @Test
    public void testRecreationBuilding() {
        Building b = Mockito.mock(Building.class);
        Mockito.when(b.getLocation()).thenReturn(new Point(0,0));
        Mockito.when(b.getSize()).thenReturn(new Point(1,1));
        Mockito.when(b.getType()).thenReturn(BuildingType.RECREATION);
        bm.placeBuilding(b);
        assertEquals(1, bm.getBuildingCount(BuildingType.RECREATION));
    }

    @Test
    public void testEatingBuilding() {
        Building b = Mockito.mock(Building.class);
        Mockito.when(b.getLocation()).thenReturn(new Point(0,0));
        Mockito.when(b.getSize()).thenReturn(new Point(1,1));
        Mockito.when(b.getType()).thenReturn(BuildingType.EATING);
        bm.placeBuilding(b);
        assertEquals(1, bm.getBuildingCount(BuildingType.EATING));
    }

    @Test
    public void testLearningBuilding() {
        Building b = Mockito.mock(Building.class);
        Mockito.when(b.getLocation()).thenReturn(new Point(0,0));
        Mockito.when(b.getSize()).thenReturn(new Point(1,1));
        Mockito.when(b.getType()).thenReturn(BuildingType.LEARNING);
        bm.placeBuilding(b);
        assertEquals(1, bm.getBuildingCount(BuildingType.LEARNING));
    }

    @Test
    public void testSleepingBuilding() {
        Building b = Mockito.mock(Building.class);
        Mockito.when(b.getLocation()).thenReturn(new Point(0,0));
        Mockito.when(b.getSize()).thenReturn(new Point(1,1));
        Mockito.when(b.getType()).thenReturn(BuildingType.SLEEPING);
        bm.placeBuilding(b);
        assertEquals(1, bm.getBuildingCount(BuildingType.SLEEPING));
    }
}
