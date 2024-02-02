package com.solvd.laba.xml.jaxb;

import com.solvd.laba.football.domain.Game;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@XmlRootElement(name = "games")
@XmlAccessorType(XmlAccessType.FIELD)
public class GamesList {
    @XmlElement(name = "game")
    private List<Game> games = new ArrayList<>();
}
