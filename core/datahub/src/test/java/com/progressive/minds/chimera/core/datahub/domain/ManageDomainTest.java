package com.progressive.minds.chimera.core.datahub.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class ManageDomainTest {
String domainName = "Panthers Labs - Chimera";
    @Test
    void createDomain() throws IOException {

        String content = Files.readString(Paths.get("/home/manish/Chimera2.0/chimera/Product_Documentation.md"));
        String domainDesc = content;
        Map<String, String> customProperties = new HashMap<>();
        customProperties.put("Property1", "Value1");
        customProperties.put("Property2", "Value2");

        ManageDomain manageDomain = new ManageDomain();
        manageDomain.createDomain(domainName,domainDesc, customProperties);
    }

    @Test
    void createDomains() {
        List<ManageDomain.DomainRecords> subdomains = new ArrayList<>();
        ManageDomain.DomainRecords subDomain1 = new ManageDomain.DomainRecords("PantherSubDomain2", "SubDomain Doc", "ParentDomainPanther", null, null, null, null);
        ManageDomain.DomainRecords subDomain = new ManageDomain.DomainRecords("PantherSubDomain1", "SubDomain Doc", "ParentDomainPanther", null, null, null, null);
        subdomains.add(subDomain1);
        subdomains.add(subDomain);
        ManageDomain.DomainRecords mainDomain = new ManageDomain.DomainRecords("ParentDomainPanther", "Main Domain Doc", null, null, null, null, subdomains);
        ManageDomain manageDomain = new ManageDomain();
        manageDomain.createDomains(mainDomain);
    }

    @Test
    void addDomainOwners() {
    }

    @Test
    void addEntitiesToDomain() {
    }
}