package be.howest.ti.game.web.views.response;

import be.howest.ti.game.logic.classes.Game;


public class CreateGameResponse extends AbstractResponseWithHiddenStatus{
    private Game game;

    public CreateGameResponse() {
        super(200);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
