package com.progressive.minds.chimera.core.datahub.modal;

import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Set;

public class Owners {

    // Valid owners set as a constant
    private static final Set<String> VALID_OWNERS = Set.of(
            "CUSTOM", "TECHNICAL_OWNER", "BUSINESS_OWNER", "DATA_STEWARD",
            "NONE", "DEVELOPER", "DATAOWNER", "PRODUCER",
            "DELEGATE", "CONSUMER", "STAKEHOLDER", "$UNKNOWN"
    );

    @NotNull
    public String name;

    @NotNull
    public String type;

    public String getName() {
        return name;
    }

    public String getType() {
        // Check if type is null or empty before proceeding
        if (type == null || type.trim().isEmpty()) {
            return "urn:li:ownershipType:__system__none";
        }

        String result = VALID_OWNERS.contains(type.toUpperCase(Locale.ROOT)) ?
                "urn:li:ownershipType:__system__" + type.toLowerCase(Locale.ROOT) :
                "urn:li:ownershipType:__system__none";

        return result;
    }
}
