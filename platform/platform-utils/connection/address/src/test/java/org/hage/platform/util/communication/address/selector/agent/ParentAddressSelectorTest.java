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
 * Created: 2012-11-12
 * $Id$
 */

package org.hage.platform.util.communication.address.selector.agent;


import org.hage.platform.util.communication.address.agent.AgentAddress;
import org.hage.platform.util.communication.address.agent.DefaultAgentAddress;
import org.hage.platform.util.communication.address.node.NodeAddress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Tests for the {@link ParentAddressSelector} class.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ParentAddressSelectorTest {

    private AgentAddress address;

    private ParentAddressSelector selector;

    @Mock
    private NodeAddress nodeAddress;

    @Before
    public void setUp() {
        address = new DefaultAgentAddress(nodeAddress);
    }

    @Test
    public void shouldCreateCorrectly() {
        // given
        selector = ParentAddressSelector.create(address);

        // then
        assertThat(selector.getChildAddress(), equalTo(address));
    }

    @Test
    public void shouldNotSelectAnything() {
        // given
        selector = ParentAddressSelector.create(address);
        final AgentAddress anotherAddress = mock(AgentAddress.class);

        // then
        assertThat(selector.selects(address), is(false));
        assertThat(selector.selects(anotherAddress), is(false));
        assertThat(selector.selects(null), is(false));
    }
}
