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


import com.google.common.base.Objects;
import org.jage.agent.ISimpleAgent;
import org.jage.agent.ISimpleAggregate;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * The action of moving an agent to another aggregate.
 *
 * @author AGH AgE Team
 */
@AgentActionContext(MoveAgentActionContext.ACTION_NAME)
public class MoveAgentActionContext extends AbstractAgentActionContext implements IActionWithAgentReferenceContext {

    /**
     * The action name of this context.
     */
    public static final String ACTION_NAME = "moveAgent";

    /**
     * The agent to move or null.
     */
    private ISimpleAgent agent;

    /**
     * The parent of the moving agent or null.
     */
    private ISimpleAggregate parent;

    /**
     * Target aggregate where an agent is going to migrate.
     */
    private ISimpleAggregate target;

    @Override
    public void setAgent(final ISimpleAgent agent) {
        this.agent = checkNotNull(agent);
    }

    /**
     * Returns the agent which is to move or null if agent is not set.
     *
     * @return the agent which is to move
     */
    @Override
    public ISimpleAgent getAgent() {
        return agent;
    }

    @Override
    public void setParent(final ISimpleAggregate parent) {
        this.parent = checkNotNull(parent);
    }

    /**
     * Returns the parent of the moving agent.
     *
     * @return the parent of the moving agent or <TT>null</TT> if the aggregate which receives this action is the parent
     */
    @Override
    public ISimpleAggregate getParent() {
        return parent;
    }

    /**
     * Returns the target aggregate (the new parent of the agent).
     *
     * @return the target aggregate.
     */
    public ISimpleAggregate getTarget() {
        return target;
    }

    /**
     * Sets the target aggregate (the new parent of the agent).
     *
     * @param target the target aggregate.
     */
    public void setTarget(final ISimpleAggregate target) {
        this.target = checkNotNull(target);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("agent", agent).add("parent", parent).add("target", target).toString();
    }

}
