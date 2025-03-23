package com.progressive.minds.chimera.core.workflows.activities.activityImplementations;

import com.progressive.minds.chimera.core.dataSource.formats.jdbc.jdbc;
import com.progressive.minds.chimera.core.workflows.activities.PersistDataActivity;
import com.progressive.minds.chimera.core.api_service.dto.PersistMetadata;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.pipelineutils.PersistDataUtils;



public class PersistDataActivityImpl implements PersistDataActivity {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(jdbc.class);

    @Override
    public void persistData(PersistMetadata config) {
        logger.logInfo("******* Data Persist Activity Started *******");
        PersistDataUtils.persistData(config);
        logger.logInfo("******* Data Persist Activity Completed *******");
    }
       
}
