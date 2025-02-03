package com.progressive.minds.chimera.common.workflows.activities;

import com.progressive.minds.chimera.dto.PipelineMetadata;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.io.IOException;

@ActivityInterface
public interface FetchPipelineMetadataActivity {

    @ActivityMethod
    public PipelineMetadata getPipelineMetadata(String pipelineName) throws IOException, InterruptedException;

}





