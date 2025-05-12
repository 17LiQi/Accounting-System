package com.as.server.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Period {
    DAILY("daily"), WEEKLY("weekly"), MONTHLY("monthly"), YEARLY("yearly");

    private static final Logger log = LoggerFactory.getLogger(Period.class);
    private final String value;

    Period(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Period fromValue(String value) {
        log.debug("Deserializing Period from value: {}", value);
        if (value == null) {
            log.error("Period value is null");
            throw new IllegalArgumentException("Period cannot be null");
        }
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid period value: {}", value, e);
            throw new IllegalArgumentException("Invalid period value: " + value, e);
        }
    }
}