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

package org.jage.examples.strategy;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.SimpleAgent;

/**
 * This agent presents an example of using strategies.
 *
 * @author AGH AgE Team
 */
public class StrategySimpleAgent extends SimpleAgent {

	private static final long serialVersionUID = 2L;

	private final static Logger log = LoggerFactory.getLogger(StrategySimpleAgent.class);

	private IEchoStrategy echoStrategy;

	public StrategySimpleAgent(final AgentAddress address) {
		super(address);
	}

	@Inject
	public StrategySimpleAgent(final AgentAddressSupplier supplier) {
		super(supplier);
	}

	@Inject
	public void setEchoStrategy(final IEchoStrategy echoStrategy) {
		this.echoStrategy = echoStrategy;
	}

	/**
	 * Executes a step of the agent. This agent simply executes an echo strategy that was configured for it.
	 * <p>
	 * {@inheritDoc}
	 *
	 * @see org.jage.agent.SimpleAgent#step()
	 */
	@Override
	public void step() {
		log.info("{} says Hello World from {}", getAddress().getFriendlyName(), getParentAddress().getFriendlyName());

		echoStrategy.echo();

		try {
			Thread.sleep(200);
		} catch (final InterruptedException e) {
			log.error("Interrupted", e);
		}
	}

	@Override
	public boolean finish() {
		log.info("Finishing Hello World Simple Agent: {}", getAddress().getFriendlyName());
		return true;
	}
}
