package com.football.scoreboard.live.scoreboard.repository;

import com.football.scoreboard.live.scoreboard.model.Match;

import java.util.List;

public interface MatchRepository {

    Match saveMatch(Match match);

    Match findMatchById(String matchId);

    List<Match> findAllMatches();

    void deleteMatchById(String matchId);
}
