package be.howest.ti.game.web.views.response;

import be.howest.ti.game.logic.classes.Card;
import be.howest.ti.game.logic.classes.TokenCollection;

public class ReserveDevelopmentResponse extends AbstractResponseWithHiddenStatus {
    private final TokenCollection tokens;
    private final Card reserve;

    public ReserveDevelopmentResponse(Card c , TokenCollection tc) {
        super(200);
        this.reserve = c;
        this.tokens = tc;
    }

    public Card getReserve(){
        return this.reserve;
    }

    public TokenCollection getTokens(){
        return this.tokens;
    }
}
