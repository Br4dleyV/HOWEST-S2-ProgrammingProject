package be.howest.ti.game.web.views.response;
import be.howest.ti.game.logic.classes.Noble;
import be.howest.ti.game.logic.classes.Player;

/**
 * Response for claiming a noble.
 */
public class ChooseNobleResponse extends AbstractResponseWithHiddenStatus {

    private final Player player;
    private final Noble noble;

    public ChooseNobleResponse(Player player, Noble noble) {
        super(200);
        this.player = player;
        this.noble = noble;
    }

    public Player getPlayer() {
        return player;
    }

    public Noble getNoble() {
        return noble;
    }
}

