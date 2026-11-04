package be.howest.ti.game.web.views.request;

import io.vertx.ext.web.RoutingContext;

public class JoinGameRequest extends BaseSplendorRequest {
    private int gameId;
    private String playerName;

    public JoinGameRequest(RoutingContext ctx) {
        super(ctx);

        int paramGameId = params.pathParameter("gameId").getInteger();
        String paramPlayerName = params.pathParameter("playerName").getString();

        if (paramGameId <= 0) {
            throw new IllegalArgumentException("Game ID must be a positive integer");
        }
        if (paramPlayerName == null || paramPlayerName.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }

        this.gameId = paramGameId;
        this.playerName = paramPlayerName;
    }

    public int getGameId() {
        return gameId;
    }

    public String getPlayerName() {
        return playerName;
    }

}
