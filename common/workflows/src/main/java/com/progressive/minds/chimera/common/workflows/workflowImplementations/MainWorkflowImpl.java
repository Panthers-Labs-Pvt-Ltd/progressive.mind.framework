package com.progressive.minds.chimera.common.workflows.workflowImplementations;

import com.progressive.minds.chimera.common.workflows.FetchPipelineMetadata;
import com.progressive.minds.chimera.common.workflows.MainWorkflow;
import com.progressive.minds.chimera.common.workflows.ExtractDataWorkflow;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

import com.progressive.minds.chimera.dto.ExtractMetadata;

import com.progressive.minds.chimera.dto.PipelineMetadata;
// import io.temporal.api.enums.v1.WorkflowIdReusePolicy;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class MainWorkflowImpl implements MainWorkflow {

    @Override
    public void executeMainWorkflow(String workflowId, String pipelineName) throws IOException, InterruptedException {

        ChildWorkflowOptions childOptions = ChildWorkflowOptions.newBuilder()
                .setWorkflowId(workflowId + new Timestamp(System.currentTimeMillis()))
//                .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE)
                .build();

        FetchPipelineMetadata pipelineMetadataFlow = Workflow.newChildWorkflowStub(FetchPipelineMetadata.class, childOptions);
        PipelineMetadata pipelineMetadata = pipelineMetadataFlow.getPipelineMetadata(pipelineName);

        List<ExtractMetadata> extractMetadata = pipelineMetadata.getExtractMetadata();
        extractMetadata.forEach(config -> System.out.println(config.getExtractSourceType()));

        if (extractMetadata != null && !extractMetadata.isEmpty()) {
            extractMetadata.forEach(config -> {
                ExtractDataWorkflow extractDataWorkflow = Workflow.newChildWorkflowStub(ExtractDataWorkflow.class, childOptions);
                extractDataWorkflow.extractData(config);
            });
        }

        
    
    }
}
