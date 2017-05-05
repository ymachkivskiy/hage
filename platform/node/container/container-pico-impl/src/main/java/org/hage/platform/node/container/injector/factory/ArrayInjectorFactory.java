package org.hage.platform.node.container.injector.factory;


import org.hage.platform.node.container.definition.ArrayDefinition;
import org.hage.platform.node.container.injector.ArrayInjector;
import org.hage.platform.node.container.injector.Injector;


final class ArrayInjectorFactory implements InjectorFactory<ArrayDefinition> {

    @Override
    public <T> Injector<T> createAdapter(final ArrayDefinition definition) {
        return new ArrayInjector<T>(definition);
    }
}
