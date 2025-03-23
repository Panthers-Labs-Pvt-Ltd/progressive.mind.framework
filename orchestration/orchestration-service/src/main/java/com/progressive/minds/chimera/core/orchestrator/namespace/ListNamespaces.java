package com.progressive.minds.chimera.core.orchestrator.namespace;


import io.temporal.api.workflowservice.v1.*;
import io.temporal.serviceclient.WorkflowServiceStubs;

import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

public class ListNamespaces {
    private static final ChimeraLogger LOGGER = ChimeraLoggerFactory.getLogger("DeprecateNamespace");
    // TODO: Use Customer Authorizer to check if the user has the required permissions to deprecate a namespace
    public void listNamespaces(WorkflowServiceStubs service) {
        ListNamespacesRequest listNamespaces = ListNamespacesRequest.newBuilder().build();
    ListNamespacesResponse listNamespacesResponse = service.blockingStub().listNamespaces(listNamespaces);
    LOGGER.logInfo("List of Namespaces: " + listNamespacesResponse);

}
}

