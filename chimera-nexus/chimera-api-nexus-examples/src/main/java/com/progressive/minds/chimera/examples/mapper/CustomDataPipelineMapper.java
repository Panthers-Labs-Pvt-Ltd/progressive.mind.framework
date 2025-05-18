package com.progressive.minds.chimera.examples.mapper;

import com.progressive.minds.chimera.examples.mapper.generated.DataPipelineMapper;
import com.progressive.minds.chimera.examples.model.generated.DataPipeline;
import org.apache.ibatis.executor.BatchResult;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.GeneralInsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertSelectStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.progressive.minds.chimera.examples.mapper.generated.DataPipelineDynamicSqlSupport.*;
import static com.progressive.minds.chimera.examples.mapper.generated.DataPipelineDynamicSqlSupport.pipelineType;

public class CustomDataPipelineMapper implements DataPipelineMapper {

    private final DataPipelineMapper delegate;

    public CustomDataPipelineMapper(DataPipelineMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<DataPipeline> selectMany(SelectStatementProvider selectStatement) {
        return delegate.selectMany(selectStatement);
    }

    @Override
    public Optional<DataPipeline> selectOne(SelectStatementProvider selectStatement) {
        return delegate.selectOne(selectStatement);
    }

    @Override
    public long count(SelectStatementProvider selectStatement) {
        return delegate.count(selectStatement);
    }

    @Override
    public int delete(DeleteStatementProvider deleteStatement) {
        return delegate.delete(deleteStatement);
    }

    @Override
    public int insert(InsertStatementProvider<DataPipeline> insertStatement) {
        return delegate.insert(insertStatement);
    }

    public int customInsert(DataPipeline row) {
        return MyBatis3Utils.insert(
                delegate::insert,
                row,
                dataPipeline,
                c -> c.map(id).toProperty("id")
                        .map(name).toProperty("name")
                        .map(pipelineType).toProperty("pipelineType")
        );
    }

    @Override
    public int insertMultiple(MultiRowInsertStatementProvider<DataPipeline> insertStatement) {
        return 0;
    }

    @Override
    public List<BatchResult> flush() {
        return List.of();
    }

    @Override
    public int generalInsert(GeneralInsertStatementProvider insertStatement) {
        return 0;
    }

    @Override
    public int insertSelect(InsertSelectStatementProvider insertSelectStatement) {
        return 0;
    }

    @Override
    public int update(UpdateStatementProvider updateStatement) {
        return 0;
    }
}
