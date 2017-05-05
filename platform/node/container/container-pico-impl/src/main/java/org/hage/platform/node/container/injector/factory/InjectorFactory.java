package org.hage.platform.node.container.injector.factory;


import org.hage.platform.node.container.definition.IComponentDefinition;
import org.picocontainer.ComponentAdapter;


public interface InjectorFactory<D extends IComponentDefinition> {

    /**
     * Creates an injector for the given instance provider and definition.
     *
     * @param <T>        the type of the injector
     * @param definition the definition
     * @return an injector
     */
    <T> ComponentAdapter<T> createAdapter(D definition);
}
