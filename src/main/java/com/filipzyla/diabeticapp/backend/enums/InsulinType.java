package com.filipzyla.diabeticapp.backend.enums;

public enum InsulinType {
    BASE("base"),
    CORRECT("correction"),
    MEAL("food");

    private final String msg;

    InsulinType(String s) {
        msg = s;
    }

    public String getMsg() {
        return msg;
    }
}