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
 * Created: 2012-08-21
 * $Id$
 */

package org.jage.lifecycle;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.jage.annotation.ReturnValuesAreNonnullByDefault;
import org.jage.communication.message.BaseServiceMessage;
import org.jage.communication.message.ServiceHeaderWithType;
import org.jage.services.core.LifecycleManager;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A message type used by {@link LifecycleManager}.
 * 
 * @author AGH AgE Team
 */
@ReturnValuesAreNonnullByDefault
@Immutable
public final class LifecycleMessage extends BaseServiceMessage<Serializable> {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new LifecycleMessage.
	 * 
	 * @param header
	 *            A header that contains metadata for this message.
	 * @param payload
	 *            A payload to transport.
	 */
	public LifecycleMessage(final ServiceHeaderWithType<LifecycleCommand> header, @Nullable final Serializable payload) {
		super(header, payload);
	}

	@Override @Nonnull public ServiceHeaderWithType<LifecycleCommand> getHeader() {
		return (ServiceHeaderWithType<LifecycleCommand>)super.getHeader();
	}

	/**
	 * Returns the command that this message represents.
	 * 
	 * @return a command.
	 */
	public LifecycleCommand getCommand() {
		return getHeader().getType();
	}

	@Nonnull public static LifecycleMessage create(@Nonnull final LifecycleCommand type,
			@Nullable final Serializable payload) {
		return new LifecycleMessage(ServiceHeaderWithType.create(checkNotNull(type)), payload);
	}

	@Nonnull public static LifecycleMessage create(@Nonnull final LifecycleCommand type) {
		return new LifecycleMessage(ServiceHeaderWithType.create(type), null);
	}

	/**
	 * A list of available commands (message types).
	 *
	 * @author AGH AgE Team
	 */
	public static enum LifecycleCommand {
		/**
		 * Start the computation.
		 */
		START,
		/**
		 * Pause the computation.
		 */
		PAUSE,
		/**
		 * Stop the computation.
		 */
		STOP,
		/**
		 * Node failure.
		 */
		FAIL,
		/**
		 * Notifications.
		 */
		NOTIFY,
		/**
		 * Shutdown the environment.
		 */
		EXIT,
	}
}
