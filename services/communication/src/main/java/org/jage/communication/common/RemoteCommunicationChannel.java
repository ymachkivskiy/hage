package org.jage.communication.common;


import org.jage.address.node.NodeAddress;

import java.io.Serializable;


public interface RemoteCommunicationChannel<T extends Serializable> {

    void sendMessageToAll(final T message);

    void setMessageToNode(final T message, NodeAddress nodeAddress);

    void subscribeChannel(RemoteMessageSubscriber<T> subscriber);

    void unsubscribeChannel(RemoteMessageSubscriber<T> subscriber);
}
