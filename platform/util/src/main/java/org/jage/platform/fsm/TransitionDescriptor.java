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

package org.jage.platform.fsm;

import static java.lang.String.format;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A descriptor of the transition in the transition table.
 * <p>
 * Package-protected class.
 * 
 * @param <S>
 *            a states type.
 * @param <E>
 *            an events type
 * 
 * @author AGH AgE Team
 */
class TransitionDescriptor<S extends Enum<S>, E extends Enum<E>> {

	private static final Runnable EMPTY_ACTION = new Runnable() {
		@Override
		public void run() {
			// Empty
		}
	};

	private final S initial;

	private final E event;

	@Nonnull private final Runnable action;

	private final S target;

	@SuppressWarnings("unchecked")
	public static final TransitionDescriptor<?, ?> NULL = new TransitionDescriptor(null, null, null, null) {
		@Override
		boolean isNull() {
			return true;
		}

		@Override
		public String toString() {
			return format("(null)");
		}
	};

	public TransitionDescriptor(@Nullable final S initial, @Nullable final E event, @Nullable final S target,
	        @Nullable final Runnable action) {
		this.initial = initial;
		this.event = event;
		this.action = action != null ? action : EMPTY_ACTION;
		this.target = target;
	}

	@Nonnull final Runnable getAction() {
		return action;
	}

	@Nullable final S getTarget() {
		return target;
	}

	@Nullable final S getInitial() {
		return initial;
	}

	@Nullable final E getEvent() {
		return event;
	}

	@SuppressWarnings("static-method")
    boolean isNull() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Nonnull public static <V extends Enum<V>, Z extends Enum<Z>> TransitionDescriptor<V, Z> getNull() {
		return (TransitionDescriptor<V, Z>)NULL;
	}

	@Override
	public String toString() {
		return format("(%s-[%s]->%s)", initial, event, target);
	}
}
