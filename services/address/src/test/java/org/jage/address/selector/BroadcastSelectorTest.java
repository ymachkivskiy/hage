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
 * Created: 2009-03-12
 * $Id$
 */

package org.jage.address.selector;


import org.jage.address.Address;
import org.jage.address.agent.AgentAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Tests for the {@link BroadcastSelector} class.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class BroadcastSelectorTest {

    private BroadcastSelector<Address> selector;

    @Test
    public void shouldCreateCorrectly() {
        // given
        selector = BroadcastSelector.create();

        // then
        assertThat(selector, is(notNullValue()));
    }

    @Test
    public void shouldSelectEverything() {
        // given
        selector = BroadcastSelector.create();
        final AgentAddress address1 = mock(AgentAddress.class);
        final AgentAddress address2 = mock(AgentAddress.class);

        // then
        assertThat(selector.selects(address1), is(true));
        assertThat(selector.selects(address2), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenAddressNull() {
        // given
        selector = BroadcastSelector.create();

        // when
        selector.selects(null);
    }

}
