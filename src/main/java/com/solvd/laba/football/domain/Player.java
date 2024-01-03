package com.solvd.laba.football.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Player {
    private final long id;
    // TODO: figure out how to make it final and remove setter
    @Setter
    @NonNull
    private Person person;
    // TODO: add preferred position
}
