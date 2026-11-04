package be.howest.ti.game.logic.classes;

import java.util.*;

import be.howest.ti.game.logic.enums.GemType;

public class Game {
    // Changes At Creation of game
    private final String gameName;
    private Player gameOwner;
    private final int numberOfPlayers;
    private final int gameId;

    // Changes In lobby
    private final List<Player> players;

    // Changes at multiple times
    private Player firstPlayer;
    private Player currentPlayer;
    private List<Level> market;
    private Set<Noble> unclaimedNobles;
    private boolean returnExcessTokensRequired;
    private TokenCollection unclaimedTokens;
    private Player winner;
    private String gameState;

    public Game(int gameId, String name, int numberOfPlayers) {
        this.gameId = gameId;
        this.gameName = name;
        this.players = new ArrayList<>();
        this.numberOfPlayers = numberOfPlayers;
        this.unclaimedNobles = new HashSet<>();
        this.unclaimedTokens = new TokenCollection();
    }

    public void setHost(Player host) { // Only gets called once, when the game is created
        this.gameOwner = host;
        this.firstPlayer = host;
        this.currentPlayer = host;
        this.gameState = "NOT_STARTED";

        joinGame(host);
    }

    public int getGameId() {
        return gameId;
    }

    public Player getGameOwner() {
        return gameOwner;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void nextPlayer() {
        int currentIndex = players.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % players.size();
        currentPlayer = players.get(nextIndex);
        checkIfWinner();
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameState() {
        return gameState;
    }

    public List<Level> getMarket() {
        return market;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isReturnExcessTokensRequired() {
        return returnExcessTokensRequired;
    }

    public List<Noble> getUnclaimedNobles() {
        return new ArrayList<>(unclaimedNobles);
    }

    public TokenCollection getUnclaimedTokens() {
        return unclaimedTokens;
    }

    public Player getWinner() {
        return winner;
    }

    public void joinGame(Player player) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(player.getName())) {
                throw new IllegalArgumentException("Player with this name already exists");
            }
        }
        if (players.size() >= numberOfPlayers) {
            throw new IllegalStateException("Game is full");
        }
        players.add(player);
    }

    public void startGame() {
        if (players.size() < numberOfPlayers) {
            throw new IllegalStateException("Not enough players to start the game");
        }
        this.gameState = "STARTED";
        setupGameBoard();
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public void initialiseEmptyLevels(){
        this.market = new ArrayList<>();
        for (int levelNum = 1; levelNum <= 3; levelNum++) {
            Level level = new Level(levelNum);
            market.add(level);
        }
    }

    private void setupGameBoard() {
        // 1. Load and setup development cards
        List<Card> allCards = DataReader.readDevelopmentCards("/data/developments.txt");

        initialiseEmptyLevels();

        // Build card stacks
        for (Card card : allCards) {
            int cardLevel = card.getLevel();
            for(Level level : market){
                if(cardLevel == level.getLevel()){
                    level.addCardToUnclaimed(card);
                }
            }
        }

        // Draw cards on visible side
        for(Level level : market){
            Collections.shuffle(level.getUnclaimedCards());
            for(int i = 0; i < 4; i ++){
                level.drawNewCard();
            }
        }

        // 2. Load and select nobles
        List<Noble> allNobles = DataReader.readNobles("/data/nobles.txt");
        Collections.shuffle(allNobles);
        int nobleCount = numberOfPlayers + 1;
        unclaimedNobles = new HashSet<>(allNobles.subList(0, Math.min(nobleCount, allNobles.size())));

        // 3. Initialize token supply
        unclaimedTokens = new TokenCollection();

        int tokenAmount;
        switch (numberOfPlayers) {
            case 2 -> tokenAmount = 4;
            case 3 -> tokenAmount = 5;
            default -> tokenAmount = 7;
        }


        // Base tokens for 4 players
        unclaimedTokens.addTokens(GemType.DIAMOND, tokenAmount);
        unclaimedTokens.addTokens(GemType.EMERALD, tokenAmount);
        unclaimedTokens.addTokens(GemType.ONYX, tokenAmount);
        unclaimedTokens.addTokens(GemType.RUBY, tokenAmount);
        unclaimedTokens.addTokens(GemType.SAPPHIRE, tokenAmount);
        unclaimedTokens.addTokens(GemType.GOLD, 5);

        // 4. Initialize game state
        returnExcessTokensRequired = false;
        currentPlayer = firstPlayer;
    }

    public void checkIfWinner() {
        for (Player player : players) {
            if (player.getTotalPrestigePoints() >= 15) {
                startFinalRound();
            }
        }
    }

    public void startFinalRound(){
        if (currentPlayer == firstPlayer) {
            decideWinner();
        }
    }

    public void decideWinner() {
        Player bestPlayer = null;
        int scoreToBeat = 15;

        for (Player player : players) {
            if (player.getTotalPrestigePoints() >= scoreToBeat) {
                bestPlayer = player;
                scoreToBeat = player.getTotalPrestigePoints();
            }
        }

        this.winner = bestPlayer;
        gameState = "ENDED";
    }

    public void removeNoble(Noble noble){
        unclaimedNobles.remove(noble);
    }

    public void updateTokens(TokenCollection tokens) {
        unclaimedTokens.removeTokens(tokens); // Remove Player Tokens
    }

    public Player getPlayerByName(String playerName) {
        for (Player player : this.players) {
            if (Objects.equals(player.getName().toLowerCase(), playerName.toLowerCase())) {
                return player;
            }
        }
        return null;
    }

    public Level getLevel(int levelNumber) {
        for (Level level : market){
            if(level.getLevel() == levelNumber){
                return level;
            }
        }
        throw new IllegalArgumentException("Level number does not exist in market" + levelNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Game game = (Game) obj;
        return gameId == game.gameId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(gameId);
    }
}
