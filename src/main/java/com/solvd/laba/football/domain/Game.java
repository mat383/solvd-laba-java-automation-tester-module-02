package com.solvd.laba.football.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.solvd.laba.football.domain.interfaces.Identifiable;
import com.solvd.laba.json.JacksonDateTimeAdapter;
import com.solvd.laba.json.JacksonPartialDurationAdapter;
import com.solvd.laba.xml.jaxb.JaxbDateTimeAdapter;
import com.solvd.laba.xml.jaxb.JaxbPartialDurationAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
@XmlAccessorType(XmlAccessType.FIELD)
public class Game implements Identifiable {
    @Setter(AccessLevel.NONE)
    @XmlAttribute(name = "id")
    private Long id;
    private String name;
    @XmlJavaTypeAdapter(JaxbDateTimeAdapter.class)
    @JsonDeserialize(using = JacksonDateTimeAdapter.class)
    private LocalDateTime time;
    @XmlJavaTypeAdapter(JaxbPartialDurationAdapter.class)
    @JsonDeserialize(using = JacksonPartialDurationAdapter.class)
    private Duration duration;
    @XmlJavaTypeAdapter(JaxbPartialDurationAdapter.class)
    @JsonDeserialize(using = JacksonPartialDurationAdapter.class)
    private Duration firstHalfDuration;
    @XmlTransient
    @JsonIgnore
    private Team teamA = new Team();
    @Setter(AccessLevel.NONE)
    @XmlTransient
    @JsonIgnore
    private List<PlayerPerformance> playerPerformancesTeamA = new ArrayList<>();
    @XmlTransient
    @JsonIgnore
    private Team teamB = new Team();
    @Setter(AccessLevel.NONE)
    @XmlTransient
    @JsonIgnore
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
     * player performance's game is set to null
     * on successful removal (if this game contained player performance)
     */
    public void removePlayerPerformance(@NonNull PlayerPerformance playerPerformance) {
        if (this.playerPerformancesTeamA.remove(playerPerformance)
                || this.playerPerformancesTeamB.remove(playerPerformance)) {
            playerPerformance.setGame(null);
        }
    }

    /**
     * add player performance to team, based on player performance team
     * player performance team have to mach one of the game teams and cannot be null
     * player performance's game is automatically set to this game
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
