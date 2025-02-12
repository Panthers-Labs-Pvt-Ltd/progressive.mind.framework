package com.progressive.minds.chimera.core.workflows.workflowImplementations;

import com.progressive.minds.chimera.core.workflows.*;
import com.progressive.minds.chimera.dto.ExtractMetadata;
import com.progressive.minds.chimera.dto.PersistMetadata;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import com.progressive.minds.chimera.dto.TransformMetadataConfig;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

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
        List<TransformMetadataConfig> transformMetadata = pipelineMetadata.getTransformMetadata();
        List<PersistMetadata> persistMetadata = pipelineMetadata.getPersistMetadata();
        extractMetadata.forEach(config -> System.out.println(config.getExtractSourceType()));

        if (extractMetadata != null && !extractMetadata.isEmpty()) {
            extractMetadata.forEach(config -> {
                ExtractDataWorkflow extractDataWorkflow = Workflow.newChildWorkflowStub(ExtractDataWorkflow.class, childOptions);
                try {
                    extractDataWorkflow.extractData(config);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        if (transformMetadata != null && !transformMetadata.isEmpty()) {
            transformMetadata.forEach(config -> {
                System.out.println("Transform Metadata : " + config);
                TransformDataWorkflow transformDataWorkflow = Workflow.newChildWorkflowStub(TransformDataWorkflow.class, childOptions);
                try {
                    transformDataWorkflow.transformData(config);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if (persistMetadata != null && !persistMetadata.isEmpty()) {
            persistMetadata.forEach(config -> {
                System.out.println("Persist Metadata : " + config);
                PersistDataWorkflow persistDataWorkflow = Workflow.newChildWorkflowStub(PersistDataWorkflow.class, childOptions);
                try {
                    persistDataWorkflow.persistData(config);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        
    
    }
}
