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

package org.hage.platform.util.communication.address.selector;


import com.google.common.collect.ImmutableSet;
import org.hage.platform.util.communication.address.agent.AgentAddress;
import org.hage.platform.util.communication.address.agent.DefaultAgentAddress;
import org.hage.platform.util.communication.address.node.NodeAddress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Nullable;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSetWithExpectedSize;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


/**
 * Tests for the {@link PredicateSelector} class.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class PredicateSelectorTest {

    private Set<AgentAddress> addresses;

    private PredicateSelector<AgentAddress> selector;

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

    @Test(expected = IllegalArgumentException.class)
    public void initialAddressesCannotBeNull() {
        // when
        selector = PredicateSelector.create(null);
    }

    @Test
    public void shouldCreateCorrectly() {
        // given
        selector = PredicateSelector.create(new AddressPredicate<AgentAddress>() {

            @Override
            public boolean apply(@Nullable final AgentAddress input) {
                return true;
            }
        });

        // then
        assertThat(selector, is(notNullValue()));
    }

    @Test
    public void shouldSelectProcessAddressesWithPredicate() {
        // given
        final Set<AgentAddress> processedAddresses = newHashSetWithExpectedSize(addresses.size());
        selector = PredicateSelector.create(new AddressPredicate<AgentAddress>() {

            @Override
            public boolean apply(@Nullable final AgentAddress address) {
                return processedAddresses.add(address);
            }
        });

        // when
        for(final AgentAddress address : addresses) {
            selector.selects(address);
        }

        // then
        assertThat(addresses, everyItem(isIn(processedAddresses)));
    }

}
