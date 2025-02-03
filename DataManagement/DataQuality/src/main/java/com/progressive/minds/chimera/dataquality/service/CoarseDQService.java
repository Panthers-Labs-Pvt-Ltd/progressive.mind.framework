package com.progressive.minds.chimera.dataquality.service;

import com.progressive.minds.chimera.common.util.ChimeraDataFrame;
import com.progressive.minds.chimera.common.util.Engine;
import com.progressive.minds.chimera.dataquality.repository.DQRepository;
import com.progressive.minds.chimera.dataquality.entities.DQRules;
import com.progressive.minds.chimera.dataquality.entities.DataControls;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

import java.util.List;

import static org.mybatis.dynamic.sql.SqlBuilder.select;
import org.springframework.beans.factory.annotation.Autowired;

public class CoarseDQService {
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(CoarseDQService.class);

    // inject DataFrame within CoarseDQService
    private ChimeraDataFrame dataFrame;
    private Engine engine;

    @Autowired
    private DQRepository<DataControls> dataControlsDQRepository;

    @Autowired
    private DQRepository<DQRules> dqRulesRepository;

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

    /**
     * Fetches coarse data quality controls.
     * @return List of coarse data quality controls.
     */
    public List<DataControls> getCoarseDQControls() {
        logger.logInfo("Fetching coarse data quality controls.");
        DataControls dataControls = new DataControls();
        SelectStatementProvider selectStatement = select(dataControls.allColumns()).from(dataControls)
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return dataControlsDQRepository.selectMany(selectStatement);
    }

    /**
     * Updates coarse data quality controls.
     * @return List of coarse data quality rules.
     */
    public List<DQRules> getCoarseDQRules() {
        logger.logInfo("Fetching coarse data quality rules.");
        DQRules dqRules = new DQRules();
        SelectStatementProvider selectStatement = select(dqRules.allColumns()).from(dqRules)
                .build()
                .render(RenderingStrategies.MYBATIS3);

        return dqRulesRepository.selectMany(selectStatement);
    }

    /**
     * Insert coarse data quality rules.
     *
     * @param dqRules List of coarse data quality rules.
     * @return
     */
    public int insertCoarseDQRules(DQRules dqRules) {
        logger.logInfo("Updating coarse data quality rules.");

        // update the rule
        InsertStatementProvider<DQRules> insertStatement =
                SqlBuilder.insert(dqRules)
                        .into(DQRules.of("EDL_DQ_RULES"))
                        .build()
                        .render(RenderingStrategies.MYBATIS3);
        return dqRulesRepository.insert(insertStatement);
    }

    /**
     * Insert coarse data quality controls.
     *
     * @param dataControls List of coarse data quality controls.
     * @return
     */
    public int insertCoarseDQControls(DataControls dataControls) {
        logger.logInfo("Updating coarse data quality controls.");

        // update the control
        InsertStatementProvider<DataControls> insertStatement =
                SqlBuilder.insert(dataControls)
                        .into(DataControls.of("EDL_DATA_CONTROLS"))
                        .build()
                        .render(RenderingStrategies.MYBATIS3);
        return dataControlsDQRepository.insert(insertStatement);
    }

    // write code to update the rules
    public int updateDQRules() {
        logger.logInfo("Updating coarse data quality rules.");
        return 0;
    }

    public int updateDQControls() {
        logger.logInfo("Updating coarse data quality controls.");
        return 0;
    }

    public int deleteDQRules() {
        logger.logInfo("Deleting coarse data quality rules.");
        return 0;
    }

    public int deleteDQControls() {
        logger.logInfo("Deleting coarse data quality controls.");
        return 0;
    }

    public int deleteDQRuleById() {
        logger.logInfo("Deleting coarse data quality rule by id.");
        return 0;
    }

    public int deleteDQControlById() {
        logger.logInfo("Deleting coarse data quality control by id.");
        return 0;
    }

}
