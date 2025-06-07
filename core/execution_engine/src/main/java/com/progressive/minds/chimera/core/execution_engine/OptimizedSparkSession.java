/*
We use OptimizedSparkSession only for Spark on EKS in a batch mode.

We never intend to use Spark for Streaming purposes.

 */

package com.progressive.minds.chimera.core.executionEngine;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import com.progressive.minds.chimera.core.execution_engine.estimator.CapacityEstimator;
import com.progressive.minds.chimera.core.execution_engine.estimator.HadoopS3Estimator;
import com.progressive.minds.chimera.core.execution_engine.estimator.SQLEstimator;
import scala.Int;


public class OptimizedSparkSession extends SparkSession {

    private static final Logger log = LogManager.getLogger(OptimizedSparkSession.class.getName());
    private static String application;
    private static String testmode;
    private static SparkSessionBuilder builder;
    private static final String SPARK = "spark";
    private static final String MANUAL = "manual";

    public OptimizedSparkSession(SparkContext sc) {
        super(sc);
    }

    public static SparkSessionBuilder builder(){
        builder = new SparkSessionBuilder();
        return builder;
    }

    private static void preInit() {
        log.info("preInitializer");
    }
    /*
    This is the initiation of any spark application
     */
    public static OptimizedSparkSession get(String applicationName,String mode) {
        application = applicationName;
        testmode = mode;
        return get();
    }

    /*
    This should never be called. This API would soon be removed.
     */
    public static OptimizedSparkSession get() {
        log.info("Currently OptimizedSparkSession supports kubernetes only");

        //Find out the calling application name.

        // preInit
        preInit();

        //basic configuration
        if(!testmode.equalsIgnoreCase("test")) {
            builder = (SparkSessionBuilder) OptimizedSparkSession.builder()
                    // we will support cluster mode on EKS
                    .config("spark.submit.deployMode", "cluster");}
        // Should be passed from spark-submit "k8s://https://BCCFC2782CB4F39B0E313D89057B3546.gr7.us-east-1.eks.amazonaws.com:443"
        else {
            //code for local mode .Commented as we have to run with cluster
            builder = (SparkSessionBuilder) OptimizedSparkSession.builder().config("spark.master", "local[8]");
        }
        setKubernetesConfiguration();
        log.info("Kubernetes setup complete");

        //Capacity Plan
        new CapacityEstimator(builder).setConfigValue();
        log.info("Capacity Planned");

        // Some extra configuration might be required for Driver and Executor
        setDriverConfiguration();
        log.info("Driver setup complete");

        setExecutorConfiguration();
        log.info("Executor setup complete");

        //To-do
        setNetworkingConfiguration();
        log.info("Network setup complete");

        setSchedulingConfiguration();
        log.info("Scheduling setup complete");

        setShuffleConfiguration();
        log.info("Shuffle setup complete");

        setDynamicConfiguration();
        log.info("Dynamic Allocation setup complete");

        setThreadConfiguration();
        log.info("Thread setup complete");

        setExecutorMetrics();

        //set up Sql Parameters. We want to avoid running queries in hive and rely on Spark SQL.
        new SQLEstimator(builder).setConfigValue();

        setCompressionAndSerializationBehaviors();
        log.info("Compression and Serialization setup complete");

        new HadoopS3Estimator(builder).setConfigValue();
        log.info("Hadoop-AWS setup complete");

        //finally
        OptimizedSparkSession sparkSession = builder.getOrCreate();
        log.info("Initial Session built");

        log.info("-----------------------Spark Configs Begin--------------------------");
        sparkSession.conf().getAll();
        sparkSession.sparkContext().getConf().getAll();
        sparkSession.sqlContext().conf().getAllConfs();
        log.info("-----------------------Spark Configs End--------------------------");

        setLogger();
        return sparkSession;
    }



    /* ------------------------------------------------------------------------------------
     * Private methods
     * -------------------------------------------------------------------------------------*/

