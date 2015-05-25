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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.jage.action.SingleAction;
import org.jage.action.testHelpers.ActionTestException;
import org.jage.action.testHelpers.HelperTestAggregateActionService;
import org.jage.action.testHelpers.MultipleActionContext;
import org.jage.address.agent.AgentAddress;
import org.jage.platform.component.provider.IComponentInstanceProvider;

import static org.jage.address.selector.Selectors.singleAddress;
import static org.jage.utils.AgentTestUtils.createMockAgentWithAddress;

/**
 * Tests for the {@link AggregateActionService} class: multi-annotated contexts.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class MultiAnnotatedActionTest {

	private final SimpleAggregate aggregate = new SimpleAggregate(mock(AgentAddress.class));

	private final HelperTestAggregateActionService actionService = new HelperTestAggregateActionService();

	private final ISimpleAgent agent = createMockAgentWithAddress();

	@Mock
	private IComponentInstanceProvider componentInstanceProvider;

	private final MultipleActionContext context = new MultipleActionContext();

	@Before
	public void setUp() {
		aggregate.setInstanceProvider(componentInstanceProvider);
		aggregate.add(agent);
		actionService.setAggregate(aggregate);
		actionService.setInstanceProvider(componentInstanceProvider);
	}

	@Test(expected = ActionTestException.class)
	public void testFailNoDefaultAction() {
		// when
		actionService.doAction(new SingleAction(singleAddress(agent.getAddress()), context));
		actionService.processActions();
	}

	@Test
	public void testExecuteSpecificAction() {
		// when
		actionService.doAction(new SingleAction(singleAddress(agent.getAddress()), context, "mult1Action"));
		actionService.processActions();

		// then
		assertThat(context.actionRun, is(true));
	}

	@Test(expected = ActionTestException.class)
	public void testActionNotFoundInContext() {
		// when
		actionService.doAction(new SingleAction(singleAddress(agent.getAddress()), context, "mult3Action"));
		actionService.processActions();
	}
}
