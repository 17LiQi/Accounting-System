package com.as.server.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 账户类型
 */
public enum AccountType {
    CASH("现金"),

    BANK("银行卡"),

    ALIPAY("支付宝"),

    WECHAT("微信"),
    //投资账户
    INVESTMENT("投资账户"),
    //信用卡
    CREDIT_CARD("信用卡");



    private final String value;

    AccountType(String value) {
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
    public static AccountType fromValue(String value) {
        for (AccountType b : AccountType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
