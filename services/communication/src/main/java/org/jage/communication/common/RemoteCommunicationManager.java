package org.jage.communication.common;


import com.hazelcast.core.IMap;
import org.jage.address.node.NodeAddress;
import org.jage.communication.message.ServiceMessage;

import java.io.Serializable;
import java.util.Set;


public interface RemoteCommunicationManager {

    <T extends ServiceMessage> RemoteCommunicationChannel<T> getCommunicationChannelForService(String serviceName);

    <K, V> IMap<K, V> getDistributedMap(String mapName);

    int getClusterSize();

    Set<NodeAddress> getRemoteNodeAddresses();

    NodeAddress getLocalNodeAddress();
}
