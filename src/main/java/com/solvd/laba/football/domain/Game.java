package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    private Team teamA = new Team();
    @Setter(AccessLevel.NONE)
    private List<PlayerPerformance> playerPerformancesTeamA = new ArrayList<>();
    private Team teamB = new Team();
    @Setter(AccessLevel.NONE)
    private List<PlayerPerformance> playerPerformancesTeamB = new ArrayList<>();

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


    /**
     * removes playerPerformance from a team he is in
     *
     * @param playerPerformance
     */
    public void removePlayerPerformance(@NonNull PlayerPerformance playerPerformance) {
        this.playerPerformancesTeamA.remove(playerPerformance);
        this.playerPerformancesTeamB.remove(playerPerformance);
    }

    /**
     * add player performance to team, based on player performance team
     * player performance team have to mach one of the game teams and cannot be null
     * player's game is automatically set to this game
     *
     * @param playerPerformance
     */
    public void addPlayerPerformance(@NonNull PlayerPerformance playerPerformance) {
        if (playerPerformance.getTeam() == null) {
            throw new IllegalArgumentException("Player performance team cannot be null");
        }

        if (playerPerformance.getTeam().equals(this.teamA)) {
            this.playerPerformancesTeamA.add(playerPerformance);
            playerPerformance.setGame(this);
        } else if (playerPerformance.getTeam().equals(this.teamB)) {
            this.playerPerformancesTeamB.add(playerPerformance);
            playerPerformance.setGame(this);
        } else {
            throw new IllegalArgumentException("Player performance team doesn't match any of the game teams");
        }
    }

    public List<PlayerPerformance> getPlayerPerformancesFromTeamA() {
        return Collections.unmodifiableList(this.playerPerformancesTeamA);
    }

    /**
     * adds player performance to team A
     * and sets player performance's game to this game
     *
     * @param playerPerformance
     */
    public void addPlayerPerformanceToTeamA(@NonNull PlayerPerformance playerPerformance) {
        this.playerPerformancesTeamA.add(playerPerformance);
        playerPerformance.setGame(this);
    }


    public List<PlayerPerformance> getPlayerPerformancesFromTeamB() {
        return Collections.unmodifiableList(this.playerPerformancesTeamB);
    }

    /**
     * adds player performance to team B
     * and sets player performance's game to this game
     *
     * @param playerPerformance
     */
    public void addPlayerPerformanceToTeamB(@NonNull PlayerPerformance playerPerformance) {
        this.playerPerformancesTeamB.add(playerPerformance);
        playerPerformance.setGame(this);
    }


    public Optional<PlayerPerformance> findGoalkeeperPerformance(PenaltyShot penaltyShot) {
        return Stream.concat(
                        this.playerPerformancesTeamA.stream(),
                        this.playerPerformancesTeamB.stream())
                .filter((playerPerformance -> playerPerformance.hasPenaltyShotAsGoalkeeper(penaltyShot)))
                .findAny();
    }

    public Optional<PlayerPerformance> findShooterPerformance(PenaltyShot penaltyShot) {
        return Stream.concat(
                        this.playerPerformancesTeamA.stream(),
                        this.playerPerformancesTeamB.stream())
                .filter((playerPerformance -> playerPerformance.hasPenaltyShotAsShooter(penaltyShot)))
                .findAny();
    }

    public Optional<PlayerPerformance> findAttackerPerformance(GoalAttempt goalAttempt) {
        return Stream.concat(
                        this.playerPerformancesTeamA.stream(),
                        this.playerPerformancesTeamB.stream())
                .filter((playerPerformance -> playerPerformance.hasGoalAttemptAsAttacker(goalAttempt)))
                .findAny();
    }

    public Optional<PlayerPerformance> findDefenderPerformance(GoalAttempt goalAttempt) {
        return Stream.concat(
                        this.playerPerformancesTeamA.stream(),
                        this.playerPerformancesTeamB.stream())
                .filter((playerPerformance -> playerPerformance.hasGoalAttemptAsDefender(goalAttempt)))
                .findAny();
    }
}
