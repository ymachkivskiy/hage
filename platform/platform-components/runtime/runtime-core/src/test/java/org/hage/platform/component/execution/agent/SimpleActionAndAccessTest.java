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


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.component.execution.action.SingleAction;
import org.hage.platform.component.execution.action.testHelpers.*;
import org.hage.platform.component.provider.IComponentInstanceProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hage.platform.communication.address.selector.Selectors.singleAddress;
import static org.hage.platform.component.execution.utils.AgentTestUtils.createMockAgentWithAddress;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Tests for the {@link AggregateActionService} class: simple actions and methods access qualifiers.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleActionAndAccessTest {


    private final SimpleAggregate aggregate = new SimpleAggregate(mock(AgentAddress.class));

    private final HelperTestAggregateActionService actionService = new HelperTestAggregateActionService();

    private final ISimpleAgent agent = createMockAgentWithAddress();

    @Mock
    private IComponentInstanceProvider componentInstanceProvider;

    @Before
    public void setUp() {
        aggregate.setInstanceProvider(componentInstanceProvider);
        aggregate.add(agent);
        actionService.setAggregate(aggregate);
        actionService.setInstanceProvider(componentInstanceProvider);
    }

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testNullContext() {
        // when
        new SingleAction(singleAddress(agent.getAddress()), null);
    }

    @Test
    public void testExecuteDefaultActionFromContext() {
        // given
        final SimpleTestActionContext context = new SimpleTestActionContext();

        // when
        actionService.doAction(new SingleAction(singleAddress(agent.getAddress()), context));
        actionService.processActions();

        // then
        assertThat(context.actionRun, is(true));
    }

    @Test(expected = ActionTestException.class)
    public void testFailPrivateMethod() throws Exception {
        // given
        final PrivateTestActionContext context = new PrivateTestActionContext();

        // when
        actionService.doAction(new SingleAction(singleAddress(agent.getAddress()), context));
        actionService.processActions();
    }

    @Test
    public void testPackageMethod() throws Exception {
        // given
        final PackageTestActionContext context = new PackageTestActionContext();

        // when
        actionService.doAction(new SingleAction(singleAddress(agent.getAddress()), context));
        actionService.processActions();

        // then
        assertThat(context.actionRun, is(true));
    }
}
