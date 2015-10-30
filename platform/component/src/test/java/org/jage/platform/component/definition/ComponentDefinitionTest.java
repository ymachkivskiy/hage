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
 * Created: 2012-02-09
 * $Id$
 */

package org.jage.platform.component.definition;


import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.Iterables.elementsEqual;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


public class ComponentDefinitionTest {

    private String name = "name";

    private Class<?> type = String.class;

    private boolean isSingleton = false;

    private ComponentDefinition definition;

    @Before
    public void setUp() {
        definition = new ComponentDefinition(name, type, isSingleton);
    }

    @Test
    public void testShouldCreateDefinition() {
        assertEquals(name, definition.getName());
        assertEquals(type, definition.getType());
        assertEquals(isSingleton, definition.isSingleton());
    }

    @Test
    public void testHasNoArgumentsByDefault() {
        // when
        Map<String, IArgumentDefinition> arguments = definition.getPropertyArguments();

        // then
        assertNotNull(arguments);
        assertTrue(arguments.isEmpty());
    }

    @Test
    public void testShouldAddArgument() {
        // given
        String propertyName = "property";
        IArgumentDefinition argument = mock(IArgumentDefinition.class);

        // when
        definition.addPropertyArgument(propertyName, argument);

        // then
        Map<String, IArgumentDefinition> arguments = definition.getPropertyArguments();
        assertNotNull(arguments);
        assertThat(arguments.size(), is(1));
        assertThat(arguments, hasEntry(propertyName, argument));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShouldThrowExceptionIfDuplicateArgument() {
        // given
        String propertyName = "property";
        IArgumentDefinition argument = mock(IArgumentDefinition.class);
        definition.addPropertyArgument(propertyName, argument);

        // when
        definition.addPropertyArgument(propertyName, argument);
    }

    @Test
    public void testShouldPreserveArgumentsOrdering() {
        // given
        String propertyName1 = "property1";
        String propertyName2 = "property2";
        String propertyName3 = "property3";
        IArgumentDefinition argument1 = mock(IArgumentDefinition.class);
        IArgumentDefinition argument2 = mock(IArgumentDefinition.class);
        IArgumentDefinition argument3 = mock(IArgumentDefinition.class);

        // when
        definition.addPropertyArgument(propertyName2, argument2);
        definition.addPropertyArgument(propertyName1, argument1);
        definition.addPropertyArgument(propertyName3, argument3);

        // then
        Map<String, IArgumentDefinition> arguments = definition.getPropertyArguments();
        assertTrue(elementsEqual(arguments.keySet(), asList(propertyName2, propertyName1, propertyName3)));
        assertTrue(elementsEqual(arguments.values(), asList(argument2, argument1, argument3)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArgumentsShouldBeImmutable() {
        // given
        String propertyName = "property";
        IArgumentDefinition argument = mock(IArgumentDefinition.class);
        Map<String, IArgumentDefinition> arguments = definition.getPropertyArguments();

        // when
        arguments.put(propertyName, argument);
    }
}
