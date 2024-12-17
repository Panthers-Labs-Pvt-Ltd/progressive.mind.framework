package com.progressive.minds.chimera.foundational.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;

public class StructuredChimeraLoggerSuite extends ChimeraLoggerSuiteBase {

    private static final ChimeraLogger LOGGER = ChimeraLoggerFactory.getLogger("StructuredChimeraLoggerSuite");

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private String compactAndToRegexPattern(Level level, String json) {
        try {
            return JSON_MAPPER.readTree(json).toString()
                    .replace("<level>", level.toString())
                    .replace("<className>", className())
                    .replace("<timestamp>", "[^\"]+")
                    .replace("\"<stacktrace>\"", ".*")
                    .replace("{", "\\{") + "\n";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
        return "target/structured.log";
    }

    @Override
    String expectedPatternForBasicMsg(Level level) {
        return compactAndToRegexPattern(level, """
  {
    "ts": "<timestamp>",
    "level": "<level>",
    "msg": "This is a log message",
    "logger": "<className>"
  }""");
    }

    @Override
    String expectedPatternForBasicMsgWithEscapeChar(Level level) {
        return compactAndToRegexPattern(level, """
  {
    "ts": "<timestamp>",
    "level": "<level>",
    "msg": "This is a log message\\\\nThis is a new line \\\\t other msg",
    "logger": "<className>"
  }""");
    }

    @Override
    String expectedPatternForBasicMsgWithException(Level level) {
        return compactAndToRegexPattern(level, """
  {
    "ts": "<timestamp>",
    "level": "<level>",
    "msg": "This is a log message",
    "exception": {
      "class": "java.lang.RuntimeException",
      "msg": "OOM",
      "stacktrace": "<stacktrace>"
    },
    "logger": "<className>"
  }""");
    }

    @Override
    String expectedPatternForMsgWithMDC(Level level) {
        return compactAndToRegexPattern(level, """
  {
    "ts": "<timestamp>",
    "level": "<level>",
    "msg": "Lost executor 1.",
    "context": {
      "executor_id": "1"
    },
    "logger": "<className>"
  }""");
    }

    @Override
    String expectedPatternForMsgWithMDCs(Level level) {
        return compactAndToRegexPattern(level, """
  {
    "ts": "<timestamp>",
    "level": "<level>",
    "msg": "Lost executor 1, reason: the shuffle data is too large",
    "context": {
      "executor_id": "1",
      "reason": "the shuffle data is too large"
    },
    "logger": "<className>"
  }""");
    }

    @Override
    String expectedPatternForMsgWithMDCsAndException(Level level) {
        return compactAndToRegexPattern(level, """
  {
    "ts": "<timestamp>",
    "level": "<level>",
    "msg": "Lost executor 1, reason: the shuffle data is too large",
    "context": {
      "executor_id": "1",
      "reason": "the shuffle data is too large"
    },
    "exception": {
      "class": "java.lang.RuntimeException",
      "msg": "OOM",
      "stacktrace": "<stacktrace>"
    },
    "logger": "<className>"
  }""");
    }

    @Override
    String expectedPatternForMsgWithMDCValueIsNull(Level level) {
        return compactAndToRegexPattern(level, """
  {
    "ts": "<timestamp>",
    "level": "<level>",
    "msg": "Lost executor null.",
    "context": {
      "executor_id": null
    },
    "logger": "<className>"
  }""");
    }

    @Override
    String expectedPatternForScalaCustomLogKey(Level level) {
        return compactAndToRegexPattern(level, """
  {
    "ts": "<timestamp>",
    "level": "<level>",
    "msg": "Scala custom log message.",
    "context": {
      "custom_log_key": "Scala custom log message."
    },
    "logger": "<className>"
  }""");
    }

    @Override
    String expectedPatternForJavaCustomLogKey(Level level) {
        return compactAndToRegexPattern(level, """
  {
    "ts": "<timestamp>",
    "level": "<level>",
    "msg": "Java custom log message.",
    "context": {
      "custom_log_key": "Java custom log message."
    },
    "logger": "<className>"
  }""");
    }
}
