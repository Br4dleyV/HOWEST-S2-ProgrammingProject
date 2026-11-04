package be.howest.ti.game.web.views.request;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;


public class BuyDevelopmentRequest extends ContextBasedRequestView {
    private final Integer gameId;
    private final String playerName;
    private final String cardName;
    private final JsonObject payment;

    public BuyDevelopmentRequest(RoutingContext ctx) {
        super(ctx);
        JsonObject body = ctx.body().asJsonObject(); // Get entire JSON body


        String requestPlayerName = ctx.pathParam("playerName");
        Integer requestGameId = params.pathParameter("gameId").getInteger();
        String requestCardName = body.getJsonObject("development").getString("name");

        if (requestCardName == null || requestCardName.isEmpty()) {
            throw new IllegalArgumentException("Card name cannot be null or empty");
        }
        if (requestPlayerName == null || requestPlayerName.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        if (requestGameId == null) {
            throw new IllegalArgumentException("Game ID cannot be null");
        }

        this.payment = body.getJsonObject("payment");
        this.cardName = requestCardName;
        this.playerName = requestPlayerName;
        this.gameId = requestGameId;
    }

    public Integer getGameId(){
        return this.gameId;
    }
    public String getPlayerName(){
        return this.playerName;
    }
    public String getCardName() {
        return cardName;
    }
    public JsonObject getPayment() {
        return payment;
    }

}
