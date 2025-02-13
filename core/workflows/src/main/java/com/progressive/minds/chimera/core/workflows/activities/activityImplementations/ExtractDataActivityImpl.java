package com.progressive.minds.chimera.core.workflows.activities.activityImplementations;

import com.progressive.minds.chimera.core.workflows.activities.ExtractDataActivity;
import com.progressive.minds.chimera.dto.ExtractMetadata;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.pipelineutils.ExtractDataUtils;

public class ExtractDataActivityImpl implements ExtractDataActivity {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(ExtractDataActivityImpl.class);

    @Override
    public void extractData(ExtractMetadata config) {
        logger.logInfo("******* Data Extraction Activity Started *******");
        ExtractDataUtils.getSourceType(config);
        logger.logInfo("******* Data Extraction Activity Completed *******");
    }

    
}
