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

package org.jage.examples.properties.xml;

import java.util.Collection;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.AbstractAgent;
import org.jage.agent.AgentException;
import org.jage.agent.SimpleAgent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.Property;
import org.jage.property.PropertyGetter;
import org.jage.property.PropertySetter;
import org.jage.query.AgentEnvironmentQuery;

/**
 * This agent publishes some properties and watches its environment for other agents.
 *
 * @author AGH AgE Team
 */
public class PropertiesSimpleAgent extends SimpleAgent {

	private static final long serialVersionUID = 2L;

	/**
	 * PropertiesSimpleAgent properties.
	 *
	 * @author AGH AgE Team
	 */
	public static class Properties {

		/**
		 * Actor property.
		 */
		public static final String ACTOR = "Actor";
	}

	private final Logger log = LoggerFactory.getLogger(PropertiesSimpleAgent.class);

	private String actor = null;

	@PropertySetter(propertyName = Properties.ACTOR)
	public void setActor(final String actor) {
		this.actor = actor;
	}

	@PropertyGetter(propertyName = Properties.ACTOR)
	public String getActor() {
		return actor;
	}

	/**
	 * Steps counter
	 */
	private transient int counter = 0;

	private SimpleFunctionCounter functionCounter;

	public PropertiesSimpleAgent(final AgentAddress address) {
		super(address);
	}

	@Inject
	public PropertiesSimpleAgent(final AgentAddressSupplier supplier) {
		super(supplier);
	}

	@Override
	public void init() throws ComponentException {
		super.init();
		log.info("Initializing agent: {}", getAddress());
	}

	/**
	 * Executes a step of the agent. In the execution a <code>function counter</code> strategy is called and the
	 * environment is queried. {@inheritDoc}
	 *
	 * @see org.jage.agent.SimpleAgent#step()
	 */
	@Override
	public void step() {
		counter++;
		log.info("Agent {}: step {}", getAddress(), counter);
		log.info("Agent: {} counted function. The result for {} is: {}",
		        new Object[] { getAddress(), functionCounter.toString(), functionCounter.countSquareSum() });
		if ((counter + hashCode()) % 3 == 0) {
			watch();
		}

		try {
			Thread.sleep(200);
		} catch (final InterruptedException e) {
			log.error("Interrupted", e);
		}
	}

	private void watch() {
		Collection<SimpleAgent> answer;
		try {
			final AgentEnvironmentQuery<SimpleAgent, SimpleAgent> query = new AgentEnvironmentQuery<SimpleAgent, SimpleAgent>();

			answer = queryEnvironment(query);
			log.info("Agent: {} can see in its environment: {} following agents:", getAddress(), getParentAddress());
			for (final SimpleAgent entry : answer) {
				final AgentAddress agentAddress = (AgentAddress)entry.getProperty(AbstractAgent.Properties.ADDRESS)
				        .getValue();
				if (agentAddress != getAddress()) {
					log.info("    agent: {} with properties:", agentAddress);

					for (final Property property : entry.getProperties()) {
						log.info("        {}: {}", property.getMetaProperty().getName(), property.getValue());
					}
				}
			}
		} catch (final AgentException e) {
			log.error("Agent exception", e);
		} catch (final InvalidPropertyPathException e) {
			log.error("Invalid property", e);
		}

	}

	@Override
	public boolean finish() {
		log.info("Finishing {}", getAddress());
		return true;
	}

	@PropertyGetter(propertyName = "functionCounter", isMonitorable = true)
	public SimpleFunctionCounter getFunctionCounter() {
		return functionCounter;
	}

	/**
	 * Sets a <code>function counter</code> strategy.
	 *
	 * @param functionCounter
	 *            A function to use by this agent.
	 */
	@Inject
	@PropertySetter(propertyName = "functionCounter")
	public void setFunctionCounter(final SimpleFunctionCounter functionCounter) {
		this.functionCounter = functionCounter;
	}
}
