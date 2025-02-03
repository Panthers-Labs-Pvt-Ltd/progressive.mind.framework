package com.progressive.minds.chimera.dataquality.entities;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import java.sql.Timestamp;

public class DQUserConfig extends SqlTable {
    public final SqlColumn<Integer> rowNum = column("row_num");
    public final SqlColumn<String> dqConfigId = column("dq_config_id");
    public final SqlColumn<String> controlNm = column("control_nm");
    public final SqlColumn<String> ruleNm = column("rule_nm");
    public final SqlColumn<String> processTypNm = column("process_typ_nm");
    public final SqlColumn<String> checkLevel = column("check_level");
    public final SqlColumn<String> databaseNm = column("database_nm");
    public final SqlColumn<String> tableNm = column("table_nm");
    public final SqlColumn<String> ruleCol = column("rule_col");
    public final SqlColumn<String> ruleValue = column("rule_value");
    public final SqlColumn<String> reserved5 = column("reserved_5");
    public final SqlColumn<String> reserved4 = column("reserved_4");
    public final SqlColumn<String> reserved3 = column("reserved_3");
    public final SqlColumn<String> reserved2 = column("reserved_2");
    public final SqlColumn<String> reserved1 = column("reserved_1");
    public final SqlColumn<Timestamp> createdTs = column("created_ts");
    public final SqlColumn<String> createdBy = column("created_by");
    public final SqlColumn<Timestamp> updatedTs = column("updated_ts");
    public final SqlColumn<String> updatedBy = column("updated_by");
    public final SqlColumn<String> activeFlg = column("active_flg");

    public DQUserConfig() {
        super("edl_dq_user_config");
    }
}