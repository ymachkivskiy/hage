package org.jage.communication.common;


import org.jage.communication.message.ServiceMessage;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

public abstract class RemoteMessageConsumer<RemoteMessageT extends ServiceMessage>
        implements Predicate<RemoteMessageT>, Consumer<RemoteMessageT> {

    private Optional<Set<Long>> trackedConversationIds;

    protected RemoteMessageConsumer() {
        this(null);
    }

    protected RemoteMessageConsumer(Set<Long> trackedConversationIds) {
        this.trackedConversationIds = ofNullable(trackedConversationIds);
    }

    @Override
    public final boolean test(RemoteMessageT remoteMessage) {
        return ofNullable(remoteMessage.getHeader().getConversationId())
                .map(this::isTrackingConversation)
                .orElse(true)
                &&
                messageMatch(remoteMessage);
    }

    private boolean isTrackingConversation(Long conversationId) {
        return trackedConversationIds
                .map(conversations -> conversations.contains(conversationId))
                .orElse(true);
    }

    protected boolean messageMatch(RemoteMessageT remoteMessage) {
        return true;
    }
}
