package com.solvd.laba.football.service.impl;

import com.solvd.laba.football.domain.Player;
import com.solvd.laba.football.domain.PlayerPerformance;
import com.solvd.laba.football.domain.Team;
import com.solvd.laba.football.service.GameOutcomePredictorService;

import java.util.List;

import static java.util.Collections.max;

public class GameOutcomePredictorServiceImpl implements GameOutcomePredictorService {
    @Override
    public Team predictGameWinner(Team teamA, Team teamB) {
        double teamAScore = calculateTeamScore(teamA);
        double teamBScore = calculateTeamScore(teamB);

        if (teamAScore > teamBScore) {
            return teamA;
        } else {
            return teamB;
        }
    }

    private double calculatePlayerScore(Player player) {
        double score = 0;
        int matchingPerformancesCount = 0;
        for (PlayerPerformance playerPerformance : player.getPlayerPerformances()) {
            // take into account only performance with most probable position
            if (playerPerformance.getPosition().getId().equals(player.getPreferredPosition().getId())) {
                matchingPerformancesCount++;
                score += max(
                        List.of(playerPerformance.getDefensivePerformance(),
                                playerPerformance.getOffensivePerformance(),
                                playerPerformance.getCooperativePerformance())
                );
            }
        }

        if (matchingPerformancesCount < 1) {
            return 0;
        } else {
            return score / matchingPerformancesCount;
        }
    }

    private double calculateTeamScore(Team team) {
        double teamsScore = 0;
        for (Player player : team.getPlayers()) {
            teamsScore += calculatePlayerScore(player);
        }
        if (team.getPlayers().isEmpty()) {
            return 0;
        } else {
            return teamsScore / team.getPlayers().size();
        }
    }
}
