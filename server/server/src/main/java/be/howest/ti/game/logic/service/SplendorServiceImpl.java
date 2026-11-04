package be.howest.ti.game.logic.service;

import be.howest.ti.game.logic.classes.*;
import be.howest.ti.game.logic.enums.GemType;
import be.howest.ti.game.web.views.response.*;
import io.vertx.core.json.JsonObject;

import java.util.*;

public class SplendorServiceImpl implements SplendorService {
    private final GameManager games = new GameManager();
    private int gameIdCounter = 0;

    // Singleton instance
    private static final SplendorServiceImpl INSTANCE = new SplendorServiceImpl();

    public SplendorServiceImpl() { // Private constructor to prevent instantiation
    }

    public static SplendorServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public JoinGameResponse joinGame(int gameId, String playerName, String token) {
        Game game = games.getGame(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found");
        }
        Player player = new Player(playerName, token);
        game.joinGame(player);

        if (game.getPlayers().size() == game.getNumberOfPlayers()) {
            game.startGame();
            // Setup game board, distribute cards, etc. here
        }

        return new JoinGameResponse(gameId, playerName, token);
    }

    @Override
    public Game createGame(String gameName, int numberOfPlayers) {
        gameIdCounter++;

        Game game = new Game(gameIdCounter, gameName, numberOfPlayers);
        games.addGame(game);

        return game;
    }

    @Override
    public GetGameDetailsResponse getGame(int gameId) {
        return new GetGameDetailsResponse(games.getGame(gameId));
    }

    @Override
    public GetGamesResponse getGames() {
        return new GetGamesResponse(games);
    }

    public UpdateTokensResponse updateTokens(Integer gameId, String playerName, JsonObject tc) {
        Game currentGame = games.getGame(gameId);
        Player updatePlayer = currentGame.getPlayerByName(playerName);

        // Check if the player is allowed to take the tokens
        if (currentGame.getCurrentPlayer() != updatePlayer) {
            throw new IllegalArgumentException("It's not your turn to take tokens.");
        }

        // Check if player tries to take 2 of the same color and if there are at least 4
        // available
        for (String key : tc.fieldNames()) {
            int amount = tc.getInteger(key, 0);
            if (amount == 2) {
                GemType gemType = GemType.valueOf(key.toUpperCase());
                if (!currentGame.getUnclaimedTokens().canTakeTwoOfSameColor(gemType)) {
                    throw new IllegalArgumentException(
                            "Cannot take 2 tokens of " + gemType + " when fewer than 4 are available.");
                }
            }
        }

        TokenCollection tokens = makeTokenCollectionOfJson(tc);

        currentGame.updateTokens(tokens);
        updatePlayer.updateTokens(tokens);
        currentGame.nextPlayer();

        return new UpdateTokensResponse(updatePlayer.getTokens());
    }

    public BuyDevelopmentResponse buyDevelopment(Integer gameId, String playerName, String cardName, JsonObject payment) {
        Game currentGame = games.getGame(gameId);
        Player player = currentGame.getPlayerByName(playerName);
        Card dev = getCardByName(gameId, cardName);
        Level level = currentGame.getLevel(dev.getLevel());
        TokenCollection paymentCollection = makeTokenCollectionOfJson(payment);

        // Buy card for player (removes tokens from player and adds card)
        TokenCollection price = player.buyCard(dev, paymentCollection);

        // Remove card from market through level
        level.removeVisibleCard(dev);

        // Add tokens back to unclaimed pile
        currentGame.getUnclaimedTokens().addTokens(price);

        // Draw new card on market
        level.drawNewCard();

        // Check if player has enough points to win
        if (player.getTotalPrestigePoints() >= 15) {
            currentGame.setGameState("FINISHED");
        }

        // Go to next player
        currentGame.nextPlayer();

        return new BuyDevelopmentResponse(player);
    }

    public ReserveDevelopmentResponse reserveDevelopment(Integer gameId, String playerName, String cardName) {
        Game currentGame = games.getGame(gameId);
        Player player = currentGame.getPlayerByName(playerName);
        Card dev = getCardByName(gameId, cardName);
        Level level = currentGame.getLevel(dev.getLevel());

        player.reserveCard(dev);

        //get gold only if left in bank
        if(currentGame.getUnclaimedTokens().getAmount(GemType.GOLD) > 0){
            player.getTokens().addTokens(GemType.GOLD);
            currentGame.getUnclaimedTokens().removeTokens(GemType.GOLD, 1);
        }

        level.removeVisibleCard(dev);
        level.drawNewCard();

        currentGame.nextPlayer();



        return new ReserveDevelopmentResponse(dev, player.getTokens());
    }

    public BuyReserveDevelopmentResponse buyReserveDevelopment(Integer gameId, String playerName, String cardName,  JsonObject payment) {
        Game currentGame = games.getGame(gameId);
        Card dev = getReservedCard(gameId, playerName, cardName);
        TokenCollection paymentCollection = makeTokenCollectionOfJson(payment);
        Player player = currentGame.getPlayerByName(playerName);

        TokenCollection price = player.buyReserveCard(dev, paymentCollection);

        currentGame.getUnclaimedTokens().addTokens(price);

        currentGame.nextPlayer();

        return new BuyReserveDevelopmentResponse(player.getTokens(), dev);
    }

    public Card getCardByName(Integer gameId, String cardName) {
        for (Level level : games.getGame(gameId).getMarket()) {
            for (Card c : level.getVisibleCards()) {
                if (c.getName().equals(cardName)) {
                    return c;
                }
            }
        }

        throw new IllegalArgumentException("Card not found in current game: " + cardName);
    }

    private Card getReservedCard(Integer gameId, String playerName, String cardName) {
        Game game = games.getGame(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        Player p = game.getPlayerByName(playerName);

        for (Card c : p.getReserved()) {
            if (c.getName().equals(cardName)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Card not found in current game: " + cardName);
    }

    private TokenCollection makeTokenCollectionOfJson(JsonObject takeObject) {
        Map<GemType, Integer> tokensMap = new EnumMap<>(GemType.class);
        for (String key : takeObject.fieldNames()) {
            try {
                GemType gemType = GemType.valueOf(key.toUpperCase());
                int amount = takeObject.getInteger(key);
                tokensMap.put(gemType, amount);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid GemType: " + key);
            }
        }
        return new TokenCollection(tokensMap);
    }

    @Override
    public TokenCollection getTokens(int gameId) {
        throw new UnsupportedOperationException("Unimplemented method 'getTokens'");
    }

    @Override
    public ChooseNobleResponse claimNoble(Integer gameId, String playerName, String nobleName) {
        Game game = games.getGame(gameId);
        Player player = game.getPlayerByName(playerName);

        Noble noble = getNobleByName(gameId, nobleName);
        player.takeNoble(noble);
        game.removeNoble(noble);

        return new ChooseNobleResponse(player, noble);
    }

    private Noble getNobleByName(Integer gameId, String nobleName) {
        for (Noble noble : games.getGame(gameId).getUnclaimedNobles()) {
            if (Objects.equals(noble.getName(), nobleName)) {
                return noble;
            }
        }

        throw new IllegalArgumentException("Noble not found in current game: " + nobleName);
    }

}
