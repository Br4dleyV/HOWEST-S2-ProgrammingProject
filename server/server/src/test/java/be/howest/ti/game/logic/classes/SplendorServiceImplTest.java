package be.howest.ti.game.logic.classes;

import be.howest.ti.game.logic.service.SplendorServiceImpl;
import be.howest.ti.game.web.views.response.*;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class SplendorServiceImplTest {

    private SplendorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = SplendorServiceImpl.getInstance();
    }

    @Test
    void testSingletonInstance() {
        assertSame(service, SplendorServiceImpl.getInstance());
    }

    @Test
    void testCreateGameAndJoinGame() {
        var game = service.createGame("TestGame", 2);
        assertNotNull(game);
        assertEquals("TestGame", game.getGameName());

        JoinGameResponse response = service.joinGame(game.getGameId(), "Alice", "token1");
        assertEquals("Alice", response.getPlayerName());
        assertEquals(game.getGameId(), response.getGameId());
        assertEquals("token1", response.getPlayerToken());
    }

    @Test
    void testJoinGameThrowsIfGameNotFound() {
        assertThrows(IllegalArgumentException.class, () -> service.joinGame(9999, "Bob", "token2"));
    }

    @Test
    void testGetGameAndGetGames() {
        var game = service.createGame("AnotherGame", 2);
        assertNotNull(service.getGame(game.getGameId()));
        assertNotNull(service.getGames());
    }

    @Test
    void testUpdateTokensThrowsIfNotPlayersTurn() {
        var game = service.createGame("TokensGame", 2);
        service.joinGame(game.getGameId(), "Alice", "tokenA");
        service.joinGame(game.getGameId(), "Bob", "tokenB");
        JsonObject tc = new JsonObject().put("ruby", 1);
        int gameId = game.getGameId();
        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateTokens(gameId, "Bob", tc));
    }

    @Test
    void testUpdateTokensThrowsIfCannotTakeTwoOfSameColor() {
        var game = service.createGame("TokensGame2", 2);
        service.joinGame(game.getGameId(), "Alice", "tokenA");
        service.joinGame(game.getGameId(), "Bob", "tokenB");
        JsonObject tc = new JsonObject().put("ruby", 2);
        int gameId = game.getGameId();
        assertThrows(
                IllegalArgumentException.class,
                () -> service.updateTokens(gameId, "Alice", tc));
    }

    @Test
    void testGetTokensThrowsUnsupported() {
        assertThrows(UnsupportedOperationException.class, () -> service.getTokens(1));
    }

    @Test
    void testBuyDevelopmentThrowsIfCardNotFound() {
        // Arrange
        var game = service.createGame("DevGame", 2);
        service.joinGame(game.getGameId(), "Alice", "tokenA");
        service.joinGame(game.getGameId(), "Bob", "tokenB");
        int gameId = game.getGameId();
        JsonObject obj = new JsonObject();

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> service.buyDevelopment(gameId, "Alice", "NonExistentCard", obj)
        );
    }


    @Test
    void testReserveDevelopmentThrowsIfCardNotFound() {
        var game = service.createGame("ReserveGame", 2);
        service.joinGame(game.getGameId(), "Alice", "tokenA");
        service.joinGame(game.getGameId(), "Bob", "tokenB");
        int gameId = game.getGameId();
        assertThrows(
                IllegalArgumentException.class,
                () -> service.reserveDevelopment(gameId, "Alice", "NonExistentCard"));
    }

    @Test
    void testBuyReserveDevelopmentThrowsIfCardNotFound() {
        var game = service.createGame("ReserveBuyGame", 2);
        service.joinGame(game.getGameId(), "Alice", "tokenA");
        service.joinGame(game.getGameId(), "Bob", "tokenB");
        int gameId = game.getGameId();
        JsonObject obj = new JsonObject();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.buyReserveDevelopment(gameId, "Alice", "NonExistentCard", obj));
    }

    @Test
    void testClaimNobleThrowsIfNobleNotFound() {
        var game = service.createGame("NobleGame", 2);
        service.joinGame(game.getGameId(), "Alice", "tokenA");
        service.joinGame(game.getGameId(), "Bob", "tokenB");
        int gameId = game.getGameId();
        assertThrows(
                IllegalArgumentException.class,
                () -> service.claimNoble(gameId, "Alice", "NonExistentNoble"));
    }

    @Test
    void testMakeTokenCollectionOfJsonThrowsIfInvalidGemType() {
        JsonObject tc = new JsonObject().put("notagem", 1);
        Method method = getPrivateMethod("makeTokenCollectionOfJson", JsonObject.class);
        method.setAccessible(true);
        assertThrows(
                IllegalArgumentException.class,
                () -> invokeAndUnwrap(method, service, tc));
    }

    // Helper method to get a private method by name and parameter types
    private Method getPrivateMethod(String name, Class<?>... params) {
        try {
            return SplendorServiceImpl.class.getDeclaredMethod(name, params);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to invoke and unwrap exceptions
    private Object invokeAndUnwrap(Method method, Object obj, Object... args) {
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) cause;
            }
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetGameDetailsResponse() {
        var game = service.createGame("DetailsGame", 2);
        var response = service.getGame(game.getGameId());
        assertNotNull(response);
    }

    @Test
    void testGetGamesResponse() {
        service.createGame("GameA", 2);
        service.createGame("GameB", 3);
        var response = service.getGames();
        assertNotNull(response);
    }


}