package be.howest.ti.game.logic.classes;

import be.howest.ti.game.logic.enums.GemType;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TokenCollectionTest {

    @Test
    void testAddTokensCorrectly() {
        TokenCollection collection = new TokenCollection();
        Map<GemType, Integer> add = new EnumMap<>(GemType.class);
        add.put(GemType.RUBY, 2);
        collection.addTokens(add);

        assertEquals(2, collection.getTokens().get(GemType.RUBY));
    }

    @Test
    void testRemoveTokensCorrectly() {
        TokenCollection collection = new TokenCollection();
        Map<GemType, Integer> start = new EnumMap<>(GemType.class);
        start.put(GemType.RUBY, 3);
        collection.addTokens(start);

        Map<GemType, Integer> toRemove = new EnumMap<>(GemType.class);
        toRemove.put(GemType.RUBY, 2);
        collection.removeTokens(new TokenCollection(toRemove));

        assertEquals(1, collection.getTokens().get(GemType.RUBY));
    }

    @Test
    void testTokenRemovedWhenZero() {
        TokenCollection collection = new TokenCollection();
        Map<GemType, Integer> start = new EnumMap<>(GemType.class);
        start.put(GemType.RUBY, 1);
        collection.addTokens(start);

        Map<GemType, Integer> toRemove = new EnumMap<>(GemType.class);
        toRemove.put(GemType.RUBY, 1);
        collection.removeTokens(toRemove);

        assertFalse(collection.getTokens().containsKey(GemType.RUBY),
                "Token should be removed if amount reaches zero");
    }

    @Test
    void testTokenDoesNotGoNegative() {
        TokenCollection collection = new TokenCollection();
        Map<GemType, Integer> start = new EnumMap<>(GemType.class);
        start.put(GemType.EMERALD, 1);
        collection.addTokens(start);

        Map<GemType, Integer> toRemove = new EnumMap<>(GemType.class);
        toRemove.put(GemType.EMERALD, 5);
        TokenCollection tokensToRemove = new TokenCollection(toRemove);

        assertThrows(IllegalArgumentException.class, () -> {
            collection.removeTokens(tokensToRemove);
        });
    }
}