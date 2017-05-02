package org.hage.platform.component.container.injector.factory;


import org.hage.platform.component.container.definition.MapDefinition;
import org.hage.platform.component.container.injector.Injector;
import org.hage.platform.component.container.injector.MapInjector;


final class MapInjectorFactory implements InjectorFactory<MapDefinition> {

    @Override
    public <T> Injector<T> createAdapter(final MapDefinition definition) {
        return new MapInjector<T>(definition);
    }
}
