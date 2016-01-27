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


import org.hage.platform.component.query.AgentEnvironmentQuery;
import org.hage.platform.util.communication.address.agent.AgentAddress;

import java.util.Collection;


/**
 * Interface used by agents to contact with the external environment (their parents).
 *
 * @author AGH AgE Team
 */
public interface IAgentEnvironment {

    /**
     * Queries an environment of the parent.
     * <p>
     * <p>
     * <pre>
     *         T
     *        /|\
     *       / | \
     *      A  B  C
     *     /|\
     *    / | \
     *  *X  Y  Z
     * </pre>
     * <p>
     * The parent environment in the above situation (when seen from the agent X (with an asterisk)) consists of agents:
     * A, B and C (agents of the T aggregate).
     *
     * @param <E>   A type of the agents in the aggregate.
     * @param <T>   A type of the elements in the result.
     * @param query The query to perform.
     * @return the result of the query.
     */
    <E extends IAgent, T> Collection<T> queryParent(AgentEnvironmentQuery<E, T> query);

    /**
     * Queries the environment.
     *
     * @param <E>   A type of the agents in the aggregate.
     * @param <T>   A type of the elements in the result.
     * @param query The query to perform.
     * @return the result of the query.
     */
    <E extends IAgent, T> Collection<T> query(AgentEnvironmentQuery<E, T> query);

    /**
     * Returns the parent's address.
     *
     * @return the parent's address
     */
    AgentAddress getAddress();

}
