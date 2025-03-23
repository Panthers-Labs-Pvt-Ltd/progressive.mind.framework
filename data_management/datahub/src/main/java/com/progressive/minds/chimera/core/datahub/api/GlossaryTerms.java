package com.progressive.minds.chimera.core.datahub.api;

import java.util.List;

public class GlossaryTerms {
    private GlossaryTermsValue value;

    // Getters and Setters
    public GlossaryTermsValue getValue() {
        return value;
    }

    public void setValue(GlossaryTermsValue value) {
        this.value = value;
    }

    public static class GlossaryTermsValue {
        private AuditStamp auditStamp;
        private List<GlossaryTerm> terms;

        // Getters and Setters
        public AuditStamp getAuditStamp() {
            return auditStamp;
        }

        public void setAuditStamp(AuditStamp auditStamp) {
            this.auditStamp = auditStamp;
        }

        public List<GlossaryTerm> getTerms() {
            return terms;
        }

        public void setTerms(List<GlossaryTerm> terms) {
            this.terms = terms;
        }
    }

    public static class AuditStamp {
        private String actor;
        private long time;

        // Getters and Setters
        public String getActor() {
            return actor;
        }

        public void setActor(String actor) {
            this.actor = actor;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

    public static class GlossaryTerm {
        private String urn;

        // Getters and Setters
        public String getUrn() {
            return urn;
        }

        public void setUrn(String urn) {
            this.urn = urn;
        }
    }
}
