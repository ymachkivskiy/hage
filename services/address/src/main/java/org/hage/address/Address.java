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
 * Created: 2011-07-19
 * $Id$
 */

package org.hage.address;


import org.hage.address.node.NodeAddress;
import org.hage.annotation.ReturnValuesAreNonnullByDefault;

import java.io.Serializable;


/**
 * A generic type of addresses used in AgE.
 * <p>
 * An address consists of three elements:
 * <ul>
 * <li>an identifier;
 * <li>a user friendly name;
 * <li>the address of the node this address was created on.
 * </ul>
 * <p>
 * <p>The identifier and node address together distinguish addresses. The friendly name is not mandatory and should
 * have no impact on addressing logic.
 * <p>
 * <p>Identity should be preserved across serialization (equals, hashcode).
 *
 * @author AGH AgE Team
 */
@ReturnValuesAreNonnullByDefault
public interface Address extends Serializable {

    /**
     * Returns the identifier of this address.
     *
     * @return the identifier.
     */
    String getIdentifier();

    /**
     * Returns the user friendly name of this address.
     *
     * @return the user friendly name
     */
    String getFriendlyName();

    /**
     * Returns the address of the node on which this address was created.
     *
     * @return The node address, not null.
     */
    NodeAddress getNodeAddress();

    /**
     * Provides a string representation of the address, based on the user friendly name.
     *
     * @return a user friendly string representation
     */
    @Override
    String toString();

    /**
     * Provides a string representation of the address, based on the address identifier.
     *
     * @return a identifying string representation.
     */
    String toQualifiedString();
}
