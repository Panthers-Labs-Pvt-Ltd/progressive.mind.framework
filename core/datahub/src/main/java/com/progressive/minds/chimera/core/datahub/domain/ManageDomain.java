package com.progressive.minds.chimera.core.datahub.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.common.*;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.ByteString;
import com.linkedin.data.DataMap;
import com.linkedin.data.template.StringMap;
import com.linkedin.domain.DomainProperties;
import com.linkedin.events.metadata.ChangeType;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.linkedin.events.metadata.ChangeType.UPSERT;
import static com.progressive.minds.chimera.core.datahub.DataHubUtils.SYSTEM_USER;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;

public class ManageDomain  {
    private static final ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageDomain.class);

    public record DomainRecords(
            @NotNull String name,
            @NotNull String documentation,
            @Null String parentDomain,
            @Null Map<String, String> domainOwners,
            @Null String[] assets,
            @Null Map<String, String> customProperties,
            @Null List<DomainRecords> domainHierarchy) {
    }

    String LoggerTag = "[DataHub- Manage Domain] -";

    // Method to create a domain
    public String createDomain(String domainName, String domainDocumentation, Map<String, String> customProperties) {
        try {
            Urn domainUrn = Urn.createFromString("urn:li:domain:" +
                    replaceSpecialCharsAndLowercase(domainName));

            AuditStamp createdStamp = new AuditStamp()
                    .setActor(new CorpuserUrn(SYSTEM_USER))
                    .setTime(Instant.now().toEpochMilli());

            StringMap MapCustomProperties = new StringMap();
            MapCustomProperties.putAll(customProperties);

            DomainProperties domainProperties = new DomainProperties()
                    .setName(domainName)
                    .setCreated(createdStamp)
                    .setCustomProperties(MapCustomProperties)
                    .setDescription(domainDocumentation);

            // Convert the aspect DataMap to JSON and then to ByteString
            DataMap dataMap = domainProperties.data();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(dataMap);
            ByteString byteString = ByteString.copyAvroString(jsonString, true);

            // Create GenericAspect
            GenericAspect genericAspect = new GenericAspect();
            genericAspect.setValue(byteString);
            genericAspect.setContentType("application/json");

            MetadataChangeProposal proposal = new MetadataChangeProposal();
            proposal.setEntityUrn(domainUrn);
            proposal.setEntityType("domain");
            proposal.setAspectName("domainProperties");
            proposal.setAspect(genericAspect);
            proposal.setChangeType(ChangeType.UPSERT);

            return emitProposal(proposal, "domain");

        } catch (URISyntaxException | JsonProcessingException | ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to create domain: " + domainName, e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public String createDomains(DomainRecords domainRecords) {
        try {

            AuditStamp createdStamp = new AuditStamp()
                    .setActor(new CorpuserUrn(SYSTEM_USER))
                    .setTime(Instant.now().toEpochMilli());

            Urn domainUrn = Urn.createFromString("urn:li:domain:" +
                    replaceSpecialCharsAndLowercase(domainRecords.name));

            DomainProperties domainProperties = new DomainProperties()
                    .setName(domainRecords.name)
                    .setCreated(createdStamp)
                    .setDescription(domainRecords.documentation);

             if (domainRecords.customProperties != null && !domainRecords.customProperties.isEmpty())
             {
                 StringMap MapCustomProperties = new StringMap();
                 MapCustomProperties.putAll(domainRecords.customProperties);
                 domainProperties.setCustomProperties(MapCustomProperties);
             }

            if (domainRecords.parentDomain != null && !domainRecords.parentDomain.isEmpty())
            {
                Urn parentDomainUrn = Urn.createFromString("urn:li:domain:" +
                        replaceSpecialCharsAndLowercase(domainRecords.name));
                        domainProperties.setParentDomain(parentDomainUrn);
            }


                MetadataChangeProposal proposal = createProposal(String.valueOf(domainUrn), "domain",
                    "domainProperties", "UPSERT", domainProperties);

            DatahubLogger.logInfo(LoggerTag + "Preparing for MetadataChangeProposal : " + proposal);

            String retVal = emitProposal(proposal, "domain");

            if (domainRecords.domainHierarchy != null && !domainRecords.domainHierarchy.isEmpty())
            {
                for (DomainRecords subdomain : domainRecords.domainHierarchy) {
                    createDomains(subdomain);
                }
            }

                return retVal;
            } catch (URISyntaxException | JsonProcessingException | ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to create domain: ", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to create a parent domain
    public String createParentDomain(String domainName, String domainDocumentation, String parentDomainName,
                                     Map<String, String> customProperties) {
        try {
            Urn domainUrn = Urn.createFromString("urn:li:domain:" +
                    replaceSpecialCharsAndLowercase(domainName));
            Urn parentDomainUrn = Urn.createFromString("urn:li:domain:" +
                    replaceSpecialCharsAndLowercase(parentDomainName));

            AuditStamp createdStamp = new AuditStamp()
                    .setActor(new CorpuserUrn(SYSTEM_USER))
                    .setTime(Instant.now().toEpochMilli());

            StringMap MapCustomProperties = new StringMap();
            MapCustomProperties.putAll(customProperties);

            DomainProperties domainProperties = new DomainProperties()
                    .setName(domainName)
                    .setCreated(createdStamp)
                    .setCustomProperties(MapCustomProperties)
                    .setParentDomain(parentDomainUrn)
                    .setDescription(domainDocumentation);

            // Convert the aspect DataMap to JSON and then to ByteString
            DataMap dataMap = domainProperties.data();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(dataMap);
            ByteString byteString = ByteString.copyAvroString(jsonString, true);

            // Create GenericAspect
            GenericAspect genericAspect = new GenericAspect();
            genericAspect.setValue(byteString);
            genericAspect.setContentType("application/json");

            MetadataChangeProposal proposal = new MetadataChangeProposal();
            proposal.setEntityUrn(domainUrn);
            proposal.setEntityType("domain");
            proposal.setAspectName("domainProperties");
            proposal.setAspect(genericAspect);
            proposal.setChangeType(ChangeType.UPSERT);

            return emitProposal(proposal, "domain");

        } catch (URISyntaxException | JsonProcessingException | ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to create domain with parent: " + domainName, e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to add domain owners
    public String addDomainOwners(String domainName, Map<String, String> domainOwners)
            throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        // Create an OwnerArray to store the owners
        OwnerArray ownerArray = new OwnerArray();
        Urn domainUrn = Urn.createFromString("urn:li:domain:" +
                replaceSpecialCharsAndLowercase(domainName));

        // Iterate over the domainOwners map and create Owner objects
        domainOwners.forEach((ownerName, ownershipType) -> {
            Owner owner = new Owner()
                    .setOwner(new CorpuserUrn(ownerName))
                    .setType(OwnershipType.valueOf(ownershipType)); // Using OwnershipType Enum
            ownerArray.add(owner);
        });

        Ownership ownership = new Ownership()
                .setOwners(ownerArray);

        DataMap dataMap = ownership.data(); // Convert to DataMap

        // Serialize DataMap to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(dataMap);

        // Convert JSON string to ByteString
        ByteString byteString = ByteString.unsafeWrap(jsonString.getBytes(StandardCharsets.UTF_8));

        GenericAspect genericAspect = new GenericAspect();
        genericAspect.setValue(byteString); // Use ByteString
        genericAspect.setContentType("application/json");

        // Create MetadataChangeProposal
        MetadataChangeProposal proposal = new MetadataChangeProposal();
        proposal.setEntityUrn(domainUrn); // Set the domain URN
        proposal.setEntityType("domain");
        proposal.setAspectName("ownership");
        proposal.setAspect(genericAspect);
        proposal.setChangeType(UPSERT);

        return emitProposal(proposal, "ownership");
    }

    public String addEntitiesToDomain(Urn assetUrn, Urn domainUrn) throws Exception {

        // Serialize DomainProperties to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(domainUrn.toString());

        // Convert JSON string to ByteString
        ByteString byteString = ByteString.unsafeWrap(jsonString.getBytes(StandardCharsets.UTF_8));

        // Create a GenericAspect
        GenericAspect genericAspect = new GenericAspect();
        genericAspect.setValue(byteString);
        genericAspect.setContentType("application/json");

        // Create MetadataChangeProposal
        MetadataChangeProposal proposal = new MetadataChangeProposal();
        proposal.setEntityUrn(assetUrn); // Set the asset URN
        proposal.setAspectName("domain"); // Aspect name
        proposal.setAspect(genericAspect); // Set the aspect
        proposal.setChangeType(ChangeType.UPSERT); // Change type

        // Emit the proposal using the RestEmitter (assume initialized globally)
        return emitProposal(proposal, "domain");

    }
}
