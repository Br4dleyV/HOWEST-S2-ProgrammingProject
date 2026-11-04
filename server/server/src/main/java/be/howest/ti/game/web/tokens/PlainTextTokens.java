package be.howest.ti.game.web.tokens;

public class PlainTextTokens implements TokenManager {

    private static final int TOKEN_GAME_ID_PART = 0;
    private static final int TOKEN_PLAYER_PART = 1;
    private static final int TOKEN_EXPECTED_PARTS = 2;

    public String createToken(SplendorHTTPPlayer user) {
        return String.format("%s_%s", user.getGameId(), user.getPlayerName());
    }

    @Override
    public <T> String createToken(T user) {
        if (user instanceof SplendorHTTPPlayer splendorHTTPPlayer) {
            return createToken(splendorHTTPPlayer);
        } else {
            throw new IllegalArgumentException("Unsupported user type");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T parseToken(String token) {
        String[] parts = token.split("_");

        if (parts.length != TOKEN_EXPECTED_PARTS) {
            throw new InvalidTokenException();
        }

        return (T) new SplendorHTTPPlayer(Integer.parseInt(parts[TOKEN_GAME_ID_PART]), parts[TOKEN_PLAYER_PART]);
    }

}
