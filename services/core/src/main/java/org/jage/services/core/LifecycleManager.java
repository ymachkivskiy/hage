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
 * Created: 2012-08-21
 * $Id$
 */

package org.jage.services.core;

/**
 * A service that manages the lifecycle of every AgE node.
 * 
 * @author AGH AgE Team
 */
public interface LifecycleManager {

	public static final String SERVICE_NAME = LifecycleManager.class.getSimpleName();

	/**
	 * States of this lifecycle manager (in other words - states of the node).
	 *
	 * @author AGH AgE Team
	 */
	public static enum State {
		/**
		 * Initial state of the node.
		 */
		OFFLINE,
		/**
		 * Node has been initialized.
		 */
		INITIALIZED,
		/**
		 * Computation was loaded and initialized.
		 */
		CONFIGURED,
		/**
		 * Computation is running.
		 */
		RUNNING,
		/**
		 * Computation is paused.
		 */
		PAUSED,
		/**
		 * Computation has been stopped.
		 */
		STOPPED,
		/**
		 * Node has failed.
		 */
		FAILED,
		/**
		 * Node has terminated (terminal state).
		 */
		TERMINATED;
	}

	/**
	 * Events that can occur in the node.
	 *
	 * @author AGH AgE Team
	 */
	public static enum Event {
		/**
		 * Sent by the bootstrapper.
		 */
		INITIALIZE,
		/**
		 * Sent by the configuration service when configuration is available.
		 */
		CONFIGURE,
		/**
		 * Starts the computation.
		 */
		START_COMMAND,
		/**
		 * Notifies that the core component is starting.
		 */
		CORE_STARTING,
		/**
		 * Pauses the computation.
		 */
		PAUSE,
		/**
		 * Resumes the computation.
		 */
		RESUME,
		/**
		 * Stops the computation completely.
		 */
		STOP_COMMAND,
		/**
		 * Notifies that the core component has stopped.
		 */
		CORE_STOPPED,
		/**
		 * Clears the node from the computation configuration.
		 */
		CLEAR,
		/**
		 * Indicates that an error occurred.
		 */
		ERROR,
		/**
		 * Terminates the node.
		 */
		EXIT;
	}

    void start();
}
