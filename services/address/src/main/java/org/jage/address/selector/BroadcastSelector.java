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
 * Created: 2009-03-10
 * $Id$
 */

package org.jage.address.selector;


import org.jage.address.Address;

import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Objects.toStringHelper;
import static java.util.Objects.requireNonNull;


/**
 * The selector, which selects all available addresses.
 *
 * @param <T> type of address which can be selected by this selector
 * @author AGH AgE Team
 */
@Immutable
public class BroadcastSelector<T extends Address> implements AddressSelector<T> {

    private static final long serialVersionUID = 8005515047272772268L;

    /**
     * Constructs a new selector that selects all addresses.
     *
     * @param <T> type of address which can be selected by the selector.
     * @return a new broadcast selector.
     */
    public static <T extends Address> BroadcastSelector<T> create() {
        return new BroadcastSelector<>();
    }

    @Override
    public boolean selects(final T address) {
        requireNonNull(address);
        return true;
    }

    @Override
    public String toString() {
        return toStringHelper(this).toString();
    }
}
