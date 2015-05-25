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
 * File: PicoComponentInstanceProviderTest.java
 * Created: 19-02-2013
 * Author: Daniel
 * $Id$
 */

package org.jage.platform.component.pico;

import java.util.List;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.jage.platform.component.definition.ComponentDefinition;

/**
 * Tests for PicoComponentInstanceProvider.
 * 
 * @author AGH AgE Team
 */
public class PicoComponentInstanceProviderTest {

	@Test
	public void shouldReturnComponentOfGivenType() {
		// given
		final Class<String> componentType = String.class;
		final PicoComponentInstanceProvider provider = new PicoComponentInstanceProvider();
		provider.addComponent(new ComponentDefinition("name", componentType, true));
		
		// when
		final String component = provider.getComponent(componentType);
		
		// then
		assertNotNull(component);
	}
	
	@Test
	public void shouldReturnAllComponentsOfGivenType() {
		// given
		final Class<String> componentType = String.class;
		final PicoComponentInstanceProvider provider = new PicoComponentInstanceProvider();
		provider.addComponent(new ComponentDefinition("name1", componentType, true));
		provider.addComponent(new ComponentDefinition("name2", componentType, true));
		
		// when
		final List<String> components = provider.getComponents(componentType);
		
		// then
		assertThat(components.size(), is(2));
	}
	
//	ComponentDefinition{name=2d00fd11-e85b-4ebf-99c1-7a4180cc6923, type=class org.jage.platform.component.definition.ClassWithProperties, isSingleton=true}
//	ComponentDefinition{name=cde397fb-e636-4522-9a2e-6b87e1252b62, type=class org.jage.platform.component.definition.ClassWithProperties, isSingleton=true}
//	ComponentDefinition{name=bf1fab54-6686-4711-9b6c-92b3db294dc3, type=class org.jage.platform.component.definition.ClassWithProperties, isSingleton=true}

}
