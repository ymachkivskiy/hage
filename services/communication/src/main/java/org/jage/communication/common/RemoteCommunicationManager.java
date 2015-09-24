package org.jage.communication.common;


import com.hazelcast.core.IMap;

import java.io.Serializable;


public interface RemoteCommunicationManager {

    <T extends Serializable> RemoteCommunicationChannel<T> getCommunicationChannelForService(String serviceName);

    <K, V> IMap<K, V> getDistributedMap(String mapName);
}
