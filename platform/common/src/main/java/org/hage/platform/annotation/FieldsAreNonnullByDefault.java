package org.hage.platform.annotation;


import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.*;


@Documented
@Nonnull
@TypeQualifierDefault(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
public @interface FieldsAreNonnullByDefault {
    // Empty
}
