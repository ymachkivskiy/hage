package org.hage.communication.synch;

import lombok.extern.slf4j.Slf4j;
import org.hage.communication.message.service.ServiceMessage;
import org.hage.communication.message.service.consume.ConversationMessageConsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.Collections.singleton;

@Slf4j
class ResponseSynch<RemoteMessageT extends ServiceMessage, ResultT> extends ConversationMessageConsumer<RemoteMessageT> implements Callable<ResultT> {

    private final ConversationMessagesAggregator<ResultT, RemoteMessageT> messagesAggregator;
    private final BlockingQueue<RemoteMessageT> messagesBlockingQueue = new LinkedBlockingQueue<>();

    public ResponseSynch(Long conversationId, ConversationMessagesAggregator<ResultT, RemoteMessageT> messagesAggregator) {
        super(singleton(conversationId));
        this.messagesAggregator = messagesAggregator;
    }

    @Override
    public ResultT call() throws Exception {
        //user by inner synchronizator thread
        log.debug("Started aggregating messages");

        while (!messagesAggregator.hasAllMessages()) {
            log.debug("Not all messages has been received yet. Waiting for new message...");

            RemoteMessageT message = messagesBlockingQueue.take();

            log.debug("Got message {}", message);

            messagesAggregator.appendMessage(message);
        }

        log.debug("Finished aggregating messages");

        return messagesAggregator.getAggregatedResult().get();
    }

    @Override
    protected void consumeMatchingMessage(RemoteMessageT message) {
        // used by hazelcast thread
        log.debug("Get message {} adding it to inner queue", message);
        messagesBlockingQueue.offer(message);
    }
}
