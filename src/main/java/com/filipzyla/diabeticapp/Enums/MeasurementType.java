package com.filipzyla.diabeticapp.Enums;

public enum MeasurementType {
    INSULIN("insulin"),
    SUGAR("sugar");

    private String msg;

    MeasurementType(String s) {
        msg=s;
    }

    public String getMsg() {
        return msg;
    }
}
