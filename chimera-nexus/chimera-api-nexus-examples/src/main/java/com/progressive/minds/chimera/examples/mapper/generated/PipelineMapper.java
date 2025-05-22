package com.progressive.minds.chimera.examples.mapper.generated;

import static com.progressive.minds.chimera.examples.mapper.generated.PipelineDynamicSqlSupport.*;

import com.progressive.minds.chimera.examples.model.generated.Pipeline;
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
public interface PipelineMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<Pipeline>, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    BasicColumn[] selectList = BasicColumn.columnList(id, name);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="PipelineResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR)
    })
    List<Pipeline> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("PipelineResult")
    Optional<Pipeline> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, pipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, pipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    default int insert(Pipeline row) {
        return MyBatis3Utils.insert(this::insert, row, pipeline, c ->
            c.map(id).toProperty("id")
            .map(name).toProperty("name")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    default int insertMultiple(Collection<Pipeline> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, pipeline, c ->
            c.map(id).toProperty("id")
            .map(name).toProperty("name")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    default int insertSelective(Pipeline row) {
        return MyBatis3Utils.insert(this::insert, row, pipeline, c ->
            c.map(id).toPropertyWhenPresent("id", row::getId)
            .map(name).toPropertyWhenPresent("name", row::getName)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    default Optional<Pipeline> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, pipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    default List<Pipeline> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, pipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    default List<Pipeline> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, pipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, pipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    static UpdateDSL<UpdateModel> updateAllColumns(Pipeline row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(row::getId)
                .set(name).equalTo(row::getName);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(Pipeline row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(row::getId)
                .set(name).equalToWhenPresent(row::getName);
    }
}