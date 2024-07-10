package pl.Aevise.SupperSpeed.api.controller.utils;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.Aevise.SupperSpeed.api.controller.utils.NameBeautifier.*;

class NameBeautifierTest {

    @ParameterizedTest
    @MethodSource
    void checkThatFirstLetterOfAWordIsCapitalized(String expected, String input) {
        assertThat(expected).isEqualTo(capitalizeFirstLetter(input));
    }

    private static Stream<Arguments> checkThatFirstLetterOfAWordIsCapitalized() {
        return Stream.of(
                Arguments.of("Warsaw", "warsaw"),
                Arguments.of("Warsaw", "wArSaW"),
                Arguments.of("Warsaw", "WaRsAw"),
                Arguments.of("Warsaw", "WARSAW"),
                Arguments.of("Warsaw", "WARsaw"),
                Arguments.of("Warsaw", "warSAW")
        );
    }

    @ParameterizedTest
    @MethodSource
    void checkThatNamesAreCorrectlySplitAndCapitalized(String expected, String input) {
        assertThat(expected).isEqualTo(handleName(input));
    }

    private static Stream<Arguments> checkThatNamesAreCorrectlySplitAndCapitalized() {
        return Stream.of(
                Arguments.of("Warsaw", "warsaw"),
                Arguments.of("Warsaw", "wArSaW"),
                Arguments.of("Warsaw", "WaRsAw"),
                Arguments.of("Warsaw", "WARSAW"),
                Arguments.of("Warsaw", "WARsaw"),
                Arguments.of("War Saw", "war SAW"),
                Arguments.of("War Saw", "waR SAW"),
                Arguments.of("War Saw", "WAR SAW"),
                Arguments.of("War Saw", "wAr sAw"),
                Arguments.of("War Saw", "WaR SaW"),
                Arguments.of("War-Saw", "war-SAW"),
                Arguments.of("War-Saw", "waR-SAW"),
                Arguments.of("War-Saw", "WAR-SAW"),
                Arguments.of("War-Saw", "wAr-sAw"),
                Arguments.of("War-Saw", "WaR-SaW")
        );
    }
}