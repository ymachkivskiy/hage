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
 * Created: 2009-05-20
 * $Id$
 */

package org.hage.platform.component.agent;


import org.hage.address.agent.AgentAddress;
import org.hage.address.selector.BroadcastSelector;
import org.hage.platform.component.action.SingleAction;

import java.util.Collection;


/**
 * An interface for simple aggregates. It adds methods for performing actions.
 *
 * @author AGH AgE Team
 */
public interface ISimpleAggregate extends IAggregate<ISimpleAgent>, ISimpleAgent {

    /**
     * Validate given action. It checks if used addresses point to agents in current aggregate.
     *
     * @param action single action to validate
     * @return collection of agent addresses used in action, if no addresses is used (action has selectors such as
     * {@link BroadcastSelector}, etc.) the empty collection is returned; <code>null</code> is returned when
     * action didn't validate addresses - then a default validation is performed
     * @throws AgentException when validation fails.
     */
    Collection<AgentAddress> validateAction(SingleAction action);
}
