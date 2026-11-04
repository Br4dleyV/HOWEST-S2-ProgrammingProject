package be.howest.ti.game.web.views.request;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ReserveDevelopmentRequest extends BaseSplendorRequest {
    private final Integer gameId;
    private final String playerName;
    private final String card;

    public ReserveDevelopmentRequest(RoutingContext ctx) {
        super(ctx);

        JsonObject body = ctx.body().asJsonObject(); // Get entire JSON body
        this.card = body.getJsonObject("development").getString("name");
        this.playerName = ctx.pathParam("playerName");
        this.gameId = params.pathParameter("gameId").getInteger();

        if (card == null || card.isEmpty()) {
            throw new IllegalArgumentException("Card name cannot be null or empty");
        }

        if (gameId == null || playerName == null) {
            throw new IllegalArgumentException("gameId and playerName cannot be null");
        }
    }

    public Integer getGameId(){
        return this.gameId;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public String getCard(){
        return this.card;
    }
}
