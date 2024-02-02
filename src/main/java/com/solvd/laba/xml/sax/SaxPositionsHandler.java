package com.solvd.laba.xml.sax;

import com.solvd.laba.football.domain.Position;
import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SaxPositionsHandler extends DefaultHandler {

    @Getter
    private final List<Position> positions = new ArrayList<>();
    private Position parsedPosition;
    private StringBuilder tagContent = new StringBuilder();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "position":
                this.parsedPosition = new Position();
                this.parsedPosition.setId(Long.parseLong(attributes.getValue("id")));
                break;
            case "name":
                this.tagContent.setLength(0);
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "position":
                this.positions.add(parsedPosition);
                break;
            case "name":
                this.parsedPosition.setName(this.tagContent.toString());
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.tagContent.append(ch, start, length);
    }
}
