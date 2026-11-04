package be.howest.ti.game.web.views.response;

import be.howest.ti.game.logic.classes.TokenCollection;


public class UpdateTokensResponse extends AbstractResponseWithHiddenStatus{
    private TokenCollection tokens;

    public UpdateTokensResponse(TokenCollection tc) {
        super(200);
        this.tokens = tc;
    }

    public void setTokens(TokenCollection tc){
        this.tokens = tc;
    }

    public TokenCollection getTokens(){
        return tokens;
    }

}
