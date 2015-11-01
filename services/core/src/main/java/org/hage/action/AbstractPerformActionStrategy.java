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
 * Created: 2009-05-17
 * $Id$
 */

package org.hage.action;


import org.hage.address.agent.AgentAddress;
import org.hage.agent.AgentException;
import org.hage.agent.IAgent;
import org.hage.agent.ISimpleAggregate;
import org.hage.property.ClassPropertyContainer;

import java.util.Collection;


/**
 * Abstract action strategy. Contains empty implementations of action strategy methods. Should be extended by actions
 * which define only a subset of methods.
 *
 * @author AGH AgE Team
 */
public class AbstractPerformActionStrategy extends ClassPropertyContainer implements IPerformActionStrategy {

    @Override
    public Collection<AgentAddress> init(final ISimpleAggregate aggregate, final SingleAction action)
            throws AgentException {
        return aggregate.validateAction(action);
    }

    @Override
    public void perform(final IAgent target, final IActionContext context) throws AgentException {
        // do nothing by default
    }

    @Override
    public void finish(final IAgent target, final IActionContext context) {
        // do nothing by default
    }

}
