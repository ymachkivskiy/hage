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

package org.jage.action.preparators;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jage.action.Action;
import org.jage.action.IActionContext;
import org.jage.action.SingleAction;
import org.jage.agent.IAgent;

/**
 * Tests for {@link SingleActionPreparator}.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class SingleActionPreparatorTest extends AbstractActionPreparatorTest {

	@Mock
	private IActionContext actionContext;

	@InjectMocks
	private final SingleActionPreparator<IAgent> preparator = new SingleActionPreparator<IAgent>();

	@Test
	public void testPrepareAction() {
		// when
		Collection<Action> actions = preparator.prepareActions(agent);

		// then
		assertEquals(1, actions.size());
		Action action = actions.iterator().next();
		assertTrue(action instanceof SingleAction);
		SingleAction singleAction = (SingleAction)action;
		assertTrue(singleAction.getTarget().selects(agentAddress));
		assertEquals(actionContext, singleAction.getContext());
	}
}
