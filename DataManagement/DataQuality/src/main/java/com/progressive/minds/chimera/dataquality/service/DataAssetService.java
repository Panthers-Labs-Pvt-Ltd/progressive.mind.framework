package com.progressive.minds.chimera.dataquality.service;

import com.progressive.minds.chimera.common.util.ChimeraDataFrame;
import com.progressive.minds.chimera.dataquality.common.DataAsset;
import com.progressive.minds.chimera.dataquality.entities.DQRulesEntity;
import com.progressive.minds.chimera.dataquality.entities.DQUserConfigEntity;
import com.progressive.minds.chimera.dataquality.repository.DQRepository;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Service
public class DataAssetService {

    private String database;
    private String table;
    private ChimeraDataFrame dataframe;

    @Autowired
    private DQRepository<DQUserConfigEntity> dqUserConfigEntityDQRepository;

    @Autowired
    private DQRepository<DQRulesEntity> dqRulesEntityDQRepository;

    public DataAssetService(String database, String table, ChimeraDataFrame data) {
        this.database = database;
        this.table = table;
        this.dataframe = data;
    }

    // get DQUserConfigEntity by database and table
    public DQUserConfigEntity getUserConfig() {
        assert dqUserConfigEntityDQRepository != null;

        DQUserConfigEntity dqUserConfigEntity = new DQUserConfigEntity();

        SelectStatementProvider selectStatement = select(dqUserConfigEntity.allColumns())
                .from(dqUserConfigEntity)
                .where(dqUserConfigEntity.databaseNm, isEqualTo(database))
                .and(dqUserConfigEntity.tableNm, isEqualTo(table))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        return dqUserConfigEntityDQRepository.selectOne(selectStatement).orElse(null);
    }

    // set DQUserConfigEntity by database and table
    public void setUserConfig(DQUserConfigEntity userConfig) {
        assert dqUserConfigEntityDQRepository != null;

        DQUserConfigEntity dqUserConfigEntity = new DQUserConfigEntity();
        InsertStatementProvider<DQUserConfigEntity> insertRow =
                SqlBuilder.insert(userConfig)
                        .into(dqUserConfigEntity)
                        .build()
                        .render(RenderingStrategies.MYBATIS3);

        dqUserConfigEntityDQRepository.insert(insertRow);
    }

    // get data quality rules for the data asset
    public List<DQRulesEntity> getAssetRules() {
        return getAssetRules(database, table);
    }

    private List<DQRulesEntity> getAssetRules(String database, String table) {
        assert dqUserConfigEntityDQRepository != null;

        DQRulesEntity dqRulesEntity = new DQRulesEntity();
        DQUserConfigEntity dqUserConfigEntity = new DQUserConfigEntity();

        SelectStatementProvider selectStatement = select(dqRulesEntity.allColumns())
                .from(dqRulesEntity)
                .join(dqUserConfigEntity)
                .on(dqUserConfigEntity.ruleNm, equalTo(dqRulesEntity.ruleName))
                .where(dqUserConfigEntity.databaseNm, isEqualTo(database))
                .and(dqUserConfigEntity.tableNm, isEqualTo(table))
                .orderBy(dqRulesEntity.controlName)
                .build()
                .render(RenderingStrategies.MYBATIS3);

        return dqRulesEntityDQRepository.selectMany(selectStatement);
    }

    // Sets data quality rules for a specific column
    // public void setColumnRules(String columnName, List<DQRulesEntity> rules) {
    //     columnRules.put(columnName, rules);
    // }

    // Gets data quality rules for a specific column
    // public List<DQRulesEntity> getColumnRules(String columnName) {
    //     return columnRules.get(columnName);
    // }
}