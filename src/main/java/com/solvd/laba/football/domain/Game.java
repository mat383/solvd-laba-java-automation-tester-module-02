package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Game implements Identifiable {
    @Setter(AccessLevel.NONE)
    private Long id;
    private String name;
    private LocalDateTime time;
    private Duration duration;
    private Duration firstHalfDuration;
    private Team teamA;
    @Setter(AccessLevel.NONE)
    private List<Player> playersTeamA = new ArrayList<>();
    private Team teamB;
    @Setter(AccessLevel.NONE)
    private List<Player> playersTeamB = new ArrayList<>();

    public Game(Long id, String name,
                LocalDateTime time, Duration duration, Duration firstHalfDuration,
                Team teamA, Team teamB) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.duration = duration;
        this.firstHalfDuration = firstHalfDuration;
        this.teamA = teamA;
        this.teamB = teamB;
    }

    @Override
    public void setId(long id) {
        if (this.id != null) {
            throw new RuntimeException("Player's id can only be set once.");
        }
        this.id = id;
    }


    public List<Player> getPlayersFromTeamA() {
        return Collections.unmodifiableList(this.playersTeamA);
    }

    public void addPlayerToTeamA(@NonNull Player player) {
        this.playersTeamA.add(player);
    }

    public void removePlayerFromTeamA(@NonNull Player player) {
        this.playersTeamA.remove(player);
    }

    public List<Player> getPlayersFromTeamB() {
        return Collections.unmodifiableList(this.playersTeamB);
    }

    public void addPlayerToTeamB(@NonNull Player player) {
        this.playersTeamB.add(player);
    }

    public void removePlayerFromTeamB(@NonNull Player player) {
        this.playersTeamB.remove(player);
    }
}
