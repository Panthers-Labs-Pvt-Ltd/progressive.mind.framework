package com.progressive.minds.chimera;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Timer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetricsCollectorDaemonTest {

    private static HTTPServer prometheusServer;
    private static final SystemInfo systemInfo = new SystemInfo();
    private static final HardwareAbstractionLayer hardware = systemInfo.getHardware();

    @BeforeAll
    public static void setUp() throws IOException {
        // Register JVM default metrics
        DefaultExports.initialize();

        // Start HTTP server and bind to IPv4 localhost
        prometheusServer = new HTTPServer("0.0.0.0", 8084);
    }

    @AfterAll
    public static void tearDown() {
        if (prometheusServer != null) {
            prometheusServer.stop();
        }
    }

    @Test
    public void testMetricsCollection() {
        // Create a Timer to run the MetricsTask
        Timer timer = new Timer();
        // timer.schedule(new MetricsCollectorDaemon.MetricsTask(), 0);

        // Wait for the metrics to be collected
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify that the metrics are registered and have values
        Double cpuGauge = CollectorRegistry.defaultRegistry.getSampleValue("system_cpu_usage");
        Double totalMemoryGauge = CollectorRegistry.defaultRegistry.getSampleValue("system_memory_total_bytes");
        Double availableMemoryGauge = CollectorRegistry.defaultRegistry.getSampleValue("system_memory_available_bytes");
        Double networkSentGauge = CollectorRegistry.defaultRegistry.getSampleValue("network_bytes_sent_total");
        Double networkReceivedGauge = CollectorRegistry.defaultRegistry.getSampleValue("network_bytes_received_total");

        // assertNotNull(cpuGauge);
//        assertNotNull(totalMemoryGauge);
//        assertNotNull(availableMemoryGauge);
//        assertNotNull(networkSentGauge);
//        assertNotNull(networkReceivedGauge);
//
//        assertTrue(cpuGauge >= 0);
//        assertTrue(totalMemoryGauge > 0);
//        assertTrue(availableMemoryGauge >= 0);
//        assertTrue(networkSentGauge >= 0);
//        assertTrue(networkReceivedGauge >= 0);
    }
}