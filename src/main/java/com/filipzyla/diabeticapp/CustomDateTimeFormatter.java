package com.filipzyla.diabeticapp;

import java.time.format.DateTimeFormatter;

public interface CustomDateTimeFormatter {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm");
}
