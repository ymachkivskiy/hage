package org.jage.communication.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jage.address.node.NodeAddress;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;


@Slf4j
public abstract class AbstractRemoteChanel<MessageT extends Serializable>
        implements IStatefulComponent, RemoteMessageSubscriber<MessageT> {

    @Autowired
    private RemoteCommunicationManager remoteCommunicationManager;

    private final String serviceName;
    private List<MessageFilterConsumer<MessageT>> messageConsumers = new ArrayList<>();

    protected RemoteCommunicationChannel<MessageT> remoteChanel;

    protected AbstractRemoteChanel(String serviceName) {
        this.serviceName = serviceName;
    }

    protected final void registerConsumerHandler(Predicate<MessageT> matchingPredicate, Consumer<MessageT> messageConsumer) {
        log.debug("Registered consumer handler {} for predicate {}", messageConsumer, matchingPredicate);
        messageConsumers.add(new MessageFilterConsumer<>(matchingPredicate, messageConsumer));
    }

    protected NodeAddress getLocalAddress() {
        return remoteCommunicationManager.getLocalNodeAddress();
    }

    protected Set<NodeAddress> getRemoteNodeAddresses() {
        return remoteCommunicationManager.getRemoteNodeAddresses();
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

        messageConsumers.stream()
                .filter(pF -> pF.getPredicate().test(message))
                .forEach(pF -> pF.getConsumer().accept(message));

    }


    @Data
    @AllArgsConstructor
    private static class MessageFilterConsumer<MessageT> {

        private Predicate<MessageT> predicate;
        private Consumer<MessageT> consumer;
    }
}
