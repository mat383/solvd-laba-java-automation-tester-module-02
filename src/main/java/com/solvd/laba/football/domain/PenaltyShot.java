package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PenaltyShot implements Identifiable {
    @Setter(AccessLevel.NONE)
    private Long id;
    private ShootOutcome outcome;
    private PlayerPerformance goalkeeperPerformance;
    private PlayerPerformance shooterPerformance;


    @Override
    public void setId(long id) {
        if (this.id != null) {
            throw new RuntimeException("Player's id can only be set once.");
        }
        this.id = id;
    }
}
