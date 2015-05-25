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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A runnable that wraps a {@link CallableWithParameters} instance.
 * 
 * @param <T>
 *            a type parameter from the {@code CallableWithParameters} instance.
 * 
 * @author AGH AgE Team
 */
public class CallableAdapter<T> implements Runnable {

	@Nonnull
	private final CallableWithParameters<T> callable;

	private T parameters;

	/**
	 * Creates a new adapter.
	 * 
	 * @param callable
	 *            a callable to call.
	 */
	public CallableAdapter(final CallableWithParameters<T> callable) {
		super();
		this.callable = callable;
	}

	/**
	 * Sets the parameters to call the callable with.
	 * 
	 * @param parameters
	 *            parameters to use.
	 */
	public void setParameters(@Nullable final T parameters) {
		this.parameters = parameters;
	}

	@Override
	public void run() {
		callable.call(parameters);
	}
}
