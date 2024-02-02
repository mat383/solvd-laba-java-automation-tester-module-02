package com.solvd.laba.xml.jaxb;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JaxbDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    private static final Pattern DATETIME_REGEX =
            Pattern.compile("(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d)T(\\d\\d):(\\d\\d):(\\d\\d)");

    @Override
    public LocalDateTime unmarshal(String s) throws Exception {
        Matcher matcher = DATETIME_REGEX.matcher(s);
        if (matcher.matches()) {
            return LocalDateTime.of(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4)),
                    Integer.parseInt(matcher.group(5)),
                    Integer.parseInt(matcher.group(6))
            );
        } else {
            throw new RuntimeException("wrong datetime format: " + s);
        }
    }

    @Override
    public String marshal(LocalDateTime localDateTime) throws Exception {
        return "%4d-%2d-%2dT%2d:%2d:%2d".formatted(
                localDateTime.getYear(),
                localDateTime.getMonthValue(),
                localDateTime.getDayOfMonth(),
                localDateTime.getHour(),
                localDateTime.getMinute(),
                localDateTime.getSecond()
        );
    }
}
