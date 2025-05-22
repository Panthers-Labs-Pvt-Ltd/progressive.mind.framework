package com.progressive.minds.chimera.examples.mapper.generated;

import jakarta.annotation.Generated;
import java.sql.JDBCType;
import org.mybatis.dynamic.sql.AliasableSqlTable;
import org.mybatis.dynamic.sql.SqlColumn;

public final class PipelineDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    public static final Pipeline pipeline = new Pipeline();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source field: test.PIPELINE.id")
    public static final SqlColumn<Long> id = pipeline.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source field: test.PIPELINE.name")
    public static final SqlColumn<String> name = pipeline.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2025-05-22T21:46:35.8468508+05:30", comments="Source Table: test.PIPELINE")
    public static final class Pipeline extends AliasableSqlTable<Pipeline> {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public Pipeline() {
            super("\"test\".\"PIPELINE\"", Pipeline::new);
        }
    }
}