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
 * Created: 2009-03-11
 * $Id$
 */

package org.hage.platform.communication.address.selector;


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.agent.DefaultAgentAddress;
import org.hage.platform.communication.address.node.NodeAddress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Tests for the {@link UnicastSelector} class.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class UnicastSelectorTest {

    private AgentAddress address;

    private UnicastSelector<AgentAddress> selector;

    @Mock
    private NodeAddress nodeAddress;

    @Before
    public void setUp() {
        address = new DefaultAgentAddress(nodeAddress);
    }

    @Test(expected = NullPointerException.class)
    public void initialAddressCannotBeNull() {
        // when
        selector = UnicastSelector.create((AgentAddress) null);
    }

    @Test
    public void shouldCreateCorrectly() {
        // given
        selector = UnicastSelector.create(address);

        // then
        assertThat(selector, is(notNullValue()));
        assertThat(getOnlyElement(selector.getAddresses()), is(address));
    }

    @Test
    public void shouldSelectOnlyOneAddress() {
        // given
        selector = UnicastSelector.create(address);
        final AgentAddress anotherAddress = mock(AgentAddress.class);

        // then
        assertThat(selector.selects(address), is(true));
        assertThat(selector.selects(anotherAddress), is(false));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenAddressNull() {
        // given
        selector = UnicastSelector.create(address);

        // when
        selector.selects(null);
    }
}
