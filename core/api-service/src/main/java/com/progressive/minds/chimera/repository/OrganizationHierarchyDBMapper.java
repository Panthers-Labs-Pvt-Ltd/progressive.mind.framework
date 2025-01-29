
package com.progressive.minds.chimera.repository;

import com.progressive.minds.chimera.dto.OrganizationHierarchy;
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
public interface OrganizationHierarchyDBMapper extends
    CommonCountMapper, CommonDeleteMapper,
    CommonInsertMapper<OrganizationHierarchy>, CommonSelectMapper,
    CommonUpdateMapper {

  @SelectProvider(type=SqlProviderAdapter.class, method="select")
  Optional<OrganizationHierarchy> selectOne(SelectStatementProvider selectStatement);


  @SelectProvider(type=SqlProviderAdapter.class, method="select")
  List<OrganizationHierarchy> selectMany(SelectStatementProvider selectStatement);
}
