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
 * Created: 2012-04-07
 * $Id$
 */

package org.jage.address.selector;


import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import org.jage.address.Address;
import org.jage.address.agent.AgentAddress;
import org.jage.address.selector.agent.ParentAddressSelector;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.difference;
import static java.util.Collections.shuffle;


/**
 * Utility class for selectors.
 *
 * @author AGH AgE Team
 */
@Immutable
public final class Selectors {

    private Selectors() {
        // Empty
    }

    /**
     * Creates a selector that selects a single address.
     *
     * @param address address that should be selected.
     * @param <T>     type of an address which will be selected by the selector.
     * @return a selector.
     */
    public static <T extends Address> ExplicitSelector<T> singleAddress(final T address) {
        return UnicastSelector.create(address);
    }

    /**
     * Creates a selector that selects all possible addresses.
     *
     * @param <T> type of an address which will be selected by the selector.
     * @return a selector.
     */
    public static <T extends Address> AddressSelector<T> allAddresses() {
        return BroadcastSelector.create();
    }

    /**
     * Creates a selector that selects all addresses from the collection.
     *
     * @param collection addresses that should be selected.
     * @param <T>        type of an address which will be selected by the selector.
     * @return a selector.
     */
    public static <T extends Address> ExplicitSelector<T> allAddressesFrom(final Collection<T> collection) {
        return MulticastSelector.create(collection);
    }

    /**
     * Creates a selector that selects one address from the collection.
     *
     * @param collection addresses to use.
     * @param <T>        type of an address which will be selected by the selector.
     * @return a selector.
     */
    public static <T extends Address> ExplicitSelector<T> anyAddressFrom(final Collection<T> collection) {
        checkArgument(!collection.isEmpty(), "At least one address must be provided.");
        final ArrayList<T> list = newArrayList(collection);
        shuffle(list);
        return UnicastSelector.create(getLast(list));
    }

    /**
     * Creates a selector that selects all addresses matching the predicate.
     *
     * @param predicate predicate that will be used to select addresses.
     * @param <T>       type of an address which will be selected by the selector.
     * @return a selector.
     */
    public static <T extends Address> AddressSelector<T> allAddressesMatching(final Predicate<T> predicate) {
        return PredicateSelector.create(predicate);
    }

    /**
     * Creates a parent-agent selector for the given agent address.
     *
     * @param childAddress an address of the child.
     * @return a selector.
     */
    public static ParentAddressSelector parentOf(final AgentAddress childAddress) {
        return ParentAddressSelector.create(childAddress);
    }

    /**
     * Filters all addresses from the collections using the specified selector.
     * <p>
     * Formally it returns all addresses from {@code addresses} for which {@code selector.selects(address) == false}.
     *
     * @param addresses addresses that should be filtered.
     * @param selector  a selector to use.
     * @param <T>       an address type.
     * @return a set of addresses.
     */
    public static <T extends Address> Set<T> filterUnselected(final Collection<T> addresses,
            final AddressSelector<T> selector) {
        return difference(ImmutableSet.copyOf(addresses), filter(addresses, selector));
    }

    /**
     * Filters all addresses from the collections using the specified selector.
     * <p>
     * Formally it returns all addresses from {@code addresses} for which {@code selector.selects(address) == true}.
     *
     * @param addresses addresses that should be filtered.
     * @param selector  a selector to use.
     * @param <T>       an address type.
     * @return a set of addresses.
     */
    public static <T extends Address> Set<T> filter(final Collection<T> addresses, final AddressSelector<T> selector) {
        final ImmutableSet.Builder<T> builder = ImmutableSet.builder();
        addresses.stream()
                .filter(selector::selects)
                .forEach(builder::add);
        return builder.build();
    }

    /**
     * Filters all addresses from the collections using the specified selector.
     * <p>
     * Formally it returns all addresses from {@code selector.getAddresses()} which are not in the provided collection.
     *
     * @param addresses addresses that should be filtered.
     * @param selector  a selector to use.
     * @param <T>       an address type.
     * @return a set of addresses.
     */
    public static <T extends Address> Set<T> getUnknownAddresses(final Collection<T> addresses,
            final ExplicitSelector<T> selector) {
        return difference(selector.getAddresses(), ImmutableSet.copyOf(addresses));
    }
}
