package be.howest.ti.game.web.views.request;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ChooseNobleRequest extends BaseSplendorRequest {
    private final Integer gameId;
    private final String playerName;
    private final String nobleName;

    public ChooseNobleRequest(RoutingContext ctx) {
        super(ctx);
        JsonObject body = ctx.body().asJsonObject();

        String nobleNameInput = body.getString("name");
        String requestPlayerName = ctx.pathParam("playerName");
        Integer requestGameId = params.pathParameter("gameId").getInteger();
        if (nobleNameInput == null || nobleNameInput.isEmpty()) {
            throw new IllegalArgumentException("Noble name cannot be null or empty");
        }
        if (requestPlayerName == null || requestPlayerName.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        if (requestGameId == null) {
            throw new IllegalArgumentException("Game ID cannot be null");
        }

        this.nobleName = nobleNameInput;
        this.playerName = requestPlayerName;
        this.gameId = requestGameId;
    }

    public Integer getGameId() {
        return this.gameId;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getNoble() {
        return this.nobleName;
    }
}
