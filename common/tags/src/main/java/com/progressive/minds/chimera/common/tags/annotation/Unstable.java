package com.progressive.minds.chimera.common.tags.annotation;

import java.lang.annotation.*;

/**
 * Indicates that a class, method, field, or package is unstable and may change in incompatible ways
 * in the future.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE})
public @interface Unstable {
}
