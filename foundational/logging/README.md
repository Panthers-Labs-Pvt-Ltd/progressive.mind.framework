# Guidelines for the Structured Logging Framework - Java Logging

Use the ```com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory``` to get the logger instance in Java code:

* Getting Logger Instance: Instead of using `org.slf4j.LoggerFactory`, use `com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory` to ensure structured logging.

```java
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

private static final ChimeraLogger LOGGER = ChimeraLoggerFactory.getLogger();
```

* Logging Messages with Variables: When logging messages with variables, wrap all the variables with `MDC`s and they will be automatically added to the Mapped Diagnostic Context (MDC). This allows for structured logging and better log analysis.

```java
import com.progressive.minds.chimera.foundational.logging.LogKey.LogKeys;
import com.progressive.minds.chimera.foundational.logging.MDC;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger();
logger.logError("Unable to delete file for partition {}", MDC.of(LogKeys.PARTITION_ID, i));
```

* Constant String Messages: For logging constant string messages, use the standard logging methods.

```java
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;

private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger();
logger.logError("Failed to abort the writer after failing to write map output.", e);
```

* If you want to output logs in `java code` through the structured log framework, you can define `custom LogKey` and use it in `java` code as follows:

```java
// To add a `custom LogKey`, implement `LogKey`
public static class CUSTOM_LOG_KEY implements LogKey { }
 
import org.apache.spark.internal.MDC;
logger.error("Unable to delete key {} for cache", MDC.of(CUSTOM_LOG_KEY, "key"));
//
```

* Exceptions: To ensure logs are compatible with Spark SQL and log analysis tools, avoid `Exception.printStackTrace()`. Use `logError`, `logWarning`, and `logInfo` methods from the `ChimeraLogger` to log exceptions, maintaining structured and parsable logs. In such cases, it would be useful to pass MDC. Mapped Diagnostic Context (MDC) will be used in log messages. The values of the MDC will be inline in the log message, while the key-value pairs will be part of the ThreadContext.