package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Player implements Identifiable {
    private Long id;
    // TODO: figure out how to make it final and remove setter
    @Setter
    @NonNull
    private Person person;
    // TODO: add preferred position

    @Override
    public void setId(long id) {
        if (this.id != null) {
            throw new RuntimeException("Player's id can only be set once.");
        }
        this.id = id;
    }
}
