package org.hage.platform.node.container.builder;


import org.hage.platform.node.container.definition.IComponentDefinition;

import java.util.List;


/**
 * This interface allows to define a Jage configuration in Java, using a friendly fluent interface. <br />
 * E.g.:
 * <pre>Configuration()
 * 	.Component("component")
 * 		.withConstructorArg(String.class, "value")
 * 		.withConstructorArgRef("reference")
 * 		.withProperty("property1";, String.class, "value")
 * 		.withPropertyRef("property2";, "anotherComponent";)
 * 	.build()</pre>
 * <p>
 * Arrays, Lists, Sets and Maps can be created alike:
 * <p>
 * <pre>Configuration()
 * 	.Array("array", String.class)
 * 		.withItem("value1")
 * 		.withItem("value2")
 * 	.List("list", Integer.class)
 * 		.withItem("1")
 * 		.withItem("2")
 * 	.Set("set", Object.class)
 * 		.withItemRef("anotherComponent")
 * 	.Map("map", String.class, Object.class)
 * 		.withItem()
 * 			.key("key1")
 * 			.valueRef("anotherComponent")
 * 		.withItem()
 * 			.key("key2")
 * 			.valueRef("yetAnotherComponent")
 * 	.build()</pre>
 * <p>
 * You can also nest component definitions:
 * <p>
 * <pre>Configuration()
 * 	.Component("component")
 * 		.withInner(Configuration()
 * 			.Component("innerComponent")
 * 		)
 * 	.build()</pre>
 * Recent IDE's autocompletition features should help you see the available configuration options at each step. <br />
 * <br />
 * The default implementation is {@link ConfigurationBuilder}, which provides a helper static factory method <i>Configuration()</i> for creating
 * instances.
 *
 * @author AGH AgE Team
 */
public interface IConfigurationBuilder {

    /**
     * Declares a component with a given name, type, and scope.
     *
     * @param name        The component's name.
     * @param type        The component's type.
     * @param isSingleton whether this component has a singleton scope.
     * @return This builder
     */
    ComponentBuilder Component(String name, Class<?> type, boolean isSingleton);

    /**
     * Declares a component with a given name, type. The singleton scope will be defaulted to false.
     *
     * @param name The component's name.
     * @param type The component's type.
     * @return This builder
     */
    ComponentBuilder Component(String name, Class<?> type);

    /**
     * Alias for a component with no singleton scope.
     *
     * @param name The component's name.
     * @param type The component's type.
     * @return This builder
     */
    ComponentBuilder Agent(String name, Class<?> type);

    /**
     * Alias for a component with singleton scope.
     *
     * @param name The component's name.
     * @param type The component's type.
     * @return This builder
     */
    ComponentBuilder Strategy(String name, Class<?> type);

    /**
     * Declares a set component with a given name. The element type will be defaulted to Object and the singleton scope
     * to false.
     *
     * @param name The component's name.
     * @return This builder
     */
    CollectionBuilder Set(String name);

    /**
     * Declares a set component with a given name and element type. The singleton scope will be defaulted to false.
     *
     * @param name        The component's name.
     * @param elementType The component's element type.
     * @return This builder
     */
    CollectionBuilder Set(String name, Class<?> elementType);

    /**
     * Declares a set component with a given name, element type, and scope.
     *
     * @param name        The component's name.
     * @param elementType The component's element type.
     * @param isSingleton whether this component has a singleton scope.
     * @return This builder
     */
    CollectionBuilder Set(String name, Class<?> elementType, boolean isSingleton);

    /**
     * Declares a list component with a given name. The element type will be defaulted to Object and the singleton scope
     * to false.
     *
     * @param name The component's name.
     * @return This builder
     */
    CollectionBuilder List(String name);

    /**
     * Declares a list component with a given name and element type. The singleton scope will be defaulted to false.
     *
     * @param name        The component's name.
     * @param elementType The component's element type.
     * @return This builder
     */
    CollectionBuilder List(String name, Class<?> elementType);

    /**
     * Declares a list component with a given name, element type, and scope.
     *
     * @param name        The component's name.
     * @param elementType The component's element type.
     * @param isSingleton whether this component has a singleton scope.
     * @return This builder
     */
    CollectionBuilder List(String name, Class<?> elementType, boolean isSingleton);

    /**
     * Declares an array component with a given name and element type. The singleton scope will be defaulted to false.
     *
     * @param name        The component's name.
     * @param elementType The component's element type.
     * @return This builder
     */
    ArrayBuilder Array(String name, Class<?> elementType);

    /**
     * Declares an array component with a given name, element type, and scope.
     *
     * @param name        The component's name.
     * @param elementType The component's element type.
     * @param isSingleton whether this component has a singleton scope.
     * @return This builder
     */
    ArrayBuilder Array(String name, Class<?> elementType, boolean isSingleton);

    /**
     * Declares a map component with a given name. The key and value type will be defaulted to Object, the singleton
     * scope to false
     *
     * @param name The component's name.
     * @return This builder
     */
    MapBuilder Map(String name);

    /**
     * Declares a map component with a given name, key type and value type. The singleton scope will be defaulted to
     * false
     *
     * @param name      The component's name.
     * @param keyType   The component's key type.
     * @param valueType The component's value type.
     * @return This builder
     */
    MapBuilder Map(String name, Class<?> keyType, Class<?> valueType);

    /**
     * Declares a map component with a given name, key type, value type, and scope.
     *
     * @param name        The component's name.
     * @param keyType     The component's key type.
     * @param valueType   The component's value type.
     * @param isSingleton whether this component has a singleton scope.
     * @return This builder
     */
    MapBuilder Map(String name, Class<?> keyType, Class<?> valueType, boolean isSingleton);

    /**
     * Returns the configuration described with this builder.
     *
     * @return The configuration described.
     */
    List<IComponentDefinition> build();
}
