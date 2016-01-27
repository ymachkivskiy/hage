package org.hage.platform.util.communication.api;


import org.hage.platform.util.communication.address.node.NodeAddress;
import org.hage.platform.util.communication.message.service.ServiceMessage;


public interface RawRemoteChannel<T extends ServiceMessage> {

    void sendMessageToAll(final T message);

    void sendMessageToNode(final T message, NodeAddress nodeAddress);

    void subscribeChannel(RemoteMessageSubscriber<T> subscriber);

    void unsubscribeChannel(RemoteMessageSubscriber<T> subscriber);

    Long nextConversationId();
}
