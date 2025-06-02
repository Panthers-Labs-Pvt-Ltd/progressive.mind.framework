package com.progressive.minds.chimera.core.execution_engine.estimator;

import org.apache.spark.sql.SparkSession;

/*
 * Setting up s3.max_concurrent_requests
 * Explore possible Use of Distcp.
 */

public class HadoopS3Estimator extends ConfEstimator {

    public HadoopS3Estimator(SparkSession.Builder ctxBuilder) {
        super(ctxBuilder);
    }

    @Override
    void calculate() {
        // Used to calculate
    }

    @Override
    public void setConfigValue() {
        setConnectionProperty();

         // S3 API level parameters.
        builder.config("spark.hadoop.fs.s3a.list.version", 2);

        builder.config("spark.hadoop.mapreduce.fileoutputcommitter.algorithm.version", "2");
        builder.config("spark.hadoop.mapreduce.fileoutputcommitter.marksuccessfuljobs", "true");

        setMultiPartConfiguration();

        setFastUploadConfiguration();

        setEncryptionConfiguration();

        setBucketingConfiguration();

        setInconsistencyConfiguration();

        setRetryConfiguration();

        /*

nothing in those logs. Try org.apache.hadoop.fs.s3a at debug
and upgrade to 3.3.1
After that, turn on s3 bucket logging to a separate bucket, look in the access
logs for errors (500 etc), or throttling 503. Do not worry about 401 on HEAD,
they are used for existence probes
 */

        // The maximum number of threads to allow in the pool used by TransferManager. Increase connection/thread counts
        // for performance optimization.
        // This should be calculated - Double the number of worker threads.
        builder.config("spark.hadoop.fs.s3a.threads.max", 1500);

        //the maximum number of tasks cached if all threads are already uploading.
        // This should be calculated - Double the number of worker threads.
        builder.config("spark.hadoop.fs.s3a.max.total.tasks", 1500);

        // number of times we should retry errors
        builder.config("spark.hadoop.fs.s3a.attempts.maximum", 20);

        // socket send buffer to be used in Amazon client
        builder.config("spark.hadoop.fs.s3a.socket.send.buffer", 8 * 1024*(long)1024); //8MB

        // socket send buffer to be used in Amazon client
        builder.config("spark.hadoop.fs.s3a.socket.recv.buffer", 8 * 1024*(long)1024); //8MB

        // number of records to get while paging through a directory listing
        builder.config("spark.hadoop.fs.s3a.paging.maximum", 5000);

        // the time an idle thread waits before terminating
        builder.config("spark.hadoop.fs.s3a.threads.keepalivetime", 60);

        // blocksize
        builder.config("spark.hadoop.fs.s3a.block.size", "128M");

        //enable multiobject-delete calls? When enabled, multiple single-object delete requests are replaced by
        // a single 'delete multiple objects'-request, reducing the number of requests.
        // Beware: legacy S3-compatible object stores might not support this request.
        builder.config("spark.hadoop.fs.s3a.multiobjectdelete.enable", true);

        // read ahead buffer size to prevent connection re-establishments.
        builder.config("spark.hadoop.fs.s3a.readahead.range", 8*1024*(long)1024); //8 MB

        // This is an interesting bit to use.
        // Which input strategy to use for buffering, seeking and similar when reading data.
        // * "normal": General input. Some seeks, some reads.
        // * "sequential": Optimized for sequential access.
        // * "random": Optimized purely for random seek+read/positionedRead operations; The performance of sequential IO may be
        //              reduced in exchange for more efficient {@code seek()} operations.
        builder.config("spark.hadoop.fs.s3a.experimental.input.fadvise", "normal");

    }

    private void setInconsistencyConfiguration() {
        // Inconsistency (visibility delay) injection settings.
    }

    private void setRetryConfiguration() {
        // Number of times to retry any repeatable S3 client request on failure, excluding throttling requests: {@value}.
        builder.config("spark.hadoop.fs.s3a.retry.limit", 20);

        // Interval between retry attempts.: {@value}.
        builder.config("spark.hadoop.fs.s3a.retry.interval", "500ms");

        // Number of times to retry any throttled request: {@value}.
        builder.config("spark.hadoop.fs.s3a.retry.throttle.limit", 20);

        //Interval between retry attempts on throttled requests: {@value}.
        builder.config("spark.hadoop.fs.s3a.retry.throttle.interval", "500ms");

        // Should etags be exposed as checksums?
        builder.config("spark.hadoop.fs.s3a.etag.checksum.enabled", false);
    }

    private void setBucketingConfiguration() {


        /*BUCKET_PATTERN = FS_S3A_BUCKET_PREFIX + "%s.%s"
         Prefix for S3A bucket-specific properties: {@value}.
        */
    }

