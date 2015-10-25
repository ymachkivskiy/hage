package org.jage.communication.api;


import lombok.extern.slf4j.Slf4j;
import org.jage.address.node.NodeAddress;
import org.jage.communication.message.ServiceMessage;
import org.jage.communication.message.consume.ConversationMessageConsumer;
import org.jage.communication.message.consume.MessageConsumer;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@Slf4j
public abstract class BaseRemoteChanel<MessageT extends ServiceMessage>
        implements IStatefulComponent, RemoteMessageSubscriber<MessageT> {

    @Autowired
    private RemoteCommunicationManager remoteCommunicationManager;

    private final String serviceName;
    private List<MessageConsumer<MessageT>> messageConsumers = new LinkedList<>();

    private RemoteChannel<MessageT> remoteChanel;

    protected BaseRemoteChanel(String serviceName) {
        this.serviceName = serviceName;
    }

    protected final void registerMessageConsumer(MessageConsumer<MessageT> remoteMessageConsumer) {
        log.debug("Registered remote message consumer {}", remoteMessageConsumer);
        messageConsumers.add(remoteMessageConsumer);
    }

    protected NodeAddress getLocalAddress() {
        return remoteCommunicationManager.getLocalNodeAddress();
    }

    protected Set<NodeAddress> getRemoteNodeAddresses() {
        return remoteCommunicationManager.getRemoteNodeAddresses();
    }

    protected final void sendMessageToAll(MessageT message) {
        remoteChanel.sendMessageToAll(message);
    }

    protected final void sendMessageToNode(MessageT message, NodeAddress nodeAddress) {
        if (nodeAddress.equals(getLocalAddress())) {
            onRemoteMessage(message);
        } else {
            remoteChanel.sendMessageToNode(message, nodeAddress);
        }
    }

    @Override
    public final void init() throws ComponentException {
        remoteChanel = remoteCommunicationManager.getCommunicationChannelForService(serviceName);
        remoteChanel.subscribeChannel(this);
        postInit();
    }

    @Override
    public final boolean finish() throws ComponentException {
        remoteChanel.unsubscribeChannel(this);
        return true;
    }

    protected void postInit() {
    }

    @Override
    public final void onRemoteMessage(MessageT message) {
        log.debug("Received message: {}", message);

        messageConsumers.forEach(consumer -> consumer.consumeMessage(message));
    }
}
