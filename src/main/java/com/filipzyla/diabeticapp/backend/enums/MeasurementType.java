package com.filipzyla.diabeticapp.backend.enums;

public enum MeasurementType {
    INSULIN("insulin"),
    SUGAR("sugar");

    private final String msg;

    MeasurementType(String s) {
        msg = s;
    }

    public String getMsg() {
        return msg;
    }
}