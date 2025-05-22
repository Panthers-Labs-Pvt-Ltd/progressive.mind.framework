package com.progressive.minds.chimera.examples.mapper.generated;

import static com.progressive.minds.chimera.examples.mapper.generated.DataPipelineDynamicSqlSupport.*;

import com.progressive.minds.chimera.examples.model.generated.DataPipeline;
import jakarta.annotation.Generated;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface DataPipelineMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<DataPipeline>, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    BasicColumn[] selectList = BasicColumn.columnList(id, name, pipelineType);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="DataPipelineResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="pipeline_type", property="pipelineType", jdbcType=JdbcType.VARCHAR)
    })
    List<DataPipeline> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("DataPipelineResult")
    Optional<DataPipeline> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, dataPipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, dataPipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    default int insert(DataPipeline row) {
        return MyBatis3Utils.insert(this::insert, row, dataPipeline, c ->
            c.map(id).toProperty("id")
            .map(name).toProperty("name")
            .map(pipelineType).toProperty("pipelineType")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    default int insertMultiple(Collection<DataPipeline> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, dataPipeline, c ->
            c.map(id).toProperty("id")
            .map(name).toProperty("name")
            .map(pipelineType).toProperty("pipelineType")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    default int insertSelective(DataPipeline row) {
        return MyBatis3Utils.insert(this::insert, row, dataPipeline, c ->
            c.map(id).toPropertyWhenPresent("id", row::getId)
            .map(name).toPropertyWhenPresent("name", row::getName)
            .map(pipelineType).toPropertyWhenPresent("pipelineType", row::getPipelineType)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    default Optional<DataPipeline> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, dataPipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    default List<DataPipeline> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, dataPipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    default List<DataPipeline> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, dataPipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, dataPipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    static UpdateDSL<UpdateModel> updateAllColumns(DataPipeline row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(row::getId)
                .set(name).equalTo(row::getName)
                .set(pipelineType).equalTo(row::getPipelineType);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8458437+05:30", comments="Source Table: test.DATA_PIPELINE")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(DataPipeline row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(row::getId)
                .set(name).equalToWhenPresent(row::getName)
                .set(pipelineType).equalToWhenPresent(row::getPipelineType);
    }
}