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

package org.hage.platform.component.container.injector;


import org.hage.platform.component.container.*;
import org.hage.platform.component.container.definition.ComponentDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.picocontainer.Injector;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


/**
 * Tests for StatefulComponentInjector.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ComponentInstanceProviderAwareInjectorTest extends AbstractBaseInjectorTest {

    @Mock
    private PicoMutableInstanceContainer container;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullDefinition() {
        // when
        new StatefulComponentInjector<Object>(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotSupportCreatingComponents() {
        // given
        final Injector<Object> injector = injectorFor(anyDefinition());

        // when
        injector.getComponentInstance(container, null);
    }

    @Override
    protected <T> Injector<T> injectorFor(final ComponentDefinition definition) {
        return new ComponentInstanceProviderAwareInjector<T>(definition);
    }

    @Test
    public void shouldDoNothingIfInterfacesNotImplemented() {
        // given
        final Object instance = mock(Object.class);
        final Injector<Object> injector = injectorFor(instance);

        // when
        injector.decorateComponentInstance(container, null, instance);

        // then
        verifyNoMoreInteractions(instance);
    }

    @Test
    public void shouldInjectIComponentInstanceProviderAware() {
        // given
        final InstanceContainerAware instance = mock(InstanceContainerAware.class);
        final Injector<InstanceContainerAware> injector = injectorFor(instance);

        // when
        injector.decorateComponentInstance(container, null, instance);

        // then
        verify(instance).setInstanceProvider(container);
    }

    @Test
    public void shouldInjectIMutableComponentInstanceProviderAware() {
        // given
        final MutableInstanceContainerAware instance = mock(MutableInstanceContainerAware.class);
        final Injector<MutableInstanceContainerAware> injector = injectorFor(instance);

        // when
        injector.decorateComponentInstance(container, null, instance);

        // then
        verify(instance).setMutableInstanceContainer(container);
    }

    @Test
    public void shouldInjectISelfAwareComponentInstanceProviderAware() {
        // given
        final SelfAwareInstanceContainerAware instance = mock(SelfAwareInstanceContainerAware.class);
        final ComponentDefinition definition = definitionFor(instance);
        final Injector<SelfAwareInstanceContainerAware> injector = injectorFor(definition);

        // when
        injector.decorateComponentInstance(container, null, instance);

        // then
        final ArgumentCaptor<SelfAwareInstanceContainer> argument = ArgumentCaptor.forClass(SelfAwareInstanceContainer.class);
        verify(instance).setSelfAwareInstanceContainer(argument.capture());
        final SelfAwareInstanceContainer provider = argument.getValue();
        assertThat(provider.getName(), is(equalTo(definition.getName())));

        // when
        provider.getInstance();
        // then
        verify(container).getInstance(definition.getName());

    }
}
