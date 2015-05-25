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
 * Created: 2012-03-15
 * $Id$
 */

package org.jage.communication;

import javax.annotation.concurrent.Immutable;

import org.jage.exception.AgeException;

/**
 * {@code CommunicationException} is thrown whenever there is problem with communication between different nodes.
 *
 * @author AGH AgE Team
 */
@Immutable
public class CommunicationException extends AgeException {

	private static final long serialVersionUID = -2313554547438876828L;

	/**
	 * Constructs a new communication exception.
	 *
	 * @param message
	 *            the detail message.
	 * @param cause
	 *            the cause of this exception.
	 */
	public CommunicationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new communication exception.
	 *
	 * @param message
	 *            the detail message.
	 */
	public CommunicationException(final String message) {
		super(message);
	}

	/**
	 * Constructs a new communication exception with {@code null} as its detail message.
	 *
	 * @param cause
	 *            the cause of this exception.
	 */
	public CommunicationException(final Throwable cause) {
		super(cause);
	}

}
