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


import com.google.common.base.Predicate;
import org.hage.platform.communication.address.Address;

import java.io.Serializable;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;


/**
 * A selector that selects addresses based on some predicate.
 *
 * @param <T> type of an address which can be selects by this selector.
 * @author AGH AgE Team
 */
public class PredicateSelector<T extends Address> implements AddressSelector<T> {

    private final Predicate<T> predicate;

    /**
     * Constructs a new selector for the predicate.
     *
     * @param predicate a predicate to use.
     */
    private PredicateSelector(final Predicate<T> predicate) {
        checkArgument(predicate instanceof Serializable, "Predicate must be serializable.");
        this.predicate = requireNonNull(predicate);
    }

    /**
     * Constructs a new selector for the predicate.
     *
     * @param predicate a predicate to use.
     */
    public static <T extends Address> PredicateSelector<T> create(final Predicate<T> predicate) {
        return new PredicateSelector<>(predicate);
    }

    @Override
    public boolean selects(final T address) {
        return predicate.apply(address);
    }

    @Override
    public String toString() {
        return toStringHelper(this).addValue(predicate).toString();
    }
}