    private void setFastUploadConfiguration() {
        // fast block-by-block upload mechanism is the only supported upload mechanism
        // hopefully this configuration will go away
        builder.config("spark.hadoop.fs.s3a.fast.upload", true);

        //initial size of memory buffer for a fast upload
        //This is marked deprecated as well. But we still need to set it up
        builder.config("spark.hadoop.fs.s3a.fast.buffer.size", 1048576); //1MB

        // What buffer to use. This is marked Unstable and hence must be tested rigorously
        // Various buffer "disk", "array", "bytebuffer". Set up as disk for now
        // * Buffer blocks to disk: Capacity is limited to available disk space.
        // * Use an in-memory array. Fast but will run of heap rapidly.
        // * Use a byte buffer. May be more memory efficient than the in-memory array
        builder.config("spark.hadoop.fs.s3a.fast.upload.buffer", "bytebuffer");

        // Maximum Number of blocks a single output stream can have active (uploading, or queued to the
        // central FileSystem instance's pool of queued operations.
        // This stops a single stream overloading the shared thread pool.
        // Default is {@link #DEFAULT_FAST_UPLOAD_ACTIVE_BLOCKS} Unstable
        // Limit of queued block upload operations before writes
        builder.config("spark.hadoop.fs.s3a.fast.upload.active.blocks", 8);

        //The value can be 0 (default), 1 or 2. When set to 0, bucket existence checks won't be done during initialization thus
        // making it faster. Though it should be noted that when the bucket is not available in S3, or if fs.s3a.endpoint points
        // to the wrong instance of a private S3 store consecutive calls like listing, read, write etc. will fail with an
        // UnknownStoreException. When set to 1, the bucket existence check will be done using the V1 API of the S3 protocol
        // which doesn't verify the client's permissions to list or read data in the bucket. When set to 2, the bucket existence
        // check will be done using the V2 API of the S3 protocol which does verify that the client has permission to read the bucket.
        builder.config("spark.hadoop.fs.s3a.bucket.probe", 0);
    }

    private void setMultiPartConfiguration() {
        // size of each of or multipart pieces in bytes. It is currently set at 64MB which is high. We should decrease this to
        // increase parallelism - maybe 32MB. Max multipart size ~2.14GB is capped in hadoop-aws code. We are not going to get over that, ever.
        builder.config("spark.hadoop.fs.s3a.multipart.size", "32M");// 64 MB

        // minimum size in bytes before we start a multipart uploads or copy. This can be reduced as well to 32MB
        builder.config("spark.hadoop.fs.s3a.multipart.threshold", "128M");// 128 MB

        // should we try to purge old multipart uploads when starting up
        builder.config("spark.hadoop.fs.s3a.multipart.purge", true);

        // purge any multipart uploads older than this number of seconds
        builder.config("spark.hadoop.fs.s3a.multipart.purge.age", 86400); //1 day

    }

    private void setEncryptionConfiguration(){
        // Private | PublicRead | PublicReadWrite | AuthenticatedRead | LogDeliveryWrite | BucketOwnerRead | BucketOwnerFullControl
        // s3 server-side encryption, see S3AEncryptionMethods for valid options
        // The standard encryption algorithm AWS supports. Different implementations may support others (or none).
        // Use the S3AEncryptionMethods instead when configuring which Server Side Encryption to use.

        // Used to specify which AWS KMS key to use if
        // * {@link #SERVER_SIDE_ENCRYPTION_ALGORITHM} is
        // * {@code SSE-KMS} (will default to aws/s3
        // * master key if left blank).
        // * With with {@code SSE_C}, the base-64 encoded AES 256 key.
        // * May be set within a JCEKS file.
        // * Value: "{@value}".
    }


    private void setConnectionProperty() {
        //use a custom endpoint?
        builder.config("spark.hadoop.fs.s3a.endpoint", "s3.us-east-1.amazonaws.com");


        // This is an optional prefix we can use to initialize the User-Agent header to send in HTTP requests to AWS
        // services (with Hadoop version number appended). Do not need to set it up as yet.

        //S3A Implementation
        builder.config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem");
        //The implementation class of the S3A AbstractFileSystem
        builder.config("spark.hadoop.fs.AbstractFileSystem.s3a.impl", "org.apache.hadoop.fs.s3a.S3A");


        // aws credentials provider
        // * SimpleAWSCredentialsProvider(binding, conf)
        // * EnvironmentVariableCredentialsProvider()
        // * InstanceProfileCredentialsProvider.getInstance()
        // * AnonymousAWSCredentialsProvider - For publicly accessible S3


        // Security Token Service Endpoint. If unset, uses the default endpoint
        builder.config("spark.hadoop.fs.s3a.assumed.role.sts.endpoint", "https://sts.amazonaws.com");

        // Region for the STS endpoint; only relevant if the endpoint is set. Needed for v4 signing
        builder.config("spark.hadoop.fs.s3a.assumed.role.sts.endpoint.region", "us-east-1");

        // connect to s3 over ssl?
        builder.config("spark.hadoop.fs.s3a.connection.ssl.enabled", true);


        // seconds until we give up trying to establish a connection to s3
        builder.config("spark.hadoop.fs.s3a.connection.establish.timeout", 50000);

        // seconds until we give up on a connection to s3
        builder.config("spark.hadoop.fs.s3a.connection.timeout", 2000000); //Increased it 10 times

        // number of simultaneous connections to s3
        // This should be fs.s3a.max.total.tasks + for fs.s3a.threads.max + no. of worker threads
        builder.config("spark.hadoop.fs.s3a.connection.maximum", 3750);
    }
}
