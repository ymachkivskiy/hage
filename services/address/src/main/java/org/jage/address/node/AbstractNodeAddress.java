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
 * Created: 2012-10-28
 * $Id$
 */

package org.jage.address.node;


/**
 * Abstract implementation of a node address, which ensures comparison is based on identifiers.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractNodeAddress implements NodeAddress {

    private static final long serialVersionUID = -3060430633934000296L;

    @Override
    public final int compareTo(final NodeAddress that) {
        return getIdentifier().compareTo(that.getIdentifier());
    }

    @Override
    public abstract boolean equals(final Object obj);
}
