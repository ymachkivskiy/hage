package org.hage.communication.api;


import com.hazelcast.core.IMap;
import org.hage.address.node.NodeAddress;
import org.hage.communication.message.service.ServiceMessage;

import java.util.Set;


public interface RemoteCommunicationManager {

    <T extends ServiceMessage> RawRemoteChannel<T> getCommunicationChannelForService(String serviceName);

    <K, V> IMap<K, V> getDistributedMap(String mapName);

    int getClusterSize();

    Set<NodeAddress> getRemoteNodeAddresses();

    NodeAddress getLocalNodeAddress();

    Set<NodeAddress> getAllNodeAddresses();
}
