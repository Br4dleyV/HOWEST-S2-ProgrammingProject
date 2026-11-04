package be.howest.ti.game.logic.classes;

import java.util.*;

import be.howest.ti.game.logic.enums.GemType;

public class DataReader {

    public static List<Noble> readNobles(String resourcePath) {
        List<Noble> nobles = new ArrayList<>();
        try (Scanner scanner = new Scanner(DataReader.class.getResourceAsStream(resourcePath))) {
            if (scanner.hasNextLine())
                scanner.nextLine(); // skip header
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank() || line.startsWith("#"))
                    continue;
                String[] parts = line.split("\t");
                if (parts.length == 3) {
                    nobles.add(new Noble(parts[0], parseCostToTokens(parts[1]), Integer.parseInt(parts[2])));
                }
            }
        }
        return nobles;
    }

    public static List<Card> readDevelopmentCards(String resourcePath) {
        List<Card> cards = new ArrayList<>();
        try (Scanner scanner = new Scanner(DataReader.class.getResourceAsStream(resourcePath))) {
            if (scanner.hasNextLine())
                scanner.nextLine(); // skip header
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank() || line.startsWith("#"))
                    continue;
                String[] parts = line.split("\t");
                if (parts.length == 6) {
                    String name = parts[0];
                    int level = Integer.parseInt(parts[1]);
                    int points = Integer.parseInt(parts[4]);
                    String cost = parts[5];
                    GemType bonus = typeToGem(parts[2]);
                    cards.add(new Card(name, level, parseCostToTokens(cost), bonus, points));
                }
            }
        }
        return cards;
    }

    // Helper om Type-letter om te zetten naar GemType enum naam
    private static GemType typeToGem(String type) {
        switch (type) {
            case "C":
                return GemType.DIAMOND;
            case "E":
                return GemType.EMERALD;
            case "O":
                return GemType.ONYX;
            case "R":
                return GemType.RUBY;
            case "S":
                return GemType.SAPPHIRE;
            default:
                return GemType.GOLD;
        }
    }

    // Helper om kosten naar een TokenCollection te parseren
    private static TokenCollection parseCostToTokens(String cost) {
        Map<GemType, Integer> counts = new EnumMap<>(GemType.class);
        for (char c : cost.toCharArray()) {
            GemType type = null;
            switch (c) {
                case 'C':
                    type = GemType.DIAMOND;
                    break;
                case 'E':
                    type = GemType.EMERALD;
                    break;
                case 'O':
                    type = GemType.ONYX;
                    break;
                case 'R':
                    type = GemType.RUBY;
                    break;
                case 'S':
                    type = GemType.SAPPHIRE;
                    break;
                default:
                    continue;
            }
            counts.put(type, counts.getOrDefault(type, 0) + 1);
        }
        TokenCollection tokens = new TokenCollection();
        tokens.addTokens(counts);
        return tokens;
    }

    public static void main(String[] args) {
        readNobles("/data/nobles.txt");
        readDevelopmentCards("/data/developments.txt");
    }
}