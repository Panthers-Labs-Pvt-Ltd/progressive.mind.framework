/*
package com.progressive.minds.chimera.core.datahub.emitter;

import com.linkedin.mxe.MetadataChangeProposal;
import datahub.client.Emitter;
import datahub.client.MetadataWriteResponse;
import datahub.client.rest.RestEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import datahub.client.Callback;
import datahub.shaded.io.netty.handler.codec.http.HttpResponse;

public class RestEmitterUtil {
    private static RestEmitter emitter;
*/
/*
    public static Emitter getEmitter() {
        emitter = RestEmitter.create(b -> b
                .server("http://localhost:8080") // Replace with your DataHub server URL
                .token("YOUR_AUTH_TOKEN")        // Replace with your token if required
        );

        return emitter;
    }
     public static String SampleEmitter(MetadataChangeProposal mcpw, String dataProduct) throws IOException, ExecutionException, InterruptedException {
        // MetadataWriteResponse requestFuture = emitter.emit(mcpw, null).get();
         Emitter emitter = getEmitter();
// Non-blocking using callback
         final HttpResponse httpResponse = new HttpResponse();
        emitter.emit(mcpw, new Callback() {
            @Override
            public void onCompletion(MetadataWriteResponse response) {
                if (response.isSuccess()) {
                    System.out.println(String.format(dataProduct + "Successfully emitted metadata event for %s", mcpw.getEntityUrn()));
                } else {
                    // Get the underlying http response
                     httpResponse = (HttpResponse) response.getUnderlyingResponse();
                    System.out.println(String.format(dataProduct + "Failed to emit metadata event for %s, aspect: %s with status code: %d",
                            mcpw.getEntityUrn(), mcpw.getAspectName(), httpResponse.getStatus()));
                }
            }
            @Override
            public void onFailure(Throwable exception) {
                System.out.println(
                        String.format(dataProduct + "Failed to emit metadata event for %s, aspect: %s due to %s", mcpw.getEntityUrn(),
                                mcpw.getAspectName(), exception.getMessage()));
            }
        });
        return httpResponse.status().reasonPhrase().toString();
    }
    public static String emitProposal(MetadataChangeProposal proposal, String Type) {
        Emitter emitter = getEmitter();
        Future<MetadataWriteResponse> response = null;
        try {
            response = emitter.emit(proposal, null);
            String returnCode = response.get().getResponseContent();
            if (returnCode.contains("success")) {
                System.out.println("Domain created successfully!");
            } else {
                System.out.println(returnCode);
            }
            return returnCode;
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }*//*

}



public Optional<DatahubEmitterConfig> initializeEmitter(Config sparkConf) {
    String emitterType =
            sparkConf.hasPath(SparkConfigParser.EMITTER_TYPE)
                    ? sparkConf.getString(SparkConfigParser.EMITTER_TYPE)
                    : "rest";
    switch (emitterType) {
        case "rest":
            String gmsUrl =
                    sparkConf.hasPath(SparkConfigParser.GMS_URL_KEY)
                            ? sparkConf.getString(SparkConfigParser.GMS_URL_KEY)
                            : "http://localhost:8080";
            String token =
                    sparkConf.hasPath(SparkConfigParser.GMS_AUTH_TOKEN)
                            ? sparkConf.getString(SparkConfigParser.GMS_AUTH_TOKEN)
                            : null;
            boolean disableSslVerification =
                    sparkConf.hasPath(SparkConfigParser.DISABLE_SSL_VERIFICATION_KEY)
                            && sparkConf.getBoolean(SparkConfigParser.DISABLE_SSL_VERIFICATION_KEY);
            boolean disableChunkedEncoding =
                    sparkConf.hasPath(SparkConfigParser.REST_DISABLE_CHUNKED_ENCODING)
                            && sparkConf.getBoolean(SparkConfigParser.REST_DISABLE_CHUNKED_ENCODING);
            int retry_interval_in_sec =
                    sparkConf.hasPath(SparkConfigParser.RETRY_INTERVAL_IN_SEC)
                            ? sparkConf.getInt(SparkConfigParser.RETRY_INTERVAL_IN_SEC)
                            : 5;

            int max_retries =
                    sparkConf.hasPath(SparkConfigParser.MAX_RETRIES)
                            ? sparkConf.getInt(SparkConfigParser.MAX_RETRIES)
                            : 0;

            log.info(
                    "REST Emitter Configuration: GMS url {}{}",
                    gmsUrl,
                    (sparkConf.hasPath(SparkConfigParser.GMS_URL_KEY) ? "" : "(default)"));
            if (token != null) {
                log.info("REST Emitter Configuration: Token {}", "XXXXX");
            }

            if (disableSslVerification) {
                log.warn("REST Emitter Configuration: ssl verification will be disabled.");
            }

            RestEmitterConfig restEmitterConf =
                    RestEmitterConfig.builder()
                            .server(gmsUrl)
                            .token(token)
                            .disableSslVerification(disableSslVerification)
                            .maxRetries(max_retries)
                            .retryIntervalSec(retry_interval_in_sec)
                            .disableChunkedEncoding(disableChunkedEncoding)
                            .build();
            return Optional.of(new RestDatahubEmitterConfig(restEmitterConf));
        case "kafka":
            KafkaEmitterConfig.KafkaEmitterConfigBuilder kafkaEmitterConfig =
                    KafkaEmitterConfig.builder();
            if (sparkConf.hasPath(SparkConfigParser.KAFKA_EMITTER_BOOTSTRAP)) {
                kafkaEmitterConfig.bootstrap(
                        sparkConf.getString(SparkConfigParser.KAFKA_EMITTER_BOOTSTRAP));
            }
            if (sparkConf.hasPath(SparkConfigParser.KAFKA_EMITTER_SCHEMA_REGISTRY_URL)) {
                kafkaEmitterConfig.schemaRegistryUrl(
                        sparkConf.getString(SparkConfigParser.KAFKA_EMITTER_SCHEMA_REGISTRY_URL));
            }

            if (sparkConf.hasPath(KAFKA_EMITTER_SCHEMA_REGISTRY_CONFIG)) {
                Map<String, String> schemaRegistryConfig = new HashMap<>();
                sparkConf
                        .getConfig(KAFKA_EMITTER_SCHEMA_REGISTRY_CONFIG)
                        .entrySet()
                        .forEach(
                                entry -> {
                                    schemaRegistryConfig.put(
                                            entry.getKey(), entry.getValue().unwrapped().toString());
                                });
                kafkaEmitterConfig.schemaRegistryConfig(schemaRegistryConfig);
            }

            if (sparkConf.hasPath(KAFKA_EMITTER_PRODUCER_CONFIG)) {
                Map<String, String> kafkaConfig = new HashMap<>();
                sparkConf
                        .getConfig(KAFKA_EMITTER_PRODUCER_CONFIG)
                        .entrySet()
                        .forEach(
                                entry -> {
                                    kafkaConfig.put(entry.getKey(), entry.getValue().unwrapped().toString());
                                });
                kafkaEmitterConfig.producerConfig(kafkaConfig);
            }
            if (sparkConf.hasPath(SparkConfigParser.KAFKA_MCP_TOPIC)) {
                String mcpTopic = sparkConf.getString(SparkConfigParser.KAFKA_MCP_TOPIC);
                return Optional.of(new KafkaDatahubEmitterConfig(kafkaEmitterConfig.build(), mcpTopic));
            } else {
                return Optional.of(new KafkaDatahubEmitterConfig(kafkaEmitterConfig.build()));
            }
        case "file":
            log.info("File Emitter Configuration: File emitter will be used");
            FileEmitterConfig.FileEmitterConfigBuilder fileEmitterConfig = FileEmitterConfig.builder();
            fileEmitterConfig.fileName(sparkConf.getString(SparkConfigParser.FILE_EMITTER_FILE_NAME));
            return Optional.of(new FileDatahubEmitterConfig(fileEmitterConfig.build()));
        case "s3":
            log.info("S3 Emitter Configuration: S3 emitter will be used");
            S3EmitterConfig.S3EmitterConfigBuilder s3EmitterConfig = S3EmitterConfig.builder();
            if (sparkConf.hasPath(SparkConfigParser.S3_EMITTER_BUCKET)) {
                s3EmitterConfig.bucketName(sparkConf.getString(SparkConfigParser.S3_EMITTER_BUCKET));
            }

            if (sparkConf.hasPath(SparkConfigParser.S3_EMITTER_PREFIX)) {
                s3EmitterConfig.pathPrefix(sparkConf.getString(SparkConfigParser.S3_EMITTER_PREFIX));
            }

            if (sparkConf.hasPath(SparkConfigParser.S3_EMITTER_REGION)) {
                s3EmitterConfig.region(sparkConf.getString(SparkConfigParser.S3_EMITTER_REGION));
            }

            if (sparkConf.hasPath(S3_EMITTER_PROFILE)) {
                s3EmitterConfig.profileName(sparkConf.getString(S3_EMITTER_PROFILE));
            }

            if (sparkConf.hasPath(S3_EMITTER_ENDPOINT)) {
                s3EmitterConfig.endpoint(sparkConf.getString(S3_EMITTER_ENDPOINT));
            }

            if (sparkConf.hasPath(S3_EMITTER_ACCESS_KEY)) {
                s3EmitterConfig.accessKey(sparkConf.getString(S3_EMITTER_ACCESS_KEY));
            }

            if (sparkConf.hasPath(S3_EMITTER_SECRET_KEY)) {
                s3EmitterConfig.secretKey(sparkConf.getString(S3_EMITTER_SECRET_KEY));
            }

            if (sparkConf.hasPath(S3_EMITTER_FILE_NAME)) {
                s3EmitterConfig.fileName(sparkConf.getString(S3_EMITTER_FILE_NAME));
            }

            return Optional.of(new S3DatahubEmitterConfig(s3EmitterConfig.build()));
        default:
            log.error(
                    "DataHub Transport {} not recognized. DataHub Lineage emission will not work",
                    emitterType);
            break;
    }

    return Optional.empty();
}
*/
