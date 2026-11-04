package be.howest.ti.game.logic.classes;

import org.junit.jupiter.api.Test;

import be.howest.ti.game.web.tokens.SplendorHTTPPlayer;

import static org.junit.jupiter.api.Assertions.*;

class SplendorHTTPPlayerTest {
    @Test
    void testEqualsAndHashCode() {
        SplendorHTTPPlayer p1 = new SplendorHTTPPlayer(1, "Alice");
        SplendorHTTPPlayer p2 = new SplendorHTTPPlayer(1, "Alice");
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}