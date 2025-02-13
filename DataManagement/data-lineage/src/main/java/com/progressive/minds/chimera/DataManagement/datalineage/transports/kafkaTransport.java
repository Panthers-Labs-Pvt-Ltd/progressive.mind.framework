package com.progressive.minds.chimera.DataManagement.datalineage.transports;


import java.util.List;
import java.util.Properties;

import com.progressive.minds.chimera.DataManagement.datalineage.SharedLogger;
import io.openlineage.client.OpenLineageClient;
import io.openlineage.client.transports.KafkaConfig;
import io.openlineage.client.transports.KafkaTransport;


/**
 * If a transport type is set to kafka, then the below parameters would be read and used when building KafkaProducer.
 *  This transport requires the artifact org.apache.kafka:kafka-clients:3.1.0 (or compatible) on your classpath.
 *
 *  Configuration
 *  type - string, must be "kafka". Required.
 *  topicName - string specifying the topic on what events will be sent. Required.
 *  properties - a dictionary containing a Kafka producer config as in Kafka producer config. Required.
 *  messageKey - string, key for all Kafka messages produced by transport. Optional, default value described below.
 *  Default values for messageKey are:
 *      run:{parentJob.namespace}/{parentJob.name} - for RunEvent with parent facet
 *      run:{job.namespace}/{job.name} - for RunEvent
 *      job:{job.namespace}/{job.name} - for JobEvent
 *      dataset:{dataset.namespace}/{dataset.name} - for DatasetEvent
 *
 * Behavior
 * Events are serialized to JSON, and then dispatched to the Kafka topic.
 */
public class kafkaTransport implements OpenLineageTransportTypes.KafkaAsTransport, SharedLogger {
    String LoggerTag = "[Open Lineage] - KafkaAsTransport";
    public OpenLineageClient set(String topicName, String messageKey, List<String> bootstrapServers) {
        LineageLogger.logInfo( "Setting Kafka As Open Lineage Transport Type");

        Properties kafkaProperties = new Properties();
        kafkaProperties.setProperty("bootstrap.servers", String.join(", ", bootstrapServers));
        kafkaProperties.setProperty("acks", "all");
        kafkaProperties.setProperty("retries", "3");
        kafkaProperties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProperties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaConfig kafkaConfig = new KafkaConfig(topicName, messageKey,kafkaProperties );


        OpenLineageClient client = OpenLineageClient.builder()
                .transport(
                        new KafkaTransport(kafkaConfig))
                .build();
        return client;
    }
}
