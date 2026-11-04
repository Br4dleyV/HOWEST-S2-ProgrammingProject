package be.howest.ti.game.logic.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.howest.ti.game.logic.enums.GemType;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {
    private Level level;

    @BeforeEach
    void setUp() {
        level = new Level(1);
    }

    @Test
    void testConstructorSetsLevelCorrectly() {
        assertEquals(1, level.getLevel());
    }

    @Test
    void testInitialCardStackSizeIsZero() {
        assertEquals(0, level.getCardStackSize());
    }

    @Test
    void testInitialVisibleCardsIsEmpty() {
        assertTrue(level.getVisibleCards().isEmpty());
    }

    @Test
    void testInitialUnclaimedCardsIsEmpty() {
        assertTrue(level.getUnclaimedCards().isEmpty());
    }

    @Test
    void testDecrementCardStackWhenGreaterThanZero() {
        // handmatig instellen via reflectie of alternatieve aanpak
        // tijdelijke test met subclass om toegang te krijgen tot beschermde waarden
        Level customLevel = new Level(1) {
            {
                try {
                    java.lang.reflect.Field f = Level.class.getDeclaredField("cardStackSize");
                    f.setAccessible(true);
                    f.setInt(this, 3);
                } catch (Exception e) {
                    fail("Failed to set field with reflection: " + e.getMessage());
                }
            }
        };
        customLevel.decrementCardStack();
        assertEquals(2, customLevel.getCardStackSize());
    }

    @Test
    void testCanAssignVisibleCards() {
        ArrayList<Card> visible = new ArrayList<>();
        visible.add(new Card("Test", 1, new TokenCollection(), GemType.RUBY, 0));
        try {
            java.lang.reflect.Field f = Level.class.getDeclaredField("visibleCards");
            f.setAccessible(true);
            f.set(level, visible);
        } catch (Exception e) {
            fail("Reflection failed");
        }

        assertEquals(1, level.getVisibleCards().size());
    }

    @Test
    void testCanAssignUnclaimedCards() {
        ArrayList<Card> unclaimed = new ArrayList<>();
        unclaimed.add(new Card("Test", 1, new TokenCollection(), GemType.RUBY, 0));
        try {
            java.lang.reflect.Field f = Level.class.getDeclaredField("unclaimedCards");
            f.setAccessible(true);
            f.set(level, unclaimed);
        } catch (Exception e) {
            fail("Reflection failed");
        }

        assertEquals(1, level.getUnclaimedCards().size());
    }
}