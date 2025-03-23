package com.progressive.minds.chimera.core.datahub.api;

import java.util.List;

public class Domains {
    private DomainsValue value;

    // Getters and Setters
    public DomainsValue getValue() {
        return value;
    }

    public void setValue(DomainsValue value) {
        this.value = value;
    }

    public static class DomainsValue {
        private List<String> domains;

        // Getters and Setters
        public List<String> getDomains() {
            return domains;
        }

        public void setDomains(List<String> domains) {
            this.domains = domains;
        }
    }
}
