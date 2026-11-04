package be.howest.ti.game.logic.classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {
    // General fields
    private final String name;

    // Fields for before game has started
    private boolean isReady;

    // Fields for after game has started
    private final List<Card> bought;
    private final List<Card> reserved;
    private final Set<Noble> nobles;
    private final TokenCollection tokens;
    private int totalPrestigePoints;
    private final String playerToken;

    @SuppressWarnings("unused")

    // Constructor
    public Player(String name, String playerToken) {
        this.name = name;
        this.isReady = false;
        this.bought = new ArrayList<>();
        this.reserved = new ArrayList<>();
        this.nobles = new HashSet<>();
        this.tokens = new TokenCollection();
        this.totalPrestigePoints = 0;
        this.playerToken = playerToken;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public TokenCollection getBonuses() {
        TokenCollection tc = new TokenCollection();
        for (Card card : bought) {
            tc.addTokens(card.getBonus());
        }
        return tc;
    }

    public List<Card> getBought() {
        return bought;
    }

    public List<Card> getReserved() {
        return reserved;
    }

    public Set<Noble> getNobles() {
        return nobles;
    }

    public TokenCollection getTokens() {
        return tokens;
    }

    public String getPlayerToken() {
        return playerToken;
    }

    public int getTotalPrestigePoints() {
        return totalPrestigePoints;
    }

    public void updateTotalPrestigePoints() {
        // Calculate the total prestige points based on the bought cards
        totalPrestigePoints = 0;
        for (Card card : bought) {
            totalPrestigePoints += card.getPrestigePoints();
        }
        // Calculate nobles prestige points
        for (Noble noble : nobles) {
            totalPrestigePoints += noble.getPrestigePoints();
        }
    }

    public boolean isReady() {
        return isReady;
    }

    public void toggleReady() {
        this.isReady = !this.isReady;
    }

    public void updateTokens(TokenCollection tc) {
        tokens.addTokens(tc);
    }

    public TokenCollection buyCard(Card dev, TokenCollection payment) {
        TokenCollection price = dev.calculateActualPayment(payment, this.getBonuses());
        tokens.removeTokens(price);
        bought.add(dev);
        updateTotalPrestigePoints();

        return price;
    }

    public void reserveCard(Card dev) {
        // Check how many cards are already reserved
        if (reserved.size() >= 3) {
            throw new IllegalStateException("Cannot reserve more than 3 cards");
        }
        reserved.add(dev);
    }

    public TokenCollection buyReserveCard(Card dev, TokenCollection payment) {
        TokenCollection price = dev.calculateActualPayment(payment, this.getBonuses());
        tokens.removeTokens(price);
        reserved.remove(dev);
        bought.add(dev);
        updateTotalPrestigePoints();

        return price;
    }

    public void takeNoble(Noble noble) {
        if (!canTakeNoble(noble)) {
            throw new IllegalStateException("Not enough bonuses to take this noble");
        }
        this.nobles.add(noble);
        updateTotalPrestigePoints();
    }

    public boolean canTakeNoble(Noble noble) {
        TokenCollection playerBonuses = this.getBonuses();
        TokenCollection requiredBonuses = noble.getNeededBonuses();

        return playerBonuses.hasAtLeast(requiredBonuses);
    }
}