package com.progressive.minds.chimera.core.execution_engine.estimator;

import org.apache.logging.log4j.Logger;
import org.apache.spark.sql.SparkSession;
import org.apache.logging.log4j.LogManager;

import static java.lang.Math.round;

/*
This class is responsible to manage all the configuration related to driver.
 */
public class DriverEstimator extends ConfEstimator {

    private static final Logger log = LogManager.getLogger(DriverEstimator.class.getName());

    protected DriverEstimator(SparkSession.Builder ctxBuilder) {
        //protected builder variable initialized
        super(ctxBuilder);
        //if you want to test, validity of session and builder.
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

    public static int[] estimateSparkDriverCore() {
        return  new int[3];
    }
}

