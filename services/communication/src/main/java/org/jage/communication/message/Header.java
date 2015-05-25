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
 * Created: 2011-07-27
 * $Id$
 */

package org.jage.communication.message;

import java.io.Serializable;

import javax.annotation.Nonnull;

import org.jage.address.Address;
import org.jage.address.selector.AddressSelector;

/**
 * DefaultMessage header which contains sender and receiver addresses. The header is parametrised by the type of an address.
 * 
 * @param <A>
 *            A type of sender and receiver address.
 * 
 * @author AGH AgE Team
 */
public interface Header<A extends Address> extends Serializable {

	/**
	 * Returns an address of the sender.
	 * 
	 * @return The address of the sender.
	 */
	@Nonnull A getSenderAddress();

	/**
	 * Returns a selector that selects all receivers.
	 * 
	 * @return A selector for receivers.
	 */
	@Nonnull AddressSelector<A> getReceiverSelector();
}
