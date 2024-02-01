package com.solvd.laba.xml;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlFieldParsers {
    private static final Pattern TIMEDATE_REGEX =
            Pattern.compile("(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d)T(\\d\\d):(\\d\\d):(\\d\\d)");

    private static final Pattern DATE_REGEX =
            Pattern.compile("(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d)");

    private static final Pattern TIME_REGEX =
            Pattern.compile("(\\d\\d):(\\d\\d):(\\d\\d)");

    private static final Pattern DURATION_REGEX =
            Pattern.compile("PT((?<hour>\\d+)H)?((?<minute>\\d+)M)?((?<second>\\d+)S)?");

    public static LocalDateTime parseDateTime(String text) {
        Matcher matcher = TIMEDATE_REGEX.matcher(text);
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
            throw new RuntimeException("wrong datetime format: " + text);
        }
    }

    public static LocalDate parseDate(String text) {
        Matcher matcher = DATE_REGEX.matcher(text);
        if (matcher.matches()) {
            return LocalDate.of(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3))
            );
        } else {
            throw new RuntimeException("wrong date format: " + text);
        }
    }

    public static LocalTime parseTime(String text) {
        Matcher matcher = TIME_REGEX.matcher(text);
        if (matcher.matches()) {
            return LocalTime.of(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3))
            );
        } else {
            throw new RuntimeException("wrong time format: " + text);
        }
    }

    public static Duration parseDuration(String text) {
        Matcher matcher = DURATION_REGEX.matcher(text);

        if (matcher.matches()) {
            Duration duration = Duration.ZERO;
            if (matcher.group("hour") != null) {
                duration = duration.plusHours(Integer.parseInt(matcher.group("hour")));
            }
            if (matcher.group("minute") != null) {
                duration = duration.plusMinutes(Integer.parseInt(matcher.group("minute")));
            }
            if (matcher.group("second") != null) {
                duration = duration.plusSeconds(Integer.parseInt(matcher.group("second")));
            }
            return duration;
        } else {
            throw new RuntimeException("wrong duration format: " + text);
        }

    }
}
