package com.progressive.minds.chimera.dataquality.entities;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import java.sql.JDBCType;
import java.sql.Timestamp;

public final class DataControlsEntity extends SqlTable {
    /*  Data Controls Table */
//    row_num 					serial,
//    control_id					varchar(255) default('DC-'::text ||
//    lpad(((nextval('edl_data_control_config_seq'::regclass))::CHARACTER VARYING)::text, 5,
//            '0'::text)) not null,
//    control_name				varchar(255) not null,
//    control_desc				varchar(255),
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
//    CONSTRAINT edl_data_control_pkey
//    PRIMARY KEY (control_name),
//    CONSTRAINT edl_data_control_unq
//    UNIQUE(control_name)

    // Create MyBatis entity class for DataControls table

    public final SqlColumn<String> controlId = column("CONTROL_ID", JDBCType.VARCHAR);
    public final SqlColumn<String> controlName = column("CONTROL_NAME", JDBCType.VARCHAR);
    public final SqlColumn<String> controlDesc = column("CONTROL_DESC", JDBCType.VARCHAR);
    public final SqlColumn<String> reserved5 = column("RESERVED_5", JDBCType.VARCHAR);
    public final SqlColumn<String> reserved4 = column("RESERVED_4", JDBCType.VARCHAR);
    public final SqlColumn<String> reserved3 = column("RESERVED_3", JDBCType.VARCHAR);
    public final SqlColumn<String> reserved2 = column("RESERVED_2", JDBCType.VARCHAR);
    public final SqlColumn<String> reserved1 = column("RESERVED_1", JDBCType.VARCHAR);
    public final SqlColumn<Timestamp> createdTs = column("CREATED_TS", JDBCType.TIMESTAMP);
    public final SqlColumn<String> createdBy = column("CREATED_BY", JDBCType.VARCHAR);
    public final SqlColumn<Timestamp> updatedTs = column("UPDATED_TS", JDBCType.TIMESTAMP);
    public final SqlColumn<String> updatedBy = column("UPDATED_BY", JDBCType.VARCHAR);
    public final SqlColumn<String> activeFlg = column("ACTIVE_FLG", JDBCType.VARCHAR);

    public DataControlsEntity() {
        super("DATA_CONTROLS");
    }

}
