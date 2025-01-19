package com.progressive.minds.chimera.core.datahub.api;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataProduct {
    private String urn;

    @JsonProperty("ownership")
    private Ownership ownership;

    @JsonProperty("dataProductKey")
    private DataProductKey dataProductKey;

    @JsonProperty("domains")
    private Domains domains;

    @JsonProperty("glossaryTerms")
    private GlossaryTerms glossaryTerms;

    @JsonProperty("globalTags")
    private GlobalTags globalTags;

    // Getters and Setters
    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public Ownership getOwnership() {
        return ownership;
    }

    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
    }

    public DataProductKey getDataProductKey() {
        return dataProductKey;
    }

    public void setDataProductKey(DataProductKey dataProductKey) {
        this.dataProductKey = dataProductKey;
    }

    public Domains getDomains() {
        return domains;
    }

    public void setDomains(Domains domains) {
        this.domains = domains;
    }

    public GlossaryTerms getGlossaryTerms() {
        return glossaryTerms;
    }

    public void setGlossaryTerms(GlossaryTerms glossaryTerms) {
        this.glossaryTerms = glossaryTerms;
    }

    public GlobalTags getGlobalTags() {
        return globalTags;
    }

    public void setGlobalTags(GlobalTags globalTags) {
        this.globalTags = globalTags;
    }
}
