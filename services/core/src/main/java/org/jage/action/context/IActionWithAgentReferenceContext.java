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

package org.jage.action.context;


import org.jage.action.IActionContext;
import org.jage.agent.ISimpleAgent;
import org.jage.agent.ISimpleAggregate;


/**
 * Action context which contains a reference to an agent.
 *
 * @author AGH AgE Team
 */
public interface IActionWithAgentReferenceContext extends IActionContext {

    /**
     * Sets the agent reference for the context.
     *
     * @param agent the agent reference for the context.
     */
    void setAgent(ISimpleAgent agent);

    /**
     * Returns the agent reference for the context.
     *
     * @return the agent reference for the context.
     */
    ISimpleAgent getAgent();

    /**
     * Sets the parent of the agent that is contained in this context.
     *
     * @param parent the parent aggregate.
     */
    void setParent(ISimpleAggregate parent);

    /**
     * Returns the parent of the agent that is contained in this context.
     *
     * @return the parent aggregate.
     */
    ISimpleAggregate getParent();
}
