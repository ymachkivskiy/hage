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

package org.hage.platform.component.agent;


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.selector.ExplicitSelector;
import org.hage.platform.communication.address.selector.Selectors;
import org.hage.platform.component.action.Action;
import org.hage.platform.component.action.NullInstanceProvider;
import org.hage.platform.component.action.SingleAction;
import org.hage.platform.component.action.context.PassToParentActionContext;
import org.hage.platform.component.action.testHelpers.ActionTestException;
import org.hage.platform.component.action.testHelpers.DoNotPassToParentTestActionContext;
import org.hage.platform.component.action.testHelpers.HelperTestAggregateActionService;
import org.hage.platform.component.action.testHelpers.PassToParentTestActionContext;
import org.hage.platform.component.provider.IComponentInstanceProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hage.platform.communication.address.selector.Selectors.parentOf;
import static org.hage.platform.communication.address.selector.Selectors.singleAddress;
import static org.hage.platform.component.utils.AgentTestUtils.createMockAgentWithAddress;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Tests for the {@link AggregateActionService} class: the action validation.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ActionValidationTest {

    private final SimpleAggregate aggregate = new SimpleAggregate(mock(AgentAddress.class));

    private final HelperTestAggregateActionService actionService = new HelperTestAggregateActionService();

    private final SimpleAggregate aggregate2 = new SimpleAggregate(mock(AgentAddress.class));

    private final HelperTestAggregateActionService actionService2 = new HelperTestAggregateActionService();

    private final ISimpleAgent agent = createMockAgentWithAddress();

    private final ISimpleAgent agent2 = createMockAgentWithAddress();

    @Mock
    private IComponentInstanceProvider instanceProvider;

    @Before
    public void setUp() {
        actionService.setAggregate(aggregate);
        actionService.setInstanceProvider(instanceProvider);

        actionService2.setAggregate(aggregate2);
        actionService2.setInstanceProvider(new NullInstanceProvider());

        aggregate.setInstanceProvider(instanceProvider);
        aggregate.setActionService(actionService);
        aggregate2.setInstanceProvider(new NullInstanceProvider());
        aggregate2.setActionService(actionService2);

        aggregate.add(agent);
        aggregate.add(aggregate2);
        aggregate2.add(agent2);
    }

    // FIXME

    @Test(expected = ActionTestException.class)
    public void testNoSuchAgent() {
        // given
        final DoNotPassToParentTestActionContext context = new DoNotPassToParentTestActionContext();
        final ExplicitSelector<AgentAddress> random = singleAddress(mock(AgentAddress.class));

        // when
        actionService.doAction(new SingleAction(random, context));
        actionService.processActions();
    }

    @Test
    public void testInvokeOnSelf() {
        // given
        final DoNotPassToParentTestActionContext context = new DoNotPassToParentTestActionContext();

        // when
        actionService2.doAction(new SingleAction(singleAddress(agent2.getAddress()), context));
        actionService2.processActions();

        // then
        assertThat(context.actionTarget, is(agent2));
    }

    @Test
    public void testInvokeOnOwnAggregate() {
        // given
        final DoNotPassToParentTestActionContext context = new DoNotPassToParentTestActionContext();

        // when
        actionService2.doAction(new SingleAction(parentOf(agent2.getAddress()), context));
        actionService2.processActions();

        // then
        assertThat(context.actionTarget, is((ISimpleAgent) aggregate2));
    }

    /**
     * This action fails because there is an attempt to invoke it on the agent in the parent aggregate, but action is
     * not wrapped in {@link PassToParentActionContext}. Correct usage is shown in
     * {@link #testPassToParentInvokeOnAgentInParent()}
     */
    @Test(expected = ActionTestException.class)
    public void testInvokeOnAgentInParent() {
        // given
        final DoNotPassToParentTestActionContext context = new DoNotPassToParentTestActionContext();

        // when
        actionService2.doAction(new SingleAction(singleAddress(agent.getAddress()), context));
        actionService2.processActions();
    }

    /**
     * This action fails because there is an attempt to invoke action on parent aggregate, but action is not wrapped in
     * {@link PassToParentActionContext} . Correct usage is shown in {@link #testPassToParentInvokeOnParentAggregate()}
     */
    @Test(expected = ActionTestException.class)
    public void testInvokeOnParentAggregate() {
        // given
        final DoNotPassToParentTestActionContext context = new DoNotPassToParentTestActionContext();

        // when
        actionService2.doAction(new SingleAction(singleAddress(aggregate.getAddress()), context));
        actionService2.processActions();
    }

    @Test
    public void testPassToParentInvokeOnSelf() {
        // given
        final PassToParentTestActionContext context = new PassToParentTestActionContext();

        // when
        actionService2.doAction(new SingleAction(singleAddress(agent2.getAddress()), context));
        actionService2.processActions();

        // then
        assertThat(context.actionTarget, is(agent2));
    }

    @Test
    public void testPassToParentInvokeOnOwnAggregate() {
        // given
        final PassToParentTestActionContext context = new PassToParentTestActionContext();

        // when
        actionService2.doAction(new SingleAction(Selectors.parentOf(agent2.getAddress()), context));
        actionService2.processActions();

        // then
        assertThat(context.actionTarget, is((ISimpleAgent) aggregate2));
    }

    @Test
    public void testPassToParentInvokeOnAgentInParent() {
        // given
        final PassToParentTestActionContext context = new PassToParentTestActionContext();
        final Action action = new SingleAction(singleAddress(agent.getAddress()), context);
        final PassToParentActionContext outerContext = new PassToParentActionContext(agent2.getAddress(), action);

        // when
        actionService2.doAction(new SingleAction(parentOf(agent2.getAddress()), outerContext));
        actionService2.processActions();
        actionService.processActions();

        // then
        assertThat(context.actionTarget, is(agent));
    }

    @Test
    public void testPassToParentInvokeOnParentAggregate() {
        // given
        final PassToParentTestActionContext context = new PassToParentTestActionContext();
        final SingleAction action = new SingleAction(parentOf(aggregate2.getAddress()), context);
        final PassToParentActionContext outerContext = new PassToParentActionContext(agent2.getAddress(), action);

        // when
        actionService2.doAction(new SingleAction(parentOf(agent2.getAddress()), outerContext));
        actionService2.processActions();
        actionService.processActions();

        // then
        assertThat(context.actionTarget, is((ISimpleAgent) aggregate));
    }

    /**
     * This action fails because there is an attempt to perform action on parent aggregate, but the aggregate has no
     * agent environment.
     */
    @Test(expected = ActionTestException.class)
    public void testPassToParentInvokeOnUnavailableParentAggregate() {
        // given
        aggregate.setAgentEnvironment(null);
        final PassToParentTestActionContext context = new PassToParentTestActionContext();
        final SingleAction action = new SingleAction(parentOf(agent.getAddress()), context);
        final PassToParentActionContext outerContext = new PassToParentActionContext(agent.getAddress(), action);

        // when
        actionService.doAction(new SingleAction(parentOf(agent.getAddress()), outerContext));
        actionService.processActions();
    }
}
