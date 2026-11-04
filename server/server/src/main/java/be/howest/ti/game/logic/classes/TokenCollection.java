package be.howest.ti.game.logic.classes;

import be.howest.ti.game.logic.enums.GemType;

import java.util.*;

public class TokenCollection {
    private final Map<GemType, Integer> tokens;

    public TokenCollection() {
        tokens = new EnumMap<>(GemType.class);
    }

    public TokenCollection(Map<GemType, Integer> tc) {
        this();
        tokens.putAll(tc);
    }

    public Map<GemType, Integer> getTokens() {
        return tokens;
    }

    public boolean contains(GemType token){
        return this.getTokens().containsKey(token);
    }

    public void addTokens(Map<GemType, Integer> other){
        mergeTokens(other, 1);
    }

    public void addTokens(TokenCollection other) {
        mergeTokens(other.getTokens(), 1);
    }

    public void addTokens(GemType key) {
        int currentAmount = tokens.getOrDefault(key, 0);
        tokens.put(key, currentAmount + 1);
    }

    public void addTokens(GemType key, int value) {
        int currentAmount = tokens.getOrDefault(key, 0);
        tokens.put(key, currentAmount + value);
    }

    public void removeTokens(TokenCollection other) {
        mergeTokens(other.getTokens(), -1);
    }

    public void removeTokens(Map<GemType, Integer> other) {
        mergeTokens(other, -1);
    }

    public void removeTokens(GemType token, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot remove a negative amount of tokens.");
        }

        int current = tokens.getOrDefault(token, 0);

        if (amount > current) {
            throw new IllegalArgumentException("Not enough " + token + " tokens to remove. Requested: " + amount + ", Available: " + current);
        }

        int newAmount = current - amount;

        if (newAmount == 0) {
            tokens.remove(token);
        } else {
            tokens.put(token, newAmount);
        }
    }


    private void mergeTokens(Map<GemType, Integer> other, int multiplier) {
        TokenCollection temp = new TokenCollection(this.tokens);

        try {
            for (Map.Entry<GemType, Integer> entry : other.entrySet()) {
                GemType type = entry.getKey();
                int amount = entry.getValue() * multiplier;
                if (amount > 0) {
                    temp.addTokens(type, amount);
                } else {
                    temp.removeTokens(type, -amount);
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Token merge failed: " + e.getMessage());
        }

        this.tokens.clear();
        this.tokens.putAll(temp.getTokens());
    }
    public boolean canTakeTwoOfSameColor(GemType type) {
    return tokens.getOrDefault(type, 0) >= 4;
    }

    public boolean hasAtLeast(TokenCollection other) {
        for (GemType gem : GemType.values()) {
            if (this.getAmount(gem) < other.getAmount(gem)) {
                return false;
            }
        }
        return true;
    }

    public int getAmount(GemType type) {
        return tokens.getOrDefault(type, 0);
    }

    @Override
    public String toString() {
        return tokens.toString();
    }
}
