package be.howest.ti.game.web;

import be.howest.ti.game.logic.classes.*;
import be.howest.ti.game.logic.service.SplendorService;
import be.howest.ti.game.logic.service.SplendorServiceImpl;
import be.howest.ti.game.web.tokens.PlainTextTokens;
import be.howest.ti.game.web.tokens.SplendorHTTPPlayer;
import be.howest.ti.game.web.tokens.TokenManager;
import be.howest.ti.game.web.views.request.*;
import be.howest.ti.game.web.views.response.*;

import java.util.*;
import java.util.function.Supplier;

public class SplendorOpenApiBridge extends OpenApiBridge { // NOSONAR this is not a monster class, it is a bridge :-)
    private final Supplier<SplendorService> serviceFactory;

    public SplendorOpenApiBridge() {
        this(SplendorServiceImpl::new, new PlainTextTokens());
    }

    // Factory needed to differentiate between group-tokens, can be simplified with
    // a single service in the student version.
    SplendorOpenApiBridge(Supplier<SplendorService> serviceFactory, TokenManager tokenManager) {
        installPlayerTokenManager(tokenManager);
        this.serviceFactory = serviceFactory;
    }

    private final Map<String, SplendorService> services = new HashMap<>();

    @SuppressWarnings("unused")
    private SplendorService getService(ContextBasedRequestView request) {
        return services.computeIfAbsent(request.getGroupSecret().toString(),
                k -> serviceFactory.get());
    }

    // region Generic Checks
    @SuppressWarnings("unused")
    private static String ensureUrlSafe(String type, String txt) {
        if (!txt.matches("[a-zA-Z0-9]+")) { // letters and digits only or throw
            throw new IllegalArgumentException(type + " should be alphanumeric because it can be used in the url");
        }
        return txt;
    }
    // end region

    // region General operations

    @Operation("get-info")
    public GetInfoResponse getInfo(BaseSplendorRequest request) {
        return new GetInfoResponse();
    }

    @Operation("get-gems")
    public GetGemsResponse getGems(GetGemsRequest request) {
        return new GetGemsResponse();
    }

    @Operation("get-nobles")
    public GetNoblesResponse getNobles(GetNoblesRequest request) {
        Nobles nobles = new Nobles();
        return new GetNoblesResponse(nobles.getNobles());
    }

    @Operation("get-developments")
    public GetDevelopmentsResponse getDevelopments(GetDevelopmentsRequest request) {
        return new GetDevelopmentsResponse();
    }

    // endregion

    // region Game Management operations

    @Operation("get-games")
    public GetGamesResponse getGames(GetGamesRequest request) {
        return getService(request).getGames();
    }

    @Operation("create-game")
    public CreateGameResponse createGame(CreateGameRequest request) {
        String gameName = request.getGameName();
        String playerName = request.getPlayerName();
        int numberOfPlayers = request.getNumberOfPlayers();

        // Create a new game
        Game game = getService(request).createGame(gameName, numberOfPlayers);
        // Create a new player with a token
        String token = createToken(new SplendorHTTPPlayer(game.getGameId(), playerName));
        Player host = new Player(playerName, token);
        // Join the game as host
        game.setHost(host);

        CreateGameResponse response = new CreateGameResponse();
        response.setGame(game);
        return response;
    }

    @Operation("delete-games")
    public DeleteGamesResponse deleteGames(DeleteGamesRequest request) {
        return new DeleteGamesResponse();
    }

    @Operation("get-game-details")
    public GetGameDetailsResponse getGameDetails(GetGameDetailsRequest request) {
        return getService(request).getGame(
                request.getGameId()
        );
    }

    @Operation("join-game")
    public JoinGameResponse joinGame(JoinGameRequest request) {
        int requestedGameId = request.getGameId();
        String playerName = request.getPlayerName();
        String token = createToken(new SplendorHTTPPlayer(requestedGameId, playerName));

        return getService(request).joinGame(
                requestedGameId,
                playerName,
                token
        );
    }

    // endregion

    // region Game Action operations
    @Operation("update-tokens")
    public UpdateTokensResponse updateTokens(UpdateTokensRequest request) {
        return getService(request).updateTokens(
                request.getGameId(),
                request.getPlayerName(),
                request.getTokens()
        );
    }

    @Operation("buy-development")
    public BuyDevelopmentResponse buyDevelopment(BuyDevelopmentRequest request) {
        return getService(request).buyDevelopment(
                request.getGameId(),
                request.getPlayerName(),
                request.getCardName(),
                request.getPayment()
        );
    }

    @Operation("reserve-development")
    public ReserveDevelopmentResponse reserveDevelopment(ReserveDevelopmentRequest request) {
        return getService(request).reserveDevelopment(
                request.getGameId(),
                request.getPlayerName(),
                request.getCard());
    }

    @Operation("buy-reserved-development")
    public BuyReserveDevelopmentResponse buyReserveDevelopment(BuyReserveDevelopmentRequest request) {
        return getService(request).buyReserveDevelopment(
                request.getGameId(),
                request.getPlayerName(),
                request.getCard(),
                request.getPayment()
        );
    }

    @Operation("choose-noble")
    public ChooseNobleResponse claimNoble(ChooseNobleRequest request) {
        return getService(request).claimNoble(
                request.getGameId(),
                request.getPlayerName(),
                request.getNoble());
    }
    // endregion

}
