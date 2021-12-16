package com.filipzyla.diabeticapp.backend.enums;

public enum SugarUnits {
    MILLI_GRAM("mg/dl");

    private final String msg;

    SugarUnits(String s) {
        msg = s;
    }

    public String getMsg() {
        return msg;
    }

}