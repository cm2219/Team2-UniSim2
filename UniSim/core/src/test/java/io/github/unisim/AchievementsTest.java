package io.github.unisim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AchievementsTest {

    @Test
    public void testAchievementsExist() {
        Achievements a = new Achievements();
        assertFalse(a.getAchievements().isEmpty());
    }

    @Test
    public void testAchievementMarkAsComplete() {
        Achievements a = new Achievements();
        a.completeAchievement("Rock Bottom");
        assertTrue(a.getAchievements().get("Rock Bottom").achieved);
    }
}
