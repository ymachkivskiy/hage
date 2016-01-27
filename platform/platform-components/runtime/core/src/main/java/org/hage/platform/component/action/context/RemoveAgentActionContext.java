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
 * Created: 2008-10-07
 * $Id$
 */

package org.hage.platform.component.action.context;


import com.google.common.base.Objects;
import org.hage.platform.util.communication.address.agent.AgentAddress;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * The action of removing an agent from an aggregate.
 *
 * @author AGH AgE Team
 */
@AgentActionContext(RemoveAgentActionContext.ACTION_NAME)
public class RemoveAgentActionContext extends AbstractAgentActionContext {

    /**
     * The action name of this context.
     */
    public static final String ACTION_NAME = "removeAgent";

    /**
     * The address of agent to remove.
     */
    private final AgentAddress agentAddress;

    /**
     * Constructor.
     *
     * @param agentAddress the address of agent to remove
     */
    public RemoveAgentActionContext(final AgentAddress agentAddress) {
        this.agentAddress = checkNotNull(agentAddress);
    }

    /**
     * Returns the address of agent to remove.
     *
     * @return the address of agent to remove
     */
    public AgentAddress getAgentAddress() {
        return agentAddress;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("agentAddress", agentAddress).toString();
    }

}
