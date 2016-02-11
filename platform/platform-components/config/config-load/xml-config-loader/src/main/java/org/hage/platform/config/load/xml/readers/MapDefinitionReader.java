package org.hage.platform.config.load.xml.readers;


import org.dom4j.Element;
import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.component.definition.IArgumentDefinition;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.component.definition.MapDefinition;
import org.hage.platform.config.load.xml.ConfigTags;

import java.util.Map;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hage.platform.config.load.xml.ConfigAttributes.*;
import static org.hage.platform.config.load.xml.ConfigUtils.*;

public class MapDefinitionReader extends AbstractDefinitionReader<IComponentDefinition> {

    @SuppressWarnings("rawtypes")
    private final Class<? extends Map> mapClass;

    /**
     * Creates a {@link CollectionDefinitionReader} using a given map class.
     *
     * @param mapClass the map class to be used in created definitions
     */
    public MapDefinitionReader(@SuppressWarnings("rawtypes") final Class<? extends Map> mapClass) {
        this.mapClass = mapClass;
    }

    @Override
    public MapDefinition read(final Element element) throws ConfigurationException {
        final String nameAttribute = getRequiredAttribute(element, NAME);
        final String keyTypeAttribute = getRequiredAttribute(element, KEY_TYPE);
        final String valueTypeAttribute = getRequiredAttribute(element, VALUE_TYPE);
        final String isSingletonAttribute = getRequiredAttribute(element, IS_SINGLETON);

        final MapDefinition definition = new MapDefinition(nameAttribute, mapClass,
            toClass(keyTypeAttribute), toClass(valueTypeAttribute),
            toBoolean(isSingletonAttribute));

        for (final Element item : getChildrenIncluding(element, ConfigTags.ENTRY)) {
            final IArgumentDefinition key = getArgumentReader().read(getChild(getChild(item, ConfigTags.KEY)));
            final IArgumentDefinition value = getArgumentReader().read(getOnlyElement(getChildrenExcluding(item, ConfigTags.KEY)));
            definition.addItem(key, value);
        }

        for (final Element child : getChildrenExcluding(element, ConfigTags.ENTRY)) {
            final IComponentDefinition innerDefinition = getInstanceReader().read(child);
            definition.addInnerComponentDefinition(innerDefinition);
        }

        return definition;
    }
}
