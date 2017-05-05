package org.hage.platform.node.container.definition;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;


/**
 * A definition of a single component used in the AgE configuration.
 * <p>
 * Instances need to be serializable, so that they could be sent to remote nodes.
 *
 * @author AGH AgE Team
 */
public interface IComponentDefinition extends Serializable {

    /**
     * Returns the name of the component described in this definition.
     *
     * @return The name of the component.
     */
    String getName();

    /**
     * Returns a class of the component described in this definition.
     *
     * @return A class of the component.
     */
    Class<?> getType();

    /**
     * Returns a list of generic type parameters.
     * <p>
     * The returned list contains parameters in the order defined in the configuration.
     *
     * @return A list of Type instances.
     */
    List<Type> getTypeParameters();

    /**
     * Returns information about the scope of the component.
     *
     * @return True, if the component is in a singleton scope, false otherwise.
     */
    boolean isSingleton();

    /**
     * Returns a list of value providers for all constructor arguments that were defined for this component.
     *
     * @return A list of single value providers.
     */
    List<IArgumentDefinition> getConstructorArguments();

    /**
     * Returns a list of components that were defined in this component.
     *
     * @return A list of component definitions.
     */
    List<IComponentDefinition> getInnerComponentDefinitions();
}
