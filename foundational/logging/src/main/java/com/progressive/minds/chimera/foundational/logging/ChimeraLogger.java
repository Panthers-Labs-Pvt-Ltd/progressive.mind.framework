package com.progressive.minds.chimera.foundational.logging;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChimeraLogger implements Serializable {
    // Make the log field transient so that objects with Logging can
    // be serialized and used on another machine
    private transient Logger log_;
    private volatile boolean initialized = false;
    private volatile Level defaultRootLevel = null;
    private volatile boolean defaultChimeraLog4jConfig = false;
    private volatile boolean structuredLoggingEnabled = true;


    protected String logName() {
        return this.getClass().getName().replaceAll("\\$", "");
    }

    public ChimeraLogger(Logger slf4jLogger) {
        initializeLogIfNecessary(false);
        this.log_ = slf4jLogger;
    }

    private Logger log() {
        return log_;
    }

    /**
     * Enable Structured logging framework.
     */
    private void enableStructuredLogging() {
        structuredLoggingEnabled = true;
    }

    /**
     * Disable Structured logging framework.
     */
    private void disableStructuredLogging() {
        structuredLoggingEnabled = false;
    }

    /**
     * Return true if Structured logging framework is enabled.
     */
    private boolean isStructuredLoggingEnabled() {
        return structuredLoggingEnabled;
    }

    // withLogContext start
    private void withLogContext(Map<String, String> context, Runnable body) {
        // put into thread context only when structured logging is enabled
        CloseableThreadContext.Instance closeableThreadContextOpt = null;
        if (isStructuredLoggingEnabled()) {
            closeableThreadContextOpt = CloseableThreadContext.putAll(context);
        }

        try {
            body.run();
        } finally {
            if (closeableThreadContextOpt != null) {
                closeableThreadContextOpt.close();
            }
        }
    }

    private record MessageThrowable(String message, Throwable throwable) {
        static MessageThrowable of(String message, Throwable throwable) {
            return new MessageThrowable(message, throwable);
        }
    }

    private static final MessageFactory MESSAGE_FACTORY = ParameterizedMessageFactory.INSTANCE;

    private void withLogContext(
            String pattern,
            MDC[] mdcs,
            Throwable throwable,
            Consumer<MessageThrowable> func) {
        Map<String, String> context = new HashMap<>();
        Object[] args = new Object[mdcs.length];
        for (int index = 0; index < mdcs.length; index++) {
            MDC mdc = mdcs[index];
            String value = (mdc.value() != null) ? mdc.value().toString() : null;
            if (isStructuredLoggingEnabled()) {
                context.put(mdc.key().getName(), value);
            }
            args[index] = value;
        }
        MessageThrowable messageThrowable = MessageThrowable.of(
                MESSAGE_FACTORY.newMessage(pattern, args).getFormattedMessage(), throwable);
        try (CloseableThreadContext.Instance ignored = CloseableThreadContext.putAll(context)) {
            func.accept(messageThrowable);
        }
    }
    // withLogContext end

    // Log info start
    public void logInfo(String msg) {
        if (log().isInfoEnabled()) log().info(msg);
    }

    public void logInfo(LogEntry entry) {
        if (log().isInfoEnabled()) {
            withLogContext(entry.getContext(), () -> log().info(entry.getMessage()));
        }
    }

    public void logInfo(String msg, Throwable throwable) {
        if (log().isInfoEnabled()) log().info(msg, throwable);
    }

    public void logInfo(LogEntry entry, Throwable throwable) {
        if (log().isInfoEnabled()) {
            withLogContext(entry.getContext(), () -> log().info(entry.getMessage(), throwable));
        }
    }

    public void logInfo(String msg, MDC... mdcs) {
        if (mdcs == null || mdcs.length == 0) {
            log().info(msg);
        } else if (log().isInfoEnabled()) {
            withLogContext(msg, mdcs, null, mt -> log().info(mt.message));
        }
    }
    public void logInfo(String msg, Throwable throwable, MDC... mdcs) {
        if (mdcs == null || mdcs.length == 0) {
            log().info(msg, throwable);
        } else if (log().isInfoEnabled()) {
            withLogContext(msg, mdcs, throwable, mt -> log().info(mt.message, mt.throwable));
        }
    }
    // Log info end


    //Log debug start
    public void logDebug(String msg) {
        if (log().isDebugEnabled()) log().debug(msg);
    }

    public void logDebug(LogEntry entry) {
        if (log().isDebugEnabled()) {
            withLogContext(entry.getContext(), () -> log().debug(entry.getMessage()));
        }
    }

    public void logDebug(String msg, Throwable throwable) {
        if (log().isDebugEnabled()) log().debug(msg, throwable);
    }

    public void logDebug(LogEntry entry, Throwable throwable) {
        if (log().isDebugEnabled()) {
            withLogContext(entry.getContext(), () -> log().debug(entry.getMessage(), throwable));
        }
    }

    public void logDebug(String msg, MDC... mdcs) {
        if (mdcs == null || mdcs.length == 0) {
            log().debug(msg);
        } else if (log().isDebugEnabled()) {
            withLogContext(msg, mdcs, null, mt -> log().debug(mt.message));
        }
    }
    //Log debug end

    // Log trace start
    public void logTrace(String msg) {
        if (log().isTraceEnabled()) log().trace(msg);
    }

    public void logTrace(LogEntry entry) {
        if (log().isTraceEnabled()) {
            withLogContext(entry.getContext(), () -> log().trace(entry.getMessage()));
        }
    }

    public void logTrace(String msg, Throwable throwable) {
        if (log().isTraceEnabled()) log().trace(msg, throwable);
    }

    public void logTrace(LogEntry entry, Throwable throwable) {
        if (log().isTraceEnabled()) {
            withLogContext(entry.getContext(), () -> log().trace(entry.getMessage(), throwable));
        }
    }

    public void logTrace(String msg, MDC... mdcs) {
        if (mdcs == null || mdcs.length == 0) {
            log().trace(msg);
        } else if (log().isTraceEnabled()) {
            withLogContext(msg, mdcs, null, mt -> log().trace(mt.message));
        }
    }
    // Log trace end

    // Log warning start
    public void logWarning(String msg) {
        if (log().isWarnEnabled()) log().warn(msg);
    }

    public void logWarning(LogEntry entry) {
        if (log().isWarnEnabled()) {
            withLogContext(entry.getContext(), () -> log().warn(entry.getMessage()));
        }
    }

    public void logWarning(String msg, Throwable throwable) {
        if (log().isWarnEnabled()) log().warn(msg, throwable);
    }

    public void logWarning(LogEntry entry, Throwable throwable) {
        if (log().isWarnEnabled()) {
            withLogContext(entry.getContext(), () -> log().warn(entry.getMessage(), throwable));
        }
    }

    public void logWarning(String msg, MDC... mdcs) {
        if (mdcs == null || mdcs.length == 0) {
            log().warn(msg);
        } else if (log().isWarnEnabled()) {
            withLogContext(msg, mdcs, null, mt -> log().warn(mt.message));
        }
    }

    public void logWarning(String msg, Throwable throwable, MDC... mdcs) {
        if (mdcs == null || mdcs.length == 0) {
            log().warn(msg, throwable);
        } else if (log().isWarnEnabled()) {
            withLogContext(msg, mdcs, throwable, mt -> log().warn(mt.message, mt.throwable));
        }
    }
    // Log warning end

    // Log error start
    public void logError(String msg) {
        if (log().isErrorEnabled()) log().error(msg);
    }

    public void logError(LogEntry entry) {
        if (log().isErrorEnabled()) {
            withLogContext(entry.getContext(), () -> log().error(entry.getMessage()));
        }
    }

    public void logError(String msg, Throwable throwable) {
        if (log().isErrorEnabled()) log().error(msg, throwable);
    }

    public void logError(LogEntry entry, Throwable throwable) {
        if (log().isErrorEnabled()) {
            withLogContext(entry.getContext(), () -> log().error(entry.getMessage(), throwable));
        }
    }

    public void logError(String msg, MDC... mdcs) {
        if (mdcs == null || mdcs.length == 0) {
            logError(msg);
        } else if (log().isErrorEnabled()) {
            withLogContext(msg, mdcs, null, mt -> log().error(mt.message));
        }
    }

    public void logError(String msg, Throwable throwable, MDC... mdcs) {
        if (mdcs == null || mdcs.length == 0) {
            log().error(msg, throwable);
        } else if (log().isErrorEnabled()) {
            withLogContext(msg, mdcs, throwable, mt -> log().error(mt.message, mt.throwable));
        }
    }
    // Log error end

    public static String getStackTraceString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    // Initializations private methods
    private void initializeLogIfNecessary() {
        initializeLogIfNecessary(false);
    }

    private boolean initializeLogIfNecessary(boolean silent) {
        if (!initialized) {
            synchronized (initLock) {
                if (!initialized) {
                    initializeLogging(silent);
                    return true;
                }
            }
        }
        return false;
    }

    private static final Object initLock = new Object();

    private void initializeLogging(boolean silent) {
        if (isLog4j2()) {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            // If Log4j 2 is used but is initialized by default configuration,
            // load a default properties file
            if (defaultChimeraLog4jConfig || islog4j2DefaultConfigured()) {
                defaultChimeraLog4jConfig = true;
                // TODO
                String defaultLogProps = isStructuredLoggingEnabled() ?
                        "log4j2-defaults.properties" :
                        "log4j2-pattern-layout-defaults.properties";

                try{
                    URL defaultLogPropsUrl = getClass().getClassLoader().getResource(defaultLogProps);
                    if (defaultLogPropsUrl == null) {
                        throw new LoggingException("Could not find " + defaultLogProps);
                    }
                    URI configLocation = defaultLogPropsUrl.toURI();
                    context.setConfigLocation(configLocation);
                    if (!silent) {
                        System.err.println("Using Chimera's default log4j profile: " + defaultLogProps);
                    }
                } catch (URISyntaxException e) {
                    throw new LoggingException("Could not create the URI based on default log Props " + defaultLogProps);
                }
            }
            if (defaultRootLevel == null) {
                defaultRootLevel = context.getRootLogger().getLevel();
            }
        }
        initialized = true;
        log();
    }

    private static boolean isLog4j2() {
        // This distinguishes the log4j 1.2 binding, currently
        // org.slf4j.impl.Log4jLoggerFactory, from the log4j 2.0 binding, currently
        // org.apache.logging.slf4j.Log4jLoggerFactory
        return "org.apache.logging.slf4j.Log4jLoggerFactory"
                .equals(LoggerFactory.getILoggerFactory().getClass().getName());
    }

    /**
     * Return true if log4j2 is initialized by default configuration which has one
     * appender with error level. See `org.apache.logging.log4j.core.config.DefaultConfiguration`.
     */
    private boolean islog4j2DefaultConfigured() {
        org.apache.logging.log4j.core.Logger rootLogger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        return rootLogger.getAppenders().isEmpty() ||
                (rootLogger.getAppenders().size() == 1
                        && rootLogger.getLevel() == Level.ERROR
                        //&& LogManager.getContext().getConfiguration().isInstanceOf[DefaultConfiguration]
                );
    }
}
