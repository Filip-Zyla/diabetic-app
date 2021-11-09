package com.filipzyla.diabeticapp.Enums;

public enum InsulinType {
    BASE("base"),
    CORRECT("correct"),
    MEAL("meal");

    private String msg;

    InsulinType(String s){
        msg=s;
    }

    public String getMsg() {
        return msg;
    }
}
