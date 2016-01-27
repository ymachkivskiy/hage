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

package org.hage.platform.util.communication.address.agent;


import org.hage.platform.util.communication.address.AbstractAddress;
import org.hage.platform.util.communication.address.node.NodeAddress;
import org.hage.platform.util.communication.address.node.UnspecifiedNodeAddress;

import javax.annotation.concurrent.Immutable;
import java.util.UUID;


/**
 * This class provides a default implementation of the agent address.
 * <p>
 * <p>
 * Identifiers are based on generated UUIDs, so no two ever created instances of this class can be equal. This identity
 * is preserved across serialization.
 *
 * @author AGH AgE Team
 */
@Immutable
public class DefaultAgentAddress extends AbstractAddress implements AgentAddress {

    private static final long serialVersionUID = -6247664264733075722L;

    public DefaultAgentAddress() {
        this(new UnspecifiedNodeAddress());
    }

    public DefaultAgentAddress(final NodeAddress nodeAddress) {
        super(UUID.randomUUID().toString(), nodeAddress);
    }

    public DefaultAgentAddress(final String friendlyName) {
        this(new UnspecifiedNodeAddress(), friendlyName);
    }

    public DefaultAgentAddress(final NodeAddress nodeAddress, final String friendlyName) {
        super(UUID.randomUUID().toString(), nodeAddress, friendlyName);
    }
}
