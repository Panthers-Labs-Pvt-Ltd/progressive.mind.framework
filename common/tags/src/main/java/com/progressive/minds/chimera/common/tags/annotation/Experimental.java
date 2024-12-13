package com.progressive.minds.chimera.common.tags.annotation;

import java.lang.annotation.*;

/**
 * An experimental user-facing API.
 * Experimental API's might change or be removed in minor versions of Spark, or be adopted as
 * first-class Chimera API's.
 * Indicates that a class, method, field, or package is private and should not be used by
 * external code. This is a stronger indication than {@link Unstable}.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE})
public @interface Experimental {
}
