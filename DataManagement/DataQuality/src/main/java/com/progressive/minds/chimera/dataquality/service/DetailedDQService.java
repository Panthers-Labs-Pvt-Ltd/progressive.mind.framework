package com.progressive.minds.chimera.dataquality.service;

import com.progressive.minds.chimera.common.util.ChimeraDataFrame;
import com.progressive.minds.chimera.common.util.Engine;
import com.progressive.minds.chimera.dataquality.entities.DQRules;
import com.progressive.minds.chimera.dataquality.entities.DataControls;

import java.util.ArrayList;
import java.util.List;

public class DetailedDQService {

    // Inject DataFrame within DetailedDQService
    private ChimeraDataFrame dataFrame;
    private Engine engine;

    public DetailedDQService(ChimeraDataFrame dataFrame, Engine engine) {
        this.dataFrame = dataFrame;
        this.engine = engine;
    }

    // This method is not implemented yet.
    /**
     * Performs detailed data quality checks.
     */
    public void performDetailedDQ() {
    }

    // This method is not implemented yet.
    /**
     * Fetches detailed data quality controls.
     * @return List of detailed data quality controls.
     */
    public List<DataControls> getDetailedDQControls() {
        return new ArrayList<>();
    }

    // This method is not implemented yet.
    /**
     * Fetches detailed data quality rules.
     * @return List of detailed data quality rules.
     */
    public List<DQRules> getDetailedDQRules() {
        return new ArrayList<>();
    }

    // This method is not implemented yet.
    /**
     * Updates detailed data quality rules.
     * @param dqRules List of detailed data quality rules.
     */
    public void updateDetailedDQRules(List<DQRules> dqRules) {
    }
}
