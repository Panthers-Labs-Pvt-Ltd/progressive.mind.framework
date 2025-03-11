package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.JDBCType;
import java.sql.Timestamp;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class TransformMetadataConfigDynamicSqlEntity {

    public static TransformMetadataConfigEntity transformMetadataConfig = new TransformMetadataConfigEntity();

    public static final SqlColumn<String> pipelineName = transformMetadataConfig.pipelineName;
    public static final SqlColumn<Integer> sequenceNumber = transformMetadataConfig.sequenceNumber;
    public static final SqlColumn<String> sqlText = transformMetadataConfig.sqlText;
    public static final SqlColumn<String> transformDataframeName = transformMetadataConfig.transformDataframeName;
    public static final SqlColumn<Timestamp> createdTimestamp = transformMetadataConfig.createdTimestamp;
    public static final SqlColumn<String> createdBy = transformMetadataConfig.createdBy;
    public static final SqlColumn<Timestamp> updatedTimestamp = transformMetadataConfig.updatedTimestamp;
    public static final SqlColumn<String> updatedBy = transformMetadataConfig.updatedBy;
    public static final SqlColumn<String> activeFlag = transformMetadataConfig.activeFlag;

    public static final class TransformMetadataConfigEntity extends SqlTable {

        public final SqlColumn<String> pipelineName = column("PIPELINE_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Integer> sequenceNumber = column("SEQUENCE_NUMBER", JDBCType.INTEGER);
        public final SqlColumn<String> sqlText = column("SQL_TEXT", JDBCType.CLOB);
        public final SqlColumn<String> transformDataframeName = column("TRANSFORM_DATAFRAME_NAME", JDBCType.VARCHAR);
        public final SqlColumn<Timestamp> createdTimestamp = column("CREATED_TIMESTAMP", JDBCType.TIMESTAMP);
        public final SqlColumn<String> createdBy = column("CREATED_BY", JDBCType.VARCHAR);
        public final SqlColumn<Timestamp> updatedTimestamp = column("UPDATED_TIMESTAMP", JDBCType.TIMESTAMP);
        public final SqlColumn<String> updatedBy = column("UPDATED_BY", JDBCType.VARCHAR);
        public final SqlColumn<String> activeFlag = column("ACTIVE_FLAG", JDBCType.VARCHAR);

        public TransformMetadataConfigEntity() {
            super("TRANSFORM_METADATA_CONFIG");
        }
    }
}
