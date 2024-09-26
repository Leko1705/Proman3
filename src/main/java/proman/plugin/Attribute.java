package proman.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Field of a Bean class is annotated with {@link Attribute} if
 * this field is set while Plugin-loading. Fields that are annotated
 * as an Attribute are required to be <b>not final</b>. If this
 * is a required Attribute the field should also be annotated with {@link Required}.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {

    String value();
}
