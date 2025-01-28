package com.progressive.minds.chimera.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class OrganizationHierarchyDynamicSqlEntity {

    /* Organization_Hierarchy Table */
    public static OrganizationHierarchyEntity organizationHierarchy = new OrganizationHierarchyEntity();

    public static final SqlColumn<String> orgHierName = organizationHierarchy.orgHierName;
    public static final SqlColumn<String> orgTypeName = organizationHierarchy.orgTypeName;
    public static final SqlColumn<String> parentOrgName = organizationHierarchy.parentOrgName;
    public static final SqlColumn<String> cooOwner = organizationHierarchy.cooOwner;
    public static final SqlColumn<String> opsLead = organizationHierarchy.opsLead;
    public static final SqlColumn<String> techLead = organizationHierarchy.techLead;
    public static final SqlColumn<String> busOwner = organizationHierarchy.busOwner;
    public static final SqlColumn<String> orgDesc = organizationHierarchy.orgDesc;
    public static final SqlColumn<String> orgEmail = organizationHierarchy.orgEmail;
    public static final SqlColumn<String> orgCi = organizationHierarchy.orgCi;
    public static final SqlColumn<String> userField1 = organizationHierarchy.userField1;
    public static final SqlColumn<String> userField2 = organizationHierarchy.userField2;
    public static final SqlColumn<String> userField3 = organizationHierarchy.userField3;
    public static final SqlColumn<String> userField4 = organizationHierarchy.userField4;
    public static final SqlColumn<String> userField5 = organizationHierarchy.userField5;
    public static final SqlColumn<Timestamp> createdTimestamp = organizationHierarchy.createdTimestamp;
    public static final SqlColumn<String> createdBy = organizationHierarchy.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = organizationHierarchy.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = organizationHierarchy.updatedBy;
    public static final SqlColumn<String> activeFlag = organizationHierarchy.activeFlag;

    public static final class OrganizationHierarchyEntity extends SqlTable {

        /* Columns */
        public final SqlColumn<String> orgHierName = column("ORG_HIER_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> orgTypeName = column("ORG_TYPE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> parentOrgName = column("PARENT_ORG_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> cooOwner = column("COO_OWNER", JDBCType.VARCHAR);
        public final SqlColumn<String> opsLead = column("OPS_LEAD", JDBCType.VARCHAR);
        public final SqlColumn<String> techLead = column("TECH_LEAD", JDBCType.VARCHAR);
        public final SqlColumn<String> busOwner = column("BUS_OWNER", JDBCType.VARCHAR);
        public final SqlColumn<String> orgDesc = column("ORG_DESC", JDBCType.LONGNVARCHAR);
        public final SqlColumn<String> orgEmail = column("ORG_EMAIL", JDBCType.VARCHAR);
        public final SqlColumn<String> orgCi = column("ORG_CI", JDBCType.VARCHAR);
        public final SqlColumn<String> userField1 = column("USER_FIELD_1", JDBCType.VARCHAR);
        public final SqlColumn<String> userField2 = column("USER_FIELD_2", JDBCType.VARCHAR);
        public final SqlColumn<String> userField3 = column("USER_FIELD_3", JDBCType.VARCHAR);
        public final SqlColumn<String> userField4 = column("USER_FIELD_4", JDBCType.VARCHAR);
        public final SqlColumn<String> userField5 = column("USER_FIELD_5", JDBCType.VARCHAR);
        public final SqlColumn<Timestamp> createdTimestamp = column("CREATED_TIMESTAMP", JDBCType.TIMESTAMP);
        public final SqlColumn<String> createdBy = column("CREATED_BY", JDBCType.VARCHAR);
        public final SqlColumn<Timestamp> updatedTimestamp = column("UPDATED_TIMESTAMP", JDBCType.TIMESTAMP);
        public final SqlColumn<String> updatedBy = column("UPDATED_BY", JDBCType.VARCHAR);
        public final SqlColumn<String> activeFlag = column("ACTIVE_FLAG", JDBCType.VARCHAR);

        public OrganizationHierarchyEntity() {
            super("ORGANIZATION_HIERARCHY");
        }
    }
}
