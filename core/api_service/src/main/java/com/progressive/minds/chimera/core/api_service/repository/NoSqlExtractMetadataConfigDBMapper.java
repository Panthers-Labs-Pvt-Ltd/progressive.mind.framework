package com.progressive.minds.chimera.core.api_service.repository;

import com.progressive.minds.chimera.core.api_service.dto.NoSqlExtractMetadataConfig;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonSelectMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper;

@Mapper
public interface NoSqlExtractMetadataConfigDBMapper extends
    CommonCountMapper, CommonDeleteMapper,
    CommonInsertMapper<NoSqlExtractMetadataConfig>, CommonSelectMapper,
    CommonUpdateMapper {

  @SelectProvider(type=SqlProviderAdapter.class, method="select")
  Optional<NoSqlExtractMetadataConfig> selectOne(SelectStatementProvider selectStatement);


  @SelectProvider(type=SqlProviderAdapter.class, method="select")
  List<NoSqlExtractMetadataConfig> selectMany(SelectStatementProvider selectStatement);
}
