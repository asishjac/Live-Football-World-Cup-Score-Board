package com.football.scoreboard.live.scoreboard.service;

import com.football.scoreboard.live.scoreboard.exception.MatchNotFoundException;
import com.football.scoreboard.live.scoreboard.model.Match;
import com.football.scoreboard.live.scoreboard.repository.MatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.football.scoreboard.live.scoreboard.util.ValidationUtil.*;

@Service
@Slf4j
public class MatchOperationServiceImpl implements MatchOperationService {

    private final MatchRepository matchRepository;

    public MatchOperationServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Match startMatch(String homeTeam, String awayTeam) {
        log.info("Starting match between {} and {}", homeTeam, awayTeam);
        validateTeams(homeTeam, awayTeam);
        var matchesInProgress = matchRepository.findAllMatches();
        if (matchesInProgress.stream().anyMatch(match ->
                isTeamPlaying(match, homeTeam) || isTeamPlaying(match, awayTeam))) {
            throw new IllegalStateException("A match is already in progress involving one or both of the teams.");
        }
        var match = matchRepository.saveMatch(new Match(homeTeam, awayTeam,0,0));
        log.info("Match started successfully with ID: {}", match.matchId());
        return match;
    }


    @Override
    public void updateMatchScore(String matchId, int homeTeamScore, int awayTeamScore) {
        log.info("Updating match score for match ID: {} with home team score: {} and away team score: {}", matchId, homeTeamScore, awayTeamScore);
        isAbsoluteScore(homeTeamScore);
        isAbsoluteScore(awayTeamScore);
        isValidString(matchId);
        var match = getMatchById(matchId);
        match = matchRepository.saveMatch(match.withAwayTeamScore(awayTeamScore).withHomeTeamScore(homeTeamScore));
        log.info("Match score updated successfully for match ID: {} ", match.matchId());
    }

    @Override
    public void finishMatch(String matchId) {
        log.info("Finishing match {}", matchId);
        isValidString(matchId);
        var match = getMatchById(matchId);
        matchRepository.deleteMatchById(match.matchId());
        log.info("Match finished successfully for match ID: {}", match.matchId());
    }

    @Override
    public List<String> getMatchSummary() {
        log.info("Getting match summary");
        return List.of();
    }

    private boolean isTeamPlaying(Match match, String team) {
        return match.homeTeam().equalsIgnoreCase(team) || match.awayTeam().equalsIgnoreCase(team);
    }

    private Match getMatchById(String matchId){
        return Optional.ofNullable(matchRepository.findMatchById(matchId))
                .orElseThrow(() -> new MatchNotFoundException("Match with ID " + matchId + " not found"));
    }
}
