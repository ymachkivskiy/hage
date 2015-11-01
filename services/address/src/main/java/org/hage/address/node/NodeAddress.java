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

package org.hage.address.node;


import org.hage.annotation.ReturnValuesAreNonnullByDefault;

import java.io.Serializable;


/**
 * Represents the address of a single node.
 * <p>
 * <p>
 * Identity of node addresses should be based on their identifier.
 * <p>
 * <p>
 * Node addresses should be comparable on the base of their identifiers.
 *
 * @author AGH AgE Team
 */
@ReturnValuesAreNonnullByDefault
public interface NodeAddress extends Serializable, Comparable<NodeAddress> {

    /**
     * Returns a globally unique identifier, comparable and preserved across serialization.
     *
     * @return the identifier of this node address
     */
    String getIdentifier();
}
