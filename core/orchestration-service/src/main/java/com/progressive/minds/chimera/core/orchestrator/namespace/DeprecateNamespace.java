package com.progressive.minds.chimera.core.orchestrator.namespace;

import io.temporal.api.workflowservice.v1.*;
import io.temporal.serviceclient.WorkflowServiceStubs;

import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

public class DeprecateNamespace {
    private static final ChimeraLogger LOGGER = ChimeraLoggerFactory.getLogger("DeprecateNamespace");
    // TODO: Use Customer Authorizer to check if the user has the required permissions to deprecate a namespace
    public void deprecateNamespace(WorkflowServiceStubs service, String namespaceName) {
        //...
DeprecateNamespaceRequest deprecateNamespaceRequest = DeprecateNamespaceRequest.newBuilder()
.setNamespace(namespaceName) //specify the namespace that you want to deprecate
.build();
DeprecateNamespaceResponse response = service.blockingStub().deprecateNamespace(deprecateNamespaceRequest);
LOGGER.logInfo("Namespace deprecated: " + response);
//...


}
}

