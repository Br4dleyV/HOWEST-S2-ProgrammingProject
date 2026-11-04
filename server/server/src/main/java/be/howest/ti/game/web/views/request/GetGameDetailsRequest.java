package be.howest.ti.game.web.views.request;

import io.vertx.ext.web.RoutingContext;

public class GetGameDetailsRequest extends BaseSplendorRequest {
    private int gameId;

    public GetGameDetailsRequest(RoutingContext ctx) {
        super(ctx);

        Integer paramGameId = params.pathParameter("gameId").getInteger();
        // If gameId is null, error
        if (paramGameId == null) {
            throw new IllegalArgumentException("gameId should not be null");
        }
        if (paramGameId < 0) {
            throw new IllegalArgumentException("gameId should be a positive integer");
        }
        
        this.gameId = paramGameId;
    }

    public int getGameId() {
        return gameId;
    }
}
