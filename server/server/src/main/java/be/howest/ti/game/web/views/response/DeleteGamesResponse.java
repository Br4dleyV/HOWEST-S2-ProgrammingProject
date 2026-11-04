
package be.howest.ti.game.web.views.response;

public class DeleteGamesResponse extends AbstractResponseWithHiddenStatus{
    public DeleteGamesResponse() {
        super(200);
    }

    public String deleteGames(){
        return "test";
    }
}
