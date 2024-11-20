package com.football.scoreboard.live.scoreboard.model;

import lombok.With;

import java.time.LocalDateTime;
import java.util.UUID;

public record Match(
        String matchId,
        String homeTeam,
        String awayTeam,
        @With int homeTeamScore,
        @With int awayTeamScore,
        LocalDateTime startTime
) {

    /**
     * Constructor for the Match record.
     *
     * @param homeTeam       the name of the home team.
     * @param awayTeam       the name of the away team.
     * @param homeTeamScore  the initial score of the home team.
     * @param awayTeamScore  the initial score of the away team.
     */
    public Match(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) {
        this(UUID.randomUUID().toString(), homeTeam, awayTeam, homeTeamScore, awayTeamScore, LocalDateTime.now());
    }

    /**
     * Calculates and returns the total score of the match.
     *
     * @return the total score of the match.
     */
    public int getTotalScore() {
        return homeTeamScore + awayTeamScore;
    }
}
