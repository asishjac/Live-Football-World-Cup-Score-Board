package com.football.scoreboard.live.scoreboard.repository;

import com.football.scoreboard.live.scoreboard.model.Match;

import java.util.List;

public interface MatchRepository {

    Match saveMatch(String homeTeam, String awayTeam);

    Match findMatchById(String matchId);

    List<Match> findAllMatches();

    void deleteMatchById(String matchId);
}
