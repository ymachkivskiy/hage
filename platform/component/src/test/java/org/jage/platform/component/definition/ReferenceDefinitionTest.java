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

package org.jage.platform.component.definition;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReferenceDefinitionTest {

	private final String expectedName = "target";

	private final ReferenceDefinition definition = new ReferenceDefinition(expectedName);

	@Test
	public void testTargetNameGetter() {
		// when
		String actualName = definition.getTargetName();

		// then
		assertEquals(expectedName, actualName);
	}

	@Test
	public void testEquals() {
		// then
		assertTrue(definition.equals(definition));
		assertFalse(definition.equals(new Object()));
		assertFalse(definition.equals(null));
		assertTrue(definition.equals(new ReferenceDefinition(expectedName)));
		assertFalse(definition.equals(new ReferenceDefinition("not" + expectedName)));
	}

	@Test
	public void testHashCode() {
		// given
		ReferenceDefinition other = new ReferenceDefinition(expectedName);

		// then
		assertEquals(definition.hashCode(), other.hashCode());
	}
}
