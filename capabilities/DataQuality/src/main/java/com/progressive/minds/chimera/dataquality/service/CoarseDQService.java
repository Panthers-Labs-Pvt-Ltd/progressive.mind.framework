package com.progressive.minds.chimera.dataquality.service;

import com.progressive.minds.chimera.common.util.ChimeraDataFrame;
import com.progressive.minds.chimera.common.util.Engine;
import com.progressive.minds.chimera.dataquality.entities.DQRules;
import com.progressive.minds.chimera.dataquality.entities.DataControls;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CoarseDQService {
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(CoarseDQService.class);

    // inject DataFrame within CoarseDQService
    private ChimeraDataFrame dataFrame;
    private Engine engine;

    public CoarseDQService(ChimeraDataFrame dataFrame, Engine engine) {
        this.dataFrame = dataFrame;
        this.engine = engine;
    }

    // This method is not implemented yet.

    // profile the data
    /**
     * Performs coarse data profiling.
     */
    public void performCoarseDataProfiling() {
        logger.logInfo("Performing coarse data profiling.");
    }

    // This method is not implemented yet.
    /**
     * Performs coarse data quality checks.
     */
    public void performCoarseDQ() {
        logger.logInfo("Performing coarse data quality checks.");
    }

    // This method is not implemented yet.
    /**
     * Fetches coarse data quality controls.
     * @return List of coarse data quality controls.
     */
    public List<DataControls> getCoarseDQControls() {
        logger.logInfo("Fetching coarse data quality controls.");
        return new ArrayList<>();
    }

    // This method is not implemented yet.
    /**
     * Updates coarse data quality controls.
     * @return List of coarse data quality rules.
     */
    public List<DQRules> getCoarseDQRules() {
        logger.logInfo("Fetching coarse data quality rules.");
        return new ArrayList<>();
    }

    // This method is not implemented yet.
    /**
     * Updates coarse data quality rules.
     * @param dqRules List of coarse data quality rules.
     */
    public void updateCoarseDQRules(List<DQRules> dqRules) {
        logger.logInfo("Updating coarse data quality rules.");
    }
}
