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
 * Created: 2008-10-07
 * $Id$
 */

package org.jage.exception;

/**
 * A basic class for all AgE exceptions.
 *
 * @author AGH AgE Team
 */
public class AgeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/***
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message
	 *            the detail message.
	 *
	 * @see Exception#Exception(String)
	 */
	public AgeException(final String message) {
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
	public AgeException(final Throwable cause) {
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
	public AgeException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
