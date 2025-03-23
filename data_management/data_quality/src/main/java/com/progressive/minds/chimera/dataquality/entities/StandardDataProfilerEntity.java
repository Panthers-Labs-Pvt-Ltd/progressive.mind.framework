package com.progressive.minds.chimera.dataquality.entities;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class StandardDataProfilerEntity extends SqlTable {
    public final SqlColumn<Integer> profilerId = column("profiler_id");
    public final SqlColumn<String> tableNm = column("table_nm");
    public final SqlColumn<String> columnNm = column("column_nm");
    public final SqlColumn<String> approxDistinctValues = column("approx_distinct_values");
    public final SqlColumn<String> completeness = column("completeness");
    public final SqlColumn<String> datatype = column("datatype");
    public final SqlColumn<String> description = column("description");
    public final SqlColumn<String> reserved5 = column("reserved_5");
    public final SqlColumn<String> reserved4 = column("reserved_4");
    public final SqlColumn<String> reserved3 = column("reserved_3");
    public final SqlColumn<String> reserved2 = column("reserved_2");
    public final SqlColumn<String> reserved1 = column("reserved_1");

    public StandardDataProfilerEntity() {
        super("edl_std_data_profiler");
    }
}