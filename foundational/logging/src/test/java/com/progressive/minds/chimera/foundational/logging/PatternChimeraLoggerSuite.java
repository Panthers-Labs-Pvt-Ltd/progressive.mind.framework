package com.progressive.minds.chimera.foundational.logging;

import org.apache.logging.log4j.Level;

public class PatternChimeraLoggerSuite extends ChimeraLoggerSuiteBase {

    private static final ChimeraLogger LOGGER = ChimeraLoggerFactory.getLogger("PatternChimeraLoggerSuite");

    private String toRegexPattern(Level level, String msg) {
        return msg
                .replace("<level>", level.toString())
                .replace("<className>", className());
    }

    @Override
    ChimeraLogger logger() {
        return LOGGER;
    }

    @Override
    String className() {
        return logger().logName();
    }

    @Override
    String logFilePath() {
        return "target/pattern.log";
    }

    @Override
    String expectedPatternForBasicMsg(Level level) {
        return toRegexPattern(level, ".*<level> <className>: This is a log message\n");
    }

    @Override
    String expectedPatternForBasicMsgWithEscapeChar(Level level) {
        return toRegexPattern(level,
                ".*<level> <className>: This is a log message\\nThis is a new line \\t other msg\\n");
    }

    @Override
    String expectedPatternForBasicMsgWithException(Level level) {
        return toRegexPattern(level, """
        .*<level> <className>: This is a log message
        [\\s\\S]*""");
    }

    @Override
    String expectedPatternForMsgWithMDC(Level level) {
        return toRegexPattern(level, ".*<level> <className>: Lost executor 1.\n");
    }

    @Override
    String expectedPatternForMsgWithMDCs(Level level) {
        return toRegexPattern(level,
                ".*<level> <className>: Lost executor 1, reason: the shuffle data is too large\n");
    }

    @Override
    String expectedPatternForMsgWithMDCsAndException(Level level) {
        return toRegexPattern(level,"""
      .*<level> <className>: Lost executor 1, reason: the shuffle data is too large
      [\\s\\S]*""");
    }

    @Override
    String expectedPatternForMsgWithMDCValueIsNull(Level level) {
        return toRegexPattern(level, ".*<level> <className>: Lost executor null.\n");
    }

    @Override
    String expectedPatternForScalaCustomLogKey(Level level) {
        return toRegexPattern(level, ".*<level> <className>: Scala custom log message.\n");
    }

    @Override
    String expectedPatternForJavaCustomLogKey(Level level) {
        return toRegexPattern(level, ".*<level> <className>: Java custom log message.\n");
    }
}
