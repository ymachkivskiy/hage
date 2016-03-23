package org.hage.platform.component.container.builder;


import org.hage.platform.component.container.definition.AbstractComponentDefinition;
import org.hage.platform.component.container.definition.ComponentDefinition;


public final class ComponentBuilder extends AbstractBuilder {

    private ComponentDefinition definition;

    ComponentBuilder(ConfigurationBuilder parent) {
        super(parent);
    }

    @Override
    protected AbstractComponentDefinition getCurrentDefinition() {
        return definition;
    }

    @Override
    public ComponentBuilder withConstructorArg(Class<?> type, String stringValue) {
        return (ComponentBuilder) super.withConstructorArg(type, stringValue);
    }

    @Override
    public ComponentBuilder withConstructorArgRef(String targetName) {
        return (ComponentBuilder) super.withConstructorArgRef(targetName);
    }

    ComponentBuilder building(ComponentDefinition definition) {
        this.definition = definition;
        return this;
    }

    /**
     * Specifies a value property for the given Component definition.
     *
     * @param propertyName The property name.
     * @param type         The property type.
     * @param stringValue  The property string value.
     * @return This builder.
     */
    public ComponentBuilder withProperty(String propertyName, Class<?> type, String stringValue) {
        definition.addPropertyArgument(propertyName, value(type, stringValue));
        return this;
    }

    /**
     * Specifies a reference property for the given Component definition.
     *
     * @param propertyName The property name.
     * @param targetName   The target component name.
     * @return This builder.
     */
    public ComponentBuilder withPropertyRef(String propertyName, String targetName) {
        definition.addPropertyArgument(propertyName, reference(targetName));
        return this;
    }
}
