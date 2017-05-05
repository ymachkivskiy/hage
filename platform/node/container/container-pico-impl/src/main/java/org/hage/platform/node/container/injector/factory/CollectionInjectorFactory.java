package org.hage.platform.node.container.injector.factory;


import org.hage.platform.node.container.definition.CollectionDefinition;
import org.hage.platform.node.container.injector.CollectionInjector;
import org.hage.platform.node.container.injector.Injector;


final class CollectionInjectorFactory implements InjectorFactory<CollectionDefinition> {

    @Override
    public <T> Injector<T> createAdapter(final CollectionDefinition definition) {
        return new CollectionInjector<T>(definition);
    }
}
