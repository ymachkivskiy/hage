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


import org.jage.action.ComplexAction;
import org.jage.action.IActionContext;
import org.jage.action.SingleAction;
import org.jage.action.testHelpers.ActionTestException;
import org.jage.action.testHelpers.BadStrategyTestActionContext;
import org.jage.action.testHelpers.HelperTestActionStrategy;
import org.jage.action.testHelpers.HelperTestAggregateActionService;
import org.jage.action.testHelpers.HelperTestBadStrategy;
import org.jage.action.testHelpers.MixedActionContext;
import org.jage.action.testHelpers.MixedActionStrategy;
import org.jage.action.testHelpers.NonExistentStrategyTestActionContext;
import org.jage.action.testHelpers.SimpleTestActionContext;
import org.jage.action.testHelpers.StrategyTestActionContext;
import org.jage.address.agent.AgentAddress;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.jage.address.selector.Selectors.singleAddress;
import static org.jage.utils.AgentTestUtils.createMockAgentWithAddress;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Tests for the {@link AggregateActionService} class: the complex actions execution.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class StrategyActionTest {

    private final SimpleAggregate aggregate = new SimpleAggregate(mock(AgentAddress.class));

    private final HelperTestAggregateActionService actionService = new HelperTestAggregateActionService();

    private final ISimpleAgent agent = createMockAgentWithAddress();

    @Mock
    private IComponentInstanceProvider instanceProvider;

    @Before
    public void setUp() {
        aggregate.setInstanceProvider(instanceProvider);
        aggregate.add(agent);
        aggregate.setActionService(actionService);
        actionService.setAggregate(aggregate);
        actionService.setInstanceProvider(instanceProvider);
    }

    @Test
    public void testBasicStrategyAction() {
        // given
        given(instanceProvider.getInstance(StrategyTestActionContext.ACTION_NAME)).willReturn(
                new HelperTestActionStrategy());

        final StrategyTestActionContext context = new StrategyTestActionContext();

        // when
        actionService.doAction(new SingleAction(singleAddress(agent.getAddress()), context));
        actionService.processActions();

        // then
        verify(instanceProvider, times(3)).getInstance(StrategyTestActionContext.ACTION_NAME);
        assertThat(context.actionRun, is(true));
    }

    @Test(expected = ActionTestException.class)
    public void testStrategyAndMethodNotFound() {
        // given
        given(instanceProvider.getInstance(NonExistentStrategyTestActionContext.ACTION_NAME)).willReturn(null);
        final NonExistentStrategyTestActionContext context = new NonExistentStrategyTestActionContext();

        // when
        actionService.doAction(new SingleAction(singleAddress(agent.getAddress()), context));
        actionService.processActions();
    }

    @Test(expected = ActionTestException.class)
    public void testNotAnActionStrategy() {
        // given
        given(instanceProvider.getInstance(BadStrategyTestActionContext.ACTION_NAME)).willReturn(
                new HelperTestBadStrategy());

        final BadStrategyTestActionContext context = new BadStrategyTestActionContext();

        // when
        actionService.doAction(new SingleAction(singleAddress(agent.getAddress()), context));
        actionService.processActions();
    }

    @Test
    public void testFallbackToAggregateMethod() {
        // given
        given(instanceProvider.getInstance("simpleTestAction")).willReturn(null);
        final SimpleTestActionContext context = new SimpleTestActionContext();

        // when
        actionService.doAction(new SingleAction(singleAddress(agent.getAddress()), context));
        actionService.processActions();

        // then
        assertThat(context.actionRun, is(true));
        verify(instanceProvider, times(3)).getInstance("simpleTestAction");
    }

    /**
     * Test a complex action consisting of two simple actions, using the same context. One of the actions is strategic,
     * the other is executed in the aggregate.
     */
    @Test
    public void testMixedComplexAction() {
        // given
        given(instanceProvider.getInstance(MixedActionContext.STRATEGIC_ACTION_ID)).willReturn(
                new MixedActionStrategy());
        given(instanceProvider.getInstance(MixedActionContext.AGGREGATE_ACTION_ID)).willReturn(null);

        final MixedActionContext context = new MixedActionContext();

        final ComplexAction c = new ComplexAction();
        c.addChild(new SingleAction(singleAddress(agent.getAddress()), context, MixedActionContext.STRATEGIC_ACTION_ID));
        c.addChild(new SingleAction(singleAddress(agent.getAddress()), context, MixedActionContext.AGGREGATE_ACTION_ID));

        // when
        actionService.doAction(c);
        actionService.processActions();

        // then
        verify(instanceProvider, times(3)).getInstance(MixedActionContext.STRATEGIC_ACTION_ID);
        verify(instanceProvider, times(3)).getInstance(MixedActionContext.AGGREGATE_ACTION_ID);
        assertThat(context.getExecAggr(), is(true));
        assertThat(context.getExecStrat(), is(true));
    }

    /**
     * Test a complex action consisting of three simple actions, using the same context. One of the actions is
     * strategic, the other is executed in the aggregate, and the last one is an unknown action.
     */
    @Test
    public void testUnknownAction() {
        // All actions will perform only initialization phase in this phase unknown action won't be found and
        // AgentException will be thrown.

        // given
        given(instanceProvider.getInstance(MixedActionContext.STRATEGIC_ACTION_ID)).willReturn(
                new MixedActionStrategy());
        given(instanceProvider.getInstance(MixedActionContext.AGGREGATE_ACTION_ID)).willReturn(null);
        given(instanceProvider.getInstance(MixedActionContext.UNKNOWN_ACTION_ID)).willReturn(null);

        final MixedActionContext context = new MixedActionContext();

        final ComplexAction c = new ComplexAction();
        c.addChild(new SingleAction(singleAddress(agent.getAddress()), context, MixedActionContext.STRATEGIC_ACTION_ID));
        c.addChild(new SingleAction(singleAddress(agent.getAddress()), context, MixedActionContext.AGGREGATE_ACTION_ID));
        c.addChild(new SingleAction(singleAddress(agent.getAddress()), context, MixedActionContext.UNKNOWN_ACTION_ID));

        // when
        actionService.doAction(c);
        try {
            actionService.processActions();
            // then
            fail();
        } catch(final ActionTestException e) {
            assertThat(context.getExecAggr(), is(true));
            assertThat(context.getExecStrat(), is(true));
            assertThat(context.getExecUnknown(), is(false));
        }
    }

    /**
     * Test a complex action consisting of three simple actions, using the same context. One of the actions is
     * strategic, the other is executed in the aggregate, and the last one is unknown. Use an aggregate with overridden
     * unknown action handling.
     */
    @SuppressWarnings("hiding")
    @Test
    public void testUnknownActionWithCustomHandling() {
        // all actions will perform in all phases because default handling of unknown action
        // does not throw any exception so it does not stop actions processing

        // given
        given(instanceProvider.getInstance(MixedActionContext.STRATEGIC_ACTION_ID)).willReturn(
                new MixedActionStrategy());
        given(instanceProvider.getInstance(MixedActionContext.AGGREGATE_ACTION_ID)).willReturn(null);
        given(instanceProvider.getInstance(MixedActionContext.UNKNOWN_ACTION_ID)).willReturn(null);

        final MixedActionContext context = new MixedActionContext();

        final ComplexAction c = new ComplexAction();
        c.addChild(new SingleAction(singleAddress(agent.getAddress()), context, MixedActionContext.STRATEGIC_ACTION_ID));
        c.addChild(new SingleAction(singleAddress(agent.getAddress()), context, MixedActionContext.AGGREGATE_ACTION_ID));
        c.addChild(new SingleAction(singleAddress(agent.getAddress()), context, MixedActionContext.UNKNOWN_ACTION_ID));

        final HelperTestAggregateActionService actionService = new HelperTestAggregateActionService() {

            @Override
            protected void handleUnknownAction(final String actionName, final IActionContext context) throws AgentException {
                assertThat(context, is(instanceOf(MixedActionContext.class)));
                assertThat(actionName, is(MixedActionContext.UNKNOWN_ACTION_ID));
                ((MixedActionContext) context).setExecUnknown(true);
            }
        };
        actionService.setAggregate(aggregate);
        actionService.setInstanceProvider(instanceProvider);

        // when
        actionService.doAction(c);
        actionService.processActions();

        // then
        assertThat(context.getExecAggr(), is(true));
        assertThat(context.getExecStrat(), is(true));
        assertThat(context.getExecUnknown(), is(true));

        verify(instanceProvider, times(3)).getInstance(MixedActionContext.STRATEGIC_ACTION_ID);
        verify(instanceProvider, times(3)).getInstance(MixedActionContext.AGGREGATE_ACTION_ID);
        verify(instanceProvider, times(3)).getInstance(MixedActionContext.UNKNOWN_ACTION_ID);
    }
}
