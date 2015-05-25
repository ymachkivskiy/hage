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
 * Created: 2011-03-15
 * $Id$
 */

package org.jage.examples.messages;

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.action.AgentActions;
import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.address.selector.Selectors;
import org.jage.agent.AgentException;
import org.jage.agent.SimpleAgent;
import org.jage.communication.message.DefaultHeader;
import org.jage.communication.message.Message;
import org.jage.communication.message.DefaultMessage;
import org.jage.exception.AgeException;
import org.jage.query.AgentEnvironmentQuery;

import com.google.common.base.Predicate;

import static com.google.common.collect.Iterables.consumingIterable;

/**
 * This agent sends and receives messages.
 *
 * @author AGH AgE Team
 */
public class MessagesSimpleAgent extends SimpleAgent {

	private static final long serialVersionUID = 2L;

	private final Logger log = LoggerFactory.getLogger(MessagesSimpleAgent.class);

	private AgentAddress receiverAddress = null;

	/**
	 * DefaultMessage receiver
	 */
	private String receiver = null;

	/**
	 * Content of the message
	 */
	private String messageToSend = null;

	private int sent = 0;
	private int received = 0;

	public MessagesSimpleAgent(final AgentAddress address) {
		super(address);
	}

	@Inject
	public MessagesSimpleAgent(final AgentAddressSupplier supplier) {
		super(supplier);
	}

	public void setReceiver(final @Nonnull String receiver) {
		this.receiver = receiver;
	}

	public void setMessageToSend(final @Nonnull String messageToSend) {
		this.messageToSend = messageToSend;
	}

	@Override
	public void step() {
		if (receiverAddress == null) {
			try {
				final AgentEnvironmentQuery<SimpleAgent, SimpleAgent> query = new AgentEnvironmentQuery<>();
				final Collection<SimpleAgent> answer = queryEnvironment(query);

				for (final SimpleAgent entry : answer) {
					final AgentAddress agentAddress = (AgentAddress)entry.getProperty("address").getValue();
					if (agentAddress.getFriendlyName().startsWith(receiver)) {
						receiverAddress = agentAddress;
					}
				}
			} catch (final AgeException e) {
				log.error("Agent exception", e);
			}
		}

		send();

		for (final Message<AgentAddress, ?> message : consumingIterable(getMessages())) {
			read(message);
		}
	}

	@Override
	public boolean finish() {
		log.info("Finishing {}. Sent {}. Received {}.", getAddress().getFriendlyName(), sent, received);
		return true;
	}

	private void send() {
		if (receiverAddress != null && messageToSend != null) {
			final DefaultHeader<AgentAddress> header =
					new DefaultHeader<>(getAddress(), Selectors.allAddressesMatching(new AgentAddressPredicate()));
			final DefaultMessage<AgentAddress, String> textMessage = new DefaultMessage<>(header, messageToSend);
			try {
				doAction(AgentActions.sendMessage(textMessage));
				sent++;
			} catch (final AgentException e) {
				log.error("Can't send a message.", e);
			}
		}
	}

	private void read(final Message<AgentAddress, ?> message) {
		received++;
		log.info("Agent {} received message from {}: {}", getAddress().getFriendlyName(),
				message.getHeader().getSenderAddress().getFriendlyName(), message.getPayload());
	}

	private class AgentAddressPredicate implements Predicate<AgentAddress>, Serializable {
		@Override public boolean apply(@Nullable final AgentAddress input) {
			return input.getFriendlyName().startsWith(receiver);
		}
	}
}
