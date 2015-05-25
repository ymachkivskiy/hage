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
 * Created: 2012-01-30
 * $Id$
 */

package org.jage.emas.migration;

import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.strategy.IStrategy;

/**
 * Strategy for agents migrations.
 *
 * @param <A>
 *            the type of agents
 *
 * @author AGH AgE Team
 */
public interface Migration<A extends IAgent> extends IStrategy {

	/**
	 * Migrates the given agent.
	 *
	 * @param agent
	 *            an agent to be migrated
	 * @throws AgentException
	 *             if the migration fails
	 */
	void migrate(A agent) throws AgentException;
}
