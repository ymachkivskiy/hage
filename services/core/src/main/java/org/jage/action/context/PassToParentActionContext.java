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
 * Created: 2009-05-19
 * $Id$
 */

package org.jage.action.context;

import org.jage.action.Action;
import org.jage.action.IActionContext;
import org.jage.address.agent.AgentAddress;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Context for the action "pass to parent". It holds an action which is ordered on aggregate's parent agent and address
 * of action invoker.
 * 
 * @see org.jage.agent.SimpleAggregate
 * 
 * @author AGH AgE Team
 */
@AgentActionContext(PassToParentActionContext.ACTION_NAME)
public class PassToParentActionContext implements IActionContext {

	/**
	 * The action name of this context.
	 */
	public static final String ACTION_NAME = "passToParent";

	private final Action action;

	private final AgentAddress invoker;

	/**
	 * Creates a new "pass-to-parent" context.
	 * 
	 * @param invoker
	 *            the conxtext creator.
	 * @param action
	 *            the action to pass to the parent.
	 */
	public PassToParentActionContext(final AgentAddress invoker, final Action action) {
		this.invoker = checkNotNull(invoker);
		this.action = checkNotNull(action);
	}

	/**
	 * Returns the creator of this context.
	 * 
	 * @return the creator of this context.
	 */
	public AgentAddress getInvoker() {
		return invoker;
	}

	/**
	 * Returns the action to pass.
	 * 
	 * @return the action to pass.
	 */
	public Action getAction() {
		return action;
	}

}
