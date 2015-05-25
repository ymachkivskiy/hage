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

package org.jage.workplace;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.jage.address.agent.AgentAddress;
import org.jage.agent.IAgent;
import org.jage.agent.IAgentEnvironment;
import org.jage.communication.message.Message;

/**
 * The component that carries out simulation. It is responsible for agents, their life and activities.
 *
 * @author AGH AgE Team
 */
public interface Workplace extends IAgentEnvironment {

	/**
	 * States of workplaces.
	 */
	public enum State {
		/**
		 * A workplace is running and performing computation.
		 */
		RUNNING,
		/**
		 * A workplace is stopping - it will transition into STOPPED state.
		 */
		STOPPING,
		/**
		 * A workplace is paused temporarily.
		 */
		PAUSED,
		/**
		 * The workplace is stopped.
		 */
		STOPPED
	}

	/**
	 * Sets new workplace environment to this workplace.
	 * 
	 * @param workplaceEnvironment
	 *            A workplace environment to set.
	 * 
	 * @throws WorkplaceException
	 *             when environment is already set.
	 */
	void setEnvironment(@CheckForNull WorkplaceEnvironment workplaceEnvironment);

	/**
	 * Starts the workplace.
	 */
	void start();

	/**
	 * Pauses the workplace.
	 */
	void pause();

	/**
	 * Resumes the workplace if paused.
	 */
	void resume();

	/**
	 * Stops the workplace. After calling this method workplace can be restarted - using the {@link #start} method.
	 */
	void stop();

	/**
	 * Terminates the workplace. After calling this method workplace cannot be started anymore.
	 */
	boolean terminate();

	/**
	 * Checks whether the agent is running.
	 *
	 * @return true, if the agent is running.
	 */
	boolean isRunning();

	/**
	 * Checks whether the agent is paused.
	 *
	 * @return true, if the agent is paused.
	 */
	boolean isPaused();

	/**
	 * Checks whether the agent is stopped.
	 *
	 * @return true, if the agent is stopped.
	 */
	boolean isStopped();

	/**
	 * Delivers an agent message to the workplace.
	 *
	 * @param message
	 *            A message to be delivered.
	 */
	void deliverMessage(@Nonnull Message<AgentAddress, ?> message);

	void sendAgent(@Nonnull IAgent agent);

	/**
	 * Returns the agent operating in this workplace.
	 *
	 * @return an agent.
	 */
	@Nonnull IAgent getAgent();
}
