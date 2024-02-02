package com.solvd.laba.xml.jaxb;

import com.solvd.laba.football.domain.Position;
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
@XmlRootElement(name = "positions")
@XmlAccessorType(XmlAccessType.FIELD)
public class PositionsList {
    @XmlElement(name = "position")
    private List<Position> positions = new ArrayList<>();
}
