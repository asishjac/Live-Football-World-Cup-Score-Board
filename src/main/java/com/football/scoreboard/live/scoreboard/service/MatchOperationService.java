package com.football.scoreboard.live.scoreboard.service;

import com.football.scoreboard.live.scoreboard.model.Match;

import java.util.List;

public interface MatchOperationService {

    Match startMatch(String homeTeam, String awayTeam);

    Match getMatch(String matchId);

    void finishMatch(String matchId);

    List<String> getMatchSummary();

}
