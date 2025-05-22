package com.progressive.minds.chimera.examples.mapper.generated;

import jakarta.annotation.Generated;
import java.sql.JDBCType;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class MetaDataPipelineDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8478472+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    public static final MetaDataPipeline metaDataPipeline = new MetaDataPipeline();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8478472+05:30", comments="Source field: test.META_DATA_PIPELINE.id")
    public static final SqlColumn<Long> id = metaDataPipeline.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8478472+05:30", comments="Source field: test.META_DATA_PIPELINE.name")
    public static final SqlColumn<String> name = metaDataPipeline.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8478472+05:30", comments="Source field: test.META_DATA_PIPELINE.meta_pipeline")
    public static final SqlColumn<String> metaPipeline = metaDataPipeline.metaPipeline;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8478472+05:30", comments="Source Table: test.META_DATA_PIPELINE")
    public static final class MetaDataPipeline extends AliasableSqlTable<MetaDataPipeline> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> metaPipeline = column("meta_pipeline", JDBCType.VARCHAR);

        public MetaDataPipeline() {
            super("\"test\".\"META_DATA_PIPELINE\"", MetaDataPipeline::new);
        }
    }
}