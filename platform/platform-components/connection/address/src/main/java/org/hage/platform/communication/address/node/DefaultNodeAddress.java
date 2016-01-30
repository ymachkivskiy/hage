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

package org.hage.platform.communication.address.node;


import com.google.common.base.Objects;

import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * An implementation of a node address that does not depend on Hazelcast.
 * <p>
 * <p>Consist of a local part and a hostname.
 *
 * @author AGH AgE Team
 */
@Immutable
public class DefaultNodeAddress extends AbstractNodeAddress {

    private static final String DEFAULT_HOSTNAME = "localhost";

    private static final long serialVersionUID = 1840324110860769872L;

    private final String localPart;

    private final String hostname;

    public DefaultNodeAddress(final String localPart) {
        this(localPart, DEFAULT_HOSTNAME);
    }

    public DefaultNodeAddress(final String localPart, final String hostname) {
        this.localPart = checkNotNull(localPart);
        this.hostname = checkNotNull(hostname);
    }

    public String getLocalPart() {
        return localPart;
    }

    public String getHostname() {
        return hostname;
    }

    @Override
    public String getIdentifier() {
        return toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(localPart, hostname);
    }

    @Override
    public String toString() {
        return localPart + "@" + hostname;
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof DefaultNodeAddress)) {
            return false;
        }
        final DefaultNodeAddress that = (DefaultNodeAddress) obj;
        return equal(localPart, that.localPart) && equal(hostname, that.hostname);
    }
}
