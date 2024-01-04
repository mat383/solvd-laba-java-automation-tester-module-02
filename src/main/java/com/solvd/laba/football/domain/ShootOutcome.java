package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ShootOutcome implements Identifiable {
    private Long id;
    @Setter
    private String name;

    @Override
    public void setId(long id) {
        if (this.id != null) {
            throw new RuntimeException("ShootOutcome id can only be set once.");
        }
        this.id = id;
    }
}
