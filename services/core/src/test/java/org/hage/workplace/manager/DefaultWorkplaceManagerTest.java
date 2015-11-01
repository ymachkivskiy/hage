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
 * Created: 2012-09-12
 * $Id$
 */

package org.hage.workplace.manager;


import org.hage.address.node.NodeAddress;
import org.hage.address.node.NodeAddressSupplier;
import org.hage.platform.component.definition.ComponentDefinition;
import org.hage.platform.component.exception.ComponentException;
import org.hage.platform.component.pico.PicoComponentInstanceProvider;
import org.hage.platform.component.pico.PicoComponentInstanceProviderFactory;
import org.hage.services.core.LifecycleManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.ComponentFactory;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVisitor;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


/**
 * Tests for the {@link DefaultWorkplaceManager} class.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkplaceManagerTest {

    private static final String TEST_COMPONENT_KEY = "test";
    private final PicoComponentInstanceProvider instanceProvider = (PicoComponentInstanceProvider) PicoComponentInstanceProviderFactory
            .createInstanceProvider();
    @Mock
    // (answer = Answers.CALLS_REAL_METHODS)
    private NodeAddressSupplier addressProvider;
    @Mock
    // (answer = Answers.CALLS_REAL_METHODS)
    private LifecycleManager lifecycleManager;

    @Before
    public void setUp() throws ComponentException {
        given(addressProvider.get()).willReturn(mock(NodeAddress.class));

        //instanceProvider.addComponent(LoopbackCommunicationService.class);
        instanceProvider.addComponentInstance(addressProvider);
        instanceProvider.addComponentInstance(lifecycleManager);
        instanceProvider.addComponent(DefaultWorkplaceManager.class);
        instanceProvider.verify();
    }

    @Test
    @Ignore
    public void shouldRecreateConfiguration() throws ComponentException {
        // given
        final DefaultWorkplaceManager instance = instanceProvider.getInstance(DefaultWorkplaceManager.class);
        final ComponentDefinition componentDefinition = new ComponentDefinition(TEST_COMPONENT_KEY, Object.class, false);

        // when
        //instance.computationConfigurationUpdated((Collection)newArrayList(componentDefinition));

        instanceProvider.verify();
        final Collector collectorBefore = new Collector();
        instanceProvider.accept(collectorBefore);

        instance.teardownConfiguration();

        instanceProvider.verify();
        final Collector collectorAfter = new Collector();
        instanceProvider.accept(collectorAfter);

        //instance.computationConfigurationUpdated((Collection)newArrayList(componentDefinition));
        instanceProvider.verify();

        final Collector collectorAfter2 = new Collector();
        instanceProvider.accept(collectorAfter2);

        // then
        assertThat(collectorBefore.containers, hasSize(2));
        assertThat(collectorAfter.containers, hasSize(1));

        boolean hasTest = false;
        for(final ComponentAdapter<?> adapter : collectorBefore.adapters) {
            if(!hasTest) {
                hasTest = adapter.getComponentKey().equals(TEST_COMPONENT_KEY);
            }
        }
        assertThat(hasTest, is(true));

        hasTest = false;
        for(final ComponentAdapter<?> adapter : collectorAfter.adapters) {
            if(!hasTest) {
                hasTest = adapter.getComponentKey().equals(TEST_COMPONENT_KEY);
            }
        }
        assertThat(hasTest, is(false));

        hasTest = false;
        for(final ComponentAdapter<?> adapter : collectorBefore.adapters) {
            if(!hasTest) {
                hasTest = adapter.getComponentKey().equals(TEST_COMPONENT_KEY);
            }
        }
        assertThat(hasTest, is(true));
    }

    private class Collector implements PicoVisitor {

        private final List<Parameter> parameters = newArrayList();

        private final List<PicoContainer> containers = newArrayList();

        private final List<ComponentFactory> factories = newArrayList();

        private final List<ComponentAdapter<?>> adapters = newArrayList();

        @Override
        public Object traverse(final Object node) {
            return node;
        }

        @Override
        public boolean visitContainer(final PicoContainer pico) {
            containers.add(pico);
            return CONTINUE_TRAVERSAL;
        }

        @Override
        public void visitComponentAdapter(final ComponentAdapter<?> componentAdapter) {
            adapters.add(componentAdapter);
        }

        @Override
        public void visitComponentFactory(final ComponentFactory componentFactory) {
            factories.add(componentFactory);
        }

        @Override
        public void visitParameter(final Parameter parameter) {
            parameters.add(parameter);
        }
    }
}
