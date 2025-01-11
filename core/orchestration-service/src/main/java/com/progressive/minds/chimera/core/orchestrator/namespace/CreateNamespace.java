package com.progressive.minds.chimera.core.orchestrator.namespace;

import com.google.protobuf.util.Durations;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import io.temporal.api.workflowservice.v1.RegisterNamespaceRequest;
import io.temporal.api.workflowservice.v1.RegisterNamespaceResponse;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class CreateNamespace {
    private static final ChimeraLogger LOGGER = ChimeraLoggerFactory.getLogger("CreateNamespace");

    // TODO: Use Customer Authorizer to check if the user has the required permissions to create a namespace
    public void createNamespacesForTeam(WorkflowServiceStubs service, String teamName, String[] environments) {
        for (String environment : environments) {
            String namespace = teamName + "-" + environment;
            createNamespace(service, namespace, environment);
        }
    }

    private void createNamespace(WorkflowServiceStubs service, String nameSpace, String environment) {
        // This may take up to 10sec per namespace
        // Create a RegisterNamespaceRequest with the desired namespace name
        RegisterNamespaceRequest request = RegisterNamespaceRequest.newBuilder()
                .setNamespace(nameSpace)
                .setWorkflowExecutionRetentionPeriod(Durations.fromDays(NamespaceDefaults.WorkflowExecutionRetentionPeriodInDays)) // keeps the Workflow Execution
                //Event History for these many days in the Persistence store.
                //.putAllData() - check what is this used for
                //.setOwnerEmail("")
                //.setSecurityToken("temporal-security-token")
                //.addClusters() or setClusters - what is this used for
                //.setActiveClusterName() - when should we use this.
                .setDescription("Namespace for team " + nameSpace + " in " + environment)
                // TODO: History Archival State and URI
                //.setHistoryArchivalState()
                //.setHistoryArchivalStateValue()
                //.setHistoryArchivalUri()
                // TODO: Visibility Archival State and URI
                //.setVisibilityArchivalState(NamespaceConfig.ArchivalState.ARCHIVAL_STATE_DISABLED)
                //.setVisibilityArchivalUri("")
                //.setVisibilityArchivalStateValue()
                //.setBadBinaries(NamespaceConfig.BadBinaries.newBuilder().build()) - Explore what is this used for
                .setIsGlobalNamespace(false) // Set to true if you want to share the namespace across multiple Temporal clusters
                .build();

        // Register the namespace
        RegisterNamespaceResponse response = service.blockingStub().registerNamespace(request);

        LOGGER.logInfo("Namespace created: " + response);
    }

    // TODO: Write code to configure access control for namespaces in Temporal

    // Write code to set resource quotas for namespaces in Temporal

}