    private static void setKubernetesConfiguration(){
        //The desired context from your K8S config file used to configure the K8S client for
        // interacting with the cluster.  Useful if your config file has multiple clusters or
        // user identities defined.  The client library used locates the config file via the
        // KUBECONFIG environment variable or by defaulting to .kube/config under your home directory.
        // If not specified then your current context is used.  You can always override specific aspects
        // of the config file provided configuration using other Spark on K8S configuration options.
        //Ignore spark.kubernetes.context since we want to take this responsibility out of developers hand


        //The namespace that will be used for running the driver and executor pods.It is advisable to set up a namespace
        // to avoid confusion.Default value is default

        // Container image to use for Spark containers. Individual container types (e.g. driver or executor)
        // can also be configured to use different images if desired, by setting the container type-specific
        // image name.
        //I am expecting developers to put the container image from yaml.
        //Alternatively developer can set up driver and executor image separately for use.

        //Kubernetes image pull policy. Valid values are Always, Never, and IfNotPresent.
        //In production, it should always be set to IfNotPresent. In development, it should be set to Always.
        builder.config("spark.kubernetes.container.image.pullPolicy", "Always");

        // Max size limit for a config map. This is configurable as per
        // https://etcd.io/docs/v3.4.0/dev-guide/limit/ on k8s server end.
        builder.config("spark.kubernetes.configMap.maxSize", 1572864); //1.5MB

        //It is always advisable to set request timeout to be used in milliseconds for starting the driver
        builder.config("spark.kubernetes.submission.requestTimeout", 10000); //10sec

        //connection timeout to be used in milliseconds for starting the driver
        builder.config("spark.kubernetes.submission.connectionTimeout", 10000); //10sec

        //request timeout to be used in milliseconds for driver to request executors
        builder.config("spark.kubernetes.driver.requestTimeout", 10000); //10sec

        //connection timeout to be used in milliseconds for driver to request executors
        builder.config("spark.kubernetes.driver.connectionTimeout", 10000); //10sec

        // Service account that is used when running the driver pod. The driver pod uses this service account
        // when requesting executor pods from the API server. If specific credentials are given for the driver
        // pod to use, the driver will favor using those credentials instead.
        // serviceName should always be set up from command line
        builder.config("spark.kubernetes.authenticate.driver.serviceAccountName", SPARK);

        // Service account that is used when running the executor pod. If this parameter is not setup,
        // the fallback logic will use the driver's service account."


        //Specify the hard cpu limit for the driver pod
        // Driver should be used as orchestrator ONLY.
        builder.config("spark.kubernetes.driver.limit.cores", 8);

        //Specify the cpu request for the driver pod
        builder.config("spark.kubernetes.driver.request.cores", 5);

        //Specify the hard cpu limit for each executor pod. We never want to use more than 8 cores
        builder.config("spark.kubernetes.executor.limit.cores", 8);

        //Specify the cpu request for each executor pod. We will fix the number of cores to 5
        builder.config("spark.kubernetes.executor.request.cores", 5);


        builder.config("spark.kubernetes.executor.podNamePrefix", application);

        // Number of pods to launch at once in each round of executor allocation. Do not want to keep it
        // too low or too high
        builder.config("spark.kubernetes.allocation.batch.size", 5);

        // Time to wait between each round of executor allocation. Kept to default of 1s.
        builder.config("spark.kubernetes.allocation.batch.delay", "1s");

        // Time to wait before a newly created executor POD request, which does not reached the POD pending
        // state yet, considered timeout and will be deleted. Keeping it to default 600s
        builder.config("spark.kubernetes.allocation.executor.timeout", "600s");

        // In cluster mode, whether to wait for the application to finish before exiting the launcher process.
        builder.config("spark.kubernetes.submission.waitAppCompletion", true);

        // Interval between reports of the current app status in cluster mode. Increasing to 5s
        builder.config("spark.kubernetes.report.interval", "5s");

        // Interval between polls against the Kubernetes API server to inspect the state of executors.
        // Increasing it to a min. Not sure if this is the right thing to do.
        builder.config("spark.kubernetes.executor.apiPollingInterval", "60s");

        // Interval between successive inspection of executor events sent from the Kubernetes API.
        // Increasing it to 5s
        builder.config("spark.kubernetes.executor.eventProcessingInterval", "5s");

        // This sets the Memory Overhead Factor that will allocate memory to non-JVM jobs which in the case
        // of JVM tasks will default to 0.10 and 0.40 for non-JVM jobs"
        builder.config("spark.kubernetes.memoryOverheadFactor", 0.1);

        // Specify name of the ConfigMap,containing the HADOOP_CONF_DIR files,to be mounted on the driver and executors
        // for custom Hadoop configuration.

        //This sets the resource type internally. Valid Values are java, python, r
        builder.config("spark.kubernetes.resource.type", "java");

        // If set to true then emptyDir volumes created to back SPARK_LOCAL_DIRS will have their medium set
        // to Memory so that they will be created as tmpfs (i.e. RAM) backed volumes. This may improve
        // performance but scratch space usage will count towards your pods memory limit so you may wish to
        // request more memory. Setting up false for now.
        builder.config("spark.kubernetes.local.dirs.tmpfs", false);

        // Another interesting way create pod spec - File containing a template pod spec for executors

        // If set to false then executor pods will not be deleted in case of failure or normal termination.
        builder.config("spark.kubernetes.executor.deleteOnTermination", true);

        // How long to wait for executors to shut down gracefully before a forceful kill.
        builder.config("spark.kubernetes.dynamicAllocation.deleteGracePeriod", "5s");

        // Time to wait for graceful deletion of Spark pods when spark-submit is used for killing an application.
        builder.config("spark.kubernetes.appKillPodDeletionGracePeriod", "5s");

        // If set to true, all containers in the executor pod will be checked when reporting executor status.
        builder.config("spark.kubernetes.executor.checkAllContainers", true);

        // When a registered executor's POD is missing from the Kubernetes API server's polled
        // list of PODs then this delta time is taken as the accepted time difference between the
        // registration time and the time of the polling. After this time the POD is considered
        // missing from the cluster and the executor will be removed.
        builder.config("spark.kubernetes.executor.missingPodDetectDelta", "30s");

        //Attaching volume now
        builder.config("spark.kubernetes.driver.volumes.persistentVolumeClaim.efs-pvc-mount-d.options.claimName", "efs-pvc");
        builder.config("spark.kubernetes.driver.volumes.persistentVolumeClaim.efs-pvc-mount-d.mount.path", "/opt/efs/spark");
        builder.config("spark.kubernetes.driver.volumes.persistentVolumeClaim.efs-pvc-mount-d.mount.subPath",SPARK);
        builder.config("spark.kubernetes.driver.volumes.persistentVolumeClaim.efs-pvc-mount-d.mount.readOnly",false);
        builder.config("spark.kubernetes.driver.volumes.persistentVolumeClaim.efs-pvc-mount-d.options.storageClass",MANUAL);

        builder.config("spark.kubernetes.executor.volumes.persistentVolumeClaim.efs-pvc-mount-e.options.claimName", "efs-pvc");
        builder.config("spark.kubernetes.executor.volumes.persistentVolumeClaim.efs-pvc-mount-e.mount.path", "/opt/efs/spark");
        builder.config("spark.kubernetes.executor.volumes.persistentVolumeClaim.efs-pvc-mount-e.mount.subPath",SPARK);
        builder.config("spark.kubernetes.executor.volumes.persistentVolumeClaim.efs-pvc-mount-e.mount.readOnly",false);
        builder.config("spark.kubernetes.executor.volumes.persistentVolumeClaim.efs-pvc-mount-e.options.storageClass",MANUAL);

        builder.config("spark.kubernetes.executor.volumes.persistentVolumeClaim.spark-local-dir-1.options.claimName", "efs-pvc-tmp");
        builder.config("spark.kubernetes.executor.volumes.persistentVolumeClaim.spark-local-dir-1.mount.path", "/opt/efs/spark/tmp");
        builder.config("spark.kubernetes.executor.volumes.persistentVolumeClaim.spark-local-dir-1.mount.readOnly", false);
        builder.config("spark.kubernetes.executor.volumes.persistentVolumeClaim.spark-local-dir-1.options.storageClass", MANUAL);


        // Support for Python
        // set '${PYSPARK_PYTHON.key}' and '${PYSPARK_DRIVER_PYTHON.key}' configurations
        // or $ENV_PYSPARK_PYTHON and $ENV_PYSPARK_DRIVER_PYTHON environment variables

        // Set ONLY if required Comma separated list of the Kubernetes secrets used to access private image registries.
    }

