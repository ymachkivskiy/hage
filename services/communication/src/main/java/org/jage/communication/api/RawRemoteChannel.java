package org.jage.communication.api;


import org.jage.address.node.NodeAddress;
import org.jage.communication.message.service.ServiceMessage;


public interface RawRemoteChannel<T extends ServiceMessage> {

    void sendMessageToAll(final T message);

    void sendMessageToNode(final T message, NodeAddress nodeAddress);

    void subscribeChannel(RemoteMessageSubscriber<T> subscriber);

    void unsubscribeChannel(RemoteMessageSubscriber<T> subscriber);

    Long nextConversationId();
}
