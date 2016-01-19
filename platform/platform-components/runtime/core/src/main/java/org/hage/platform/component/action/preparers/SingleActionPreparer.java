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

package org.hage.platform.component.action.preparers;


import org.hage.address.agent.AgentAddress;
import org.hage.address.selector.ExplicitSelector;
import org.hage.address.selector.Selectors;
import org.hage.platform.component.action.Action;
import org.hage.platform.component.action.IActionContext;
import org.hage.platform.component.action.SingleAction;
import org.hage.platform.component.agent.IAgent;
import org.hage.platform.component.strategy.AbstractStrategy;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.singletonList;


/**
 * A simple {@link IActionPreparer} that expects a single {@link IActionContext} and build an action upon it.
 *
 * @param <T> a type of the agent that the preparator operates on.
 * @author AGH AgE Team
 */
public class SingleActionPreparer<T extends IAgent> extends AbstractStrategy implements IActionPreparer<T> {

    @Inject
    private IActionContext actionContext;

    @Override
    public List<Action> prepareActions(final T agent) {
        final AgentAddress agentAddress = agent.getAddress();
        final ExplicitSelector<AgentAddress> addressSelector = Selectors.singleAddress(agentAddress);
        return singletonList(new SingleAction(addressSelector, actionContext));
    }

    /**
     * Sets the action context that will be used in the prepared action.
     *
     * @param actionContext the action context to set
     */
    public void setActionContext(final IActionContext actionContext) {
        this.actionContext = actionContext;
    }
}
