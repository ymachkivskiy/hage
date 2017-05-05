package org.hage.platform.node.container.builder;


import org.hage.platform.node.container.definition.AbstractComponentDefinition;
import org.hage.platform.node.container.definition.CollectionDefinition;
import org.hage.platform.node.container.definition.ReferenceDefinition;


/**
 * This Builder adds several methods specific to Collection Components.
 *
 * @author AGH AgE Team
 */
public final class CollectionBuilder extends AbstractBuilder {

    private CollectionDefinition definition;

    CollectionBuilder(ConfigurationBuilder parent) {
        super(parent);
    }

    @Override
    protected AbstractComponentDefinition getCurrentDefinition() {
        return definition;
    }

    @Override
    public CollectionBuilder withConstructorArg(Class<?> type, String stringValue) {
        return (CollectionBuilder) super.withConstructorArg(type, stringValue);
    }

    @Override
    public CollectionBuilder withConstructorArgRef(String targetName) {
        return (CollectionBuilder) super.withConstructorArgRef(targetName);
    }

    CollectionBuilder building(CollectionDefinition definition) {
        this.definition = definition;
        return this;
    }

    /**
     * Adds a value item to the current Collection definition. The item's type will be inferred from the definition.
     *
     * @param stringValue The item's string value.
     * @return This builder.
     */
    public CollectionBuilder withItem(String stringValue) {
        return withItem((Class<?>) definition.getElementsType(), stringValue);
    }

    /**
     * Adds a value item to the current Collection definition.
     *
     * @param type        The item's type.
     * @param stringValue The item's string value.
     * @return This builder.
     */
    public CollectionBuilder withItem(Class<?> type, String stringValue) {
        definition.addItem(value(type, stringValue));
        return this;
    }

    /**
     * Adds a single reference item to the current Collection definition.
     *
     * @param targetName The target component's name.
     * @return This builder.
     */
    public CollectionBuilder withItemRef(String targetName) {
        definition.addItem(new ReferenceDefinition(targetName));
        return this;
    }
}
