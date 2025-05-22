package com.progressive.minds.chimera.examples.mapper.generated;

import jakarta.annotation.Generated;
import java.sql.JDBCType;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class DataPipelineDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0870186+05:30", comments="Source Table: test.DATA_PIPELINE")
    public static final DataPipeline dataPipeline = new DataPipeline();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0870186+05:30", comments="Source field: test.DATA_PIPELINE.id")
    public static final SqlColumn<Long> id = dataPipeline.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0870186+05:30", comments="Source field: test.DATA_PIPELINE.name")
    public static final SqlColumn<String> name = dataPipeline.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0870186+05:30", comments="Source field: test.DATA_PIPELINE.pipeline_type")
    public static final SqlColumn<String> pipelineType = dataPipeline.pipelineType;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-21T21:04:10.0870186+05:30", comments="Source Table: test.DATA_PIPELINE")
    public static final class DataPipeline extends AliasableSqlTable<DataPipeline> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> pipelineType = column("pipeline_type", JDBCType.VARCHAR);

        public DataPipeline() {
            super("\"test\".\"DATA_PIPELINE\"", DataPipeline::new);
        }
    }
}