package com.progressive.minds.chimera.dataquality.entities;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class DQRulesEntity extends SqlTable {

//    create table if not exists edl_dq_rules
//            (
//                    row_num 					serial,
//                    rule_id					varchar(255) default('DQ-'::text ||
//    lpad(((nextval('edl_persist_dim_seq'::regclass))::CHARACTER VARYING)::text, 5,
//            '0'::text)) not null,
//    rule_name				varchar(255) not null,
//    control_name 		varchar(255),
//    rule_desc			varchar(500),
//    rule_example		varchar(255),
//    reserved_5					varchar(500),
//    reserved_4					varchar(500),
//    reserved_3					varchar(500),
//    reserved_2					varchar(500),
//    reserved_1					varchar(500),
//    created_ts					timestamp default CURRENT_TIMESTAMP,
//    created_by					varchar(255) default CURRENT_USER,
//    updated_ts					timestamp,
//    updated_by					varchar(255),
//    active_flg					varchar(1)	default 'Y':: CHARACTER VARYING,
//    CONSTRAINT edl_dq_pkey
//    PRIMARY KEY (rule_id),
//    CONSTRAINT edl_dq_rules_pk
//    UNIQUE (rule_name),
//    CONSTRAINT edl_dq_unq
//    UNIQUE (rule_name, control_name),
//    CONSTRAINT edl_dq_rules_config_edl_dc_rules_control_name_fk
//    FOREIGN KEY (control_name) REFERENCES edl_data_controls
//);

    public final SqlColumn<String> ruleId = column("RULE_ID");
    public final SqlColumn<String> ruleName = column("RULE_NAME");
    public final SqlColumn<String> controlName = column("CONTROL_NAME");
    public final SqlColumn<String> ruleDesc = column("RULE_DESC");
    public final SqlColumn<String> ruleExample = column("RULE_EXAMPLE");
    public final SqlColumn<String> reserved5 = column("RESERVED_5");
    public final SqlColumn<String> reserved4 = column("RESERVED_4");
    public final SqlColumn<String> reserved3 = column("RESERVED_3");
    public final SqlColumn<String> reserved2 = column("RESERVED_2");
    public final SqlColumn<String> reserved1 = column("RESERVED_1");
    public final SqlColumn<String> createdTs = column("CREATED_TS");
    public final SqlColumn<String> createdBy = column("CREATED_BY");
    public final SqlColumn<String> updatedTs = column("UPDATED_TS");
    public final SqlColumn<String> updatedBy = column("UPDATED_BY");
    public final SqlColumn<String> activeFlg = column("ACTIVE_FLG");

    public DQRulesEntity() {
        super("EDL_DQ_RULES");
    }

}
