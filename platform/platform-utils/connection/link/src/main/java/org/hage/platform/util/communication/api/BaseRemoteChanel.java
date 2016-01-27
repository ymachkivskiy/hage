package org.hage.platform.util.communication.api;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.IStatefulComponent;
import org.hage.platform.component.exception.ComponentException;
import org.hage.platform.util.communication.address.node.NodeAddress;
import org.hage.platform.util.communication.message.service.ServiceMessage;
import org.hage.platform.util.communication.message.service.consume.MessageConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@Slf4j
public abstract class BaseRemoteChanel<MessageT extends ServiceMessage>
        implements IStatefulComponent, RemoteMessageSubscriber<MessageT> {

    @Autowired
    private RemoteCommunicationManager remoteCommunicationManager;
    private RawRemoteChannel<MessageT> remoteChanel;

    private final String serviceName;
    private List<MessageConsumer<MessageT>> messageConsumers = new LinkedList<>();

    protected BaseRemoteChanel() {
        this.serviceName = getClass().getSimpleName();
    }

    protected final void registerMessageConsumer(MessageConsumer<MessageT> remoteMessageConsumer) {
        log.debug("Registered remote message consumer {}", remoteMessageConsumer);
        messageConsumers.add(remoteMessageConsumer);
    }

    protected final NodeAddress getLocalAddress() {
        return remoteCommunicationManager.getLocalNodeAddress();
    }

    protected final Set<NodeAddress> getRemoteNodeAddresses() {
        return remoteCommunicationManager.getRemoteNodeAddresses();
    }

    protected final Set<NodeAddress> getAllClusterAddresses() {
        return remoteCommunicationManager.getAllNodeAddresses();
    }

    protected final void send(MessageT message) {
        remoteChanel.sendMessageToAll(wrapWithSenderAddress(message));
    }

    protected final void send(MessageT message, NodeAddress nodeAddress) {
        message = wrapWithSenderAddress(message);
        if (nodeAddress.equals(getLocalAddress())) {
            onRemoteMessage(message);
        } else {
            remoteChanel.sendMessageToNode(message, nodeAddress);
        }
    }

    protected void postInit() {
    }

    public Long nextConversationId() {
        return remoteChanel.nextConversationId();
    }

    @PostConstruct
    @Override
    public final void init() throws ComponentException {
        remoteChanel = remoteCommunicationManager.getCommunicationChannelForService(serviceName);
        remoteChanel.subscribeChannel(this);
        postInit();
    }

    @PreDestroy
    @Override
    public final boolean finish() throws ComponentException {
        remoteChanel.unsubscribeChannel(this);
        return true;
    }

    @Override
    public final void onRemoteMessage(MessageT message) {
        log.debug("Received message: {}", message);

        messageConsumers.forEach(consumer -> consumer.consumeMessage(message));
    }

    private MessageT wrapWithSenderAddress(MessageT message) {
        message.getHeader().setSender(getLocalAddress());
        return message;
    }

}
