package org.hage.platform.node.container;


import org.hage.platform.node.container.definition.IComponentDefinition;

import java.lang.reflect.Type;
import java.util.Collection;


public class DefaultSelfAwareInstanceContainer implements SelfAwareInstanceContainer {

    private final InstanceContainer provider;

    private final IComponentDefinition definition;

    public DefaultSelfAwareInstanceContainer(final InstanceContainer provider,
                                             final IComponentDefinition definition) {
        this.provider = provider;
        this.definition = definition;
    }

    @Override
    public Object getInstance() {
        return getInstance(getName());
    }

    @Override
    public String getName() {
        return definition.getName();
    }

    @Override
    public Object getInstance(final String name) {
        return provider.getInstance(name);
    }

    @Override
    public <T> T getInstance(final Class<T> type) {
        return provider.getInstance(type);
    }

    @Override
    public <T> T getParametrizedInstance(final Class<T> type, final Type[] typeParameters) {
        return provider.getParametrizedInstance(type, typeParameters);
    }

    @Override
    public <T> Collection<T> getInstances(final Class<T> type) {
        return provider.getInstances(type);
    }

    @Override
    public Class<?> getComponentType(final String name) {
        return provider.getComponentType(name);
    }
}
