package be.howest.ti.game.logic.classes;

import java.util.List;

public class Nobles {
    private final List<Noble> nobleList;

    public Nobles() {
        this.nobleList = DataReader.readNobles("/data/nobles.txt");
    }

    public List<Noble> getNobles() {
        return nobleList;
    }
}