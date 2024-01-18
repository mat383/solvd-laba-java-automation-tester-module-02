package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class Person implements Identifiable {
    private Long id;
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private LocalDate birthDate;

    public Person(Long id, String firstName, String lastName, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public Person(String firstName, String lastName, LocalDate birthDate) {
        this(null, firstName, lastName, birthDate);
    }


    @Override
    public void setId(long id) {
        if (this.id != null) {
            throw new RuntimeException("Person's id can only be set once.");
        }
        this.id = id;
    }
}
