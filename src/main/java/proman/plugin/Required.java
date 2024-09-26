package proman.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Bean's field is annotated as {@link Required} if it must be specified
 * in the plugins' extension. A Required-Annotated Field should also be annotated
 * with {@link Attribute}. Otherwise, this Annotation will have no effect.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {
}
