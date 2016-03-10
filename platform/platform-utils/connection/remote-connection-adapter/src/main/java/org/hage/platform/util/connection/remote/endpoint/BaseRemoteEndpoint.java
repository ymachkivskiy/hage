package org.hage.platform.util.connection.remote.endpoint;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.communication.address.NodeAddress;
import org.hage.platform.util.connection.ClusterAddressManager;
import org.hage.platform.util.connection.ConversationIdProvider;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.chanel.FrameSender;
import org.hage.platform.util.executors.WorkerExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import static java.util.Collections.singleton;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;
import static org.hage.platform.util.connection.frame.util.FrameCreators.*;
import static org.hage.util.concurrency.Utils.getWithBlocking;

@Slf4j
@RequiredArgsConstructor(access = PROTECTED)
public abstract class BaseRemoteEndpoint<M extends Serializable> {

    @Getter
    private final ConnectionDescriptor descriptor;
    @Getter(PACKAGE)
    private final Class<M> messageClazz;

    private final Map<Long, ResponseBlockingAggregator<M, ?>> aggregatorsForConversation = new ConcurrentHashMap<>();

    @Setter(PACKAGE)
    private FrameSender frameSender;

    @Autowired
    private WorkerExecutor workerExecutor;
    @Autowired
    private ConversationIdProvider conversationIdProvider;
    @Autowired
    private ClusterAddressManager addressManager;

    protected final void sendToAll(M message) {
        log.debug("Send message '{}' to all", message);

        frameSender.send(broadcastFrameWithDataNotRequireResponse(message));
    }

    protected final void sendTo(M message, Set<NodeAddress> addresses) {
        log.debug("Send message '{}' to nodes '{}'", message, addresses);

        frameSender.send(multicastFrameWithDataNotRequireResponse(message, addresses));
    }

    protected final void sendTo(M message, NodeAddress address) {
        sendTo(message, singleton(address));
    }

    protected final <R> R sendToAllAndAggregateResponse(M message, MessageAggregator<M, R> aggregator) {
        log.debug("Send message '{}' to all and aggregate response", message);
        return sendToAndAggregateResponses(message, aggregator, addressManager.getAllAddresses());
    }

    protected final <R> R sendToAndAggregateResponses(M message, MessageAggregator<M, R> aggregator, Set<NodeAddress> addresses) {
        log.debug("Send message '{}' to '{}' and aggregate response", message, addresses);

        Long conversationId = conversationIdProvider.nextConversationId();
        ResponseBlockingAggregator<M, R> blockingAggregator = new ResponseBlockingAggregator<>(aggregator, addresses);
        aggregatorsForConversation.put(conversationId, blockingAggregator);
        Future<R> responseFuture = workerExecutor.submit(blockingAggregator);

        frameSender.send(multicastFrameRequest(message, addresses, conversationId));

        R response = getWithBlocking(responseFuture);
        aggregatorsForConversation.remove(conversationId);

        return response;
    }

    final void consumeResponseMessageForConversation(MessageEnvelope<M> messageEnvelope, Long conversationId) {
        log.debug("Consume remote message '{}' insinde conversation '{}'", messageEnvelope, conversationId);

        ResponseBlockingAggregator<M, ?> responseBlockingAggregator = aggregatorsForConversation.get(conversationId);
        if (responseBlockingAggregator != null) {
            responseBlockingAggregator.addMessage(messageEnvelope);
        } else {
            log.warn("Got remote message '{}' with conversation '{}', but response aggregator is absent", messageEnvelope, conversationId);
        }
    }


    protected M consumeMessageAndRespond(MessageEnvelope<M> envelope) {
        log.error("Executed not implemented consume message '{}' and respond method", envelope);
        throw new UnsupportedOperationException("consumeMessageAndRespond is not supported for " + getClass().getName());
    }

    protected void consumeMessage(MessageEnvelope<M> envelope) {
        log.warn("Consumer for endpoint '{}' messages not defined. Got message '{}', but did not process it", getClass().getName(), envelope);
    }
}
