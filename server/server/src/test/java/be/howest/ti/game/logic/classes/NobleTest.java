package be.howest.ti.game.logic.classes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NobleTest {

    @Test
    void testConstructorAndGetters() {
        TokenCollection neededBonuses = new TokenCollection();
        Noble noble = new Noble("Duke", neededBonuses, 3);

        assertEquals("Duke", noble.getName());
        assertEquals(neededBonuses, noble.getNeededBonuses());
        assertEquals(3, noble.getPrestigePoints());
    }

    @Test
    void testToStringContainsFields() {
        TokenCollection neededBonuses = new TokenCollection();
        Noble noble = new Noble("Baron", neededBonuses, 2);

        String str = noble.toString();
        assertTrue(str.contains("Baron"));
        assertTrue(str.contains("neededBonuses"));
        assertTrue(str.contains("2"));
    }

    @Test
    void testDifferentNobleInstances() {
        TokenCollection bonuses1 = new TokenCollection();
        TokenCollection bonuses2 = new TokenCollection();
        Noble noble1 = new Noble("Queen", bonuses1, 4);
        Noble noble2 = new Noble("King", bonuses2, 5);

        assertNotEquals(noble1.getName(), noble2.getName());
        assertNotEquals(noble1.getPrestigePoints(), noble2.getPrestigePoints());
        assertNotSame(noble1.getNeededBonuses(), noble2.getNeededBonuses());
    }

    @Test
    void testEqualsAndHashCode() {
        TokenCollection bonuses = new TokenCollection();
        Noble noble1 = new Noble("Duke", bonuses, 3);
        Noble noble2 = new Noble("Duke", bonuses, 3);
        Noble noble3 = new Noble("Baron", bonuses, 2);

        assertEquals(noble1, noble2);
        assertEquals(noble1.hashCode(), noble2.hashCode());
        assertNotEquals(noble1, noble3);
    }

    @Test
    void testEqualsWithNullAndOtherClass() {
        TokenCollection bonuses = new TokenCollection();
        Noble noble = new Noble("Duke", bonuses, 3);

        assertNotEquals(null, noble);
        assertNotEquals("not a noble", noble);
    }

    @Test
    void testToStringNotNull() {
        TokenCollection bonuses = new TokenCollection();
        Noble noble = new Noble("Duke", bonuses, 3);

        assertNotNull(noble.toString());
    }
}