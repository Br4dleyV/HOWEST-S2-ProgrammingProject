package be.howest.ti.game.logic.classes;

import org.junit.jupiter.api.Test;

import be.howest.ti.game.logic.enums.GemType;

import static org.junit.jupiter.api.Assertions.*;

class GemTypeTest {
    @Test
    void testEnumValues() {
        assertNotNull(GemType.valueOf("DIAMOND"));
        assertNotNull(GemType.valueOf("RUBY"));
        assertNotNull(GemType.valueOf("SAPPHIRE"));
        assertNotNull(GemType.valueOf("EMERALD"));
        assertNotNull(GemType.valueOf("ONYX"));
        assertNotNull(GemType.valueOf("GOLD"));
    }
}