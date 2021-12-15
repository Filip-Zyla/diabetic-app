package com.filipzyla.diabeticapp.backend.enums;

public enum SugarUnits {
    MILLI_GRAM("mg/dl", 0.056),
    MILLI_MOL("mmol/l", 18);

    /*
    MOLES_TO_GRAMS = 18
    GRAMS_TO_MOLES = 0.056
     */

    private final String msg;
    private final double conversion;

    SugarUnits(String s, double c) {
        msg = s;
        conversion = c;
    }

    public String getMsg() {
        return msg;
    }

    public double getConversion() {
        return conversion;
    }
}