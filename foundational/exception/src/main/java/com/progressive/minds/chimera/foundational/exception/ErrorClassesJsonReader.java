package com.progressive.minds.chimera.foundational.exception;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.module.scala.DefaultScalaModule;
import org.apache.commons.text.StringSubstitutor;


import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ErrorClassesJsonReader {

    private final Map<String, ErrorInfo> errorInfoMap;

    public ErrorClassesJsonReader(List<URL> jsonFileURLs) {
        if (jsonFileURLs.isEmpty()) {
            throw new IllegalArgumentException("JSON file URLs must not be empty");
        }

        this.errorInfoMap = jsonFileURLs.stream()
                .map(ErrorClassesJsonReader::readAsMap)
                .reduce(new HashMap<>(), (map1, map2) -> {
                    map1.putAll(map2);
                    return map1;
                });
    }

    public String getErrorMessage(String errorClass, Map<String, String> messageParameters) {
        String messageTemplate = getMessageTemplate(errorClass);
        StringSubstitutor substitutor = new StringSubstitutor(messageParameters);
        try {
            return substitutor.replace(messageTemplate.replaceAll("<([a-zA-Z\\d_-]+)>", "\\$\\{$1\\}"));
        } catch (IllegalArgumentException ex) {
            throw Exception.internalError(
                    String.format("Could not replace parameters for error class: '%s' Parameters: %s", errorClass, messageParameters),
                    (Throwable) ex
            );
        }
    }

    public String getMessageTemplate(String errorClass) {
        String[] errorClasses = errorClass.split("\\.");
        String mainErrorClass = errorClasses[0];
        String subErrorClass = errorClasses.length > 1 ? errorClasses[1] : null;

        ErrorInfo errorInfo = errorInfoMap.get(mainErrorClass);
        if (errorInfo == null) {
            throw Exception.internalError("Cannot find main error class: " + errorClass);
        }

        if (subErrorClass == null) {
            return errorInfo.getMessageTemplate();
        }

        Map<String, ErrorSubInfo> subClassMap = errorInfo.getSubClass();
        if (subClassMap == null || !subClassMap.containsKey(subErrorClass)) {
            throw Exception.internalError("Cannot find sub error class: " + errorClass);
        }

        return errorInfo.getMessageTemplate() + " " + subClassMap.get(subErrorClass).getMessageTemplate();
    }

    public String getSqlState(String errorClass) {
        String mainErrorClass = errorClass.split("\\.")[0];
        ErrorInfo errorInfo = errorInfoMap.get(mainErrorClass);
        return errorInfo != null ? errorInfo.getSqlState() : null;
    }

    private static Map<String, ErrorInfo> readAsMap(URL url) {
        try {
            JsonMapper mapper = JsonMapper.builder().addModule(new DefaultScalaModule()).build();
            Map<String, ErrorInfo> map = mapper.readValue(url, new TypeReference<TreeMap<String, ErrorInfo>>() {});
            map.keySet().stream()
                    .filter(key -> key.contains("."))
                    .findFirst()
                    .ifPresent(key -> {
                        throw Exception.internalError("Found the (sub-)error class with dots: " + key);
                    });
            return map;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read error classes JSON from: " + url, e);
        }
    }
}
