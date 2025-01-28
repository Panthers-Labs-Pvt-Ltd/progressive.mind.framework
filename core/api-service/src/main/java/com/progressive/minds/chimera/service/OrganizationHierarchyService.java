package com.progressive.minds.chimera.service;

import static com.progressive.minds.chimera.entity.OrganizationHierarchyDynamicSqlEntity.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.progressive.minds.chimera.dto.OrganizationHierarchy;
import com.progressive.minds.chimera.repository.OrganizationHierarchyDBMapper;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrganizationHierarchyService {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConnectionsService.class);

    private final OrganizationHierarchyDBMapper orgHierDBMapper;

    public OrganizationHierarchyService(OrganizationHierarchyDBMapper orgHierDBMapper) {
        this.orgHierDBMapper = orgHierDBMapper;
    }

    public long getTotalNumberOfOrgHierarchy() {
        logger.info("Fetching total number of Org Hierarchy.");
        SelectStatementProvider countStatementProvider = select(count())
            .from(organizationHierarchy)
            .build()
            .render(RenderingStrategies.MYBATIS3);

        long count = orgHierDBMapper.count(countStatementProvider);
        logger.info("Total Org Hierarchy count: {}", count);
        return count;
    }

    public Optional<OrganizationHierarchy> getOrgHierarchyByName(String orgName) {
        logger.info("Fetching Org Hierarchy with name: {}", orgName);
        SelectStatementProvider selectStatement = select(organizationHierarchy.allColumns())
            .from(organizationHierarchy)
            .where(orgHierName, isEqualTo(orgName))
            .build()
            .render(RenderingStrategies.MYBATIS3);

        return orgHierDBMapper.selectOne(selectStatement);
    }

    public List<OrganizationHierarchy> getAllOrgHierarchy() {
        logger.info("Fetching all Org Hierarchy.");
        SelectStatementProvider selectStatement = select(organizationHierarchy.allColumns())
            .from(organizationHierarchy)
            .build()
            .render(RenderingStrategies.MYBATIS3);

        return orgHierDBMapper.selectMany(selectStatement);
    }

    public int insertOrgHier(OrganizationHierarchy orgHier) {
        logger.info("Inserting new org Hier: {}", orgHier);
        InsertStatementProvider<OrganizationHierarchy> insertStatement = insert(orgHier)
            .into(organizationHierarchy)
            .map(orgHierName).toProperty("orgHierName")
            .map(orgTypeName).toProperty("orgTypeName")
            .map(parentOrgName).toProperty("parentOrgName")
            .map(cooOwner).toProperty("cooOwner") 
            .map(opsLead).toProperty("opsLead")
            .map(techLead).toProperty("techLead")
            .map(busOwner).toProperty("busOwner")
            .map(orgDesc).toProperty("orgDesc")
            .map(orgEmail).toProperty("orgEmail")
            .map(orgCi).toProperty("orgCi")
            .map(userField1).toProperty("userField1")
            .map(userField2).toProperty("userField2")
            .map(userField3).toProperty("userField3")
            .map(userField4).toProperty("userField4")
            .map(userField5).toProperty("userField5")
            .map(activeFlag).toProperty("activeFlag")
            .map(createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
            .map(createdBy).toProperty("createdBy")
            .build()
            .render(RenderingStrategies.MYBATIS3);            
        return orgHierDBMapper.insert(insertStatement);
    }

    public int updateOrgHier(OrganizationHierarchy updatedOrg) {
        logger.info("Updating Org Hier: {}", updatedOrg.getOrgHierName());
        UpdateStatementProvider updateStatement = update(organizationHierarchy)
            .set(orgTypeName).equalToWhenPresent(updatedOrg.getOrgTypeName())
            .set(parentOrgName).equalToWhenPresent(updatedOrg.getParentOrgName())
            .set(cooOwner).equalToWhenPresent(updatedOrg.getCooOwner())
            .set(opsLead).equalToWhenPresent(updatedOrg.getOpsLead())
            .set(techLead).equalToWhenPresent(updatedOrg.getTechLead())
            .set(busOwner).equalToWhenPresent(updatedOrg.getBusOwner())
            .set(orgDesc).equalToWhenPresent(updatedOrg.getOrgDesc())
            .set(orgEmail).equalToWhenPresent(updatedOrg.getOrgEmail())
            .set(orgCi).equalToWhenPresent(updatedOrg.getOrgCi())
            .set(userField1).equalToWhenPresent(updatedOrg.getUserField1())
            .set(userField2).equalToWhenPresent(updatedOrg.getUserField2())
            .set(userField3).equalToWhenPresent(updatedOrg.getUserField3())
            .set(userField4).equalToWhenPresent(updatedOrg.getUserField4())
            .set(userField5).equalToWhenPresent(updatedOrg.getUserField5())
            .set(activeFlag).equalToWhenPresent(updatedOrg.getActiveFlag())
            .set(updatedBy).equalToWhenPresent(updatedOrg.getUpdatedBy())
            .set(updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
            .where(orgHierName, isEqualTo(updatedOrg.getOrgHierName()))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return orgHierDBMapper.update(updateStatement);
    }
            

    public int deleteOrgHier(String orgName) {
        logger.info("Deleting Org Hier: {}", orgName);
        DeleteStatementProvider deleteStatement = deleteFrom(organizationHierarchy)
            .where(orgHierName, isEqualTo(orgName))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return orgHierDBMapper.delete(deleteStatement);

    }
    
}
