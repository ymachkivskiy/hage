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

package org.hage.platform.component.workplace;


import org.hage.platform.component.agent.IAgent;
import org.hage.platform.component.query.AgentEnvironmentQuery;
import org.hage.platform.component.query.IQuery;
import org.hage.platform.component.workplace.manager.WorkplaceManagerMessage;
import org.hage.platform.util.communication.address.agent.AgentAddress;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;


/**
 * Interface used by workplaces to communicate with the manager.
 *
 * @author AGH AgE Team
 */
public interface WorkplaceEnvironment {

    /**
     * Called when the workplace was stopped.
     *
     * @param workplace A workplace that stopped.
     */
    void onWorkplaceStop(@Nonnull Workplace workplace);

    /**
     * Queries workplaces located in this environment.
     *
     * @param query The query to perform.
     * @param <E>   A type of elements in the collection. E must be a realisation of {@link IAgent}.
     * @param <T>   A type of elements in the result.
     * @return the result of the query
     */
    @Nonnull
    <E extends IAgent, T> Collection<T> queryWorkplaces(AgentEnvironmentQuery<E, T> query);

    /**
     * Sends a message to other workplaces that are located in this environment.
     *
     * @param message The message to send.
     */
    void sendMessage(@Nonnull WorkplaceManagerMessage message);

    void requestRemoval(@Nonnull AgentAddress simpleWorkplace);

    /**
     * Returns addresses of all workplaces in the environment.
     *
     * @return the set of addresses of all workplaces.
     */
    @Nonnull
    Set<AgentAddress> getAddressesOfWorkplaces();

    void cacheQueryResults(@Nonnull AgentAddress address, @Nonnull IQuery<?, ?> query, Iterable<?> results);
}
