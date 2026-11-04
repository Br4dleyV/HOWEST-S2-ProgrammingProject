package be.howest.ti.game.web.views.response;

import be.howest.ti.game.logic.classes.Game;

public class GetGameDetailsResponse extends AbstractResponseWithHiddenStatus{
    private final Game game;
    
    public GetGameDetailsResponse(Game game) {
        super(200);
        this.game = game;
    }

    public Game getGame(){
        return game;
    }
}
