package com.football.scoreboard.live.scoreboard.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {

    /**
     * Validates the home and away team names for a match.
     *
     * @param homeTeam The name of the home team. It cannot be null, empty, or contain only whitespaces.
     * @param awayTeam The name of the away team. It cannot be null, empty, or contain only whitespaces.
     *
     * @throws IllegalArgumentException If either the home or away team name is invalid or if both team names are the same.
     */
    public static void validateTeams(String homeTeam, String awayTeam) {
        isValidString(homeTeam);
        isValidString(awayTeam);
        if (homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new IllegalArgumentException("Home and Away teams cannot be the same");
        }
    }

    /**
     * Validates if a given string is not null, not empty, and does not contain only whitespaces.
     *
     * @param str The string to be validated.
     *
     * @return {@code true} if the string is valid, i.e., not null, not empty, and does not contain only whitespaces.
     *         {@code false} otherwise.
     *
     * @throws IllegalArgumentException If the input string is null, empty, or contains only whitespaces.
     */
    public static boolean isValidString(String str) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException("Input string cannot be null, empty, or contain only whitespaces");
        }
        return true;
    }

    /**
     * Validates if a given score is a non-negative integer.
     *
     * @param score The score to be validated.
     *
     * @return {@code true} if the score is a non-negative integer, i.e., it is not negative.
     *         {@code false} otherwise.
     *
     * @throws IllegalArgumentException If the input score is negative.
     */
    public static boolean isAbsoluteScore(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        return true;
    }

}
