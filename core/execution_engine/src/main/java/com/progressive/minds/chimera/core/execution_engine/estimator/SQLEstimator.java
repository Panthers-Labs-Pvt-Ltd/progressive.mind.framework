package com.progressive.minds.chimera.core.execution_engine.estimator;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.spark.sql.SparkSession.Builder;
import org.apache.spark.unsafe.array.ByteArrayMethods;
import scala.Int;

public class SQLEstimator extends ConfEstimator {

    Logger logger =  LogManager.getLogger(SQLEstimator.class.getName());

    public SQLEstimator(Builder ctxBuilder) {
        super(ctxBuilder);
    }

    @Override
    void calculate() {
           /*
        to calculate
         */
    }

    @Override
    public void setConfigValue() {
        calculate();

        //spark catalog implementation
        //possible value "hive", "in-memory"
        builder.config("spark.sql.catalogImplementation","in-memory");

        // System preserved database should not exists in metastore. However it's hard to guarantee it
        // for every session, because case-sensitivity differs. Here we always lowercase it to make our
        // life easier.
        //.transform(_.toLowerCase(Locale.ROOT))
        // Default "global_temp"
        builder.config("spark.sql.globalTempDatabase", "global_temp");

        //"The max number of iterations the analyzer runs."
        builder.config("spark.sql.analyzer.maxIterations", 100);

        //Configures a list of rules to be disabled in the optimizer, in which the rules are specified by their
        // rule names and separated by comma. It is not guaranteed that all the rules in this configuration
        // will eventually be excluded, as some rules are necessary for correctness. The optimizer will
        // log the rules that have indeed been excluded.

        //The max number of iterations the optimizer runs.
        builder.config("spark.sql.optimizer.maxIterations", 100);

        //The threshold of set size for InSet conversion.
        builder.config("spark.sql.optimizer.inSetConversionThreshold", 10);

        //The maximum size of the cache that maps qualified table names to table relation plans.
        builder.config("spark.sql.filesourceTableRelationCacheSize", 1000);

        //When nonzero, enable caching of generated classes for operators and expressions.
        // All jobs share the cache that can use up to the specified number for generated classes.
        builder.config("spark.sql.codegen.cache.maxEntries", 100);

        //When true, put comment in the generated code. Since computing huge comments can be extremely expensive in
        // certain cases, such as deeply-nested expressions which operate over inputs with wide schemas, default is false.
        builder.config("spark.sql.codegen.comments", true);

        // When enabling the debug, Spark SQL internal table properties are not filtered out; however,
        // some related DDL commands (e.g., ANALYZE TABLE and CREATE TABLE LIKE) might not work properly.
        // Only used for internal debugging. Not all functions are supported when it is enabled.
        builder.config("spark.sql.debug", false);


        // The name of a class that implements org.apache.spark.sql.columnar.CachedBatchSerializer.
        // It will be used to translate SQL data into a format that can more efficiently be cached.
        // The underlying API is subject to change so use with caution. Multiple classes cannot be specified.
        // The class must have a no-arg constructor.
        builder.config("spark.sql.cache.serializer", "org.apache.spark.sql.execution.columnar.DefaultCachedBatchSerializer");

        // List of class names implementing QueryExecutionListener that will be automatically added to newly created sessions.
        // The classes should have either a no-arg constructor, or a constructor that expects a SparkConf argument.

        //Number of executions to retain in the Spark UI.
        builder.config("spark.sql.ui.retainedExecutions", 500);

        // The maximum degree of parallelism to fetch and broadcast the table. If we encounter memory issue like
        // frequently full GC or OOM when broadcast table we can decrease this number in order to reduce memory usage.
        // Notice the number should be carefully chosen since decreasing parallelism might cause longer waiting for other
        // broadcasting. Also, increasing parallelism may cause memory problem.
        builder.config("spark.sql.broadcastExchange.maxThreadThreshold", 128);

        //The maximum degree of parallelism to execute the subquery.
        builder.config("spark.sql.subquery.maxThreadThreshold", 16);

        //Threshold of SQL length beyond which it will be truncated before adding to event. Defaults to no truncation.
        // If set to 0, callsite will be logged instead.
        builder.config("spark.sql.event.truncate.length", Int.MaxValue());

        //Flag to revert to legacy behavior where a cloned SparkSession receives SparkConf defaults, dropping any
        // overrides in its parent SparkSession.
        builder.config("spark.sql.legacy.sessionInitWithConfigDefaults", false);

        //Time-to-live (TTL) value for the metadata caches: partition file metadata cache and session catalog cache.
        // This configuration only has an effect when this value having a positive value (> 0). It also requires setting
        // '${StaticSQLConf.CATALOG_IMPLEMENTATION.key}' to `hive`, setting
        // '${SQLConf.HIVE_FILESOURCE_PARTITION_FILE_CACHE_SIZE.key}' > 0 and
        // setting '${SQLConf.HIVE_MANAGE_FILESOURCE_PARTITIONS.key}' to `true` to be applied to the partition file metadata cache.
        builder.config("spark.sql.metadataCacheTTLSeconds", -1);

        //Configures the max set size in InSet for which Spark will generate code with switch statements.
        // This is applicable only to bytes, shorts, ints, dates.
        //The max set size for using switch statements in InSet must be non-negative and less than or equal to 600
        builder.config("spark.sql.optimizer.inSetSwitchThreshold", 400);

        //Configures the log level for logging the change from the original plan to the new " +
        //                        "plan after a rule or batch is applied. The value can be 'trace', 'debug', 'info', " +
        //                        "'warn', or 'error'. The default log level is 'trace'.
        builder.config("spark.sql.planChangeLog.level", "trace");


        //When true, we will generate predicate for partition column when it's used as join key
        builder.config("spark.sql.optimizer.dynamicPartitionPruning.enabled", true);

        //When true, distinct count statistics will be used for computing the data size of the partitioned table after
        // dynamic partition pruning, in order to evaluate if it is worth adding an extra subquery as the pruning filter
        // if broadcast reuse is not applicable.
        builder.config("spark.sql.optimizer.dynamicPartitionPruning.useStats", true);

        //When statistics are not available or configured not to be used, this config will be used as the fallback filter
        // ratio for computing the data size of the partitioned table after dynamic partition pruning, in order to evaluate
        // if it is worth adding an extra subquery as the pruning filter if broadcast reuse is not applicable.
        builder.config("spark.sql.optimizer.dynamicPartitionPruning.fallbackFilterRatio", (long) 0.5);

        //When true, dynamic partition pruning will only apply when the broadcast exchange of a broadcast hash join
        // operation can be reused as the dynamic pruning filter.
        builder.config("spark.sql.optimizer.dynamicPartitionPruning.reuseBroadcastOnly", true);

        //When set to true Spark SQL will automatically select a compression codec for each column based on statistics of the data.
        builder.config("spark.sql.inMemoryColumnarStorage.compressed", true);

        //Controls the size of batches for columnar caching.  Larger batch sizes can improve memory utilization and compression,
        // but risk OOMs when caching data.
        builder.config("spark.sql.inMemoryColumnarStorage.batchSize", 100000);

        //When true, enable partition pruning for in-memory columnar tables.
        builder.config("spark.sql.inMemoryColumnarStorage.partitionPruning", true);

        //When true, enable in-memory table scan accumulators.
        builder.config("spark.sql.inMemoryTableScanStatistics.enable", true);

        //Enables vectorized reader for columnar caching.
        builder.config("spark.sql.inMemoryColumnarStorage.enableVectorizedReader", true);

        //When true, use OffHeapColumnVector in ColumnarBatch.
        builder.config("spark.sql.columnVector.offheap.enabled", false);

        //When true, prefer sort merge join over shuffled hash join. Sort merge join consumes less memory than
        // shuffled hash join and it works efficiently when both join tables are large. On the other hand,
        // shuffled hash join can improve performance (e.g., of full outer joins) when one of join tables is ''
        // much smaller.
        builder.config("spark.sql.join.preferSortMergeJoin", true);

        //When true, enable use of radix sort when possible. Radix sort is much faster but requires additional
        // memory to be reserved up-front. The memory overhead may be significant when sorting very small
        // rows (up to 50% more in this case).
        builder.config("spark.sql.sort.enableRadixSort", true);

        //Configures the maximum size in bytes for a table that will be broadcast to all worker nodes when
        // performing a join.  By setting this value to -1 broadcasting can be disabled. Note that currently
        // statistics are only supported for Hive Metastore tables where the command
        // `ANALYZE TABLE <tableName> COMPUTE STATISTICS noscan` has been run, and file-based data source
        // tables where the statistics are computed directly on the files of data.
        builder.config("spark.sql.autoBroadcastJoinThreshold", 10485760); //10MB

        //Timeout in seconds for the broadcast wait time in broadcast joins
        builder.config("spark.sql.broadcastTimeout", 600);

        //Minimal increase rate in number of partitions between attempts when executing a take on a query.
        // Higher values lead to more partitions read. Lower values might lead to longer execution times
        // as more jobs will be run
        builder.config("spark.sql.limit.scaleUpFactor", 8);

        //The default number of partitions to use when shuffling data for joins or aggregations.
        // Note: For structured streaming, this configuration cannot be changed between query restarts
        // from the same checkpoint location.

        //When true, enable adaptive query execution, which re-optimizes the query plan in the middle of
        // query execution, based on accurate runtime statistics.
        builder.config("spark.sql.adaptive.enabled", true);

        //Adaptive query execution is skipped when the query does not have exchanges or sub-queries.
        // By setting this config to true (together with '${ADAPTIVE_EXECUTION_ENABLED.key}' set to true),
        // Spark will force apply adaptive query execution for all supported queries.
        builder.config("spark.sql.adaptive.forceApply", true);

        //Configures the log level for adaptive execution logging of plan changes. The value can be
        // 'trace', 'debug', 'info', 'warn', or 'error'. The default log level is 'debug'.
        builder.config("spark.sql.adaptive.logLevel", "debug");

        // The advisory size in bytes of the shuffle partition during adaptive optimization
        // (when ${ADAPTIVE_EXECUTION_ENABLED.key} is true). It takes effect when Spark coalesces small
        // shuffle partitions or splits skewed shuffle partition."
        builder.config("spark.sql.adaptive.advisoryPartitionSizeInBytes", 100*1024*(long)1024); //100 MB

        //s"When true and '${ADAPTIVE_EXECUTION_ENABLED.key}' is true, Spark will coalesce contiguous shuffle
        // partitions according to the target size (specified by '${ADVISORY_PARTITION_SIZE_IN_BYTES.key}'),
        // to avoid too many small tasks."
        builder.config("spark.sql.adaptive.coalescePartitions.enabled", true);

        // The suggested (not guaranteed) minimum number of shuffle partitions after coalescing.
        // If not set, the default value is the default parallelism of the Spark cluster.
        // This configuration only has an effect when '${ADAPTIVE_EXECUTION_ENABLED.key}' and
        // '${COALESCE_PARTITIONS_ENABLED.key}' are both true."
        // Do Not set. We have provided the advisoryPartitionSize
        builder.config("spark.sql.adaptive.coalescePartitions.minPartitionNum", 20);

        //"The initial number of shuffle partitions before coalescing. If not set, it equals to
        // ${SHUFFLE_PARTITIONS.key}. This configuration only has an effect when '${ADAPTIVE_EXECUTION_ENABLED.key}'
        // and '${COALESCE_PARTITIONS_ENABLED.key}' are both true.
        // .checkValue(_ > 0, "The initial number of partitions must be positive.")
        // Do not set. We have provided the advisoryPartitionSize
        builder.config("spark.sql.adaptive.coalescePartitions.initialPartitionNum", 20);

        //"Whether to fetch the contiguous shuffle blocks in batch. Instead of fetching blocks one by one,
        // fetching contiguous shuffle blocks for the same map task in batch can reduce IO and improve performance.
        // Note, multiple contiguous blocks exist in single fetch request only happen when
        // '${ADAPTIVE_EXECUTION_ENABLED.key}' and '${COALESCE_PARTITIONS_ENABLED.key}' are both true.
        // This feature also depends on a relocatable serializer, the concatenation support codec in use,
        // the new version shuffle fetch protocol and io encryption is disabled.
        builder.config("spark.sql.adaptive.fetchShuffleBlocksInBatch", true);

        //s"When true and '${ADAPTIVE_EXECUTION_ENABLED.key}' is true, Spark tries to use local shuffle reader to
        // read the shuffle data when the shuffle partitioning is not needed, "for example, after converting
        // sort-merge join to broadcast-hash join."
        builder.config("spark.sql.adaptive.localShuffleReader.enabled", true);

        //s"When true and '${ADAPTIVE_EXECUTION_ENABLED.key}' is true, Spark dynamically handles skew in sort-merge
        // join by splitting (and replicating if needed) skewed partitions."
        builder.config("spark.sql.adaptive.skewJoin.enabled", true);

        //"A partition is considered as skewed if its size is larger than this factor multiplying the median
        // partition size and also larger than 'spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes'"
        builder.config("spark.sql.adaptive.skewJoin.skewedPartitionFactor", 5);

        // A partition is considered as skewed if its size in bytes is larger than this threshold and also larger
        // than '${SKEW_JOIN_SKEWED_PARTITION_FACTOR.key}' multiplying the median partition size. Ideally this
        // config should be set larger than '${ADVISORY_PARTITION_SIZE_IN_BYTES.key}'."
        builder.config("spark.sql.adaptive.skewJoin.skewedPartitionThresholdInByte", 256*1024*(long)1024); //256 MB

        //"The relation with a non-empty partition ratio lower than this config will not be considered as the build
        // side of a broadcast-hash join in adaptive execution regardless of its size.This configuration only has
        // an effect when '${ADAPTIVE_EXECUTION_ENABLED.key}' is true.
        // .checkValue(_ >= 0, "The non-empty partition ratio must be positive number."
        builder.config("spark.sql.adaptive.nonEmptyPartitionRatioForBroadcastJoin", (long) 0.2);


        // A partition is considered as skewed if its size is larger than this factor multiplying the median
        // partition size and also larger than 'spark.sql.adaptive.skewJoin.skewedPartition ThresholdInBytes'

        //When true, common subexpressions will be eliminated
        builder.config("spark.sql.subexpressionElimination.enabled", true);

        //The maximum entries of the cache used for interpreted subexpression elimination."
        //.checkValue(_ >= 0, "The maximum must not be negative")
        builder.config("spark.sql.subexpressionElimination.cache.maxEntries", 100);

        //"Whether the query analyzer should be case sensitive or not. Default to case insensitive.
        // It is highly discouraged to turn on case sensitive mode."
        builder.config("spark.sql.caseSensitive", false);

        //"When true, the query optimizer will infer and propagate data constraints in the query plan to optimize
        // them. Constraint propagation can sometimes be computationally expensive for certain kinds of query plans
        // (such as those with a large number of predicates and aliases) which might negatively impact
        // overall runtime."
        builder.config("spark.sql.constraintPropagation.enabled", true);


        //When true, string literals (including regex patterns); remain escaped in our SQL  +
        //"parser. The default is false since Spark 2.0. Setting it to true can restore the behavior " +
        //"prior to Spark 2.0." //
        builder.config("spark.sql.parser.escapedStringLiterals", false);

        // When estimating the output data size of a table scan, multiply the file size with this factor as
        // the estimated data size, in case the data is compressed in the file and lead to a heavily
        // underestimated result.
        // .checkValue(_ > 0, "the value of fileCompressionFactor must be greater than 0" //
        builder.config("spark.sql.sources.fileCompressionFactor", (long) 1.0);

        //When true, the Parquet data source merges schemas collected from all data files, otherwise the
        // schema is picked from the summary file or a random data file if no summary file is available.
        builder.config("spark.sql.parquet.mergeSchema", false);

        //When true, we make assumption that all part-files of Parquet are consistent with  +
        //"summary files and we will ignore them when merging schema. Otherwise, if this is " +
        //"false, which is the default, we will merge all part-files. This should be considered " +
        //"as expert-only option, and shouldn't be enabled before knowing what it means exactly." //
        builder.config("spark.sql.parquet.respectSummaryFiles", false);

        //Some other Parquet-producing systems, in particular Impala and older versions of  +
        //"Spark SQL, do not differentiate between binary data and strings when writing out the " +
        //"Parquet schema. This flag tells Spark SQL to interpret binary data as a string to provide " +
        //"compatibility with these systems." //
        builder.config("spark.sql.parquet.binaryAsString", false);

        //Some Parquet-producing systems, in particular Impala, store Timestamp into INT96.  +
        //"Spark would also store Timestamp as INT96 because we need to avoid precision lost of the " +
        //"nanoseconds field. This flag tells Spark SQL to interpret INT96 data as a timestamp to " +
        //"provide compatibility with these systems." //
        builder.config("spark.sql.parquet.int96AsTimestamp", true);

        //This controls whether timestamp adjustments should be applied to INT96 data when  +
        //"converting to timestamps, for data written by Impala.  This is necessary because Impala " +
        //"stores INT96 data with a different timezone offset than Hive & Spark." //
        builder.config("spark.sql.parquet.int96TimestampConversion", true);

        //Sets which Parquet timestamp type to use when Spark writes data to Parquet files.  +
        //"INT96 is a non-standard but commonly used timestamp type in Parquet. TIMESTAMP_MICROS " +
        //"is a standard timestamp type in Parquet, which stores number of microseconds from the " +
        //"Unix epoch. TIMESTAMP_MILLIS is also standard, but with millisecond precision, which " +
        //"means Spark has to truncate the microsecond portion of its timestamp value

        //Sets the compression codec used when writing Parquet files. If either `compression` or  +
        //"`parquet.compression` is specified in the table-specific options/properties, the " +
        //"precedence would be `compression`, `parquet.compression`, " +
        //"`spark.sql.parquet.compression.codec`. Acceptable values include: none, uncompressed, " +
        builder.config("spark.sql.parquet.compression.codec", "snappy");

        //Enables Parquet filter push-down optimization when set to true. //
        builder.config("spark.sql.parquet.filterPushdown", true);

        //If true, enables Parquet filter push-down optimization for Date.  +
        //s"This configuration only has an effect when '${PARQUET_FILTER_PUSHDOWN_ENABLED.key}' is " +
        //"enabled." //
        builder.config("spark.sql.parquet.filterPushdown.date", true);

        //If true, enables Parquet filter push-down optimization for Timestamp.  +
        //s"This configuration only has an effect when '${PARQUET_FILTER_PUSHDOWN_ENABLED.key}' is " +
        //"enabled and Timestamp stored as TIMESTAMP_MICROS or TIMESTAMP_MILLIS type." //
        builder.config("spark.sql.parquet.filterPushdown.timestamp", true);

        //If true, enables Parquet filter push-down optimization for Decimal.  +
        //s"This configuration only has an effect when '${PARQUET_FILTER_PUSHDOWN_ENABLED.key}' is " +
        //"enabled." //
        builder.config("spark.sql.parquet.filterPushdown.decimal", true);

        //If true, enables Parquet filter push-down optimization for string startsWith function.  +
        //s"This configuration only has an effect when '${PARQUET_FILTER_PUSHDOWN_ENABLED.key}' is " +
        //"enabled." //
        builder.config("spark.sql.parquet.filterPushdown.string.startsWith", true);

        //The maximum number of values to filter push-down optimization for IN predicate.  +
        //"Large threshold won't necessarily provide much better performance. " +
        //"The experiment argued that 300 is the limit threshold. " +
        //"By setting this value to 0 this feature can be disabled. " +
        //s"This configuration only has an effect when '${PARQUET_FILTER_PUSHDOWN_ENABLED.key}' is " +
        //"enabled." .checkValue(threshold => threshold >= 0, "The threshold must not be negative.")//
        builder.config("spark.sql.parquet.pushdown.inFilterThreshold", 10);

        //If true, data will be written in a way of Spark 1.4 and earlier. For example, decimal  +
        //"values will be written in Apache Parquet's fixed-length byte array format, which other " +
        //"systems such as Apache Hive and Apache Impala use. If false, the newer format in Parquet " +
        //"will be used. For example, decimals will be written in int-based format. If Parquet " +
        //"output is intended for use with systems that do not support this newer format, set to true." //
        builder.config("spark.sql.parquet.writeLegacyFormat", false);


        //The output committer class used by Parquet. The specified class needs to be a  +
        //"subclass of org.apache.hadoop.mapreduce.OutputCommitter. Typically, it's also a subclass " +
        //"of org.apache.parquet.hadoop.ParquetOutputCommitter. If it is not, then metadata " +
        //"summaries will never be created, irrespective of the value of " +
        //"parquet.summary.metadata.level" to use S3A

        //Enables vectorized parquet decoding. //
        builder.config("spark.sql.parquet.enableVectorizedReader", true);

        //If true, enables Parquet's native record-level filtering using the pushed down filters. This configuration only
        // has an effect when '${PARQUET_FILTER_PUSHDOWN_ENABLED.key}' is enabled and the vectorized reader is not used.
        // You can ensure the vectorized reader is not used by setting '${PARQUET_VECTORIZED_READER_ENABLED.key}' to false
        builder.config("spark.sql.parquet.recordLevelFilter.enabled", false);

        // The number of rows to include in a parquet vectorized reader batch. The number should be carefully chosen to minimize
        // overhead and avoid OOMs in reading data."
        builder.config("spark.sql.parquet.columnarReaderBatchSize", 10240);


        //When true, enable the metadata-only query optimization that use the table's metadata  to produce the partition columns
        // instead of table scans. It applies when all the columns scanned are partition columns and the query has an aggregate
        // operator that satisfies distinct semantics. By default the optimization is disabled, and deprecated as of Spark 3.0 since
        // it may return incorrect results when the files are empty, see also SPARK-26709.It will be removed in the future releases.
        // If you must use, use 'SparkSessionExtensions' instead to inject it as a custom rule." //


        //The name of internal column for storing raw/un-parsed JSON and CSV records that fail to parse.

        //Timeout in seconds for the broadcast wait time in broadcast joins. //



        //Set a Fair Scheduler pool for a JDBC client session. //


        //The default data source to use in input/output.
        builder.config("spark.sql.sources.default", "parquet");

        //When true, a table created by a Hive CTAS statement (no USING clause) without specifying any storage property will be
        // converted to a data source table, using the data source set by ${DEFAULT_DATA_SOURCE_NAME.key}."
        builder.config("spark.sql.hive.convertCTAS", false);

        //When true, fast stats (number of files and total size of all files); will be gathered in parallel while repairing table
        // partitions to avoid the sequential listing in Hive metastore.
        builder.config("spark.sql.hive.gatherFastStats", true);

        //When true, automatically infer the data types for partitioned columns. //
        builder.config("spark.sql.sources.partitionColumnTypeInference.enabled", true);

        //When false, we will treat bucketed table as normal table //
        builder.config("spark.sql.sources.bucketing.enabled", true);

        //The maximum number of buckets allowed..checkValue(_ > 0, "the value of spark.sql.sources.bucketing.maxBuckets must be greater than 0" //
        builder.config("spark.sql.sources.bucketing.maxBuckets", 100000);

        //When true, decide whether to do bucketed scan on input tables based on query plan  +
        //"automatically. Do not use bucketed scan if 1. query does not have operators to utilize " +
        //"bucketing (e.g. join, group-by, etc);, or 2. there's an exchange operator between these " +
        //s"operators and table scan. Note when '${BUCKETING_ENABLED.key}' is set to " +
        //"false, this configuration does not take any effect." //
        builder.config("spark.sql.sources.bucketing.autoBucketedScan.enabled", true);

        //When false, we will throw an error if a query contains a cartesian product without explicit CROSS JOIN syntax."
        builder.config("spark.sql.crossJoin.enabled", true);

        // When true, the ordinal numbers are treated as the position in the select list. When false, the ordinal numbers in
        // order/sort by clause are ignored.
        builder.config("spark.sql.orderByOrdinal", true);

        // When true, the ordinal numbers in group by clauses are treated as the position in the select list. When false,
        // the ordinal numbers are ignored.
        builder.config("spark.sql.groupByOrdinal", true);

        //When true, aliases in a select list can be used in group by clauses. When false,an analysis exception is thrown in the case.
        builder.config("spark.sql.groupByAliases", true);

        builder.config("spark.sql.sources.commitProtocolClass", "org.apache.spark.sql.execution.datasources.SQLHadoopMapReduceCommitProtocol");


        //The maximum number of paths allowed for listing files at driver side. If the number of detected paths exceeds this value
        // during partition discovery, it tries to list the files with another Spark distributed job. This configuration is effective
        // only when using file-based sources such as Parquet, JSON and ORC.
        // .checkValue(parallel => parallel >= 0, "The maximum number of paths allowed for listing files at driver side must not be negative"
        builder.config("spark.sql.sources.parallelPartitionDiscovery.threshold", 32);

        //The number of parallelism to list a collection of path recursively, Set the number to prevent file listing from generating too many tasks.
        builder.config("spark.sql.sources.parallelPartitionDiscovery.parallelism", 10000);

        // If true, Spark will not fetch the block locations for each file on listing files. This speeds up file listing, but the scheduler cannot
        // schedule tasks to take advantage of data locality. It can be particularly useful if data is read from a remote cluster so the scheduler
        // could never take advantage of locality anyway.
        builder.config("spark.sql.sources.ignoreDataLocality", false);

        builder.config("spark.sql.selfJoinAutoResolveAmbiguity", true);

        //When true, fail the Dataset query if it contains ambiguous self-join. //
        builder.config("spark.sql.analyzer.failAmbiguousSelfJoin", true);

        //Whether to retain group by columns or not in GroupedData
        builder.config("spark.sql.retainGroupColumns", true);

        //When doing a pivot without specifying values for the pivot column this is the maximum number of (distinct); values that will be
        // collected without error.
        builder.config("spark.sql.pivotMaxValues", 10000);

        //When true, we could use `datasource`.`path` as table in SQL query. //
        builder.config("spark.sql.runSQLOnFiles", true);

        //When true, the whole stage (of multiple operators); will be compiled into single java method.
        builder.config("spark.sql.codegen.wholeStage", true);

        //When true, embed the (whole-stage); codegen stage ID into the class name of the generated class as a suffix" //
        builder.config("spark.sql.codegen.useIdInClassName", true);

        //The maximum number of fields (including nested fields); that will be supported before deactivating whole-stage codegen." //
        builder.config("spark.sql.codegen.maxFields", 100);

        //This config determines the fallback behavior of several codegen generators during tests. `FALLBACK` means trying codegen first
        // and then fallbacking to interpreted if any compile error happens. Disabling fallback if `CODEGEN_ONLY`.
        //"`NO_CODEGEN` skips codegen and goes interpreted path always. Note that this config works only for tests.

        // When true, (whole stage); codegen could be temporary disabled for the part of query that fail to compile generated code"
        builder.config("spark.sql.codegen.fallback", true);

        //The maximum number of codegen lines to log when errors occur. Use -1 for unlimited.
        // .checkValue(maxLines => maxLines >= -1, "The maximum must be a positive integer, 0 to disable logging or -1 to apply no limit.
        builder.config("spark.sql.codegen.logging.maxLines", 1000);

        // The maximum bytecode size of a single compiled Java function generated by whole-stage codegen. When the compiled function exceeds this
        // threshold, the whole-stage codegen is deactivated for this subtree of the current query plan. The default value is 65535, which is the
        // largest bytecode size possible for a valid Java method. When running on HotSpot, it may be preferable to set the value to
        // ${CodeGenerator.DEFAULT_JVM_HUGE_METHOD_LIMIT} to match HotSpot's implementation.
        builder.config("spark.sql.codegen.hugeMethodLimit", 65535);

        // The threshold of source-code splitting in the codegen. When the number of characters in a single Java
        // function (without comment); exceeds the threshold, the function will be automatically split to multiple
        // smaller ones. We cannot know how many bytecode will be generated, so use the code length as metric.
        // When running on HotSpot, a function's bytecode should not go beyond 8KB, otherwise it
        // will not be JITted; it also should not be too small, otherwise there will be many function calls.
        // .checkValue(threshold => threshold > 0, "The threshold must be a positive integer.
        builder.config("spark.sql.codegen.methodSplitThreshold", 1024);

        // When true, whole stage codegen would put the logic of consuming rows of each physical operator into individual methods, instead of a
        // single big method. This can be used to avoid oversized function that can miss the opportunity of JIT optimization.
        builder.config("spark.sql.codegen.splitConsumeFuncByOperator", true);

        // The maximum number of bytes to pack into a single partition when reading files. This configuration is effective only when using
        // file-based sources such as Parquet, JSON and ORC.
        builder.config("spark.sql.files.maxPartitionBytes", 134217728); // 128MB parquet.block.size

        // The estimated cost to open a file, measured by the number of bytes could be scanned in the same time. This is used when putting
        // multiple files into a partition. It's better to over estimated, then the partitions with small files will be faster than partitions with
        // bigger files (which is scheduled first);. This configuration is effective only when using file-based sources such as Parquet, JSON and ORC." //

        builder.config("spark.sql.files.openCostInBytes", 4194304);

        // The suggested (not guaranteed); minimum number of split file partitions. If not set, the default value is `spark.default.parallelism`.
        // This configuration is effective only when using file-based sources such as Parquet, JSON and ORC.
        // .checkValue(v => v > 0, "The min partition number must be a positive integer.
        builder.config("spark.sql.files.minPartitionNum", 20);

        // Whether to ignore corrupt files. If true, the Spark jobs will continue to run when encountering corrupted files and the contents that
        // have been read will still be returned. This configuration is effective only when using file-based sources such as Parquet, JSON and ORC.
        // We should be failing the job
        builder.config("spark.sql.files.ignoreCorruptFiles", false);

        //Whether to ignore missing files. If true, the Spark jobs will continue to run when encountering missing files and the contents that have
        // been read will still be returned. This configuration is effective only when using file-based sources such as Parquet, JSON and ORC.
        // We should be failing the job
        builder.config("spark.sql.files.ignoreMissingFiles", false);

        //Maximum number of records to write out to a single file. If this value is zero or negative, there is no limit.
        // It should be 100MB/Row size. For now, just keep it unlimited
        builder.config("spark.sql.files.maxRecordsPerFile", 0);

        //When true, the planner will try to find out duplicated exchanges and re-use them.
        builder.config("spark.sql.exchange.reuse", true);

        //When true, the planner will try to find out duplicated subqueries and re-use them.
        builder.config("spark.sql.execution.reuseSubquery", true);

        //Whether to remove redundant project exec node based on children's output and ordering requirement.
        builder.config("spark.sql.execution.removeRedundantProjects", true);

        //Whether to remove redundant physical sort node
        builder.config("spark.sql.execution.removeRedundantSorts", true);

        //This enables substitution using syntax like `${var}`, `${system:var}`, and `${env:var}`." //
        builder.config("spark.sql.variable.substitute", true);

        //Enable two-level aggregate hash map. When enabled, records will first be inserted/looked-up at a 1st-level, small, fast map, and
        // then fallback to a 2nd-level, larger, slower map when 1st level is full or keys cannot be found. When disabled, records go directly
        // to the 2nd level.
        builder.config("spark.sql.codegen.aggregate.map.twolevel.enabled", true);

        //Enable vectorized aggregate hash map. This is for testing/benchmarking only. //
        builder.config("spark.sql.codegen.aggregate.map.vectorized.enable", false);

        // When true, the code generator would split aggregate code into individual methods instead of a single big method. This can be used to
        // avoid oversized function that can miss the opportunity of JIT optimization.
        builder.config("spark.sql.codegen.aggregate.splitAggregateFunc.enabled", true);

        // The maximum depth of a view reference in a nested view. A nested view may reference other nested views, the dependencies are organized
        // in a directed acyclic graph (DAG);. However the DAG depth may become too large and cause unexpected behavior. This configuration puts
        // a limit on this: when the depth of a view exceeds this value during analysis, we terminate the resolution to avoid potential errors.
        // .checkValue(depth => depth > 0, "The maximum depth of a view reference in a nested view must be positive.
        // We should avoid very deep nesting in sql.
        builder.config("spark.sql.view.maxNestedViewDepth", 20);

        // In the case of ObjectHashAggregateExec, when the size of the in-memory hash map grows too large, we will fall back to sort-based
        // aggregation. This option sets a row count threshold for the size of the hash map.
        builder.config("spark.sql.objectHashAggregate.sortBased.fallbackThreshold", 128);

        // Decides if we use ObjectHashAggregateExec
        builder.config("spark.sql.execution.useObjectHashAggregateExec", true);

        // Whether to ignore null fields when generating JSON objects in JSON data source and JSON functions such as to_json. If false, it
        // generates null for null fields in JSON objects.
        builder.config("spark.sql.jsonGenerator.ignoreNullFields", true);

        // Whether to optimize JSON expressions in SQL optimizer. It includes pruning unnecessary columns from from_json, simplifing from_json
        // to_json, to_json named_struct(from_json.col1, from_json.col2, ....);.
        builder.config("spark.sql.optimizer.enableJsonExpressionOptimization", true);

        //When true, SQL commands use parallel file listing,as opposed to single thread listing. This usually speeds up commands that need to
        // list many directories." //
        builder.config("spark.sql.statistics.parallelFileListingInStatsComputation.enabled", true);

        //The default table size used in query planning. By default, it is set to Long.MaxValue  +
        //s"which is larger than `${AUTO_BROADCASTJOIN_THRESHOLD.key}` to be more conservative. " +
        //"That is to say by default the optimizer will not choose to broadcast a table unless it " +
        //"knows for sure its size is small enough." //
        builder.config("spark.sql.defaultSizeInBytes", Long.MAX_VALUE);

        //The maximum relative standard deviation allowed in HyperLogLog+ algorithm when generating column level statistics.
        builder.config("spark.sql.statistics.ndv.maxError", (long) 0.05);

        // Generates histograms when computing column statistics if enabled. Histograms can provide better estimation accuracy. Currently,
        // Spark only supports equi-height histogram. Note that collecting histograms takes extra cost. For example, collecting column
        // statistics usually takes only one table scan, but generating equi-height histogram will cause an extra table scan.
        builder.config("spark.sql.statistics.histogram.enabled", false);

        //The number of bins when generating histograms..checkValue(num => num > 1, "The number of bins must be greater than 1." //
        builder.config("spark.sql.statistics.histogram.numBins", 254);

        // Accuracy of percentile approximation when generating equi-height histograms. Larger value means better accuracy. The relative
        // error can be deduced by 1.0 / PERCENTILE_ACCURACY.
        builder.config("spark.sql.statistics.percentile.accuracy", 10000);

        // Enables automatic update for table size once table's data is changed. Note that if the total number of files of the table is very
        // large, this can be expensive and slow down data change commands.
        builder.config("spark.sql.statistics.size.autoUpdate.enabled", false);

        // Enables CBO for estimation of plan statistics when set true.
        builder.config("spark.sql.cbo.enabled", true);

        //When true, the logical plan will fetch row counts and column statistics from catalog
        builder.config("spark.sql.cbo.planStats.enabled", true);

        // Enables join reorder in CBO.
        builder.config("spark.sql.cbo.joinReorder.enabled", true);

        // The maximum number of joined nodes allowed in the dynamic programming algorithm.
        // .checkValue(number => number > 0, "The maximum number must be a positive integer." //
        builder.config("spark.sql.cbo.joinReorder.dp.threshold", 12);

        // The weight of the ratio of cardinalities (number of rows); in the cost comparison function. The ratio of sizes in bytes has weight
        // 1 - this value. The weighted geometric mean of these ratios is used to decide which of the candidate plans will be chosen by the CBO.
        // .checkValue(weight => weight >= 0 && weight <= 1, "The weight value must be in [0, 1]
        builder.config("spark.sql.cbo.joinReorder.card.weight", (long) 0.7);

        // Applies star-join filter heuristics to cost based join enumeration.
        builder.config("spark.sql.cbo.joinReorder.dp.star.filter", false);

        // When true, it enables join reordering based on star schema detection.
        builder.config("spark.sql.cbo.starSchemaDetection", true);

        // Specifies the upper limit of the ratio between the largest fact tables for a star join to be considered.
        builder.config("spark.sql.cbo.starJoinFTRatio", (long) 0.9);

        //The ID of session local timezone in the format of either region-based zone IDs or zone offsets. Region IDs must have the form
        // 'area/city', such as 'America/Los_Angeles'. Zone offsets must be in the format '(|-);HH', '(|-);HH:mm' or '(|-);HH:mm:ss',
        // e.g '-08', '01:00' or '-13:33:33'. Also 'UTC' and 'Z' are supported as aliases of '00:00'. Other short names are not recommended
        // to use because they can be ambiguous.
        // .checkValue(isValidTimezone, s"Cannot resolve the given timezone with ZoneId.of(_, ZoneId.SHORT_IDS);" //


        // Threshold for number of rows guaranteed to be held in memory by the window operator
        builder.config("spark.sql.windowExec.buffer.in.memory.threshold", 4096);

        // Threshold for number of rows to be spilled by window operator
        builder.config("spark.sql.windowExec.buffer.spill.threshold", Integer.MAX_VALUE);

        //Threshold for number of rows guaranteed to be held in memory by the sort merge join operator
        builder.config("spark.sql.sortMergeJoinExec.buffer.in.memory.threshold", ByteArrayMethods.MAX_ROUNDED_ARRAY_LENGTH);

        //Threshold for number of rows to be spilled by sort merge join operator
        builder.config("spark.sql.sortMergeJoinExec.buffer.spill.threshold", Integer.MAX_VALUE);

        //Threshold for number of rows guaranteed to be held in memory by the cartesian product operator
        builder.config("spark.sql.cartesianProductExec.buffer.in.memory.threshold", 4096);

        //Threshold for number of rows to be spilled by cartesian product operator //
        builder.config("spark.sql.cartesianProductExec.buffer.spill.threshold", Integer.MAX_VALUE);

        //When true, quoted Identifiers (using backticks); in SELECT statement are interpreted as regular expressions.
        builder.config("spark.sql.parser.quotedRegexColumnNames", false);

        // Number of points to sample per partition in order to determine the range boundaries for range partitioning, typically used in
        // global sorting (without limit)
        builder.config("spark.sql.execution.rangeExchange.sampleSizePerPartition", 100);

        builder.config("spark.sql.execution.arrow.enabled", false);

        //When true, make use of Apache Arrow for columnar data transfers in PySpark.  +
        //"This optimization applies to: " +
        //"1. pyspark.sql.DataFrame.toPandas " +
        //"2. pyspark.sql.SparkSession.createDataFrame when its input is a Pandas DataFrame " +
        //"The following data types are unsupported: " +
        //"ArrayType of TimestampType, and nested StructType." //


        //When true, it shows the JVM stacktrace in the user-facing PySpark exception  +
        //"together with Python stacktrace. By default, it is disabled and hides JVM stacktrace " +
        //"and shows a Python-friendly exception only." //
        builder.config("spark.sql.pyspark.jvmStacktrace.enabled", false);

        //When true, make use of Apache Arrow for columnar data transfers in SparkR.  +
        //"This optimization applies to: " +
        //"1. createDataFrame when its input is an R DataFrame " +
        //"2. collect " +
        //"3. dapply " +
        //"4. gapply " +
        //"The following data types are unsupported: " +
        //"FloatType, BinaryType, ArrayType, StructType and MapType." //
        builder.config("spark.sql.execution.arrow.sparkr.enabled", false);
        //(Deprecated since Spark 3.0, please set  +
        //"'spark.sql.execution.arrow.pyspark.fallback.enabled'.);" //
        builder.config("spark.sql.execution.arrow.fallback.enabled", true);
        //s"When true, optimizations enabled by '${ARROW_PYSPARK_EXECUTION_ENABLED.key}' will " +
        //"fallback automatically to non-optimized implementations if an error occurs." //

        //When using Apache Arrow, limit the maximum number of records that can be written
        //to a single ArrowRecordBatch in memory. If set to zero or negative there is no limit.
        builder.config("spark.sql.execution.arrow.maxRecordsPerBatch", 10000);
        //s"Same as `${BUFFER_SIZE.key}` but only applies to Pandas UDF executions. If it is not " +
        // s"set, the fallback is `${BUFFER_SIZE.key}`. Note that Pandas execution requires more " +
        // "than 4 bytes. Lowering this value could make small Pandas UDF batch iterated and " +
        // "pipelined; however, it might degrade performance. See SPARK-27870." //

        //When true, the traceback from Python UDFs is simplified. It hides  +
        // "the Python worker, (de);serialization, etc from PySpark in tracebacks, and only " +
        // "shows the exception messages from UDFs. Note that this works only with CPython 3.7+
        //." //
        builder.config("spark.sql.execution.pyspark.udf.simplifiedTraceback.enabled", false);
        //When true, columns will be looked up by name if labeled with a string and fallback  +
        //"to use position if not. When false, a grouped map Pandas UDF will assign columns from " +
        //"the returned Pandas DataFrame based on position, regardless of column label type. " +
        //"This configuration will be deprecated in future releases." //
        builder.config("spark.sql.legacy.execution.pandas.groupedMap.assignColumnsByName", true);
        //When true, Arrow will perform safe type conversion when converting  +
        //"Pandas.Series to Arrow array during serialization. Arrow will raise errors " +
        //"when detecting unsafe type conversion like overflow. When false, disabling Arrow's type " +
        //"check and do type conversions anyway. This config only works for Arrow 0.11.0+
        //." //
        builder.config("spark.sql.execution.pandas.convertToArrowArraySafely", false);
        //When true, the apply function of the rule verifies whether the right node of the +
        //" except operation is of type Filter or Project followed by Filter. If yes, the rule" +
        //" further verifies 1); Excluding the filter operations from the right (as well as the" +
        //" left node, if any); on the top, whether both the nodes evaluates to a same result." +
        //" 2); The left and right nodes don't contain any SubqueryExpressions. 3); The output" +
        //" column names of the left node are distinct. If all the conditions are met, the" +
        //" rule will replace the except operation with a Filter by flipping the filter" +
        //" condition(s); of the right node." //
        builder.config("spark.sql.optimizer.replaceExceptWithFilter", true);
        //When true (default);, establishing the result type of an arithmetic operation  +
        //"happens according to Hive behavior and SQL ANSI 2011 specification, ie. rounding the " +
        //"decimal part of the result if an exact representation is not possible. Otherwise, NULL " +
        //"is returned in those cases, as previously." //
        builder.config("spark.sql.decimalOperations.allowPrecisionLoss", true);
        //When integral literal is used in decimal operations, pick a minimum precision  +
        //"required by the literal if this config is true, to make the resulting precision and/or " +
        //"scale smaller. This can reduce the possibility of precision lose and/or overflow." //
        builder.config("spark.sql.legacy.literal.pickMinimumPrecision", true);
        //Regex to decide which keys in a Spark SQL command's options map contain sensitive  +
        //"information. The values of options whose names that match this regex will be redacted " +
        //"in the explain output. This redaction is applied on top of the global redaction " +
        //s"configuration defined by ${SECRET_REDACTION_PATTERN.key}." /
        //Regex to decide which parts of strings produced by Spark contain sensitive  +
        //"information. When this regex matches a string part, that string part is replaced by a " +
        //"dummy value. This is currently used to redact the output of SQL explain commands. " +
        //"When this conf is not set, the value from `spark.redaction.string.regex` is used." //

        //When this option is set to false and all inputs are binary, `functions.concat` returns  +
        //"an output as binary. Otherwise, it returns as a string." //
        builder.config("spark.sql.function.concatBinaryAsString", false);
        //When this option is set to false and all inputs are binary, `elt` returns  +
        //"an output as binary. Otherwise, it returns as a string." //
        builder.config("spark.sql.function.eltOutputAsString", false);
        //When this option is set to true, partition column values will be validated with  +
        //"user-specified schema. If the validation fails, a runtime exception is thrown. " +
        //"When this option is set to false, the partition column value will be converted to null " +
        //"if it can not be casted to corresponding user-specified schema." //
        builder.config("spark.sql.sources.validatePartitionColumns", true);
        //The max number of entries to be stored in queue to wait for late epochs.  +
        //"If this parameter is exceeded by the size of the queue, stream will stop with an error." //
        builder.config("spark.sql.streaming.continuous.epochBacklogQueueSize", 10000);

        builder.config("spark.sql.streaming.continuous.executorQueueSize", 1024);
        //The interval at which continuous execution readers will poll to check whether +
        //" the epoch has advanced on the driver." //
        builder.config("spark.sql.streaming.continuous.executorPollIntervalMs", 100);
        //A comma-separated list of data source short names or fully qualified data source  +
        //"implementation class names for which Data Source V2 code path is disabled. These data " +
        //"sources will fallback to Data Source V1 code path." //


        //A comma-separated list of fully qualified data source register class names for which +
        //" StreamWriteSupport is disabled. Writes to these sources will fall back to the V1 Sinks." //


        //Whether to fast fail task execution when writing output to FileFormat datasource.  +
        //"If this is enabled, in `FileFormatWriter` we will catch `FileAlreadyExistsException` " +
        //"and fast fail output task without further task retry. Only enabling this if you know " +
        //"the `FileAlreadyExistsException` of the output task is unrecoverable, i.e., further " +
        //"task attempts won't be able to success. If the `FileAlreadyExistsException` might be " +
        //"recoverable, you should keep this as disabled and let Spark to retry output tasks. " +
        //"This is disabled by default." //
        builder.config("spark.sql.execution.fastFailOnFileFormatOutput", false);
        //When INSERT OVERWRITE a partitioned data source table, we currently support 2 modes:  +
        //"static and dynamic. In static mode, Spark deletes all the partitions that match the " +
        //"partition specification(e.g. PARTITION(a=1,b);); in the INSERT statement, before " +
        //"overwriting. In dynamic mode, Spark doesn't delete partitions ahead, and only overwrite " +
        //"those partitions that have data written into it at runtime. By default we use static " +
        //"mode to keep the same behavior of Spark prior to 2.3. Note that this config doesn't " +
        //"affect Hive serde tables, as they are always overwritten with dynamic mode. This can " +
        //"also be set as an output option for a data source using key partitionOverwriteMode " +
        //"(which takes precedence over this setting);, e.g. " +


        //When inserting a value into a column with different data type, Spark will perform  +
        //"type coercion. Currently, we support 3 policies for the type coercion rules: ANSI, " +
        //"legacy and strict. With ANSI policy, Spark performs the type coercion as per ANSI SQL. " +
        //"In practice, the behavior is mostly the same as PostgreSQL. " +
        //"It disallows certain unreasonable type conversions such as converting " +
        //"`string` to `int` or `double` to `boolean`. " +
        //"With legacy policy, Spark allows the type coercion as long as it is a valid `Cast`, " +
        //"which is very loose. e.g. converting `string` to `int` or `double` to `boolean` is " +
        //"allowed. It is also the only behavior in Spark 2.x and it is compatible with Hive. " +
        //"With strict policy, Spark doesn't allow any possible precision loss or data truncation " +
        //"in type coercion, e.g. converting `double` to `int` or `decimal` to `double` is " +
        //"not allowed.


        //When true, Spark SQL uses an ANSI compliant dialect instead of being Hive compliant. For example, Spark will throw an exception at
        // runtime instead of returning null results when the inputs to a SQL operator/function are invalid. For full details of this dialect,
        // you can find them in the section \"ANSI Compliance\" of Spark's documentation. Some ANSI dialect features may be not from the ANSI SQL
        // standard directly, but their behaviors align with ANSI SQL's style"
        //builder.config("spark.sql.ansi.enabled", true);

        // When perform a repartition following a shuffle, the output row ordering would be nondeterministic. If some downstream stages fail and
        // some tasks of the repartition stage retry, these tasks may generate different data, and that can lead to correctness issues. Turn on
        // this config to insert a local sort before actually doing repartition to generate consistent repartition results. The performance of
        // repartition(); may go down since we insert extra local sort before it.
        // Set it to false since we would like to rely on hash joins wherever required.
        builder.config("spark.sql.execution.sortBeforeRepartition", false);

        // Prune nested fields from a logical relation's output which are unnecessary in satisfying a query. This optimization allows columnar file
        // format readers to avoid reading unnecessary nested column data. Currently Parquet and ORC are the data sources that implement this
        // optimization.
        builder.config("spark.sql.optimizer.nestedSchemaPruning.enabled", true);

        //When true, the optimizer will disable user-specified hints that are additional directives for better planning of a query.
        // We need developers to provide hints.
        builder.config("spark.sql.optimizer.disableHints", false);

        //A comma-separated list of data source short names or fully qualified data source implementation class names for which Spark tries to
        // push down predicates for nested columns and/or names containing `dots` to data sources. This configuration is only effective with
        // file-based data sources in DSv1. Currently, Parquet and ORC implement both optimizations. The other data sources don't support this
        // feature yet. So the default value is 'parquet,orc'." //
        builder.config("spark.sql.optimizer.nestedPredicatePushdown.supportedFileSources", "parquet,orc");

        // Prune nested fields from object serialization operator which are unnecessary in satisfying a query. This optimization allows object
        // serializers to avoid executing unnecessary nested expressions.
        builder.config("spark.sql.optimizer.serializer.nestedSchemaPruning.enabled", true);

        // Prune nested fields from expressions in an operator which are unnecessary in satisfying a query. Note that this optimization doesn't
        // prune nested fields from physical data source scanning. For pruning nested fields from scanning, please use
        // `spark.sql.optimizer.nestedSchemaPruning.enabled` config.
        builder.config("spark.sql.optimizer.expression.nestedPruning.enabled", true);

        // In SQL queries with a SORT followed by a LIMIT like 'SELECT x FROM t ORDER BY y LIMIT m', if m is under this threshold,
        // do a top-K sort in memory, otherwise do a global sort which spills to disk if necessary.
        builder.config("spark.sql.execution.topKSortFallbackThreshold", ByteArrayMethods.MAX_ROUNDED_ARRAY_LENGTH);

        // If it is set to true, column names of the requested schema are passed to CSV parser. Other column values can be ignored during parsing
        // even if they are malformed.
        builder.config("spark.sql.csv.parser.columnPruning.enabled", true);

        // If it is set, it configures the buffer size of CSV input during parsing. It is the same as inputBufferSize option in CSV which has a
        // higher priority. Note that this is a workaround for the parsing library's regression, and this configuration is internal and supposed
        // to be removed in the near future.
        // DO NOT populate


        // Capacity for the max number of rows to be held in memory by the fast hash aggregate product operator. The bit
        // is not for actual value, but the actual numBuckets is determined by loadFactor
        builder.config("spark.sql.codegen.aggregate.fastHashMap.capacityBit", 20);

        // When set to true, TRUNCATE TABLE command will not try to set back original permission and ACLs when re-creating
        // the table/partition paths.
        builder.config("spark.sql.truncateTable.ignorePermissionAcl.enabled", false);

        // When set to true, the key attribute resulted from running `Dataset.groupByKey` for non-struct key type, will be
        // named as `value`, following the behavior of Spark version 2.4 and earlier.
        builder.config("spark.sql.legacy.dataset.nameNonStructGroupingKeyAsValue", false);

        // Maximum number of fields of sequence-like entries can be converted to strings in debug output. Any elements beyond
        // the limit will be dropped and replaced by a""" "... N more fields" placeholder."""
        builder.config("spark.sql.debug.maxToStringFields", 25);

        // Maximum number of characters to output for a plan string.  If the plan is longer, further output will be truncated.
        // The default setting always generates a full plan. Set this to a lower value such as 8k if plan strings are taking
        // up too much memory or are causing OutOfMemory errors in the driver or UI processes.
        // .checkValue(i => i >= 0 && i <= ByteArrayMethods.MAX_ROUNDED_ARRAY_LENGTH,
        // "Invalid value for 'spark.sql.maxPlanStringLength'.  Length must be a valid string
        // This is only for Dev and UAT. In Product, it should be set to 8k
        builder.config("spark.sql.maxPlanStringLength", ByteArrayMethods.MAX_ROUNDED_ARRAY_LENGTH);

        // Maximum number of characters to output for a metadata string. e.g. file location in `DataSourceScanExec`,
        // every value will be abbreviated if exceed length.
        // .checkValue(_ > 3, "This value must be bigger than 3." //
        // This is only for Dev and UAT. In Product, it should be set to 50
        builder.config("spark.sql.maxMetadataStringLength", 500);

        // If it is set to true, SET command will fail when the key is registered as a SparkConf entry.
        builder.config("spark.sql.legacy.setCommandRejectsSparkCoreConfs", true);

        // If the configuration property is set to true, java.time.Instant and java.time.LocalDate classes of Java 8 API
        // are used as external types for Catalyst's TimestampType and DateType. If it is set to false, java.sql.Timestamp
        // and java.sql.Date are used for the same purpose.
        builder.config("spark.sql.datetime.java8API.enabled", true);

        // Configures the query explain mode used in the Spark SQL UI. The value can be 'simple', 'extended', 'codegen',
        // 'cost', or 'formatted'. The default value is 'formatted'.
        // "Invalid value for 'spark.sql.ui.explainMode'.
        // Valid values are 'simple', 'extended', 'codegen', 'cost' and 'formatted'.
        builder.config("spark.sql.ui.explainMode", "formatted");

        // The max length of a file that can be read by the binary file data source. Spark will fail fast and not attempt
        // to read the file if its length exceeds this value. The theoretical max is Int.MaxValue, though VMs might
        // implement a smaller max.
        builder.config("spark.sql.sources.binaryFile.maxLength", Integer.MAX_VALUE);

        //If it is set to true, date/timestamp will cast to string in binary comparisons with String
        builder.config("spark.sql.legacy.typeCoercion.datetimeToString.enabled", false);

        // Name of the default catalog. This will be the current catalog if users have not explicitly set the current
        // catalog yet.

        // A catalog implementation that will be used as the v2 interface to Spark's built-in
        // v1 catalog: $SESSION_CATALOG_NAME. This catalog shares its identifier namespace with the
        // $SESSION_CATALOG_NAME and must be consistent with it; for example, if a table can be loaded by the
        // $SESSION_CATALOG_NAME, this catalog must also return the table metadata. To delegate operations to the
        // $SESSION_CATALOG_NAME, implementations can extend 'CatalogExtension'.


        // The policy to deduplicate map keys in builtin function: CreateMap, MapFromArrays, MapFromEntries, StringToMap,
        // MapConcat and TransformKeys. When EXCEPTION, the query fails if duplicated map keys are detected. When LAST_WIN,
        // the map key that is inserted at last takes precedence.
        // This is a very specific functionality and should be carried out when really required.


        //When true, enable filter pushdown to CSV datasource. //
        builder.config("spark.sql.csv.filterPushdown.enabled", true);

        //When true, enable filter pushdown to JSON datasource. //
        builder.config("spark.sql.json.filterPushdown.enabled", true);

        //When true, enable filter pushdown to Avro datasource. //
        builder.config("spark.sql.avro.filterPushdown.enabled", true);

        // The number of partitions to be handled in one turn when use `AlterTableAddPartitionCommand` to add partitions
        // into table. The smaller batch size is, the less memory is required for the real handler, e.g. Hive Metastore.
        builder.config("spark.sql.addPartitionInBatch.size", 100);

        //Timeout for executor to wait for the termination of transformation script when EOF
        // .checkValue(_ > 0, "The timeout value must be positive
        builder.config("spark.sql.scriptTransformation.exitTimeoutInSeconds", "5s");

        // When true, if two bucketed tables with the different number of buckets are joined, the side with a bigger number
        // of buckets will be coalesced to have the same number of buckets as the other side. Bigger number of buckets is
        // divisible by the smaller number of buckets. Bucket coalescing is applied to sort-merge joins and shuffled hash join.
        // Note: Coalescing bucketed table can avoid unnecessary shuffling in join, but it also reduces parallelism and
        // could possibly cause OOM for shuffled hash join
        builder.config("spark.sql.bucketing.coalesceBucketsInJoin.enabled", true);

        // The ratio of the number of two buckets being coalesced should be less than or equal to this value for bucket
        // coalescing to be applied. This configuration only has an effect when
        // '${COALESCE_BUCKETS_IN_JOIN_ENABLED.key}' is set to true.
        // .checkValue(_ > 0, "The difference must be positive.
        builder.config("spark.sql.bucketing.coalesceBucketsInJoin.maxBucketRatio", 4);

        // The maximum number of partitionings that a HashPartitioning can be expanded to. This configuration is applicable
        // only for BroadcastHashJoin inner joins and can be set to '0' to disable this feature.
        // .checkValue(_ >= 0, "The value must be non-negative.
        // If the number of partitions goes beyond 8, something does not look right
        builder.config("spark.sql.execution.broadcastHashJoin.outputPartitioningExpandLimit", 8);

        // When true, NULL-aware anti join execution will be planed into BroadcastHashJoinExec with flag isNullAwareAntiJoin
        // enabled, optimized from O(M*N); calculation into O(M); calculation using Hash lookup instead of Looping lookup.
        // Only support for singleColumn NAAJ for now.
        builder.config("spark.sql.optimizeNullAwareAntiJoin", true);

    }

}
