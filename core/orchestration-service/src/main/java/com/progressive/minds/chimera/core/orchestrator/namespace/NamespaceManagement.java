package com.progressive.minds.chimera.core.orchestrator.namespace;

import io.temporal.serviceclient.WorkflowServiceStubs;

/**
 ## Setting up Namespace

 Namespaces are used to isolate workflows and task queues within Temporal. You can create multiple namespaces to organize your workflows based on different criteria such as environment, team, or application. Namespaces provide a way to manage access control, resource quotas, and retention policies for workflows. You can create namespaces using the Temporal CLI or API and configure them with the desired settings.

 Example of creating a namespace using the Temporal CLI:
 ```bash
 tctl namespace register -n my-namespace
 ```

 ### Key question per best practices for managing namespaces in Temporal:

 1. If there are 100 teams in an organization and there are 4 environments (dev, test, stage, prod), how many namespaces would you create?
 2. What are some best practices for managing namespaces in Temporal?
 3. How do you configure access control for namespaces in Temporal?
 4. How do you set resource quotas for namespaces in Temporal?
 5. What are some common retention policies for namespaces in Temporal?
 6. How do you delete a namespace in Temporal?
 7. How do you list all namespaces in Temporal?
 8. How do you get details about a specific namespace in Temporal?
 9. How do you update the settings of a namespace in Temporal?
 10. How do you archive a namespace in Temporal?
 11. How do you restore an archived namespace in Temporal?
 12. How do you purge a namespace in Temporal?
 13. How do you configure replication for namespaces in Temporal?
 14. How do you configure retention policies for workflows in a namespace in Temporal?
 15. How do you configure retention policies for task queues in a namespace in Temporal?
 16. How do you configure resource quotas for workflows in a namespace in Temporal?
 17. How do you configure resource quotas for task queues in a namespace in Temporal?
 18. How do you configure access control for workflows in a namespace in Temporal?
 19. How do you configure access control for task queues in a namespace in Temporal?
 20. How do you configure archival policies for workflows in a namespace in Temporal?
 21. How do you configure archival policies for task queues in a namespace in Temporal?
 22. How do you configure replication for workflows in a namespace in Temporal?
 23. How do you configure replication for task queues in a namespace in Temporal?
 */

public class NamespaceManagement {
    public static void main(String[] args) {
        // Create a WorkflowServiceStubs instance to communicate with the Temporal service
        WorkflowServiceStubs service = WorkflowServiceStubs.newInstance();
        CreateNamespace nm = new CreateNamespace();
        String[] environments = {"dev"};
        nm.createNamespacesForTeam(service, "my-namespace", environments);

        UpdateNamespace un = new UpdateNamespace(service, "my-namespace");
        un.updateNamespace("Updated description", "test@gmail.com");

        // Deprecation and Deletion Not allowed via programmatic API. It must be done via CLI.
    }

    // Write code to configure access control for namespaces in Temporal


}
