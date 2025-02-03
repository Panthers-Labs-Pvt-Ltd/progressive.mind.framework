package com.progressive.minds.chimera.core.datahub.modal;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Set;

@Getter
@Setter
public class Owners {

    // Valid owners set as a constant
    private static final Set<String> VALID_OWNERS = Set.of(
            "CUSTOM", "TECHNICAL_OWNER", "BUSINESS_OWNER", "DATA_STEWARD",
            "NONE", "DEVELOPER", "DATAOWNER", "PRODUCER",
            "DELEGATE", "CONSUMER", "STAKEHOLDER", "$UNKNOWN"
    );

    // Get the name of the owner
    @Getter
    @NotNull
    private String name;

    public Owners(){}

    public Owners(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @NotNull
    private String type;

    // Get the type of the owner, with validation logic
    public String getType() {
        // Check if type is null or empty before proceeding
        if (type == null || type.trim().isEmpty()) {
            return "urn:li:ownershipType:__system__none";
        }

        return VALID_OWNERS.contains(type.toUpperCase(Locale.ROOT)) ?
                "urn:li:ownershipType:__system__" + type.toLowerCase(Locale.ROOT) :
                "urn:li:ownershipType:__system__none";

    }
}


