package com.progressive.minds.chimera.core.datahub.common;

import com.linkedin.common.*;
import com.linkedin.mxe.MetadataChangeProposal;
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
                            .setTypeUrn(Urn.createFromString("urn:li:ownershipType:__system__business_owner"))
                            .setType(OwnershipType.CUSTOM)
                    ;
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                ownerArray.add(owner);
            });
        AuditStamp createdStamp = new AuditStamp()
                .setActor(new CorpuserUrn("manish.kumar.gupta@outlook.com"))
                .setTime(Instant.now().toEpochMilli());


        Ownership ownership = new Ownership()
                .setOwners(ownerArray).setLastModified(createdStamp);

        MetadataChangeProposal proposal = createProposal(String.valueOf(entityUrn), entityType,
                aspectName, changeType,ownership);

        String retVal= emitProposal(proposal, "ownership");
        System.out.println("OWNE" + retVal);
        return  retVal;
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
                        .setOwner(new CorpuserUrn(ownerRec.name))
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
                .setActor(new CorpuserUrn("manish.kumar.gupta@outlook.com"))
                .setTime(Instant.now().toEpochMilli());


        Ownership ownership = new Ownership()
                .setOwners(ownerArray).setLastModified(createdStamp);

        MetadataChangeProposal proposal = createProposal(String.valueOf(entityUrn), entityType,
                aspectName, changeType,ownership);

        String retVal= emitProposal(proposal, "ownership");
        System.out.println("OWNE" + retVal);
        return  retVal;
    }

}
