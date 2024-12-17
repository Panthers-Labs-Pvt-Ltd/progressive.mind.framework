package com.progressive.minds.chimera.common.tags.annotation;

import java.lang.annotation.*;

/**
 * A class that is considered private to the internals of Chimera -- there is a high-likelihood
 * they will be changed in future versions of Chimera.
 *
 * This should be used only when the standard Scala / Java means of protecting classes are
 * insufficient.  In particular, Java has no equivalent of private[Chimera], so we use this annotation
 * in its place.
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE})
public @interface Private {
}
