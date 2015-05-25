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
 * Created: 2012-03-03
 * $Id$
 */

package org.jage.address.node;

/**
 * This is a representation of an unspecified node address.
 *
 * <p>
 * The "unspecified node" is any node in the distributed environment.
 *
 * <p>
 * Instances of this implementation have no identifier and cannot be compared to any other instance. They will throw
 * exception if asked for an identifier or comparison.
 *
 * @author AGH AgE Team
 */
public final class UnspecifiedNodeAddress implements NodeAddress {
	private static final long serialVersionUID = -2328801738164300641L;

	@Override
	public int compareTo(final NodeAddress arg0) {
		throw new UnsupportedOperationException("An unspecified node is not comparable");
	}

	@Override
	public String getIdentifier() {
		throw new UnsupportedOperationException("An unspecified node have no identifier");
	}
}
