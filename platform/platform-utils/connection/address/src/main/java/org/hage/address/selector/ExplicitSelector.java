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

package org.hage.address.selector;


import org.hage.address.Address;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Set;


/**
 * A selector that can explicitly provide a set of addresses selected by it.
 * <p>
 * The requirement for this kind of selector is that it may select only addresses that it returns via {#getAddresses}.
 *
 * @param <T> an address type.
 * @author AGH AgE Team
 */
@Immutable
public interface ExplicitSelector<T extends Address> extends AddressSelector<T> {

    /**
     * Returns all addresses selected by this selector.
     *
     * @return a set of addresses.
     */
    @Nonnull
    Set<T> getAddresses();

}
