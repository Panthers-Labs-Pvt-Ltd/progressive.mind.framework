package com.progressive.minds.chimera.DataManagement.datalineage.models.Transport;


import com.progressive.minds.chimera.DataManagement.datalineage.SharedLogger;
import com.progressive.minds.chimera.DataManagement.datalineage.models.OpenLineageTransportTypes;
import io.openlineage.client.OpenLineageClient;
import io.openlineage.client.transports.s3.S3TransportConfig;
import io.openlineage.client.transports.s3.S3Transport;
/**
 * the S3 Transport emits OpenLineageChimera events to a S3 Bucket Directly.
 * Configuration
 * type - string, must be "s3". Required.
 * endpoint - string, the endpoint for S3 compliant service like MinIO, Ceph, etc. Optional
 * bucketName - string, the S3 bucket name. Required
 * fileNamePrefix - string, prefix for the event file names. It is separated from the timestamp with underscore. It can include path and file name prefix. Optional.
 *
 * Behavior
 * Events are serialized to JSON and stored in the specified S3 bucket.
 *Each event file is named based on its eventTime, converted to epoch milliseconds, with an optional prefix if configured.
 *
 * Credentials
 * To authenticate, the transport uses the default credentials provider chain. The possible authentication methods include:
 *      Java system properties
 *      Environment variables
 *      Shared credentials config file (by default ~/.aws/config)
 *      EC2 instance credentials (convenient in EMR and Glue)
 */
public class s3Transport implements OpenLineageTransportTypes.S3Transport, SharedLogger {
String LoggerTag = "[Open Lineage] - S3AsTransport";

    public OpenLineageClient set(String inS3EndPoint, String inBucketName, String inFilePrefix ) {
        LineageLogger.logInfo( "Setting S3 As Open Lineage Transport Type");
        S3TransportConfig s3Config = new S3TransportConfig();

        s3Config.setEndpoint(inS3EndPoint);
        s3Config.setBucketName(inBucketName);
        s3Config.setFileNamePrefix(inFilePrefix);

        OpenLineageClient client = OpenLineageClient.builder()
                .transport(
                        new S3Transport(s3Config))
                .build();
        return  client;
    }

}