package com.progressive.minds.chimera.dataquality.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DQRepository<T> extends
        CommonCountMapper, CommonDeleteMapper,
        CommonInsertMapper<T>, CommonSelectMapper,
        CommonUpdateMapper {

    @SelectProvider(type= SqlProviderAdapter.class, method="select")
    Optional<T> selectOne(SelectStatementProvider selectStatement);


    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    List<T> selectMany(SelectStatementProvider selectStatement);
}
