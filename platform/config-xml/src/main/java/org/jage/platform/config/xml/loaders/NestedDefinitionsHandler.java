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
 * Created: 2012-04-10
 * $Id$
 */

package org.jage.platform.config.xml.loaders;

import java.util.List;

import static java.lang.String.format;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;

import static org.dom4j.DocumentHelper.createXPath;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.xml.ConfigAttributes;
import org.jage.platform.config.xml.ConfigNamespaces;
import org.jage.platform.config.xml.ConfigTags;

import static org.jage.platform.config.xml.ConfigUtils.getRequiredAttribute;

/**
 * This Loader extracts components nested in {@code <property>}, {@code <constructor-arg>}, collections and maps, add
 * them to the context and replace them with references. It assumes all components have names, so anonymous definition
 * must have been already resolved.
 *
 * @author AGH AgE Team
 */
public final class NestedDefinitionsHandler extends AbstractDocumentLoader {

	private static final XPath  CTR_PROP_XPATH = initXpath(createXPath(format(
			"//%1$s:%2$s/*[@%4$s] | //%1$s:%3$s/*[@%4$s]",
			ConfigNamespaces.DEFAULT.getPrefix(),
	        ConfigTags.CONSTRUCTOR_ARG.toString(),
	        ConfigTags.PROPERTY.toString(),
	        ConfigAttributes.NAME.toString())));

	private static final XPath  COLLECTION_XPATH = initXpath(createXPath(format(
			"//%1$s:%2$s/*[@%5$s] | //%1$s:%3$s/*[@%5$s] | //%1$s:%4$s/*[@%5$s]",
	        ConfigNamespaces.DEFAULT.getPrefix(),
	        ConfigTags.LIST.toString(),
	        ConfigTags.SET.toString(),
	        ConfigTags.ARRAY.toString(),
	        ConfigAttributes.NAME.toString())));

	private static final XPath KEY_XPATH = initXpath(createXPath(format(
			"//%s:%s/*[@%s]",
			ConfigNamespaces.DEFAULT.getPrefix(),
	        ConfigTags.KEY.toString(),
	        ConfigAttributes.NAME.toString())));

	private static final XPath ENTRY_XPATH = initXpath(createXPath(format(
			"//%s:%s/*[@%s]",
			ConfigNamespaces.DEFAULT.getPrefix(),
	        ConfigTags.ENTRY.toString(),
	        ConfigAttributes.NAME.toString())));

	@Override
	public Document loadDocument(final String path) throws ConfigurationException {
		final Document document = getDelegate().loadDocument(path);
		extractFromConstructorsAndProperties(document);
		extractFromCollections(document);
		extractFromMaps(document);
		return document;
	}

	@SuppressWarnings("unchecked")
    private void extractFromConstructorsAndProperties(final Document document) throws ConfigurationException {
    	for (final Element element : (List<Element>)CTR_PROP_XPATH.selectNodes(document)) {
    		final Element ctrOrPropElement = element.getParent();
    		final Element componentElement = ctrOrPropElement.getParent();

    		// first we replace the element with a reference one
			final String nameAttribute = getRequiredAttribute(element, ConfigAttributes.NAME);
			ctrOrPropElement.remove(element);
			ctrOrPropElement.add(newReferenceElement(nameAttribute));

			// then we append it after the parent element
			final int index = componentElement.elements().indexOf(ctrOrPropElement);
			componentElement.elements().add(index + 1, element);
		}
    }

	@SuppressWarnings("unchecked")
    private void extractFromCollections(final Document document) {
    	for (final Element element : (List<Element>)COLLECTION_XPATH.selectNodes(document)) {
    		final Element listElement = element.getParent();

    		// we append to the list a reference just after the element
    		final int index = listElement.elements().indexOf(element);
    		final String nameAttribute = element.attributeValue(ConfigAttributes.NAME.toString());
			listElement.elements().add(index + 1, newReferenceElement(nameAttribute));
		}
    }

	@SuppressWarnings("unchecked")
	private void extractFromMaps(final Document document) {
		// We extract from keys later, so that their definitions come first, after the entry itself

		for (final Element element : selectNodes(ENTRY_XPATH, document)) {
			final Element entryElement = element.getParent();
			final Element mapElement = entryElement.getParent();

			// first we replace the element with a reference one
			final String nameAttribute = element.attributeValue(ConfigAttributes.NAME.toString());
			entryElement.remove(element);
			entryElement.add(newReferenceElement(nameAttribute));

			// then we append it after the entry element
			final int index = mapElement.elements().indexOf(entryElement);
			mapElement.elements().add(index + 1, element);
		}

		for (final Element element : selectNodes(KEY_XPATH, document)) {
			final Element keyElement = element.getParent();
			final Element entryElement = keyElement.getParent();
			final Element mapElement = entryElement.getParent();

			// first we replace the element with a reference one
			final String nameAttribute = element.attributeValue(ConfigAttributes.NAME.toString());
			keyElement.remove(element);
			keyElement.add(newReferenceElement(nameAttribute));

			// then we append it after the entry element
			final int index = mapElement.elements().indexOf(entryElement);
			mapElement.elements().add(index + 1, element);
		}
    }
}
