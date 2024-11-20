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

    /**
     * Starts a new football match between the specified home team and away team.
     * This method validates the teams, checks if any of the teams are already playing in another match,
     * creates a new match with initial scores of 0-0, saves it to the repository, and returns it.
     *
     * @param homeTeam The name of the home team.
     * @param awayTeam The name of the away team.
     * @return The newly created Match record.
     * @throws IllegalStateException if either of the teams are already playing in another match.
     * @throws IllegalArgumentException if either of the teams are not valid.
     */
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

    /**
     * Updates the score of a football match with the provided match ID.
     * This method first validates the match ID and the scores to ensure they are valid.
     * It then retrieves the match with the provided ID, updates its score with the new scores,
     * saves the updated match to the repository, and logs the successful update.
     *
     * @param matchId        The ID of the match to update.
     * @param homeTeamScore  The new score of the home team.
     * @param awayTeamScore  The new score of the away team.
     * @throws MatchNotFoundException if no match is found with the provided ID.
     * @throws IllegalArgumentException if the provided scores or match ID are invalid.
     */
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

    /**
     * Finishes a football match with the provided match ID.
     * This method first validates the match ID to ensure it is valid.
     * It then retrieves the match with the provided ID, deletes it from the repository,
     * and logs the successful completion of the match.
     *
     * @param matchId The ID of the match to finish.
     * @throws MatchNotFoundException if no match is found with the provided ID.
     * @throws IllegalArgumentException if the provided match ID is invalid.
     */
    @Override
    public void finishMatch(String matchId) {
        log.info("Finishing match {}", matchId);
        isValidString(matchId);
        var match = getMatchById(matchId);
        matchRepository.deleteMatchById(match.matchId());
        log.info("Match finished successfully for match ID: {}", match.matchId());
    }

    /**
     * Retrieves a summary of all active matches in the scoreboard.
     * The summary includes the match details in a formatted manner.
     *
     * @return A list of strings representing the match summary.
     *         Each string contains the match details in the format:
     *         "{match_position}. {home_team} {home_team_score} - {away_team} {away_team_score}"
     *         The matches are ordered by total score in descending order,
     *         and in case of a tie, by start time in reverse chronological order.
     *         If no active matches are found, an empty list is returned.
     */
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

    /**
     * Retrieves a match record from the repository based on the provided match ID.
     * If a match with the given ID is found, it is returned.
     * If no match is found, a {@link MatchNotFoundException} is thrown.
     *
     * @param matchId The ID of the match to retrieve.
     * @return The Match record with the provided match ID.
     * @throws MatchNotFoundException if no match is found with the provided ID.
     */
    private Match getMatchById(String matchId) {
        return Optional.ofNullable(matchRepository.findMatchById(matchId))
                .orElseThrow(() -> new MatchNotFoundException("Match with ID " + matchId + " not found"));
    }

    /**
     * Orders a list of matches based on total score and start time.
     *
     * @param matches The list of matches to be ordered.
     * @return The ordered list of matches. The matches are sorted in descending order of total score.
     *         In case of a tie in total score, matches are sorted in reverse chronological order based on start time.
     */
    private List<Match> orderMatches(List<Match> matches) {
        return matches.stream()
                .sorted(Comparator.comparingInt(Match::getTotalScore).reversed()
                        .thenComparing(Match::startTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    /**
     * Formats a list of matches into a scoreboard summary.
     *
     * @param matches The list of matches to be formatted.
     * @return A list of strings representing the formatted scoreboard.
     *         Each string contains the match details in the format:
     *         "{match_position}. {home_team} {home_team_score} - {away_team} {away_team_score}"
     */
    private List<String> formatScoreBoard(List<Match> matches) {
        return IntStream.range(0, matches.size())
                .mapToObj(i -> {
                    Match match = matches.get(i);
                    return (i + 1) + ". " + match.homeTeam() + " " + match.homeTeamScore() +
                            " - " + match.awayTeam() + " " + match.awayTeamScore();
                }).collect(Collectors.toList());
    }
}
