package be.howest.ti.game.logic.classes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    private GameManager manager;
    private Game mockGame1;
    private Game mockGame2;

@BeforeEach
void setUp() {
    manager = new GameManager();
    mockGame1 = new Game(1, "Game1",  4);
    mockGame2 = new Game(2,"Game2" ,  4);
}

@Test
void testRemoveGame() {
    manager.addGame(mockGame1);
    manager.addGame(mockGame2);
    manager.removeGame(mockGame1);
    assertFalse(manager.getGames().contains(mockGame1));
    assertTrue(manager.getGames().contains(mockGame2));
}

@Test
void testRemoveGameThatDoesNotExistDoesNothing() {
    manager.addGame(mockGame1);
    manager.removeGame(mockGame2); // mockGame2 was never added
    assertEquals(1, manager.getGames().size());
    assertTrue(manager.getGames().contains(mockGame1));
}
}