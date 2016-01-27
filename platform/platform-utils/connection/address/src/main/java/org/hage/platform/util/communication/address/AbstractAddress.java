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

package org.hage.platform.util.communication.address;


import com.google.common.base.Objects;
import org.hage.platform.util.communication.address.node.NodeAddress;

import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * This class provides a standard implementation of basic methods of addresses.
 * <p>
 * <p>
 * Equality is based on identifier and runtime class - that is, different address subclasses form distinct address
 * namespaces, in which identity is based on the identifier.
 *
 * @author AGH AgE Team
 */
@Immutable
public abstract class AbstractAddress implements Address {

    private static final long serialVersionUID = 6679668915580834792L;

    private static final String SEPARATOR = "@";

    private final String identifier;

    private final String friendlyName;

    private final NodeAddress nodeAddress;

    public AbstractAddress(final String identifier, final NodeAddress nodeAddress) {
        this(identifier, nodeAddress, identifier);
    }

    public AbstractAddress(final String identifier, final NodeAddress nodeAddress, final String friendlyName) {
        this.identifier = checkNotNull(identifier);
        this.friendlyName = checkNotNull(friendlyName);
        this.nodeAddress = checkNotNull(nodeAddress);
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(getIdentifier(), getNodeAddress());
    }

    @Override
    public final String getIdentifier() {
        return identifier;
    }

    @Override
    public String getFriendlyName() {
        return friendlyName;
    }

    @Override
    public final NodeAddress getNodeAddress() {
        return nodeAddress;
    }

    @Override
    public String toQualifiedString() {
        return getIdentifier() + SEPARATOR + getNodeAddress();
    }

    @Override
    public final boolean equals(final Object obj) {
        if(obj == this) {
            return true;
        }
        /*
         * getClass() instead of instanceOf is used on purpose, so that no instances of distinct subclasses could ever
		 * be equal.
		 */
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Address that = (Address) obj;
        return equal(getIdentifier(), that.getIdentifier()) && equal(getNodeAddress(), that.getNodeAddress());
    }

    @Override
    public String toString() {
        return getFriendlyName() + SEPARATOR + getNodeAddress();
    }

}
