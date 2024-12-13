package com.progressive.minds.chimera.common.tags.annotation;

import java.lang.annotation.*;

/**
 * A lower-level, unstable API intended for developers.
 *
 * Developer API's might change or be removed in minor versions of Chimera.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE})
public @interface DeveloperAPI {
}
