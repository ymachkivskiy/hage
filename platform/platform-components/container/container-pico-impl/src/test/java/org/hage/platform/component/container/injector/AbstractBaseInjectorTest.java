package org.hage.platform.component.container.injector;


import org.hage.platform.component.container.definition.ComponentDefinition;
import org.picocontainer.Injector;


/**
 * Base class for injector tests.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractBaseInjectorTest {

    protected final ComponentDefinition anyDefinition() {
        return definitionFor(Object.class);
    }

    protected final ComponentDefinition definitionFor(final Class<?> targetClass) {
        return new ComponentDefinition("any", targetClass, false);
    }

    protected final <T> Injector<T> injectorFor(final Class<T> targetClass) {
        return injectorFor(definitionFor(targetClass));
    }

    protected abstract <T> Injector<T> injectorFor(final ComponentDefinition definition);

    protected final <T> Injector<T> injectorFor(final T instance) {
        return injectorFor(definitionFor(instance));
    }

    protected final <T> ComponentDefinition definitionFor(final T instance) {
        return definitionFor(instance.getClass());
    }
}
