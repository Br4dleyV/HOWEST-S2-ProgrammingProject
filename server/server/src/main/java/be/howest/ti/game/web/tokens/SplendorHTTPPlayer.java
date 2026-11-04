package be.howest.ti.game.web.tokens;

import io.vertx.ext.auth.impl.UserImpl;

public class SplendorHTTPPlayer extends UserImpl {

    private final int gameId;
    private final String playerName;

    public SplendorHTTPPlayer(int gameId, String playerName) {
        this.gameId = gameId;
        this.playerName = playerName;

    }

    public int getGameId() {
        return gameId;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        SplendorHTTPPlayer that = (SplendorHTTPPlayer) obj;
        
        if (gameId != that.gameId) return false;
        return playerName != null ? playerName.equals(that.playerName) : that.playerName == null;
    }

    @Override
    public int hashCode() {
        int result = gameId;
        result = 31 * result + (playerName != null ? playerName.hashCode() : 0);
        return result;
    }
}