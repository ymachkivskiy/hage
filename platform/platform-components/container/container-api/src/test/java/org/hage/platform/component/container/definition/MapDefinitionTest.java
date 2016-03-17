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
 * Created: 2009-04-20
 * $Id$
 */

package org.hage.platform.component.container.definition;


import org.junit.Test;

import java.util.HashMap;
import java.util.TreeMap;

import static org.junit.Assert.*;


/**
 * Tests for the {@link MapDefinition} class.
 *
 * @author AGH AgE Team
 */
public class MapDefinitionTest {

    private static final String MAP_NAME = "map name";

    /**
     * Tests the short constructor.
     */
    @Test
    public void testCollectionDefinitionConstructor1() {
        MapDefinition definition = new MapDefinition(MAP_NAME, true);

        assertEquals(MAP_NAME, definition.getName());
        assertEquals(HashMap.class, definition.getType());
        assertTrue(definition.isSingleton());
        assertEquals(2, definition.getTypeParameters().size());
        assertEquals(Object.class, definition.getElementsKeyType());
        assertEquals(Object.class, definition.getElementsValueType());
    }

    /**
     * Tests the long constructor.
     */
    @Test
    public void testCollectionDefinitionConstructor2() {
        MapDefinition definition = new MapDefinition(MAP_NAME, TreeMap.class, String.class, Integer.class, true);

        assertEquals(MAP_NAME, definition.getName());
        assertEquals(TreeMap.class, definition.getType());
        assertTrue(definition.isSingleton());
        assertEquals(2, definition.getTypeParameters().size());
        assertEquals(String.class, definition.getElementsKeyType());
        assertEquals(Integer.class, definition.getElementsValueType());
    }

    @Test
    public void innerDefinitionTest() {
        MapDefinition definition = new MapDefinition("a map", false);
        MapDefinition innerDefinition = new MapDefinition("inner map", true);
        definition.addInnerComponentDefinition(innerDefinition);

        assertEquals(1, definition.getInnerComponentDefinitions().size());
        assertSame(innerDefinition, definition.getInnerComponentDefinitions().get(0));
    }
}

