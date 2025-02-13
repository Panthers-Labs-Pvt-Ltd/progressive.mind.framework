package com.progressive.minds.chimera.dataquality.entities;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class DataControlsMapEntity extends SqlTable {
//    create table if not exists edl_data_controls_map
//            (
//                    row_num 					serial,
//                    map_id					varchar(255) default('DM-'::text ||
//    lpad(((nextval('edl_data_control_map_dim_seq'::regclass))::CHARACTER VARYING)::text, 5,
//            '0'::text)) not null,
//    control_name				varchar(255),
//    control_desc				varchar(500),
//    process_typ_nm			varchar(255),
//    ref_metadata				varchar(255),
//    check_lvl						varchar(255),
//    reserved_5					varchar(500),
//    reserved_4					varchar(500),
//    reserved_3					varchar(500),
//    reserved_2					varchar(500),
//    reserved_1					varchar(500),
//    created_ts					timestamp default CURRENT_TIMESTAMP,
//    created_by					varchar(255) default CURRENT_USER,
//    updated_ts					timestamp,
//    updated_by					varchar(255),
//    active_flg					varchar(1)	default 'Y':: CHARACTER VARYING
//);

    public DataControlsMapEntity() {
        super("edl_data_controls_map");
    }
    public final SqlColumn<String> mapId = column("map_id");
    public final SqlColumn<String> controlName = column("control_name");
    public final SqlColumn<String> controlDesc = column("control_desc");
    public final SqlColumn<String> processTypNm = column("process_typ_nm");
    public final SqlColumn<String> refMetadata = column("ref_metadata");
    public final SqlColumn<String> checkLvl = column("check_lvl");
    public final SqlColumn<String> reserved5 = column("reserved_5");
    public final SqlColumn<String> reserved4 = column("reserved_4");
    public final SqlColumn<String> reserved3 = column("reserved_3");
    public final SqlColumn<String> reserved2 = column("reserved_2");
    public final SqlColumn<String> reserved1 = column("reserved_1");
    public final SqlColumn<String> createdBy = column("created_by");
    public final SqlColumn<String> updatedBy = column("updated_by");
    public final SqlColumn<String> activeFlg = column("active_flg");
}
