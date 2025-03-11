package com.progressive.minds.chimera.core.api_service.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonSelectMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;

import com.google.common.base.Optional;
import com.progressive.minds.chimera.core.api_service.dto.ExtractMetadataConfig;

@Mapper
public interface ExtractConfigDBMapper extends
    CommonCountMapper, CommonDeleteMapper,
    CommonInsertMapper<ExtractMetadataConfig>, CommonSelectMapper,
    CommonUpdateMapper {

  @SelectProvider(type=SqlProviderAdapter.class, method="select")
  Optional<ExtractMetadataConfig> selectOne(SelectStatementProvider selectStatement);


  @SelectProvider(type=SqlProviderAdapter.class, method="select")
  List<ExtractMetadataConfig> selectMany(SelectStatementProvider selectStatement);
}
