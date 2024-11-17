package com.progressive.minds.chimera.core.logger;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

public class ChimeraLogger extends LogManager implements Serializable {

    private final Logger logger;

    public ChimeraLogger(Class<?> clazz) {
        logger = LogManager.getLogger(clazz);
    }

    public void logInfo(String tag, String message) {
        String formattedMessage = "[" + tag + "]: " + message;
        logger.info(formattedMessage);
    }

    public void logDebug(String tag, String message) {
        String formattedMessage = "[" + tag + "]: " + message;
        logger.debug(formattedMessage);
    }

    public void logTrace(String tag, String message) {
        String formattedMessage = "[" + tag + "]: " + message;
        logger.trace(formattedMessage);
    }

    public void logError(String tag, String message) {
        String formattedMessage = "[" + tag + "]: " + message;
        logger.error(formattedMessage);
    }

    public void logError(String tag, String message, Throwable e) {
        String formattedMessage = "[" + tag + "]: " + message;
        logger.error(formattedMessage, e);
    }

    public void logWarning(String tag, String message) {
        String formattedMessage = "[" + tag + "]: " + message;
        logger.warn(formattedMessage);
    }
}
