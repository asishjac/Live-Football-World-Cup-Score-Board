package com.football.scoreboard.live.scoreboard.repository;

import com.football.scoreboard.live.scoreboard.model.Match;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class MatchRepositoryImpl implements MatchRepository{

    private final Map<String, Match> liveMatchesMap = new ConcurrentHashMap<>();
    @Override
    public Match saveMatch(Match match) {
        return null;
    }

    @Override
    public Match findMatchById(String matchId) {
        return null;
    }

    @Override
    public List<Match> findAllMatches() {
        return List.of();
    }

    @Override
    public void deleteMatchById(String matchId) {

    }
}
