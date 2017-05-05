package org.hage.platform.cluster.connection.remote.endpoint;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.api.ClusterAddressManager;
import org.hage.platform.cluster.api.ConversationIdProvider;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.cluster.connection.chanel.ConnectionDescriptor;
import org.hage.platform.cluster.connection.chanel.FrameSender;
import org.hage.platform.cluster.connection.frame.Frame;
import org.hage.platform.cluster.connection.frame.process.DiagnosticsProcessor;
import org.hage.platform.cluster.connection.frame.process.ResponsivenessProcessor;
import org.hage.platform.cluster.connection.frame.util.FrameUtil;
import org.hage.platform.util.executors.simple.WorkerExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;
import static org.hage.platform.cluster.connection.frame.process.ConversationIdProcessor.withConversation;
import static org.hage.platform.cluster.connection.frame.process.ConversationIdProcessor.withoutConversation;
import static org.hage.platform.cluster.connection.frame.process.IncludeSenderProcessor.includingSender;
import static org.hage.platform.cluster.connection.frame.process.PayloadDataProcessor.withData;
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

        frameSender.send(
            FrameUtil.createFrame(
                broadcast(),
                includingSender(),
                ResponsivenessProcessor.requiresNoResponse(),
                withData(message),
                withoutConversation(),
                DiagnosticsProcessor.successful()
            ));
    }

    protected final void sendTo(M message, Set<NodeAddress> addresses) {
        log.debug("Send message '{}' to nodes '{}'", message, addresses);

        frameSender.send(
            FrameUtil.createFrame(
                multicast(addresses),
                includingSender(),
                ResponsivenessProcessor.requiresNoResponse(),
                withData(message),
                withoutConversation(),
                DiagnosticsProcessor.successful()
            ));
    }

    protected final void sendTo(M message, NodeAddress address) {
        log.debug("Send message '{}' to node '{}'", message, address);
        frameSender.send(
            FrameUtil.createFrame(
                unicast(address),
                includingSender(),
                ResponsivenessProcessor.requiresNoResponse(),
                withData(message),
                withoutConversation(),
                DiagnosticsProcessor.successful()
            )
        );
    }

    protected final <R> R sendToAllAndAggregateResponse(M message, MessageAggregator<M, R> aggregator) {
        log.debug("Send broadcast message '{}' to all and aggregate response", message);

        return sendAndAggregate(
            FrameUtil.createFrame(
                broadcast(),
                includingSender(),
                ResponsivenessProcessor.requiresResponse(),
                withData(message),
                DiagnosticsProcessor.successful()
            ),
            aggregator
        );
    }

    protected final <R> R sendToAndAggregateResponses(M message, MessageAggregator<M, R> aggregator, Set<NodeAddress> addresses) {
        log.debug("Send multicast message '{}' to '{}' and aggregate response", message, addresses);

        return sendAndAggregate(
            FrameUtil.createFrame(
                multicast(addresses),
                includingSender(),
                ResponsivenessProcessor.requiresResponse(),
                withData(message),
                DiagnosticsProcessor.successful()
            ),
            aggregator
        );
    }

    protected final M sendToAndWaitForResponse(M message, NodeAddress address) {
        log.debug("Send unicast message '{}' to '{}' and aggregate response", message, address);

        return sendAndAggregate(
            FrameUtil.createFrame(
                unicast(address),
                includingSender(),
                ResponsivenessProcessor.requiresResponse(),
                withData(message),
                DiagnosticsProcessor.successful()
            ),
            l -> l.get(0).getBody()
        );
    }

    final void consumeResponseMessageForConversation(MessageEnvelope<M> messageEnvelope, Long conversationId) {
        log.debug("Consume remote message '{}' inside conversation '{}'", messageEnvelope, conversationId);

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

    private <R> R sendAndAggregate(Frame frame, MessageAggregator<M, R> aggregator) {

        Long conversationId = conversationIdProvider.nextConversationId();

        Set<NodeAddress> addressesForAggregation = FrameUtil.isBroadcastFrame(frame)
            ? addressManager.getAllAddresses()
            : FrameUtil.getReceiverAdresses(frame);

        ResponseBlockingAggregator<M, R> blockingAggregator = new ResponseBlockingAggregator<>(aggregator, addressesForAggregation);

        Future<R> responseFuture = workerExecutor.submit(blockingAggregator);

        aggregatorsForConversation.put(conversationId, blockingAggregator);

        frameSender.send(withConversation(conversationId).process(frame));

        R result = getWithBlocking(responseFuture);

        aggregatorsForConversation.remove(conversationId);

        return result;
    }
}
