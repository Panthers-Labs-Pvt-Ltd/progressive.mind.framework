package com.progressive.minds.chimera.foundational.logging;

import org.slf4j.LoggerFactory;

public class ChimeraLoggerFactory {
    public static ChimeraLogger getLogger(String name) { return new ChimeraLogger(LoggerFactory.getLogger(name)); }

    public static ChimeraLogger getLogger(Class<?> clazz) { return new ChimeraLogger(LoggerFactory.getLogger(clazz)); }
}
