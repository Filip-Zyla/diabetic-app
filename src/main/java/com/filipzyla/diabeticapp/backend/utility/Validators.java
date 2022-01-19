package com.filipzyla.diabeticapp.backend.utility;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;

public class Validators {
    public static final Integer MAX_SUGAR = 500;
    public static final Integer MIN_SUGAR = 30;
    public static final Integer MAX_INSULIN = 50;
    public static final Integer MIN_INSULIN = 1;

    public static final String SUGAR_RANGE = " " + Validators.MIN_SUGAR + " - " + Validators.MAX_SUGAR;
    public static final String INSULIN_RANGE = " " + Validators.MIN_INSULIN + " - " + Validators.MAX_INSULIN;

    private final static Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9]{3,30}");
    private final static Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,30}$");

    public static boolean validateSugar(Integer sugar) {
        return sugar >= MIN_SUGAR && sugar <= MAX_SUGAR;
    }

    public static boolean validateInsulin(Integer insulin) {
        return insulin >= MIN_INSULIN && insulin <= MAX_INSULIN;
    }

    public static boolean validateEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static boolean validateUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean validatePassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}