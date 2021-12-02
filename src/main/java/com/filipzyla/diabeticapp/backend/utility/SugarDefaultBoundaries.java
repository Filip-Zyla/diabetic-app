package com.filipzyla.diabeticapp.backend.utility;

import com.filipzyla.diabeticapp.backend.enums.SugarUnits;

public interface SugarDefaultBoundaries {
    SugarUnits DEFAULT_UNITS = SugarUnits.MILLI_MOL;
    double DEFAULT_HYPOGLYCEMIA = 70;
    double DEFAULT_HYPERGLYCEMIA = 110;
    double DEFAULT_HYPERGLYCEMIA_AFTER_MEAL = 150;
}