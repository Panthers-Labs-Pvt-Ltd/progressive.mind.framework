package com.progressive.minds.chimera.core.datalineage;

import com.progressive.minds.chimera.core.datalineage.models.OpenLineageTransportTypes;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

public interface SharedLogger {

    ChimeraLogger LineageLogger = ChimeraLoggerFactory.getLogger(OpenLineageTransportTypes.class);

}
