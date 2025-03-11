package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class OrganizationTypesDynamicSqlEntity {

    /* Organization_Types Table */
    public static OrganizationTypesEntity organizationTypes = new OrganizationTypesEntity();

    public static final SqlColumn<String> orgTypeName = organizationTypes.orgTypeName;
    public static final SqlColumn<String> orgTypeDesc = organizationTypes.orgTypeDesc;
    public static final SqlColumn<String> userField1 = organizationTypes.userField1;
    public static final SqlColumn<String> userField2 = organizationTypes.userField2;
    public static final SqlColumn<String> userField3 = organizationTypes.userField3;
    public static final SqlColumn<String> userField4 = organizationTypes.userField4;
    public static final SqlColumn<String> userField5 = organizationTypes.userField5;
    public static final SqlColumn<Timestamp> createdTimestamp = organizationTypes.createdTimestamp;
    public static final SqlColumn<String> createdBy = organizationTypes.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = organizationTypes.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = organizationTypes.updatedBy;
    public static final SqlColumn<String> activeFlag = organizationTypes.activeFlag;

    public static final class OrganizationTypesEntity extends SqlTable {

        /* Columns */
        public final SqlColumn<String> orgTypeName = column("ORG_TYPE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<String> orgTypeDesc = column("ORG_TYPE_DESC", JDBCType.VARCHAR);
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

        public OrganizationTypesEntity() {
            super("ORGANIZATION_TYPES");
        }
    }
}
