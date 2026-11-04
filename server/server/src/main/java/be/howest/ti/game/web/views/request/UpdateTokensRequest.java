package be.howest.ti.game.web.views.request;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class UpdateTokensRequest extends BaseSplendorRequest {
    private final Integer gameId;
    private final String playerName;
    private final JsonObject tokens;

    public UpdateTokensRequest(RoutingContext ctx) {
        super(ctx);

        JsonObject body = ctx.body().asJsonObject(); // Get entire JSON body

        this.tokens = body.getJsonObject("take");
        this.playerName = ctx.pathParam("playerName");
        this.gameId = params.pathParameter("gameId").getInteger();

        if (gameId == null || playerName == null || tokens == null) {
            throw new IllegalArgumentException("gameId, playerName and tokens cannot be null");
        }
    }

    public Integer getGameId() {
        return this.gameId;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public JsonObject getTokens() {
        return this.tokens;
    }
}
