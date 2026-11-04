package be.howest.ti.game.logic.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private Player host;

    @BeforeEach
    void setUp() {
        host = new Player("HostPlayer", null);
        game = new Game(1, "TestGame", 4);
        game.setHost(host); // ADD THIS
    }

    @Test
    void testGameConstructor() {
        assertEquals("TestGame", game.getGameName());
        assertEquals(4, game.getNumberOfPlayers());
        assertEquals(host, game.getGameOwner());
        assertTrue(game.getPlayers().contains(host));
    }

    @Test
    void testJoinGame() {
        Player alice = new Player("Alice", null);
        game.joinGame(alice);
        assertTrue(game.getPlayers().contains(alice));
    }

    @Test
    void testJoinGameTooManyPlayers() {
        game.joinGame(new Player("A", null));
        game.joinGame(new Player("B", null));
        game.joinGame(new Player("C", null));
        // Host + A + B + C = 4 players (full)
        Player playerD = new Player("D", null);
        assertThrows(IllegalStateException.class, () -> game.joinGame(playerD));
    }

    @Test
    void testGetPlayerByName() {
        Player alice = new Player("Alice", null);
        game.joinGame(alice);
        assertEquals(alice, game.getPlayerByName("Alice"));
        assertNull(game.getPlayerByName("NonExisting"));
    }

    @Test
    void testSetHostAndGetters() {
        Player newHost = new Player("NewHost", null);
        game.setHost(newHost);
        assertEquals(newHost, game.getGameOwner());
        assertEquals(newHost, game.getFirstPlayer());
        assertEquals(newHost, game.getCurrentPlayer());
    }

    @Test
    void testGetGameId() {
        assertEquals(1, game.getGameId());
    }

    @Test
    void testNextPlayerCycles() {
        Player a = new Player("A", null);
        Player b = new Player("B", null);
        game.joinGame(a);
        game.joinGame(b);
        Player before = game.getCurrentPlayer();
        game.nextPlayer();
        assertNotEquals(before, game.getCurrentPlayer());
    }

    @Test
    void testSetAndGetGameState() {
        game.setGameState("CUSTOM");
        assertEquals("CUSTOM", game.getGameState());
    }

    @Test
    void testInitialiseEmptyLevels() {
        game.initialiseEmptyLevels();
        assertEquals(3, game.getMarket().size());
    }

    @Test
    void testRemoveNoble() {
        TokenCollection neededBonuses = new TokenCollection(); // or use appropriate constructor/values
        Noble noble = new Noble("TestNoble", neededBonuses, 3);
        game.getUnclaimedNobles().add(noble);
        game.removeNoble(noble);
        assertFalse(game.getUnclaimedNobles().contains(noble));
    }

    @Test
    void testUpdateTokens() {
        TokenCollection tokens = new TokenCollection();
        game.updateTokens(tokens);
        assertNotNull(game.getUnclaimedTokens());
    }

    @Test
    void testGetLevelThrows() {
        game.initialiseEmptyLevels();
        assertThrows(IllegalArgumentException.class, () -> game.getLevel(99));
    }

    @Test
    void testIsReturnExcessTokensRequired() {
        assertFalse(game.isReturnExcessTokensRequired());
    }

    @Test
    void testGetUnclaimedNoblesAndTokens() {
        assertNotNull(game.getUnclaimedNobles());
        assertNotNull(game.getUnclaimedTokens());
    }

    @Test
    void testGetWinnerInitiallyNull() {
        assertNull(game.getWinner());
    }

    @Test
    void testStartFinalRoundAndDecideWinner() {
        game.startFinalRound(); // Should not throw
        game.decideWinner(); // Should not throw
        // Assert that the game state is ENDED after deciding winner
        assertEquals("ENDED", game.getGameState());
    }

    @Test
    void testJoinGameDuplicateNameThrows() {
        Player alice = new Player("Alice", null);
        game.joinGame(alice);
        Player alice2 = new Player("Alice", null);
        assertThrows(IllegalArgumentException.class, () -> game.joinGame(alice2));
    }

    @Test
    void testSetupGameBoardIsCalledByStartGame() {
        game.joinGame(new Player("A", null));
        game.joinGame(new Player("B", null));
        game.joinGame(new Player("C", null));
        game.startGame();
        // Market, nobles, and tokens should be initialized
        assertEquals(3, game.getMarket().size());
        assertNotNull(game.getUnclaimedNobles());
        assertNotNull(game.getUnclaimedTokens());
    }

    @Test
    void testNextPlayerWrapsAround() {
        Player a = new Player("A", null);
        Player b = new Player("B", null);
        Player c = new Player("C", null);
        game.joinGame(a);
        game.joinGame(b);
        game.joinGame(c);
        Player first = game.getCurrentPlayer();
        game.nextPlayer();
        game.nextPlayer();
        game.nextPlayer();
        game.nextPlayer();
        assertEquals(first, game.getCurrentPlayer());
    }

    @Test
    void testCheckIfWinnerTriggersStartFinalRound() {
        Player winner = new Player("Winner", null) {
            @Override
            public int getTotalPrestigePoints() {
                return 15;
            }
        };
        game.joinGame(winner);
        game.checkIfWinner();
        // Assert that the game state is ENDED if winner is found
        assertEquals("ENDED", game.getGameState());
    }

    @Test
    void testStartFinalRoundCallsDecideWinnerIfCurrentIsFirst() {
        game.startFinalRound(); // Should not throw, just coverage
        // Assert that the game state is ENDED after final round
        assertEquals("ENDED", game.getGameState());
    }

    @Test
    void testDecideWinnerSetsWinnerAndGameState() {
        Player winner = new Player("Winner", null) {
            @Override
            public int getTotalPrestigePoints() {
                return 20;
            }
        };
        game.joinGame(winner);
        game.decideWinner();
        assertEquals(winner, game.getWinner());
        assertEquals("ENDED", game.getGameState());
    }

    @Test
    void testEqualsAndHashCodeWithSameReference() {
        assertEquals(game, game);
        assertEquals(game.hashCode(), game.hashCode());
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        assertNotEquals(null, game);
        assertNotEquals("not a game", game);
    }

    @Test
    void testGetLevelReturnsCorrectLevel() {
        game.initialiseEmptyLevels();
        Level level = game.getLevel(1);
        assertNotNull(level);
    }

    @Test
    void testGetLevelThrowsForInvalidLevel() {
        game.initialiseEmptyLevels();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> game.getLevel(-1));
        assertTrue(ex.getMessage().contains("Level number does not exist in market"));
    }
}