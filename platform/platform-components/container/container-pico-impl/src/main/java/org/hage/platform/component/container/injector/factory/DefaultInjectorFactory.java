package org.hage.platform.component.container.injector.factory;


import com.google.common.collect.ImmutableMap;
import org.hage.platform.component.container.definition.*;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.behaviors.Cached;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;


public final class DefaultInjectorFactory implements InjectorFactory<IComponentDefinition> {

    private static final Map<Class<?>, InjectorFactory<?>> FACTORIES = ImmutableMap.<Class<?>, InjectorFactory<?>> of(
            ArrayDefinition.class, new ArrayInjectorFactory(),
            CollectionDefinition.class, new CollectionInjectorFactory(),
            MapDefinition.class, new MapInjectorFactory(),
            ComponentDefinition.class, new ComponentInjectorFactory());

    @Override
    public <T> ComponentAdapter<T> createAdapter(final IComponentDefinition definition) {
        final Class<?> definitionClass = definition.getClass();
        checkArgument(FACTORIES.containsKey(definitionClass), "Unknown definition type %s", definitionClass);

        // Warnings can be suppressed, as types are being mapped
        @SuppressWarnings("rawtypes")
        InjectorFactory injectorFactory = FACTORIES.get(definitionClass);
        @SuppressWarnings("unchecked")
        ComponentAdapter<T> adapter = injectorFactory.createAdapter(definition);

        if(definition.isSingleton()) {
            adapter = new Cached<T>(adapter);
        }

        return adapter;
    }
}
