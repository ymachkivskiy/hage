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

package org.jage.agent;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.Action;
import org.jage.action.preparers.IActionPreparer;
import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.property.PropertyField;

/**
 * This agent implementation relies on a {@link org.jage.action.preparers.IActionPreparer} to provide its actual behavior.
 * <p>
 *
 * Given the agent's state and environment, the {@link org.jage.action.preparers.IActionPreparer} prepares an appropriate action, which is then
 * run by the agent.
 *
 * @author AGH AgE Team
 */
public class ActionDrivenAgent extends SimpleAgent {

	/**
	 * ActionDrivenAgent properties.
	 *
	 * @author AGH AgE Team
	 */
	public static class Properties {

		/**
		 * The actual step of computation.
		 */
		public static final String STEP = "step";
	}

	public ActionDrivenAgent(final AgentAddress address) {
	    super(address);
    }

	@Inject
	public ActionDrivenAgent(final AgentAddressSupplier supplier) {
    	super(supplier.get());
    }

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ActionDrivenAgent.class);

	@Inject
	private IActionPreparer<ActionDrivenAgent> actionPreparator;

	@PropertyField(propertyName = Properties.STEP)
	private long step = 0;

    public long getStep() {
	    return step;
    }

	@Override
	public void step() {
		try {
			final List<Action> actions = actionPreparator.prepareActions(this);
			log.debug("Submitting actions: {}", actions);
			doActions(actions);
		} catch (final AgentException e) {
			LOG.error("An exception occurred during the action call", e);
		}

		step++;
		notifyMonitorsForChangedProperties();
	}
}
