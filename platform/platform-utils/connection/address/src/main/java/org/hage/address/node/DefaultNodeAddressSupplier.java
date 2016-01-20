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


import javax.annotation.concurrent.Immutable;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;


/**
 * A default implementation of the node address supplier.
 * <p>
 * This implementation generates addresses consisting of two parts: PID of JVM that runs the node and the local
 * hostname. If PID is unavailable it falls back a UUID-based string.
 *
 * @author AGH AgE Team
 * @see DefaultNodeAddress
 */
@Immutable
public class DefaultNodeAddressSupplier implements NodeAddressSupplier {

    private DefaultNodeAddress nodeAddress;

    public DefaultNodeAddressSupplier() {
        final String localPart = getLocalPart();
        try {
            nodeAddress = new DefaultNodeAddress(localPart, getHostname());
        } catch(final UnknownHostException e) {
            nodeAddress = new DefaultNodeAddress(localPart);
        }
    }

    // XXX: This method was only tested with Sun/Oracle JVM.
    private static String getLocalPart() {
        final String name = ManagementFactory.getRuntimeMXBean().getName(); //TODO find out another way to obtain PID of JVM process
        final int pos = name.indexOf("@");
        if(pos <= 1) { // If 0 - there is no real PID
            return UUID.randomUUID().toString();
        }
        return name.substring(0, pos);
    }

    private static String getHostname() throws UnknownHostException {
        final InetAddress addr = InetAddress.getLocalHost();
        return addr.getHostName();
    }

    @Override
    public DefaultNodeAddress get() {
        return nodeAddress;
    }
}
