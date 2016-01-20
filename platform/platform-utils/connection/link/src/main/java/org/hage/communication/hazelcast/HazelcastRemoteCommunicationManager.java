package org.hage.communication.hazelcast;


import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import lombok.extern.slf4j.Slf4j;
import org.hage.address.node.HazelcastNodeAddress;
import org.hage.address.node.NodeAddress;
import org.hage.address.node.NodeAddressSupplier;
import org.hage.communication.api.RemoteCommunicationManager;
import org.hage.communication.message.service.ServiceMessage;
import org.hage.platform.component.IStatefulComponent;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Set;

import static com.google.common.base.Objects.toStringHelper;
import static java.util.stream.Collectors.toSet;


@ThreadSafe
@Slf4j
class HazelcastRemoteCommunicationManager
        implements IStatefulComponent, NodeAddressSupplier, RemoteCommunicationManager {

    public static final String HAZELCAST_INSTANCE_NAME = "HageInstance";
    public static final String SERVICE_PREFIX = "service-";

    private static final HazelcastInstance hazelcastInstance;
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
        } catch (final InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        return true;
    }

    @Nonnull
    @Override
    public <T extends ServiceMessage> HazelcastRawRemoteChannel<T> getCommunicationChannelForService(final String serviceName) {
        return new HazelcastRawRemoteChannel<>(hazelcastInstance, SERVICE_PREFIX + serviceName);
    }

    @Nonnull
    @Override
    public <K, V> IMap<K, V> getDistributedMap(final String mapName) {
        return hazelcastInstance.getMap(mapName);
    }

    @Override
    public Set<NodeAddress> getRemoteNodeAddresses() {
        Set<NodeAddress> remoteAddresses = getAllNodeAddresses();
        remoteAddresses.remove(getLocalNodeAddress());
        return remoteAddresses;
    }

    @Override
    public Set<NodeAddress> getAllNodeAddresses() {
        return hazelcastInstance.getCluster().getMembers()
                .stream()
                .map(member -> new HazelcastNodeAddress(member.getUuid()))
                .collect(toSet());
    }

    @Override
    public int getClusterSize() {
        return hazelcastInstance.getCluster().getMembers().size();
    }

    @Override
    public NodeAddress getLocalNodeAddress() {
        return nodeAddress;
    }

    @Nonnull
    @Override
    public NodeAddress get() {
        return getLocalNodeAddress();
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
