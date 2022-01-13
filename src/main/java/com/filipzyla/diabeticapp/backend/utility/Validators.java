package com.filipzyla.diabeticapp.backend.utility;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;

public class Validators {
    public static final Integer MAX_SUGAR = 500;
    public static final Integer MIN_SUGAR = 30;
    public static final Integer MAX_INSULIN = 50;
    public static final Integer MIN_INSULIN = 1;

    public static final String WRONG_SUGAR_MSG = "Values must be between " + Validators.MIN_SUGAR + " - " + Validators.MAX_SUGAR;
    public static final String WRONG_INSULIN_MSG = "Values must be between " + Validators.MIN_INSULIN + " - " + Validators.MAX_INSULIN;
    public static final String WRONG_EMAIL_MSG = "Not valid email";
    public final static String WRONG_USERNAME_MSG = "Username must contain only letters, numbers and underscore, it's length must be between 3 and 30";
    public final static String WRONG_PASSWORD_MSG = "Password must contain at least one: digit, upper case character, " +
            "lower case character, one special character from !@#$%&*()-+=^ and length must be between 8 and 30";

    private final static Pattern USERNAME_PATTERN = Pattern.compile("[A-Za-z0-9_]{3,30}");
    private final static Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,30}$");

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