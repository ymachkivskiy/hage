package org.hage.platform.util.connection.remote.api;

import org.hage.platform.util.connection.remote.endpoint.ConnectionDescriptorProvider;

import java.io.Serializable;

public interface RemoteMessageSubscriber<M extends Serializable, R extends Serializable> extends ConnectionDescriptorProvider {
    R consumeAndRespondToRemoteMessage(RemoteMessage<M> remoteMessage);

    void consumeRemoteMessage(RemoteMessage<M> remoteMessage);
}
