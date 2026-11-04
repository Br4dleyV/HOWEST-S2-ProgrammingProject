package be.howest.ti.game.logic.classes;

import be.howest.ti.game.logic.enums.GemType;

import java.util.*;

public class Card {
    private final String name;
    private final int level;
    private final TokenCollection cost;
    private final GemType bonus;
    private final int prestigePoints;

    public Card(String name, int level, TokenCollection cost, GemType bonus, int prestigePoints) {
        this.name = name;
        this.level = level;
        this.cost = cost;
        this.bonus = bonus;
        this.prestigePoints = prestigePoints;
    }

    public Card(String name) {
        this.name = name;
        this.level = 1;
        this.cost = new TokenCollection();
        this.bonus = GemType.RUBY;
        this.prestigePoints = 1;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public TokenCollection getCost() {
        return cost;
    }

    public GemType getBonus() {
        return bonus;
    }

    public int getPrestigePoints() {
        return prestigePoints;
    }

    public TokenCollection calculateActualPayment(TokenCollection receivedPayment, TokenCollection bonuses) {
        TokenCollection cardCost = new TokenCollection(this.getCost().getTokens());
        TokenCollection actualPayment = new TokenCollection();
        int remainingGold = receivedPayment.getAmount(GemType.GOLD);

        // Loops through cost of a card
        // Checks if you have a bonus card and deducts it from the price
        // Checks if you have fully paid and uses gold where necessary
        // Returns the price that will be paid
        for (Map.Entry<GemType, Integer> entry : cardCost.getTokens().entrySet()) {
            GemType gem = entry.getKey();
            int costAmount = entry.getValue();
            int cardBonus = bonuses.getAmount(gem);
            int effectiveCost = Math.max(0, costAmount - cardBonus);

            int available = receivedPayment.getAmount(gem);
            int used = Math.min(available, effectiveCost);
            actualPayment.addTokens(gem, used);

            int shortfall = effectiveCost - used;

            if (shortfall > 0) {
                if (remainingGold < shortfall) {
                    throw new IllegalStateException("You dont have enough tokens to buy this card.");
                }
                actualPayment.addTokens(GemType.GOLD, shortfall);
                remainingGold -= shortfall;
            }
        }

        return actualPayment;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", level='" + level + '\'' +
                ", cost=" + cost +
                ", bonus=" + bonus +
                ", prestigePoints=" + prestigePoints +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Card card = (Card) obj;
        return Objects.equals(name, card.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}