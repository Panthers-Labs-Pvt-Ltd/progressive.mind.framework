package com.progressive.minds.chimera.core.datahub.referances;

import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

public interface SharedLogger {

    String SYSTEM_USER = "Chimera";
    ChimeraLogger DatahubLogger = ChimeraLoggerFactory.getLogger(SharedLogger.class);

}
