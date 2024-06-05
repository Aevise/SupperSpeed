package pl.Aevise.SupperSpeed.api.controller.utils;

import java.util.List;

public class NameBeautifier {

    private final static List<String> NAME_SEPARATORS = List.of(" ", "-");

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static String handleName(String cityName) {
        if (cityName == null || cityName.isEmpty()) {
            return cityName;
        }
        String newCityName = cityName;

        for (String character : NAME_SEPARATORS) {
            if (cityName.contains(character)) {
                newCityName = nameSplitterAndModifier(cityName, character);
            }
        }

        return newCityName;
    }

    private static String nameSplitterAndModifier(String cityName, String characterToSplit) {
        String[] input = cityName.split(characterToSplit);
        StringBuilder beautifiedName = new StringBuilder();
        for (String part : input) {
            beautifiedName.append(capitalizeFirstLetter(part)).append(characterToSplit);
        }
        return beautifiedName.substring(0, beautifiedName.length() - 1);
    }
}
