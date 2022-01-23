package com.filipzyla.diabeticapp.backend.utility;

import com.filipzyla.diabeticapp.backend.enums.SugarUnits;

public interface SugarDefaultSettings {
    SugarUnits DEFAULT_UNITS = SugarUnits.MILLI_GRAM;
    Integer DEFAULT_HYPOGLYCEMIA = 70;
    Integer DEFAULT_HYPERGLYCEMIA = 110;
    Integer DEFAULT_HYPERGLYCEMIA_AFTER_MEAL = 150;
}