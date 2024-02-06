package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
@Setter
public class PlayerPerformance implements Identifiable {
    @Setter(AccessLevel.NONE)
    private Long id;
    private Game game;
    private Team team;
    private Player player;
    private Position position;
    private Double defensivePerformance;
    private Double offensivePerformance;
    private Double cooperativePerformance;
    private LocalTime start;
    private LocalTime end;
    private List<PenaltyShot> penaltyShotsAsShooter = new ArrayList<>();
    private List<PenaltyShot> penaltyShotsAsGoalkeeper = new ArrayList<>();
    private List<GoalAttempt> goalAttemptsAsAttacker = new ArrayList<>();
    private List<GoalAttempt> goalAttemptsAsDefender = new ArrayList<>();

    public PlayerPerformance() {
        this.game = new Game();
        this.team = new Team();
        this.player = new Player();
        this.position = new Position();
    }

    public PlayerPerformance(Long id, Game game, Team team, Player player, Position position,
                             Double defensivePerformance, Double offensivePerformance, Double cooperativePerformance,
                             LocalTime start, LocalTime end) {
        this.id = id;
        this.game = game;
        this.team = team;
        this.player = player;
        this.position = position;
        this.defensivePerformance = defensivePerformance;
        this.offensivePerformance = offensivePerformance;
        this.cooperativePerformance = cooperativePerformance;
        this.start = start;
        this.end = end;
    }

    @Override
    public void setId(long id) {
        if (this.id != null) {
            throw new RuntimeException("Player's id can only be set once.");
        }
        this.id = id;
    }


    public List<PenaltyShot> getAllPenaltyShots() {
        return Stream.concat(
                        this.penaltyShotsAsGoalkeeper.stream(),
                        this.penaltyShotsAsShooter.stream())
                .toList();
    }

    /**
     * checks this player performance participated in
     * penalty shoot as shooter
     * if penaltyShot is null then it always returns false
     *
     * @param penaltyShot
     * @return
     */
    public boolean hasPenaltyShotAsShooter(PenaltyShot penaltyShot) {
        if (penaltyShot == null) {
            return false;
        }
        return this.penaltyShotsAsShooter.contains(penaltyShot);
    }

    /**
     * checks this player performance participated in
     * penalty shoot as goalkeeper
     * if penaltyShot is null then it always returns false
     *
     * @param penaltyShot
     * @return
     */
    public boolean hasPenaltyShotAsGoalkeeper(PenaltyShot penaltyShot) {
        if (penaltyShot == null) {
            return false;
        }
        return this.penaltyShotsAsGoalkeeper.contains(penaltyShot);
    }

    public void addPenaltyShotAsShooter(@NonNull PenaltyShot penaltyShot) {
        this.penaltyShotsAsShooter.add(penaltyShot);
    }

    public void addPenaltyShotAsGoalkeeper(@NonNull PenaltyShot penaltyShot) {
        this.penaltyShotsAsGoalkeeper.add(penaltyShot);
    }


    /**
     * removes penalty shoot no matter whether it is as shooter or as goalkeeper
     *
     * @param penaltyShot
     */
    public void removePenaltyShot(@NonNull PenaltyShot penaltyShot) {
        this.penaltyShotsAsGoalkeeper.remove(penaltyShot);
        this.penaltyShotsAsShooter.remove(penaltyShot);
    }

    public List<GoalAttempt> getAllGoalAttempts() {
        return Stream.concat(
                        this.goalAttemptsAsAttacker.stream(),
                        this.goalAttemptsAsDefender.stream())
                .toList();
    }

    /**
     * checks this player performance participated in
     * goal attempt as attacker
     * if goalAttempt is null then it always returns false
     *
     * @param goalAttempt
     * @return
     */
    public boolean hasGoalAttemptAsAttacker(GoalAttempt goalAttempt) {
        if (goalAttempt == null) {
            return false;
        }
        return this.goalAttemptsAsAttacker.contains(goalAttempt);
    }

    /**
     * checks this player performance participated in
     * goal attempt as defender
     * if goalAttempt is null then it always returns false
     *
     * @param goalAttempt
     * @return
     */
    public boolean hasGoalAttemptAsDefender(GoalAttempt goalAttempt) {
        if (goalAttempt == null) {
            return false;
        }
        return this.goalAttemptsAsDefender.contains(goalAttempt);
    }

    public void addGoalAttemptAsAttacker(@NonNull GoalAttempt goalAttempt) {
        this.goalAttemptsAsAttacker.add(goalAttempt);
    }

    public void addGoalAttemptAsDefender(@NonNull GoalAttempt goalAttempt) {
        this.goalAttemptsAsDefender.add(goalAttempt);
    }


    /**
     * removes goal attempts no matter whether it is as attacker or as defender
     *
     * @param goalAttempt
     */
    public void removeGoalAttempt(@NonNull GoalAttempt goalAttempt) {
        this.goalAttemptsAsAttacker.remove(goalAttempt);
        this.goalAttemptsAsDefender.remove(goalAttempt);
    }


    public static PlayerPerformanceBuilder builder() {
        return new PlayerPerformanceBuilder();
    }

    public static class PlayerPerformanceBuilder {
        private final PlayerPerformance playerPerformance = new PlayerPerformance();

        public PlayerPerformanceBuilder id(Long id) {
            this.playerPerformance.id = id;
            return this;
        }

        public PlayerPerformanceBuilder player(Player player) {
            this.playerPerformance.player = player;
            return this;
        }

        public PlayerPerformanceBuilder game(Game game) {
            this.playerPerformance.game = game;
            return this;
        }

        public PlayerPerformanceBuilder defensivePerformance(Double defensivePerformance) {
            this.playerPerformance.defensivePerformance = defensivePerformance;
            return this;
        }

        public PlayerPerformanceBuilder offensivePerformance(Double offensivePerformance) {
            this.playerPerformance.offensivePerformance = offensivePerformance;
            return this;
        }

        public PlayerPerformanceBuilder cooperativePerformance(Double cooperativePerformance) {
            this.playerPerformance.cooperativePerformance = cooperativePerformance;
            return this;
        }

        public PlayerPerformanceBuilder start(LocalTime start) {
            this.playerPerformance.start = start;
            return this;
        }

        public PlayerPerformanceBuilder end(LocalTime end) {
            this.playerPerformance.end = end;
            return this;
        }

        public PlayerPerformanceBuilder position(Position position) {
            this.playerPerformance.position = position;
            return this;
        }

        public PlayerPerformanceBuilder team(Team team) {
            this.playerPerformance.team = team;
            return this;
        }

        public PlayerPerformanceBuilder penaltyShootAsShooter(PenaltyShot penaltyShot) {
            this.playerPerformance.penaltyShotsAsShooter.add(penaltyShot);
            return this;
        }

        public PlayerPerformanceBuilder penaltyShootAsGoalkeeper(PenaltyShot penaltyShot) {
            this.playerPerformance.penaltyShotsAsGoalkeeper.add(penaltyShot);
            return this;
        }

        public PlayerPerformanceBuilder goalAttemptAsAttacker(GoalAttempt goalAttempt) {
            this.playerPerformance.goalAttemptsAsAttacker.add(goalAttempt);
            return this;
        }

        public PlayerPerformanceBuilder goalAttemptAsDefender(GoalAttempt goalAttempt) {
            this.playerPerformance.goalAttemptsAsDefender.add(goalAttempt);
            return this;
        }

        public PlayerPerformance build() {
            return this.playerPerformance;
        }

    }
}
