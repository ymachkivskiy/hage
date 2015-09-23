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

package org.jage.action.testHelpers;


import org.jage.action.IActionContext;
import org.jage.action.IPerformActionStrategy;
import org.jage.action.SingleAction;
import org.jage.address.agent.AgentAddress;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.agent.ISimpleAggregate;
import org.jage.property.ClassPropertyContainer;

import java.util.Collection;


public class HelperTestActionStrategy extends ClassPropertyContainer implements IPerformActionStrategy {

    /*
     * (non-Javadoc)
     *
     * @see org.jage.action.IActionStrategy#init(org.jage.agent.IAggregate, org.jage.address.selector.IAddressSelector,
     * org.jage.action.IActionContext)
     */
    @Override
    public Collection<AgentAddress> init(ISimpleAggregate aggregate, SingleAction action) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jage.action.IActionStrategy#perfomAction(org.jage.agent.IAgent, org.jage.action.IActionContext)
     */
    @Override
    public void perform(IAgent target, IActionContext context) throws AgentException {

        StrategyTestActionContext staContext = (StrategyTestActionContext) context;
        staContext.actionRun = true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jage.action.IActionStrategy#terminate(org.jage.agent.IAgent, org.jage.action.IActionContext)
     */
    @Override
    public void finish(IAgent target, IActionContext context) {
        // TODO Auto-generated method stub

    }

}
