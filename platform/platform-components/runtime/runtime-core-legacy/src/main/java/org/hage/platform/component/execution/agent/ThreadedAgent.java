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

package org.hage.platform.component.execution.agent;


/**
 * An agent that can be run as a thread.
 *
 * @author AGH AgE Team
 */
public interface ThreadedAgent extends IAgent {

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
}
