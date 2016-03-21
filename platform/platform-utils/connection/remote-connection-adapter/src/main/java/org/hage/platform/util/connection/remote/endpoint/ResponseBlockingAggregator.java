package org.hage.platform.util.connection.remote.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.cluster.NodeAddress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@RequiredArgsConstructor
class ResponseBlockingAggregator<M extends Serializable, R> implements Callable<R> {

    private final MessageAggregator<M, R> messagesAggregator;
    private final Set<NodeAddress> expectedMessageSenders;

    private final BlockingQueue<MessageEnvelope<M>> messagesBlockingQueue = new LinkedBlockingQueue<>();
    private final List<MessageEnvelope<M>> collectedMessages = new ArrayList<>();

    @Override
    public R call() throws Exception {
        log.debug("Started aggregating messages");

        while (!expectedMessageSenders.isEmpty()) {
            log.debug("Not all messages has been received yet. Waiting for new message...");
            MessageEnvelope<M> message = messagesBlockingQueue.take();
            log.debug("Got message {}", message);

            expectedMessageSenders.remove(message.getOrigin());
            collectedMessages.add(message);
        }

        log.debug("Got all messages, aggregating...");
        R result = messagesAggregator.aggregate(collectedMessages);
        log.debug("Finished aggregating messages into '{}'", result);

        return result;
    }


    public void addMessage(MessageEnvelope<M> message) {
        log.debug("Get message {} adding it to inner queue", message);
        messagesBlockingQueue.offer(message);
    }

}
