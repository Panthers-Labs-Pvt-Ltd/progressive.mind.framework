package com.progressive.minds.chimera.foundational.logging.utils;

public class LogUtil {

    public String getLogSchema() {
        return """
    |ts TIMESTAMP,
    |level STRING,
    |msg STRING,
    |context map<STRING, STRING>,
    |exception STRUCT<
    |  class STRING,
    |  msg STRING,
    |  stacktrace ARRAY<STRUCT<
    |    class STRING,
    |    method STRING,
    |    file STRING,
    |    line STRING
    |  >>
    |>,
    |logger STRING""".strip();
    }
}
