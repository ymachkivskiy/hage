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
 * Created: 2012-03-29
 * $Id$
 */

package org.jage.action.ordering;


import org.jage.action.context.AddAgentActionContext;
import org.jage.action.context.GetAgentActionContext;
import org.jage.action.context.KillAgentActionContext;
import org.jage.action.context.MoveAgentActionContext;
import org.jage.action.context.PassToParentActionContext;
import org.jage.action.context.RemoveAgentActionContext;
import org.jage.action.context.SendMessageActionContext;


/**
 * This comparator defines a default ordering for all aggregate actions. The ordering is as follows:
 * <ol>
 * <li>In order:
 * <ol>
 * <li>{@link GetAgentActionContext}
 * <li>{@link PassToParentActionContext}
 * <li>{@link SendMessageActionContext}
 * <li>
 * </ol>
 * <p>
 * <li>Always executed last: {@link MoveAgentActionContext}, {@link KillAgentActionContext},
 * {@link RemoveAgentActionContext}, {@link AddAgentActionContext}
 * </ol>
 * <p>
 * This is the default comparator used by the aggregate.
 *
 * @author AGH AgE Team
 */
public class DefaultActionComparator extends ActionComparator {

    /**
     * Constructs a new comparator instance.
     *
     * @see DefaultActionComparator
     */
    @SuppressWarnings("unchecked")
    public DefaultActionComparator() {
        addContextsAsOrdered(GetAgentActionContext.class, PassToParentActionContext.class,
                             SendMessageActionContext.class);

        addAlwaysLastContext(MoveAgentActionContext.class);
        addAlwaysLastContext(AddAgentActionContext.class);
        addAlwaysLastContext(KillAgentActionContext.class);
        addAlwaysLastContext(RemoveAgentActionContext.class);
    }
}
