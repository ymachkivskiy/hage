package org.jage.communication.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


@Slf4j
public abstract class AbstractRemoteServiceChanelEndpoint<MessageT extends Serializable>
        implements IStatefulComponent, RemoteMessageSubscriber<MessageT> {

    private final String serviceName;
    @Autowired
    private RemoteCommunicationManager remoteCommunicationManager;
    private RemoteCommunicationChannel<MessageT> remoteChanel;
    private List<MessageFilterConsumer<MessageT>> messageConsumers = new ArrayList<>();

    protected AbstractRemoteServiceChanelEndpoint(String serviceName) {
        this.serviceName = serviceName;
    }

    protected final void registerConsumerHandler(Predicate<MessageT> matchingPredicate, Consumer<MessageT> messageConsumer) {
        log.debug("Registered consumer handler {} for predicate {}", messageConsumer, matchingPredicate);
        messageConsumers.add(new MessageFilterConsumer<>(matchingPredicate, messageConsumer));
    }

    protected final void sendMessage(MessageT messageT) {
        remoteChanel.publish(messageT);
    }

    @Override
    public final void init() throws ComponentException {
        remoteChanel = remoteCommunicationManager.getCommunicationChannelForService(serviceName);
        remoteChanel.subscribe(this);
        postInit();
    }

    @Override
    public final boolean finish() throws ComponentException {
        remoteChanel.unsubscribe(this);
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
