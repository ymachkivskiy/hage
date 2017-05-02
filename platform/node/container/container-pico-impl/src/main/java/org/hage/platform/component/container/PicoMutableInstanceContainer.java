package org.hage.platform.component.container;


import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;

import java.lang.reflect.Type;
import java.util.List;


public interface PicoMutableInstanceContainer extends MutableInstanceContainer, MutablePicoContainer {

    /**
     * Retrieves all component adapters inside this container that are associated with the specified parametrized type.
     *
     * @param type           the type of the components.
     * @param typeParameters an array of type parameters.
     * @return a collection containing all the {@link ComponentAdapter}s inside this container that are associated with
     * the specified type. Changes to this collection will not be reflected in the container itself.
     */
    List<ComponentAdapter<?>> getComponentAdapters(Class<?> type, Type[] typeParameters);

}
