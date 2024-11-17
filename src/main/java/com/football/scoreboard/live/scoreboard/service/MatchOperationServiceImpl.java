package com.football.scoreboard.live.scoreboard.service;

import com.football.scoreboard.live.scoreboard.model.Match;
import com.football.scoreboard.live.scoreboard.repository.MatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class MatchOperationServiceImpl implements MatchOperationService {

    private MatchRepository matchRepository;

    public MatchOperationServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Match startMatch(String homeTeam, String awayTeam) {
        log.info("Starting match between {} and {}", homeTeam, awayTeam);
        return null;
    }

    @Override
    public Match getMatch(String matchId) {
        log.info("Getting match details for {}", matchId);
        return null;
    }

    @Override
    public void finishMatch(String matchId) {
        log.info("Finishing match {}", matchId);

    }

    @Override
    public List<String> getMatchSummary() {
        log.info("Getting match summary");
        return List.of();
    }
}
