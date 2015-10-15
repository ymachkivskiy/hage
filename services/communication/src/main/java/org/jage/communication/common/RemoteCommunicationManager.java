package org.jage.communication.common;


import com.hazelcast.core.IMap;
import org.jage.address.node.NodeAddress;

import java.io.Serializable;
import java.util.Set;


public interface RemoteCommunicationManager {

    <T extends Serializable> RemoteCommunicationChannel<T> getCommunicationChannelForService(String serviceName);

    <K, V> IMap<K, V> getDistributedMap(String mapName);

    Set<NodeAddress> getRemoteNodeAddresses();

    NodeAddress getLocalNodeAddress();
}
