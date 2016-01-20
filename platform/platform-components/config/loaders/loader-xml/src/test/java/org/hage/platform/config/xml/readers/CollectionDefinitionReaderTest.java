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

package org.hage.platform.config.xml.readers;


import org.dom4j.Element;
import org.hage.platform.component.definition.CollectionDefinition;
import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.component.definition.IArgumentDefinition;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.xml.ConfigAttributes;
import org.hage.platform.config.xml.ConfigTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.hage.platform.config.xml.util.ElementBuilder.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


/**
 * Unit tests for CollectionDefinitionReader.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class CollectionDefinitionReaderTest {

    @SuppressWarnings("rawtypes")
    private final Class<ArrayList> collectionClass = ArrayList.class;
    @InjectMocks
    private final CollectionDefinitionReader reader = new CollectionDefinitionReader(collectionClass);
    @Mock
    @SuppressWarnings("unused")
    private IDefinitionReader<IArgumentDefinition> argumentReader;
    @Mock
    @SuppressWarnings("unused")
    private IDefinitionReader<IComponentDefinition> instanceReader;

    @Test
    public void testValidDefinition() throws ConfigurationException {
        // given
        final Element element = listElement().build();

        // when
        final CollectionDefinition definition = reader.read(element);

        // then
        assertNotNull(definition);
        assertThat(definition.getName(), is(SOME_NAME));
        assertEquals(collectionClass, definition.getType());
        assertEquals(String.class, definition.getElementsType());
        assertThat(definition.isSingleton(), is(true));
    }

    @Test(expected = ConfigurationException.class)
    public void testNameAttributeIsRequired() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.LIST)
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testNameAttributeIsNotEmpty() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.LIST)
                .withAttribute(ConfigAttributes.NAME, "")
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testValueTypeAttributeIsRequired() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.LIST)
                .withAttribute(ConfigAttributes.NAME, SOME_NAME)
                .build();

        // when
        final CollectionDefinition definition = reader.read(element);

        // then
        assertEquals(Object.class, definition.getElementsType());
    }

    @Test(expected = ConfigurationException.class)
    public void testValueTypeAttributeIsNotEmpty() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.LIST)
                .withAttribute(ConfigAttributes.NAME, SOME_NAME)
                .withAttribute(ConfigAttributes.VALUE_TYPE, "")
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testSingletonAttributeIsRequired() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.LIST)
                .withAttribute(ConfigAttributes.NAME, SOME_NAME)
                .withAttribute(ConfigAttributes.VALUE_TYPE, SOME_CLASS)
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testSingletonAttributeIsNotEmpty() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.LIST)
                .withAttribute(ConfigAttributes.NAME, SOME_NAME)
                .withAttribute(ConfigAttributes.VALUE_TYPE, SOME_CLASS)
                .withAttribute(ConfigAttributes.IS_SINGLETON, "")
                .build();

        // when
        reader.read(element);
    }
}
