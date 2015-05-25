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
 * Created: 2013-09-14
 * $Id$
 */

package org.jage.services.core;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.jage.bus.AgeEvent;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Event generated by the {@link CoreComponent}.
 */
@Immutable
public class CoreComponentEvent implements AgeEvent {

	@Nonnull private final Type type;

	public enum Type {
		/**
		 * The event sent when the core component has been successfully configured.
		 */
		CONFIGURED,
		/**
		 * The event sent at the beginning of the {@link CoreComponent#start()} method. It notifies that the core component
		 * is about to start.
		 */
		STARTING,
		/**
		 * The event sent at the end of the {@link CoreComponent#stop()} method. It notifies that there is no more workplaces
		 * running.
		 */
		STOPPED
	}

	public CoreComponentEvent(@Nonnull final Type type) {
		this.type = checkNotNull(type);
	}

	@Nonnull public Type getType() {
		return type;
	}

	@Override public String toString() {
		return toStringHelper(this).addValue(type).toString();
	}
}
