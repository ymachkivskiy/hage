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
 * Created: 2011-04-30
 * $Id$
 */

package org.hage.platform.component.execution.action.preparers;


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.selector.ExplicitSelector;
import org.hage.platform.communication.address.selector.Selectors;
import org.hage.platform.component.execution.action.Action;
import org.hage.platform.component.execution.action.IActionContext;
import org.hage.platform.component.execution.action.SingleAction;
import org.hage.platform.component.execution.agent.IAgent;
import org.hage.platform.component.execution.strategy.AbstractStrategy;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * A {@link IActionPreparer} that expects a list of {@link IActionContext}s and build an aggregated complex action
 * upon them.
 *
 * @param <T> a type of the agent that the preparator operates on.
 * @author AGH AgE Team
 */
public class MultipleActionPreparer<T extends IAgent> extends AbstractStrategy implements IActionPreparer<T> {

    @Inject
    private List<IActionContext> actionContexts;

    @Override
    public List<Action> prepareActions(final IAgent agent) {
        final AgentAddress agentAddress = agent.getAddress();
        final ExplicitSelector<AgentAddress> addressSelector = Selectors.singleAddress(agentAddress);

        return actionContexts.stream()
                .map(aContext -> new SingleAction(addressSelector, aContext))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Sets action contexts that will be used in prepared actions (one action per single context).
     *
     * @param actionContexts action contexts to set.
     */
    public void setActionContexts(final List<IActionContext> actionContexts) {
        this.actionContexts = actionContexts;
    }
}
