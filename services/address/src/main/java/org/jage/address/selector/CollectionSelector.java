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

package org.jage.address.selector;

import java.util.Collection;
import java.util.Set;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.jage.address.Address;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.collect.Sets.difference;

/**
 * Selector that selects explicitly provided addresses.
 *
 * @param <T>
 * 		type of an address which can be selects by this selector.
 *
 * @author AGH AgE Team
 */
@Immutable
public class CollectionSelector<T extends Address> implements ExplicitSelector<T> {

	private final ImmutableSet<T> addresses;

	/**
	 * Constructs a new selector for the collection of addresses.
	 *
	 * @param addresses
	 * 		a collection of addresses.
	 */
	public CollectionSelector(final Collection<T> addresses) {
		this.addresses = ImmutableSet.copyOf(addresses);
	}

	/**
	 * Constructs a new selector for the collection of addresses.
	 *
	 * @param addresses
	 * 		a collection of addresses.
	 */
	public static <T extends Address> CollectionSelector<T> create(final Collection<T> addresses) {
		return new CollectionSelector<>(addresses);
	}

	/**
	 * Constructs a new selector from the current by excluding all provided addresses.
	 *
	 * @param collection
	 * 		a collection of addresses.
	 */
	public CollectionSelector<T> except(final Collection<T> collection) {
		return new CollectionSelector<>(difference(addresses, ImmutableSet.copyOf(collection)));
	}

	@Override @Nonnull
	public Set<T> getAddresses() {
		return Sets.newCopyOnWriteArraySet(addresses);
	}

	@Override
	public boolean selects(final T address) {
		return addresses.contains(requireNonNull(address));
	}

	@Override
	public String toString() {
		return toStringHelper(this).addValue(addresses).toString();
	}
}
