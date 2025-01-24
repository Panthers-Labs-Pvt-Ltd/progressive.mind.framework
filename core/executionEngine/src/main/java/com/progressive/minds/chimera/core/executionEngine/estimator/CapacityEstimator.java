package com.progressive.minds.chimera.core.executionEngine.estimator;

import org.apache.logging.log4j.Logger;
import org.apache.spark.sql.SparkSession;
import org.apache.logging.log4j.LogManager;


/*
Little bit of theory
total memory on Node Cluster per executor =
            Python Memory (spark.executor.pyspark.memory)
            + Spark Off-heap memory (spark.memory.offheap.size)
            + Memory Overhead (spark.executor.memoryOverhead)
            + JVM Heap Memory (spark.executor.memory)
* Python Memory: pyspark is not supported in OptimizedSparkSession as of version 1.1.0
* Spark Off-heap memory: 0. See explanation below
* Memory Overhead:
* JVM Heap - Next section

JVM Heap Memory (spark.executor.memory) is broken down
            Spark Memory (spark.memory.Fraction)
            + User Memory (1 - spark.memory.Fraction)
            + Reserved Memory (300MB)
* Spark Memory - Next section
* User Memory is mainly used to store data needed for RDD conversion operations, such as lineage. You can store your own data
    structures there that will be used inside transformations. It's up to you what would be stored in this memory and how. Spark
    makes completely no accounting on what you do there and whether you respect this boundary or not
* Reserved Memory Spark reserves this memory to store internal objects. It guarantees to reserve sufficient memory for the
    system even for small JVM heaps

Spark Memory is the new Unified Memory is broken down by
            Storage Memory (spark.memory.storageFraction)
            + Execution Memory (1 - spark.memory.storageFraction)
* Storage Memory is used for caching and broadcasting data.
* Execution Memory is mainly used to store temporary data in the shuffle, join, sort, aggregation, etc.

Default logic of spark.executor.memoryOverhead = Min of (10% of spark.executor.memory, 384MB)

pyspark is not supported in OptimizedSparkSession as of version 1.1.0

Spark.Memory.OffHeap needs to be managed by application developers and hence trickier. For now, I recommend disabling it. We should
get a good memory snapshot on execution and then use this feature. This feature if managed carefully can provide up to 20% performance
benefit, 20% smaller memory footprint and much better control of memory management.


There are three sections to this -
1. driver node
2. executor nodes, and
3. sometimes even for the node manager
 */

public class CapacityEstimator extends ConfEstimator {
    Logger logger =  LogManager.getLogger(CapacityEstimator.class.getName());

    public CapacityEstimator(SparkSession.Builder builder) {
        super(builder);
    }

    // We must be able to ensure that entire platform is set to scale horizontally. Meaning if data volume or application instances
    // increase, we should able to add additional nodes to manage performance. Idea is
    // * Not to clog the server with overheads.
    // * Do not stay idle, waiting for something. Move forward when you can without adding too much complexity in design.

    // Lets understand how our application is designed. Interview questions -
    // When do you kick off application (trigger points) - This can be deciphered from Lineage as well and can be deduced when
    // the application can be kicked off automatically. Worst case, we can auto-analyze and comment on when the application could have
    // started.

    // Right now lets tune for Challenger, CECL, Capital and Risk Reporting.
    // Say 10 applications want to run in parallel.

    private int executorMemory;

    @Override
    public void calculate() {
            //Number of node in Cluster
            int numberOfExecutor;
            int numberOfNode = 5;
            int numberOfCoresPerNode = 36;
            int memoryPerNode = 72 * 1024; //Always in mb
            logger.info(String.format("Node #: %d; CorePerNode #: %d; memoryPerNode: %d m",numberOfNode, numberOfCoresPerNode, memoryPerNode));
            int totalMemory = numberOfNode * memoryPerNode; //720GB
            int memoryOverhead = Math.round((float) 0.1 * memoryPerNode) * numberOfNode; //72
            int totalAvailableCore = (numberOfCoresPerNode - 1) * numberOfNode; //350
            int totalAvailableMemory = totalMemory - memoryOverhead - 3000; //648GB approx
            logger.info(String.format("Total Memory: %d m; memoryOverHead: %d m; Total Available Core: %d; total Available Memory: %d m", totalMemory,memoryOverhead, totalAvailableCore, totalAvailableMemory));
            //same for driver
            int corePerExecutor = 7;
            int numberOfPossibleExecutors = Math.round((float) totalAvailableCore / corePerExecutor);
            logger.info("Possible Number of Executors: {}", numberOfPossibleExecutors);
            // Get the number of running applications and application to be scheduled in next 15 mins
            // RDS would maintain the changes within metadata module for now. It should later be moved to
            // workflowOrchestration Service.
            // This application should be part of the application to be scheduled.
            int applicationsInScope = 3;
            logger.info("Spark applications running or scheduled shortly: {}",applicationsInScope);
            // Assuming every application has equal footprint
            numberOfExecutor = Math.round((float) totalAvailableCore / corePerExecutor / applicationsInScope) - 1;
            logger.info("Number of executor for this application: {}",numberOfExecutor);

            executorMemory = Math.round((float) totalAvailableMemory / numberOfExecutor / applicationsInScope);
            logger.info("Executor Memory: {}", executorMemory);


    }

