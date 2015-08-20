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
 * Created: 2014-01-01
 * $Id$
 */

package org.jage.communication.message;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base implementation for the {@link ServiceHeader} which defines a type of a message.
 *
 * @param <T>
 * 		enum that lists all types of a message.
 */
@Immutable
public class ServiceHeaderWithType<T extends Enum<T>> implements ServiceHeader {

	private final T type;

	private ServiceHeaderWithType(final T type) {
		this.type = checkNotNull(type);
	}

	public static <V extends Enum<V>> ServiceHeaderWithType<V> create(final V type) {
		return new ServiceHeaderWithType<>(checkNotNull(type));
	}

	/**
	 * Returns the type that this header represents.
	 *
	 * @return a type that this header represents.
	 */
	@Nonnull public T getType() {
		return type;
	}

	@Override
	public String toString() {
		return toStringHelper(this).addValue(type).toString();
	}
}
