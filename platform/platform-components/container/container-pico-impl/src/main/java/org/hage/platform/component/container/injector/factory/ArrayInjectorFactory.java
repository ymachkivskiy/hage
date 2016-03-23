package org.hage.platform.component.container.injector.factory;


import org.hage.platform.component.container.definition.ArrayDefinition;
import org.hage.platform.component.container.injector.ArrayInjector;
import org.hage.platform.component.container.injector.Injector;


final class ArrayInjectorFactory implements InjectorFactory<ArrayDefinition> {

    @Override
    public <T> Injector<T> createAdapter(final ArrayDefinition definition) {
        return new ArrayInjector<T>(definition);
    }
}
