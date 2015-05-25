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
 * Created: 2013-12-31
 * $Id$
 */

package org.jage.communication;

import javax.annotation.Nonnull;

import com.hazelcast.core.IMap;

public interface CommunicationManager {

	String SERVICE_NAME = CommunicationManager.class.getSimpleName();

	/**
	 * Returns a communication channel for a given service name.
	 *
	 * @param serviceName
	 * 		a name of the service.
	 * @param <T>
	 * 		a type of messages in the communication channel.
	 *
	 * @return a communication channel for the service.
	 */
	@Nonnull <T> CommunicationChannel<T> getCommunicationChannelForService(String serviceName);

	/**
	 * Returns a distributed map with a given name.
	 *
	 * @param mapName
	 * 		a name of the map to return.
	 * @param <K>
	 * 		a type of keys in the map.
	 * @param <V>
	 * 		a type of values in the map.
	 *
	 * @return a distributed map with the given name.
	 */
	@Nonnull <K, V> IMap<K, V> getDistributedMap(String mapName);
}
