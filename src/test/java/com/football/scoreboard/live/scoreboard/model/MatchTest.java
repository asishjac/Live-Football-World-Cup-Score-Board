package com.football.scoreboard.live.scoreboard.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    @Test
    void testMatchInitialization() {

        Match match = new Match("Team A", "Team B", 1, 2);

        assertNotNull(match.matchId(), "Match ID should not be null");
        assertEquals("Team A", match.homeTeam(), "Home team should be 'Team A'");
        assertEquals("Team B", match.awayTeam(), "Home team should be 'Team B'");
        assertEquals(1, match.homeTeamScore(), "Home team score should be 1");
        assertEquals(2, match.awayTeamScore(), "Home team score should be 2");
        assertEquals(LocalDateTime.now().getMinute(), match.startTime().getMinute(), "Start time should be within the current minute");
        assertEquals(3,match.getTotalScore(), "Total score should be 3");
    }

    @Test
    void testUniqueMatchId(){

        Match match1 = new Match("Team A", "Team B", 1, 2);
        Match match2 = new Match("Team A", "Team B", 1, 2);

        assertNotNull(match1.matchId(), "Match ID should not be null");
        assertNotNull(match2.matchId(), "Match ID should not be null");
        assertNotEquals(match1.matchId(), match2.matchId(), "Match IDs should be unique");
    }

    @Test
    void testScoreChangeAndOtherFieldsUnchanged(){

        Match match = new Match("Team A", "Team B", 1, 2);
        var matchID = match.matchId();

        //Update home and away team scores
        match = match.withHomeTeamScore(2);
        match = match.withAwayTeamScore(3);

        //Check if the scores have been updated correctly
        assertEquals(matchID, match.matchId(), "Match ID should remain the same");
        assertEquals("Team A", match.homeTeam(), "Home team should remain the same");
        assertEquals("Team B", match.awayTeam(), "Home team should remain the same");
        assertEquals(2, match.homeTeamScore(), "Home team score should be 2");
        assertEquals(3, match.awayTeamScore(), "Away team score should be 3");
        assertEquals(5, match.getTotalScore(), "Total score should be 5");

    }

}
