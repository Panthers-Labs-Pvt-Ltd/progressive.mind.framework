package com.progressive.minds.chimera.core.api_service.repository;

import com.progressive.minds.chimera.core.api_service.dto.ChildDTO;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonSelectMapper;

@Mapper
public interface ChildRepo extends
      CommonInsertMapper<ChildDTO>, CommonSelectMapper{
}
