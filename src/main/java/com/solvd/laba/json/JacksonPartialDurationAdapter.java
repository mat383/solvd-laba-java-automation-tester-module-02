package com.solvd.laba.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Partial Duration adapter for Jackson, doesn't support full duration class
 * only supports hours, minutes and seconds and only as integers
 */
public class JacksonPartialDurationAdapter extends JsonDeserializer<Duration> {
    private static final Pattern DURATION_REGEX =
            Pattern.compile("PT((?<hour>\\d+)H)?((?<minute>\\d+)M)?((?<second>\\d+)S)?");

    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String durationString = jsonParser.getValueAsString();
        Matcher matcher = DURATION_REGEX.matcher(durationString);

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
            throw new RuntimeException("wrong duration format: " + durationString);
        }

    }
}
