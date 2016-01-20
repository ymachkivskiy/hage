package org.hage.communication.api;


import org.hage.address.node.NodeAddress;
import org.hage.communication.message.service.ServiceMessage;


public interface RawRemoteChannel<T extends ServiceMessage> {

    void sendMessageToAll(final T message);

    void sendMessageToNode(final T message, NodeAddress nodeAddress);

    void subscribeChannel(RemoteMessageSubscriber<T> subscriber);

    void unsubscribeChannel(RemoteMessageSubscriber<T> subscriber);

    Long nextConversationId();
}
