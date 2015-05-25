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

package org.jage.address;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.jage.address.node.NodeAddress;

/**
 *
 * @author AGH AgE Team
 */
public class AbstractAddressTest {

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeAtNullIdentifier() {
		// when
		new TestAddress(null, mock(NodeAddress.class), "");
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeAtNullNodeAddress() {
		// when
		new TestAddress("", null, "");
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowNpeAtNullFriendlyName() {
		// when
		new TestAddress("", mock(NodeAddress.class), null);
	}

	@Test
	public void shouldReturnIdentifier() {
		// given
		final String identifier = "identifier";

		// when
		final Address address = new TestAddress(identifier, mock(NodeAddress.class));

		// then
		assertThat(address.getIdentifier(), is(identifier));
	}

	@Test
	public void shouldReturnNodeAddress() {
		// given
		final NodeAddress nodeAddress = mock(NodeAddress.class);

		// when
		final Address address = new TestAddress("", nodeAddress);

		// then
		assertThat(address.getNodeAddress(), is(nodeAddress));
	}

	@Test
	public void shouldReturnFriendlyName() {
		// given
		final String friendlyName = "friendlyName";

		// when
		final Address address = new TestAddress("", mock(NodeAddress.class), friendlyName);

		// then
		assertThat(address.getFriendlyName(), is(friendlyName));
	}

	@Test
	public void shouldUseIdentifierAsDefaultFriendlyName() {
		// given
		final String identifier = "identifier";

		// when
		final Address address = new TestAddress(identifier, mock(NodeAddress.class));

		// then
		assertThat(address.getFriendlyName(), is(identifier));
	}

	@Test
	public void shouldUseIdentifierInQualifiedToString() {
		// given
		final String identifier = "identifier";
		final String nodeAddressValue = "nodeAddress";
		final NodeAddress nodeAddress = mock(NodeAddress.class, nodeAddressValue);

		// when
		final Address address = new TestAddress(identifier, nodeAddress);

		// then
		assertThat(address.toQualifiedString(), is(identifier + "@" + nodeAddressValue));
	}

	@Test
	public void shouldUseFriendlyNameInToString() {
		// given
		final String friendlyName = "friendlyName";
		final String nodeAddressValue = "nodeAddress";
		final NodeAddress nodeAddress = mock(NodeAddress.class, nodeAddressValue);

		// when
		final Address address = new TestAddress("", nodeAddress, friendlyName);

		// then
		assertThat(address.toString(), is(friendlyName + "@" + nodeAddressValue));
	}

	@Test
	public void testHashcode() {
		// given
		final String identifier = "identifier";
		final String otherIdentifier = "otherIdentifier";
		final NodeAddress nodeAddress = mock(NodeAddress.class);
		final NodeAddress otherNodeAddress = mock(NodeAddress.class);
		final String friendlyName = "friendlyName";
		final String otherFriendlyName = "friendlyName";
		final TestAddress first = new TestAddress(identifier, nodeAddress, friendlyName);
		TestAddress second;

		// same identifier, nodeAddress, friendlyName
		second = new TestAddress(identifier, nodeAddress, friendlyName);
		assertThat(first.hashCode(), is(equalTo(second.hashCode())));

		// same identifier, nodeAddress, different friendlyName
		second = new TestAddress(identifier, nodeAddress, otherFriendlyName);
		assertThat(first.hashCode(), is(equalTo(second.hashCode())));

		// different identifier
		second = new TestAddress(otherIdentifier, nodeAddress);
		assertThat(first.hashCode(), is(not(equalTo(second.hashCode()))));

		// different nodeAddress
		second = new TestAddress(identifier, otherNodeAddress);
		assertThat(first.hashCode(), is(not(equalTo(second.hashCode()))));
	}

	@Test
	public void testEquals() {
		// given
		final String identifier = "identifier";
		final String otherIdentifier = "otherIdentifier";
		final NodeAddress nodeAddress = mock(NodeAddress.class);
		final NodeAddress otherNodeAddress = mock(NodeAddress.class);
		final String friendlyName = "friendlyName";
		final String otherFriendlyName = "friendlyName";
		final TestAddress first = new TestAddress(identifier, nodeAddress, friendlyName);
		TestAddress second;

		// same identifier, nodeAddress, friendlyName
		second = new TestAddress(identifier, nodeAddress, friendlyName);
		assertThat(first, is(equalTo(second)));

		// same identifier, nodeAddress, different friendlyName
		second = new TestAddress(identifier, nodeAddress, otherFriendlyName);
		assertThat(first, is(equalTo(second)));

		// different identifier
		second = new TestAddress(otherIdentifier, nodeAddress);
		assertThat(first, is(not(equalTo(second))));

		// different nodeAddress
		second = new TestAddress(identifier, otherNodeAddress);
		assertThat(first, is(not(equalTo(second))));
	}

	@SuppressWarnings("serial")
	private static class TestAddress extends AbstractAddress {

		public TestAddress(final String identifier, final NodeAddress nodeAddress, final String friendlyName) {
			super(identifier, nodeAddress, friendlyName);
		}

		public TestAddress(final String identifier, final NodeAddress nodeAddress) {
			super(identifier, nodeAddress);
		}
	}
}
