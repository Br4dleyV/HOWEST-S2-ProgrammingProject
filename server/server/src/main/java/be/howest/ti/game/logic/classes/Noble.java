package be.howest.ti.game.logic.classes;

import java.util.Objects;

public class Noble {
    private final String name;
    private final TokenCollection neededBonuses;
    private final int prestigePoints;

    // Constructor
    public Noble(String name, TokenCollection neededBonuses, int prestigePoints) {
        this.name = name;
        this.neededBonuses = neededBonuses;
        this.prestigePoints = prestigePoints;
    }

    // Getters
    public String getName() {
        return name;
    }

    public TokenCollection getNeededBonuses() {
        return neededBonuses;
    }

    public int getPrestigePoints() {
        return prestigePoints;
    }

    @Override
    public String toString() {
        return "Noble{" +
                "name='" + name + '\'' +
                ", neededBonuses=" + neededBonuses +
                ", prestigePoints=" + prestigePoints +
                '}';
    }
    // Override equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Noble noble = (Noble) o;
        return Objects.equals(name, noble.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}