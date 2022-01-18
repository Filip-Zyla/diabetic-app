package com.filipzyla.diabeticapp.backend.enums;

public enum SugarType {
    BEFORE_MEAL("Before meal"),
    AFTER_MEAL("After meal"),
    EMPTY_STOMACH("On empty stomach");

    private String msg;

    SugarType(String s) {
        msg = s;
    }

    public String getMsg() {
        return msg;
    }
}