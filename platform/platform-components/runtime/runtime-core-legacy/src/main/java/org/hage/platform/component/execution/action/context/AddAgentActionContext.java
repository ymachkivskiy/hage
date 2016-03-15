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

package org.hage.platform.component.execution.action.context;


import com.google.common.base.Objects;
import org.hage.platform.component.execution.agent.ISimpleAgent;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * The action of adding an agent to an aggregate.
 *
 * @author AGH AgE Team
 */
@AgentActionContext(AddAgentActionContext.ACTION_NAME)
public class AddAgentActionContext extends AbstractAgentActionContext {

    /**
     * The action name of this context.
     */
    public static final String ACTION_NAME = "addAgent";

    /**
     * The agent to add to an aggregate.
     */
    private final ISimpleAgent agent;

    /**
     * Constructor.
     *
     * @param agent the agent to add
     */
    public AddAgentActionContext(final ISimpleAgent agent) {
        this.agent = checkNotNull(agent);
    }

    /**
     * Returns the agent to add.
     *
     * @return the agent to add
     */
    public ISimpleAgent getAgent() {
        return agent;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("agent", agent).toString();
    }
}
