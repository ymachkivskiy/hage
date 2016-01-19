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


import org.hage.address.agent.AgentAddress;
import org.hage.address.selector.UnicastSelector;
import org.hage.communication.message.Message;
import org.hage.platform.component.action.Action;
import org.hage.platform.component.action.SingleAction;
import org.hage.platform.component.action.context.GetAgentActionContext;
import org.hage.platform.component.action.context.IActionWithAgentReferenceContext;
import org.hage.platform.component.action.context.RemoveAgentActionContext;
import org.hage.platform.component.action.context.SendMessageActionContext;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * Tests for the {@link AggregateActionService} class: other tests.
 *
 * @author AGH AgE Team
 */
public class AggregateActionServiceTest {

    private final AggregateActionService actionService = new AggregateActionService();

    @SuppressWarnings("unchecked")
    @Test
    public void testThatAggregateConformsToDefaultActionPriorites() {
        // Note this test can be written much better when the aggregate will be more open for testing.

        // given
        final SingleAction action1 = new SingleAction(mock(UnicastSelector.class), new GetAgentActionContext(
                mock(IActionWithAgentReferenceContext.class)));
        final SingleAction action2 = new SingleAction(mock(UnicastSelector.class), new SendMessageActionContext(
                mock(Message.class)));
        final SingleAction action3 = new SingleAction(mock(UnicastSelector.class), new RemoveAgentActionContext(
                mock(AgentAddress.class)));

        final AggregateActionService aggregateSpy = spy(actionService);
        final ArgumentCaptor<Action> argument = ArgumentCaptor.forClass(Action.class);

        // add them in reverse order
        aggregateSpy.doAction(action3);
        aggregateSpy.doAction(action2);
        aggregateSpy.doAction(action1);

        willDoNothing().given(aggregateSpy).processAction(any(Action.class));

        // when
        aggregateSpy.processActions();

        // then
        verify(aggregateSpy, times(3)).processAction(argument.capture());

        assertThat(argument.getAllValues().get(0), is(equalTo((Action) action1)));
        assertThat(argument.getAllValues().get(1), is(equalTo((Action) action2)));
        assertThat(argument.getAllValues().get(2), is(equalTo((Action) action3)));
    }

}
