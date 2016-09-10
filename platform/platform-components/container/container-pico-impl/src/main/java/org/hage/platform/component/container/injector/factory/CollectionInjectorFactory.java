package org.hage.platform.component.container.injector.factory;


import org.hage.platform.component.container.definition.CollectionDefinition;
import org.hage.platform.component.container.injector.CollectionInjector;
import org.hage.platform.component.container.injector.Injector;


final class CollectionInjectorFactory implements InjectorFactory<CollectionDefinition> {

    @Override
    public <T> Injector<T> createAdapter(final CollectionDefinition definition) {
        return new CollectionInjector<T>(definition);
    }
}
