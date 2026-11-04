package be.howest.ti.game.logic.classes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NoblesTest {
    @Test
    void testGetNobles() {
        Nobles nobles = new Nobles();
        assertNotNull(nobles.getNobles());
    }
}
