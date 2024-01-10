package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoalAttempt implements Identifiable {
    @Setter(AccessLevel.NONE)
    private Long id;
    private ShootOutcome outcome;
    private PlayerPerformance defenderPerformance;
    private PlayerPerformance attackerPerformance;
    private LocalTime gameTime;

    @Override
    public void setId(long id) {
        if (this.id != null) {
            throw new RuntimeException("Player's id can only be set once.");
        }
        this.id = id;
    }
}
