package com.progressive.minds.chimera;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.NetworkIF;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Define Requirements
 * Metrics to Collect:  CPU usage, Memory usage, Disk I/O, Network traffic
 * Compatibility:   Spark and Flink engines (integrate using custom metrics or sidecars).
 * Kubernetes:
 *      Run as a sidecar container or daemonset.
 *      Collect metrics for the host pod/container.
 *
 * Core Functionality:
 *      Using OSHI library for system metrics.
 * Expose collected metrics through a REST API or push to Prometheus.
 * Deployment:
 *       Deploy as a lightweight process or sidecar in Kubernetes.
 */

public class collectorDaemon {
    private final SystemInfo systemInfo;
    private final HardwareAbstractionLayer hardware;

    public collectorDaemon() {
        this.systemInfo = new SystemInfo();
        this.hardware = systemInfo.getHardware();
    }

    public void start() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MetricsTask(), 0, 5000); // Collect every 5 seconds
    }

    private class MetricsTask extends TimerTask {
        @Override
        public void run() {
            collectAndLogMetrics();
        }
    }

    private void collectAndLogMetrics() {
        // CPU Usage
        CentralProcessor processor = hardware.getProcessor();
        double cpuLoad = processor.getSystemCpuLoad(1) * 100;

        // Memory Usage
        GlobalMemory memory = hardware.getMemory();
        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();

        // Network Usage
        NetworkIF[] networkIFs = hardware.getNetworkIFs().toArray(new NetworkIF[0]);
        for (NetworkIF net : networkIFs) {
            net.updateAttributes();
            long bytesSent = net.getBytesSent();
            long bytesReceived = net.getBytesRecv();
            System.out.printf("Interface: %s, Sent: %d, Received: %d%n", net.getDisplayName(), bytesSent, bytesReceived);
        }

        // Log metrics (can also push to a metrics collector like Prometheus)
        System.out.printf("CPU Load: %.2f%%, Total Memory: %d MB, Available Memory: %d MB%n",
                cpuLoad, totalMemory / 1024 / 1024, availableMemory / 1024 / 1024);
    }

    public static void main(String[] args) {
        collectorDaemon daemon = new collectorDaemon();
        daemon.start();
    }
}