    private static void setDriverConfiguration(){
        //Details to be taken from Kubernetes PV

        // By default, Spark starts with no listeners but the one for WebUI. however we can change the default
        // behaviour using the spark.extraListeners setting. spark.extraListeners is a comma-separated list of
        // listener class names that are registered with Spark’s listener bus when SparkContext is initialized.

        builder//.config("spark.driver.log.persistToDfs.enabled", "true")
                //.config("spark.driver.log.dfsDir", "s3a://...")
                //Base directory in which Spark driver logs are synced
                .config("spark.history.fs.driverlog.cleaner.enabled", "true")
                .config("spark.history.fs.driverlog.cleaner.maxAge", "2d");

        //Run time configuration
        builder//.config("spark.driver.extraClassPath", "??")
                //Depends on the code base which will using these configs
                //such as any dependent jars, which need to be called at runtime
                //.config("spark.driver.defaultJavaOptions", "??")
                .config("spark.driver.extraJavaOptions", "-XX:+UseG1GC -XX:+UnlockDiagnosticVMOptions -XX:+G1SummarizeConcMark " +
                        "-XX:InitiatingHeapOccupancyPercent=35 -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps " +
                        "-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/efs/spark/logs/dumps "+
                        "-XX:OnOutOfMemoryError='kill -9 %p'");

        log.debug("Driver Configuration: {}", builder);
    }

    private static void setExecutorConfiguration(){


        // Sets the number of latest rolling log files that are going to be retained by the system.
        // Older log files will be deleted. Disabled by default.
        builder.config("spark.executor.logs.rolling.maxRetainedFiles", 5);

        // Enable executor log compression. If it is enabled, the rolled executor logs will be compressed.
        // Disabled by default.
        builder.config("spark.executor.logs.rolling.enableCompression", false);

        // Set the max size of the file in bytes by which the executor logs will be rolled over.
        // Rolling is disabled by default. See <code>spark.executor.logs.rolling.maxRetainedFiles</code>
        // for automatic cleaning of old logs.
        builder.config("spark.executor.logs.rolling.maxSize", "10m");

        // Set the strategy of rolling of executor logs. By default it is disabled. It can
        // be set to "time" (time-based rolling) or "size" (size-based rolling). For "time",
        // use <code>spark.executor.logs.rolling.time.interval</code> to set the rolling interval.
        // For "size", use <code>spark.executor.logs.rolling.maxSize</code> to set
        // the maximum file size for rolling.
        builder.config("spark.executor.logs.rolling.strategy", "size");

        // Add the environment variable specified by <code>EnvironmentVariableName</code> to the Executor
        // process. The user can specify multiple of these to set multiple environment variables.

        builder.config("spark.executor.extraJavaOptions","-XX:+UseG1GC -XX:+UnlockDiagnosticVMOptions -XX:+G1SummarizeConcMark " +
                "-XX:InitiatingHeapOccupancyPercent=35 -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps " +
                "-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/efs/spark/logs/dumps "+
                "-XX:OnOutOfMemoryError='kill -9 %p'");

        log.debug("Executor Configuration: {}", builder);
    }

