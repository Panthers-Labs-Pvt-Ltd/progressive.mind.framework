package com.progressive.minds.chimera.core.api_service.entity;

import java.sql.Timestamp;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import java.sql.JDBCType;

public  class ParentDynamicSqlEntity {

    /* Parent Table */
    public static final ParentTable parentTable = new ParentTable();

    public static final SqlColumn<Integer> id = parentTable.id;
    public static final SqlColumn<String> name = parentTable.name;
    public static final SqlColumn<Timestamp> createdAt = parentTable.createdAt;

    public static final class ParentTable extends SqlTable {
        public final SqlColumn<Integer> id = column("ID", JDBCType.INTEGER);
        public final SqlColumn<String> name = column("NAME", JDBCType.VARCHAR);
        public final SqlColumn<Timestamp> createdAt = column("CREATED_AT", JDBCType.TIMESTAMP);

        public ParentTable() {
            super("parent_table");
        }
    }
}
