package com.progressive.minds.Chimera.foundational.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import static com.progressive.minds.chimera.core.utilities.chimeraUtils.ChimeraUtils.*;
import java.io.IOException;
import java.util.*;


public class ThrowableHelper {

    private static final ErrorClassesJsonReader errorReader = new ErrorClassesJsonReader(
            Collections.singletonList(getSparkClassLoader().getResource("errors.json"))
    );

    public static String getMessage(String errorClass, Map<String, String> messageParameters) {
        return getMessage(errorClass, messageParameters, "");
    }

    public static String getMessage(String errorClass, Map<String, String> messageParameters, String context) {
        String displayMessage = errorReader.getErrorMessage(errorClass, messageParameters);
        String displayQueryContext = context.isEmpty() ? "" : "\n" + context;
        String prefix = errorClass.startsWith("_LEGACY_ERROR_TEMP_") ? "" : "[" + errorClass + "] ";
        return prefix + displayMessage + displayQueryContext;
    }

    public static String getSqlState(String errorClass) {
        return errorReader.getSqlState(errorClass);
    }

    public static boolean isInternalError(String errorClass) {
        return "INTERNAL_ERROR".equals(errorClass);
    }

    public static String getMessage(Throwable e, ErrorMessageFormat format) {
        try {
            switch (format) {
                case PRETTY:
                    return e.getMessage();
                case MINIMAL:
                case STANDARD:
                    return e.getErrorClass() == null
                            ? generateLegacyErrorJson(e)
                            : generateErrorJson(e, format);
                default:
                    throw new IllegalArgumentException("Unknown format: " + format);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to generate JSON message", ex);
        }
    }

    public static String getMessage(Throwable throwable) {
        try {
            return toJsonString(generator -> {
                JsonGenerator g = generator.setPrettyPrinter(new MinimalPrettyPrinter());
                g.writeStartObject();
                g.writeStringField("errorClass", throwable.getClass().getCanonicalName());
                g.writeObjectFieldStart("messageParameters");
                g.writeStringField("message", throwable.getMessage());
                g.writeEndObject();
                g.writeEndObject();
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate JSON message", e);
        }
    }

    private static String generateLegacyErrorJson(Throwable e) throws IOException {
        return toJsonString(generator -> {
            JsonGenerator g = generator.useDefaultPrettyPrinter();
            g.writeStartObject();
            g.writeStringField("errorClass", "LEGACY");
            g.writeObjectFieldStart("messageParameters");
            g.writeStringField("message", e.getMessage());
            g.writeEndObject();
            g.writeEndObject();
        });
    }

    private static String generateErrorJson(Throwable e, ErrorMessageFormat format) throws IOException {
        return toJsonString(generator -> {
            JsonGenerator g = generator.useDefaultPrettyPrinter();
            g.writeStartObject();
            g.writeStringField("errorClass", e.getErrorClass());
            if (format == ErrorMessageFormat.STANDARD) {
                g.writeStringField("messageTemplate", errorReader.getMessageTemplate(e.getErrorClass()));
            }
            String sqlState = e.getSqlState();
            if (sqlState != null) {
                g.writeStringField("sqlState", sqlState);
            }
            Map<String, String> messageParameters = e.getMessageParameters();
            if (!messageParameters.isEmpty()) {
                g.writeObjectFieldStart("messageParameters");
                messageParameters.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .forEach(entry -> {
                            try {
                                g.writeStringField(entry.getKey(), entry.getValue().replaceAll("#\\d+", "#x"));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                g.writeEndObject();
            }
            g.writeEndObject();
        });
    }
}
