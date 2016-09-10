package org.hage.platform.component.container.builder;


import org.hage.platform.component.container.definition.*;


/**
 * This Builder adds several methods specific to Map Components.
 *
 * @author AGH AgE Team
 */
public final class MapBuilder extends AbstractBuilder {

    private MapDefinition definition;

    private MapKey mapKey = new MapKey();

    private MapValue mapValue = new MapValue();

    MapBuilder(ConfigurationBuilder parent) {
        super(parent);
    }

    MapBuilder building(MapDefinition definition) {
        this.definition = definition;
        return this;
    }

    @Override
    protected AbstractComponentDefinition getCurrentDefinition() {
        return definition;
    }

    @Override
    public MapBuilder withConstructorArg(Class<?> type, String stringValue) {
        return (MapBuilder) super.withConstructorArg(type, stringValue);
    }

    @Override
    public MapBuilder withConstructorArgRef(String targetName) {
        return (MapBuilder) super.withConstructorArgRef(targetName);
    }

    /**
     * Adds an item to the current Map definition.
     *
     * @return This builder
     */
    public MapKey withItem() {
        return mapKey;
    }

    public final class MapKey {

        private IArgumentDefinition key;

        /**
         * Specifies the key of the item being added. The key's type will be inferred from the definition.
         *
         * @param stringValue The key's string value.
         * @return This builder.
         */
        public MapValue key(String stringValue) {
            return key((Class<?>) definition.getElementsKeyType(), stringValue);
        }

        /**
         * Specifies the key of the item being added.
         *
         * @param type        The key's type.
         * @param stringValue The key's string value.
         * @return This builder.
         */
        public MapValue key(Class<?> type, String stringValue) {
            key = new ValueDefinition(type, stringValue);
            return mapValue;
        }

        /**
         * Specifies the key of the item being added.
         *
         * @param targetName The key target component's name.
         * @return This builder.
         */
        public MapValue keyRef(String targetName) {
            key = new ReferenceDefinition(targetName);
            return mapValue;
        }
    }


    public final class MapValue {

        /**
         * Specifies the value of the item being added. The value's type will be inferred from the definition.
         *
         * @param stringValue The value's string value.
         * @return This builder.
         */
        public MapBuilder value(String stringValue) {
            return value((Class<?>) definition.getElementsValueType(), stringValue);
        }

        /**
         * Specifies the value of the item being added.
         *
         * @param type        The value's type.
         * @param stringValue The value's string value.
         * @return This builder.
         */
        public MapBuilder value(Class<?> type, String stringValue) {
            IArgumentDefinition value = new ValueDefinition(type, stringValue);
            definition.addItem(mapKey.key, value);
            return MapBuilder.this;
        }

        /**
         * Specifies the value of the item being added.
         *
         * @param targetName The value target component's name.
         * @return This builder.
         */
        public MapBuilder valueRef(String targetName) {
            IArgumentDefinition value = new ReferenceDefinition(targetName);
            definition.addItem(mapKey.key, value);
            return MapBuilder.this;
        }
    }
}