    private static void setCompressionAndSerializationBehaviors() {
        // Whether to compress broadcast variables before sending them. Generally a good idea.
        // Compression will use <code>spark.io.compression.codec</code>.
        builder.config("spark.broadcast.compress", true);

        //To disable autobroadcast join
        builder.config("spark.sql.autoBroadcastJoinThreshold", -1);

        // Whether to compress RDD checkpoints. Generally a good idea.
        // Compression will use <code>spark.io.compression.codec</code>.
        builder.config("spark.checkpoint.compress", true);

        // The codec used to compress internal data such as RDD partitions, event log, broadcast variables
        // and shuffle outputs. By default, Spark provides four codecs: <code>lz4</code>, <code>lzf</code>,
        // <code>snappy</code>, and <code>zstd</code>. You can also use fully qualified class names to
        // specify the codec, e.g.
        //    <code>org.apache.spark.io.LZ4CompressionCodec</code>,
        //    <code>org.apache.spark.io.LZFCompressionCodec</code>,
        //    <code>org.apache.spark.io.SnappyCompressionCodec</code>, and
        //    <code>org.apache.spark.io.ZStdCompressionCodec</code>.
        // Based on codec - other configuration can be set up. For now, I have setup per Snappy
        builder.config("spark.io.compression.codec", "org.apache.spark.io.SnappyCompressionCodec");

        // Block size in Snappy compression, in the case when Snappy compression codec is used.
        // Lowering this block size will also lower shuffle memory usage when Snappy is used.
        // Default unit is bytes, unless otherwise specified.

        builder.config("spark.io.compression.snappy.blockSize", "32k");

        // If you use Kryo serialization, give a comma-separated list of custom class names to register
        // with Kryo. See the <a href="tuning.html#data-serialization">tuning guide</a> for more details.

        // Whether to track references to the same object when serializing data with Kryo, which is
        // necessary if your object graphs have loops and useful for efficiency if they contain multiple
        // copies of the same object. Can be disabled to improve performance if you know this is not the case.
        builder.config("spark.kryo.referenceTracking", false);

        // Whether to require registration with Kryo. If set to 'true', Kryo will throw an exception if an
        // unregistered class is serialized. If set to false (the default), Kryo will write unregistered
        // class names along with each object. Writing class names can cause significant performance overhead,
        // so enabling this option can enforce strictly that a user has not omitted classes from registration.
        builder.config("spark.kryo.registrationRequired", false);

        // If you use Kryo serialization, give a comma-separated list of classes that register your custom classes with
        //Kryo.This property is useful if you need to register your classes in a custom way, e.g. to specify a custom
        // field serializer. Otherwise <code>spark.kryo.classesToRegister</code> is simpler.It should be set to classes
        // that extend <a href="api/scala/org/apache/spark/serializer/KryoRegistrator.html"> <code>KryoRegistrator
        // </code></a>. See the <a href="tuning.html#data-serialization">tuning guide</a> for more details.

        //Whether to use unsafe based Kryo serializer. Can be substantially faster by using Unsafe Based IO.
        builder.config("spark.kryo.unsafe", true);

        // Maximum allowable size of Kryo serialization buffer, in MiB unless otherwise specified.
        // This must be larger than any object you attempt to serialize and must be less than 2048m.
        // Increase this if you get a "buffer limit exceeded" exception inside Kryo.
        builder.config("spark.kryoserializer.buffer.max", "1024m");

        // Initial size of Kryo serialization buffer, in KiB unless otherwise specified. Note that there
        // will be one buffer <i>per core</i> on each worker. This buffer will grow up to
        // <code>spark.kryoserializer.buffer.max</code> if needed.
        builder.config("spark.kryoserializer.buffer", "8m");

        // Whether to compress serialized RDD partitions (e.g. for <code>StorageLevel.MEMORY_ONLY_SER</code> in Java
        // and Scala or <code>StorageLevel.MEMORY_ONLY</code> in Python). Can save substantial space at the cost
        // of some extra CPU time. Compression will use <code>spark.io.compression.codec</code>.
        builder.config("spark.rdd.compress", true);

        // Class to use for serializing objects that will be sent over the network or need to be cached
        // in serialized form. The default of Java serialization works with any Serializable Java object
        // but is quite slow, so we recommend <a href="tuning.html">using
        // <code>org.apache.spark.serializer.KryoSerializer</code> and configuring Kryo serialization</a>
        // when speed is necessary. Can be any subclass of
        // <a href="api/scala/org/apache/spark/serializer/Serializer.html">
        // <code>org.apache.spark.Serializer</code></a>.
        builder.config("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

        // When serializing using org.apache.spark.serializer.JavaSerializer, the serializer caches
        // objects to prevent writing redundant data, however that stops garbage collection of those
        // objects. By calling 'reset' you flush that info from the serializer, and allow old
        // objects to be collected. To turn off this periodic reset set it to -1.
        // By default it will reset the serializer every 100 objects.
        builder.config("spark.serializer.objectStreamReset", 100);

        log.debug("Compression and Serializer Config: {}", builder);
    }

    private static void setShuffleConfiguration() {
        // Maximum size of map outputs to fetch simultaneously from each reduce task, in MiB unless
        // otherwise specified. Since each output requires us to create a buffer to receive it, this
        // represents a fixed memory overhead per reduce task, so keep it small unless you have a
        // large amount of memory.
        builder.config("spark.reducer.maxSizeInFlight", "48m");

        // This configuration limits the number of remote requests to fetch blocks at any given point.
        // When the number of hosts in the cluster increase, it might lead to very large number
        // of inbound connections to one or more nodes, causing the workers to fail under load.
        // By allowing it to limit the number of fetch requests, this scenario can be mitigated.
        builder.config("spark.reducer.maxReqsInFlight", Int.MaxValue());

        // This configuration limits the number of remote blocks being fetched per reduce task from a
        // given host port. When a large number of blocks are being requested from a given address in a
        // single fetch or simultaneously, this could crash the serving executor or Node Manager. This
        // is especially useful to reduce the load on the Node Manager when external shuffle is enabled.
        // You can mitigate this issue by setting it to a lower value.
        builder.config("spark.reducer.maxBlocksInFlightPerAddress", Int.MaxValue());

        // Whether to compress map output files. Generally a good idea. Compression will use
        //                <code>spark.io.compression.codec</code>.
        builder.config("spark.shuffle.compress", true);

        // Size of the in-memory buffer for each shuffle file output stream, in KiB unless otherwise
        // specified. These buffers reduce the number of disk seeks and system calls made in creating
        // intermediate shuffle files.
        builder.config("spark.shuffle.file.buffer", "128m");

        // (Netty only) Fetches that fail due to IO-related exceptions are automatically retried if this is
        // set to a non-zero value. This retry logic helps stabilize large shuffles in the face of long GC
        // pauses or transient network connectivity issues.
        builder.config("spark.shuffle.io.maxRetries", "3");

        // (Netty only) Connections between hosts are reused in order to reduce connection buildup for
        // large clusters. For clusters with many hard disks and few hosts, this may result in insufficient
        // concurrency to saturate all disks, and so users may consider increasing this value.
        builder.config("spark.shuffle.io.numConnectionsPerPeer", 1);

        // (Netty only) Off-heap buffers are used to reduce garbage collection during shuffle and cache
        // block transfer. For environments where off-heap memory is tightly limited, users may wish to
        // turn this off to force all allocations from Netty to be on-heap.
        builder.config("spark.shuffle.io.preferDirectBufs", "true");

        // (Netty only) How long to wait between retries of fetches. The maximum delay caused by retrying
        // is 15 seconds by default, calculated as <code>maxRetries * retryWait</code>.
        builder.config("spark.shuffle.io.retryWait", "5s");

        // Length of the accept queue for the shuffle service. For large applications, this value may
        // need to be increased, so that incoming connections are not dropped if the service cannot keep
        // up with a large number of connections arriving in a short period of time. This needs to
        // be configured wherever the shuffle service itself is running, which may be outside of the
        // application (see <code>spark.shuffle.service.enabled</code> option below). If set below 1,
        // will fallback to OS default defined by Netty's <code>io.netty.util.NetUtil#SOMAXCONN</code>.
        builder.config("spark.shuffle.io.backLog", -1);

        // Enables the external shuffle service. This service preserves the shuffle files written by
        // executors so the executors can be safely removed. The external shuffle service must be set
        // up in order to enable it. See <a href="job-scheduling.html#configuration-and-setup">dynamic allocation
        // configuration and setup documentation</a> for more information.
        builder.config("spark.shuffle.service.enabled", "false");

        //Port on which the external shuffle service will run.
        builder.config("spark.shuffle.service.port", 7737);

        //Cache entries limited to the specified memory footprint, in bytes unless otherwise specified.
        // The max number of chunks allowed to be transferred at the same time on shuffle service.
        // Note that new incoming connections will be closed when the max number is hit. The client will
        // retry according to the shuffle retry configs (see <code>spark.shuffle.io.maxRetries</code> and
        // <code>spark.shuffle.io.retryWait</code>), if those limits are reached the task will fail with
        // fetch failure.
        builder.config("spark.shuffle.maxChunksBeingTransferred", Long.MAX_VALUE);

        // (Advanced) In the sort-based shuffle manager, avoid merge-sorting data if there is no
        // map-side aggregation and there are at most this many reduce partitions.
        builder.config("spark.shuffle.sort.bypassMergeThreshold", 200);

        // Whether to compress data spilled during shuffles. Compression will use <code>spark.io.compression.codec</code>.
        builder.config("spark.shuffle.spill.compress", true);
        builder.config("spark.shuffle.spill.compress", true);

        //Threshold in bytes above which the size of shuffle blocks in HighlyCompressedMapStatus is accurately
        // recorded. This helps to prevent OOM by avoiding underestimating shuffle block size when fetch shuffle blocks.
        builder.config("spark.shuffle.accurateBlockThreshold", 100*1024*(long)1024);

        //Timeout in milliseconds for registration to the external shuffle service.
        builder.config("spark.shuffle.registration.timeout", 200);

        //When we fail to register to the external shuffle service, we will retry for maxAttempts times.
        builder.config("spark.shuffle.registration.maxAttempts", 3);

        log.debug("Shuffle Config: {}",builder);
    }

    private static void setExecutorMetrics() {
        // Whether to write per-stage peaks of executor metrics (for each executor) to the event log.
        // Note: The metrics are polled (collected) and sent in the executor heartbeat,
        //        and this is always done; this configuration is only to determine if aggregated metric peaks
        //        are written to the event log.
        builder.config("spark.eventLog.logStageExecutorMetrics", true);

        // Whether to collect process tree metrics (from the /proc filesystem) when collecting executor metrics.
        // Note: The process tree metrics are collected only if the /proc filesystem exists.
        builder.config("spark.executor.processTreeMetrics.enabled", true);

        // How often to collect executor metrics (in milliseconds). If 0, the polling is done on executor
        // heartbeats (thus at the heartbeat interval, specified by <code>spark.executor.heartbeatInterval</code>).
        // If positive, the polling is done at this interval.
        log.debug("Metrics config: {}", builder);
    }

    /*
    Depending on jobs and cluster configurations, we can set number of threads in several places in Spark to utilize
    available resources efficiently to get better performance. Prior to Spark 3.0, these thread configurations apply
    to all roles of Spark, such as driver, executor, worker and master. From Spark 3.0, we can configure threads in
    finer granularity starting from driver and executor. Take RPC module as example in below table. For other modules,
    like shuffle, just replace "rpc" with "shuffle" in the property names except
    <code>spark.{driver|executor}.rpc.netty.dispatcher.numThreads</code>, which is only for RPC module.
    The default value for number of thread-related config keys is the minimum of the number of cores requested for the
    driver or executor, or, in the absence of that value, the number of cores available for the JVM (with a hardcoded
    upper limit of 8).
     */
    private static void setThreadConfiguration() {
        int sparkRpcIOThreads = 2;
        builder.config("spark.rpc.io.threads", sparkRpcIOThreads);
        builder.config("spark.rpc.io.serverThreads", sparkRpcIOThreads);
        builder.config("spark.rpc.io.clientThreads", sparkRpcIOThreads);
        builder.config("spark.rpc.netty.dispatcher.numThreads", sparkRpcIOThreads);


        int thread = 2; //get the value of minimum of the number of cores requested for the driver or executor
        //Number of threads used in the server thread pool
        //Fall back on <code>spark.rpc.io.serverThreads</code>
        builder.config("spark.executor.rpc.io.serverThreads", thread);
        builder.config("spark.driver.rpc.io.serverThreads", thread);

        builder.config("spark.executor.shuffle.io.serverThreads", thread);
        builder.config("spark.driver.shuffle.io.serverThreads", thread);

        //Number of threads used in the client thread pool
        //Fall back on spark.rpc.io.clientThreads
        builder.config("spark.executor.rpc.io.clientThreads", thread);
        builder.config("spark.driver.rpc.io.clientThreads", thread);

        builder.config("spark.executor.shuffle.io.clientThreads", thread);
        builder.config("spark.driver.shuffle.io.clientThreads", thread);

        //set shuffle partitions
        builder.config("spark.sql.shuffle.partitions", 300);


        //Number of threads used in RPC message dispatcher thread pool
        //Fall back on spark.rpc.netty.dispatcher.numThreads
        builder.config("spark.executor.rpc.netty.dispatcher.numThreads", thread);
        builder.config("spark.driver.rpc.netty.dispatcher.numThreads", thread);

    }

    private static void setDynamicConfiguration() {
        // Whether to use dynamic resource allocation, which scales the number of executors registered
        // with this application up and down based on the workload.
        // For more detail, see the description <a href="job-scheduling.html#dynamic-resource-allocation">here</a>.
        // This requires    <code>spark.shuffle.service.enabled</code> or
        //                  <code>spark.dynamicAllocation.shuffleTracking.enabled</code> to be set.
        // The following configurations are also relevant:
        //                  <code>spark.dynamicAllocation.minExecutors</code>,
        //                  <code>spark.dynamicAllocation.maxExecutors</code>, and
        //                  <code>spark.dynamicAllocation.initialExecutors</code>
        //                  <code>spark.dynamicAllocation.executorAllocationRatio</code>

        // If dynamic allocation is enabled and an executor has been idle for more than this duration,
        // the executor will be removed. For more detail, see this
        // <a href="job-scheduling.html#resource-allocation-policy">description</a>.
        builder.config("spark.dynamicAllocation.executorIdleTimeout", "60s");

        // If dynamic allocation is enabled and an executor which has cached data blocks has been idle for more than
        // this duration, the executor will be removed. For more details, see this
        // <a href="job-scheduling.html#resource-allocation-policy">description</a>.
        builder.config("spark.dynamicAllocation.cachedExecutorIdleTimeout", "600s");

        // Initial number of executors to run if dynamic allocation is enabled.
        // If `--num-executors` (or `spark.executor.instances`) is set and larger than this value, it will
        // be used as the initial number of executors.
        // default it to spark.dynamicAllocation.minExecutors

        //Upper bound for the number of executors if dynamic allocation is enabled.

        //Lower bound for the number of executors if dynamic allocation is enabled.
        builder.config("spark.dynamicAllocation.minExecutors", 1);

        // By default, the dynamic allocation will request enough executors to maximize the parallelism
        // according to the number of tasks to process. While this minimizes the latency of the job, with
        // small tasks this setting can waste a lot of resources due to executor allocation overhead, as
        // some executor might not even do any work. This setting allows to set a ratio that will be used
        // to reduce the number of executors w.r.t. full parallelism.
        // Defaults to      1.0 to give maximum parallelism.
        //                  0.5 will divide the target number of executors by 2
        // The target number of executors computed by the dynamicAllocation can still be overridden by the
        //                  <code>spark.dynamicAllocation.minExecutors</code> and
        //                  <code>spark.dynamicAllocation.maxExecutors</code> settings
        builder.config("spark.dynamicAllocation.executorAllocationRatio", 1.0);

        // If dynamic allocation is enabled and there have been pending tasks backlogged for more than
        // this duration, new executors will be requested. For more detail, see this
        // <a href="job-scheduling.html#resource-allocation-policy">description</a>.
        builder.config("spark.dynamicAllocation.schedulerBacklogTimeout", "1s");

        // Same as <code>spark.dynamicAllocation.schedulerBacklogTimeout</code>, but used only for
        // subsequent executor requests. For more detail, see this
        // <a href="job-scheduling.html#resource-allocation-policy">description</a>.
        builder.config("spark.dynamicAllocation.sustainedSchedulerBacklogTimeout", "1s");

        // Experimental. Enables shuffle file tracking for executors, which allows dynamic allocation
        // without the need for an external shuffle service. This option will try to keep alive executors
        // that are storing shuffle data for active jobs.
        builder.config("spark.dynamicAllocation.shuffleTracking.enabled", true);

        // When shuffle tracking is enabled, controls the timeout for executors that are holding shuffle
        // data. The default value means that Spark will rely on the shuffles being garbage collected to be
        // able to release executors. If for some reason garbage collection is not cleaning up shuffles
        // quickly enough, this option can be used to control when to time out executors even when they are
        // storing shuffle data. Setting it to 10minutes for now
        builder.config("spark.dynamicAllocation.shuffleTracking.timeout", "600s");

        log.debug("Dynamic config: {}",builder);

    }


    //Since this class is really a canvas implementation Logger should actually be set per the calling class.
    private static void setLogger(){
        //You have the application name already present.
    }

    private static void setNetworkingConfiguration(){
        // Maximum message size (in MiB) to allow in "control plane" communication; generally only applies to map
        // output size information sent between executors and the driver. Increase this if you are running
        // jobs with many thousands of map and reduce tasks and see messages about the RPC message size.
        builder.config("spark.rpc.message.maxSize", 128);

        // Port for all block managers to listen on. These exist on both the driver and the executors.
        String blockManagerPort = "0"; //get the value from Cluster configuration

        builder.config("spark.blockManager.port", blockManagerPort);

        // Driver-specific port for the block manager to listen on, for cases where it cannot use the same
        // configuration as executors.
        builder.config("spark.driver.blockManager.port", blockManagerPort);

        // Hostname or IP address for the driver.
        // This is used for communicating with the executors and the standalone Master.
        // String sparkDriverHost = "nvwdsr005063372.intranet.barcapint.com"; //get the value from Cluster Configuration
        // Hostname or IP address where to bind listening sockets. This config overrides the SPARK_LOCAL_IP
        // environment variable (see below).
        // It also allows a different address from the local one to be advertised to executors or external systems.
        // This is useful, for example, when running containers with bridged networking. For this to properly work,
        // the different ports used by the driver (RPC, block manager and UI) need to be forwarded from the
        // container's host.

        // commented because giving error as port should be between 1024 and 65535


        // Length of the accept queue for the RPC server. For large applications, this value may
        // need to be increased, so that incoming connections are not dropped when a large number of
        // connections arrives in a short period of time.

        builder.config("spark.rpc.io.backLog", "256");

        // Default timeout for all network interactions. This config will be used in place of
        //    spark.storage.blockManagerHeartbeatTimeoutMs,
        //    spark.shuffle.io.connectionTimeout,
        //    spark.rpc.askTimeout
        //    spark.rpc.lookupTimeout if they are not configured.
        builder.config("spark.network.timeout", "120s");

        // If enabled then off-heap buffer allocations are preferred by the shared allocators.
        // Off-heap buffers are used to reduce garbage collection during shuffle and cache
        // block transfer. For environments where off-heap memory is tightly limited, users may wish to
        // turn this off to force all allocations to be on-heap.
        builder.config("spark.network.io.preferDirectBufs", true);

        // Maximum number of retries when binding to a port before giving up.
        // When a port is given a specific value (non 0), each subsequent retry will
        // increment the port used in the previous attempt by 1 before retrying. This essentially allows
        // it to try a range of ports from the start port specified to port + maxRetries.
        builder.config("spark.port.maxRetries", 16);

        //Number of times to retry before an RPC task gives up. RPC task will run at most times of this number.
        builder.config("spark.rpc.numRetries", 3);

        //Duration for an RPC ask operation to wait before retrying.
        builder.config("spark.rpc.retry.wait", "3s");

        //Duration for an RPC ask operation to wait before timing out. Typically should be same as spark.network.timeout
        builder.config("spark.rpc.askTimeout", "120s");

        //Duration for an RPC remote endpoint lookup operation to wait before timing out.
        builder.config("spark.rpc.lookupTimeout", "120s");

        // Remote block will be fetched to disk when size of the block is above this threshold
        // in bytes. This is to avoid a giant request takes too much memory. Note this
        // configuration will affect both shuffle fetch and block manager remote block fetch.
        // For users who enabled external shuffle service, this feature can only work when
        // external shuffle service is at least 2.3.0.
        builder.config("spark.network.maxRemoteBlockSizeFetchToMem", "200m");

        log.debug("Network config: {}",builder);
    }

    private static void setSchedulingConfiguration() {
        // When running on a <a href="spark-standalone.html">standalone deploy cluster</a> or a
        // <a href="running-on-mesos.html#mesos-run-modes">Mesos cluster in "coarse-grained"
        // sharing mode</a>, the maximum amount of CPU cores to request for the application from
        // across the cluster (not from each machine). If not set, the default will be
        // <code>spark.deploy.defaultCores</code> on Spark's standalone cluster manager, or
        // infinite (all available cores) on Mesos.
        builder.config("spark.cores.max", 5);

        // How long to wait to launch a data-local task before giving up and launching it
        // on a less-local node. The same wait will be used to step through multiple locality levels
        // (process-local, node-local, rack-local and then any). It is also possible to customize the
        // waiting time for each level by setting <code>spark.locality.wait.node</code>, etc.
        // You should increase this setting if your tasks are long and see poor locality, but the
        //  default usually works well.
        builder.config("spark.locality.wait", "3s");

        // Customize the locality wait for node locality. For example, you can set this to 0 to skip
        // node locality and search immediately for rack locality (if your cluster has rack information).
        builder.config("spark.locality.wait.node", "3s");

        // Customize the locality wait for process locality. This affects tasks that attempt to access
        // cached data in a particular executor process.
        builder.config("spark.locality.wait.process", "3s");

        // Customize the locality wait for rack locality.
        builder.config("spark.locality.wait.rack", "3s");

        // Maximum amount of time to wait for resources to register before scheduling begins.
        builder.config("spark.scheduler.maxRegisteredResourcesWaitingTime", "30s");

        // The minimum ratio of registered resources (registered resources / total expected resources)
        // (resources are executors in yarn mode and Kubernetes mode, CPU cores in standalone mode and
        // Mesos coarse-grained mode['spark.cores.max' value is total expected resources for Mesos coarse-grained mode])
        // to wait for before scheduling begins. Specified as a double between 0.0 and 1.0.
        // Regardless of whether the minimum ratio of resources has been reached,
        // the maximum amount of time it will wait before scheduling begins is controlled by config
        // <code>spark.scheduler.maxRegisteredResourcesWaitingTime</code>.
        builder.config("spark.scheduler.minRegisteredResourcesRatio", 0.8);

        // The <a href="job-scheduling.html#scheduling-within-an-application">scheduling mode</a> between
        // jobs submitted to the same SparkContext. Can be set to <code>FAIR</code>
        // to use fair sharing instead of queueing jobs one after another. Useful for multi-user services.
        builder.config("spark.scheduler.mode", "FIFO");

        //The interval length for the scheduler to revive the worker resource offers to run tasks.
        builder.config("spark.scheduler.revive.interval", "1s");

        // The default capacity for event queues. Spark will try to initialize an event queue
        // using capacity specified by `spark.scheduler.listenerbus.eventqueue.queueName.capacity`
        // first. If it's not configured, Spark will use the default capacity specified by this
        // config. Note that capacity must be greater than 0. Consider increasing value (e.g. 20000)
        // if listener events are dropped. Increasing this value may result in the driver using more memory.
        builder.config("spark.scheduler.listenerbus.eventqueue.capacity", 10000);

        // Capacity for shared event queue in Spark listener bus, which hold events for external listener(s)
        // that register to the listener bus. Consider increasing value, if the listener events corresponding
        // to shared queue are dropped. Increasing this value may result in the driver using more memory.
        builder.config("spark.scheduler.listenerbus.eventqueue.shared.capacity", 10000);

        // Capacity for appStatus event queue, which hold events for internal application status listeners.
        // Consider increasing value, if the listener events corresponding to appStatus queue are dropped.
        // Increasing this value may result in the driver using more memory.
        builder.config("spark.scheduler.listenerbus.eventqueue.appStatus.capacity", 10000);

        // Capacity for executorManagement event queue in Spark listener bus, which hold events for internal
        // executor management listeners. Consider increasing value if the listener events corresponding to
        // executorManagement queue are dropped. Increasing this value may result in the driver using more memory.
        builder.config("spark.scheduler.listenerbus.eventqueue.executorManagement.capacity", 10000);

        // Capacity for eventLog queue in Spark listener bus, which hold events for Event logging listeners
        // that write events to eventLogs. Consider increasing value if the listener events corresponding to eventLog
        // queue are dropped. Increasing this value may result in the driver using more memory.
        builder.config("spark.scheduler.listenerbus.eventqueue.eventLog.capacity", 10000);

        // If set to "true", Spark will merge ResourceProfiles when different profiles are specified
        // in RDDs that get combined into a single stage. When they are merged, Spark chooses the maximum of
        // each resource and creates a new ResourceProfile. The default of false results in Spark throwing
        // an exception if multiple different ResourceProfiles are found in RDDs going into the same stage.
        builder.config("spark.scheduler.resource.profileMergeConflicts", false);

        // The timeout in seconds to wait to acquire a new executor and schedule a task before aborting a
        // TaskSet which is unschedulable because all executors are excluded due to task failures.
        builder.config("spark.scheduler.excludeOnFailure.unschedulableTaskSetTimeout", "120s");

        // If set to "true", prevent Spark from scheduling tasks on executors that have been excluded
        // due to too many task failures. The algorithm used to exclude executors and nodes can be further
        // controlled by the other "spark.excludeOnFailure" configuration options.
        builder.config("spark.excludeOnFailure.enabled", "true");

        // If set to "true", performs speculative execution of tasks. This means if one or more tasks are
        // running slowly in a stage, they will be re-launched.
        // Keep spark speculation as false
        builder.config("spark.speculation", "false");

        // How often Spark will check for tasks to speculate.
        builder.config("spark.speculation.interval", "5s");

        // How many times slower a task is than the median to be considered for speculation.
        builder.config("spark.speculation.multiplier", "1.5");

        // Fraction of tasks which must be complete before speculation is enabled for a particular stage.
        builder.config("spark.speculation.quantile", "0.75");

        // Task duration after which scheduler would try to speculative run the task. If provided, tasks
        // would be speculatively run if current stage contains less tasks than or equal to the number of
        // slots on a single executor and the task is taking longer time than the threshold. This config
        // helps speculate stage with very few tasks. Regular speculation configs may also apply if the
        // executor slots are large enough. E.g. tasks might be re-launched if there are enough successful
        // runs even though the threshold hasn't been reached. The number of slots is computed based on
        // the conf values of spark.executor.cores and spark.task.cpus minimum 1.
        // Default unit is bytes, unless otherwise specified.
        builder.config("spark.speculation.task.duration.threshold", "10s");

        // Number of cores to allocate for each task.
        builder.config("spark.task.cpus", 1);

        // Number of failures of any particular task before giving up on the job.
        // The total number of failures spread across different tasks will not cause the job
        // to fail; a particular task has to fail this number of attempts.
        // Should be greater than or equal to 1. Number of allowed retries = this value - 1.
        builder.config("spark.task.maxFailures", 4);

        // Enables monitoring of killed / interrupted tasks. When set to true, any task which is killed
        // will be monitored by the executor until that task actually finishes executing. See the other
        // <code>spark.task.reaper.*</code> configurations for details on how to control the exact behavior
        // of this monitoring. When set to false (the default), task killing will use an older code
        // path which lacks such monitoring.
        builder.config("spark.task.reaper.enabled", true);

        // When <code>spark.task.reaper.enabled = true</code>, this setting controls the frequency at which
        // executors will poll the status of killed tasks. If a killed task is still running when polled
        // then a warning will be logged and, by default, a thread-dump of the task will be logged
        // (this thread dump can be disabled via the <code>spark.task.reaper.threadDump</code> setting,
        // which is documented below).
        builder.config("spark.task.reaper.pollingInterval", "20s");

        // When <code>spark.task.reaper.enabled = true</code>, this setting controls whether task thread
        // dumps are logged during periodic polling of killed tasks. Set this to false to disable
        // collection of thread dumps.
        builder.config("spark.task.reaper.threadDump", true);

        // When <code>spark.task.reaper.enabled = true</code>, this setting specifies a timeout after
        // which the executor JVM will kill itself if a killed task has not stopped running. The default
        // value, -1, disables this mechanism and prevents the executor from self-destructing. The purpose
        // of this setting is to act as a safety-net to prevent runaway noncancellable tasks from rendering
        // an executor unusable.
        builder.config("spark.task.reaper.killTimeout", -1);

        // Number of consecutive stage attempts allowed before a stage is aborted.
        builder.config("spark.stage.maxConsecutiveAttempts", 4);

        log.debug("Network config: {}",builder);
    }
}