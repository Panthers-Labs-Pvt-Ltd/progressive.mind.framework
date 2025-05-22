package com.progressive.minds.chimera.examples.mapper;

import com.progressive.minds.chimera.examples.mapper.generated.MetaDataPipelineDynamicSqlSupport;
import com.progressive.minds.chimera.examples.mapper.generated.MetaDataPipelineMapper;
import com.progressive.minds.chimera.examples.model.generated.MetaDataPipeline;
import org.apache.ibatis.executor.BatchResult;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.GeneralInsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertSelectStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;

import java.util.List;
import java.util.Optional;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

public class CustomMetaDataPipelineMapper implements MetaDataPipelineMapper {

    private final MetaDataPipelineMapper delegate;

    public CustomMetaDataPipelineMapper(MetaDataPipelineMapper delegate) {
        this.delegate = delegate;
    }


    public void customDelete(long dataId) {
        delegate.delete(c ->
                c.where(MetaDataPipelineDynamicSqlSupport.id, isEqualTo(dataId)));
    }

    @Override
    public List<MetaDataPipeline> selectMany(SelectStatementProvider selectStatement) {
        return delegate.selectMany(selectStatement);
    }

    @Override
    public Optional<MetaDataPipeline> selectOne(SelectStatementProvider selectStatement) {
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
    public int insert(InsertStatementProvider<MetaDataPipeline> insertStatement) {
        return delegate.insert(insertStatement);
    }

    @Override
    public int insertMultiple(MultiRowInsertStatementProvider<MetaDataPipeline> insertStatement) {
        return delegate.insertMultiple(insertStatement);
    }

    @Override
    public List<BatchResult> flush() {
        return delegate.flush();
    }

    @Override
    public int generalInsert(GeneralInsertStatementProvider insertStatement) {
        return delegate.generalInsert(insertStatement);
    }

    @Override
    public int insertSelect(InsertSelectStatementProvider insertSelectStatement) {
        return delegate.insertSelect(insertSelectStatement);
    }

    @Override
    public int update(UpdateStatementProvider updateStatement) {
        return delegate.update(updateStatement);
    }
}
