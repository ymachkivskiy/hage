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
 * Created: 2012-04-07
 * $Id$
 */

package org.hage.platform.component.action;


import org.hage.address.agent.AgentAddress;
import org.hage.address.selector.Selectors;
import org.hage.communication.message.Message;
import org.hage.platform.component.action.context.*;
import org.hage.platform.component.agent.ISimpleAgent;

import static org.hage.address.selector.Selectors.parentOf;
import static org.hage.address.selector.Selectors.singleAddress;


/**
 * Factory methods for agent actions.
 *
 * @author AGH AgE Team
 */
public class AgentActions {

    /**
     * Creates an action which adds the second agent to the parent of the first one.
     *
     * @param agent    the agent to which parent the new agent will be added
     * @param newAgent the agent to be added
     * @return an addToParent action
     */
    public static SingleAction addToParent(final ISimpleAgent agent, final ISimpleAgent newAgent) {
        AddAgentActionContext context = new AddAgentActionContext(newAgent);
        return new SingleAction(Selectors.parentOf(agent.getAddress()), context);
    }

    /**
     * Creates a death action.
     *
     * @param agent the agent to be killed
     * @return a death action
     */
    public static SingleAction death(final ISimpleAgent agent) {
        return new SingleAction(singleAddress(agent.getAddress()), new KillAgentActionContext());
    }

    /**
     * Creates a migration action.
     *
     * @param agent       the agent to be migrated
     * @param destination the migration destination
     * @return a migration action
     */
    public static Action migrate(final ISimpleAgent agent, final AgentAddress destination) {
        ComplexAction action = new ComplexAction();

        MoveAgentActionContext moveContext = new MoveAgentActionContext();
        SingleAction moveAction = new SingleAction(singleAddress(destination), moveContext);

        GetAgentActionContext getAgentContext = new GetAgentActionContext(moveContext);
        action.addChild(new SingleAction(singleAddress(agent.getAddress()), getAgentContext));

        PassToParentActionContext parentContext = new PassToParentActionContext(agent.getAddress(), moveAction);
        action.addChild(new SingleAction(parentOf(agent.getAddress()), parentContext));

        return action;
    }

    /**
     * Creates a send message action.
     *
     * @param message the message to be sent
     * @return a send message action
     */
    public static SingleAction sendMessage(final Message<AgentAddress, ?> message) {
        return new SingleAction(message.getHeader().getReceiverSelector(), new SendMessageActionContext(message));
    }
}
