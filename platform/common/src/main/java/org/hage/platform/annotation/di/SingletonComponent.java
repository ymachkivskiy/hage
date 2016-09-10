package org.hage.platform.annotation.di;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Retention(RUNTIME)
@Target(TYPE)
@Component
@Scope(SCOPE_SINGLETON)
public @interface SingletonComponent {
}
