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
 * Created: 2012-07-18
 * $Id$
 */

package org.jage.address.node;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A default implementation of a node address.
 *
 * @author AGH AgE Team
 */
public class HazelcastNodeAddress extends AbstractNodeAddress {

	private static final long serialVersionUID = 1L;

	private final String uuid;

	/**
	 * Constructs a new {@code HazelcastNodeAddress} based on a UUID.
	 *
	 * @param uuid
	 *            Hazelcast UUID for the node.
	 *
	 * @throws NullPointerException
	 *             If {@code uuid} is {@code null}.
	 */
	public HazelcastNodeAddress(final String uuid) {
		this.uuid = checkNotNull(uuid);
	}

	@Override
	public String getIdentifier() {
		return uuid;
	}

	@Override
	public String toString() {
		return String.format("H[...%s]", uuid.substring(uuid.length()-4, uuid.length()));
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(uuid);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof  HazelcastNodeAddress)) {
			return false;
		}

		final HazelcastNodeAddress other = (HazelcastNodeAddress)obj;
		return Objects.equals(uuid, other.uuid);
	}
}
