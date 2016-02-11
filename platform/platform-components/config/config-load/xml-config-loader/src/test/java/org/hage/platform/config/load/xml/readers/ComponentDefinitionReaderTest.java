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
import org.hage.platform.component.definition.ComponentDefinition;
import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.component.definition.IArgumentDefinition;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.load.xml.ConfigAttributes;
import org.hage.platform.config.load.xml.ConfigTags;
import org.hage.platform.config.load.xml.util.ElementBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


/**
 * Unit tests for ComponentDefinitionReader.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ComponentDefinitionReaderTest {

    @InjectMocks
    private final ComponentDefinitionReader reader = new ComponentDefinitionReader();
    @Mock
    private IDefinitionReader<IArgumentDefinition> argumentReader;
    @Mock
    private IDefinitionReader<IComponentDefinition> instanceReader;

    @Before
    public void setup() throws ConfigurationException {
        final IArgumentDefinition value = mock(IArgumentDefinition.class);
        given(argumentReader.read(Matchers.any(Element.class))).willReturn(value);
        final IComponentDefinition instance = mock(IComponentDefinition.class);
        given(instanceReader.read(Matchers.any(Element.class))).willReturn(instance);
    }

    @Test
    public void testValidBasicDefinition() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.componentElement().build();

        // when
        final ComponentDefinition definition = reader.read(element);

        // then
        assertNotNull(definition);
        assertThat(definition.getName(), org.hamcrest.Matchers.is(ElementBuilder.SOME_NAME));
        assertThat(definition.getType(), is(Object.class));
        assertThat(definition.isSingleton(), is(true));
        assertTrue(definition.getConstructorArguments().isEmpty());
        assertTrue(definition.getPropertyArguments().isEmpty());
        assertTrue(definition.getInnerComponentDefinitions().isEmpty());
    }

    @Test(expected = ConfigurationException.class)
    public void testNameAttributeIsRequired() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.element(ConfigTags.COMPONENT)
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testNameAttributeisNotEmpty() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.element(ConfigTags.COMPONENT)
                .withAttribute(ConfigAttributes.NAME, ElementBuilder.EMPTY_STRING)
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testClassAttributeIsRequired() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.element(ConfigTags.COMPONENT)
                .withAttribute(ConfigAttributes.NAME, ElementBuilder.SOME_NAME)
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testClassAttributeisNotEmpty() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.element(ConfigTags.COMPONENT)
                .withAttribute(ConfigAttributes.NAME, ElementBuilder.SOME_NAME)
                .withAttribute(ConfigAttributes.CLASS, ElementBuilder.EMPTY_STRING)
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testSingletonAttributeIsRequired() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.element(ConfigTags.COMPONENT)
                .withAttribute(ConfigAttributes.NAME, ElementBuilder.SOME_NAME)
                .withAttribute(ConfigAttributes.CLASS, ElementBuilder.SOME_CLASS)
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testSingletonAttributeisNotEmpty() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.element(ConfigTags.COMPONENT)
                .withAttribute(ConfigAttributes.NAME, ElementBuilder.SOME_NAME)
                .withAttribute(ConfigAttributes.CLASS, ElementBuilder.SOME_CLASS)
                .withAttribute(ConfigAttributes.IS_SINGLETON, ElementBuilder.EMPTY_STRING)
                .build();

        // when
        reader.read(element);
    }

    @Test
    public void testConstructorArgument() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.componentElement()
                .withBody(
                        ElementBuilder.constructorElement(ElementBuilder.anyElement()),
                        ElementBuilder.constructorElement(ElementBuilder.anyElement()))
                .build();

        // when
        final ComponentDefinition definition = reader.read(element);

        // then
        final List<IArgumentDefinition> constructorArgs = definition.getConstructorArguments();
        assertThat(constructorArgs.size(), is(2));
    }

    @Test(expected = ConfigurationException.class)
    public void testPropertyArgumentNameIsRequired() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.componentElement()
                .withBody(ElementBuilder.element(ConfigTags.PROPERTY))
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testPropertyArgumentNameisNotEmpty() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.componentElement()
                .withBody(ElementBuilder.propertyElement("", ElementBuilder.anyElement()))
                .build();

        // when
        reader.read(element);
    }

    @Test
    public void testPropertyArgument() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.componentElement()
                .withBody(
                        ElementBuilder.propertyElement("someProperty1", ElementBuilder.anyElement()),
                        ElementBuilder.propertyElement("someProperty2", ElementBuilder.anyElement()))
                .build();

        // when
        final ComponentDefinition definition = reader.read(element);

        // then
        final Map<String, IArgumentDefinition> propertyInitializers = definition.getPropertyArguments();
        assertThat(propertyInitializers.size(), is(2));
        assertTrue(propertyInitializers.containsKey("someProperty1"));
        assertTrue(propertyInitializers.containsKey("someProperty2"));
    }

    @Test
    public void testInnerDefinitions() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.componentElement()
                .withBody(
                        ElementBuilder.anyElement(),
                        ElementBuilder.anyElement(),
                        ElementBuilder.anyElement())
                .build();

        // when
        final ComponentDefinition definition = reader.read(element);

        // then
        assertThat(definition.getInnerComponentDefinitions().size(), is(3));
    }

    @Test
    public void testPropertyAndConstructorArgumentsNotInInner() throws ConfigurationException {
        // given
        final Element element = ElementBuilder.componentElement()
                .withBody(
                        ElementBuilder.anyElement(),
                        ElementBuilder.propertyElement("someProperty", ElementBuilder.anyElement()),
                        ElementBuilder.anyElement(),
                        ElementBuilder.constructorElement(ElementBuilder.anyElement()),
                        ElementBuilder.anyElement())
                .build();

        // when
        final ComponentDefinition definition = reader.read(element);

        // then
        assertThat(definition.getInnerComponentDefinitions().size(), is(3));
    }
}
