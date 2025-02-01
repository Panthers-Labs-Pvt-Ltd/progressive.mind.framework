package com.progressive.minds.chimera.core.datahub.ownership;

import com.linkedin.common.*;
import com.linkedin.common.urn.UrnUtils;
import com.linkedin.events.metadata.ChangeType;
import com.linkedin.metadata.aspect.patch.builder.OwnershipPatchBuilder;
import com.linkedin.metadata.aspect.patch.builder.StructuredPropertiesPatchBuilder;
import com.linkedin.mxe.MetadataChangeProposal;
import com.progressive.minds.chimera.core.datahub.modal.Field;
import com.progressive.minds.chimera.core.datahub.modal.Owners;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.Urn;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import com.linkedin.common.Ownership;
import com.linkedin.common.Owner;
import com.linkedin.common.OwnershipSource;

import static com.linkedin.common.OwnershipSourceType.MANUAL;
import static com.progressive.minds.chimera.core.datahub.Constants.*;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.createProposal;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;

public class ManageOwners {
    public static String addOwners(Urn entityUrn, String entityType, String aspectName, String changeType,
                                   Map<String, String> ownersInfo) throws IOException, ExecutionException, InterruptedException {
        ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageOwners.class);

        OwnerArray ownerArray = new OwnerArray();
        DatahubLogger.logInfo("Adding Owners.......");
        ownersInfo.forEach((ownerName, ownershipType) -> {
            Owner owner;
            try {
                owner = new Owner()
                        .setOwner(new CorpuserUrn(ownerName))
                        .setSource(new OwnershipSource().setType(MANUAL))
                        .setTypeUrn(Urn.createFromString(String.valueOf(DEFAULT_OWNERSHIP_TYPE_URN)))
                        .setType(OwnershipType.CUSTOM)
                ;
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            ownerArray.add(owner);
        });
        AuditStamp createdStamp = new AuditStamp()
                .setActor(new CorpuserUrn(DATAHUB_ACTOR))
                .setTime(Instant.now().toEpochMilli());


        Ownership ownership = new Ownership()
                .setOwners(ownerArray).setLastModified(createdStamp);

        MetadataChangeProposal proposal = createProposal(String.valueOf(entityUrn), entityType,
                OWNERSHIP_ASPECT_NAME, changeType, ownership);

        return emitProposal(proposal, entityType);
    }

    public static String addOwners(Urn entityUrn, String entityType, String aspectName, String changeType,
                                   List<Owners> ownersInfo) throws IOException, ExecutionException, InterruptedException {
        ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(ManageOwners.class);
        OwnerArray ownerArray = new OwnerArray();
        DatahubLogger.logInfo("Adding Owners.......");
        ownersInfo.forEach(ownerRec -> {

            Owner owner;
            try {
                owner = new Owner()
                        .setOwner(new CorpuserUrn(ownerRec.getName()))
                        .setSource(new OwnershipSource().setType(MANUAL))
                        .setTypeUrn(Urn.createFromString(ownerRec.getType()))
                        .setType(OwnershipType.CUSTOM)
                ;
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            ownerArray.add(owner);
        });
        AuditStamp createdStamp = new AuditStamp()
                .setActor(new CorpuserUrn(DATAHUB_ACTOR))
                .setTime(Instant.now().toEpochMilli());


        Ownership ownership = new Ownership()
                .setOwners(ownerArray).setLastModified(createdStamp);

        MetadataChangeProposal proposal = createProposal(String.valueOf(entityUrn), entityType,
                OWNERSHIP_ASPECT_NAME, changeType, ownership);

        return emitProposal(proposal, entityType);

    }

    public static void modifyOwners(String changeType, String entityUrn, String entityType, List<Owners> ownersList)
            throws IOException, ExecutionException, InterruptedException, URISyntaxException {
        MetadataChangeProposal ownershipPatchBuilder = new OwnershipPatchBuilder()
                .urn(UrnUtils.getUrn("urn:li:dataset:(urn:li:dataPlatform:hive,fct_users_created,PROD)"))
                .addOwner(new CorpuserUrn("urn:li:corpuser:manish.kumar@natwest.com"), OwnershipType.CUSTOM).build();

/*        for (Owners owner : ownersList) {
            if (changeType.equalsIgnoreCase("ADD")) {
                ownershipPatchBuilder.addOwner(new CorpuserUrn(owner.getName()), OwnershipType.CUSTOM);
            } else {
                ownershipPatchBuilder.removeOwner(new CorpuserUrn(owner.getName()));
            }*/

            MetadataChangeProposal proposal = createProposal(entityUrn, entityType,
                    OWNERSHIP_ASPECT_NAME, "PATCH", ownershipPatchBuilder);
             emitProposal(proposal, entityType);

      //  }
    }
}
