package be.howest.ti.game.web.views.request;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class CreateGameRequest extends BaseSplendorRequest {
    private final String gameName;
    private final String playerName;
    private final int numberOfPlayers;

    public CreateGameRequest(RoutingContext ctx) {
        super(ctx);

        JsonObject body = ctx.body().asJsonObject(); // Get entire JSON body

        String requestGameName = body.getString("gameName");
        String requestPlayerName = body.getString("playerName");
        Integer numOfPlayers = body.getInteger("numberOfPlayers");

        if (requestGameName == null || requestPlayerName == null || numOfPlayers == null) {
            throw new IllegalArgumentException("gameName, playerName & numberOfPlayers cannot be null");
        }

        if (numOfPlayers < 2 || numOfPlayers > 4) {
            throw new IllegalArgumentException("numberOfPlayers should be between 2 and 4");
        }

        this.gameName = requestGameName;
        this.playerName = requestPlayerName;
        this.numberOfPlayers = numOfPlayers;
    }

    public String getGameName() {
        return gameName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}