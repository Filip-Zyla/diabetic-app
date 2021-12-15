package com.filipzyla.diabeticapp.backend.enums;

public enum MeasurementType {
    INSULIN("Insulin"),
    SUGAR("Sugar");

    private final String msg;

    MeasurementType(String s) {
        msg = s;
    }

    public String getMsg() {
        return msg;
    }
}