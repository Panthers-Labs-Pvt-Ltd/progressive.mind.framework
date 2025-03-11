package com.progressive.minds.chimera.core.workflows.activities.activityImplementations;

import com.progressive.minds.chimera.core.workflows.activities.TransformDataActivity;
import com.progressive.minds.chimera.core.api_service.dto.TransformMetadataConfig;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.pipelineutils.TransformDataUtils;



public class TransformDataActivityImpl implements TransformDataActivity {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(TransformDataActivityImpl.class);

    @Override
    public void transformData(TransformMetadataConfig config) {
        logger.logInfo("******* Transform Data Activity Started *******");
        TransformDataUtils.transformData(config);
        logger.logInfo("******* Transform Data Activity Completed *******");
    }
}






