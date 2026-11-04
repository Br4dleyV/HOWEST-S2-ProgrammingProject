package be.howest.ti.game.web.views.request;

import io.vertx.ext.web.RoutingContext;

public class GetGamesRequest extends BaseSplendorRequest{
    public GetGamesRequest(RoutingContext ctx) {
        super(ctx);
    }
}
