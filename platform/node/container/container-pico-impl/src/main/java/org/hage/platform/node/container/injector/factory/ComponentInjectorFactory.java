package org.hage.platform.node.container.injector.factory;


import com.google.common.collect.ImmutableList;
import org.hage.platform.node.container.definition.ComponentDefinition;
import org.hage.platform.node.container.injector.*;
import org.picocontainer.ComponentAdapter;

import java.util.List;


final class ComponentInjectorFactory implements InjectorFactory<ComponentDefinition> {

    @Override
    public <T> Injector<T> createAdapter(final ComponentDefinition definition) {
        ComponentAdapter<T> instantiator = new ConstructorInjector<T>(definition);
        List<Injector<T>> injectors = ImmutableList.<Injector<T>> of(
                new AutowiringInjector<T>(definition),
                new PropertiesInjector<T>(definition),
                new ComponentInstanceProviderAwareInjector<T>(definition),
                new StatefulComponentInjector<T>(definition));

        return new ComponentInjector<T>(definition, instantiator, injectors);
    }
}
