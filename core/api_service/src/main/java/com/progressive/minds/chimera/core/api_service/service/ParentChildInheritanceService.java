package com.progressive.minds.chimera.core.api_service.service;


import static com.progressive.minds.chimera.core.api_service.entity.ChildDynamicSqlEntity.additionalColumn;
import static com.progressive.minds.chimera.core.api_service.entity.ChildDynamicSqlEntity.childTable;
import static com.progressive.minds.chimera.core.api_service.entity.ChildDynamicSqlEntity.id;
import static com.progressive.minds.chimera.core.api_service.entity.ChildDynamicSqlEntity.name;
import static com.progressive.minds.chimera.core.api_service.entity.ParentDynamicSqlEntity.createdAt;
// import static com.progressive.minds.chimera.core.api_service.entity.ParentDynamicSqlEntity.createdAt;
// import static com.progressive.minds.chimera.core.api_service.entity.ParentDynamicSqlEntity.id;
// import static com.progressive.minds.chimera.core.api_service.entity.ParentDynamicSqlEntity.name;
// import static com.progressive.minds.chimera.core.api_service.entity.ParentDynamicSqlEntity.parentTable;

import com.progressive.minds.chimera.core.api_service.dto.ChildDTO;
// import com.progressive.minds.chimera.core.api_service.dto.ParentDTO;
import com.progressive.minds.chimera.core.api_service.repository.ChildRepo;
// import com.progressive.minds.chimera.core.api_service.repository.ParentRepo;
import java.sql.Timestamp;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParentChildInheritanceService {

  // @Autowired
  // private ParentRepo parentRepo;
  @Autowired
  private ChildRepo childRepo;

  // public int insertParentRecord(ParentDTO parentDTO) {
  //   InsertStatementProvider<ParentDTO> insertRow = SqlBuilder.insert(parentDTO)
  //       .into(parentTable)
  //       .map(id).toProperty("id")
  //       .map(name).toProperty("name")
  //       .map(createdAt).toProperty("createdAt")
  //       .build()
  //       .render(RenderingStrategies.MYBATIS3);
  //   return parentRepo.insert(insertRow);
  // }

  public int insertChildRecord(ChildDTO childDTO) {
    InsertStatementProvider<ChildDTO> insertRow = SqlBuilder.insert(childDTO)
        .into(childTable)
        .map(id).toProperty("id")
        .map(name).toProperty("name")
        .map(createdAt).toConstant("'" + new Timestamp(System.currentTimeMillis()) + "'")
        .map(additionalColumn).toProperty("additionalColumn")
        .build()
        .render(RenderingStrategies.MYBATIS3);
    return childRepo.insert(insertRow);
  }
}
