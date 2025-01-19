package com.progressive.minds.chimera.core.datahub.common;

import com.linkedin.common.*;
import com.linkedin.data.ByteString;
import com.linkedin.data.DataMap;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import com.progressive.minds.chimera.core.datahub.SharedLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import datahub.shaded.jackson.databind.ObjectMapper;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.Urn;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import com.linkedin.common.Ownership;
import com.linkedin.common.Owner;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.CorpGroupUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.common.OwnershipSource;

import java.util.Collections;
import static com.linkedin.common.OwnershipSourceType.MANUAL;
import static com.progressive.minds.chimera.core.datahub.DataHubUtils.SYSTEM_USER;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.createProposal;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;

public class ManageOwners {
    public static String addOwners(Urn entityUrn, String entityType, String aspectName, String changeType,
                                   Map<String, String> ownersInfo) throws IOException, ExecutionException, InterruptedException {
        ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(SharedLogger.class);

        OwnerArray ownerArray = new OwnerArray();
        DatahubLogger.logInfo("Adding Owners.......");
        // Iterate over the domainOwners map and create Owner objects
        ownersInfo.forEach((ownerName, ownershipType) -> {
            DatahubLogger.logInfo("Adding Owners......." + ownerName + " and " + ownershipType);
            Owner owner;
            try {
                owner = new Owner()
                        .setOwner(new CorpuserUrn(ownerName))
                        .setSource(new OwnershipSource().setType(MANUAL))
                        .setTypeUrn(Urn.createFromString("urn:li:corpGroup:BusinessUsers"));
            } catch (URISyntaxException e) {
                DatahubLogger.logInfo("Adding Owners Failed For......." + ownerName + " and " + ownershipType + e);
                throw new RuntimeException(e);
            }
            ownerArray.add(owner);
        });

        AuditStamp createdStamp = new AuditStamp()
                .setActor(new CorpuserUrn(SYSTEM_USER))
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
