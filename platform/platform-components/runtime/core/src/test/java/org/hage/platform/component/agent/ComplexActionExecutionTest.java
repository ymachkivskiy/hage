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


import org.hage.platform.component.action.ComplexAction;
import org.hage.platform.component.action.SingleAction;
import org.hage.platform.component.action.testHelpers.ActionTestException;
import org.hage.platform.component.action.testHelpers.HelperTestAggregateActionService;
import org.hage.platform.component.action.testHelpers.TracingActionContext;
import org.hage.platform.component.provider.IComponentInstanceProvider;
import org.hage.platform.util.communication.address.agent.AgentAddress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hage.platform.component.utils.AgentTestUtils.createMockAgentWithAddress;
import static org.hage.platform.util.communication.address.selector.Selectors.singleAddress;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Tests for the {@link AggregateActionService} class: the complex actions execution.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ComplexActionExecutionTest {

    private final SimpleAggregate aggregate = new SimpleAggregate(mock(AgentAddress.class));

    private final HelperTestAggregateActionService actionService = new HelperTestAggregateActionService();

    private final ISimpleAgent agent = createMockAgentWithAddress();

    private final TracingActionContext context = new TracingActionContext();

    @Mock
    private IComponentInstanceProvider componentInstanceProvider;

    @Before
    public void setUp() {
        aggregate.setInstanceProvider(componentInstanceProvider);
        aggregate.add(agent);
        actionService.setAggregate(aggregate);
        actionService.setInstanceProvider(componentInstanceProvider);
    }

    @Test(expected = ActionTestException.class)
    public void testEmptyComplex() {
        // given
        final ComplexAction action = new ComplexAction();

        // when
        actionService.doAction(action);
        actionService.processActions();
    }

    @Test
    public void testSimpleTrace() {
        // when
        actionService.doAction(new SingleAction(singleAddress(agent.getAddress()), context, "c1Action"));
        actionService.processActions();

        // then
        assertThat(context.trace, is("C1I1"));
    }

    @Test
    public void testFlatComplex() {
        // given
        final ComplexAction action = new ComplexAction();
        action.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c1Action"));
        action.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c3Action"));
        action.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c2Action"));

        // when
        actionService.doAction(action);
        actionService.processActions();

        // then
        assertThat(context.trace, is("C1IC3IC2I132"));
    }

    /**
     * Create a tree structure of complex actions, looking like this:
     * <p>
     * <pre>
     *  c
     *   1
     *  2
     * </pre>
     */
    @Test
    public void testTreeComplexAction1() {
        // given
        final ComplexAction action = new ComplexAction();
        final ComplexAction action1 = new ComplexAction();
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c1Action"));
        action.addChild(action1);
        action.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c2Action"));

        // when
        actionService.doAction(action);
        actionService.processActions();

        // then
        assertThat(context.trace, is("C1IC2I12"));
    }

    /**
     * Create a tree structure of complex actions, looking like this:
     * <p>
     * <pre>
     *  c
     *   1
     *  c
     *   2
     * </pre>
     */
    @Test
    public void testTreeComplexAction2() {
        // given
        final ComplexAction action = new ComplexAction();
        final ComplexAction action1 = new ComplexAction();
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c1Action"));
        action.addChild(action1);
        final ComplexAction action2 = new ComplexAction();
        action2.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c2Action"));
        action.addChild(action2);

        // when
        actionService.doAction(action);
        actionService.processActions();

        // then
        assertThat(context.trace, is("C1IC2I12"));
    }

    /**
     * Create a tree structure of complex actions, looking like this:
     * <p>
     * <pre>
     *  c
     *   1
     *  c
     *   2
     *   3
     * </pre>
     */
    @Test
    public void testTreeComplexAction3() {
        // given
        final ComplexAction action = new ComplexAction();
        final ComplexAction action1 = new ComplexAction();
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c1Action"));
        action.addChild(action1);
        final ComplexAction action2 = new ComplexAction();
        action2.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c2Action"));
        action2.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c3Action"));
        action.addChild(action2);

        // when
        actionService.doAction(action);
        actionService.processActions();

        // then
        assertThat(context.trace, is("C1IC2IC3I123"));
    }

    /**
     * Create a tree structure of complex actions, looking like this:
     * <p>
     * <pre>
     *  1
     *  c
     *   2
     *   3
     * </pre>
     */
    @Test
    public void testComplexAction4() {
        // given
        final ComplexAction action = new ComplexAction();
        action.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c1Action"));
        final ComplexAction action1 = new ComplexAction();
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c2Action"));
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c3Action"));
        action.addChild(action1);

        // when
        actionService.doAction(action);
        actionService.processActions();

        // then
        assertThat(context.trace, is("C1IC2IC3I123"));
    }

    /**
     * Create a tree structure of complex actions, looking like this:
     * <p>
     * <pre>
     *  1
     *  c
     *   2
     *   3
     *   4
     *   4
     * </pre>
     */
    @Test
    public void testComplexAction5() {
        // given
        final ComplexAction action = new ComplexAction();
        action.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c1Action"));
        final ComplexAction action1 = new ComplexAction();
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c2Action"));
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c3Action"));
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c4Action"));
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c4Action"));
        action.addChild(action1);

        // when
        actionService.doAction(action);
        actionService.processActions();

        // then
        assertThat(context.trace, is("C1IC2IC3IC4IC4I12344"));
    }

    /**
     * Create a tree structure of complex actions, looking like this:
     * <p>
     * <pre>
     *  1
     *  2
     *  c
     *   3
     *   4
     * </pre>
     */
    @Test
    public void testComplexAction6() {
        // given
        final ComplexAction action = new ComplexAction();
        action.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c1Action"));
        action.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c2Action"));
        final ComplexAction action1 = new ComplexAction();
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c3Action"));
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c4Action"));
        action.addChild(action1);

        // when
        actionService.doAction(action);
        actionService.processActions();

        // then
        assertThat(context.trace, is("C1IC2IC3IC4I1234"));
    }

    /**
     * Create a complex tree structure of complex actions, looking like this:
     * <p>
     * <pre>
     *  c
     *   2
     *   3
     *  c
     *   3
     *   4
     *   1
     *  c // action3
     *   c // action32
     *    c // action324
     *     1
     *   3
     *  c
     *   1
     *  1
     * </pre>
     */
    @Test
    public void testConvolutedComplexAction() {
        // given
        final ComplexAction action = new ComplexAction();
        final ComplexAction action1 = new ComplexAction();
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c2Action"));
        action1.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c3Action"));
        action.addChild(action1);
        final ComplexAction action2 = new ComplexAction();
        action2.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c3Action"));
        action2.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c4Action"));
        action2.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c1Action"));
        action.addChild(action2);
        final ComplexAction action3 = new ComplexAction();
        final ComplexAction action32 = new ComplexAction();
        final ComplexAction action324 = new ComplexAction();
        action324.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c1Action"));
        action32.addChild(action324);
        action3.addChild(action32);
        action3.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c3Action"));
        action.addChild(action3);
        final ComplexAction action4 = new ComplexAction();
        action4.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c1Action"));
        action.addChild(action4);
        action.addChild(new SingleAction(singleAddress(agent.getAddress()), context, "c1Action"));

        // when
        actionService.doAction(action);
        actionService.processActions();

        // then
        assertThat(context.trace, is("C2IC3IC3IC4IC1IC1IC3IC1IC1I233411311"));
    }
}
