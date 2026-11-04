package be.howest.ti.game.web.views.response;

import be.howest.ti.game.logic.classes.Player;


public class BuyDevelopmentResponse extends AbstractResponseWithHiddenStatus{
    private final Player player;
    public BuyDevelopmentResponse(Player player) {
        super(200);
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }
}
