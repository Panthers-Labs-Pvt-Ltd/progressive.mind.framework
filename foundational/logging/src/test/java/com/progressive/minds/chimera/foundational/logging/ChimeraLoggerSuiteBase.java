package com.progressive.minds.chimera.foundational.logging;

import com.progressive.minds.chimera.foundational.logging.LogKey.LogKey;
import com.progressive.minds.chimera.foundational.logging.LogKey.LogKeys;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class ChimeraLoggerSuiteBase {

    abstract ChimeraLogger logger();
    abstract String className();
    abstract String logFilePath();

    private File logFile() throws IOException {
        String pwd = new File(".").getCanonicalPath();
        return new File(pwd + File.separator + logFilePath());
    }

    // Return the newly added log contents in the log file after executing the function `f`
    private String captureLogOutput(Runnable func) throws IOException {
        String content = "";
        if (logFile().exists()) {
            content = Files.readString(logFile().toPath());
        }
        func.run();
        String newContent = Files.readString(logFile().toPath());
        return newContent.substring(content.length());
    }

    @FunctionalInterface
    private interface ExpectedResult {
        String apply(Level level) throws IOException;
    }

    private void checkLogOutput(Level level, Runnable func, ExpectedResult result) {
        try {
            assertTrue(captureLogOutput(func).matches(result.apply(level)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final String basicMsg = "This is a log message";

    private final String basicMsgWithEscapeChar =
            "This is a log message\nThis is a new line \t other msg";

    private final MDC executorIDMDC = MDC.of(LogKeys.EXECUTOR_ID, "1");
    private final String msgWithMDC = "Lost executor {}.";

    private final MDC[] mdcs = new MDC[] {
            MDC.of(LogKeys.EXECUTOR_ID, "1"),
            MDC.of(LogKeys.REASON, "the shuffle data is too large")};
    private final String msgWithMDCs = "Lost executor {}, reason: {}";

    private final MDC[] emptyMDCs = new MDC[0];

    private final MDC executorIDMDCValueIsNull = MDC.of(LogKeys.EXECUTOR_ID, null);

    private final MDC javaCustomLogMDC =
            MDC.of(JavaCustomLogKeys.CUSTOM_LOG_KEY, "Java custom log message.");

    // test for basic message (without any mdc)
    abstract String expectedPatternForBasicMsg(Level level);

    // test for basic message (with escape char)
    abstract String expectedPatternForBasicMsgWithEscapeChar(Level level);

    // test for basic message and exception
    abstract String expectedPatternForBasicMsgWithException(Level level);

    // test for message (with mdc)
    abstract String expectedPatternForMsgWithMDC(Level level);

    // test for message (with mdcs)
    abstract String expectedPatternForMsgWithMDCs(Level level);

    // test for message (with mdcs and exception)
    abstract String expectedPatternForMsgWithMDCsAndException(Level level);

    // test for message (with empty mdcs and exception)
    String expectedPatternForMsgWithEmptyMDCsAndException(Level level) {
        return expectedPatternForBasicMsgWithException(level);
    }

    // test for message (with mdc - the value is null)
    abstract String expectedPatternForMsgWithMDCValueIsNull(Level level);

    // test for scala custom LogKey
    abstract String expectedPatternForScalaCustomLogKey(Level level);

    // test for java custom LogKey
    abstract String expectedPatternForJavaCustomLogKey(Level level);

    @Test
    public void testBasicMsg() {
        Runnable errorFn = () -> logger().logError(basicMsg);
        Runnable warnFn = () -> logger().logWarning(basicMsg);
        Runnable infoFn = () -> logger().logInfo(basicMsg);
        Runnable debugFn = () -> logger().logDebug(basicMsg);
        Runnable traceFn = () -> logger().logTrace(basicMsg);
        List.of(
                Pair.of(Level.ERROR, errorFn),
                Pair.of(Level.WARN, warnFn),
                Pair.of(Level.INFO, infoFn),
                Pair.of(Level.DEBUG, debugFn),
                Pair.of(Level.TRACE, traceFn)).forEach(pair ->
                checkLogOutput(pair.getLeft(), pair.getRight(), this::expectedPatternForBasicMsg));
    }

    @Test
    public void testBasicMsgWithEscapeChar() {
        Runnable errorFn = () -> logger().logError(basicMsgWithEscapeChar);
        Runnable warnFn = () -> logger().logWarning(basicMsgWithEscapeChar);
        Runnable infoFn = () -> logger().logInfo(basicMsgWithEscapeChar);
        Runnable debugFn = () -> logger().logDebug(basicMsgWithEscapeChar);
        Runnable traceFn = () -> logger().logTrace(basicMsgWithEscapeChar);
        List.of(
                Pair.of(Level.ERROR, errorFn),
                Pair.of(Level.WARN, warnFn),
                Pair.of(Level.INFO, infoFn),
                Pair.of(Level.DEBUG, debugFn),
                Pair.of(Level.TRACE, traceFn)).forEach(pair ->
                checkLogOutput(pair.getLeft(), pair.getRight(),
                        this::expectedPatternForBasicMsgWithEscapeChar));
    }

    @Test
    public void testBasicLoggerWithException() {
        Throwable exception = new RuntimeException("OOM");
        Runnable errorFn = () -> logger().logError(basicMsg, exception);
        Runnable warnFn = () -> logger().logWarning(basicMsg, exception);
        Runnable infoFn = () -> logger().logInfo(basicMsg, exception);
        Runnable debugFn = () -> logger().logDebug(basicMsg, exception);
        Runnable traceFn = () -> logger().logTrace(basicMsg, exception);
        List.of(
                Pair.of(Level.ERROR, errorFn),
                Pair.of(Level.WARN, warnFn),
                Pair.of(Level.INFO, infoFn),
                Pair.of(Level.DEBUG, debugFn),
                Pair.of(Level.TRACE, traceFn)).forEach(pair ->
                checkLogOutput(pair.getLeft(), pair.getRight(),
                        this::expectedPatternForBasicMsgWithException));
    }

    @Test
    public void testLoggerWithMDC() {
        Runnable errorFn = () -> logger().logError(msgWithMDC, executorIDMDC);
        Runnable warnFn = () -> logger().logWarning(msgWithMDC, executorIDMDC);
        Runnable infoFn = () -> logger().logInfo(msgWithMDC, executorIDMDC);
        List.of(
                Pair.of(Level.ERROR, errorFn),
                Pair.of(Level.WARN, warnFn),
                Pair.of(Level.INFO, infoFn)).forEach(pair ->
                checkLogOutput(pair.getLeft(), pair.getRight(), this::expectedPatternForMsgWithMDC));
    }

    @Test
    public void testLoggerWithMDCs() {
        Runnable errorFn = () -> logger().logError(msgWithMDCs, mdcs);
        Runnable warnFn = () -> logger().logWarning(msgWithMDCs, mdcs);
        Runnable infoFn = () -> logger().logInfo(msgWithMDCs, mdcs);
        List.of(
                Pair.of(Level.ERROR, errorFn),
                Pair.of(Level.WARN, warnFn),
                Pair.of(Level.INFO, infoFn)).forEach(pair ->
                checkLogOutput(pair.getLeft(), pair.getRight(), this::expectedPatternForMsgWithMDCs));
    }

    @Test
    public void testLoggerWithEmptyMDCsAndException() {
        Throwable exception = new RuntimeException("OOM");
        Runnable errorFn = () -> logger().logError(basicMsg, exception, emptyMDCs);
        Runnable warnFn = () -> logger().logWarning(basicMsg, exception, emptyMDCs);
        Runnable infoFn = () -> logger().logInfo(basicMsg, exception, emptyMDCs);
        List.of(
                Pair.of(Level.ERROR, errorFn),
                Pair.of(Level.WARN, warnFn),
                Pair.of(Level.INFO, infoFn)).forEach(pair ->
                checkLogOutput(pair.getLeft(), pair.getRight(),
                        this::expectedPatternForMsgWithEmptyMDCsAndException));
    }

    @Test
    public void testLoggerWithMDCsAndException() {
        Throwable exception = new RuntimeException("OOM");
        Runnable errorFn = () -> logger().logError(msgWithMDCs, exception, mdcs);
        Runnable warnFn = () -> logger().logWarning(msgWithMDCs, exception, mdcs);
        Runnable infoFn = () -> logger().logInfo(msgWithMDCs, exception, mdcs);
        List.of(
                Pair.of(Level.ERROR, errorFn),
                Pair.of(Level.WARN, warnFn),
                Pair.of(Level.INFO, infoFn)).forEach(pair ->
                checkLogOutput(pair.getLeft(), pair.getRight(),
                        this::expectedPatternForMsgWithMDCsAndException)
        );
    }

    @Test
    public void testLoggerWithMDCValueIsNull() {
        Runnable errorFn = () -> logger().logError(msgWithMDC, executorIDMDCValueIsNull);
        Runnable warnFn = () -> logger().logWarning(msgWithMDC, executorIDMDCValueIsNull);
        Runnable infoFn = () -> logger().logInfo(msgWithMDC, executorIDMDCValueIsNull);
        List.of(
                Pair.of(Level.ERROR, errorFn),
                Pair.of(Level.WARN, warnFn),
                Pair.of(Level.INFO, infoFn)).forEach(pair ->
                checkLogOutput(pair.getLeft(), pair.getRight(),
                        this::expectedPatternForMsgWithMDCValueIsNull));
    }

//    @Test
//    public void testLoggerWithScalaCustomLogKey() {
//        Runnable errorFn = () -> logger().logError("{}", scalaCustomLogMDC);
//        Runnable warnFn = () -> logger().logWarning("{}", scalaCustomLogMDC);
//        Runnable infoFn = () -> logger().logInfo("{}", scalaCustomLogMDC);
//        List.of(
//                Pair.of(Level.ERROR, errorFn),
//                Pair.of(Level.WARN, warnFn),
//                Pair.of(Level.INFO, infoFn)).forEach(pair ->
//                checkLogOutput(pair.getLeft(), pair.getRight(), this::expectedPatternForScalaCustomLogKey));
//    }

    @Test
    public void testLoggerWithJavaCustomLogKey() {
        Runnable errorFn = () -> logger().logError("{}", javaCustomLogMDC);
        Runnable warnFn = () -> logger().logWarning("{}", javaCustomLogMDC);
        Runnable infoFn = () -> logger().logInfo("{}", javaCustomLogMDC);
        List.of(
                Pair.of(Level.ERROR, errorFn),
                Pair.of(Level.WARN, warnFn),
                Pair.of(Level.INFO, infoFn)).forEach(pair ->
                checkLogOutput(pair.getLeft(), pair.getRight(), this::expectedPatternForJavaCustomLogKey));
    }
}

class JavaCustomLogKeys {
    // Custom `LogKey` must be `implements LogKey`
    public static class CUSTOM_LOG_KEY extends LogKey { }

    // Singleton
    public static final CUSTOM_LOG_KEY CUSTOM_LOG_KEY = new CUSTOM_LOG_KEY();
}
