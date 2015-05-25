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
 * Created: 2011-07-13
 * $Id$
 */

package org.jage.platform.component.definition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jage.platform.component.provider.IMutableComponentInstanceProvider;

/**
 * Tests for the {@link ArrayDefinition} class.
 * 
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ArrayDefinitionTest {

	@Mock
	private IMutableComponentInstanceProvider instanceProvider;

	@Test
	public void constructorTest() {
		// when
		ArrayDefinition definition = new ArrayDefinition("an array", Object.class, true);

		// then
		assertEquals("an array", definition.getName());
		assertEquals(Object.class, definition.getType());
		assertTrue(definition.isSingleton());
	}

	@Test
	public void innerDefinitionTest() {
		// given
		ArrayDefinition definition = new ArrayDefinition("an array", Object.class, true);
		ArrayDefinition innerDefinition = new ArrayDefinition("inner", Object.class, false);

		// when
		definition.addInnerComponentDefinition(innerDefinition);

		// then
		assertEquals(1, definition.getInnerComponentDefinitions().size());
		assertEquals(innerDefinition, definition.getInnerComponentDefinitions().get(0));
	}

}
