package org.hage.platform.util.connection.remote.api;

import org.hage.platform.util.connection.NodeAddress;

import java.io.Serializable;
import java.util.Collection;

public interface RemoteMessageSender<M extends Serializable> {
    void sendToAll(M message);

    void sendTo(M message, Collection<NodeAddress> addresses);

    void sendTo(M message, NodeAddress address);
}
