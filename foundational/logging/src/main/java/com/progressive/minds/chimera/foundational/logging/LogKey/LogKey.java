package com.progressive.minds.chimera.foundational.logging.LogKey;

import java.util.Locale;

public class LogKey {
    public String getName() {
        return getClass().getSimpleName().replaceAll("\\$", "").toLowerCase(Locale.ROOT);
    }
}
