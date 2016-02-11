package org.hage.platform.config.load.xml.readers;


import org.dom4j.Element;
import org.hage.platform.component.definition.ArrayDefinition;
import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.component.definition.IArgumentDefinition;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.load.xml.ConfigTags;

import java.lang.reflect.Array;

import static org.hage.platform.config.load.xml.ConfigAttributes.*;
import static org.hage.platform.config.load.xml.ConfigUtils.*;

/**
 * Reader for array definitions. Intended to process {@code <array>} tags.
 *
 * @author AGH AgE Team
 */
public final class ArrayDefinitionReader extends AbstractDefinitionReader<IComponentDefinition> {

    @Override
    public ArrayDefinition read(final Element element) throws ConfigurationException {
        final String nameAttribute = getRequiredAttribute(element, NAME);
        final String valueTypeAttribute = getRequiredAttribute(element, VALUE_TYPE);
        final String isSingletonAttribute = getRequiredAttribute(element, IS_SINGLETON);

        final ArrayDefinition definition = new ArrayDefinition(nameAttribute,
                                                               toArrayClass(toClass(valueTypeAttribute)),
                                                               toBoolean(isSingletonAttribute));

        for(final Element child : getChildrenIncluding(element, ConfigTags.REFERENCE, ConfigTags.VALUE)) {
            final IArgumentDefinition value = getArgumentReader().read(child);
            definition.addItem(value);
        }

        for(final Element child : getChildrenExcluding(element, ConfigTags.REFERENCE, ConfigTags.VALUE)) {
            final IComponentDefinition innerDefinition = getInstanceReader().read(child);
            definition.addInnerComponentDefinition(innerDefinition);
        }

        return definition;
    }

    private Class<? extends Object> toArrayClass(final Class<?> componentType) {
        return Array.newInstance(componentType, 0).getClass();
    }
}
