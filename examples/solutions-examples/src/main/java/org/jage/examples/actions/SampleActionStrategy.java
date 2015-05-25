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
 * Created: 2011-03-15
 * $Id$
 */

package org.jage.examples.actions;

import java.util.Collection;

import org.jage.action.IActionContext;
import org.jage.action.IPerformActionStrategy;
import org.jage.action.SingleAction;
import org.jage.address.agent.AgentAddress;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.agent.ISimpleAggregate;
import org.jage.property.ClassPropertyContainer;

/**
 * The sample action implemented as a strategy.
 * 
 * @author AGH AgE Team
 */
public class SampleActionStrategy extends ClassPropertyContainer implements IPerformActionStrategy {

	@Override
	public void finish(IAgent target, IActionContext context) {
		// Empty
	}

	@Override
	public Collection<AgentAddress> init(ISimpleAggregate aggregate, SingleAction action) throws AgentException {
		return aggregate.validateAction(action);

	}

	@Override
	public void perfom(IAgent target, IActionContext context) throws AgentException {
		log.info("Performing action on {}", target.getAddress());

	}

}
