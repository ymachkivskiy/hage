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
 * Created: 2011-04-09
 * $Id$
 */

package org.hage.platform.component.agent;


import org.hage.platform.component.action.ComplexAction;
import org.hage.platform.component.action.SingleAction;
import org.hage.platform.component.action.testHelpers.HelperTestAggregateActionService;
import org.hage.platform.component.action.testHelpers.TracingActionContext;
import org.hage.platform.component.provider.IComponentInstanceProvider;
import org.hage.platform.util.communication.address.agent.AgentAddress;
import org.hage.platform.util.communication.address.selector.AddressPredicate;
import org.hage.platform.util.communication.address.selector.AddressSelector;
import org.hage.platform.util.communication.address.selector.Selectors;
import org.hage.platform.util.communication.address.selector.UnicastSelector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;
import static org.hage.platform.component.utils.AgentTestUtils.createSimpleAgentWithoutStep;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Tests for the {@link AggregateActionService} class: the action execution.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class AggregateActionServiceExecuteActionsTest {

    private static final int AGENT_COUNT = 10;

    private final SimpleAggregate aggregate = new SimpleAggregate(mock(AgentAddress.class));

    private final HelperTestAggregateActionService actionService = new HelperTestAggregateActionService();
    private final List<UnicastSelector<AgentAddress>> unicasts = newLinkedList();
    private AgentAddress[] addresses;
    private AddressSelector<AgentAddress> broadcast;

    private ComplexAction action;

    private TracingActionContext context;

    @Mock
    private IComponentInstanceProvider componentInstanceProvider;

    @Before
    public void setUp() throws Exception {
        // Configure agents
        actionService.setInstanceProvider(componentInstanceProvider);
        actionService.setAggregate(aggregate);

        final ISimpleAgent[] agents = new SimpleAgent[AGENT_COUNT];
        addresses = new AgentAddress[AGENT_COUNT];

        for(int i = 0; i < AGENT_COUNT; i++) {
            final SimpleAgent agent = createSimpleAgentWithoutStep();
            agent.init();
            agents[i] = agent;
            addresses[i] = agent.getAddress();
        }

        aggregate.addAll(Arrays.asList(agents));

        createAction();
    }

    public void createAction() {
        unicasts.add(UnicastSelector.create(addresses[0]));
        unicasts.add(UnicastSelector.create(addresses[1]));
        unicasts.add(UnicastSelector.create(addresses[2]));

        // Define sth like old BroadUnusedSelector
        broadcast = Selectors.allAddressesMatching(new AddressPredicate<AgentAddress>() {

            @Override
            public boolean apply(@Nullable final AgentAddress input) {
                for(final AddressSelector<AgentAddress> selector : unicasts) {
                    if(selector.selects(input)) {
                        return false;
                    }
                }
                return true;
            }
        });

        context = new TracingActionContext();
        final SingleAction sa1 = new SingleAction(unicasts.get(0), context, "c1Action");
        final SingleAction sa2 = new SingleAction(unicasts.get(1), context, "c2Action");
        final SingleAction sa3 = new SingleAction(unicasts.get(2), context, "c3Action");
        final SingleAction sa4 = new SingleAction(broadcast, context, "c4Action");

        action = new ComplexAction();
        action.addChild(sa1);
        action.addChild(sa2);
        action.addChild(sa3);
        action.addChild(sa4);
    }

    @Test
    public void shouldExecuteAllInitializationPhases() {
        // when
        actionService.initializeAction(action);

        // then
        assertThat(context.trace, is("C1IC2IC3IC4I"));
    }

    /**
     * In this test actions 1-3 should be performed once on separate agents and the action 4 should be performed on
     * all remaining agents.
     */
    @Test
    public void shouldPerformAction() {
        // given
        actionService.initializeAction(action);

        // when
        actionService.executeAction(action);

        // then
        assertThat(context.trace, is("C1IC2IC3IC4I1234444444"));
    }

}
