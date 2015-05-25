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
 * Created: 2011-05-24
 * $Id$
 */

package org.jage.agent;

import java.io.Serializable;
import java.util.Queue;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.jage.address.Address;
import org.jage.address.agent.AgentAddress;
import org.jage.communication.message.DefaultMessage;
import org.jage.communication.message.Header;
import org.jage.communication.message.Message;

/**
 * Tests for the {@link AbstractAgent} class.
 *
 * @author AGH AgE Team
 */
public class AbstractAgentTest {

	@SuppressWarnings("serial")
    private final AbstractAgent abstractAgent = new AbstractAgent(mock(AgentAddress.class)) {
		@Override
		public void setAgentEnvironment(final IAgentEnvironment agentEnvironment) {
		}

		@Override
		protected IAgentEnvironment getAgentEnvironment() {
			return null;
		}
	};

	@Test
	public void receiveMessageTest() {
		// given
		final String payload = "payload";
		final DefaultMessage<AgentAddress, String> message1 = createMessage(payload);
		final DefaultMessage<AgentAddress, String> message2 = createMessage(payload);
		final DefaultMessage<AgentAddress, String> message3 = createMessage(payload);

		// when
		abstractAgent.deliverMessage(message1);
		abstractAgent.deliverMessage(message2);
		abstractAgent.deliverMessage(message3);

		// then
		final Queue<Message<AgentAddress, ?>> messages = abstractAgent.getMessages();
		assertEquals(message1, messages.poll());
		assertEquals(message2, messages.poll());
		assertEquals(message3, messages.poll());
		assertNull(messages.poll());
	}

	@SuppressWarnings("unchecked")
	private <A extends Address, P extends Serializable> DefaultMessage<A, P> createMessage(final P payload) {
		return new DefaultMessage<A, P>(mock(Header.class), payload);
	}
}
