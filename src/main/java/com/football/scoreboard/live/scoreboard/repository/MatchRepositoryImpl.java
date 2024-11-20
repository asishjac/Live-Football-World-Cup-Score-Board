package com.football.scoreboard.live.scoreboard.repository;

import com.football.scoreboard.live.scoreboard.model.Match;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class implements the MatchRepository interface and provides methods to manage live match data.
 * It uses a ConcurrentHashMap to store matches, ensuring thread-safe operations.
 *
 * This repository is intended to be used only by the MatchOperatorServiceImpl, as service layer logic and validations are handled only there.
 * This repository has only the responsibility to manage data related to matches.
 */
@Repository
@Slf4j
public class MatchRepositoryImpl implements MatchRepository{

    private final Map<String, Match> liveMatchesMap = new ConcurrentHashMap<>();

    /**
     * Saves a match in the liveMatchesMap.
     *
     * @param match The match to be saved.
     * @return The saved match.
     */
    @Override
    public Match saveMatch(Match match) {
        liveMatchesMap.put(match.matchId(), match);
        return match;
    }

    /**
     * Finds a match by its matchId in the liveMatchesMap.
     *
     * @param matchId The matchId of the match to be found.
     * @return The found match or null if not found.
     */
    @Override
    public Match findMatchById(String matchId) {
        return liveMatchesMap.get(matchId);
    }

    /**
     * Retrieves all matches from the liveMatchesMap.
     *
     * @return A list of all matches.
     */
    @Override
    public List<Match> findAllMatches() {
        return liveMatchesMap.values().stream()
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Deletes a match by its matchId from the liveMatchesMap.
     *
     * @param matchId The matchId of the match to be deleted.
     */
    @Override
    public void deleteMatchById(String matchId) {
        liveMatchesMap.remove(matchId);
    }
}
