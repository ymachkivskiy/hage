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
 * Created: 2011-05-06
 * $Id$
 */

package org.jage.action.preparers;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

import org.jage.address.agent.AgentAddress;
import org.jage.agent.IAgent;
import org.jage.agent.IAgentEnvironment;

/**
 * Base class for {@link IActionPreparer} Tests.
 * 
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractActionPreparatorTest {

	@Mock
	protected IAgent agent;

	@Mock
	protected IAgentEnvironment environment;

	@Mock
	protected AgentAddress agentAddress;

	@Before
	public void setUp() {
		when(agent.getAddress()).thenReturn(agentAddress);
	}
}
