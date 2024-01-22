package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Team implements Identifiable {
    @EqualsAndHashCode.Include
    private Long id;
    @NonNull
    @Setter
    private String name;
    @NonNull
    @Setter
    private LocalDate creationDate;
    @Setter
    private LocalDate closureDate;

    @Getter(AccessLevel.NONE)
    private List<Player> players = new ArrayList<>();

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


    @Override
    public void setId(long id) {
        if (this.id != null) {
            throw new RuntimeException("Team's id can be set only once.");
        }
        this.id = id;
    }

    public void addPlayer(@NonNull Player player) {
        if (this.players.contains(player)) {
            throw new IllegalArgumentException("Team already have player " + player);
        }
        this.players.add(player);
    }

    public boolean removePlayer(@NonNull Player player) {
        return this.players.remove(player);
    }

    public boolean havePlayer(Player player) {
        return this.players.contains(player);
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }

}
