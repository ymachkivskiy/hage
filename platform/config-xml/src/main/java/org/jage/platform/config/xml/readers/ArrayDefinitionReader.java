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

import java.lang.reflect.Array;

import org.dom4j.Element;

import org.jage.platform.component.definition.ArrayDefinition;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IArgumentDefinition;
import org.jage.platform.component.definition.IComponentDefinition;

import static org.jage.platform.config.xml.ConfigAttributes.IS_SINGLETON;
import static org.jage.platform.config.xml.ConfigAttributes.NAME;
import static org.jage.platform.config.xml.ConfigAttributes.VALUE_TYPE;
import static org.jage.platform.config.xml.ConfigTags.REFERENCE;
import static org.jage.platform.config.xml.ConfigTags.VALUE;
import static org.jage.platform.config.xml.ConfigUtils.getChildrenExcluding;
import static org.jage.platform.config.xml.ConfigUtils.getChildrenIncluding;
import static org.jage.platform.config.xml.ConfigUtils.getRequiredAttribute;
import static org.jage.platform.config.xml.ConfigUtils.toBoolean;
import static org.jage.platform.config.xml.ConfigUtils.toClass;

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

		for (final Element child : getChildrenIncluding(element, REFERENCE, VALUE)) {
			final IArgumentDefinition value = getArgumentReader().read(child);
			definition.addItem(value);
		}

		for (final Element child : getChildrenExcluding(element, REFERENCE, VALUE)) {
			final IComponentDefinition innerDefinition = getInstanceReader().read(child);
			definition.addInnerComponentDefinition(innerDefinition);
		}

		return definition;
	}

	private Class<? extends Object> toArrayClass(final Class<?> componentType) {
		return Array.newInstance(componentType, 0).getClass();
	}
}
