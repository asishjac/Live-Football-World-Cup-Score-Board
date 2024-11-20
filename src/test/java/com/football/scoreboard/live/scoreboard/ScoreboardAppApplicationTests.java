package com.football.scoreboard.live.scoreboard;

import com.football.scoreboard.live.scoreboard.model.Match;
import com.football.scoreboard.live.scoreboard.repository.MatchRepository;
import com.football.scoreboard.live.scoreboard.service.MatchOperationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScoreboardAppApplicationTests {

    @Autowired
    private MatchOperationService matchOperationService;

    @Autowired
    private MatchRepository matchRepository;

    @Test
    void testStartMatch() {
        var startedMatch = matchOperationService.startMatch("Team A", "Team B");
        assertNotNull(startedMatch);
        assertNotNull(startedMatch.matchId());
        assertEquals("Team A", startedMatch.homeTeam());
        assertEquals("Team B", startedMatch.awayTeam());
        assertEquals(0, startedMatch.homeTeamScore());
        assertEquals(0, startedMatch.awayTeamScore());

        // Finish the match
        matchOperationService.finishMatch(startedMatch.matchId());
    }

    @Test
    void testUpdateMatchScore() {
        var startedMatch = matchOperationService.startMatch("Team C", "Team D");
        matchOperationService.updateMatchScore(startedMatch.matchId(), 1, 2);

        var updatedMatch = matchRepository.findMatchById(startedMatch.matchId());

        assertEquals(1, updatedMatch.homeTeamScore());
        assertEquals(2, updatedMatch.awayTeamScore());

        // Finish the match
        matchOperationService.finishMatch(startedMatch.matchId());
    }

    @Test
    void testFinishMatch() {
        var startedMatch = matchOperationService.startMatch("Team E", "Team F");
        matchOperationService.finishMatch(startedMatch.matchId());

        var finishedMatch = matchRepository.findMatchById(startedMatch.matchId());

        assertNull(finishedMatch);
    }

    @Test
    public void testGetMatchSummary() {

        matchOperationService.startMatch("Mexico", "Canada");
        matchOperationService.startMatch("Spain", "Brazil");
        matchOperationService.startMatch("Germany", "France");
        matchOperationService.startMatch("Uruguay", "Italy");
        matchOperationService.startMatch("Argentina", "Australia");

        var matches = matchRepository.findAllMatches();


        var sortedList = matches.stream()
                .sorted(Comparator.comparing(Match::startTime))
                .toList();

        matchOperationService.updateMatchScore(sortedList.get(0).matchId(), 0, 5);
        matchOperationService.updateMatchScore(sortedList.get(1).matchId(), 10, 2);
        matchOperationService.updateMatchScore(sortedList.get(2).matchId(), 2, 2);
        matchOperationService.updateMatchScore(sortedList.get(3).matchId(), 6, 6);
        matchOperationService.updateMatchScore(sortedList.get(4).matchId(), 3, 1);

        List<String> matchSummary = matchOperationService.getMatchSummary();

        assertEquals(5, matchSummary.size());
        assertEquals("1. Uruguay 6 - Italy 6", matchSummary.get(0));
        assertEquals("2. Spain 10 - Brazil 2", matchSummary.get(1));
        assertEquals("3. Mexico 0 - Canada 5", matchSummary.get(2));
        assertEquals("4. Argentina 3 - Australia 1", matchSummary.get(3));
        assertEquals("5. Germany 2 - France 2", matchSummary.get(4));
    }
}
