package com.progressive.minds.chimera.core.api_service;

import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CheckDatabaseConnection {
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(CheckDatabaseConnection.class);
    public static void main(String[] args) {
        if (args.length < 3) {
            logger.logError("Missing required database connection parameters.");
            System.exit(1);
        }

        String url = args[0];
        String user = args[1];
        String password = args[2];

        try (Connection ignored = DriverManager.getConnection(url, user, password)) {
            logger.logInfo("PostgreSQL is available.");
        } catch (SQLException e) {
            logger.logError("PostgreSQL is not available.");
            System.exit(1); // Exit with error code if PostgreSQL is not available
        }
    }
}
