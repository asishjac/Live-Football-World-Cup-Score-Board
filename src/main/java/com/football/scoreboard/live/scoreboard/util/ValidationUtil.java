package com.football.scoreboard.live.scoreboard.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {


    public static void validateTeams(String homeTeam, String awayTeam) {
        isValidString(homeTeam);
        isValidString(awayTeam);
        if (homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new IllegalArgumentException("Home and Away teams cannot be the same");
        }
    }

    public static boolean isValidString(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Input string cannot be null, empty, or contain only whitespaces");
        }
        return true;
    }

    public static boolean isAbsoluteScore(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        return true;
    }

}
