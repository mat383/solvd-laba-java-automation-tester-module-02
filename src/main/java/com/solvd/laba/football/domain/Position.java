package com.solvd.laba.football.domain;

import com.solvd.laba.football.domain.interfaces.Identifiable;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Position implements Identifiable {
    @XmlAttribute(name = "id")
    private Long id;
    @Setter
    private String name;


    @Override
    public void setId(long id) {
        if (this.id != null) {
            throw new RuntimeException("Position's id can only be set once.");
        }
        this.id = id;
    }
}
