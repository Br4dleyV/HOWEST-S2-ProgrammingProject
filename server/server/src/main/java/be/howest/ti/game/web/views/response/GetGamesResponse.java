package be.howest.ti.game.web.views.response;

import be.howest.ti.game.logic.classes.Game;
import be.howest.ti.game.logic.classes.GameManager;

import java.util.List;

public class GetGamesResponse  extends AbstractResponseWithHiddenStatus{
    private final GameManager games;
    
    public GetGamesResponse(GameManager games) {
        super(200);

        this.games = games;
    }

    public List<Game> getGames(){
        return games.getGames();
    }
}