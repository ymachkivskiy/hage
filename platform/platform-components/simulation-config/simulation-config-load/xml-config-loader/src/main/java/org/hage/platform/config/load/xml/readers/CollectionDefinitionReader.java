package org.hage.platform.simulationconfig.load.xml.readers;


import org.dom4j.Element;
import org.hage.platform.component.container.definition.CollectionDefinition;
import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.component.container.definition.IArgumentDefinition;
import org.hage.platform.component.container.definition.IComponentDefinition;
import org.hage.platform.simulationconfig.load.xml.ConfigTags;

import java.util.Collection;


/**
 * Reader for collection definitions. Intended to process {@code <list>} and {@code <set>} tags.
 *
 * @author AGH AgE Team
 */
public class CollectionDefinitionReader extends AbstractDefinitionReader<IComponentDefinition> {

    @SuppressWarnings("rawtypes")
    private final Class<? extends Collection> collectionClass;

    /**
     * Creates a {@link CollectionDefinitionReader} using a given collection class.
     *
     * @param collectionClass the collection class to be used in created definitions
     */
    public CollectionDefinitionReader(@SuppressWarnings("rawtypes") final Class<? extends Collection> collectionClass) {
        this.collectionClass = collectionClass;
    }

    @Override
    public CollectionDefinition read(final Element element) throws ConfigurationException {
        final String nameAttribute = getRequiredAttribute(element, NAME);
        final String valueTypeAttribute = getRequiredAttribute(element, VALUE_TYPE);
        final String isSingletonAttribute = getRequiredAttribute(element, IS_SINGLETON);

        final CollectionDefinition definition = new CollectionDefinition(nameAttribute, collectionClass,
                                                                         toClass(valueTypeAttribute),
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
}
