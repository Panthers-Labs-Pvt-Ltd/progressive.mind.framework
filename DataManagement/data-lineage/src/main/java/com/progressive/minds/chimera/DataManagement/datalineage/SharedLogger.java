package com.progressive.minds.chimera.DataManagement.datalineage;


import com.progressive.minds.chimera.DataManagement.datalineage.transports.OpenLineageTransportTypes;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

public interface SharedLogger {

    ChimeraLogger LineageLogger = ChimeraLoggerFactory.getLogger(OpenLineageTransportTypes.class);

}
