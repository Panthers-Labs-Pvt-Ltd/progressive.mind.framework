package com.progressive.minds.chimera.dataquality.controls;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DataValidityControl {
    // Valid Rules List
    private Set<String> validRules;

    private static class DataValidityControlHolder {
        private static final DataValidityControl INSTANCE = new DataValidityControl();
    }

    private DataValidityControl() {
        validRules = new HashSet<String>();
        validRules.addAll(Arrays.asList(new String[]{"", ""})) ; // Populate this from table using Data_Validity.md (GX)
    }

    public static DataValidityControl getInstance() {
        return DataValidityControlHolder.INSTANCE;
    }

    public boolean isValid(String rule) {
        return validRules.contains(rule);
    }



}
