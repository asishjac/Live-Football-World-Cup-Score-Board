package com.football.scoreboard.live.scoreboard.repository;

import com.football.scoreboard.live.scoreboard.model.Match;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Comparator;
import java.util.List;


class MatchRepositoryImplTest {

    private final MatchRepositoryImpl matchRepository = new MatchRepositoryImpl();


    @Test
    void testSaveMatch() {
        var match = new Match("Team A", "Team B", 0, 0);
        var savedMatch = matchRepository.saveMatch(match);

        assertEquals(match, savedMatch);
        assertEquals(match, matchRepository.findMatchById(match.matchId()));

    }

    @Test
    void testFindMatchById() {
        var match = new Match("Team C", "Team D", 1, 1);
        matchRepository.saveMatch(match);

        var foundMatch = matchRepository.findMatchById(match.matchId());

        assertEquals(match.matchId(), foundMatch.matchId());
        assertEquals("Team C", foundMatch.homeTeam());
        assertEquals("Team D", foundMatch.awayTeam());
        assertEquals(1, foundMatch.awayTeamScore());
        assertEquals(1, foundMatch.homeTeamScore());

    }


    @Test
    void testFindAllMatch() {
        var matches = List.of(
                new Match("Team W", "Team X", 10, 10),
                new Match("Team Y", "Team Z", 0, 0)
        );

        matches.forEach(matchRepository::saveMatch);

        var foundMatches = matchRepository.findAllMatches();

        // Sort both lists by matchId
        var sortedExpected = matches.stream()
                .sorted(Comparator.comparing(Match::matchId))
                .toList();

        var sortedFound = foundMatches.stream()
                .sorted(Comparator.comparing(Match::matchId))
                .toList();

        assertEquals(sortedExpected.size(), sortedFound.size());
        assertEquals(
                sortedExpected.stream().map(Match::matchId).toList(),
                sortedFound.stream().map(Match::matchId).toList()
        );
    }

    @Test
    void testDeleteMatchById() {
        var match = new Match("Team F", "Team G", 2, 2);
        matchRepository.saveMatch(match);

        matchRepository.deleteMatchById(match.matchId());

        var foundMatch = matchRepository.findMatchById(match.matchId());

        assertNull(foundMatch);
    }


}
