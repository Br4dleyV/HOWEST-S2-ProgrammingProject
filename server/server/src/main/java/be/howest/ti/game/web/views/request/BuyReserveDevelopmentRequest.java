package be.howest.ti.game.web.views.request;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class BuyReserveDevelopmentRequest extends ContextBasedRequestView {
    private final Integer gameId;
    private final String playerName;
    private final String card;
    private final JsonObject payment;


    public BuyReserveDevelopmentRequest(RoutingContext ctx) {
        super(ctx);

        String requestPlayerName = ctx.pathParam("playerName");
        String requestCard = ctx.pathParam("developmentName");
        Integer requestGameId = params.pathParameter("gameId").getInteger();
        JsonObject requestPayment = ctx.body().asJsonObject().getJsonObject("payment");

        if (requestCard == null || requestCard.isEmpty()) {
            throw new IllegalArgumentException("Card name cannot be null or empty");
        }
        if (requestPlayerName == null || requestPlayerName.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        if (requestGameId == null) {
            throw new IllegalArgumentException("Game ID cannot be null");
        }
        if (requestPayment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }

        this.payment = requestPayment;
        this.card = requestCard;
        this.playerName = requestPlayerName;
        this.gameId = requestGameId;
    }

    public Integer getGameId() {
        return this.gameId;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getCard() {
        return this.card;
    }

    public JsonObject getPayment() {
        return payment;
    }
}
