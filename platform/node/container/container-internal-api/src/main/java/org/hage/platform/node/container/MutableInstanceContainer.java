package org.hage.platform.node.container;


import org.hage.platform.node.container.definition.IComponentDefinition;
import org.hage.platform.node.container.exception.ComponentException;

import java.util.Collection;


public interface MutableInstanceContainer extends InstanceContainer {

    void addComponent(IComponentDefinition compDefinition);

    void addSingletonComponent(Class<?> compImplementation);

    void addPrototypeComponent(Class<?> componentClazz);

    void addComponent(String key, Class<?> compType);

    void addComponentInstance(Object compInstance);

    void verify() throws ComponentException;

    void reconfigure(Collection<IComponentDefinition> defs);

    void initializeStatefulComponents();

    void finishStatefulComponents();

    MutableInstanceContainer newChildContainer();
}
