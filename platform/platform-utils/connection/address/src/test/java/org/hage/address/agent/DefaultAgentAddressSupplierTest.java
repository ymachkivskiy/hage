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
 * Created: 29-10-2012
 * $Id$
 */

package org.hage.address.agent;


import org.hage.address.node.NodeAddress;
import org.hage.address.node.NodeAddressSupplier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


/**
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAgentAddressSupplierTest {

    @InjectMocks
    private final DefaultAgentAddressSupplier supplier = new DefaultAgentAddressSupplier();
    @InjectMocks
    private final DefaultAgentAddressSupplier templatedSupplier = new DefaultAgentAddressSupplier("template");
    @InjectMocks
    private final DefaultAgentAddressSupplier wildcardedSupplier = new DefaultAgentAddressSupplier("*template*");
    @Mock
    private NodeAddressSupplier nodeAddressSupplier;

    @Before
    public void setup() {
        given(nodeAddressSupplier.get()).willReturn(mock(NodeAddress.class));
    }

    @Test
    public void shouldCreateDefaultAgentAddressInstances() {
        // when
        final AgentAddress address = supplier.get();

        // then
        assertThat(address, instanceOf(DefaultAgentAddress.class));
    }

    @Test
    public void shouldUseNodeAddressInstancesFromNodeAddressSupplier() {
        // given
        final NodeAddress nodeAddress = mock(NodeAddress.class);
        given(nodeAddressSupplier.get()).willReturn(nodeAddress);

        // when
        final AgentAddress address = supplier.get();

        // then
        assertThat(address.getNodeAddress(), is(nodeAddress));
    }

    @Test
    public void shouldUseDefaultTemplate() {
        // when
        final AgentAddress address = supplier.get();

        // then
        assertThat(address.getFriendlyName(), is("agent0"));
    }

    @Test
    public void shouldIncrementWildcardInTemplate() {
        // when
        supplier.get();
        final AgentAddress address = supplier.get();

        // then
        assertThat(address.getFriendlyName(), is("agent1"));
    }

    @Test
    public void shouldUseProvidedTemplate() {
        // when
        final AgentAddress address = templatedSupplier.get();

        // then
        assertThat(address.getFriendlyName(), is("template"));
    }

    @Test
    public void shouldReplaceWildcardInTemplate() {
        // when
        final AgentAddress address = wildcardedSupplier.get();

        // then
        assertThat(address.getFriendlyName(), is("0template0"));
    }
}
