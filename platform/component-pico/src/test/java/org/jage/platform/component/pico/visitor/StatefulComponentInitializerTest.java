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
 * File: StatefulComponentVisitorTest.java
 * Created: 21-02-2013
 * Author: Daniel
 * $Id$
 */

package org.jage.platform.component.pico.visitor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.component.pico.IPicoComponentInstanceProvider;
import org.jage.platform.component.pico.PicoComponentInstanceProvider;
import org.junit.Test;
import org.picocontainer.PicoVisitor;

import com.google.common.collect.Lists;

/**
 * Tests for StatefulComponentInitializer.
 *
 * @author AGH AgE Team
 */
public class StatefulComponentInitializerTest {

	@Test
	public void shouldLookupInRootContainer() {
		// given
		final PicoComponentInstanceProvider provider = new PicoComponentInstanceProvider();
		final StatefulRegistry registry = new StatefulRegistry();
		provider.addComponentInstance(registry);
		provider.addComponent(Stateful.class);
		final PicoVisitor visitor = new StatefulComponentInitializer();

		// when
		provider.accept(visitor);

		// then
		assertThat(registry.inits.size(), is(1));
	}

	@Test
	public void shouldLookupInNestedContainer() {
		// given
		final StatefulRegistry registry = new StatefulRegistry();
		final PicoComponentInstanceProvider parent = new PicoComponentInstanceProvider();
		final IPicoComponentInstanceProvider child = parent.makeChildContainer();
		parent.addComponentInstance(registry);
		child.addComponent(Stateful.class);
		final PicoVisitor visitor = new StatefulComponentInitializer();

		// when
		parent.accept(visitor);

		// then
		assertThat(registry.inits.size(), is(1));
	}

	@Test
	public void statefulProperlyRegisters() {
		// given
		final PicoComponentInstanceProvider provider = new PicoComponentInstanceProvider();
		final StatefulRegistry registry = new StatefulRegistry();
		provider.addComponentInstance(registry);
		provider.addComponent(Stateful.class);

		// when
		final Stateful stateful = provider.getComponent(Stateful.class);

		// then
		 assertThat(registry.inits.size(), is(1));
		 assertTrue(registry.inits.contains(stateful));
	}

	/**
	 * Stateful instances register in this component, so that we know that their init and finish has been called.
	 *
	 * @author AGH AgE Team
	 */
	public static class StatefulRegistry {

		public final List<Stateful> inits = Lists.newArrayList();
		public final List<Stateful> finishes = Lists.newArrayList();

		public void registerInit(final Stateful stateful) {
			inits.add(stateful);
		}
		
		public void registerFinish(final Stateful stateful) {
			finishes.add(stateful);
		}
	}

	public static class Stateful implements IStatefulComponent {

		@Inject
		private StatefulRegistry registry;

		@Override
		public void init() throws ComponentException {
			registry.registerInit(this);
		}

		@Override
		public boolean finish() throws ComponentException {
			registry.registerFinish(this);
			return false;
		}
	}
}
