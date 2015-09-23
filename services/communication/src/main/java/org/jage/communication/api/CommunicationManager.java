package org.jage.communication.api;


import com.hazelcast.core.IMap;

import javax.annotation.Nonnull;


public interface CommunicationManager {

    @Nonnull
    <T> CommunicationChannel<T> getCommunicationChannelForService(String serviceName);

    @Nonnull
    <K, V> IMap<K, V> getDistributedMap(String mapName);
}
