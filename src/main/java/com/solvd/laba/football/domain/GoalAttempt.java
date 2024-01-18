package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GoalAttempt implements Identifiable {
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    private Long id;
    private ShootOutcome outcome;
    private LocalTime gameTime;

    @Override
    public void setId(long id) {
        if (this.id != null) {
            throw new RuntimeException("Player's id can only be set once.");
        }
        this.id = id;
    }
}
