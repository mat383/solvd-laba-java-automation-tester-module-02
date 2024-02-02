package com.solvd.laba.xml.jaxb;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Partial Duration adapter for Jaxb, doesn't support full duration class
 * only supports hours, minutes and seconds and only as integers
 */
public class JaxbPartialDurationAdapter extends XmlAdapter<String, Duration> {
    private static final Pattern DURATION_REGEX =
            Pattern.compile("PT((?<hour>\\d+)H)?((?<minute>\\d+)M)?((?<second>\\d+)S)?");

    @Override
    public Duration unmarshal(String s) throws Exception {
        Matcher matcher = DURATION_REGEX.matcher(s);

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
            throw new RuntimeException("wrong duration format: " + s);
        }

    }

    @Override
    public String marshal(Duration duration) throws Exception {
        StringBuilder durationStringBuilder = new StringBuilder("PT");

        if (duration.toHoursPart() > 0) {
            durationStringBuilder.append("H").append(duration.toHoursPart());
        }
        if (duration.toMinutesPart() > 0) {
            durationStringBuilder.append("M").append(duration.toMinutesPart());
        }
        if (duration.toSecondsPart() > 0) {
            durationStringBuilder.append("S").append(duration.toSecondsPart());
        }

        return durationStringBuilder.toString();
    }
}
