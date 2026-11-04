package be.howest.ti.game.web.views.response;

public class JoinGameResponse extends AbstractResponseWithHiddenStatus {
    private final int gameId;
    private final String playerName;
    private final String playerToken;

    public JoinGameResponse(int gameId, String playerName, String token) {
        super(200);

        this.gameId = gameId;
        this.playerName = playerName;
        this.playerToken = token;
    }

    public int getGameId() {
        return gameId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerToken() {
        return playerToken;
    }
}