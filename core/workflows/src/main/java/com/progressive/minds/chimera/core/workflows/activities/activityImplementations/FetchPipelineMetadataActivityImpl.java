package com.progressive.minds.chimera.core.workflows.activities.activityImplementations;

import com.progressive.minds.chimera.core.workflows.activities.FetchPipelineMetadataActivity;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import com.progressive.minds.chimera.pipelineutils.PipelineMetadataFetcher;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import java.io.IOException;

public class FetchPipelineMetadataActivityImpl implements FetchPipelineMetadataActivity {
    private static final ChimeraLogger datalogger = ChimeraLoggerFactory.getLogger(ExtractDataActivityImpl.class);
    @Override
    public PipelineMetadata getPipelineMetadata(String pipelineName) throws IOException, InterruptedException {
        datalogger.logInfo("******* Fetch Metadata Activity Started *******");
        try {
            PipelineMetadata pipelineMetadata = PipelineMetadataFetcher.getPipelineMetadata(pipelineName);
            datalogger.logInfo("******* Fetch Metadata Activity Completed *******");
            return pipelineMetadata;
        } catch (IOException | InterruptedException e) {
            datalogger.logError("Error fetching metadata for pipeline: " + pipelineName, e);
            //TODO : Throw ChimeraException
            throw e;
        }
    }
}