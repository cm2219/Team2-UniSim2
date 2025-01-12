package io.github.unisim;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardTest {

    private static final String TEST_CSV_FILE = "test_leaderboard.csv";

    @Test
    void testScoreUpdate() {
        Leaderboard lb = new Leaderboard();
        lb.updateScores(1000, "A");
        lb.updateScores(2000, "B");
        lb.updateScores(500, "C");
        lb.updateScores(300, "D");
        lb.toCsvFile(TEST_CSV_FILE);

        File f = new File(TEST_CSV_FILE);
        f.delete();

        assertEquals(4, lb.getFormattedTopScores().size());
    }

    @Test
    void testScoreOrder() {
        Leaderboard lb = new Leaderboard();
        lb.updateScores(1000, "A");
        lb.updateScores(2000, "B");
        lb.updateScores(500, "C");
        lb.updateScores(300, "D");
        lb.toCsvFile(TEST_CSV_FILE);

        System.out.println(lb.getFormattedTopScores());
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
            "1. B - 2000", "2. A - 1000", "3. C - 500", "4. D - 300"));
        assertEquals(expected, lb.getFormattedTopScores());

        File f = new File(TEST_CSV_FILE);
        f.delete();
    }

    @Test
    void testNewTopFiveScore() {
        Leaderboard lb = new Leaderboard();
        lb.updateScores(1000, "A");
        lb.updateScores(2000, "B");
        lb.updateScores(500, "C");
        lb.updateScores(300, "D");
        lb.updateScores(1500, "E");
        lb.toCsvFile(TEST_CSV_FILE);

        lb.updateScores(4000, "F");

        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
            "1. F - 4000", "2. B - 2000", "3. E - 1500", "4. A - 1000", "5. C - 500"));
        assertEquals(expected, lb.getFormattedTopScores());

        File f = new File(TEST_CSV_FILE);
        f.delete();
    }
}
