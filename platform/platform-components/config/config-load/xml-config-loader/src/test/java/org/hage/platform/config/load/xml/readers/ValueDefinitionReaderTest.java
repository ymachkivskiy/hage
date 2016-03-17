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

package org.hage.platform.config.load.xml.readers;


import org.dom4j.Element;
import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.component.container.definition.ValueDefinition;
import org.hage.platform.config.load.xml.ConfigAttributes;
import org.hage.platform.config.load.xml.ConfigTags;
import org.hage.platform.config.load.xml.util.ElementBuilder;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


/**
 * Unit tests for ValueDefinitionReader.
 *
 * @author AGH AgE Team
 */
public class ValueDefinitionReaderTest {

    private final ValueDefinitionReader reader = new ValueDefinitionReader();

    @Test
    public void testValidStringDefinition() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.valueElement("String", "abc")
                .build();

        // when
        final ValueDefinition definition = reader.read(element);

        // then
        assertNotNull(definition);
        assertEquals(definition.getDesiredClass(), String.class);
        assertThat(definition.getStringValue(), is("abc"));
    }

    @Test
    public void testValidIntegerDefinition() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.valueElement("Integer", "123")
                .build();

        // when
        final ValueDefinition definition = reader.read(element);

        // then
        assertNotNull(definition);
        assertEquals(definition.getDesiredClass(), Integer.class);
        assertThat(definition.getStringValue(), is("123"));
    }


    @Test
    public void testEmptyContent() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.element(ConfigTags.VALUE)
                .withAttribute(ConfigAttributes.TYPE, "String")
                .build();

        // when
        final ValueDefinition definition = reader.read(element);

        //then
        assertNotNull(definition);
        assertThat(definition.getStringValue(), is(""));
    }

    @Test(expected = ConfigurationException.class)
    public void testTypeAttributeisRequired() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.element(ConfigTags.VALUE)
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testTypeAttributeisNotEmpty() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.element(ConfigTags.VALUE)
                .withAttribute(ConfigAttributes.TYPE, "")
                .build();

        // when
        reader.read(element);
    }
}
