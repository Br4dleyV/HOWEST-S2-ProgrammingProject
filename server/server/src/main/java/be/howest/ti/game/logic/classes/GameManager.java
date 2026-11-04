package be.howest.ti.game.logic.classes;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private final List<Game> games;

    public GameManager(){
        this.games = new ArrayList<>();
    }

    public void addGame(Game game){
        games.add(game);
    }

    public void removeGame(Game game){
        games.remove(game);
    }

    public void removeAllGames(){
        games.clear();
    }

    public List<Game> getGames(){
        return games;
    }

    public Game getGame(int gameId){
        for (Game game : games) {
            if (game.getGameId() == gameId) {
                return game;
            }
        }
        return null;
    }
}
