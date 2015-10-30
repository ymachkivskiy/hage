/**
 * Copyright (C) 2006 - 2012
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   Lukasz Faber
 *   Daniel Krzywicki
 *   and other students of AGH University of Science and Technology.
 *
 * This file is part of AgE.
 *
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created: 2012-01-28
 * $Id$
 */

package org.jage.platform.config.xml.readers;


import org.dom4j.Element;
import org.jage.platform.component.definition.CollectionDefinition;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IArgumentDefinition;
import org.jage.platform.component.definition.IComponentDefinition;

import java.util.Collection;

import static org.jage.platform.config.xml.ConfigAttributes.IS_SINGLETON;
import static org.jage.platform.config.xml.ConfigAttributes.NAME;
import static org.jage.platform.config.xml.ConfigAttributes.VALUE_TYPE;
import static org.jage.platform.config.xml.ConfigTags.REFERENCE;
import static org.jage.platform.config.xml.ConfigTags.VALUE;
import static org.jage.platform.config.xml.ConfigUtils.*;


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

        for(final Element child : getChildrenIncluding(element, REFERENCE, VALUE)) {
            final IArgumentDefinition value = getArgumentReader().read(child);
            definition.addItem(value);
        }

        for(final Element child : getChildrenExcluding(element, REFERENCE, VALUE)) {
            final IComponentDefinition innerDefinition = getInstanceReader().read(child);
            definition.addInnerComponentDefinition(innerDefinition);
        }

        return definition;
    }
}
