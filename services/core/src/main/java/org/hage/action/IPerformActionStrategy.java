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

package org.hage.action;


import org.hage.address.agent.AgentAddress;
import org.hage.address.selector.BroadcastSelector;
import org.hage.agent.AgentException;
import org.hage.agent.IAgent;
import org.hage.agent.ISimpleAggregate;

import java.util.Collection;


/**
 * An interface for all actions implemented as strategies.
 *
 * @author AGH AgE Team
 */
public interface IPerformActionStrategy {

    /**
     * Performs initialization steps and validates addresses used in action. This method can be used to perform some
     * additional steps before addresses validation, i.e. a new agent can be added to aggregate so that other actions be
     * executed on it.
     *
     * @param aggregate aggregate which executes action
     * @param action    single action to validate
     * @return collection of agent addresses used in action, if no addresses is used (action has selectors such as
     * {@link BroadcastSelector}, etc.) the empty collection is returned; <code>null</code> is returned when
     * action didn't validate addresses - then a default validation is performed
     * @throws AgentException when validation fails.
     * @see ActionPhase#INIT
     */
    Collection<AgentAddress> init(ISimpleAggregate aggregate, SingleAction action) throws AgentException;

    /**
     * Performs main phase of action ({@link ActionPhase#MAIN}) using data in object of IActionContext type which is in
     * event object parameter.
     *
     * @param target  reference to the target agent, which action will be performed on
     * @param context action context
     * @throws AgentException when action execution fails.
     */
    void perform(IAgent target, IActionContext context) throws AgentException;

    /**
     * Executes finalization phase (({@link ActionPhase#FINISH}) which is executed after performing main phase of
     * action.
     *
     * @param target  reference to the target agent, which action will be performed on
     * @param context action context
     */
    void finish(IAgent target, IActionContext context);

}
