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
 * Created: 2013-08-18
 * $Id$
 */

package org.jage.workplace.manager;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.jage.communication.message.BaseServiceMessage;
import org.jage.communication.message.ServiceHeaderWithType;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public class WorkplaceManagerMessage extends BaseServiceMessage<Serializable> {

	private static final long serialVersionUID = 1L;

	public WorkplaceManagerMessage(final ServiceHeaderWithType<MessageType> header,
			@Nullable final Serializable payload) {
		super(header, payload);
	}

	@Override @Nonnull public ServiceHeaderWithType<MessageType> getHeader() {
		return (ServiceHeaderWithType<MessageType>)super.getHeader();
	}

	@Nonnull public MessageType getType() {
		return getHeader().getType();
	}

	@Nonnull public static WorkplaceManagerMessage create(@Nonnull final MessageType type,
			@Nullable final Serializable payload) {
		return new WorkplaceManagerMessage(ServiceHeaderWithType.create(checkNotNull(type)), payload);
	}

	@Nonnull public static WorkplaceManagerMessage create(@Nonnull final MessageType type) {
		return new WorkplaceManagerMessage(ServiceHeaderWithType.create(type), null);
	}


	/**
	 * Constructs a new message with the given header and the payload.
	 *
	 * @param header
	 * 		A header that contains metadata for this message.
	 * @param payload
	 * 		A payload to transport.
	 *
	 * @return a new message.
	 */
	@Nonnull public static WorkplaceManagerMessage create(@Nonnull final ServiceHeaderWithType<MessageType> header,
			@Nullable final Serializable payload) {
		return new WorkplaceManagerMessage(header, payload);
	}

	/**
	 * Constructs a new message with the given header and with no payload.
	 *
	 * @param header
	 * 		A header that contains metadata for this message.
	 *
	 * @return a new message.
	 */
	@Nonnull public static WorkplaceManagerMessage create(@Nonnull final ServiceHeaderWithType<MessageType> header) {
		return create(header, null);
	}

	/**
	 * A list of available commands (message types).
	 *
	 * @author AGH AgE Team
	 */
	public static enum MessageType {
		AGENT_MESSAGE,
		MIGRATE_AGENT,
	}
}

