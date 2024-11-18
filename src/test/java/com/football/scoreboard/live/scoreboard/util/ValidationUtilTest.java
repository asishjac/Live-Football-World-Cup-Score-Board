package com.football.scoreboard.live.scoreboard.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void testValidateTeamsWithValidInputDoesNotThrowException() {
        assertDoesNotThrow(() ->
                ValidationUtil.validateTeams("Team A", "Team B"), "Home and Away teams cannot be the same");
    }

    @Test
    void testValidateTeamsWithSameTeamThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                ValidationUtil.validateTeams("Team A", "Team A"), "Home and away teams cannot be the same");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void testIsValidStringWithInvalidInputs(String str) {
        assertThrows(IllegalArgumentException.class, () ->
                ValidationUtil.isValidString(str), "Input string cannot be null, empty, or contain only whitespaces");
    }

    @Test
    void testIsValidStringWithValidInputDoesNotThrowException() {
        var result = ValidationUtil.isValidString("Valid String");
        assertTrue(result, "Input string is valid");
    }
}
