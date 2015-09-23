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
 * File: StatefulComponentFinisherTest.java
 * Created: 10-03-2014
 * Author: Daniel
 * $Id$
 */

package org.jage.platform.component.pico.visitor;


import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.pico.IPicoComponentInstanceProvider;
import org.jage.platform.component.pico.PicoComponentInstanceProvider;
import org.jage.platform.component.pico.visitor.StatefulComponentInitializerTest.Stateful;
import org.jage.platform.component.pico.visitor.StatefulComponentInitializerTest.StatefulRegistry;
import org.junit.Test;
import org.picocontainer.PicoVisitor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * Tests for StatefulComponentFinisher.
 *
 * @author AGH AgE Team
 */
public class StatefulComponentFinisherTest {

    @Test
    public void shouldCallFinishOnSingletons() {
        // given
        final PicoComponentInstanceProvider provider = new PicoComponentInstanceProvider();
        final StatefulRegistry registry = new StatefulRegistry();
        provider.addComponentInstance(registry);
        provider.addComponent(new ComponentDefinition("singleton", Stateful.class, true));
        final PicoVisitor visitor = new StatefulComponentFinisher();

        provider.getComponent("singleton");
        registry.inits.clear();

        // when
        provider.accept(visitor);

        // then
        assertThat(registry.inits.size(), is(0));
        assertThat(registry.finishes.size(), is(1));
    }

    @Test
    public void shouldNotCallFinishOnNonSingletons() {
        // given
        final PicoComponentInstanceProvider provider = new PicoComponentInstanceProvider();
        final StatefulRegistry registry = new StatefulRegistry();
        provider.addComponentInstance(registry);
        provider.addComponent(new ComponentDefinition("nonsingleton", Stateful.class, false));
        final PicoVisitor visitor = new StatefulComponentFinisher();

        // when
        provider.accept(visitor);

        // then
        assertThat(registry.inits.size(), is(0));
        assertThat(registry.finishes.size(), is(0));
    }

    @Test
    public void shouldLookupInNestedContainer() {
        // given
        final StatefulRegistry registry = new StatefulRegistry();
        final PicoComponentInstanceProvider parent = new PicoComponentInstanceProvider();
        final IPicoComponentInstanceProvider child = parent.makeChildContainer();
        parent.addComponentInstance(registry);
        child.addComponent(new ComponentDefinition("singleton", Stateful.class, true));
        final PicoVisitor visitor = new StatefulComponentFinisher();

        // when
        parent.accept(visitor);

        // then
        assertThat(registry.finishes.size(), is(1));
    }
}
