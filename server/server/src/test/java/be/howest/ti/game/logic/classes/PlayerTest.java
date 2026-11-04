package be.howest.ti.game.logic.classes;

import org.junit.jupiter.api.Test;

import be.howest.ti.game.logic.enums.GemType;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class PlayerTest {

    private Player player;
    private final String playerName = "Alice";
    private final String playerToken = "token123";

    @BeforeEach
    void setUp() {
        player = new Player(playerName, playerToken);
    }

    @Test
    void testConstructorInitializesFields() {
        assertEquals(playerName, player.getName());
        assertFalse(player.isReady());
        assertNotNull(player.getBought());
        assertNotNull(player.getReserved());
        assertNotNull(player.getNobles());
        assertNotNull(player.getTokens());
        assertEquals(0, player.getTotalPrestigePoints());
        assertEquals(playerToken, player.getPlayerToken());
    }

    @Test
    void testToggleReady() {
        assertFalse(player.isReady());
        player.toggleReady();
        assertTrue(player.isReady());
        player.toggleReady();
        assertFalse(player.isReady());
    }

    @Test
    void testUpdateTokensAddsTokens() {
        // Use a stub TokenCollection that records if addTokens was called
        class TestTokenCollection extends TokenCollection {
            boolean addTokensCalled = false;

            @Override
            public void addTokens(TokenCollection other) {
                addTokensCalled = true;
            }
        }
        TestTokenCollection stubTokens = new TestTokenCollection();
        Player p = new Player("Bob", "tok2");
        try {
            var field = Player.class.getDeclaredField("tokens");
            field.setAccessible(true);
            field.set(p, stubTokens);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
        TokenCollection toAdd = new TokenCollection();
        p.updateTokens(toAdd);
        assertTrue(stubTokens.addTokensCalled);
    }

    @Test
    void testGetters() {
        assertEquals(playerName, player.getName());
        assertEquals(playerToken, player.getPlayerToken());
        assertEquals(0, player.getTotalPrestigePoints());
        assertNotNull(player.getBought());
        assertNotNull(player.getReserved());
        assertNotNull(player.getNobles());
        assertNotNull(player.getTokens());
    }

    @Test
    void testBuyCardAddsToBoughtAndUpdatesTokensAndPrestige() {
        Card card = new Card("Card1", 1, new TokenCollection(), GemType.DIAMOND, 2);
        Player p = new Player("Bob", "tok2");
        int initialBought = p.getBought().size();
        p.buyCard(card, new TokenCollection());
        assertEquals(initialBought + 1, p.getBought().size());
        assertEquals(2, p.getTotalPrestigePoints());
    }

    @Test
    void testReserveCardThrowsIfMoreThanThree() {
        Card card = new Card("Card1", 1, new TokenCollection(), GemType.DIAMOND, 2);
        Player p = new Player("Bob", "tok2");
        p.reserveCard(card);
        p.reserveCard(card);
        p.reserveCard(card);
        assertThrows(IllegalStateException.class, () -> p.reserveCard(card));
    }

    @Test
    void testTakeNobleAddsNobleAndUpdatesPrestige() {
        Noble noble = new Noble("Noble", new TokenCollection(), 3);
        Player p = new Player("Bob", "tok2") {
            @Override
            public boolean canTakeNoble(Noble n) {
                return true;
            }
        };
        int initialNobles = p.getNobles().size();
        p.takeNoble(noble);
        assertEquals(initialNobles + 1, p.getNobles().size());
        assertEquals(3, p.getTotalPrestigePoints());
    }

    @Test
    void testTakeNobleThrowsIfCannotTake() {
        Noble noble = new Noble("Noble", new TokenCollection(), 3);
        Player p = new Player("Bob", "tok2") {
            @Override
            public boolean canTakeNoble(Noble n) {
                return false;
            }
        };
        assertThrows(IllegalStateException.class, () -> p.takeNoble(noble));
    }

    @Test
    void testCanTakeNobleTrueAndFalse() {
        TokenCollection needed = new TokenCollection();
        needed.addTokens(GemType.DIAMOND, 0);
        Noble noble = new Noble("Noble", needed, 1);
        Player p = new Player("Bob", "tok2");
        assertTrue(p.canTakeNoble(noble));
        // Now require more than player has
        needed.addTokens(GemType.DIAMOND, 1);
        Noble noble2 = new Noble("Noble2", needed, 1);
        assertFalse(p.canTakeNoble(noble2));
    }

    @Test
    void testGetBonusesWithNoBoughtCards() {
        Player p = new Player("Bob", "tok2");
        TokenCollection bonuses = p.getBonuses();
        assertNotNull(bonuses);
        // Should be empty
        for (GemType type : GemType.values()) {
            assertEquals(0, bonuses.getAmount(type));
        }
    }

    @Test
    void testGetBonusesWithBoughtCards() {
        Player p = new Player("Bob", "tok2");
        Card card1 = new Card("C1", 1, new TokenCollection(), GemType.DIAMOND, 0);
        Card card2 = new Card("C2", 1, new TokenCollection(), GemType.EMERALD, 0);
        p.buyCard(card1, new TokenCollection());
        p.buyCard(card2, new TokenCollection());
        TokenCollection bonuses = p.getBonuses();
        assertEquals(1, bonuses.getAmount(GemType.DIAMOND));
        assertEquals(1, bonuses.getAmount(GemType.EMERALD));
    }

    @Test
    void testUpdateTotalPrestigePointsDirectly() {
        Player p = new Player("Bob", "tok2");
        Card card = new Card("C1", 1, new TokenCollection(), GemType.DIAMOND, 2);
        p.buyCard(card, new TokenCollection());
        Noble noble = new Noble("Noble", new TokenCollection(), 3);
        p.getNobles().add(noble);
        p.updateTotalPrestigePoints();
        assertEquals(5, p.getTotalPrestigePoints());
    }

    @Test
    void testBuyReserveCardWithCardNotInReservedDoesNothing() {
        Player p = new Player("Bob", "tok2");
        Card card = new Card("C1", 1, new TokenCollection(), GemType.DIAMOND, 2);
        // Not reserving the card
        int reservedBefore = p.getReserved().size();
        int boughtBefore = p.getBought().size();
        p.buyReserveCard(card, card.getCost()); // Should not throw, just remove nothing
        assertEquals(reservedBefore, p.getReserved().size());
        assertEquals(boughtBefore + 1, p.getBought().size());
    }

    @Test
    void testBuyCardRemovesTokens() {
        Player p = new Player("Bob", "tok2");
        TokenCollection cost = new TokenCollection();
        cost.addTokens(GemType.DIAMOND, 1);
        Card card = new Card("C1", 1, cost, GemType.DIAMOND, 2);
        p.getTokens().addTokens(GemType.DIAMOND, 1);
        p.buyCard(card, cost);
        assertEquals(0, p.getTokens().getAmount(GemType.DIAMOND));
    }

    @Test
    void testBuyReserveCardRemovesTokens() {
        Player p = new Player("Bob", "tok2");
        TokenCollection cost = new TokenCollection();
        cost.addTokens(GemType.DIAMOND, 1);
        Card card = new Card("C1", 1, cost, GemType.DIAMOND, 2);
        p.getTokens().addTokens(GemType.DIAMOND, 1);
        p.reserveCard(card);
        p.buyReserveCard(card, card.getCost());
        assertEquals(0, p.getTokens().getAmount(GemType.DIAMOND));
    }

    @Test
    void testCanTakeNobleReturnsTrueWhenBonusesSuffice() {
        Player p = new Player("Bob", "tok2");
        // Give player a bought card with DIAMOND bonus
        Card card = new Card("C1", 1, new TokenCollection(), GemType.DIAMOND, 0);
        p.buyCard(card, new TokenCollection());

        TokenCollection needed = new TokenCollection();
        needed.addTokens(GemType.DIAMOND, 1);
        Noble noble = new Noble("Noble", needed, 3);

        assertTrue(p.canTakeNoble(noble));
    }

    @Test
    void testCanTakeNobleReturnsFalseWhenBonusesInsufficient() {
        Player p = new Player("Bob", "tok2");
        // Player has no bonuses
        TokenCollection needed = new TokenCollection();
        needed.addTokens(GemType.DIAMOND, 1);
        Noble noble = new Noble("Noble", needed, 3);

        assertFalse(p.canTakeNoble(noble));
    }

    @Test
    void testCanTakeNobleWithMultipleBonuses() {
        Player p = new Player("Bob", "tok2");
        // Give player two bought cards with different bonuses
        Card card1 = new Card("C1", 1, new TokenCollection(), GemType.DIAMOND, 0);
        Card card2 = new Card("C2", 1, new TokenCollection(), GemType.EMERALD, 0);
        p.buyCard(card1, new TokenCollection());
        p.buyCard(card2, new TokenCollection());

        TokenCollection needed = new TokenCollection();
        needed.addTokens(GemType.DIAMOND, 1);
        needed.addTokens(GemType.EMERALD, 1);
        Noble noble = new Noble("Noble", needed, 3);

        assertTrue(p.canTakeNoble(noble));
    }

    @Test
    void testCanTakeNobleWithZeroNeededBonuses() {
        Player p = new Player("Bob", "tok2");
        TokenCollection needed = new TokenCollection();
        Noble noble = new Noble("Noble", needed, 3);

        assertTrue(p.canTakeNoble(noble));
    }

    @Test
    void testGetBonusesReturnsEmptyWhenNoBoughtCards() {
        Player p = new Player("TestPlayer", "token");
        TokenCollection bonuses = p.getBonuses();
        for (GemType type : GemType.values()) {
            assertEquals(0, bonuses.getAmount(type));
        }
    }

    @Test
    void testGetBonusesReturnsCorrectBonusesForBoughtCards() {
        Player p = new Player("TestPlayer", "token");
        // Card with DIAMOND bonus
        Card card1 = new Card("C1", 1, new TokenCollection(), GemType.DIAMOND, 0);
        // Card with EMERALD bonus
        Card card2 = new Card("C2", 1, new TokenCollection(), GemType.EMERALD, 0);
        // Card with DIAMOND bonus
        Card card3 = new Card("C3", 1, new TokenCollection(), GemType.DIAMOND, 0);

        p.buyCard(card1, new TokenCollection());
        p.buyCard(card2, new TokenCollection());
        p.buyCard(card3, new TokenCollection());

        TokenCollection bonuses = p.getBonuses();
        assertEquals(2, bonuses.getAmount(GemType.DIAMOND));
        assertEquals(1, bonuses.getAmount(GemType.EMERALD));
        for (GemType type : GemType.values()) {
            if (type != GemType.DIAMOND && type != GemType.EMERALD) {
                assertEquals(0, bonuses.getAmount(type));
            }
        }
    }

}