package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Getter
public class Player implements Identifiable {
    private Long id;
    @Setter
    private Person person;
    @Setter
    private Position preferredPosition;
    @Getter(AccessLevel.NONE)
    private final List<PlayerPerformance> playerPerformances = new ArrayList<>();

    public Player(Long id, Person person, Position preferredPosition) {
        this.id = id;
        this.person = person;
        this.preferredPosition = preferredPosition;
    }

    @Override
    public void setId(long id) {
        if (this.id != null) {
            throw new RuntimeException("Player's id can only be set once.");
        }
        this.id = id;
    }


    public void addPlayerPerformance(PlayerPerformance playerPerformance) {
        this.playerPerformances.add(playerPerformance);
    }

    public void removePlayerPerformance(PlayerPerformance playerPerformance) {
        this.playerPerformances.remove(playerPerformance);
    }

    public List<PlayerPerformance> getPlayerPerformances() {
        return Collections.unmodifiableList(playerPerformances);
    }
}
