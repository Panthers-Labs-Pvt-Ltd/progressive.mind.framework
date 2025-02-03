package com.progressive.minds.chimera.dataquality.entities;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class DQSuggestions extends SqlTable {
    public final SqlColumn<Integer> rowNum = column("row_num");
    public final SqlColumn<String> dqCnstntId = column("dq_cnstnt_id");
    public final SqlColumn<String> processTypNm = column("process_typ_nm");
    public final SqlColumn<String> databaseNm = column("database_nm");
    public final SqlColumn<String> tableNm = column("table_nm");
    public final SqlColumn<String> ruleCol = column("rule_col");
    public final SqlColumn<String> dqConstraint = column("dq_constraint");
    public final SqlColumn<String> scalaCode = column("scala_code");
    public final SqlColumn<String> reserved5 = column("reserved_5");
    public final SqlColumn<String> reserved4 = column("reserved_4");
    public final SqlColumn<String> reserved3 = column("reserved_3");
    public final SqlColumn<String> reserved2 = column("reserved_2");
    public final SqlColumn<String> reserved1 = column("reserved_1");

    public DQSuggestions() {
        super("edl_dq_suggestions");
    }
}