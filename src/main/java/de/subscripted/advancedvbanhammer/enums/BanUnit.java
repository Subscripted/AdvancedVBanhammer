package de.subscripted.advancedvbanhammer.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum BanUnit {

    SECOND("Sekunde(n)", 1, "s"),
    MINUTE("Minute(n)", 60, "m"),
    HOUR("Stunde(n)", 60 * 60, "h"),
    DAY("Tag(e)", 24 * 60 * 60, "d"),
    WEEK("Woche(n)", 7 * 24 * 60 * 60, "w");

    private final String name;
    private final long toSecond;
    private final String shortcut;

    BanUnit(String name, long toSecond, String shortcut) {
        this.name = name;
        this.toSecond = toSecond;
        this.shortcut = shortcut;
    }

    public static List<String> getUnitAsString() {
        List<String> units = new ArrayList<>();
        for (BanUnit unit : BanUnit.values()) {
            units.add(unit.getShortcut().toLowerCase());
        }
        return units;
    }

    public static BanUnit getUnit(String unit) {
        for (BanUnit units : BanUnit.values()) {
            if (units.getShortcut().equalsIgnoreCase(unit)) {
                return units;
            }
        }
        return null;
    }
}