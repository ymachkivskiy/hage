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
 * Created: 2011-07-29
 * $Id$
 */

package org.jage.platform.component.definition;

import java.util.LinkedList;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link CollectionDefinition} class.
 * 
 * @author AGH AgE Team
 */
public class CollectionDefinitionTest {

	private static final String COLLECTION_NAME = "collection name";

	/**
	 * Tests the short constructor.
	 */
	@Test
	public void testCollectionDefinitionConstructor1() {
		CollectionDefinition definition = new CollectionDefinition(COLLECTION_NAME, LinkedList.class, true);

		assertEquals(COLLECTION_NAME, definition.getName());
		assertEquals(LinkedList.class, definition.getType());
		assertTrue(definition.isSingleton());
		assertEquals(1, definition.getTypeParameters().size());
		assertEquals(Object.class, definition.getElementsType());
	}

	/**
	 * Tests the long constructor.
	 */
	@Test
	public void testCollectionDefinitionConstructor2() {
		CollectionDefinition definition = new CollectionDefinition(COLLECTION_NAME, LinkedList.class, String.class,
		        true);

		assertEquals(COLLECTION_NAME, definition.getName());
		assertEquals(LinkedList.class, definition.getType());
		assertTrue(definition.isSingleton());
		assertEquals(1, definition.getTypeParameters().size());
		assertEquals(String.class, definition.getElementsType());
	}

}
