package com.progressive.minds.chimera.examples.services;


import com.progressive.minds.chimera.examples.mapper.CustomDataPipelineMapper;
import com.progressive.minds.chimera.examples.mapper.generated.ContractCustomerMapper;
import com.progressive.minds.chimera.examples.mapper.generated.CustomerMapper;
import com.progressive.minds.chimera.examples.mapper.generated.DataPipelineMapper;
import com.progressive.minds.chimera.examples.mapper.generated.UserProfileMapper;
import com.progressive.minds.chimera.examples.model.generated.ContractCustomer;
import com.progressive.minds.chimera.examples.model.generated.DataPipeline;
import com.progressive.minds.chimera.examples.model.generated.UserProfile;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChimeraUserProfileService {

  @Autowired private UserProfileMapper userProfileMapper;

  @Autowired
  private ContractCustomerMapper contractCustomerMapper;

  @Autowired
  private CustomerMapper customerMapper;

  @Autowired
  private DataPipelineMapper dataPipelineMapper;

  @Autowired
  private CustomDataPipelineMapper customDataPipelineMapper;

  public void createUserProfile(UserProfile profile) {
    int insert = userProfileMapper.insert(profile);
  }

  public List<UserProfile> getAllUserProfile() {
    return userProfileMapper.select(SelectDSLCompleter.allRows());
  }


  @Transactional
  public void createContractCustomer(ContractCustomer contractCustomer){
    contractCustomerMapper.insert(contractCustomer);
  }

  @Transactional
  public void createDataPipeLine(DataPipeline dataPipeline){
    customDataPipelineMapper.customInsert(dataPipeline);
  }

  public List<DataPipeline> getAllDataPipeLines(){
    return dataPipelineMapper.select(SelectDSLCompleter.allRows());
  }

}
