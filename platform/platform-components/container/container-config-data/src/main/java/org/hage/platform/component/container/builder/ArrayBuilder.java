package org.hage.platform.component.container.builder;


import org.hage.platform.component.container.definition.AbstractComponentDefinition;
import org.hage.platform.component.container.definition.ArrayDefinition;
import org.hage.platform.component.container.definition.ReferenceDefinition;


/**
 * This Builder adds several methods specific to Array Components.
 *
 * @author AGH AgE Team
 */
public final class ArrayBuilder extends AbstractBuilder {

    private ArrayDefinition definition;

    ArrayBuilder(ConfigurationBuilder parent) {
        super(parent);
    }

    ArrayBuilder building(ArrayDefinition definition) {
        this.definition = definition;
        return this;
    }

    @Override
    protected AbstractComponentDefinition getCurrentDefinition() {
        return definition;
    }

    @Override
    public ArrayBuilder withConstructorArg(Class<?> type, String stringValue) {
        return (ArrayBuilder) super.withConstructorArg(type, stringValue);
    }

    @Override
    public ArrayBuilder withConstructorArgRef(String targetName) {
        return (ArrayBuilder) super.withConstructorArgRef(targetName);
    }

    /**
     * Adds a value item to the current Array definition. The item's type will be inferred from the definition.
     *
     * @param stringValue The item's string value.
     * @return This builder.
     */
    public ArrayBuilder withItem(String stringValue) {
        return withItem(definition.getType().getComponentType(), stringValue);
    }

    /**
     * Adds a value item to the current Array definition.
     *
     * @param type        The item's type.
     * @param stringValue The item's string value.
     * @return This builder.
     */
    public ArrayBuilder withItem(Class<?> type, String stringValue) {
        definition.addItem(value(type, stringValue));
        return this;
    }

    /**
     * Adds a reference item to the current Array definition.
     *
     * @param targetName The target component's name.
     * @return This builder.
     */
    public ArrayBuilder withItemRef(String targetName) {
        definition.addItem(new ReferenceDefinition(targetName));
        return this;
    }
}
