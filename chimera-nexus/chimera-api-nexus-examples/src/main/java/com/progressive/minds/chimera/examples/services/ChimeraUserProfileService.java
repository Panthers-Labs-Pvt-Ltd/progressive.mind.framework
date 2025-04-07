package com.progressive.minds.chimera.examples.services;

import com.progressive.minds.chimera.examples.mapper.ContractCustomerMapper;
import com.progressive.minds.chimera.examples.mapper.CustomerMapper;
import com.progressive.minds.chimera.examples.mapper.UserProfileMapper;
import com.progressive.minds.chimera.examples.model.ContractCustomer;
import com.progressive.minds.chimera.examples.model.UserProfile;
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

}
