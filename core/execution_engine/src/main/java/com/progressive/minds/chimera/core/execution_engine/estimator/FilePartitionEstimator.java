package com.progressive.minds.chimera.core.execution_engine.estimator;

import org.apache.logging.log4j.Logger;
import org.apache.spark.sql.SparkSession;
import org.apache.logging.log4j.LogManager;

/*

The number of partitions decided in the input RDD/Dataset could affect
the efficiency of the entire execution pipeline of the Job. Hence, it is important to set things right.
This class in essence, optimizes configurations as used in
org.apache.spark.sql.execution.datasources.FilePartition (A collection of file blocks
that should be read as a single task)

Four configurations which decide this -
sparkSession.sessionState.conf.filesOpenCostInBytes
sparkSession.sessionState.conf.filesMaxPartitionBytes
sparkSession.sessionState.conf.filesMinPartitionNum
sparkSession.sparkContext.defaultParallelism

Implementing E-73
 */

public class FilePartitionEstimator extends ConfEstimator {
    private final Logger log = LogManager.getLogger(FilePartitionEstimator.class.getName());

    protected FilePartitionEstimator(SparkSession.Builder ctxBuilder) {
        super(ctxBuilder);
        log.info(ctxBuilder);
    }

    @Override
    void calculate() {
        /*
        to calculate
         */

    }

    @Override
    void setConfigValue() {
        /*
        to set the config value
         */

    }
}
