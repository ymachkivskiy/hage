package org.hage.agent;

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

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Helper methods for dealing with agents.
 *
 * @author AGH AgE Team
 */
public final class AgentTreeIterators {

    private AgentTreeIterators() {
    }

    /**
     * If the input agent is an aggregate, returns a pre-order iterable over the agent tree represented by the input.
     * Otherwise, returns a singleton iterable over the agent itself.
     *
     * @param agent the root agent
     * @return a pre-order iterable over the root agent tree
     */
    public static Iterable<IAgent> preOrderTree(final IAgent agent) {
        checkNotNull(agent);
        Iterable<IAgent> result = Collections.singleton(agent);
        if(agent instanceof IAggregate) {
            Iterable<IAgent> children = FluentIterable.from((IAggregate<?>) agent).transformAndConcat(
                    AgentTreeIterators::preOrderTree);
            result = Iterables.concat(result, children);
        }
        return result;
    }

    /**
     * If the input agent is an aggregate, returns a post-order iterable over the agent tree represented by the input.
     * Otherwise, returns a singleton iterable over the agent itself.
     *
     * @param agent the root agent
     * @return a post-order iterable over the root agent tree
     */
    public static Iterable<IAgent> postOrderTree(final IAgent agent) {
        checkNotNull(agent);
        Iterable<IAgent> result = Collections.singleton(agent);
        if(agent instanceof IAggregate) {
            Iterable<IAgent> children = FluentIterable.from((IAggregate<?>) agent).transformAndConcat(
                    AgentTreeIterators::postOrderTree);
            result = Iterables.concat(children, result);
        }
        return result;
    }

}
