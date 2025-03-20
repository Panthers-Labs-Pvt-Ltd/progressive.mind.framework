package com.progressive.minds.chimera.core.orchestrator.namespace;

import io.temporal.api.workflowservice.v1.*;
import io.temporal.serviceclient.WorkflowServiceStubs;

import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

public class DescribeNamespace {
    private static final ChimeraLogger LOGGER = ChimeraLoggerFactory.getLogger("DescribeNamespace");
    public void describeNamespace(WorkflowServiceStubs service , String namespaceName) {
        DescribeNamespaceRequest descNamespace = DescribeNamespaceRequest.newBuilder()
            .setNamespace(namespaceName) //specify the namespace you want details for
            .build();
        DescribeNamespaceResponse describeNamespaceResponse = service.blockingStub().describeNamespace(descNamespace);
        LOGGER.logInfo("Namespace Description: " + describeNamespaceResponse.toString());
    }

}
