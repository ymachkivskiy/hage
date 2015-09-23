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

package org.jage.address.selector;


import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.DefaultAgentAddress;
import org.jage.address.node.NodeAddress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Iterables.partition;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Tests for the {@link MulticastSelector} class.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class MulticastSelectorTest {

    private Set<AgentAddress> addresses;

    private MulticastSelector<AgentAddress> selector;

    @Mock
    private NodeAddress nodeAddress;

    @Before
    public void setUp() {
        final ImmutableSet.Builder<AgentAddress> builder = ImmutableSet.builder();
        for(int i = 0; i < 5; i++) {
            builder.add(new DefaultAgentAddress(nodeAddress));
        }
        addresses = builder.build();
    }

    @Test(expected = NullPointerException.class)
    public void initialAddressesCannotBeNull() {
        // when
        selector = MulticastSelector.create(null);
    }

    @Test
    public void shouldCreateCorrectly() {
        // given
        selector = MulticastSelector.create(addresses);

        // then
        assertThat(selector, is(notNullValue()));
        assertThat(selector.getAddresses(), everyItem(isIn(addresses)));
    }

    @Test
    public void shouldSelectOnlySpecifiedAddress() {
        // given
        selector = MulticastSelector.create(addresses);
        final AgentAddress anotherAddress = mock(AgentAddress.class);

        // then
        assertThat(selector.selects(anotherAddress), is(false));
    }

    @Test
    public void shouldSelectAllSpecifiedAddresses() {
        // given
        selector = MulticastSelector.create(addresses);

        // when
        final boolean allSelected = Iterables.all(addresses, new Predicate<AgentAddress>() {

            @Override
            public boolean apply(@Nullable final AgentAddress address) {
                return selector.selects(address);
            }
        });

        // then
        assertThat(allSelected, is(true));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenAddressNull() {
        // given
        selector = MulticastSelector.create(addresses);

        // when
        selector.selects(null);
    }

    @Test
    public void exceptShouldExcludeAddresses() {
        // given
        final Iterable<List<AgentAddress>> specific = partition(addresses, 2);
        final List<AgentAddress> removed = Iterables.get(specific, 0);
        final List<AgentAddress> left = Iterables.get(specific, 1);
        selector = MulticastSelector.create(addresses).except(removed);

        // when
        final boolean allSelected = Iterables.all(left, new Predicate<AgentAddress>() {

            @Override
            public boolean apply(@Nullable final AgentAddress address) {
                return selector.selects(address);
            }
        });

        final boolean allRemoved = Iterables.all(removed, new Predicate<AgentAddress>() {

            @Override
            public boolean apply(@Nullable final AgentAddress address) {
                return selector.selects(address);
            }
        });

        // then
        assertThat(allSelected, is(true));
        assertThat(allRemoved, is(false));
    }
}
