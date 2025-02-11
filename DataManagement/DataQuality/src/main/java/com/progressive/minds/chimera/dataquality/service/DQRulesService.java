package com.progressive.minds.chimera.dataquality.service;

import com.progressive.minds.chimera.dataquality.entities.DQRulesEntity;
import com.progressive.minds.chimera.dataquality.repository.DQRepository;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.select;

@Service
public class DQRulesService {

    @Autowired
    private DQRepository<DQRulesEntity> dqRulesEntityDQRepository;

    public Optional<DQRulesEntity> getRuleById(String ruleId) {
        DQRulesEntity dqRulesEntity = new DQRulesEntity();

        SelectStatementProvider selectStatement = select(dqRulesEntity.allColumns())
                .from(dqRulesEntity)
                .where(dqRulesEntity.ruleId, isEqualTo(ruleId))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        return dqRulesEntityDQRepository.selectOne(selectStatement);
    }

    public Optional<DQRulesEntity> getRuleByName(String ruleName) {
        DQRulesEntity dqRulesEntity = new DQRulesEntity();

        SelectStatementProvider selectStatement = select(dqRulesEntity.allColumns())
                .from(dqRulesEntity)
                .where(dqRulesEntity.ruleName, isEqualTo(ruleName))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        return dqRulesEntityDQRepository.selectOne(selectStatement);
    }

    public Optional<DQRulesEntity> getRuleByControlName(String controlName) {
        DQRulesEntity dqRulesEntity = new DQRulesEntity();

        SelectStatementProvider selectStatement = select(dqRulesEntity.allColumns())
                .from(dqRulesEntity)
                .where(dqRulesEntity.controlName, isEqualTo(controlName))
                .build()
                .render(RenderingStrategies.MYBATIS3);

        return dqRulesEntityDQRepository.selectOne(selectStatement);
    }

    public int insertRule(DQRulesEntity dqRules) {
        DQRulesEntity dqRulesEntity = new DQRulesEntity();

        InsertStatementProvider<DQRulesEntity> insertStatement =
                SqlBuilder.insert(dqRules)
                        .into(dqRulesEntity)
                        .build()
                        .render(RenderingStrategies.MYBATIS3);

        return dqRulesEntityDQRepository.insert(insertStatement);
    }

    public int updateRule(DQRulesEntity dqRules) {
        DQRulesEntity dqRulesEntity = new DQRulesEntity();

        UpdateStatementProvider updateStatementProvider = SqlBuilder.update(dqRulesEntity)
                .set(dqRulesEntity.ruleName).equalToWhenPresent((Supplier<String>) dqRules.ruleName)
                .set(dqRulesEntity.controlName).equalToWhenPresent((Supplier<String>) dqRules.controlName)
                .set(dqRulesEntity.activeFlg).equalToWhenPresent((Supplier<String>) dqRules.activeFlg)
                .set(dqRulesEntity.ruleDesc).equalToWhenPresent((Supplier<String>) dqRules.ruleDesc)
                .set(dqRulesEntity.ruleExample).equalToWhenPresent((Supplier<String>) dqRules.ruleExample)
                .set(dqRulesEntity.createdBy).equalToWhenPresent((Supplier<String>) dqRules.createdBy)
                .set(dqRulesEntity.createdTs).equalToWhenPresent((Supplier<String>) dqRules.createdTs)
                .set(dqRulesEntity.updatedBy).equalToWhenPresent((Supplier<String>) dqRules.updatedBy)
                .set(dqRulesEntity.updatedTs).equalToWhenPresent((Supplier<String>) dqRules.updatedTs)
                .where(dqRulesEntity.ruleId, isEqualTo(dqRules.ruleId))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return dqRulesEntityDQRepository.update(updateStatementProvider);
    }

    public int deleteRule (String ruleId) {
        DQRulesEntity dqRulesEntity = new DQRulesEntity();

        DeleteStatementProvider deleteStatementProvider =
                SqlBuilder.deleteFrom(dqRulesEntity)
                        .where(dqRulesEntity.ruleId, isEqualTo(ruleId))
                        .build()
                        .render(RenderingStrategies.MYBATIS3);

        // Execute the delete operation and return the number of rows affected
        return dqRulesEntityDQRepository.delete(deleteStatementProvider);
    }

}
