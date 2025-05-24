package com.as.server.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets period
 */
public enum Period {
    DAILY("daily"),

    WEEKLY("weekly"),

    MONTHLY("monthly"),

    YEARLY("yearly");

    private final String value;

    Period(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Period fromValue(String value) {
        for (Period b : Period.values()) {
            if (b.value.equalsIgnoreCase(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}