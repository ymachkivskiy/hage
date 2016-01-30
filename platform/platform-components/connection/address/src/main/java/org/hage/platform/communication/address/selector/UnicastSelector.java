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
 * Created: 2012-11-11
 * $Id$
 */

package org.hage.platform.communication.address.selector;


import com.google.common.collect.ImmutableSet;
import org.hage.platform.communication.address.Address;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Set;

import static com.google.common.base.Objects.toStringHelper;
import static java.util.Objects.requireNonNull;


/**
 * The most simple address selector. Always returns the address given in the constructor.
 *
 * @param <T> type of an address which can be selected by this selector.
 * @author AGH AgE Team
 */
@Immutable
public class UnicastSelector<T extends Address> implements ExplicitSelector<T> {

    private static final long serialVersionUID = 5814016849899074368L;

    private final T address;

    /**
     * The only constructor.
     *
     * @param address address to select
     */
    private UnicastSelector(final T address) {
        this.address = requireNonNull(address);
    }

    /**
     * Constructs a new selector for the provided address.
     *
     * @param address an address.
     */
    public static <T extends Address> UnicastSelector<T> create(final T address) {
        return new UnicastSelector<>(address);
    }

    @Override
    @Nonnull
    public Set<T> getAddresses() {
        return ImmutableSet.of(address);
    }

    @Override
    public boolean selects(@Nonnull final T address) {
        return this.address.equals(requireNonNull(address));
    }

    @Override
    public String toString() {
        return toStringHelper(this).addValue(address).toString();
    }
}
