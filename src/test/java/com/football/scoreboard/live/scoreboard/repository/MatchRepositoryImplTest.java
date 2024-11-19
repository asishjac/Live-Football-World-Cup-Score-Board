package com.football.scoreboard.live.scoreboard.repository;

import com.football.scoreboard.live.scoreboard.model.Match;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;


class MatchRepositoryImplTest {

    private final MatchRepositoryImpl matchRepository = new MatchRepositoryImpl();


    @Test
    void testSaveMatch() {
        var match = new Match("Team A", "Team B", 0, 0);
        var savedMatch = matchRepository.saveMatch(match);

        assertEquals(match, savedMatch);
        assertEquals(match, matchRepository.findMatchById(match.matchId()));

        // clear liveMatchesMap
        matchRepository.deleteMatchById(match.matchId());
    }

    @Test
    void testFindMatchById() {
        Match match = new Match("Team C", "Team D", 1, 1);
        matchRepository.saveMatch(match);

        var foundMatch = matchRepository.findMatchById(match.matchId());

        assertEquals(match.matchId(), foundMatch.matchId());
        assertEquals("Team C", foundMatch.homeTeam());
        assertEquals("Team D", foundMatch.awayTeam());
        assertEquals(1, foundMatch.awayTeamScore());
        assertEquals(1, foundMatch.homeTeamScore());

        // clear liveMatchesMap
        matchRepository.deleteMatchById(match.matchId());

    }


    @Test
    void testFindAllMatch() {
        var matches = List.of(
                new Match("Team B", "Team C", 10, 10),
                new Match("Team D", "Team E", 0, 0)
        );

        matches.forEach(matchRepository::saveMatch);

        var foundMatches = matchRepository.findAllMatches();
        assertEquals(matches.size(), foundMatches.size());
        assertEquals(matches.get(0).matchId(), foundMatches.get(0).matchId());
        assertEquals(matches.get(1).matchId(), foundMatches.get(1).matchId());

        // clear liveMatchesMap
        matches.forEach(match -> matchRepository.deleteMatchById(match.matchId()));
    }

    @Test
    void testDeleteMatchById() {
        var match = new Match("Team F", "Team G", 2, 2);
        matchRepository.saveMatch(match);

        matchRepository.deleteMatchById(match.matchId());

        var foundMatch = matchRepository.findMatchById(match.matchId());

        assertEquals(null, foundMatch);
    }


}
