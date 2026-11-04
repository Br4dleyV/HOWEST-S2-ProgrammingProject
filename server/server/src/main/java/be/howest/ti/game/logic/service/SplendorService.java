package be.howest.ti.game.logic.service;

import be.howest.ti.game.logic.classes.*;
import be.howest.ti.game.web.views.response.*;
import io.vertx.core.json.JsonObject;

public interface SplendorService {

    JoinGameResponse joinGame(int gameId, String playerName, String token);

    Game createGame(String gameName, int numberOfPlayers);

    GetGameDetailsResponse getGame(int gameId);

    GetGamesResponse getGames();

    UpdateTokensResponse updateTokens(Integer gameId, String playerName, JsonObject tc);

    BuyDevelopmentResponse buyDevelopment(Integer gameId, String playerName, String cardName, JsonObject payment);

    ReserveDevelopmentResponse reserveDevelopment(Integer gameId, String playerName, String cardName);

    TokenCollection getTokens(int gameId);

    BuyReserveDevelopmentResponse buyReserveDevelopment(Integer gameId, String playerName, String cardName, JsonObject payment);

    ChooseNobleResponse claimNoble(Integer gameId, String playerName, String nobleName);

    }
