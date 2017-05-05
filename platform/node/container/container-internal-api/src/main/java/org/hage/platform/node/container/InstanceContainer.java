package org.hage.platform.node.container;


import java.lang.reflect.Type;
import java.util.Collection;


public interface InstanceContainer {

    /**
     * Returns object by name which is in the container included in adapter.
     *
     * @param name the component string name (id)
     * @return required object or null if object name not found
     */
    Object getInstance(String name);

    /**
     * Returns object by type which is in the container included in the adapter.
     *
     * @param type the component class
     * @param <T>  the component type
     * @return required object or null if object of this type not found
     */
    <T> T getInstance(Class<T> type);

    /**
     * Returns an object that has a provided type and which is in the container included in the adapter. Additionally,
     * when performing a look-up generic type parameters are considered.
     *
     * @param type           the component class
     * @param typeParameters A list of type parameters.
     * @param <T>            the component type
     * @return required object or null if object of this type not found
     */
    <T> T getParametrizedInstance(Class<T> type, Type[] typeParameters);

    /**
     * Returns a collection of component instances of a given type.
     *
     * @param <T>  type of returned components
     * @param type a class of components which should be returned; it can be the class, abstract class or any interface
     *             implemented by the component instances
     * @return a collection of component instances; if no component is found, the empty collection is returned
     */
    <T> Collection<T> getInstances(Class<T> type);

    /**
     * Returns a type of the component with the given name.
     *
     * @param name The component name.
     * @return A class of the component.
     */
    Class<?> getComponentType(String name);

}
