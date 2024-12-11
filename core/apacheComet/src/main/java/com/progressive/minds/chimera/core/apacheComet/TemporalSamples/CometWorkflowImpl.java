package com.progressive.minds.chimera.core.apacheComet.TemporalSamples;


import com.progressive.minds.chimera.core.orchestrator.workflow.BaseWorkflow;

public class CometWorkflowImpl implements BaseWorkflow, SharedSparkSession {

  @Override
  public void execute(String input) {
    System.out.println("sjkdfbhevbfhr");
  /*  Dataset<Row> dataFrame = spark.emptyDataFrame();
    dataFrame = new FileReader().read("parquet", spark, "ParquetReaderTest",
        input, "", "",
        null, "", "",
        "", 0);
    System.out.println("Total Records " + dataFrame.count());*/
  }
}
