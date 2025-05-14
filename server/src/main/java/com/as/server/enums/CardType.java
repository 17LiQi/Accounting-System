package com.as.server.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets cardType
 */
public enum CardType {
    SAVINGS("SAVINGS"),

    DEBIT("DEBIT"),

    CREDIT("CREDIT"),

    VISA("VISA"),

    MASTERCARD("MASTERCARD"),

    WALLET("WALLET");

    private final String value;

    CardType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static CardType fromValue(String value) {
        for (CardType b : CardType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
