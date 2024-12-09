package com.progressive.minds.chimera;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

public class OpenTelemetryConfig {

    public static OpenTelemetry initializeTelemetry() {
        // Define resource attributes (e.g., application name)
        Resource resource = Resource.getDefault()
                .merge(Resource.create((Attributes) ResourceAttributes.SERVICE_NAME, "SparkApp"));

        // Configure a logging span exporter for traces
        LoggingSpanExporter spanExporter = new LoggingSpanExporter();

        // Set up tracer provider
        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(SimpleSpanProcessor.create(spanExporter))
                .build();

        // Set up meter provider (for metrics)
        SdkMeterProvider meterProvider = SdkMeterProvider.builder()
                .setResource(resource)
                .build();

        // Initialize OpenTelemetry SDK
        OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .setMeterProvider(meterProvider)
                .buildAndRegisterGlobal();

        // Return the OpenTelemetry instance
        return GlobalOpenTelemetry.get();
    }
}
