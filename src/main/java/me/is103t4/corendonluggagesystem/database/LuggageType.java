package me.is103t4.corendonluggagesystem.database;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public enum LuggageType {

    SUITCASE(0, "Koffer", "Suitcase"),
    BAG(1, "Tas", "Bag"),
    BAGPACK(2, "Rugzak", "Bagpack"),
    BOX(3, "Doos", "Box"),
    SPORTS_BAG(4, "Sporttas", "Sports Bag"),
    BUSINESS_CASE(5, "Zakenkoffer", "Business Case"),
    CASE(6, "Kist", "Case"),
    OTHER(7, "Overig", "Other");

    private final String nl, en;
    private int id;

    LuggageType(int id, String nl, String en) {
        this.id = id;
        this.nl = nl;
        this.en = en;
    }

    public String get(Locale locale) {
        switch (locale.getLanguage()) {
            case "en":
                return en;
            case "nl":
                return nl;
            default:
                return null;
        }
    }

    public int getId() {
        return id;
    }

    public static List<String> values(Locale locale) {
        return Arrays.stream(values()).map(t -> t.get(locale)).collect(Collectors.toList());
    }

    public static LuggageType viaLocale(String name, Locale locale) {
        for (LuggageType type : values())
            if (type.get(locale).equals(name))
                return type;
        return null;
    }
}