    @Override
    public void setConfigValue() {

         calculate();
        // Deploy time configuration
        //Number of cores for the executor/driver process


        //it enables us to set the memory utilized by every Spark driver process in cluster mode
        //it is the non-heap memory atop of the memory assigned using spark.driver.memory. If not set explicitly,
        //it is by default, driverMemory * 0.10, with minimum of 384.


        //If estimated size of the data is larger than maxResultSize given job will be aborted.
        //The goal here is to protect our application from driver loss
        builder.config("spark.driver.maxResultSize", executorMemory+"m");


        // Fraction of (heap space - 300MB) used for execution and storage. The lower this is, the more
        // frequently spills and cached data eviction occur. The purpose of this config is to set aside
        // memory for internal metadata, user data structures, and imprecise size estimation in the case of
        // sparse, unusually large records. Leaving this at the default value is recommended. For more detail,
        // including important information about correctly tuning JVM garbage collection when increasing
        // this value, see <a href="tuning.html#memory-management-overview">this description</a>.
        builder.config("spark.memory.fraction", 0.6);

        // Amount of storage memory immune to eviction, expressed as a fraction of the size of the region set
        // aside by <code>spark.memory.fraction</code>. The higher this is, the less working memory may be
        // available to execution and tasks may spill to disk more often. Leaving this at the default value
        // is recommended. For more detail, see
        // <a href="tuning.html#memory-management-overview">this description</a>.
        builder.config("spark.memory.storageFraction", 0.5);

        // If true, Spark will attempt to use off-heap memory for certain operations. If off-heap memory
        // use is enabled, then <code>spark.memory.offHeap.size</code> must be positive.

        // The absolute amount of memory which can be used for off-heap allocation, in bytes unless otherwise
        // specified. This setting has no impact on heap memory usage, so if your executors' total memory
        // consumption must fit within some hard limit then be sure to shrink your JVM heap size accordingly.
        // This must be set to a positive value when <code>spark.memory.offHeap.enabled=true</code>.


        // Enables proactive block replication for RDD blocks. Cached RDD block replicas lost due to
        // executor failures are replenished if there are any existing available replicas. This tries
        // to get the replication level of the block to the initial number.
        builder.config("spark.storage.replication.proactive", true);

        // Controls how often to trigger a garbage collection.<br><br>
        // This context cleaner triggers cleanups only when weak references are garbage collected.
        // In long-running applications with large driver JVMs, where there is little memory pressure
        // on the driver, this may happen very occasionally or not at all. Not cleaning at all may
        // lead to executors running out of disk space after a while.
        builder.config("spark.cleaner.periodicGC.interval", "5s");

        //Enables or disables context cleaning.
        builder.config("spark.cleaner.referenceTracking", true);

        // Controls whether the cleaning thread should block on cleanup tasks (other than shuffle, which is controlled by
        // <code>spark.cleaner.referenceTracking.blocking.shuffle</code> Spark property).
        builder.config("spark.cleaner.referenceTracking.blocking", true);

        //Controls whether the cleaning thread should block on shuffle cleanup tasks.
        builder.config("spark.cleaner.referenceTracking.blocking.shuffle", true);

        //Controls whether to clean checkpoint files if the reference is out of scope.
        builder.config("spark.cleaner.referenceTracking.cleanCheckpoints", true);

        logger.info("Memory Management Config: {}", builder);

    }
}
