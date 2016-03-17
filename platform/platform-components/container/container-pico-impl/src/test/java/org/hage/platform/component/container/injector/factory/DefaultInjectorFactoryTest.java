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
 * Created: 2012-03-15
 * $Id$
 */

package org.hage.platform.component.container.injector.factory;


import org.hage.platform.component.container.definition.*;
import org.hage.platform.component.container.injector.ArrayInjector;
import org.hage.platform.component.container.injector.CollectionInjector;
import org.hage.platform.component.container.injector.ComponentInjector;
import org.hage.platform.component.container.injector.MapInjector;
import org.junit.Test;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.behaviors.Cached;

import java.util.HashMap;
import java.util.Set;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Tests for DefaultInjectorFactory.
 *
 * @author AGH AgE Team
 */
public class DefaultInjectorFactoryTest {

    private static final String ANY = "any";

    private final DefaultInjectorFactory factory = new DefaultInjectorFactory();

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullDefinition() {
        // when
        factory.createAdapter(null);
    }

    @Test
    public void shouldRecogniseArrayDefinitions() {
        // given
        final ArrayDefinition definition = new ArrayDefinition(ANY, Object.class, false);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(ArrayInjector.class)));
    }

    @Test
    public void shouldRecogniseCollectionDefinitions() {
        // given
        final CollectionDefinition definition = new CollectionDefinition(ANY, Set.class, Object.class, false);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(CollectionInjector.class)));
    }

    @Test
    public void shouldRecogniseMapDefinitions() {
        // given
        final MapDefinition definition = new MapDefinition(ANY, HashMap.class, Object.class, Object.class, false);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(MapInjector.class)));
    }

    @Test
    public void shouldRecogniseComponentDefinitions() {
        // given
        final ComponentDefinition definition = new ComponentDefinition(ANY, Object.class, false);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(ComponentInjector.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnUnercognisedDefinition() {
        // given
        final IComponentDefinition definition = mock(IComponentDefinition.class);

        // when
        factory.createAdapter(definition);
    }

    @Test
    public void shouldWrapSingletonsInCachedAdapter() {
        // given
        final ComponentDefinition definition = new ComponentDefinition(ANY, Object.class, true);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(Cached.class)));
        assertThat(adapter.getDelegate(), is(instanceOf(ComponentInjector.class)));
    }
}
