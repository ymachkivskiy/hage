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

import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IArgumentDefinition;
import org.jage.platform.component.definition.ValueDefinition;

import static org.jage.platform.config.xml.ConfigAttributes.TYPE;
import static org.jage.platform.config.xml.ConfigUtils.getRequiredAttribute;
import static org.jage.platform.config.xml.ConfigUtils.toClass;

/**
 * Reader for value definitions. Intended to process {@code <value>} tags.
 *
 * @author AGH AgE Team
 */
public class ValueDefinitionReader implements IDefinitionReader<IArgumentDefinition> {

	@Override
    public ValueDefinition read(final Element element) throws ConfigurationException {
		final String type = getRequiredAttribute(element, TYPE);
		final String value = element.getText();
		return new ValueDefinition(toClass(type), value);
    }
}
