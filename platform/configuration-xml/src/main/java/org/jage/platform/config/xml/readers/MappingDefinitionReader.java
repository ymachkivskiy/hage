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


import com.google.common.collect.ImmutableMap;
import org.dom4j.Element;
import org.jage.platform.component.definition.ConfigurationException;

import java.util.Map;


/**
 * Reader which delegates to configured readers, based on an element name.
 *
 * @param <T> the type of object returned by delegate readers.
 * @author AGH AgE Team
 */
public class MappingDefinitionReader<T> implements IDefinitionReader<T> {

    private final Map<String, IDefinitionReader<T>> readers;

    /**
     * creates a MappingDefinitionReader.
     *
     * @param readers a map of delegate readers.
     */
    public MappingDefinitionReader(final Map<String, IDefinitionReader<T>> readers) {
        this.readers = ImmutableMap.copyOf(readers);
    }

    @Override
    public T read(final Element element) throws ConfigurationException {
        final String elementName = element.getName();
        final IDefinitionReader<T> reader = readers.get(elementName);
        if(reader == null) {
            throw new ConfigurationException("Invalid element type: " + elementName);
        }
        return reader.read(element);
    }
}
