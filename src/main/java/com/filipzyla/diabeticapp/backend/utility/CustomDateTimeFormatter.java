package com.filipzyla.diabeticapp.backend.utility;

import java.time.format.DateTimeFormatter;

public interface CustomDateTimeFormatter {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm");
}