package com.progressive.minds.chimera.common.tags.annotation;

import java.lang.annotation.*;

/**
 * APIs that are meant to evolve towards becoming stable APIs, but are not stable APIs yet.
 * Evolving interfaces can change from one feature release to another release (i.e. 2.1 to 2.2).
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE})
public @interface Evolving {
}
