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
 * Created: 2012-04-12
 * $Id$
 */

package org.jage.platform.config.xml.loaders;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.xml.ConfigAttributes;
import org.jage.platform.config.xml.ConfigNamespaces;
import org.jage.platform.config.xml.ConfigTags;

import java.util.List;

import static com.google.common.collect.Iterables.consumingIterable;
import static com.google.common.collect.Iterables.getOnlyElement;
import static java.lang.String.format;
import static org.dom4j.DocumentHelper.createXPath;
import static org.jage.platform.config.xml.ConfigUtils.getRequiredAttribute;
import static org.jage.platform.config.xml.ConfigUtils.toInteger;


/**
 * This loader resolves {@code <multiple>} tags, duplicating their content and unwrapping them. It assumes all
 * components have names, so anonymous definition must have been already resolved.
 *
 * @author AGH AgE Team
 */
public class MultipleTagDuplicator extends AbstractDocumentLoader {

    private static final XPath COLLECTION_XPATH = initXpath(createXPath(format(
            "//%s:%s",
            ConfigNamespaces.DEFAULT.getPrefix(),
            ConfigTags.MULTIPLE.toString())));

    @Override
    public Document loadDocument(final String path) throws ConfigurationException {
        final Document document = getDelegate().loadDocument(path);
        extractMultiples(document);
        return document;
    }

    @SuppressWarnings("unchecked")
    private void extractMultiples(final Document document) throws ConfigurationException {
        for(Element multipleElement : (List<Element>) COLLECTION_XPATH.selectNodes(document)) {
            final Element collectionElement = multipleElement.getParent();
            final int count = toInteger(getRequiredAttribute(multipleElement, ConfigAttributes.COUNT));

            // We get the position of <multiple> in the parent
            int index = collectionElement.elements().indexOf(multipleElement);

            Element child = getOnlyElement(consumingIterable((List<Element>) multipleElement.elements()));
            if(!(is(child, ConfigTags.REFERENCE) || is(child, ConfigTags.VALUE))) {
                // Child is some component so we move the definition to the parent and replace it with a reference
                collectionElement.elements().add(index++, child);
                child = newReferenceElement(getRequiredAttribute(child, ConfigAttributes.NAME));
            }

            // No we are sure child is a reference or value
            // Add 'count' copies to the collection, at the same index
            for(int i = 0; i < count; i++) {
                collectionElement.elements().add(index, child.createCopy());
            }

            // Finally, remove the multiple element
            collectionElement.elements().remove(multipleElement);
        }
    }

    private boolean is(final Element element, final ConfigTags tag) {
        return tag.toString().equals(element.getName());
    }
}
