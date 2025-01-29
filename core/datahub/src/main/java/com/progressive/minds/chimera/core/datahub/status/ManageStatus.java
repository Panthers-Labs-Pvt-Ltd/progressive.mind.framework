package com.progressive.minds.chimera.core.datahub.status;

import com.linkedin.common.urn.Urn;
import com.linkedin.common.Status;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;

import static com.progressive.minds.chimera.core.datahub.Constants.STATUS_ASPECT_NAME;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;

public class ManageStatus {

    public String addStatus(Urn entityUrn, String entityType, String changeType,
                            Boolean isRemoved ) throws Exception {

        Status removedStatus = new Status().setRemoved(isRemoved);

        GenericAspect genericAspect = serializeAspect(removedStatus);
        MetadataChangeProposal proposal = createProposal(String.valueOf(entityUrn), entityType,
                STATUS_ASPECT_NAME, changeType,genericAspect);
        return emitProposal(proposal, entityType);
    }
}
