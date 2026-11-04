package be.howest.ti.game.logic.classes;

import be.howest.ti.game.logic.enums.GemType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testConstructorWithAllFields() {
        TokenCollection cost = new TokenCollection();
        Card card = new Card("TestCard", 2, cost, GemType.EMERALD, 3);

        assertEquals("TestCard", card.getName());
        assertEquals(2, card.getLevel());
        assertEquals(cost, card.getCost());
        assertEquals(GemType.EMERALD, card.getBonus());
        assertEquals(3, card.getPrestigePoints());
    }

    @Test
    void testConstructorWithNameOnly() {
        Card card = new Card("DefaultCard");

        assertEquals("DefaultCard", card.getName());
        assertEquals(1, card.getLevel()); // Make sure Card's default level is 1
        assertNotNull(card.getCost());
        assertEquals(GemType.RUBY, card.getBonus()); // Make sure Card's default bonus is RUBY
        assertEquals(1, card.getPrestigePoints()); // Make sure Card's default prestige is 1
    }

    @Test
    void testGetters() {
        TokenCollection cost = new TokenCollection();
        Card card = new Card("CardA", 3, cost, GemType.DIAMOND, 5);

        assertEquals("CardA", card.getName());
        assertEquals(3, card.getLevel());
        assertEquals(cost, card.getCost());
        assertEquals(GemType.DIAMOND, card.getBonus());
        assertEquals(5, card.getPrestigePoints());
    }

    @Test
    void testToString() {
        TokenCollection cost = new TokenCollection();
        Card card = new Card("CardB", 1, cost, GemType.SAPPHIRE, 2);

        String str = card.toString();
        assertTrue(str.contains("CardB"));
        assertTrue(str.contains("1")); // level
        assertTrue(str.contains("cost"));
        assertTrue(str.contains("SAPPHIRE"));
        assertTrue(str.contains("2")); // prestigePoints
    }

    @Test
    void testEqualsAndHashCode() {
        TokenCollection cost = new TokenCollection();
        Card card1 = new Card("CardA", 1, cost, GemType.DIAMOND, 2);
        Card card2 = new Card("CardA", 1, cost, GemType.DIAMOND, 2);
        Card card3 = new Card("CardB", 1, cost, GemType.DIAMOND, 2);
        assertEquals(card1, card2);
        assertEquals(card1.hashCode(), card2.hashCode());
        assertNotEquals(card1, card3);
    }

    @Test
    void testEqualsWithDifferentTypesAndNull() {
        TokenCollection cost = new TokenCollection();
        Card card = new Card("CardX", 1, cost, GemType.ONYX, 1);

        assertNotEquals(null, card);
        assertNotEquals("Some String", card);
        assertEquals(card, card); // same reference
    }

    @Test
    void testHashCodeConsistency() {
        Card card = new Card("ConsistentCard");
        int hash1 = card.hashCode();
        int hash2 = card.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void testEqualsWithSameNameDifferentFields() {
        TokenCollection cost1 = new TokenCollection();
        TokenCollection cost2 = new TokenCollection();
        Card card1 = new Card("SameName", 1, cost1, GemType.RUBY, 1);
        Card card2 = new Card("SameName", 2, cost2, GemType.EMERALD, 5);
        assertEquals(card1, card2);
        assertEquals(card1.hashCode(), card2.hashCode());
    }
}