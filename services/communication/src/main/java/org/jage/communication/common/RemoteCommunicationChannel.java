package org.jage.communication.common;


import org.jage.address.node.NodeAddress;
import org.jage.communication.message.ServiceMessage;

import java.io.Serializable;


public interface RemoteCommunicationChannel<T extends ServiceMessage> {

    void sendMessageToAll(final T message);

    void sendMessageToNode(final T message, NodeAddress nodeAddress);

    void subscribeChannel(RemoteMessageSubscriber<T> subscriber);

    void unsubscribeChannel(RemoteMessageSubscriber<T> subscriber);

    Long nextConversationId();
}
