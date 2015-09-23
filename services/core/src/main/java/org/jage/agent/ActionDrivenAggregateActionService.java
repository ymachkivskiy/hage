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
 * Created: 2012-04-09
 * $Id$
 */

package org.jage.agent;


import org.jage.action.IActionContext;
import org.jage.action.SingleAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Iterables.consumingIterable;
import static com.google.common.collect.Iterators.consumingIterator;
import static com.google.common.collect.Lists.newLinkedList;


/**
 * The action service for the {@link ActionDrivenAggregate}. It guarantees that add and kill actions will be executed
 * at the very end of the step.
 *
 * @author AGH AgE Team
 * @since 2.6
 */
public class ActionDrivenAggregateActionService extends AggregateActionService {

    private static final Logger log = LoggerFactory.getLogger(ActionDrivenAggregateActionService.class);

    private final List<SingleAction> addAgentActions = newLinkedList();

    private final List<ISimpleAgent> killAgentActionTargets = newLinkedList();

    private final List<IActionContext> killAgentActionContexts = newLinkedList();

    @Override
    protected void performAddAgentAction(final SingleAction action) {
        addAgentActions.add(action);
    }

    @Override
    protected void performKillAgentAction(final ISimpleAgent target, final IActionContext context) {
        killAgentActionTargets.add(target);
        killAgentActionContexts.add(context);
    }

    /**
     * Executes all postponed (add and kill) actions.
     */
    public void performPostponedActions() {
        log.debug("Performing postponed actions of {}", this);
        // Execute all postponed actions. Don't forget to remove them from the collections.

        for(final SingleAction action : consumingIterable(addAgentActions)) {
            super.performAddAgentAction(action);
        }

        final Iterator<ISimpleAgent> targets = consumingIterator(killAgentActionTargets.iterator());
        final Iterator<IActionContext> contexts = consumingIterator(killAgentActionContexts.iterator());
        while(targets.hasNext() && contexts.hasNext()) {
            super.performKillAgentAction(targets.next(), contexts.next());
        }
    }
}
