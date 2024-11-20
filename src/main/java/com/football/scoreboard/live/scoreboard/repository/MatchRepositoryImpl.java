package com.football.scoreboard.live.scoreboard.repository;

import com.football.scoreboard.live.scoreboard.model.Match;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class MatchRepositoryImpl implements MatchRepository{

    private final Map<String, Match> liveMatchesMap = new ConcurrentHashMap<>();
    @Override
    public Match saveMatch(Match match) {
        liveMatchesMap.put(match.matchId(), match);
        return match;
    }

    @Override
    public Match findMatchById(String matchId) {
        return liveMatchesMap.get(matchId);
    }

    @Override
    public List<Match> findAllMatches() {
        return liveMatchesMap.values().stream()
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void deleteMatchById(String matchId) {
        liveMatchesMap.remove(matchId);
    }
}
