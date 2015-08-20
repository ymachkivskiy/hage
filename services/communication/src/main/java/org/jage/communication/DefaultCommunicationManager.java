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
 * Created: 2013-08-11
 * $Id$
 */

package org.jage.communication;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.node.HazelcastNodeAddress;
import org.jage.address.node.NodeAddress;
import org.jage.address.node.NodeAddressSupplier;
import org.jage.platform.component.IStatefulComponent;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Communication manager is a service that provides means of communication between separate nodes.
 *
 * <p>This version is based on Hazelcast and offers:
 * <ul>
 * <li>communication channels for services,</li>
 * <li>distributed map for the workplace manager.</li>
 * </ul>
 *
 * <p>It also acts as NodeAddressSupplier.
 */
@ThreadSafe
public class DefaultCommunicationManager implements IStatefulComponent, NodeAddressSupplier, CommunicationManager {

	/**
	 * A name of the Hazelcast instance used by this manager.
	 */
	public static final String HAZELCAST_INSTANCE_NAME = "AgE";

	private static final Logger log = LoggerFactory.getLogger(DefaultCommunicationManager.class);

	@Nonnull private static final HazelcastInstance hazelcastInstance;

	@Nonnull private static final HazelcastNodeAddress nodeAddress;

	static {
		final Config config = new Config();
		config.setInstanceName(HAZELCAST_INSTANCE_NAME);
		config.setProperty("hazelcast.logging.type", "slf4j");

		hazelcastInstance = Hazelcast.newHazelcastInstance(config);
		nodeAddress = new HazelcastNodeAddress(getLocalMemberUuid(hazelcastInstance));
		log.debug("Node UUID: {}.", getLocalMemberUuid(hazelcastInstance));
	}

	@Override public void init() { /*Empty*/ }

	@Override public boolean finish() {
		hazelcastInstance.shutdown();
		try {
			Thread.sleep(1000);
		} catch (final InterruptedException ignored) {
			Thread.currentThread().interrupt();
		}
		return true;
	}

	@Nonnull @Override public <T> CommunicationChannel<T> getCommunicationChannelForService(final String serviceName) {
		final ITopic<T> topic = hazelcastInstance.getTopic("service-" + serviceName);
		return new CommunicationChannel<>(topic);
	}

	@Nonnull @Override public <K, V> IMap<K, V> getDistributedMap(final String mapName) {
		return hazelcastInstance.getMap(mapName);
	}

	@Nonnull @Override public NodeAddress get() {
		return nodeAddress;
	}

	@Override public String toString() {
		return toStringHelper(this).add("H-UUID", getLocalMemberUuid(hazelcastInstance)).toString();
	}

	@Nonnull private static String getLocalMemberUuid(HazelcastInstance hazelcastInstance) {
		return hazelcastInstance.getCluster().getLocalMember().getUuid();
	}
}
