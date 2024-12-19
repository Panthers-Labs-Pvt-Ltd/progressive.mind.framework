package com.progressive.minds.chimera;

import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.NetworkIF;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Prometheus HTTP Server:
 * The HTTPServer exposes metrics on http://<host>:8080/metrics by default. Prometheus can scrape this endpoint.
 * Prometheus Gauges:
 * Gauges are used for metrics that can go up or down (e.g., CPU usage, memory).
 * Metrics like system_cpu_usage, system_memory_total_bytes, and network_bytes_sent_total are defined and updated regularly.
 * OSHI for Metric Collection:
 * System metrics (CPU, memory, and network) are fetched using the OSHI library.
 * Scheduled Updates:
 * A Timer updates the metrics every 5 seconds.
 * Default JVM Metrics:
 * The DefaultExports.initialize() method registers JVM-related metrics (e.g., garbage collection, memory pools) to provide additional observability.
 */
public class PrometheusMetricsDaemon {

    private static final SystemInfo systemInfo = new SystemInfo();
    private static final HardwareAbstractionLayer hardware = systemInfo.getHardware();

    // Prometheus Gauges
    private static final Gauge cpuGauge = Gauge.build()
            .name("system_cpu_usage")
            .help("System CPU Usage in percentage.")
            .register();

    private static final Gauge totalMemoryGauge = Gauge.build()
            .name("system_memory_total_bytes")
            .help("Total system memory in bytes.")
            .register();

    private static final Gauge availableMemoryGauge = Gauge.build()
            .name("system_memory_available_bytes")
            .help("Available system memory in bytes.")
            .register();

    private static final Gauge networkSentGauge = Gauge.build()
            .name("network_bytes_sent_total")
            .help("Total bytes sent over network interfaces.")
            .register();

    private static final Gauge networkReceivedGauge = Gauge.build()
            .name("network_bytes_received_total")
            .help("Total bytes received over network interfaces.")
            .register();

    public static void main(String[] args) {
        try {
            // Register JVM default metrics
            DefaultExports.initialize();

            // Start HTTP server and bind to IPv4 localhost
            System.out.println("Starting Prometheus HTTP Server on 0.0.0.0:8083...");
            HTTPServer prometheusServer = new HTTPServer("0.0.0.0", 8083);

            System.out.println("Prometheus HTTP Server started successfully. Listening on 127.0.0.1:8083...");
        } catch (IOException e) {
            System.err.println("Error starting Prometheus HTTP Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class MetricsTask extends TimerTask {
        @Override
        public void run() {
            collectMetrics();
        }
    }

    private static void collectMetrics() {
        // CPU Usage
        CentralProcessor processor = hardware.getProcessor();
        double cpuLoad = processor.getSystemCpuLoad(1) * 100;
        cpuGauge.set(cpuLoad);

        // Memory Usage
        GlobalMemory memory = hardware.getMemory();
        totalMemoryGauge.set(memory.getTotal());
        availableMemoryGauge.set(memory.getAvailable());

        // Network Usage
        long totalBytesSent = 0;
        long totalBytesReceived = 0;

        NetworkIF[] networkIFs = hardware.getNetworkIFs().toArray(new NetworkIF[0]);
        for (NetworkIF net : networkIFs) {
            net.updateAttributes();
            totalBytesSent += net.getBytesSent();
            totalBytesReceived += net.getBytesRecv();
        }
        networkSentGauge.set(totalBytesSent);
        networkReceivedGauge.set(totalBytesReceived);

        // Log to console for debugging (optional)
        System.out.printf(
                "Metrics Updated: CPU Load: %.2f%%, Memory: [Total: %d, Available: %d], Network: [Sent: %d, Received: %d]%n",
                cpuLoad, memory.getTotal(), memory.getAvailable(), totalBytesSent, totalBytesReceived);
    }
}
