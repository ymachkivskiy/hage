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
 * Created: 2012-03-16
 * $Id$
 */

package org.jage.emas.util;

import org.jage.action.AbstractPerformActionStrategy;
import org.jage.action.IActionContext;
import org.jage.action.SingleAction;
import org.jage.address.agent.AgentAddress;
import org.jage.address.selector.AddressSelector;
import org.jage.address.selector.Selectors;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.emas.agent.EmasAgent;

import com.google.common.base.Optional;

/**
 * Utility class to chain actions. Subclasses need only implement the abstract template method. The next action to
 * perform will be inferred from the chaining context, and if present, submitted for execution.
 * <p>
 * This class can be removed when proper interleaving of actions is implemented.
 *
 * @param <A>
 *            the type of agent to perform the action on
 *
 * @author AGH AgE Team
 */
public abstract class ChainingAction<A extends EmasAgent> extends AbstractPerformActionStrategy {

	@Override
	public final void perform(final IAgent target, final IActionContext context) throws AgentException {
		@SuppressWarnings("unchecked")
		// classCastExc is appropriate here
		final A agent = (A)target;
		doPerform(agent);

		final ChainingContext chainingContext = (ChainingContext)context;
		final Optional<ChainingContext> nextContext = chainingContext.getNextContext();
		if (nextContext.isPresent()) {
			final AddressSelector<AgentAddress> selector = Selectors.singleAddress(target.getAddress());
			agent.getEnvironment().submitAction(new SingleAction(selector, nextContext.get()));
		}
	}

	/**
	 * Sublasses should implement this template method to actually perform some action.
	 *
	 * @param target
	 *            the target agent
	 * @throws AgentException
	 *             if something goes wrong.
	 */
	protected abstract void doPerform(A target) throws AgentException;
}
