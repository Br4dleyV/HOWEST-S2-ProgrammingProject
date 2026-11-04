package be.howest.ti.game.web.views.response;

import be.howest.ti.game.logic.classes.Card;
import be.howest.ti.game.logic.classes.TokenCollection;

public class BuyReserveDevelopmentResponse extends AbstractResponseWithHiddenStatus{
    private TokenCollection tokens;
    private Card card;

    public BuyReserveDevelopmentResponse(TokenCollection tc , Card c) {
        super(200);
        this.tokens = tc;
        this.card = c;
    }

    public TokenCollection getTokens(){
        return this.tokens;
    }

    public Card getDevelopments(){
        return this.card;
    }
}
