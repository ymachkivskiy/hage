package org.hage.platform.annotation.di;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Configuration
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PlugableConfiguration {
    @AliasFor(annotation = Configuration.class, attribute = "value")
    String name() default "";
}
