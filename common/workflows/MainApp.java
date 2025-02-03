public class MainApp {

    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Create worker factory and workers
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker("BatchProcessingTaskQueue");

        worker.registerWorkflowImplementationTypes(
            MainWorkflowImpl.class,
            FetchPipelineMetadataWorkflowImpl.class
    );

    worker.registerActivitiesImplementations(new MainWorkflowImpl());
    factory.start();
    
     // Start Workflow Execution
     MainWorkflow workflow = client.newWorkflowStub(MainWorkflow.class,
     WorkflowOptions.newBuilder().setTaskQueue("BatchProcessingTaskQueue").build()
);

workflow.executeMainWorkflow("Test_Pipeline");
    
}
}


