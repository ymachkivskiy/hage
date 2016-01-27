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

package org.hage.platform.component.agent;


import org.hage.platform.util.communication.address.agent.AgentAddress;

import java.util.Collection;
import java.util.Set;


/**
 * Interface to all agents which can contain other agents.
 * <p>
 * <p>
 * They execute their code and are responsible for their "life".
 *
 * @param <C> a lower bound of agents located in this aggregate.
 * @author AGH AgE Team
 */
public interface IAggregate<C extends IAgent> extends IAgent, Collection<C> {

    /**
     * Checks if this aggregate contains an agent with the given address.
     *
     * @param address the address of agent to check.
     * @return <code>true</code> if this aggregate contains the agent; <code>false
     * </code>- otherwise.
     */
    boolean containsAgent(AgentAddress address);

    /**
     * Returns the agent with the specified address.
     *
     * @param address the address of an agent.
     * @return the agent with the specified address.
     */
    C getAgent(AgentAddress address);

    /**
     * Removes the agent from this aggregate.
     *
     * @param address Address of the agent to remove.
     * @throws AgentException occurs when the aggregate cannot remove the agent.
     */
    void removeAgent(AgentAddress address);

    /**
     * Returns addresses of agents located in this aggregate.
     *
     * @return addresses of agents.
     */
    Set<AgentAddress> getAgentsAddresses();

}
