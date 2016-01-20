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


package org.hage.address.selector;


import org.hage.address.Address;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;


/**
 * A selector which selects one or more addresses according to some (implementation-dependent) rules.
 *
 * @param <T> an address type.
 * @author AGH AgE Team
 */
@Immutable
public interface AddressSelector<T extends Address> extends Serializable {

    /**
     * Checks whether the given address is selected by this selector.
     *
     * @param address an address to check.
     * @return true if the address is selected, false otherwise.
     */
    boolean selects(T address);
}
