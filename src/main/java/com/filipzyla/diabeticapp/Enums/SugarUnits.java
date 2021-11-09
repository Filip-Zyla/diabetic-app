package com.filipzyla.diabeticapp.Enums;

public enum SugarUnits {
    MILLI_GRAM("mg/dl"), MILLI_MOL("mmol/l");

    String msg;

    SugarUnits(String s){
        msg=s;
    }

    public interface UnitsRatio{
        double MOLES_TO_GRAMS = 18;
        double GRAMS_TO_MOLES = 0.056;
    }
}