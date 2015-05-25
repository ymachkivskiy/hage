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

package org.jage.address.selector;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.jage.address.Address;
import org.jage.address.agent.AgentAddress;
import org.jage.address.selector.agent.ParentAddressSelector;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;

import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.common.collect.Sets.difference;

/**
 * Tests for the {@link Selectors} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class SelectorsTest {
	@Test
	public void testSingleAddress() throws Exception {
		// given
		final Address address = mock(Address.class);

		// when
		final ExplicitSelector<Address> selector = Selectors.singleAddress(address);

		// then
		assertThat(selector, is(instanceOf(UnicastSelector.class)));
	}

	@Test
	public void testAllAddresses() throws Exception {
		// when
		final AddressSelector<Address> selector = Selectors.allAddresses();

		// then
		assertThat(selector, is(instanceOf(BroadcastSelector.class)));
	}

	/*@Test
	public void testAllSameRemoteAddresses() throws Exception {

	}*/

	@Test
	public void testAllAddressesFrom() throws Exception {
		// given
		final ImmutableSet.Builder<Address> builder = ImmutableSet.builder();
		for (int i = 0; i < 10; i++) {
			builder.add(mock(Address.class));
		}
		final Set<Address> addresses = builder.build();

		// when
		final ExplicitSelector<Address> selector = Selectors.allAddressesFrom(addresses);

		// then
		assertThat(selector, is(instanceOf(CollectionSelector.class)));
		assertThat(selector.getAddresses(), is(equalTo(addresses)));
	}

	@Test
	public void testAnyAddressFrom() throws Exception {
		// given
		final ImmutableSet.Builder<Address> builder = ImmutableSet.builder();
		for (int i = 0; i < 10; i++) {
			builder.add(mock(Address.class));
		}
		final Set<Address> addresses = builder.build();

		// when
		final ExplicitSelector<Address> selector = Selectors.anyAddressFrom(addresses);

		// then
		assertThat(selector, is(instanceOf(UnicastSelector.class)));
		assertThat(getOnlyElement(selector.getAddresses()), isIn(addresses));
	}

	@Test
	public void testAllAddressesMatching() throws Exception {
		// given
		final Predicate<Address> predicate = mock(AddressPredicate.class);

		// when
		final AddressSelector<Address> selector = Selectors.allAddressesMatching(predicate);

		// then
		assertThat(selector, is(instanceOf(PredicateSelector.class)));
	}

	@Test
	public void testParentOf() throws Exception {
		// given
		final AgentAddress address = mock(AgentAddress.class);

		// when
		final AddressSelector<AgentAddress> selector = Selectors.parentOf(address);

		// then
		assertThat(selector, is(instanceOf(ParentAddressSelector.class)));
	}

	@Test
	public void testFilter() throws Exception {
		// given
		final ImmutableSet.Builder<Address> allBuilder = ImmutableSet.builder();
		final ImmutableSet.Builder<Address> selectedBuilder = ImmutableSet.builder();
		for (int i = 0; i < 10; i++) {
			final Address mock = mock(Address.class);
			allBuilder.add(mock);
			if (i % 2 == 0)          {
				selectedBuilder.add(mock);
			}
		}
		final Set<Address> allAddresses = allBuilder.build();
		final Set<Address> selectedAddresses = selectedBuilder.build();

		final AddressSelector<Address> selector = Selectors.allAddressesFrom(selectedAddresses);

		// when
		final Set<Address> set = Selectors.filter(allAddresses, selector);

		// then
		assertThat(set, is(equalTo(selectedAddresses)));
	}

	@Test
	public void testFilterUnselected() throws Exception {
		// given
		final ImmutableSet.Builder<Address> allBuilder = ImmutableSet.builder();
		final ImmutableSet.Builder<Address> selectedBuilder = ImmutableSet.builder();
		for (int i = 0; i < 10; i++) {
			final Address mock = mock(Address.class);
			allBuilder.add(mock);
			if (i % 2 == 0)          {
				selectedBuilder.add(mock);
			}
		}
		final Set<Address> allAddresses = allBuilder.build();
		final Set<Address> selectedAddresses = selectedBuilder.build();

		final AddressSelector<Address> selector = Selectors.allAddressesFrom(selectedAddresses);

		// when
		final Set<Address> set = Selectors.filterUnselected(allAddresses, selector);

		// then
		assertThat(set, is(equalTo((Set<Address>)difference(allAddresses, selectedAddresses))));
	}

	@Test
	public void testGetUnknownAddresses() throws Exception {
		// given
		final ImmutableSet.Builder<Address> allBuilder = ImmutableSet.builder();
		final ImmutableSet.Builder<Address> selectedBuilder = ImmutableSet.builder();
		for (int i = 0; i < 10; i++) {
			final Address mock = mock(Address.class);
			allBuilder.add(mock);
			if (i % 2 == 0)          {
				selectedBuilder.add(mock);
			}
		}
		final Set<Address> allAddresses = allBuilder.build();
		final Set<Address> selectedAddresses = selectedBuilder.build();

		final ExplicitSelector<Address> selector = Selectors.allAddressesFrom(allAddresses);

		// when
		final Set<Address> set = Selectors.getUnknownAddresses(selectedAddresses, selector);

		// then
		assertThat(set, is(equalTo((Set<Address>)difference(allAddresses, selectedAddresses))));
	}
}
