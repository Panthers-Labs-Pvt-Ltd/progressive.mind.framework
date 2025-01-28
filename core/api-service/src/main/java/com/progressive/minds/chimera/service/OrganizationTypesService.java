package com.progressive.minds.chimera.service;

import static com.progressive.minds.chimera.entity.OrganizationTypesDynamicSqlEntity.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import com.progressive.minds.chimera.dto.OrganizationTypes;
import com.progressive.minds.chimera.repository.OrganizationTypesDBMapper;
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
public class OrganizationTypesService {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConnectionsService.class);

    private final OrganizationTypesDBMapper OrgDBMapper;

    public OrganizationTypesService(OrganizationTypesDBMapper OrgDBMapper) {
        this.OrgDBMapper = OrgDBMapper;
    }

    public long getTotalNumberOfOrganizations() {
        logger.info("Fetching total number of Organizations.");
        SelectStatementProvider countStatementProvider = select(count())
            .from(organizationTypes)
            .build()
            .render(RenderingStrategies.MYBATIS3);

        long count = OrgDBMapper.count(countStatementProvider);
        logger.info("Total Organizations count: {}", count);
        return count;
    }

    public Optional<OrganizationTypes> getOrganizationByName(String orgName) {
        logger.info("Fetching Org with Name: {}", orgName);
        SelectStatementProvider selectStatement = select(organizationTypes.allColumns())
            .from(organizationTypes)
            .where(orgTypeName, isEqualTo(orgName))
            .build()
            .render(RenderingStrategies.MYBATIS3);

        return OrgDBMapper.selectOne(selectStatement);
    }

    public List<OrganizationTypes> getAllOrganizations() {
        logger.info("Fetching all Organizations.");
        SelectStatementProvider selectStatement = select(organizationTypes.allColumns())
            .from(organizationTypes)
            .build()
            .render(RenderingStrategies.MYBATIS3);

        return OrgDBMapper.selectMany(selectStatement);
    }

    public int insertOrganization(OrganizationTypes org) {
        logger.info("Inserting new organization: {}", org);
        InsertStatementProvider<OrganizationTypes> insertStatement = insert(org)
            .into(organizationTypes)
            .map(orgTypeName).toProperty("orgTypeName")
            .map(orgTypeDesc).toProperty("orgTypeDesc")
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

        return OrgDBMapper.insert(insertStatement);
    }

    public int updateOrganization(OrganizationTypes org) {
        logger.info("Updating Org with Name: {}", org.getOrgTypeName());
        UpdateStatementProvider updateStatement = update(organizationTypes)
            .set(orgTypeDesc).equalToWhenPresent(org.getOrgTypeDesc())
            .set(userField1).equalToWhenPresent(org.getUserField1())
            .set(userField2).equalToWhenPresent(org.getUserField2())
            .set(userField3).equalToWhenPresent(org.getUserField3())
            .set(userField4).equalToWhenPresent(org.getUserField4())
            .set(userField5).equalToWhenPresent(org.getUserField5())
            .set(activeFlag).equalToWhenPresent(org.getActiveFlag())
            .set(updatedBy).equalToWhenPresent(org.getUpdatedBy())
            .set(updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
            .where(orgTypeName, isEqualTo(org.getOrgTypeName()))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return OrgDBMapper.update(updateStatement);
    }

    public int deleteConnection(String orgName) {
        logger.info("Deleting Organization with Name: {}", orgName);
        DeleteStatementProvider deleteStatement = deleteFrom(organizationTypes)
            .where(orgTypeName, isEqualTo(orgName))
            .build()
            .render(RenderingStrategies.MYBATIS3);

        return OrgDBMapper.delete(deleteStatement);
    }
}

