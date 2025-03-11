package com.progressive.minds.chimera.core.api_service.repository;

import com.progressive.minds.chimera.core.api_service.dto.ParentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper;
import org.mybatis.dynamic.sql.util.mybatis3.CommonSelectMapper;

@Mapper
public interface ParentRepo extends
    CommonInsertMapper<ParentDTO>, CommonSelectMapper{
}
