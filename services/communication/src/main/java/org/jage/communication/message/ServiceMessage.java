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
 * Created: 2013-07-27
 * $Id$
 */

package org.jage.communication.message;

import java.io.Serializable;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Base interface for messages that are used by services.
 * It is comprised of a header ({@link org.jage.communication.message.ServiceHeader}) and content
 * which can be any Serializable object.
 *
 * @param <T>
 *            A type of the payload transported within this message (must be Serializable).
 *
 * @author AGH AgE Team
 */
public interface ServiceMessage<T extends Serializable> extends Serializable {

	/**
	 * Returns a message header.
	 * 
	 * @return A message header, never null.
	 */
	@Nonnull ServiceHeader getHeader();

	/**
	 * Returns message contents.
	 * 
	 * @return A message contents.
	 */
	@CheckForNull T getPayload();
}
