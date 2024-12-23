package com.progressive.minds.chimera.core.databaseOps.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) // For class-level annotations
public @interface Table {
    String name(); // Table name
}
