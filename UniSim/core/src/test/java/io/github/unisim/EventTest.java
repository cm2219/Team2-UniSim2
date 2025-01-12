package io.github.unisim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    @Test
    void testEventZeroIsTutorial() {
        Event e = new Event(0, 0);
        assertEquals("Tutorial", e.getEventTitle());
    }

    @Test
    void testEvenEventsAddFunding() {
        Event e = new Event(2, 0);
        assertTrue(e.getEventDescription().startsWith("Funding"));
    }
}
