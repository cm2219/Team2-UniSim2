package io.github.unisim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TimerTest {

    @Test
    void testNumberFormat() {
       Timer t = new Timer(30000, 5);
       assertEquals("03:00", t.getRemainingTime());
    }

    @Test
    void testEventNumberStartsAtZero() {
        Timer t = new Timer(3000, 5);
        assertEquals(0, t.getEventNumber());
    }

    @Test
    void testTickTimerByOneSecond() {
        Timer t = new Timer(30000, 5);
        t.tick(1000);
        assertEquals("02:59", t.getRemainingTime());
    }

    @Test
    void testTickTimerToZero() {
        Timer t = new Timer(30000, 5);
        for (int i = 0; i < 6; ++i) {
            t.tick(30000);
        }
        assertEquals("00:00", t.getRemainingTime());
    }

    @Test
    void testResetTimer() {
        Timer t = new Timer(30000, 5);
        t.tick(30000);
        t.tick(30000);
        t.reset();
        assertEquals("03:00", t.getRemainingTime());
    }

    @Test
    void testEventNumberIncreases() {
        Timer t = new Timer(30000, 5);
        t.tick(30000);
        assertEquals(1, t.getEventNumber());
    }
}
