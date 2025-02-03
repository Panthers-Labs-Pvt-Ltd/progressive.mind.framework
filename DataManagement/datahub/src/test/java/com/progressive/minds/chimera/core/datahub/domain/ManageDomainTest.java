package com.progressive.minds.chimera.core.datahub.domain;

import com.ibm.icu.impl.data.ResourceReader;
import io.datahubproject.openapi.generated.OwnershipType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;


class ManageDomainTest {
String domainName = "Panthers Labs - Chimera";
    @Test
    void createDomain() throws IOException {

        //String content = Files.readString(Paths.get("/home/manish/Chimera2.0/chimera/Product_Documentation.md"));
        String content = new String(Objects.requireNonNull(ResourceReader.class.getClassLoader()
                .getResourceAsStream("Sample.md")).readAllBytes(), StandardCharsets.UTF_8);

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
        List<ManageDomain.DomainRecords> childdomains = new ArrayList<>();

        //Sub Child
        ManageDomain.DomainRecords subChild1 = new ManageDomain.DomainRecords("OBDEF Domain", "SubDomain Doc", "Data & Analytics Domain", null, null, null, null);
        ManageDomain.DomainRecords subChild2 = new ManageDomain.DomainRecords("TACHYON Domain", "SubDomain Doc", "Data & Analytics Domain", null, null, null, null);
        //Child Domain
        ManageDomain.DomainRecords subDomain1 = new ManageDomain.DomainRecords("Data & Analytics Domain", "SubDomain Doc", "Natwest Domain", null, null, null, childdomains);
        ManageDomain.DomainRecords subDomain2 = new ManageDomain.DomainRecords("Wealth Domain", "SubDomain Doc", "Natwest Domain", null, null, null, null);

        ManageDomain.DomainRecords mainDomain = new ManageDomain.DomainRecords("Natwest Domain", "Natwest Doc",
                null, null, null, null, subdomains);

        subdomains.add(subDomain1);
        subdomains.add(subDomain2);
        childdomains.add(subChild1);
        childdomains.add(subChild2);

        ManageDomain manageDomain = new ManageDomain();
        manageDomain.createDomains(mainDomain);
    }

    @Test
    void addDomainOwners() {
        Map<String, String> ownersMap = new HashMap<>();
        ownersMap.put("BUSINESS_OWNER", OwnershipType.BUSINESS_OWNER.toString());
        ownersMap.put("Abhinav Kumar", OwnershipType.BUSINESS_OWNER.toString());
        ownersMap.put("Manish Kumar", OwnershipType.TECHNICAL_OWNER.toString());
        ownersMap.put("BUSINESS_OWNER", OwnershipType.BUSINESS_OWNER.toString());
        ownersMap.put("DATA_STEWARD", OwnershipType.DATA_STEWARD.toString());
        ownersMap.put("DEVELOPER", OwnershipType.DEVELOPER.toString());
        ownersMap.put("DATAOWNER", OwnershipType.DATAOWNER.toString());
        ownersMap.put("DELEGATE", OwnershipType.DELEGATE.toString());
        ownersMap.put("PRODUCER", OwnershipType.PRODUCER.toString());
        ownersMap.put("CUSTOM", OwnershipType.CUSTOM.toString());
        ownersMap.put("CONSUMER", OwnershipType.CONSUMER.toString());
        ownersMap.put("STAKEHOLDER", OwnershipType.STAKEHOLDER.toString());
        ownersMap.put("manish.kumar.gupta@outlook.com", OwnershipType.STAKEHOLDER.toString());


        ManageDomain manageDomain = new ManageDomain();
        try {
            manageDomain.addDomainOwners("Data & Analytics Domain", ownersMap);
        } catch (URISyntaxException|IOException|ExecutionException|InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
 }
