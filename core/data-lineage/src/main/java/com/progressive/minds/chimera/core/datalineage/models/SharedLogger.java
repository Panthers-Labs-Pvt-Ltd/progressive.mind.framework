package com.progressive.minds.chimera.core.datalineage.models;

import com.progressive.minds.chimera.core.dataSource.sourceTypes.JDBCReader;
import com.progressive.minds.chimera.core.datalineage.models.Transport.OpenLineageTransportTypes;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

public interface SharedLogger {

    ChimeraLogger LineageLogger = ChimeraLoggerFactory.getLogger(OpenLineageTransportTypes.class);

}
