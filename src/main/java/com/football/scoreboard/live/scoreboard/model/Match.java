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
    public Match(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) {
        this(UUID.randomUUID().toString(), homeTeam, awayTeam, homeTeamScore, awayTeamScore, LocalDateTime.now());
    }

    public int getTotalScore() {
        return homeTeamScore + awayTeamScore;
    }
}
