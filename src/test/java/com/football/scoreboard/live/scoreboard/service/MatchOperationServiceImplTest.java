package com.football.scoreboard.live.scoreboard.service;

import com.football.scoreboard.live.scoreboard.model.Match;
import com.football.scoreboard.live.scoreboard.repository.MatchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchOperationServiceImplTest {

    private final MatchRepository matchRepository = mock(MatchRepository.class);
    private final MatchOperationService matchOperationService = new MatchOperationServiceImpl(matchRepository);

    @Nested
    @DisplayName("Start Match Test Scenarios")
    class StartMatchTestScenarios {
        @Test
        void testStartMatch() {
            var match = new Match("Team A", "Team B", 0, 0);

            when(matchRepository.findAllMatches()).thenReturn(List.of());
            when(matchRepository.saveMatch(any(Match.class))).thenReturn(match);

            matchOperationService.startMatch("Team A", "Team B");

            // Verify that the match was saved to the repository
            Mockito.verify(matchRepository).saveMatch(any(Match.class));
        }

        @Test
        void testReturnedMatchAfterStart() {

            var match = new Match("Team A", "Team B", 0, 0);

            when(matchRepository.findAllMatches()).thenReturn(List.of());
            when(matchRepository.saveMatch(any(Match.class))).thenReturn(match);

            var returnedMatch = matchOperationService.startMatch("Team A", "Team B");

            assertNotNull(returnedMatch.matchId(), "Match Id should not be null after starting");
            assertEquals(match.homeTeam(), returnedMatch.homeTeam());
            assertEquals(match.awayTeam(), returnedMatch.awayTeam());
            assertEquals(match.homeTeamScore(), returnedMatch.homeTeamScore(), "Home team score should be 0 after starting");
            assertEquals(match.awayTeamScore(), returnedMatch.awayTeamScore(), "Away team score should be 0 after starting");
            assertEquals(match.getTotalScore(), returnedMatch.getTotalScore(), "Total score should be 0 after starting");
            assertEquals(match.startTime(), returnedMatch.startTime(), "Start time should be same");
        }

        @Test
        void testStartMatchIfTeamsAreTheSame() {

            var exceptionThrown = assertThrows(IllegalArgumentException.class, () -> matchOperationService.startMatch("Team A", "Team A"));

            assertEquals("Home and Away teams cannot be the same", exceptionThrown.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "\t", "\n"})
        void testStartMatchIfTeamsAreNullOrEmpty(String team) {

            var exceptionThrown = assertThrows(IllegalArgumentException.class, () -> matchOperationService.startMatch(team, "Team B"));
            assertEquals("Input string cannot be null, empty, or contain only whitespaces", exceptionThrown.getMessage());

            exceptionThrown = assertThrows(IllegalArgumentException.class, () -> matchOperationService.startMatch("Team A", team));
            assertEquals("Input string cannot be null, empty, or contain only whitespaces", exceptionThrown.getMessage());
        }

        @ParameterizedTest
        @MethodSource("provideStartMatchData")
        void testStartMatchIfAnyTeamsAreAlreadyPlaying(String homeTeam, String awayTeam, List<Match> matchesInProgress) {

            when(matchRepository.findAllMatches()).thenReturn(matchesInProgress);

            var exceptionThrown = assertThrows(IllegalStateException.class, () -> matchOperationService.startMatch(homeTeam, awayTeam));

            assertEquals("A match is already in progress involving one or both of the teams.", exceptionThrown.getMessage());
        }

        static Stream<Arguments> provideStartMatchData() {
            return Stream.of(
                    // Case 1: Away Team already in progress as away team in existing match, exception should be thrown
                    Arguments.of("Team A", "Team C", List.of(
                            new Match("Team B", "Team C", 10, 10)
                    )),

                    // Case 2: Away Team already in progress as home team in existing match, exception should be thrown
                    Arguments.of("Team A", "Team C", List.of(
                            new Match("Team C", "Team X", 10, 10)
                    )),

                    // Case 3: Home team already in progress as away team in existing match, exception should be thrown
                    Arguments.of("Team B", "Team X", List.of(
                            new Match("Team A", "Team B", 10, 10)
                    )),

                    // Case 4: Home team already in progress as home team in existing match, exception should be thrown
                    Arguments.of("Team B", "Team X", List.of(
                            new Match("Team B", "Team A", 10, 10)
                    ))
            );
        }
    }


    @Nested
    @DisplayName("Update Match Score Test Scenarios")
    class UpdateMatchTestScenarios {

        @Test
        void testUpdateMatchScore() {
            var match = new Match("Team A", "Team B", 0, 0);
            when(matchRepository.findMatchById("1001")).thenReturn(match);

            matchOperationService.updateMatchScore("1001", 2, 3);

            verify(matchRepository).findMatchById("1001");
            //verify(matchRepository).(match);
        }
    }

}
