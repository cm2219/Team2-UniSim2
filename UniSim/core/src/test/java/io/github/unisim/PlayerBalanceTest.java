package io.github.unisim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerBalanceTest {

    @Test
    void testAddBalance() {
        PlayerBalance b = new PlayerBalance(500);
        b.updateBalance(25);
        assertEquals(525, b.getBalance());
    }

    @Test
    void testSubtractBalance() {
        PlayerBalance b = new PlayerBalance(500);
        b.updateBalance(-25);
        assertEquals(475, b.getBalance());

        b = new PlayerBalance(500);
        b.updateBalance(-501);
        assertEquals(-1, b.getBalance());
    }

}
