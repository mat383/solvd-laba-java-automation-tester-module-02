package com.solvd.laba.football.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Getter
public class Team {
    private final long id;
    @NonNull
    @Setter
    private String name;
    @NonNull
    @Setter
    private LocalDate creationDate;
    @Setter
    private LocalDate closureDate;

    @Getter(AccessLevel.NONE)
    private Set<Player> players;
    //private List<SupportStaff> supportStaff;

    public Team(long id, String name, LocalDate creationDate, LocalDate closureDate) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name cannot be blank nor null");
        }
        if (creationDate == null) {
            throw new IllegalArgumentException("creationDate cannot be null");
        }

        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.closureDate = closureDate;
    }

    public Team(long id, String name, LocalDate creationDate) {
        this(id, name, creationDate, null);
    }

    public void addPlayer(@NonNull Player player) {
        this.players.add(player);
    }

    public boolean removePlayer(@NonNull Player player) {
        return this.players.remove(player);
    }

    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

}
