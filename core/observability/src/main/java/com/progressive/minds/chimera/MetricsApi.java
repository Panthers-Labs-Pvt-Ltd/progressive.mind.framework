package com.progressive.minds.chimera;

import static spark.Spark.*;

public class MetricsApi {
    public static void main(String[] args) {
        collectorDaemon metricsDaemon = new collectorDaemon();

        port(8084);
        get("/metrics", (req, res) -> {
            res.type("application/json");
            // Replace with collected metrics as JSON
            return "{ \"cpu\": 50.0, \"memory\": { \"total\": 16384, \"used\": 8192 } }";
        });

        metricsDaemon.start();
    }
}
