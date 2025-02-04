package com.progressive.minds.chimera.dataquality.entities;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class DataControlsLog extends SqlTable {
    public final SqlColumn<Integer> rowNum = column("row_num");
    public final SqlColumn<Integer> dcLogId = column("dc_log_id");
    public final SqlColumn<String> batchId = column("batch_id");
    public final SqlColumn<String> processTypNm = column("process_typ_nm");
    public final SqlColumn<String> controlNm = column("control_nm");
    public final SqlColumn<java.sql.Timestamp> startTs = column("start_ts");
    public final SqlColumn<java.sql.Timestamp> endTs = column("end_ts");
    public final SqlColumn<String> statusDesc = column("status_desc");
    public final SqlColumn<String> status = column("status");

    public DataControlsLog() {
        super("edl_data_control_log");
    }
}