package org.hage.platform.communication.synch;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.communication.address.node.NodeAddress;
import org.hage.platform.communication.message.service.ServiceMessage;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

@NotThreadSafe
@Slf4j
public abstract class ConversationMessagesAggregator<ResultT, RemoteMessageT extends ServiceMessage> {

    private final Set<NodeAddress> expectedMessageSenders;
    private final List<RemoteMessageT> collectedMessages = new ArrayList<>();

    public ConversationMessagesAggregator(NodeAddress... expectedMessageSenders) {
        this(Stream.of(expectedMessageSenders).collect(toSet()));
    }

    public ConversationMessagesAggregator(Collection<NodeAddress> nodeAddresses) {
        expectedMessageSenders = new HashSet<>(nodeAddresses);
    }

    public final boolean hasAllMessages() {
        log.debug("Waiting for response from {}", expectedMessageSenders);
        return expectedMessageSenders.isEmpty();
    }

    public final void appendMessage(RemoteMessageT message) {
        log.debug("Appending message {}", message);
        expectedMessageSenders.remove(message.getHeader().getSender());
        collectedMessages.add(message);
    }

    public final Optional<ResultT> getAggregatedResult() {
        log.debug("Getting aggregated result from conversation");
        if (!hasAllMessages()) {
            log.warn("Try to finalize while still waiting for responses from {}", expectedMessageSenders);
            return empty();
        }
        return ofNullable(aggregate(collectedMessages));
    }

    protected abstract ResultT aggregate(Collection<RemoteMessageT> messages);
}
