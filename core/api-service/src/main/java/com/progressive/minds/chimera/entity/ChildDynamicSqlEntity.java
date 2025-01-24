package com.progressive.minds.chimera.entity;

import java.sql.JDBCType;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class ChildDynamicSqlEntity extends ParentDynamicSqlEntity {

    /* Child Table */
    public static final ChildTable childTable = new ChildTable();

    public static final SqlColumn<String> additionalColumn = childTable.additionalColumn;

    public static final class ChildTable extends SqlTable {
        public final SqlColumn<String> additionalColumn = column("ADDITIONAL_COLUMN", JDBCType.VARCHAR);

        public ChildTable() {
            super("child_table");
        }
    }
}
