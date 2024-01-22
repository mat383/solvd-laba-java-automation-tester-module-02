package com.solvd.laba.football.service;

import com.solvd.laba.football.domain.Team;

public interface GameOutcomePredictorService {

    /**
     * predicts most probable game winner
     *
     * @return most probable winner
     */
    public Team predictGameWinner(Team teamA, Team teamB);
}
