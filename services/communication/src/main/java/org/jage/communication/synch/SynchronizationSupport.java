package org.jage.communication.synch;


import lombok.extern.slf4j.Slf4j;
import org.jage.communication.message.service.ServiceMessage;
import org.jage.communication.message.service.consume.MessageConsumer;

import java.util.concurrent.*;
import java.util.function.Supplier;


@Slf4j
public abstract class SynchronizationSupport<ResultT, RemoteMessageT extends ServiceMessage>
        implements MessageConsumer<RemoteMessageT> {

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    private final Supplier<Long> conversationIdSupplier;
    private final CompositeSynchronousCommunicationMessageConsumer<RemoteMessageT> messagesConsumers;


    public SynchronizationSupport(Supplier<Long> conversationIdSupplier) {
        this.conversationIdSupplier = conversationIdSupplier;
        this.messagesConsumers = new CompositeSynchronousCommunicationMessageConsumer<>();
    }

    public ResultT synchronousCall(RemoteMessageT remoteMessage, ConversationMessagesAggregator<ResultT, RemoteMessageT> messagesAggregator) {
        Long conversationId = conversationIdSupplier.get();
        ResponseSynch<RemoteMessageT, ResultT> responseSynch = new ResponseSynch<>(conversationId, messagesAggregator);
        messagesConsumers.registerMessageConsumer(responseSynch);

        Future<ResultT> d = executorService.submit(responseSynch);

        sendMessage(remoteMessage);

        ResultT response = getResponse(d);

        messagesConsumers.unregisterMessageConsumer(responseSynch);

        return response;
    }

    private ResultT getResponse(Future<ResultT> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error occurred during synchronization of communication", e);
        }
        return null;
    }

    protected abstract void sendMessage(RemoteMessageT message);

    @Override
    public void consumeMessage(RemoteMessageT message) {
        messagesConsumers.consumeMessage(message);
    }
}
