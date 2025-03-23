package com.progressive.minds.chimera.core.orchestrator.namespace;

import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import io.temporal.api.operatorservice.v1.DeleteNamespaceRequest;
import io.temporal.api.operatorservice.v1.DeleteNamespaceResponse;
import io.temporal.serviceclient.OperatorServiceStubs;
import io.temporal.serviceclient.OperatorServiceStubsOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class DeleteNamespace {
    private static final ChimeraLogger LOGGER = ChimeraLoggerFactory.getLogger("DeleteNamespace");
    public void deleteNamespace(WorkflowServiceStubs service,String namespaceName) {
        //...
DeleteNamespaceResponse res =
OperatorServiceStubs.newServiceStubs(OperatorServiceStubsOptions.newBuilder()
        .setChannel(service.getRawChannel())
        .validateAndBuildWithDefaults())
    .blockingStub()
    .deleteNamespace(DeleteNamespaceRequest.newBuilder().setNamespace(namespaceName).build());
//...


        LOGGER.logInfo("Namespace deprecated: " + res);

}


}

