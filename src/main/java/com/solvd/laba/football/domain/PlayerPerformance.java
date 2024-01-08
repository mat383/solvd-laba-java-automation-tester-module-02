package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PlayerPerformance implements Identifiable {
    @Setter(AccessLevel.NONE)
    private Long id;
    private Game game;
    private Double defensivePerformance;
    private Double offensivePerformance;
    private Double cooperativePerformance;
    private LocalTime start;
    private LocalTime end;
    private Position position;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<PenaltyShot> penaltyShots = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<GoalAttempt> goalAttempts = new ArrayList<>();

    public PlayerPerformance(Long id, Game game,
                             Double defensivePerformance, Double offensivePerformance, Double cooperativePerformance,
                             LocalTime start, LocalTime end, Position position) {
        this.id = id;
        this.game = game;
        this.defensivePerformance = defensivePerformance;
        this.offensivePerformance = offensivePerformance;
        this.cooperativePerformance = cooperativePerformance;
        this.start = start;
        this.end = end;
        this.position = position;
    }

    @Override
    public void setId(long id) {
        if (this.id != null) {
            throw new RuntimeException("Player's id can only be set once.");
        }
        this.id = id;
    }


    public List<PenaltyShot> getPenaltyShots() {
        return Collections.unmodifiableList(this.penaltyShots);
    }

    public void addPenaltyShot(@NonNull PenaltyShot penaltyShot) {
        this.penaltyShots.add(penaltyShot);
    }

    public void removePenaltyShot(@NonNull PenaltyShot penaltyShot) {
        this.penaltyShots.remove(penaltyShot);
    }

    public List<GoalAttempt> getGoalAttempts() {
        return Collections.unmodifiableList(this.goalAttempts);
    }

    public void addGoalAttempt(@NonNull GoalAttempt goalAttempt) {
        this.goalAttempts.add(goalAttempt);
    }

    public void removeGoalAttempt(@NonNull GoalAttempt goalAttempt) {
        this.goalAttempts.remove(goalAttempt);
    }
}
