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

package org.hage.platform.component.definition;


import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Tests for the {@link CollectionDefinition} class when used for lists.
 *
 * @author AGH AgE Team
 */
public class ListDefinitionTest {

    /**
     * Tests the constructor correctness.
     */
    @Test
    public void constructorTest() {
        CollectionDefinition definition = new CollectionDefinition("a list", LinkedList.class, true);
        assertEquals("a list", definition.getName());
        assertTrue(definition.isSingleton());
        assertEquals(Object.class, definition.getElementsType());
    }

    @Test
    public void innerDefinitionTest() {
        CollectionDefinition definition = new CollectionDefinition("a list", LinkedList.class, true);
        CollectionDefinition innerDefinition = new CollectionDefinition("inner", LinkedList.class, false);
        definition.addInnerComponentDefinition(innerDefinition);
        assertEquals(1, definition.getInnerComponentDefinitions().size());
        assertEquals(innerDefinition, definition.getInnerComponentDefinitions().get(0));
    }

}
