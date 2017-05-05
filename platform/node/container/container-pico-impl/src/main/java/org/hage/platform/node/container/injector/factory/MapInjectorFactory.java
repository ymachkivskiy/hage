package org.hage.platform.node.container.injector.factory;


import org.hage.platform.node.container.definition.MapDefinition;
import org.hage.platform.node.container.injector.Injector;
import org.hage.platform.node.container.injector.MapInjector;


final class MapInjectorFactory implements InjectorFactory<MapDefinition> {

    @Override
    public <T> Injector<T> createAdapter(final MapDefinition definition) {
        return new MapInjector<T>(definition);
    }
}
