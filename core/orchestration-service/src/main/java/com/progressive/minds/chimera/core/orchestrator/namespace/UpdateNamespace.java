package com.progressive.minds.chimera.core.orchestrator.namespace;

import com.google.protobuf.util.Durations;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import io.temporal.api.namespace.v1.NamespaceConfig;
import io.temporal.api.namespace.v1.UpdateNamespaceInfo;
import io.temporal.api.workflowservice.v1.UpdateNamespaceRequest;
import io.temporal.api.workflowservice.v1.UpdateNamespaceResponse;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.util.Optional;

public class UpdateNamespace {
    private static final ChimeraLogger LOGGER = ChimeraLoggerFactory.getLogger("UpdateNamespace");
    private WorkflowServiceStubs service = null;
    private String nameSpace = null;

    public UpdateNamespace(WorkflowServiceStubs service, String nameSpace) {
        this.service = service;
        this.nameSpace = nameSpace;
    }

    public void updateNamespace(String description, String ownerEmail) {
        UpdateNamespaceRequest updateNamespaceRequest = UpdateNamespaceRequest.newBuilder()
                .setNamespace(this.nameSpace) //the namespace that you want to update
                .setUpdateInfo(UpdateNamespaceInfo.newBuilder() //has options to update namespace info
                        .setDescription(description)
                        .setOwnerEmail(ownerEmail)
                        //.putAllData()
                        //.setState() - Really??
                        .build())
                .setConfig(NamespaceConfig.newBuilder() //has options to update namespace configuration
                        .setWorkflowExecutionRetentionTtl(Durations.fromHours(NamespaceDefaults.WorkflowExecutionRetentionTtlInHours)) //updates the retention period for the namespace "your-namespace--name" to 30 hrs.
                        //.getBadBinaries() - what is this?
                        // Other fields which we set while creating may need to be updated here
                        .build())
                .build();
        UpdateNamespaceResponse updateNamespaceResponse = service.blockingStub().updateNamespace(updateNamespaceRequest);

        LOGGER.logInfo("Namespace updated: " + updateNamespaceResponse);
    }
}
