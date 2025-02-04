package com.progressive.minds.chimera.dataquality.controls;

import com.progressive.minds.chimera.dataquality.entities.DataControlsLog;
import com.progressive.minds.chimera.dataquality.repository.DQRepository;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.types.StructType;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;

public abstract class DataControls {
    private DQRepository<DataControlsLog> dataControlsLogRepository;

    public abstract boolean validate() ;

    public int registerResult(DataControlsLog controlResults) {
        InsertStatementProvider<DataControlsLog> insertStatement =
                SqlBuilder.insert(controlResults)
                        .into(DataControlsLog.of("edl_data_control_log"))
                        .build()
                        .render(RenderingStrategies.MYBATIS3);
        return dataControlsLogRepository.insert(insertStatement);
    }
}
