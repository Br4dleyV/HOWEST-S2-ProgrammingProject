package be.howest.ti.game.logic.classes;

import org.junit.jupiter.api.Test;

import be.howest.ti.game.web.tokens.PlainTextTokens;
import be.howest.ti.game.web.tokens.SplendorHTTPPlayer;

import static org.junit.jupiter.api.Assertions.*;

class PlainTextTokensTest {
    @Test
    void testCreateAndParseToken() {
        PlainTextTokens tokens = new PlainTextTokens();
        SplendorHTTPPlayer player = new SplendorHTTPPlayer(1, "Alice");
        String token = tokens.createToken(player);
        SplendorHTTPPlayer parsed = tokens.parseToken(token);
        assertEquals(player, parsed);
    }

    @Test
    void testParseTokenInvalid() {
        PlainTextTokens tokens = new PlainTextTokens();
        assertThrows(IllegalArgumentException.class, () -> tokens.parseToken("invalid_token"));
    }
}