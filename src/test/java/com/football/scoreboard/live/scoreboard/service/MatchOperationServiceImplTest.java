package com.football.scoreboard.live.scoreboard.service;

import com.football.scoreboard.live.scoreboard.model.Match;
import com.football.scoreboard.live.scoreboard.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchOperationServiceImplTest {

    private final MatchRepository matchRepository = mock(MatchRepository.class);
    private final MatchOperationService matchOperationService = new MatchOperationServiceImpl(matchRepository);

    @Test
    void testStartMatch() {

        when(matchRepository.saveMatch("Team A", "Team B")).thenReturn(new Match("Team A", "Team B", 0, 0));

        matchOperationService.startMatch("Team A", "Team B");

        // Verify that the match was saved to the repository
        Mockito.verify(matchRepository).saveMatch("Team A", "Team B");
    }

    @Test
    void testReturnedMatchAfterStart() {

        when(matchRepository.saveMatch("Team A", "Team B")).thenReturn(new Match("Team A", "Team B", 0, 0));

        var match = matchOperationService.startMatch("Team A", "Team B");

        assertNotNull(match.matchId(), "Match Id should not be null after starting");
        assertEquals("Team A", match.homeTeam());
        assertEquals("Team B", match.awayTeam());
        assertEquals(0, match.homeTeamScore(), "Home team score should be 0 after starting");
        assertEquals(0, match.awayTeamScore(), "Away team score should be 0 after starting");
        assertEquals(0, match.getTotalScore(), "Total score should be 0 after starting");
        assertEquals(LocalDateTime.now().getMinute(), match.startTime().getMinute(), "Start time should be within the current minute");
    }

    @Test
    void testStartMatchIfTeamsAreTheSame() {

        var exceptionThrown = assertThrows(IllegalArgumentException.class, () -> matchOperationService.startMatch("Team A", "Team A"));

        assertEquals("Home and away teams cannot be the same", exceptionThrown.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void testStartMatchIfTeamsAreNullOrEmpty(String team) {

        var exceptionThrown = assertThrows(IllegalArgumentException.class, () -> matchOperationService.startMatch(team, "Team B"));
        assertEquals("Home and away teams cannot be null or empty", exceptionThrown.getMessage());

        exceptionThrown = assertThrows(IllegalArgumentException.class, () -> matchOperationService.startMatch("Team A", team));
        assertEquals("Home and away teams cannot be null or empty", exceptionThrown.getMessage());
    }

}
