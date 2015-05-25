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
 * Created: 2010-06-08
 * $Id$
 */

package org.jage.platform.argument;

import org.jage.exception.AgeException;

/**
 * Exception thrown when runtime arguments are invalid.
 *
 * @author AGH AgE Team
 */
public class InvalidRuntimeArgumentsException extends AgeException {

	private static final long serialVersionUID = 2L;

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message
	 *            the detail message.
	 *
	 * @see Exception#Exception(String)
	 */
	public InvalidRuntimeArgumentsException(final String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param cause
	 *            the cause.
	 *
	 * @see Exception#Exception(String)
	 */
	public InvalidRuntimeArgumentsException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of
	 * <tt>(cause==null ? null : cause.toString())</tt>.
	 *
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause.
	 *
	 * @see Exception#Exception(String, Throwable)
	 */
	public InvalidRuntimeArgumentsException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
