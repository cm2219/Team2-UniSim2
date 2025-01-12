package io.github.unisim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void testSameValues() {
        Point p = new Point(0, 0);
        Point q = new Point(0, 0);
        assertEquals(p, q);
    }

    @Test
    void testDifferentValues() {
        Point p = new Point(123, 456);
        Point q = new Point(456, 123);
        assertNotEquals(p, q);
    }

    @Test
    void testStringRepresentation() {
        assertEquals("(1, 23)", new Point(1, 23).toString());
    }
}
