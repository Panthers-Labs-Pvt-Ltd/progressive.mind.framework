package com.progressive.minds.chimera.core.datahubutils.domain;

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
import com.progressive.minds.chimera.core.datahubutils.DataHubUtils;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import static com.linkedin.events.metadata.ChangeType.UPSERT;
import static com.progressive.minds.chimera.core.datahubutils.common.genericUtils.emitProposal;
import static com.progressive.minds.chimera.core.datahubutils.common.genericUtils.replaceSpecialCharsAndLowercase;

public class ManageDomain implements DataHubUtils {
    private static final ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageDomain.class);

    String LoggerTag = "[DataHub- Manage Domain] -";
    //DatahubLogger.logInfo("Setting Console As Open Lineage Transport Type");
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

        } catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException("Failed to create domain: " + domainName, e);
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

        } catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException("Failed to create domain with parent: " + domainName, e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to add domain owners
    public String addDomainOwners(String domainName, Map<String, String> domainOwners)
            throws URISyntaxException, IOException {
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
