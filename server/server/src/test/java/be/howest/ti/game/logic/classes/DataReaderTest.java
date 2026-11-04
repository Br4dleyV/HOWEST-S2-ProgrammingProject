package be.howest.ti.game.logic.classes;

import be.howest.ti.game.logic.enums.GemType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataReaderTest {

    @Test
    void testReadNobles_returnsListWithExpectedType() {
        List<Noble> nobles = DataReader.readNobles("/data/nobles.txt");
        assertNotNull(nobles, "Nobles list should not be null");
        assertFalse(nobles.isEmpty(), "Nobles list should not be empty");
        for (Noble n : nobles) {
            assertNotNull(n.getName());
            assertNotNull(n.getNeededBonuses());
            assertTrue(n.getPrestigePoints() >= 0);
        }
    }

    @Test
    void testReadDevelopmentCards_returnsListWithExpectedType() {
        List<Card> cards = DataReader.readDevelopmentCards("/data/developments.txt");
        assertNotNull(cards, "Development cards list should not be null");
        assertFalse(cards.isEmpty(), "Development cards list should not be empty");
        for (Card c : cards) {
            assertNotNull(c.getName());
            assertTrue(c.getLevel() > 0, "Card level should be positive");
            assertNotNull(c.getBonus());
            assertTrue(c.getBonus() instanceof GemType);
            assertNotNull(c.getCost());
            assertTrue(c.getPrestigePoints() >= 0, "Prestige points should be non-negative");
        }
    }

    @Test
    void testTypeToGem_validTypes() throws Exception {
        var method = DataReader.class.getDeclaredMethod("typeToGem", String.class);
        method.setAccessible(true);
        assertEquals(GemType.DIAMOND, method.invoke(null, "C"));
        assertEquals(GemType.EMERALD, method.invoke(null, "E"));
        assertEquals(GemType.ONYX, method.invoke(null, "O"));
        assertEquals(GemType.RUBY, method.invoke(null, "R"));
        assertEquals(GemType.SAPPHIRE, method.invoke(null, "S"));
        assertEquals(GemType.GOLD, method.invoke(null, "X")); // fallback
    }

    @Test
    void testParseCostToTokens_validInput() throws Exception {
        var method = DataReader.class.getDeclaredMethod("parseCostToTokens", String.class);
        method.setAccessible(true);
        TokenCollection tc = (TokenCollection) method.invoke(null, "CCEERRS");
        assertEquals(2, tc.getTokens().get(GemType.DIAMOND));
        assertEquals(2, tc.getTokens().get(GemType.EMERALD));
        assertEquals(2, tc.getTokens().get(GemType.RUBY));
        assertEquals(1, tc.getTokens().get(GemType.SAPPHIRE));
    }
}
