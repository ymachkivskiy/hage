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

package org.jage.agent;

import java.util.Collection;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.jage.action.Action;
import org.jage.action.IActionContext;
import org.jage.action.SingleAction;
import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;

import static org.jage.address.selector.Selectors.singleAddress;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * Base class for agents processed in step simulation. For each step of simulation the parent of the agent executes
 * {@link #step()} method.
 *
 * @author AGH AgE Team
 */
public abstract class SimpleAgent extends AbstractAgent implements ISimpleAgent {

	private static final long serialVersionUID = 6487323250205969364L;

	private transient ISimpleAgentEnvironment agentEnvironment;

	public SimpleAgent(final AgentAddress address) {
	    super(address);
    }

	public SimpleAgent(final AgentAddressSupplier supplier) {
    	this(supplier.get());
    }

	@Override
	public void setAgentEnvironment(@CheckForNull final IAgentEnvironment agentEnvironment) {
		checkState(getAddress() != null, "Agent has no address!");
		if (agentEnvironment == null) {
			this.agentEnvironment = null;
		} else if (this.agentEnvironment == null) {
			checkArgument(agentEnvironment instanceof ISimpleAgentEnvironment);
			this.agentEnvironment = (ISimpleAgentEnvironment)agentEnvironment;
		} else {
			throw new AgentException(String.format("Environment in %s is already set.", this));
		}
	}

	@Override @Nullable
	protected ISimpleAgentEnvironment getAgentEnvironment() {
		return agentEnvironment;
	}

	/**
	 * Submits an action to be executed in the local environment.
	 *
	 * @param action
	 *            the action to be executed
	 * @throws AgentException
	 *             if the action is incorrect
	 */
	protected void doAction(final Action action) {
		checkState(agentEnvironment != null, "Agent environment is not available.");
		agentEnvironment.submitAction(action);
	}

	protected void doActions(final Collection<? extends Action> actions) {
		checkState(agentEnvironment != null, "Agent environment is not available.");
		for (final Action action : actions) {
			agentEnvironment.submitAction(action);
		}
	}

	protected final Action onSelf(final IActionContext actionContext) {
		return new SingleAction(singleAddress(getAddress()), actionContext);
	}
}
