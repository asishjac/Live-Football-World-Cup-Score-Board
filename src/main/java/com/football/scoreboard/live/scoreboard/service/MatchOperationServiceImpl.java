package com.football.scoreboard.live.scoreboard.service;

import com.football.scoreboard.live.scoreboard.exception.MatchNotFoundException;
import com.football.scoreboard.live.scoreboard.model.Match;
import com.football.scoreboard.live.scoreboard.repository.MatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        var match = matchRepository.saveMatch(new Match(homeTeam, awayTeam, 0, 0));
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
        var matches = matchRepository.findAllMatches();
        // If matches are not empty, process them
        return switch (matches.size()) {
            case 0 -> {
                log.info("No active matches found.");
                yield List.of();
            }
            default -> {
                log.info("Formatting scoreboard for {} matches.", matches.size());
                var orderedMatches = orderMatches(matches);
                yield formatScoreBoard(orderedMatches);
            }
        };
    }

    private boolean isTeamPlaying(Match match, String team) {
        return match.homeTeam().equalsIgnoreCase(team) || match.awayTeam().equalsIgnoreCase(team);
    }

    private Match getMatchById(String matchId) {
        return Optional.ofNullable(matchRepository.findMatchById(matchId))
                .orElseThrow(() -> new MatchNotFoundException("Match with ID " + matchId + " not found"));
    }

    private List<Match> orderMatches(List<Match> matches) {
        return matches.stream()
                .sorted(Comparator.comparingInt(Match::getTotalScore).reversed()
                        .thenComparing(Match::startTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private List<String> formatScoreBoard(List<Match> matches) {
        return IntStream.range(0, matches.size())
                .mapToObj(i -> {
                    Match match = matches.get(i);
                    return (i + 1) + ". " + match.homeTeam() + " " + match.homeTeamScore() +
                            " - " + match.awayTeam() + " " + match.awayTeamScore();
                }).collect(Collectors.toList());
    }
}
