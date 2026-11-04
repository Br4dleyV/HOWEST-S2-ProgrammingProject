package be.howest.ti.game.logic.classes;

import java.util.*;

public class Level {
    private final int levelNumber;
    private int cardStackSize;
    private final List<Card> visibleCards;
    private final List<Card> unclaimedCards;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.visibleCards = new ArrayList<>();
        this.unclaimedCards = new ArrayList<>();
    }

    public int getCardStackSize() {
        return cardStackSize;
    }

    public int getLevel() {
        return levelNumber;
    }

    public List<Card> getVisibleCards() {
        return visibleCards;
    }

    public List<Card> getUnclaimedCards() {
        return unclaimedCards;
    }

    public void addCardToUnclaimed(Card card) {
        unclaimedCards.add(card);
        incrementCardStack();
    }

    public void decrementCardStack() {
        if (cardStackSize > 0) {
            cardStackSize--;
        }
    }

    public void incrementCardStack() {
        cardStackSize++;
    }

    public void drawNewCard() {
        // Take upper card from deck
        Card newCard = unclaimedCards.getLast();
        // Remove upper card from deck
        unclaimedCards.removeLast();
        // Place upper card on market
        visibleCards.add(newCard);
    }

    public void removeVisibleCard(Card dev) {
        visibleCards.remove(dev);
    }

    public void removeUnclaimedCard(Card card) {
        if (unclaimedCards.contains(card)) {
            unclaimedCards.remove(card);
            decrementCardStack();
        }
    }
}