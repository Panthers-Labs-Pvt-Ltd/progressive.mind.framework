package com.progressive.minds.chimera.common.workflows.workflowImplementations;

import com.progressive.minds.chimera.common.workflows.FetchPipelineMetadata;
import com.progressive.minds.chimera.common.workflows.MainWorkflow;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;
import io.temporal.api.enums.v1.WorkflowIdReusePolicy;

import java.io.IOException;
import java.sql.Timestamp;

public class MainWorkflowImpl implements MainWorkflow {

    @Override
    public void executeMainWorkflow(String workflowId, String pipelineName) throws IOException, InterruptedException {
        ChildWorkflowOptions childOptions = ChildWorkflowOptions.newBuilder()
                .setWorkflowId(workflowId + new Timestamp(System.currentTimeMillis()))
//                .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE)
                .build();

        FetchPipelineMetadata childWorkflow = Workflow.newChildWorkflowStub(FetchPipelineMetadata.class, childOptions);
        childWorkflow.getPipelineMetadata(pipelineName);
    
    }
}
