package com.filipzyla.diabeticapp.backend.enums;

public enum SugarType {
    BEFORE_MEAL("before_meal"),
    AFTER_MEAL("after_meal"),
    EMPTY_STOMACH("empty_stomach");

    private final String msg;

    SugarType(String s) {
        msg = s;
    }

    public String getMsg() {
        return msg;
    }
}