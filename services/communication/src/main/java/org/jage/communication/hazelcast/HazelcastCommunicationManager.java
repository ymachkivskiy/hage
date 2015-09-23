package org.jage.communication.hazelcast;


import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import org.jage.address.node.HazelcastNodeAddress;
import org.jage.address.node.NodeAddress;
import org.jage.address.node.NodeAddressSupplier;
import org.jage.communication.api.CommunicationManager;
import org.jage.platform.component.IStatefulComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import static com.google.common.base.Objects.toStringHelper;


@ThreadSafe
class HazelcastCommunicationManager implements IStatefulComponent, NodeAddressSupplier, CommunicationManager {

    public static final String HAZELCAST_INSTANCE_NAME = "HageInstance";

    private static final Logger log = LoggerFactory.getLogger(HazelcastCommunicationManager.class);

    @Nonnull
    private static final HazelcastInstance hazelcastInstance;

    @Nonnull
    private static final HazelcastNodeAddress nodeAddress;

    static {
        final Config config = new Config();
        config.setInstanceName(HAZELCAST_INSTANCE_NAME);
        config.setProperty("hazelcast.logging.type", "slf4j");

        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        nodeAddress = new HazelcastNodeAddress(getLocalMemberUuid(hazelcastInstance));
        log.debug("Node UUID: {}.", getLocalMemberUuid(hazelcastInstance));
    }

    @Override
    public void init() { /*Empty*/ }

    @Override
    public boolean finish() {
        hazelcastInstance.shutdown();
        try {
            Thread.sleep(1000);
        } catch(final InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        return true;
    }

    @Nonnull
    @Override
    public <T> HazelcastCommunicationChannel<T> getCommunicationChannelForService(final String serviceName) {
        final ITopic<T> topic = hazelcastInstance.getTopic("service-" + serviceName);
        return new HazelcastCommunicationChannel<>(topic);
    }

    @Nonnull
    @Override
    public <K, V> IMap<K, V> getDistributedMap(final String mapName) {
        return hazelcastInstance.getMap(mapName);
    }

    @Nonnull
    @Override
    public NodeAddress get() {
        return nodeAddress;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("H-UUID", getLocalMemberUuid(hazelcastInstance)).toString();
    }

    @Nonnull
    private static String getLocalMemberUuid(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getCluster().getLocalMember().getUuid();
    }
}
