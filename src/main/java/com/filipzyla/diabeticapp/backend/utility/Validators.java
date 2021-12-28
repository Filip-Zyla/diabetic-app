package com.filipzyla.diabeticapp.backend.utility;

public class Validators {
    public static final Integer MAX_SUGAR = 500;
    public static final Integer MIN_SUGAR = 30;
    public static final Integer MAX_INSULIN = 50;
    public static final Integer MIN_INSULIN = 1;

    public static boolean validateSugar(Integer sugar) {
        return sugar >= MIN_SUGAR && sugar <= MAX_SUGAR;
    }

    public static boolean validateInsulin(Integer insulin) {
        return insulin >= MIN_INSULIN && insulin <= MAX_INSULIN;
    }
}
