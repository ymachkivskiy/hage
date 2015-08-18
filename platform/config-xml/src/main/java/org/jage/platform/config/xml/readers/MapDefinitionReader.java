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

import java.util.Map;

import org.dom4j.Element;

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IArgumentDefinition;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.definition.MapDefinition;

import static org.jage.platform.config.xml.ConfigAttributes.IS_SINGLETON;
import static org.jage.platform.config.xml.ConfigAttributes.KEY_TYPE;
import static org.jage.platform.config.xml.ConfigAttributes.NAME;
import static org.jage.platform.config.xml.ConfigAttributes.VALUE_TYPE;
import static org.jage.platform.config.xml.ConfigTags.ENTRY;
import static org.jage.platform.config.xml.ConfigTags.KEY;
import static org.jage.platform.config.xml.ConfigUtils.getChild;
import static org.jage.platform.config.xml.ConfigUtils.getChildrenExcluding;
import static org.jage.platform.config.xml.ConfigUtils.getChildrenIncluding;
import static org.jage.platform.config.xml.ConfigUtils.getRequiredAttribute;
import static org.jage.platform.config.xml.ConfigUtils.toBoolean;
import static org.jage.platform.config.xml.ConfigUtils.toClass;

import static com.google.common.collect.Iterables.getOnlyElement;

/**
 * Reader for map definitions. Intended to process {@code <map>} tags.
 *
 * @author AGH AgE Team
 */
public class MapDefinitionReader extends AbstractDefinitionReader<IComponentDefinition> {

	@SuppressWarnings("rawtypes")
	private final Class<? extends Map> mapClass;

	/**
	 * Creates a {@link CollectionDefinitionReader} using a given map class.
	 *
	 * @param mapClass
	 *            the map class to be used in created definitions
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

		for (final Element item : getChildrenIncluding(element, ENTRY)) {
			final IArgumentDefinition key = getArgumentReader().read(getChild(getChild(item, KEY)));
			final IArgumentDefinition value = getArgumentReader().read(getOnlyElement(getChildrenExcluding(item, KEY)));
			definition.addItem(key, value);
		}

		for (final Element child : getChildrenExcluding(element, ENTRY)) {
			final IComponentDefinition innerDefinition = getInstanceReader().read(child);
			definition.addInnerComponentDefinition(innerDefinition);
		}

		return definition;
	}
}
