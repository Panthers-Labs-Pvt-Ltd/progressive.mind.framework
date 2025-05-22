package com.progressive.minds.chimera.examples.mapper.generated;

import static com.progressive.minds.chimera.examples.mapper.generated.MetaDataPipelineDynamicSqlSupport.*;

import com.progressive.minds.chimera.examples.model.generated.MetaDataPipeline;
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
public interface MetaDataPipelineMapper extends CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<MetaDataPipeline>, CommonUpdateMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0896688+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    BasicColumn[] selectList = BasicColumn.columnList(id, name, metaPipeline);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0885393+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="MetaDataPipelineResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="meta_pipeline", property="metaPipeline", jdbcType=JdbcType.VARCHAR)
    })
    List<MetaDataPipeline> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0885393+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("MetaDataPipelineResult")
    Optional<MetaDataPipeline> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0885393+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, metaDataPipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0885393+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, metaDataPipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0885393+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    default int insert(MetaDataPipeline row) {
        return MyBatis3Utils.insert(this::insert, row, metaDataPipeline, c ->
            c.map(id).toProperty("id")
            .map(name).toProperty("name")
            .map(metaPipeline).toProperty("metaPipeline")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0885393+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    default int insertMultiple(Collection<MetaDataPipeline> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, metaDataPipeline, c ->
            c.map(id).toProperty("id")
            .map(name).toProperty("name")
            .map(metaPipeline).toProperty("metaPipeline")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0885393+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    default int insertSelective(MetaDataPipeline row) {
        return MyBatis3Utils.insert(this::insert, row, metaDataPipeline, c ->
            c.map(id).toPropertyWhenPresent("id", row::getId)
            .map(name).toPropertyWhenPresent("name", row::getName)
            .map(metaPipeline).toPropertyWhenPresent("metaPipeline", row::getMetaPipeline)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0896688+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    default Optional<MetaDataPipeline> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, metaDataPipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0896688+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    default List<MetaDataPipeline> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, metaDataPipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0896688+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    default List<MetaDataPipeline> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, metaDataPipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0896688+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, metaDataPipeline, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0896688+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    static UpdateDSL<UpdateModel> updateAllColumns(MetaDataPipeline row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(row::getId)
                .set(name).equalTo(row::getName)
                .set(metaPipeline).equalTo(row::getMetaPipeline);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0896688+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(MetaDataPipeline row, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(row::getId)
                .set(name).equalToWhenPresent(row::getName)
                .set(metaPipeline).equalToWhenPresent(row::getMetaPipeline);
    }
}