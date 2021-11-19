package com.filipzyla.diabeticapp.enums;

public enum InsulinType {
    BASE("Base"),
    CORRECT("Correction"),
    MEAL("Food");

    private String msg;

    InsulinType(String s) {
        msg = s;
    }

    public String getMsg() {
        return msg;
    }
}
